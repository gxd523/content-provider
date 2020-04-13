package com.demo.content.receiver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            readContacts();
        }

        ContentResolver contentResolver = getContentResolver();
        Uri personParse = Uri.parse("content://com.demo.content.provider/person");
        contentResolver.registerContentObserver(personParse, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Log.d("gxd", "onChange-->" + uri.toString());
            }
        });

        insertData(contentResolver, personParse);
        queryData(contentResolver, personParse);
    }

    private void queryData(ContentResolver contentResolver, Uri personParse) {
        Cursor cursor = contentResolver.query(personParse, new String[]{"name", "age", "bmi"}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("gxd", "query result-->" + cursor.getString(0) + "...." + cursor.getInt(1) + "..." + cursor.getDouble(2));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void insertData(ContentResolver contentResolver, Uri personParse) {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put("name", "guoxiaodong");
        contentValues.put("age", "28");
        contentValues.put("bmi", 20.2);
        contentResolver.insert(personParse, contentValues);

        contentValues.clear();
        contentValues.put("name", "Lucy");
        contentValues.put("age", "23");
        contentValues.put("bmi", 19.3);
        contentResolver.insert(personParse, contentValues);

        contentValues.clear();
        contentValues.put("name", "Jerry");
        contentValues.put("age", "25");
        contentValues.put("bmi", 23.3);
        contentResolver.insert(personParse, contentValues);
    }

    private void readContacts() {
        ContentResolver contentResolver = getContentResolver();
        Log.d("gxd", "MainActivity.readContacts-->" + ContactsContract.Contacts.CONTENT_URI);
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor cursor1 = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                    null,
                    null
            );
            while (cursor1 != null && cursor1.moveToNext()) {
                String phone = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d("gxd", name + ":" + phone);
            }
            if (cursor1 != null) {
                cursor1.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    protected void onPermissionGranted() {
        readContacts();
    }
}
