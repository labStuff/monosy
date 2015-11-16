package com.lixinyuyin.monosyllabicdetect.database.userdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lixinyuyin.monosyllabicdetect.database.DBSQLiteOpenHelper;

/**
 * Created by Administrator on 2015/8/14.
 */
public class UserDao {
    private DBSQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDataBase;

    public UserDao(Context context) {
        mOpenHelper = new DBSQLiteOpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public boolean isExists(String name) {
        Cursor cursor = mDataBase.query(UserTable.TABLE_NAME, null, UserTable.COLUMN_NAME + "=?"
                , new String[]{name}, null, null, null);
        int count = 0;
        count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public void add(String name, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.COLUMN_NAME, name);
        contentValues.put(UserTable.COLUMN_PASSWORD, password);
        mDataBase.insert(UserTable.TABLE_NAME, null, contentValues);
    }

    public boolean isValid(String name, String password) {
        Cursor cursor = mDataBase.query(UserTable.TABLE_NAME, null, UserTable.COLUMN_NAME + "=?"
                , new String[]{name}, null, null, null);
        if (cursor.getCount() <= 0)
            return false;
        cursor.moveToFirst();
        String realPassword = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_PASSWORD));
        return realPassword.equals(password);
    }

    public void deleteAll() {
        mDataBase.delete(UserTable.TABLE_NAME, null, null);
    }

    public void close() {
        mDataBase.close();
    }
}
