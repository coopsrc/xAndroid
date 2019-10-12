package com.coopsrc.xandroid.http.monitor.ui.details;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.coopsrc.xandroid.http.monitor.R;
import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModel;
import com.coopsrc.xandroid.http.monitor.arch.MonitorViewModelFactory;
import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.google.android.material.tabs.TabLayout;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";

    Toolbar toolbar;

    private MonitorViewModel monitorViewModel;
    private long id = 0;

    private OverviewFragment overviewFragment = OverviewFragment.newInstance();
    private DetailsFragment requestFragment = DetailsFragment.newInstance(DetailsFragment.MODE_REQUEST);
    private DetailsFragment responseFragment = DetailsFragment.newInstance(DetailsFragment.MODE_RESPONSE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
        HttpMonitorDatabase database = HttpMonitorDatabase.getInstance(this);

        monitorViewModel = MonitorViewModelFactory.getInstance(database).create(MonitorViewModel.class);
        monitorViewModel.getHttpInfoLiveData().observe(this, new Observer<HttpInfo>() {
            @Override
            public void onChanged(HttpInfo httpInfo) {
                Log.i(TAG, "onChanged: " + httpInfo);
                if (httpInfo != null) {
                    toolbar.setTitle(String.format("%s %s", httpInfo.requestInfo.getMethod(), httpInfo.requestInfo.getUrl().encodedPath()));
                } else {
                    toolbar.setTitle("");
                }
            }
        });
        id = getIntent().getLongExtra("id", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        monitorViewModel.getById(id);
        Log.i(TAG, "onResume: " + id);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        }

        ViewPager viewPager = findViewById(R.id.viewPager);
        PagerAdapter fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentPagerAdapter.addFragment(overviewFragment, "overview");
        fragmentPagerAdapter.addFragment(requestFragment, "request");
        fragmentPagerAdapter.addFragment(responseFragment, "response");
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
