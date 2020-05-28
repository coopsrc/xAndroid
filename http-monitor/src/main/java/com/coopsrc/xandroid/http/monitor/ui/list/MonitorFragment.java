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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coopsrc.xandroid.http.monitor.R;
import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModel;
import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModelFactory;
import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.http.monitor.ui.details.DetailsActivity;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MonitorFragment extends Fragment implements MonitorAdapter.OnItemClickListener {
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
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        HttpMonitorDatabase database = HttpMonitorDatabase.getInstance(getContext());

        monitorViewModel = MonitorViewModelFactory.getInstance(database).create(MonitorViewModel.class);
        monitorViewModel.getListLiveData().observe(getViewLifecycleOwner(), new Observer<List<HttpInfo>>() {
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

    @Override
    public void onItemClicked(View view, int position, HttpInfo httpInfo) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("id", httpInfo.id);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongPress(View view, int position, HttpInfo httpInfo) {

//        monitorViewModel.delete(httpInfo);

        return false;
    }
}
