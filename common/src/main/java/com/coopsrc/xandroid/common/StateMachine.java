/*
 * Copyright (C) 2021 Zhang Tingkuo(zhangtingkuo@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.coopsrc.xandroid.common;

import com.coopsrc.xandroid.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2021-02-19 10:26
 */
public final class StateMachine {
    private static final String TAG = "StateMachine";

    public static final int STATUS_INITIALLY = 0;
    public static final int STATUS_INVOKED = 1;

    final ArrayList<State> mStates = new ArrayList<>();
    final ArrayList<State> mFinishedStates = new ArrayList<>();
    final ArrayList<State> mUnfinishedStates = new ArrayList<>();

    public StateMachine() {
    }

    public void addState(State state) {
        LogUtils.tag(TAG).i("addState: %s", state);
        if (!mStates.contains(state)) {
            mStates.add(state);
        }
    }

    public void addTransition(State fromState, State toState, Event event) {
        Transition transition = new Transition(fromState, toState, event);
        toState.addIncoming(transition);
        fromState.addOutgoing(transition);
    }

    public void addTransition(State fromState, State toState, Condition condition) {
        Transition transition = new Transition(fromState, toState, condition);
        toState.addIncoming(transition);
        fromState.addOutgoing(transition);
    }

    public void addTransition(State fromState, State toState) {
        Transition transition = new Transition(fromState, toState);
        toState.addIncoming(transition);
        fromState.addOutgoing(transition);
    }

    public void start() {
        LogUtils.tag(TAG).w("start: ");
        mUnfinishedStates.addAll(mStates);
        runUnfinishedStates();
    }

    void runUnfinishedStates() {
        boolean changed;
        do {
            changed = false;
            for (int i = mUnfinishedStates.size() - 1; i >= 0; i--) {
                State state = mUnfinishedStates.get(i);
                if (state.runIfNeeded()) {
                    mUnfinishedStates.remove(i);
                    mFinishedStates.add(state);
                    changed = true;
                }
            }
        } while (changed);
    }

    public void fireEvent(Event event) {
        LogUtils.tag(TAG).i("fireEvent: %s", event);
        for (State state : mFinishedStates) {
            if (state.mOutgoings != null) {
                if (!state.mBranchStart && state.mInvokedOutTransitions > 0) {
                    continue;
                }
                for (Transition transition : state.mOutgoings) {
                    if (transition.mStatus != STATUS_INVOKED && transition.mEvent == event) {
                        LogUtils.tag(TAG).d("signal: %s", transition);
                        transition.mStatus = STATUS_INVOKED;
                        state.mInvokedOutTransitions++;
                        if (!state.mBranchStart) {
                            break;
                        }
                    }
                }
            }
        }
        runUnfinishedStates();
    }

    public void reset() {
        LogUtils.tag(TAG).w("reset: ");
        mUnfinishedStates.clear();
        mFinishedStates.clear();

        for (State state : mStates) {
            state.mStatus = STATUS_INITIALLY;
            state.mInvokedOutTransitions = 0;
            if (state.mOutgoings != null) {
                for (Transition transition : state.mOutgoings) {
                    transition.mStatus = STATUS_INITIALLY;
                }
            }
        }
    }

    public static class Event {
        final String mName;

        public Event(String name) {
            mName = name;
        }
    }

    public static class Condition {
        final String mName;

        public Condition(String name) {
            mName = name;
        }

        public boolean canProceed() {
            return true;
        }
    }

    static class Transition {
        final State mFromState;
        final State mToState;
        final Event mEvent;
        final Condition mCondition;

        int mStatus = STATUS_INITIALLY;

        public Transition(State fromState, State toState) {
            mFromState = fromState;
            mToState = toState;
            mEvent = null;
            mCondition = null;
        }

        public Transition(State fromState, State toState, Event event) {

            if (event == null) {
                throw new IllegalArgumentException();
            }

            mFromState = fromState;
            mToState = toState;
            mEvent = event;
            mCondition = null;
        }

        public Transition(State fromState, State toState, Condition condition) {

            if (condition == null) {
                throw new IllegalArgumentException();
            }

            mFromState = fromState;
            mToState = toState;
            mEvent = null;
            mCondition = condition;
        }

        @NotNull
        @Override
        public String toString() {
            String signalName;
            if (mEvent != null) {
                signalName = mEvent.mName;
            } else if (mCondition != null) {
                signalName = mCondition.mName;
            } else {
                signalName = "auto";
            }
            return "[" + mFromState.mName + " -> " + mToState.mName + " <" + signalName + ">]";
        }
    }

    public static class State {
        final String mName;

        final boolean mBranchStart;
        final boolean mBranchEnd;

        int mStatus = STATUS_INITIALLY;

        int mInvokedOutTransitions = 0;

        ArrayList<Transition> mIncomings;
        ArrayList<Transition> mOutgoings;

        public State(String name) {
            this(name, false, true);
        }

        public State(String name, boolean branchStart, boolean branchEnd) {
            mName = name;
            mBranchStart = branchStart;
            mBranchEnd = branchEnd;
        }

        void addIncoming(Transition transition) {
            if (mIncomings == null) {
                mIncomings = new ArrayList<>();
            }
            mIncomings.add(transition);
        }

        void addOutgoing(Transition transition) {
            if (mOutgoings == null) {
                mOutgoings = new ArrayList<>();
            }

            mOutgoings.add(transition);
        }

        public void run() {

        }

        final boolean checkPreCondition() {
            if (mIncomings == null) {
                return true;
            }

            if (mBranchEnd) {
                for (Transition incoming : mIncomings) {
                    if (incoming.mStatus != STATUS_INVOKED) {
                        return false;
                    }
                }
                return true;
            } else {
                for (Transition incoming : mIncomings) {
                    if (incoming.mStatus == STATUS_INVOKED) {
                        return true;
                    }
                }
                return false;
            }
        }

        final boolean runIfNeeded() {
            if (mStatus != STATUS_INVOKED) {
                if (checkPreCondition()) {
                    LogUtils.tag(TAG).d("execute: %s", this);
                    mStatus = STATUS_INVOKED;
                    run();
                    signalAutoTransitionsAfterRun();
                    return true;
                }
            }

            return false;
        }

        final void signalAutoTransitionsAfterRun() {
            if (mOutgoings != null) {
                for (Transition outgoing : mOutgoings) {
                    if (outgoing.mEvent == null) {
                        if (outgoing.mCondition == null || outgoing.mCondition.canProceed()) {
                            LogUtils.tag(TAG).d("signal: %s", outgoing);
                            mInvokedOutTransitions++;
                            outgoing.mStatus = STATUS_INVOKED;
                            if (!mBranchStart) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        public int getStatus() {
            return mStatus;
        }

        @NotNull
        @Override
        public String toString() {
            return "[" + mName + " " + mStatus + "]";
        }
    }
}
