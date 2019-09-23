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
