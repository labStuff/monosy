package com.lixinyuyin.monosyllabicdetect.application;

import android.app.Application;

/**
 * Created by zqj on 2015/8/24 09:43.
 */
public class VApplication extends Application {

    private static VApplication instance;

    public static VApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
