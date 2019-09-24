package com.coopsrc.xandroid.http.monitor.ui.list;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

import com.coopsrc.xandroid.http.monitor.R;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.http.monitor.ui.details.DetailsActivity;
import com.coopsrc.xandroid.http.monitor.utils.MonitorUtils;

import java.util.List;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 20:05
 */
public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {
    private static final String TAG = "MonitorAdapter";

    private final int color200;
    private final int color300;
    private final int color400;
    private final int color500;
    private final int colorError;

    private final AsyncListDiffer<HttpInfo> httpInfoAsyncListDiffer;

    private Context context;

    public MonitorAdapter(Context context) {
        this.context = context;
        httpInfoAsyncListDiffer = new AsyncListDiffer<>(this, new HttpInfoDiffCallback());
        color200 = ContextCompat.getColor(context, R.color.monitor_status_200);
        color300 = ContextCompat.getColor(context, R.color.monitor_status_300);
        color400 = ContextCompat.getColor(context, R.color.monitor_status_400);
        color500 = ContextCompat.getColor(context, R.color.monitor_status_500);
        colorError = ContextCompat.getColor(context, R.color.monitor_status_error);
    }

    @NonNull
    @Override
    public MonitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_monitor, parent, false);
        return new MonitorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitorViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: " + position);
        final HttpInfo httpInfo = httpInfoAsyncListDiffer.getCurrentList().get(position);
        holder.textViewId.setText(String.valueOf(httpInfo.getId()));
        holder.textViewPath.setText(String.format("%s  %s", httpInfo.requestInfo.getMethod(), httpInfo.requestInfo.getUrl().encodedPath()));
        holder.textViewHost.setText(httpInfo.requestInfo.getUrl().host());
        holder.textViewDate.setText(MonitorUtils.getDateFormatShort(httpInfo.responseInfo.getDate()));
        if ("https".equalsIgnoreCase(httpInfo.requestInfo.getUrl().scheme())) {
            holder.imageIconHttps.setImageResource(R.drawable.ic_privacy);
        } else {
            holder.imageIconHttps.setImageResource(R.drawable.ic_privacy_greay);
        }

        holder.textViewCode.setText(String.valueOf(httpInfo.responseInfo.getCode()));
        holder.textViewDuration.setText(String.format("%s ms", httpInfo.responseInfo.getDuration()));
        holder.textViewSize.setText(MonitorUtils.formatBytes(httpInfo.responseInfo.getContentLength()));
        setStatusColor(holder, httpInfo);
    }

    private void setStatusColor(MonitorViewHolder holder, HttpInfo httpInfo) {
        int color;
        if (!TextUtils.isEmpty(httpInfo.responseInfo.getError())) {
            color = colorError;
        } else if (httpInfo.responseInfo.getCode() >= 500) {
            color = color500;
        } else if (httpInfo.responseInfo.getCode() >= 400) {
            color = color400;
        } else if (httpInfo.responseInfo.getCode() >= 300) {
            color = color300;
        } else {
            color = color200;
        }
        holder.textViewCode.setTextColor(color);
        holder.textViewPath.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return httpInfoAsyncListDiffer.getCurrentList().size();
    }

    public void setData(List<HttpInfo> httpInfoList) {
        httpInfoAsyncListDiffer.submitList(httpInfoList);
    }

    class MonitorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView textViewId;
        public final TextView textViewCode;
        public final TextView textViewPath;
        public final TextView textViewHost;
        public final ImageView imageIconHttps;
        public final TextView textViewDate;
        public final TextView textViewDuration;
        public final TextView textViewSize;

        public MonitorViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.tv_id);
            textViewCode = itemView.findViewById(R.id.tv_code);
            textViewPath = itemView.findViewById(R.id.tv_path);
            textViewHost = itemView.findViewById(R.id.tv_host);
            imageIconHttps = itemView.findViewById(R.id.iv_ssl);
            textViewDate = itemView.findViewById(R.id.tv_requestDate);
            textViewDuration = itemView.findViewById(R.id.tv_duration);
            textViewSize = itemView.findViewById(R.id.tv_size);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            HttpInfo httpInfo = httpInfoAsyncListDiffer.getCurrentList().get(getAdapterPosition());

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("id", httpInfo.id);
            context.startActivity(intent);
        }
    }
}
