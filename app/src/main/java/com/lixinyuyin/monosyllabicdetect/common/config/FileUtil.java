package com.lixinyuyin.monosyllabicdetect.common.config;

import android.content.Context;

import com.lixinyuyin.monosyllabicdetect.R;
import com.lixinyuyin.monosyllabicdetect.application.VApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zqj on 2016/4/14 14:43.
 */
public class FileUtil {
    public static void generateDefaultConfig(Context context) {
        File file = new File(VApplication.PARAM_FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(context.getResources().openRawResource(R.raw.saved_par));
            out = new BufferedOutputStream(new FileOutputStream(file));
            int hasRead = 0;
            byte b[] = new byte[1024];
            while ((hasRead = in.read(b)) > 0) {
                out.write(b, 0, hasRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
