package com.lixinyuyin.monosyllabicdetect.util;

import android.content.Context;
import android.widget.Toast;

import com.lixinyuyin.monosyllabicdetect.application.VApplication;

/**
 * Created by Administrator on 2015/8/14.
 */
public class ToastUtil {
    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String content) {
        Toast.makeText(VApplication.getInstance(), content, Toast.LENGTH_SHORT).show();
    }
}
