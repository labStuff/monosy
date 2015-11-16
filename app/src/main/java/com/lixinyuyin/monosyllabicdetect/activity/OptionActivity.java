package com.lixinyuyin.monosyllabicdetect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.activity.pure.PureToneTestActivity;
import com.lixinyuyin.monosyllabicdetect.activity.resolution.ToneResolutionActivity;
import com.lixinyuyin.monosyllabicdetect.model.VAccount;


/**
 * Created by zqj on 2015/8/24 10:06.
 */
public class OptionActivity extends Activity implements View.OnClickListener {

    Button toneResolution;
    Button pureToneTest;
    Button accountSwitch;

    TextView userInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        initView();
    }

    private void initView() {
        toneResolution = (Button) findViewById(R.id.button_test_resolution);
        toneResolution.setOnClickListener(this);
        pureToneTest = (Button) findViewById(R.id.button_pure_tone_test);
        pureToneTest.setOnClickListener(this);

        userInfoTextView = (TextView) findViewById(R.id.textView_userInfo);
        userInfoTextView.setText(VAccount.getUserName() + getString(R.string.user_info_hint));

        accountSwitch = (Button) findViewById(R.id.button_account_switch);
        accountSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == toneResolution) {
            ToneResolutionActivity.start(this);
        } else if (v == pureToneTest) {
            PureToneTestActivity.start(this, VAccount.getUserName());
        } else if (v == accountSwitch) {
            LoginActivity.start(this);
            VAccount.clear();
            finish();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OptionActivity.class);
        context.startActivity(intent);
    }

}
