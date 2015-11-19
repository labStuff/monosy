package com.lixinyuyin.monosyllabicdetect.util;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.view.status.SystemBarTintManager;

/**
 * Created by zqj on 2015/11/19 15:08.
 */
public class StatusBarUtil {

    public static void setStatusBarColor(Activity activity) {
        setSystemBar(activity, R.color.blue_dark);
    }

    private static void setSystemBar(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.setAttributes(params);
            SystemBarTintManager manager = new SystemBarTintManager(activity);
            manager.setStatusBarTintEnabled(true);
            manager.setTintColor(activity.getResources().getColor(colorId));
        }
    }
}
