package com.lixinyuyin.monosyllabicdetect.database.userdata;

/**
 * Created by Administrator on 2015/8/14.
 */
public class UserTable {
    public final static String TABLE_NAME = "user_table";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_PASSWORD = "password";

    public final static String CREATE_TABLE = "create table if not exists " + TABLE_NAME
            + "(" + COLUMN_NAME + " text," + COLUMN_PASSWORD + " text)";
}
