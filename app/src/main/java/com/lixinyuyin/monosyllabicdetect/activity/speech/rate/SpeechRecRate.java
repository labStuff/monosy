package com.lixinyuyin.monosyllabicdetect.activity.speech.rate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.DefaultValueFormatter;
import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.listener.DelayClickListener;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;
import com.lixinyuyin.monosyllabicdetect.view.PaperButton;
import com.lixinyuyin.monosyllabicdetect.view.dialog.SliderDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zqj on 2015/11/16 16:03.
 */
public class SpeechRecRate extends Activity implements View.OnClickListener, MediaPlayer.OnCompletionListener,SliderDialog.OnSureListener {

    private final int mDelay = 500;

    private List<Integer> originalWordsResId = Arrays.asList(R.raw.track01_male_ai, R.raw.track01_male_bi, R.raw.track01_male_bu, R.raw.track01_male_chan, R.raw.track01_male_ci, R.raw.track01_male_de, R.raw.track01_male_dian, R.raw.track01_male_diao, R.raw.track01_male_ding,
            R.raw.track01_male_dong, R.raw.track01_male_du, R.raw.track01_male_duo, R.raw.track01_male_fen, R.raw.track01_male_ge, R.raw.track01_male_guo, R.raw.track01_male_hai, R.raw.track01_male_he, R.raw.track01_male_ji, R.raw.track01_male_jie, R.raw.track01_male_jing, R.raw.track01_male_ke,
            R.raw.track01_male_le, R.raw.track01_male_li, R.raw.track01_male_ma, R.raw.track01_male_neng, R.raw.track01_male_pa, R.raw.track01_male_qi, R.raw.track01_male_qu, R.raw.track01_male_ri, R.raw.track01_male_se, R.raw.track01_male_shan, R.raw.track01_male_shang, R.raw.track01_male_shi,
            R.raw.track01_male_shu, R.raw.track01_male_tiao, R.raw.track01_male_tong, R.raw.track01_male_wei, R.raw.track01_male_xia, R.raw.track01_male_xiang, R.raw.track01_male_yan, R.raw.track01_male_yin, R.raw.track01_male_yong, R.raw.track01_male_you, R.raw.track01_male_zao,
            R.raw.track01_male_ze, R.raw.track01_male_zhe, R.raw.track01_male_zhi, R.raw.track01_male_zhi2, R.raw.track01_male_zhong, R.raw.track01_male_zi);
    private List<Integer> shuffledList = new ArrayList<>();

    private String[] wordsResContent;
    private MediaPlayer player;
    private MediaPlayer playerCache;

    private final int mWordsNum = originalWordsResId.size();
    private int mCurrentIndex = 0;

    private boolean[] answerSheet = new boolean[mWordsNum];
    private int rightCounter = 0;
    private String currentAnswer;

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

    PieChart pieChart;

    private int mTestNum = mWordsNum;

    private DelayClickListener choiceListener = new DelayClickListener(mDelay) {
        @Override
        public void doClick(View v) {
            String choose = (String) v.getTag();
            answerSheet[mCurrentIndex] = choose.equals(currentAnswer);
            if (answerSheet[mCurrentIndex])
                rightCounter++;
            mCurrentIndex++;

            invalidPieChart();
            if (mCurrentIndex < mTestNum)
                setNeedToPlayMode();
            else
                showResult();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_rec_rate);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        setTestNumDialog();
    }

    private void initView() {
        playImageView = (ImageView) findViewById(R.id.imageView_play);
        titleTextView = (TextView) findViewById(R.id.textview_title);
        titleTextView.setText(R.string.language_recognize_rate);
        backImageView = (ImageView) findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);

        retestLayout = (RelativeLayout) findViewById(R.id.layout_replay);
        retestLayout.setVisibility(View.INVISIBLE);
        retestLayout.setOnClickListener(this);

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

        pieChart = (PieChart) findViewById(R.id.piechart);
    }

    private void initData() {
        wordsResContent = getResources().getStringArray(R.array.Male_track01_words);
        if (mWordsNum != wordsResContent.length) {
            ToastUtil.toast(this, "资源文件错误，无法使用");
        } else {
            playImageView.setOnClickListener(this);
            shuffledList.addAll(originalWordsResId);
            Collections.shuffle(shuffledList);
            playerCache = MediaPlayer.create(this, shuffledList.get(0));

            initPieChart();
            invalidPieChart();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == playImageView) {
            setPlayingMode();
            // play wav file
            if (null != player) {
                player.release();
            }
            player = playerCache;
            //            player.setVolume(0f, 1.0f);
            player.setOnCompletionListener(this);
            player.start();
            playerCache = MediaPlayer.create(this, shuffledList.get((mCurrentIndex + 1) % mWordsNum));

            //generate choices
            int selectedId = originalWordsResId.indexOf(shuffledList.get(mCurrentIndex));
            currentAnswer = wordsResContent[selectedId];

            fillChoices(generateChoices(selectedId));

        } else if (v == backImageView) {
            finish();
        } else if (v == retestLayout) {
            retestLayout.setVisibility(View.INVISIBLE);
            mCurrentIndex = 0;
            rightCounter = 0;
            Collections.shuffle(shuffledList);
            if(null != playerCache){
                playerCache.release();
            }
            playerCache = MediaPlayer.create(this, shuffledList.get(0));
            playingHint.setText(R.string.playing);
            setNeedToPlayMode();
            invalidPieChart();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        setChoiceMode();
    }

    private void setPlayingMode() {
        playingHint.setVisibility(View.VISIBLE);
        choiceView.setVisibility(View.INVISIBLE);
        playImageView.setVisibility(View.INVISIBLE);
    }

    private void setChoiceMode() {
        playingHint.setVisibility(View.INVISIBLE);
        choiceView.setVisibility(View.VISIBLE);
        playImageView.setVisibility(View.INVISIBLE);
    }

    private void setNeedToPlayMode() {
        playingHint.setVisibility(View.INVISIBLE);
        choiceView.setVisibility(View.INVISIBLE);
        playImageView.setVisibility(View.VISIBLE);
    }

    private void showResult() {
        playingHint.setText(String.format(getString(R.string.result_speech_rate), mTestNum, rightCounter, (float) 100 * rightCounter / mTestNum));
        playingHint.setVisibility(View.VISIBLE);
        playImageView.setVisibility(View.INVISIBLE);
        choiceView.setVisibility(View.INVISIBLE);
        retestLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSure(int value) {
        mTestNum = value;
        initData();
    }

    private void setTestNumDialog(){
        SliderDialog dialog = new SliderDialog(this,mWordsNum);
        dialog.show();
        dialog.setTitle("选择测试数目");
        dialog.setSureButton(R.string.sure,this);
        dialog.setCancelable(false);
    }

    private List<String> generateChoices(int answerId) {
        Random random = new Random(System.currentTimeMillis());
        List<String> choiceList = new ArrayList<>();
        choiceList.add(wordsResContent[answerId]);
        for (int i = 1; i < 4; i++) {
            String selected = wordsResContent[random.nextInt(50)];
            while (choiceList.contains(selected))
                selected = wordsResContent[random.nextInt(50)];
            choiceList.add(selected);
        }
        Collections.shuffle(choiceList);
        return choiceList;
    }

    private void fillChoices(List<String> list) {
        if (null == list || list.size() != 4) {
            return;
        }
        choiceA.setTag(list.get(0));
        choiceA.setText(list.get(0));
        choiceB.setTag(list.get(1));
        choiceB.setText(list.get(1));
        choiceC.setTag(list.get(2));
        choiceC.setText(list.get(2));
        choiceD.setTag(list.get(3));
        choiceD.setText(list.get(3));
    }

    private void initPieChart() {
        if (null != pieChart) {
            pieChart.setUsePercentValues(false);
            pieChart.setClickable(false);
            pieChart.setTouchEnabled(false);
            pieChart.setDescription("");
            pieChart.setDrawHoleEnabled(false);
            pieChart.setRotationAngle(270);
            pieChart.setRotationEnabled(false);
            pieChart.setVisibility(View.VISIBLE);
            Legend l = pieChart.getLegend();
            l.setEnabled(false);
        }
    }

    private void invalidPieChart() {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        yVals1.add(new Entry(mCurrentIndex, 0));
        yVals1.add(new Entry(mTestNum - mCurrentIndex, 1));
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("已测试");
        xVals.add("未测试");

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(0);
        // dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(0xff427fed);
        colors.add(0xffbdbdbd);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
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
