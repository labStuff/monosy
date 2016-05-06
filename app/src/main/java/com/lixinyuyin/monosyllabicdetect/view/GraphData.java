package com.lixinyuyin.monosyllabicdetect.view;

import android.os.AsyncTask;
import android.widget.Toast;

import com.lixinyuyin.monosyllabicdetect.application.VApplication;
import com.lixinyuyin.monosyllabicdetect.common.config.WDRC;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/29.
 */
public class GraphData {
    private final static String splitChar = "#";
    private String mName;
    private long mDate;
    private int[] mMinData;
    private int[] mMediumData;
    private int[] mMaxData;

    private native void readParam();

    private native void saveWdrcInit(String path);

    private native void saveIntVal(int value);

    private native void saveDoubleVal(double value);

    public GraphData(int[] minData, int[] mediumData, int[] maxData, long date) {
        mMinData = minData;
        mMediumData = mediumData;
        mMaxData = maxData;
        mDate = date;
    }

    public GraphData(String name, int[] minData, int[] mediumData, int[] maxData, long date) {
        mName = name;
        mMinData = minData;
        mMediumData = mediumData;
        mMaxData = maxData;
        mDate = date;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public long getDate() {
        return mDate;
    }

    public String getMinDataString() {
        return arrayToString(mMinData);
    }

    public String getMediumDataString() {
        return arrayToString(mMediumData);
    }

    public String getMaxDataString() {
        return arrayToString(mMaxData);
    }

    public int[] getMinData() {
        return mMinData;
    }

    public int[] getMediumData() {
        return mMediumData;
    }

    public int[] getMaxData() {
        return mMaxData;
    }

    public String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return formatter.format(new Date(mDate));
    }

    public String getYearString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(mDate));
    }

    public String getDayString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date(mDate));
    }


    public static String arrayToString(int[] data) {
        StringBuffer stringBuffer = new StringBuffer();
        if (null != data) {
            for (int i = 0; i < data.length; i++) {
                stringBuffer.append(data[i]);
                stringBuffer.append(splitChar);
            }
        }
        return stringBuffer.toString();
    }


    public static int[] stringToArray(String floatArray) {
        String[] intArrayStrings = floatArray.split(splitChar);
        int N = intArrayStrings.length;
        int[] result = new int[N];
        for (int i = 0; i < N; i++) {
            result[i] = Integer.parseInt(intArrayStrings[i]);
        }
        return result;
    }

    public void saveParams() {
        new SaveParamsTask().execute();
    }

    private class SaveParamsTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            saveParam();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(VApplication.getInstance(), "参数保存完毕", Toast.LENGTH_SHORT).show();
            readParam();
        }

        private void saveParam() {
            double[] hearingTHR = new double[6];
            hearingTHR[0] = mMinData[1];//250Hz;
            hearingTHR[1] = mMinData[2];//500Hz;
            hearingTHR[2] = mMinData[4];//1000Hz;
            hearingTHR[3] = mMinData[6];//2000Hz;
            hearingTHR[4] = mMinData[8];//4000Hz;
            hearingTHR[5] = mMinData[10];//8000Hz;

            int num = 16;//num of channels
            double[] init = new double[num];
            double[][] k = new double[num][WDRC.partNum];
            double[][] b = new double[num][WDRC.partNum];
            double[] tpi = new double[WDRC.partNum + 1];

            double[] fc = new double[num];
            double[][] B = new double[num][];
            double[][] A = new double[num][];
            WDRC.iirParams(B, A, fc);

            double[] ht = new double[num];
            double[] hearing = new double[6];
            for (int i = 0; i < 6; i++) {
                hearing[i] = hearingTHR[i];
            }
            WDRC.linearInterp(ht, hearing, fc);

            double[][] cr = new double[num][WDRC.partNum];//生成的各段压缩比数组
            double[] tk = {15, 30, 45, 60, 75, 90, 105};
            for (int i = 0; i < num; i++) {
                init[i] = WDRC.crCalculation(cr[i], ht[i]);
                WDRC.wdrcPara(k[i], b[i], tpi, tk, cr[i], init[i]);
            }
            saveWdrcInit(VApplication.PARAM_FILE_NAME);
            for (int i = 0; i < B.length; i++) {
                for (int j = 0; j < B[0].length; j++) {
                    saveDoubleVal(B[i][j]);
                }
            }
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < A[0].length; j++) {
                    saveDoubleVal(A[i][j]);
                }
            }
            for (int i = 0; i < k.length; i++) {
                for (int j = 0; j < k[0].length; j++) {
                    saveDoubleVal(k[i][j]);
                }
            }
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[0].length; j++) {
                    saveDoubleVal(b[i][j]);
                }
            }
            for (int i = 0; i < tpi.length; i++) {
                saveDoubleVal(tpi[i]);
            }
        }
    }
}
