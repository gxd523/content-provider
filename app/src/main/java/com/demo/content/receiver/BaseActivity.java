package com.demo.content.receiver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by guoxiaodong on 2019-10-24 09:37
 */
public abstract class BaseActivity extends Activity {
    private static final int MAIN_REQUEST_CODE = 336;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionActivity.startActivityForResult(new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, BaseActivity.this, MAIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAIN_REQUEST_CODE && resultCode == PermissionActivity.PERMISSION_RESULT_CODE) {
            boolean isAllPermissionsGranted = data.getBooleanExtra(PermissionActivity.EXTRA_PERMISSION_RESULT, false);
            if (isAllPermissionsGranted) {
                onPermissionGranted();
            } else {
                Toast.makeText(this, "权限获取失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected abstract void onPermissionGranted();
}
