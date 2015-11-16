package com.lixinyuyin.monosyllabicdetect.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lixinyuyin.monosyllabicdetect.application.VApplication;

/**
 * Created by zqj on 2015/8/24 09:36.
 */
public class VAccount {
    private String mName;
    private String mPassword;

    private final static String SP_NAME = "com.lixinyuyin.monosyllabicdetect";
    private final static String KEY_NAME = "key_name";
    private final static String KEY_PASSWORD = "key_password";

    public VAccount(String name, String password) {
        mName = name;
        mPassword = password;
    }

    public static void saveAccount(VAccount account) {
        SharedPreferences sPreferences = VApplication.getInstance().
                getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putString(KEY_NAME, account.mName);
        editor.putString(KEY_PASSWORD, account.mPassword);
        editor.commit();
    }

    public static void clear() {
        SharedPreferences sPreferences = VApplication.getInstance().
                getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean hasAccount() {
        SharedPreferences sPreferences = VApplication.getInstance().
                getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return !TextUtils.isEmpty(sPreferences.getString(KEY_NAME, ""));
    }

    public static String getUserName() {
        SharedPreferences sPreferences = VApplication.getInstance().
                getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sPreferences.getString(KEY_NAME, "");
    }


}
