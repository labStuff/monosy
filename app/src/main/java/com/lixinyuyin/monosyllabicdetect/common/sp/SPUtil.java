package com.lixinyuyin.monosyllabicdetect.common.sp;

import android.app.Activity;
import android.content.SharedPreferences;

import com.lixinyuyin.monosyllabicdetect.application.VApplication;

/**
 * Created by zqj on 2016/4/8 14:42.
 */
public class SPUtil {
    private static final String SP_NAME = "login_info";
    private static final String NAME = "name";
    private static final String PASSWD = "password";

    private static final String OPEN_NUM = "open_num";

    public static void saveLoginInfo(String name, String password) {
        SharedPreferences sp = VApplication.getInstance().
                getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NAME, name);
        editor.putString(PASSWD, password);
        editor.apply();
    }

    public static String getName() {
        SharedPreferences sp = VApplication.getInstance().
                getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
        String name = sp.getString(NAME, "");
        return name;
    }

    public static boolean isFirstOpen() {
        SharedPreferences sp = VApplication.getInstance().
                getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
        int openNum = sp.getInt(OPEN_NUM, 0);
        boolean result = true;
        if (openNum == 0) {
            result = true;
        } else {
            result = false;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(OPEN_NUM, openNum + 1);
        editor.apply();
        return result;
    }


}
