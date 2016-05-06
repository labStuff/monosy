package com.lixinyuyin.monosyllabicdetect.common;

import android.os.Environment;

import com.lixinyuyin.monosyllabicdetect.model.VAccount;
import com.lixinyuyin.monosyllabicdetect.util.ToastUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zqj on 2016/3/22 09:50.
 */
public class MyDate {
    private boolean mBoolean;
    private int mInt;
    private double mDouble;
    private int[] mIntArray;


    public static final String FILE_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "monosy";

    public boolean saveConfig() throws IOException {
        File file = new File(FILE_DIR_PATH);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ToastUtil.toast("新建路径失败，请检查是否存在SD卡");
                return false;
            }
        }
        String filePath = FILE_DIR_PATH + File.separator + VAccount.getUserName().hashCode() + "data.ace";
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(filePath)));
        out.writeBoolean(mBoolean);
        out.writeInt(mInt);
        out.writeDouble(mDouble);
        out.writeInt(mIntArray.length);
        for (int i = 0; i < mIntArray.length; i++) {
            out.writeInt(mIntArray[i]);
        }
        out.close();
        return true;
    }

    public static MyDate getSavedConfig() throws IOException {
        String filePath = FILE_DIR_PATH + File.separator + VAccount.getUserName().hashCode() + "data.ace";
        DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(filePath)));
        MyDate date = new MyDate();
        date.mBoolean = in.readBoolean();
        date.mInt = in.readInt();
        date.mDouble = in.readDouble();
        date.mIntArray = new int[in.readInt()];
        for (int i = 0; i < date.mIntArray.length; i++) {
            date.mIntArray[i] = in.readInt();
        }
        in.close();
        return date;
    }

    public void setBoolean(boolean mBoolean) {
        this.mBoolean = mBoolean;
    }

    public boolean getBoolean() {
        return mBoolean;
    }

    public int getInt() {
        return mInt;
    }

    public void setInt(int mInt) {
        this.mInt = mInt;
    }

    public double getDouble() {
        return mDouble;
    }

    public void setDouble(double mDouble) {
        this.mDouble = mDouble;
    }

    public int[] getIntArray() {
        return mIntArray;
    }

    public void setIntArray(int[] mIntArray) {
        this.mIntArray = mIntArray;
    }


}
