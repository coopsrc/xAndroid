package com.coopsrc.xandroid.dewdrops.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.coopsrc.xandroid.dewdrops.DewdropsBlur;
import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.annotation.Scheme;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;
import com.coopsrc.xandroid.dewdrops.widget.DragBlurringView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewInfo;
    private RadioGroup radioGroupScheme;
    private RadioGroup radioGroupAlgorithm;
    private SeekBar seekBarRadius;
    private SeekBar seekBarSampleFactor;

    private int scheme = BlurConfig.SCHEME_NATIVE;
    private int mode = BlurConfig.MODE_GAUSSIAN;
    private int radius = BlurConfig.DEFAULT_RADIUS;
    private float sampleFactor = BlurConfig.DEFAULT_SAMPLE_FACTOR;

    private ImageView imageView;
    private DragBlurringView blurringView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initConfigInfo();
    }

    private void initView() {
        textViewInfo = findViewById(R.id.textViewInfo);
        radioGroupScheme = findViewById(R.id.radioGroupScheme);
        radioGroupAlgorithm = findViewById(R.id.radioGroupAlgorithm);
        seekBarRadius = findViewById(R.id.seekBarRadius);
        seekBarSampleFactor = findViewById(R.id.seekBarSampleFactor);

        imageView = findViewById(R.id.imageView);
        blurringView = findViewById(R.id.blurringView);
        blurringView.setBlurredView(imageView);

        radioGroupScheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateSchemeChecked(checkedId);
                updateConfigInfo();
            }
        });
        radioGroupAlgorithm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateAlgorithmChecked(checkedId);
                updateConfigInfo();
            }
        });
        seekBarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRadius(progress);
                updateConfigInfo();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSampleFactor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSampleFactor(progress);
                updateConfigInfo();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initConfigInfo() {
        if (radioGroupScheme != null) {
            updateSchemeChecked(radioGroupScheme.getCheckedRadioButtonId());
        }
        if (radioGroupAlgorithm != null) {
            updateSchemeChecked(radioGroupAlgorithm.getCheckedRadioButtonId());
        }
        if (seekBarRadius != null) {
            updateRadius(seekBarRadius.getProgress());
        }
        if (seekBarSampleFactor != null) {
            updateRadius(seekBarSampleFactor.getProgress());
        }
        updateConfigInfo();
    }

    private void updateSchemeChecked(int checkedId) {
        switch (checkedId) {
            case R.id.radioSchemeJava:
                scheme = BlurConfig.SCHEME_JAVA;
                break;
            case R.id.radioSchemeNative:
                scheme = BlurConfig.SCHEME_NATIVE;
                break;
            case R.id.radioSchemeRender:
                scheme = BlurConfig.SCHEME_RENDER_SCRIPT;
                break;
            case R.id.radioSchemeOpenGL:
                scheme = BlurConfig.SCHEME_OPEN_GL;
                break;
        }
    }

    private void updateAlgorithmChecked(int checkedId) {
        switch (checkedId) {
            case R.id.radioAlgorithmGaussian:
                mode = BlurConfig.MODE_GAUSSIAN;
                break;
            case R.id.radioAlgorithmStack:
                mode = BlurConfig.MODE_STACK;
                break;
            case R.id.radioAlgorithmBox:
                mode = BlurConfig.MODE_BOX;
                break;
        }
    }

    private void updateRadius(int progress) {
        radius = progress;
    }

    private void updateSampleFactor(int progress) {
        sampleFactor = progress;
    }

    private void updateConfigInfo() {
        String info = String.format("Scheme: %s, Mode: %s, Radius: %s, Sample: %s", getSchemeText(scheme), getModeText(mode), radius, sampleFactor);
        textViewInfo.setText(info);

        updateBlurProcessor();
    }

    private String getSchemeText(@Scheme int scheme) {
        if (radioGroupScheme != null) {
            return (String) ((RadioButton) radioGroupScheme.findViewById(radioGroupScheme.getCheckedRadioButtonId())).getText();
        }
        return String.valueOf(scheme);
    }

    private String getModeText(@Mode int mode) {
        if (radioGroupAlgorithm != null) {
            return (String) ((RadioButton) radioGroupAlgorithm.findViewById(radioGroupAlgorithm.getCheckedRadioButtonId())).getText();
        }
        return String.valueOf(mode);
    }

    private void updateBlurProcessor(){

        if (blurringView!=null){
            blurringView.setBlurProcessor(DewdropsBlur.with(this)
                    .scheme(scheme)
                    .mode(mode)
                    .radius(radius)
                    .sampleFactor(sampleFactor)
                    .build());
        }
    }
}
