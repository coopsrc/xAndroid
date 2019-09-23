package com.coopsrc.xandroid.http.monitor.ui.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coopsrc.xandroid.http.monitor.R;
import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModel;
import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModelFactory;
import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MonitorFragment extends Fragment {
    private static final String TAG = "MonitorFragment";

    private RecyclerView recyclerView;

    private MonitorViewModel monitorViewModel;

    public MonitorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final MonitorAdapter adapter = new MonitorAdapter(getContext());
        recyclerView.setAdapter(adapter);

        HttpMonitorDatabase database = HttpMonitorDatabase.getInstance(getContext());

        monitorViewModel = MonitorViewModelFactory.getInstance(database).create(MonitorViewModel.class);
        monitorViewModel.getListLiveData().observe(this, new Observer<List<HttpInfo>>() {
            @Override
            public void onChanged(List<HttpInfo> httpInfos) {
                Log.i(TAG, "onChanged: " + httpInfos);
                adapter.setData(httpInfos);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        monitorViewModel.fetch(100);
    }
}
