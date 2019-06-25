package com.gxd.demo.content.receiver;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int MAIN_REQUEST_CODE = 336;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionActivity.startActivityForResult(new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, MainActivity.this, MAIN_REQUEST_CODE);
        } else {
            readContacts();
        }

        Uri personParse = Uri.parse("content://com.gxd.demo.content.provider/person");
        ContentValues contentValues = new ContentValues();
        ContentResolver contentResolver = getContentResolver();

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

        Cursor cursor = contentResolver.query(personParse, new String[]{"name", "age", "bmi"}, "age>23", null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("gxd", "query result-->" + cursor.getString(0) + "...." + cursor.getInt(1) + "..." + cursor.getDouble(2));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAIN_REQUEST_CODE && resultCode == PermissionActivity.PERMISSION_RESULT_CODE) {
            boolean isAllPermissionsGranted = data.getBooleanExtra(PermissionActivity.EXTRA_PERMISSION_RESULT, false);
            if (isAllPermissionsGranted) {
                readContacts();
            } else {
                Toast.makeText(this, "权限获取失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readContacts() {
        ContentResolver contentResolver = getContentResolver();
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
}
