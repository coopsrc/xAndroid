package com.coopsrc.xandroid.demos.blur;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.coopsrc.xandroid.demos.R;
import com.coopsrc.xandroid.demos.databinding.ActivityBlurBinding;
import com.coopsrc.xandroid.dewdrops.DewdropsBlur;
import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.annotation.Scheme;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;

public class BlurActivity extends AppCompatActivity {

//    private TextView textViewInfo;
//    private RadioGroup radioGroupScheme;
//    private RadioGroup radioGroupAlgorithm;
//    private SeekBar seekBarRadius;
//    private SeekBar seekBarSampleFactor;
//    private SeekBar seekBarBlurScale;

    private int scheme = BlurConfig.SCHEME_RENDER_SCRIPT;
    private int mode = BlurConfig.MODE_GAUSSIAN;
    private int radius = BlurConfig.DEFAULT_RADIUS;
    private float sampleFactor = BlurConfig.DEFAULT_SAMPLE_FACTOR;
    private int blurScale = BlurConfig.DEFAULT_BLUR_SCALE;
//
//    private ImageView imageView;
//    private DragBlurringView blurringView;

    private ActivityBlurBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBlurBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initConfigInfo();
    }

    private void initView() {
        mBinding.blurringView.setBlurredView(mBinding.imageView);

        mBinding.radioGroupScheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateSchemeChecked(checkedId);
                updateConfigInfo();
            }
        });
        mBinding.radioGroupAlgorithm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateAlgorithmChecked(checkedId);
                updateConfigInfo();
            }
        });
        mBinding.seekBarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        mBinding.seekBarSampleFactor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        mBinding.seekBarBlurScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBlurScale(progress);
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
        updateSchemeChecked(mBinding.radioGroupScheme.getCheckedRadioButtonId());
        updateSchemeChecked(mBinding.radioGroupAlgorithm.getCheckedRadioButtonId());
        updateRadius(mBinding.seekBarRadius.getProgress());
        updateRadius(mBinding.seekBarSampleFactor.getProgress());
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

    private void updateBlurScale(int progress) {
        blurScale = progress;
    }

    private void updateConfigInfo() {
        String info = String.format("Scheme: %s, Mode: %s, Radius: %s, Sample: %s, Scale: %s", getSchemeText(scheme), getModeText(mode), radius, sampleFactor, blurScale);
        mBinding.textViewInfo.setText(info);

        updateBlurProcessor();
    }

    private String getSchemeText(@Scheme int scheme) {
        return getSelectedText(mBinding.radioGroupScheme);
    }

    private String getModeText(@Mode int mode) {
        return getSelectedText(mBinding.radioGroupAlgorithm);
    }

    private String getSelectedText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(selectedId);
        return (String) radioButton.getText();
    }

    private void updateBlurProcessor() {

        mBinding.blurringView.setBlurProcessor(DewdropsBlur.with(this)
                .scheme(scheme)
                .mode(mode)
                .radius(radius)
                .sampleFactor(sampleFactor)
                .build());
        mBinding.blurringView.setScale(blurScale);
    }
}
