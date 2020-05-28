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
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.coopsrc.xandroid.http.monitor.R;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.utils.LogUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends BaseFragment {

    public static final int MODE_REQUEST = 0;
    public static final int MODE_RESPONSE = 1;

    private int mode;


    private TextView tvHeaders;

    private TextView tvBody;

    public static DetailsFragment newInstance(int mode) {
        DetailsFragment fragment = new DetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        fragment.setArguments(bundle);

        return fragment;
    }

    public DetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mode = bundle.getInt("mode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvHeaders = view.findViewById(R.id.tvHeaders);
        tvBody = view.findViewById(R.id.tvBody);

        monitorViewModel.getHttpInfoLiveData().observe(getViewLifecycleOwner(), new Observer<HttpInfo>() {
            @Override
            public void onChanged(HttpInfo httpInfo) {
                LogUtils.w("onChanged: %s", httpInfo);
                if (mode == MODE_REQUEST) {
                    setText(httpInfo.getRequestHeadersString(true),
                            httpInfo.getFormattedRequestBody(),
                            true);
                } else {
                    setText(httpInfo.getResponseHeadersString(true),
                            httpInfo.getFormattedResponseBody(),
                            true);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        monitorViewModel.getById(id);
    }

    private void setText(String headersString, String bodyString, boolean isPlainText) {
        if (TextUtils.isEmpty(headersString)) {
            tvHeaders.setVisibility(View.GONE);
        } else {
            tvHeaders.setVisibility(View.VISIBLE);
            tvHeaders.setText(Html.fromHtml(headersString));
        }
        if (!isPlainText) {
            tvBody.setText("(encoded or binary body omitted)");
        } else {
            tvBody.setText(bodyString);
        }
    }
}
