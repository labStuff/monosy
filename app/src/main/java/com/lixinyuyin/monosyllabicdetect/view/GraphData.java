package com.lixinyuyin.monosyllabicdetect.view;

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
}
