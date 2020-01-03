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

package com.coopsrc.xandroid.http.monitor.ui.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.coopsrc.xandroid.http.monitor.model.HttpInfo;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 20:08
 */
public class HttpInfoDiffCallback extends DiffUtil.ItemCallback<HttpInfo> {
    @Override
    public boolean areItemsTheSame(@NonNull HttpInfo oldItem, @NonNull HttpInfo newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull HttpInfo oldItem, @NonNull HttpInfo newItem) {
        return oldItem.equals(newItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull HttpInfo oldItem, @NonNull HttpInfo newItem) {
        return super.getChangePayload(oldItem, newItem);
    }
}
