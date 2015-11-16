package com.lixinyuyin.monosyllabicdetect.activity.language.rate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;

import java.io.IOException;
import java.util.Random;

/**
 * Created by zqj on 2015/11/16 16:03.
 */
public class LanguageRecRate extends Activity implements View.OnClickListener {

    private int[] wordsResId = new int[]{R.raw.ai, R.raw.bi, R.raw.bu, R.raw.chan, R.raw.ci, R.raw.de, R.raw.dian, R.raw.diao, R.raw.ding,
            R.raw.dong, R.raw.du, R.raw.duo, R.raw.fen, R.raw.ge, R.raw.guo, R.raw.hai, R.raw.he, R.raw.ji, R.raw.jie, R.raw.jing, R.raw.ke,
            R.raw.le, R.raw.li, R.raw.ma, R.raw.neng, R.raw.pa, R.raw.qi, R.raw.qu, R.raw.ri, R.raw.se, R.raw.shan, R.raw.shang, R.raw.shi,
            R.raw.shu, R.raw.tiao, R.raw.tong, R.raw.wei, R.raw.xia, R.raw.xiang, R.raw.yan, R.raw.yin, R.raw.yong, R.raw.you, R.raw.zao,
            R.raw.ze, R.raw.zhe, R.raw.zhi, R.raw.zhi2, R.raw.zhong, R.raw.zi};

    private String[] wordsResContent = new String[]{"ai", "bi", "bu", "chan", "ci", "de", "dian", "diao", "ding", "dong", "du", "duo",
            "fen", "ge", "guo", "hai", "he", "ji", "jie", "jing", "ke", "le", "li", "ma", "neng", "pa", "qi", "qu", "ri", "se", "shan",
            "shang", "shi", "shu", "tiao", "tong", "wei", "xia", "xiang", "yan", "yin", "yong", "you", "zao", "ze", "zhe", "zhi", "zhi2",
            "zhong", "zi"};

    private final int mWordsNum = wordsResId.length;

    Button nextButton;

    private int seed = 47;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_rec_rate);
        initView();
        initData();
    }

    private void initView() {
        nextButton = (Button) findViewById(R.id.button_play);
    }

    private void initData() {
        if (mWordsNum != wordsResContent.length) {
            ToastUtil.toast(this, "资源文件错误，无法使用");
        } else {
            nextButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == nextButton) {
            Random random = new Random(seed);
            int selectedIndex = random.nextInt(mWordsNum);
            int playResId = wordsResId[selectedIndex];

            if (null == mMediaPlayer) {
                mMediaPlayer = MediaPlayer.create(this, playResId);
            } else {
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + playResId);
                mMediaPlayer.reset();
                try {
                    mMediaPlayer.setDataSource(this, uri);
                } catch (IOException e) {
                    ToastUtil.toast(this, "播放失败！");
                }
            }
            mMediaPlayer.start();
            ToastUtil.toast(this, wordsResContent[selectedIndex]);
            seed = selectedIndex;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LanguageRecRate.class);
        context.startActivity(intent);
    }
}
