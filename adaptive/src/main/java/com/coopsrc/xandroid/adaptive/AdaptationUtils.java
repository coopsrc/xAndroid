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

package com.coopsrc.xandroid.adaptive;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-08-29 10:58
 */
public class AdaptationUtils {

    public static int getStatusBarHeight(Context context) {
        int height = 0;

        Resources resources = context.getResources();

        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }

        return height;
    }

    public static int getNavBarHeight(Context context) {
        int height = 0;

        Resources resources = context.getResources();

        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }

        return height;
    }
}
