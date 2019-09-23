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
import androidx.fragment.app.Fragment;
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

        monitorViewModel.getHttpInfoLiveData().observe(this, new Observer<HttpInfo>() {
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
