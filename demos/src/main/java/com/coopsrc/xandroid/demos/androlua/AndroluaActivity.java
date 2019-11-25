package com.coopsrc.xandroid.demos.androlua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.coopsrc.xandroid.androlua.Androlua;
import com.coopsrc.xandroid.demos.R;

public class AndroluaActivity extends AppCompatActivity {

    private TextView mTextViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androlua);

        mTextViewInfo = findViewById(R.id.text_view_info);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Androlua.getLuaCopyright());
        stringBuilder.append("\r\n\r\n");
        stringBuilder.append(Androlua.getLuaAuthors());

        mTextViewInfo.setText(stringBuilder.toString());
    }
}
