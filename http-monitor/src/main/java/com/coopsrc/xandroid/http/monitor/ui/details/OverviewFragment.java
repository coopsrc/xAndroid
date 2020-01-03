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

package com.coopsrc.xandroid.http.monitor.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.coopsrc.xandroid.http.monitor.R;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.http.monitor.utils.MonitorUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class OverviewFragment extends BaseFragment {
    private TextView tv_url;
    private TextView tv_method;
    private TextView tv_protocol;
    private TextView tv_response;
    private TextView tv_ssl;
    private TextView tv_requestTime;
    private TextView tv_responseTime;
    private TextView tv_duration;
    private TextView tv_requestSize;
    private TextView tv_responseSize;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    public OverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_url = view.findViewById(R.id.tv_url);
        tv_method = view.findViewById(R.id.tv_method);
        tv_protocol = view.findViewById(R.id.tv_protocol);
        tv_response = view.findViewById(R.id.tv_response);
        tv_ssl = view.findViewById(R.id.tv_https);
        tv_requestTime = view.findViewById(R.id.tv_request_time);
        tv_responseTime = view.findViewById(R.id.tv_response_time);
        tv_duration = view.findViewById(R.id.tv_duration);
        tv_requestSize = view.findViewById(R.id.tv_request_size);
        tv_responseSize = view.findViewById(R.id.tv_response_size);

        monitorViewModel.getHttpInfoLiveData().observe(this, new Observer<HttpInfo>() {
            @Override
            public void onChanged(HttpInfo httpInfo) {
                if (httpInfo != null) {
                    tv_url.setText(httpInfo.getRequestInfo().getUrl().toString());
                    tv_method.setText(httpInfo.requestInfo.getMethod());
                    tv_protocol.setText(httpInfo.responseInfo.getProtocol().toString());
                    tv_response.setText(String.valueOf(httpInfo.responseInfo.getCode()));
                    tv_ssl.setText(("https".equalsIgnoreCase(httpInfo.requestInfo.getUrl().scheme()) ? "Yes" : "No"));
                    tv_requestTime.setText(MonitorUtils.getDateFormatLong(httpInfo.requestInfo.getDate()));
                    tv_responseTime.setText(MonitorUtils.getDateFormatLong(httpInfo.responseInfo.getDate()));
                    tv_duration.setText(String.format("%s ms", httpInfo.responseInfo.getDuration()));
                    tv_requestSize.setText(MonitorUtils.formatBytes(httpInfo.requestInfo.getContentLength()));
                    tv_responseSize.setText(MonitorUtils.formatBytes(httpInfo.responseInfo.getContentLength()));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        monitorViewModel.getById(id);
    }
}
