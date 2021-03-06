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
    private static final String HORIZONTAL_DIVIDER = "──────────────────────────────────────────────────────────────────────────────────────────";
    private static final String HORIZONTAL_DOTTED_DIVIDER = "╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌";

    private static final String TOP_BORDER = String.format("%s%s%s", LEFT_TOP_CORNER, HORIZONTAL_DIVIDER, RIGHT_TOP_CORNER);
    private static final String MIDDLE_BORDER = String.format("%s%s%s", LEFT_MIDDLE_CORNER, HORIZONTAL_DOTTED_DIVIDER, RIGHT_MIDDLE_CORNER);
    private static final String BOTTOM_BORDER = String.format("%s%s%s", LEFT_BOTTOM_CORNER, HORIZONTAL_DIVIDER, RIGHT_BOTTOM_CORNER);

    private static final int CONTENT_LENGTH = (TOP_BORDER.length() - 4) * 2;

    public PrettyFormatter() {
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
        printer.println(priority, tag, TOP_BORDER);
    }

    private void printMiddleBorder(int priority, @Nullable String tag) {
        printer.println(priority, tag, MIDDLE_BORDER);
    }

    private void printBottomBorder(int priority, @Nullable String tag) {
        printer.println(priority, tag, BOTTOM_BORDER);
    }

    private void printHeader() {

    }

    private void printBody(int priority, @Nullable String tag, @NonNull String message) {
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
        }
    }

    private void printContentLine(int priority, @Nullable String tag, @NonNull String message) {
        printer.println(priority, tag, String.format("%s  %s", HORIZONTAL_LINE, message));
    }
}
