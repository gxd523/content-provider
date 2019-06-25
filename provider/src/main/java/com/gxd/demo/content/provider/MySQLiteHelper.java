package com.gxd.demo.content.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guoxiaodong on 2019-06-17 16:03
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    static final String PERSON_TABLE_NAME = "person";
    static final String JOB_TABLE_NAME = "job";

    MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 验证:
     * adb shell
     * su(#)
     * sqlite3 /data/data/com.gxd.demo/databases/person.db
     * .tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PERSON_TABLE_NAME + "(id INTEGER primary key autoincrement,name TEXT,age INTEGER,bmi REAL)");
        db.execSQL("create table " + JOB_TABLE_NAME + "(id INTEGER primary key autoincrement,name TEXT,income INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
