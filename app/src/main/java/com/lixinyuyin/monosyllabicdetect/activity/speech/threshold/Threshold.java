package com.lixinyuyin.monosyllabicdetect.activity.speech.threshold;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.listener.DelayClickListener;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;
import com.lixinyuyin.monosyllabicdetect.view.PaperButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zqj on 2015/11/18 14:59.
 */
public class Threshold extends Activity implements View.OnClickListener, OnCompletionListener {
    private final int mDelay = 500;

    private List<Integer> wavResId = Arrays.asList(
            R.raw.track07_male_canjia, R.raw.track07_male_chulai, R.raw.track07_male_daibiao, R.raw.track07_male_danshi,
            R.raw.track07_male_dedao, R.raw.track07_male_fasheng, R.raw.track07_male_gezhong, R.raw.track07_male_gongzuo,
            R.raw.track07_male_huanbao, R.raw.track07_male_jingguo, R.raw.track07_male_keyi, R.raw.track07_male_liyong,
            R.raw.track07_male_minzu, R.raw.track07_male_qunian, R.raw.track07_male_renhe, R.raw.track07_male_shuiping,
            R.raw.track07_male_tebie, R.raw.track07_male_tongzhi, R.raw.track07_male_weida, R.raw.track07_male_xuduo,
            R.raw.track07_male_yanse, R.raw.track07_male_yaoqiu, R.raw.track07_male_yishu, R.raw.track07_male_ziji);
    private List<Integer> shuffledList = new ArrayList<>();

    private int mWavNum = wavResId.size();
    private String[] wavsContent;
    private MediaPlayer player;
    private MediaPlayer playerCache;

    TextView titleTextView;
    ImageView backImageView;

    RelativeLayout retestLayout;

    ImageView playImageView;
    View choiceView;

    TextView playingHint;

    PaperButton choiceA;
    PaperButton choiceB;
    PaperButton choiceC;
    PaperButton choiceD;

    private List<String> mChoices = Arrays.asList("a", "b", "c", "d");

    private int mCurrentIndex = 0;
    private String mCurrentAnswer;

    private int mScoreCounter = 0;


    private int mVolume = 46;
    private AudioManager mAudioManager;

    private DelayClickListener choiceListener = new DelayClickListener(mDelay) {
        @Override
        public void doClick(View v) {
            String choose = (String) v.getTag();
            if (mCurrentAnswer.equals(choose))
                mScoreCounter++;
            if (mCurrentIndex == 19) {
                testOver();
            } else {
                if (mCurrentIndex % 5 == 4) {
                    mVolume = mVolume - 15;
                }
                mCurrentIndex++;
                waitToPlay();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_threshold);
        initView();
        initData();
    }

    private void initView() {
        playImageView = (ImageView) findViewById(R.id.imageView_play);
        playImageView.setOnClickListener(this);

        titleTextView = (TextView) findViewById(R.id.textview_title);
        titleTextView.setText(R.string.speech_recognize_threshold);
        backImageView = (ImageView) findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);

        choiceView = findViewById(R.id.layout_choice);
        choiceView.setVisibility(View.INVISIBLE);

        playingHint = (TextView) findViewById(R.id.textView_playing_hint);
        playingHint.setVisibility(View.INVISIBLE);

        choiceA = (PaperButton) findViewById(R.id.button_a);
        choiceB = (PaperButton) findViewById(R.id.button_b);
        choiceC = (PaperButton) findViewById(R.id.button_c);
        choiceD = (PaperButton) findViewById(R.id.button_d);
        choiceA.setOnClickListener(choiceListener);
        choiceB.setOnClickListener(choiceListener);
        choiceC.setOnClickListener(choiceListener);
        choiceD.setOnClickListener(choiceListener);
    }

    private void initData() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, mVolume);

        wavsContent = getResources().getStringArray(R.array.Male_track07_words);
        shuffledList.addAll(wavResId);
        Collections.shuffle(shuffledList);
        playerCache = MediaPlayer.create(this, shuffledList.get(mCurrentIndex));
    }

    @Override
    public void onClick(View v) {
        if (v == backImageView) {
            finish();
        } else if (v == playImageView) {
            if (null != player) {
                player.release();
            }
            adjustVolume();
            player = playerCache;
            player.setOnCompletionListener(this);
            player.start();

            int resIndex = wavResId.indexOf(shuffledList.get(mCurrentIndex));
            generateChoices(resIndex);
            playerCache = MediaPlayer.create(this, shuffledList.get((mCurrentIndex + 1) % mWavNum));
            playing();
        }
    }

    private void adjustVolume() {
        if (mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != mVolume)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, mVolume);
    }

    private void waitToPlay() {
        playingHint.setVisibility(View.INVISIBLE);
        choiceView.setVisibility(View.INVISIBLE);

        playImageView.setVisibility(View.VISIBLE);
    }

    private void playing() {
        playImageView.setVisibility(View.INVISIBLE);
        choiceView.setVisibility(View.INVISIBLE);

        playingHint.setText(R.string.playing);
        playingHint.setVisibility(View.VISIBLE);
    }

    private void choosing() {
        playImageView.setVisibility(View.INVISIBLE);
        playingHint.setVisibility(View.INVISIBLE);

        choiceView.setVisibility(View.VISIBLE);
    }

    private void generateChoices(int rightId) {
        mCurrentAnswer = wavsContent[rightId];
        mChoices.set(0, mCurrentAnswer);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i < mChoices.size(); i++) {
            String temp = wavsContent[random.nextInt(mWavNum)];
            while (mChoices.contains(temp))
                temp = wavsContent[random.nextInt(mWavNum)];
            mChoices.set(i, temp);
        }
        Collections.shuffle(mChoices);
        choiceA.setText(mChoices.get(0));
        choiceA.setTag(mChoices.get(0));
        choiceB.setText(mChoices.get(1));
        choiceB.setTag(mChoices.get(1));
        choiceC.setText(mChoices.get(2));
        choiceC.setTag(mChoices.get(2));
        choiceD.setText(mChoices.get(3));
        choiceD.setTag(mChoices.get(3));
    }

    private void testOver() {
        playImageView.setVisibility(View.INVISIBLE);
        choiceView.setVisibility(View.INVISIBLE);

        playingHint.setText(String.format(getString(R.string.result_speech_threshold), mScoreCounter));
        playingHint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        choosing();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            ToastUtil.toast(this, "音量不让调");
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, Threshold.class);
        context.startActivity(intent);
    }
}
