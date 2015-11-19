package com.lixinyuyin.monosyllabicdetect.activity.speech.rate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by zqj on 2015/11/16 16:03.
 */
public class SpeechRecRate extends Activity implements View.OnClickListener {

    private List<Integer> originalWordsResId = Arrays.asList(R.raw.ai, R.raw.bi, R.raw.bu, R.raw.chan, R.raw.ci, R.raw.de, R.raw.dian, R.raw.diao, R.raw.ding,
            R.raw.dong, R.raw.du, R.raw.duo, R.raw.fen, R.raw.ge, R.raw.guo, R.raw.hai, R.raw.he, R.raw.ji, R.raw.jie, R.raw.jing, R.raw.ke,
            R.raw.le, R.raw.li, R.raw.ma, R.raw.neng, R.raw.pa, R.raw.qi, R.raw.qu, R.raw.ri, R.raw.se, R.raw.shan, R.raw.shang, R.raw.shi,
            R.raw.shu, R.raw.tiao, R.raw.tong, R.raw.wei, R.raw.xia, R.raw.xiang, R.raw.yan, R.raw.yin, R.raw.yong, R.raw.you, R.raw.zao,
            R.raw.ze, R.raw.zhe, R.raw.zhi, R.raw.zhi2, R.raw.zhong, R.raw.zi);
    private List<Integer> shuffledList = new ArrayList<>();

    private String[] wordsResContent = new String[]{"ai爱", "bi比", "bu不", "产", "ci次", "de的", "dian电", "diao掉", "ding定",
            "dong冻", "du度", "duo多", "fen分", "ge哥", "guo果", "hai还", "he和", "ji鸡", "jie阶", "jing经", "ke科",
            "le乐", "li利", "ma妈", "neng能", "pa怕", "qi起", "qu去", "ri日", "se色", "shan山", "shang上", "shi时", "shu树", "tiao跳",
            "tong同", "wei维", "xia下", "xiang", "yan严", "yin因", "yong勇", "you又", "zao早", "ze则", "zhe者", "zhi之", "zhi值",
            "zhong中", "zi子"};

    private MediaPlayer player;
    private MediaPlayer playerCache;

    private final int mWordsNum = originalWordsResId.size();
    private int mCurrentIndex = 0;

    TextView titleTextView;

    Button nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_rec_rate);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        initData();
    }

    private void initView() {
        nextButton = (Button) findViewById(R.id.button_play);
        titleTextView = (TextView) findViewById(R.id.textview_title);
        titleTextView.setText(R.string.language_recognize_rate);
    }

    private void initData() {
        if (mWordsNum != wordsResContent.length) {
            ToastUtil.toast(this, "资源文件错误，无法使用");
        } else {
            nextButton.setOnClickListener(this);
        }
        shuffledList.addAll(originalWordsResId);
        Collections.shuffle(shuffledList);
        playerCache = MediaPlayer.create(this, shuffledList.get(0));
    }

    @Override
    public void onClick(View v) {
        if (v == nextButton) {
            if (mCurrentIndex == mWordsNum - 1) {
                Collections.shuffle(shuffledList);
                mCurrentIndex = 0;
                ToastUtil.toast(this, "下一轮");
            }
            if (null != player) {
                player.release();
            }
            player = playerCache;
//            player.setVolume(0f, 1.0f);
            player.start();
            playerCache = MediaPlayer.create(this, shuffledList.get(mCurrentIndex + 1));

            int selectedId = originalWordsResId.indexOf(shuffledList.get(mCurrentIndex));
            ToastUtil.toast(this, wordsResContent[selectedId] + "(" + selectedId + ")");
            mCurrentIndex++;
        }
    }

    @Override
    protected void onDestroy() {
        if (null != player) {
            player.release();
        }
        if (null != playerCache) {
            playerCache.release();
        }
        super.onDestroy();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SpeechRecRate.class);
        context.startActivity(intent);
    }
}
