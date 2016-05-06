package com.lixinyuyin.monosyllabicdetect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.activity.pure.PureToneTestActivity;
import com.lixinyuyin.monosyllabicdetect.activity.resolution.ToneResolutionActivity;
import com.lixinyuyin.monosyllabicdetect.activity.speech.rate.SpeechRecRate;
import com.lixinyuyin.monosyllabicdetect.activity.speech.threshold.Threshold;
import com.lixinyuyin.monosyllabicdetect.listener.DelayClickListener;
import com.lixinyuyin.monosyllabicdetect.model.VAccount;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.view.PaperButton;


/**
 * Created by zqj on 2015/8/24 10:06.
 */
public class OptionActivity extends Activity {

    private final int mDelay = 500;

    PaperButton toneResolution;
    PaperButton pureToneTest;
    PaperButton accountSwitch;
    PaperButton speechRecRate;
    PaperButton speechThreshold;
    PaperButton calibration;
    TextView title;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        mContext = this;
    }

    private void initView() {
        toneResolution = (PaperButton) findViewById(R.id.button_test_resolution);
        toneResolution.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                ToneResolutionActivity.start(mContext);
            }
        });
        pureToneTest = (PaperButton) findViewById(R.id.button_pure_tone_test);
        pureToneTest.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                PureToneTestActivity.start(mContext, VAccount.getUserName());
            }
        });

        title = (TextView) findViewById(R.id.textView_title);
        title.setText(VAccount.getUserName() + getString(R.string.user_info_hint));

        speechRecRate = (PaperButton) findViewById(R.id.button_language_rec_rate);
        speechRecRate.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                SpeechRecRate.start(mContext);
            }
        });

        accountSwitch = (PaperButton) findViewById(R.id.button_account_switch);
        accountSwitch.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                LoginActivity.start(mContext);
                VAccount.clear();
                finish();
            }
        });

        speechThreshold = (PaperButton) findViewById(R.id.button_speech_rec_threshold);
        speechThreshold.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                Threshold.start(OptionActivity.this);
            }
        });

        calibration = (PaperButton) findViewById(R.id.button_calibration);
        calibration.setOnClickListener(new DelayClickListener(mDelay) {
            @Override
            public void doClick(View v) {
                CalibrateActivity.start(OptionActivity.this);
            }
        });

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OptionActivity.class);
        context.startActivity(intent);
    }

}
