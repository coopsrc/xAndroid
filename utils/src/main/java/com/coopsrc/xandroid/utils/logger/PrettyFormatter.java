/*
 * Copyright (C) 2019 Zhang Tingkuo(zhangtingkuo@gmail.com)
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
 */

package com.coopsrc.xandroid.utils.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-01-08 17:43
 */
public final class PrettyFormatter extends Formatter {

    // tabs
    private static final char LEFT_TOP_CORNER = '┌';
    private static final char RIGHT_TOP_CORNER = '┐';
    private static final char LEFT_BOTTOM_CORNER = '└';
    private static final char RIGHT_BOTTOM_CORNER = '┘';
    private static final char LEFT_MIDDLE_CORNER = '├';
    private static final char RIGHT_MIDDLE_CORNER = '┤';
    private static final char TOP_MIDDLE_CORNER = '┬';
    private static final char BOTTOM_MIDDLE_CORNER = '┴';
    private static final char CENTER = '┼';
    private static final char HORIZONTAL_LINE = '│';
    private static final char VERTICAL_LINE = '─';
    private static final char VERTICAL_DOTTED_LINE = '╌';
    private static final String HORIZONTAL_DIVIDER = "────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final String HORIZONTAL_DOTTED_DIVIDER = "╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌";

    private static final String TOP_BORDER = String.format("%s%s%s", LEFT_TOP_CORNER, HORIZONTAL_DIVIDER, RIGHT_TOP_CORNER);
    private static final String MIDDLE_BORDER = String.format("%s%s%s", LEFT_MIDDLE_CORNER, HORIZONTAL_DOTTED_DIVIDER, RIGHT_MIDDLE_CORNER);
    private static final String BOTTOM_BORDER = String.format("%s%s%s", LEFT_BOTTOM_CORNER, HORIZONTAL_DIVIDER, RIGHT_BOTTOM_CORNER);

    private static final int CONTENT_SIZE = 134;
    private static final int CONTENT_LENGTH = CONTENT_SIZE - 6;


    public PrettyFormatter() {
        super();
    }

    public PrettyFormatter(IPrinter printer) {
        super(printer);
    }

    @Override
    public void println(int priority, @Nullable String tag, @NonNull String message) {
        printTopBorder(priority, tag);
        printBody(priority, tag, message);
        printBottomBorder(priority, tag);
    }

    private void printTopBorder(int priority, @Nullable String tag) {
        mPrinter.println(priority, tag, TOP_BORDER);
    }

    private void printMiddleBorder(int priority, @Nullable String tag) {
        mPrinter.println(priority, tag, MIDDLE_BORDER);
    }

    private void printBottomBorder(int priority, @Nullable String tag) {
        mPrinter.println(priority, tag, BOTTOM_BORDER);
    }

    private void printBody(int priority, @Nullable String tag, @NonNull String message) {
        message = message.replaceAll("\t", "    ");
        if (message.length() <= CONTENT_LENGTH) {
            printContentLine(priority, tag, message);
            return;
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + CONTENT_LENGTH);
                String part = message.substring(i, end);
                printContentLine(priority, tag, part);
                i = end;
            } while (i < newline);

            if (newline != -1 && message.indexOf('\n', 0) == i) {
                printMiddleBorder(priority, tag);
            }
        }
    }

    private void printContentLine(int priority, @Nullable String tag, @NonNull String message) {
        String format = format(message);
        mPrinter.println(priority, tag, String.format(format, HORIZONTAL_LINE, message, HORIZONTAL_LINE));
    }

    public String format(String message) {
        int asciiCount = 0;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c >= 32 && c <= 127) {
                asciiCount++;
            }
        }
        int format = CONTENT_LENGTH - message.length() + asciiCount;
        return "%s  %-" + format + "s  %s";
    }
}
