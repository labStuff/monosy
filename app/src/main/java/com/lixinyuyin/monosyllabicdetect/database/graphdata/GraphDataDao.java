package com.lixinyuyin.monosyllabicdetect.database.graphdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lixinyuyin.monosyllabicdetect.database.DBSQLiteOpenHelper;
import com.lixinyuyin.monosyllabicdetect.view.GraphData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/29.
 */
public class GraphDataDao {
    private DBSQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;

    public GraphDataDao(Context context) {
        mOpenHelper = new DBSQLiteOpenHelper(context);
        mDatabase = mOpenHelper.getWritableDatabase();
    }

    public void add(GraphData data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GraphTable.COLUMN_DATE, String.valueOf(data.getDate()));
        contentValues.put(GraphTable.COLUMN_NAME, data.getName());
        contentValues.put(GraphTable.COLUMN_MIN_DATA, data.getMinDataString());
        contentValues.put(GraphTable.COLUMN_MEDIUM_DATA, data.getMediumDataString());
        contentValues.put(GraphTable.COLUMN_MAX_DATA, data.getMaxDataString());
        mDatabase.insert(GraphTable.TABLE_NAME, null, contentValues);
    }

    public void deleteByDate(String date) {
        mDatabase.delete(GraphTable.TABLE_NAME, GraphTable.COLUMN_DATE + "=?", new String[]{date});
    }

    public List<GraphData> getAll(String name) {
        List<GraphData> result = new ArrayList<GraphData>();
        Cursor cursor = mDatabase.query(GraphTable.TABLE_NAME, null, GraphTable.COLUMN_NAME + "=?", new String[]{name},
                null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                long date = Long.parseLong(cursor.getString(cursor.getColumnIndex(GraphTable.COLUMN_DATE)));
                int[] mindata = GraphData.stringToArray(cursor.getString(cursor.getColumnIndex(GraphTable.COLUMN_MIN_DATA)));
                int[] mediumdata = GraphData.stringToArray(cursor.getString(cursor.getColumnIndex(GraphTable.COLUMN_MEDIUM_DATA)));
                int[] maxdata = GraphData.stringToArray(cursor.getString(cursor.getColumnIndex(GraphTable.COLUMN_MAX_DATA)));
                GraphData graphData = new GraphData(name, mindata, mediumdata, maxdata, date);
                result.add(graphData);
            }
        }
        return result;
    }

    public void close() {
        if (null != mDatabase) {
            mDatabase.close();
        }
    }

}
