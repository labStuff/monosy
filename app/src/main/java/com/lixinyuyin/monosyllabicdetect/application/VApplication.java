package com.lixinyuyin.monosyllabicdetect.application;

import android.app.Application;
import android.os.Environment;

import com.lixinyuyin.monosyllabicdetect.common.config.FileUtil;
import com.lixinyuyin.monosyllabicdetect.common.sp.SPUtil;

import java.io.File;

/**
 * Created by zqj on 2015/8/24 09:43.
 */
public class VApplication extends Application {

    private static VApplication instance;
    public final static String PARAM_FILE_PATH = Environment.getExternalStorageDirectory().toString();
    public final static String PARAM_FILE_NAME = PARAM_FILE_PATH + File.separator + "saved.par";

    static {
        System.loadLibrary("MyJni");
    }

    public static VApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (SPUtil.isFirstOpen()) {
            FileUtil.generateDefaultConfig(this);
        }
    }
}
