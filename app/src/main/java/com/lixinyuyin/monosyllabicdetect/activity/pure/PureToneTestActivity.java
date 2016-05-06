package com.lixinyuyin.monosyllabicdetect.activity.pure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.database.graphdata.GraphDataDao;
import com.lixinyuyin.monosyllabicdetect.model.VAccount;
import com.lixinyuyin.monosyllabicdetect.util.StatusBarUtil;
import com.lixinyuyin.monosyllabicdetect.view.GraphData;
import com.lixinyuyin.monosyllabicdetect.view.PureToneGraph;
import com.lixinyuyin.monosyllabicdetect.view.slider.Slider;

public class PureToneTestActivity extends Activity implements OnClickListener {

    public static final int SAMPLE_RATE_HZ = 44104;
    public static final int CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
    public static final int SAMPLE_BIT = AudioFormat.ENCODING_PCM_FLOAT;

    private int[] mVoiceFrequency = {125, 250, 500, 750, 1000, 1500, 2000, 3000, 4000, 5000, 8000};// unit Hz
    //    private short maxAmp = Short.MAX_VALUE;
    private int mFrameLength = (int) (0.1 * SAMPLE_RATE_HZ);
    private float[][][] mData = new float[mVoiceFrequency.length][100][mFrameLength];
    private int mBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL, SAMPLE_BIT);

    private AudioTrack audioTrack;
    private AudioManager mAudioManager;

    private float mVolume = 1.0f;
    private float mVolumeStep;
    private float mMaxVolume;
    private int mVolumeIndex = 1;// 1 ~ 100
    private int mFrequencyIndex = -1;// 0 ~ 7

    Button addButton;
    Button minusButton;
    Button playButton;
    Button volumeMin;
    Button volumeMedium;
    Button volumeMax;
    PureToneGraph pureToneGraph;
    Slider volumeSliderX;
    Slider volumeSliderx;
    TextView infoTextView;

    ImageView backImageView;
    ImageView menuImageView;
    ImageView saveImageView;
    PopupWindow menuPopWindow;

    private SinWavTask mWavTask;

    public static String mUserName;

    private short dataAmp = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        StatusBarUtil.setStatusBarColor(this);
        initView();
        initData();
        onClick(addButton);
    }

    private void initView() {

        backImageView = (ImageView) findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);

        saveImageView = (ImageView) findViewById(R.id.imageview_save);
        saveImageView.setOnClickListener(this);
        menuImageView = (ImageView) findViewById(R.id.imageview_menu);
        menuImageView.setOnClickListener(this);

        addButton = (Button) findViewById(R.id.button_fre_add);
        addButton.setOnClickListener(this);
        minusButton = (Button) findViewById(R.id.button_fre_minus);
        minusButton.setOnClickListener(this);
        playButton = (Button) findViewById(R.id.button_play);
        playButton.setVisibility(View.INVISIBLE);
        playButton.setOnClickListener(this);

        volumeMin = (Button) findViewById(R.id.button_volume_min);
        volumeMin.setOnClickListener(this);
        volumeMedium = (Button) findViewById(R.id.button_volume_medium);
        volumeMedium.setOnClickListener(this);
        volumeMax = (Button) findViewById(R.id.button_volume_max);
        volumeMax.setOnClickListener(this);

        pureToneGraph = (PureToneGraph) findViewById(R.id.gridgraph);

        volumeSliderX = (Slider) findViewById(R.id.slider_volume_X);
        volumeSliderX.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                //                setVolume(value);
                mVolumeIndex = 10 * value + volumeSliderx.getValue();
                volumeChanged();
            }
        });
        volumeSliderx = (Slider) findViewById(R.id.slider_volume_x);
        volumeSliderx.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                mVolumeIndex = value + 10 * volumeSliderX.getValue();
                volumeChanged();
            }
        });

        infoTextView = (TextView) findViewById(R.id.textview_info);
    }

    private void initData() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 60, 0);

        audioTrack =
                new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_HZ, CHANNEL, SAMPLE_BIT,
                        mBufferSize * 2, AudioTrack.MODE_STREAM);
        //        audioTrack.setVolume(mVolume);
        //        audioTrack.setStereoVolume(mVolume, mVolume);
        //        mMaxVolume = audioTrack.getMaxVolume();
        //        mMaxVolume = 100;
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //        mVolumeStep = mMaxVolume / 100;
        audioTrack.play();
        new CalWavTask().execute();
    }

    private void volumeChanged() {
        //        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mVolumeIndex,0);
        //                audioTrack.setStereoVolume(mVolumeIndex * mVolumeStep, mVolumeIndex * mVolumeStep);
        refreshInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.imageview_save:
                saveGraph();
                break;
            case R.id.imageview_menu:
                showMenuPopwindow();
                break;
            case R.id.button_volume_min:
                pureToneGraph.setMinPoint(mFrequencyIndex, mVolumeIndex);
                break;
            case R.id.button_volume_medium:
                pureToneGraph.setMediumPoint(mFrequencyIndex, mVolumeIndex);
                break;
            case R.id.button_volume_max:
                pureToneGraph.setMaxPoint(mFrequencyIndex, mVolumeIndex);
                break;
            case R.id.button_fre_add:
                mFrequencyIndex++;
                if (mFrequencyIndex >= mVoiceFrequency.length) {
                    mFrequencyIndex = mVoiceFrequency.length - 1;
                }
                frequencyChanged();
                break;
            case R.id.button_fre_minus:
                mFrequencyIndex--;
                if (mFrequencyIndex < 0) {
                    mFrequencyIndex = 0;
                }
                frequencyChanged();
                break;
            case R.id.button_play:
                if (null != mWavTask) {
                    if (mWavTask.isPause) {
                        mWavTask.reStart();
                        playButton.setBackgroundResource(R.drawable.ic_state_playing);
                    } else {
                        mWavTask.pause();
                        playButton.setBackgroundResource(R.drawable.ic_state_pausing);
                    }
                }
                break;
            default:
                break;
        }

    }

    private void saveGraph() {
        GraphDataDao mGraphDataDao = new GraphDataDao(this);
        GraphData graphData = pureToneGraph.getLineData();
        graphData.setName(mUserName);
        mGraphDataDao.add(graphData);
        mGraphDataDao.close();
        //        Toast.makeText(this, "Save Succeed!", Toast.LENGTH_SHORT).show();
        graphData.saveParams();
    }

    private void frequencyChanged() {
        if (null == mWavTask) {
            mWavTask = new SinWavTask(mFrequencyIndex);
            mWavTask.start();
        } else {
            mWavTask.setFrequency(mFrequencyIndex);
        }
        refreshInfo();
    }

    private void refreshInfo() {
        infoTextView.setText(String.format(getString(R.string.wavInfoHint), mVoiceFrequency[mFrequencyIndex],
                mVolumeIndex));
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

    private void showMenuPopwindow() {
        View menuPopWindowView = View.inflate(this, R.layout.menu_popwindow_layout, null);
        Button historyButton = (Button) menuPopWindowView.findViewById(R.id.button_history);
        Button exitButton = (Button) menuPopWindowView.findViewById(R.id.button_exit);
        historyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryRecordActivity.start(PureToneTestActivity.this);
            }
        });
        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VAccount.clear();
                finish();
            }
        });
        menuPopWindow = new PopupWindow(menuPopWindowView, 200, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        menuPopWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (null != menuPopWindow && menuPopWindow.isShowing()) {
                    menuPopWindow.dismiss();
                    menuPopWindow = null;
                }
                return false;
            }
        });
        menuPopWindow.showAsDropDown(menuImageView);

    }

    class CalWavTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            playButton.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            double pa = Math.pow(2, -0.5) * 1E-5;
            for (int k = 0; k < mData[0].length; k++) {
                double f = Math.pow(10, (double) (k + 1) / 20);
                double amp = 1.4142 * pa * f;
                for (int i = 0; i < mVoiceFrequency.length; i++) {
                    double w = 2.0 * Math.PI * mVoiceFrequency[i] / (double) SAMPLE_RATE_HZ;
                    for (int j = 0; j < mFrameLength; j++) {
                        mData[i][k][j] = (float) (amp * Math.sin(w * j));
                    }
                }
            }
            return null;
        }
    }

    class SinWavTask extends Thread {

        private int mFrequencyIndex = 0;
        private boolean isRunning = true;
        private boolean isPause = true;

        public SinWavTask(int index) {
            mFrequencyIndex = index;
        }

        @Override
        public void run() {
            while (isRunning) {
                // 将波形数据分段送入 audioTrack ,使切换更加迅速灵敏
                int frameLength = mData[mFrequencyIndex][0].length;
                if (!isPause) {
                    audioTrack.write(mData[mFrequencyIndex][mVolumeIndex - 1], 0, frameLength, AudioTrack.WRITE_BLOCKING);
                }
            }
        }

        public void setFrequency(int index) {
            if (mFrequencyIndex != index) {
                mFrequencyIndex = index;
            }
        }

        public void close() {
            isRunning = false;
        }

        public void pause() {
            isPause = true;
        }

        public void reStart() {
            isPause = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWavTask) {
            mWavTask.close();
        }
        audioTrack.stop();
        audioTrack.release();
    }

    /**
     * start self
     */
    public static void start(Context context, String userName) {
        Intent intent = new Intent(context, PureToneTestActivity.class);
        mUserName = userName;
        context.startActivity(intent);
    }
}
