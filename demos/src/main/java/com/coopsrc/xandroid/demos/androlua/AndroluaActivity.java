package com.coopsrc.xandroid.demos.androlua;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.coopsrc.xandroid.androlua.Androlua;
import com.coopsrc.xandroid.demos.databinding.ActivityAndroluaBinding;

public class AndroluaActivity extends AppCompatActivity {

    private ActivityAndroluaBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAndroluaBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Androlua.getLuaCopyright());
        stringBuilder.append("\r\n\r\n");
        stringBuilder.append(Androlua.getLuaAuthors());

        mBinding.textViewInfo.setText(stringBuilder.toString());
    }
}
