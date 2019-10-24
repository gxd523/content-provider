package com.gxd.demo.content.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by guoxiaodong on 2019-06-25 16:03
 */
public class MyProvider extends ContentProvider {
    private static final String AUTHORITY = "com.gxd.demo.content.provider";
    private static final int PersonCode = 387;
    private static final int JobCode = 77;
    private UriMatcher mUriMatcher;
    private SQLiteDatabase mReadableDatabase;

    @Override
    public boolean onCreate() {
        mReadableDatabase = new MySQLiteHelper(getContext(), "demo.db", null, 1).getReadableDatabase();

        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, MySQLiteHelper.PERSON_TABLE_NAME, PersonCode);
        mUriMatcher.addURI(AUTHORITY, MySQLiteHelper.JOB_TABLE_NAME, JobCode);
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d("gd", "query-->" + uri);
        String tableName = getTableName(uri);
        return mReadableDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    /**
     * 返回当前Url所代表数据的MIME类型
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d("gd", "insert-->" + uri + "..." + values);
        String tableName = getTableName(uri);
        mReadableDatabase.insert(tableName, null, values);
        Context context = getContext();
        if (context != null) {// 当该URI的ContentProvider数据发生变化时，通知外界（即访问该ContentProvider数据的访问者）
            context.getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * 根据URI_CODE，从而匹配ContentProvider中相应的表名
     */
    private String getTableName(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case PersonCode:
                return MySQLiteHelper.PERSON_TABLE_NAME;
            case JobCode:
                return MySQLiteHelper.JOB_TABLE_NAME;
        }
        return null;
    }
}
