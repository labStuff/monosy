package com.lixinyuyin.monosyllabicdetect.activity.resolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;
import com.lixinyuyin.monosyllabicdetect.view.ThreeIndicator;
import com.lixinyuyin.monosyllabicdetect.view.ToneResolutionGraph;
import com.lixinyuyin.monosyllabicdetect.view.slider.Slider;

/**
 * Created by zqj on 2015/8/19 15:35.
 */
public class ToneResolutionActivity extends Activity implements View.OnClickListener {

    public static final int SAMPLE_RATE_HZ = 44104;
    public static final int CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
    public static final int SAMPLE_BIT = AudioFormat.ENCODING_PCM_16BIT;

    private int[] mVoiceFrequency = {125, 250, 500, 750, 1000, 1500, 2000, 3000, 4000, 5000, 8000};// unit Hz
    private double[] mFreOffset = {0.5, 0.25, 0.125, 0.0625, 0.03125, 0.015625, 0.0078125, 0.00390625, 0.001953125};
    private short maxAmp = Short.MAX_VALUE;
    private int mFrameLength = SAMPLE_RATE_HZ / 2;
    private float mMaxVolume;
    private float mVolumeStep;

    private int mVolumeIndex = 1;
    private int mFreIndex = 0;
    private int[] mFreOffsetIndex = new int[mVoiceFrequency.length];

    private int mBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL, SAMPLE_BIT);

    private AudioTrack audioTrack;
    private AudioManager mAudioManager;

    ImageView backImageView;
    ImageView saveImageView;
    ImageView menuImageView;

    TextView freInfoTextView;
    ToneResolutionGraph toneResolutionGraph;

    Slider volumeSlider;

    Button freMinusButton;
    Button freAddButton;

    Button playButton;
    TextView playHintTextView;

    Button sameButton;
    Button differentButton;

    /**
     * indicators
     */
    ThreeIndicator indicator;
    private int mStageIndicator = ThreeIndicator.STAGE_ONE;
    private boolean mSameFlag = false;

    /**
     * 某一频点、某一分辨率下，有两轮测试机会，每轮三个测试样本，只有当三个样本群全部正确才算通过。
     */
    private int mRoll = 0;//正处在第几轮
    private boolean[] mSingleTestResult = new boolean[3];//某一频点、某一分辨率条件下 三次测试的结果

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            playHintTextView.setText(getString(R.string.not_play));
            showChoiceButtons(true);
        }
    };
    private final static int DELAY = 1500;//1.5s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toneresolution);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        initData();
        indicator.show(mStageIndicator);
        showChoiceButtons(false);
    }


    private void initView() {
        backImageView = (ImageView) findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);
        saveImageView = (ImageView) findViewById(R.id.imageview_save);
        saveImageView.setOnClickListener(this);
        menuImageView = (ImageView) findViewById(R.id.imageview_menu);
        menuImageView.setOnClickListener(this);

        freInfoTextView = (TextView) findViewById(R.id.textView_frequency_info);
        toneResolutionGraph = (ToneResolutionGraph) findViewById(R.id.tone_resolution_graph);

        volumeSlider = (Slider) findViewById(R.id.slider_volume);
        volumeSlider.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                mVolumeIndex = value;
                audioTrack.setStereoVolume(mVolumeIndex * mVolumeStep, mVolumeIndex * mVolumeStep);
            }
        });
        freMinusButton = (Button) findViewById(R.id.button_fre_minus);
        freMinusButton.setOnClickListener(this);

        freAddButton = (Button) findViewById(R.id.button_fre_add);
        freAddButton.setOnClickListener(this);

        playButton = (Button) findViewById(R.id.button_play);
        playButton.setOnClickListener(this);

        playHintTextView = (TextView) findViewById(R.id.textView_play_hint);

        sameButton = (Button) findViewById(R.id.button_same);
        sameButton.setOnClickListener(this);

        differentButton = (Button) findViewById(R.id.button_different);
        differentButton.setOnClickListener(this);

        indicator = (ThreeIndicator) findViewById(R.id.three_indicators);
    }

    private void initData() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 12, 0);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        audioTrack =
                new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_HZ, CHANNEL, SAMPLE_BIT,
                        mBufferSize * 2, AudioTrack.MODE_STREAM);
        mMaxVolume = audioTrack.getMaxVolume();
        mVolumeStep = mMaxVolume / 15;
        audioTrack.setStereoVolume(mVolumeIndex * mVolumeStep, mVolumeIndex * mVolumeStep);
        audioTrack.play();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                Toast.makeText(this, "该模式不可调节系统音量", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Toast.makeText(this, "该模式不可调节系统音量", Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.imageview_save:
                break;
            case R.id.imageview_menu:
                break;
            case R.id.button_play:
                playButton.setVisibility(View.INVISIBLE);
                playClicked();
                break;
            case R.id.button_fre_add:
                freAdd();
                break;
            case R.id.button_fre_minus:
                freMinus();
                break;
            case R.id.button_different:
                differentButtonClicked();
                break;
            case R.id.button_same:
                sameButtonClicked();
                break;
            default:
                break;
        }
    }

    private void differentButtonClicked() {
        mSingleTestResult[mStageIndicator] = !mSameFlag;
        nextTest();
    }


    private void sameButtonClicked() {
        mSingleTestResult[mStageIndicator] = mSameFlag;
        nextTest();
    }

    private void nextTest() {
        if (mStageIndicator == ThreeIndicator.STAGE_THREE) {

            if (isResultsAllRight()) {
                mFreOffsetIndex[mFreIndex]++;
                if (mFreOffsetIndex[mFreIndex] >= mFreOffset.length) {
                    mFreOffsetIndex[mFreIndex] = mFreOffset.length - 1;
                    ToastUtil.toast(this, "已达到本测试最佳分辨率，自动切换至下一频点");
                    testNextFre();
                } else {
                    toneResolutionGraph.changeData(mFreIndex, mFreOffsetIndex[mFreIndex]);
                    //轮数重置、阶段重置、三次测试结果重置
                    mRoll = 0;
                    mStageIndicator = ThreeIndicator.STAGE_ONE;
                    mSingleTestResult[0] = false;
                    mSingleTestResult[1] = false;
                    mSingleTestResult[2] = false;
                }
            } else {
                if (mRoll == 1) {
                    //测试下一个频率点
                    if (mFreOffsetIndex[mFreIndex] > 0) {
                        mFreOffsetIndex[mFreIndex] = mFreOffsetIndex[mFreIndex] - 1;
                        toneResolutionGraph.changeData(mFreIndex, mFreOffsetIndex[mFreIndex]);
                    }
                    testNextFre();
                } else {
                    mRoll++;
                    //阶段重置、三次测试结果重置
                    mStageIndicator = ThreeIndicator.STAGE_ONE;
                    mSingleTestResult[0] = false;
                    mSingleTestResult[1] = false;
                    mSingleTestResult[2] = false;
                }
            }
        } else {
            mStageIndicator++;
        }
        indicator.show(mStageIndicator);
        playButton.setVisibility(View.VISIBLE);
        showChoiceButtons(false);
    }

    private void testNextFre() {
        //测试下一个频率点
        mFreIndex++;
        if (mFreIndex >= mVoiceFrequency.length) {
            mFreIndex = mVoiceFrequency.length - 1;
        }
        freInfoTextView.setText(String.format(getString(R.string.frequency_info),
                mVoiceFrequency[mFreIndex], mFreIndex + 1));
        //轮数重置、阶段重置、三次测试结果重置
        mRoll = 0;
        mStageIndicator = ThreeIndicator.STAGE_ONE;
        mSingleTestResult[0] = false;
        mSingleTestResult[1] = false;
        mSingleTestResult[2] = false;
    }

    private boolean isResultsAllRight() {
        return mSingleTestResult[0] && mSingleTestResult[1] && mSingleTestResult[2];
    }

    private void playClicked() {
        playHintTextView.setText(getString(R.string.playing));
        new PlayWavTask().execute(0);
        mHandler.postDelayed(runnable, DELAY);
    }

    private void freMinus() {
        mFreIndex--;
        if (mFreIndex == -1) {
            mFreIndex++;
        }
        freInfoTextView.setText(String.format(getString(R.string.frequency_info),
                mVoiceFrequency[mFreIndex], mFreIndex + 1));
    }


    private void freAdd() {
        mFreIndex++;
        if (mFreIndex == mVoiceFrequency.length) {
            mFreIndex--;
        }
        freInfoTextView.setText(String.format(getString(R.string.frequency_info),
                mVoiceFrequency[mFreIndex], mFreIndex + 1));
    }

    //    private void setButtonsState(int index) {
    //        /**
    //         * 如果是0表示 播放阶段，播放按钮可以点击，选择按钮无法点击；
    //         * 如果是1表示 选择阶段，选择按钮可以点击，播放按钮无法点击；
    //         */
    //        if (index == 0) {
    ////            playButton.setClickable(true);
    ////            sameButton.setClickable(false);
    ////            differentButton.setClickable(false);
    //            playButton.setVisibility(View.VISIBLE);
    //            sameButton.setVisibility(View.INVISIBLE);
    //            differentButton.setVisibility(View.INVISIBLE);
    //        } else {
    ////            playButton.setClickable(false);
    ////            sameButton.setClickable(true);
    ////            differentButton.setClickable(true);
    //            playButton.setVisibility(View.INVISIBLE);
    //            sameButton.setVisibility(View.VISIBLE);
    //            differentButton.setVisibility(View.VISIBLE);
    //        }
    //    }

    private void showChoiceButtons(boolean show) {
        if (show) {
            sameButton.setVisibility(View.VISIBLE);
            differentButton.setVisibility(View.VISIBLE);
        } else {
            sameButton.setVisibility(View.INVISIBLE);
            differentButton.setVisibility(View.INVISIBLE);
        }
    }

    class PlayWavTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            double frequency_a = mVoiceFrequency[mFreIndex];
            double frequency_b;
            double flag = Math.random();
            //            double flag = 0.9; //This is for fast verify
            mSameFlag = false;
            if (flag < 0.34) {
                frequency_b = (1 + mFreOffset[mFreOffsetIndex[mFreIndex]]) * frequency_a;
            } else if (flag > 0.67) {
                frequency_b = (1 - mFreOffset[mFreOffsetIndex[mFreIndex]]) * frequency_a;
            } else {
                frequency_b = frequency_a;
                mSameFlag = true;
            }

            double w1 = 2.0 * Math.PI * frequency_a / SAMPLE_RATE_HZ;
            double w2 = 2.0 * Math.PI * frequency_b / SAMPLE_RATE_HZ;
            short[] mData = new short[3 * mFrameLength];
            for (int i = 0; i < mFrameLength; i++) {
                mData[i] = (short) (maxAmp * Math.sin(w1 * i));
                mData[i + 2 * mFrameLength] = (short) (maxAmp * Math.sin(w2 * i));
            }
            audioTrack.write(mData, 0, 3 * mFrameLength);
            return null;
        }
    }

    /**
     * start self
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, ToneResolutionActivity.class);
        context.startActivity(intent);
    }


}
