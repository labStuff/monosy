package com.lixinyuyin.monosyllabicdetect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lixinyuyin.monosyllabicdetect.database.graphdata.GraphTable;
import com.lixinyuyin.monosyllabicdetect.database.userdata.UserTable;

/**
 * Created by Administrator on 2015/7/29.
 */
public class DBSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mono_detect.db";
    public final static int DATABASE_VERSION = 1;

    public DBSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GraphTable.CREATE_TABLE);
        db.execSQL(UserTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
