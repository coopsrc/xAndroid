package com.coopsrc.xandroid.downloader.helper

import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.DownloaderUtils
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2018-08-20
 * Time: 17:38
 */
class WorkQueue<T>(
    private val period: Long,
    private val unit: TimeUnit = TimeUnit.SECONDS,
    private val action: (data: T) -> Unit
) {

    private val tag = "WorkQueue"

    private val queue: Queue<T> = LinkedList<T>()

    private val actionProcessor: FlowableProcessor<T> = BehaviorProcessor.create<T>().toSerialized()
    private lateinit var actionFlowable: Flowable<T>
    private var actionDisposable: Disposable? = null

    private var isComplete: Boolean = false

    init {
        Maybe.create<Any> {
            createFlowable()
        }.subscribeOn(Schedulers.newThread()).doOnError {
            Logger.e(tag, "init error: $it")
        }.subscribe()
    }

    private fun createFlowable() {
        actionFlowable = Flowable.create<Any>({ emitter ->
            while (!isComplete || !queue.isEmpty()) {
                emitter.onNext(System.currentTimeMillis())
            }
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER).sample(period, unit).doOnSubscribe {
            Logger.i(tag, "doOnSubscribe: WorkQueue Created")
        }.subscribeOn(Schedulers.io()).doOnNext {
            Logger.i(tag, "doOnNext: $it")
        }.flatMap {
            doAction(poll())
        }.doOnNext {
            actionProcessor.onNext(it)
        }.doOnError {
            actionProcessor.onError(it)
        }.doOnComplete {
            actionProcessor.onComplete()
        }.doFinally {
            actionDisposable = null
        }
    }

    fun getFlowable(): Flowable<T> {
        return actionProcessor
    }

    fun offer(element: T) {
        queue.offer(element)
    }

    private fun poll(): T {
        return queue.poll()
    }

    private fun doAction(element: T): Flowable<out T> {
        Logger.i(tag, "doAction: $element")

        action(element)

        return Flowable.just(element)
    }

    fun start(): Maybe<Any> {
        return Maybe.create<Any> {
            subscribe()
            it.onSuccess(Constants.any)
        }.subscribeOn(Schedulers.newThread())
    }

    private fun subscribe() {
        if (actionDisposable == null) {
            actionDisposable = actionFlowable.subscribe()
        }
    }

    fun stop(): Maybe<Any> {
        return Maybe.create<Any> {
            unSubscribe()
            it.onSuccess(Constants.any)
        }.subscribeOn(Schedulers.newThread())
    }

    private fun unSubscribe() {
        if (actionDisposable != null) {
            DownloaderUtils.dispose(actionDisposable)
            actionDisposable = null
        }
    }

    fun setComplete() {
        isComplete = true
    }
}