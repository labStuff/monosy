package com.lixinyuyin.monosyllabicdetect.database.graphdata;

/**
 * Created by Administrator on 2015/7/29.
 */
public class GraphTable {
    public final static String TABLE_NAME = "graphdata_table";
    public final static String COLUMN_DATE = "date";
    public final static String COLUMN_NAME = "username";
    public final static String COLUMN_MIN_DATA = "min_data";
    public final static String COLUMN_MEDIUM_DATA = "medium_data";
    public final static String COLUMN_MAX_DATA = "max_data";

    public final static String CREATE_TABLE = "create table if not exists " + TABLE_NAME +
            "(" + COLUMN_DATE + " text," + COLUMN_NAME + " text," + COLUMN_MIN_DATA + " text," + COLUMN_MEDIUM_DATA + " text," +
            COLUMN_MAX_DATA + " text)";
}
