package com.coopsrc.xandroid.http.monitor.ui.details;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModel;
import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModelFactory;
import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-24 14:25
 */
public class BaseFragment extends Fragment {
    protected MonitorViewModel monitorViewModel;
    protected long id = 0;

    private HttpMonitorDatabase database = HttpMonitorDatabase.getInstance(getContext());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        monitorViewModel = MonitorViewModelFactory.getInstance(database).create(MonitorViewModel .class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        id = getActivity().getIntent().getLongExtra("id", 0);
    }
}
