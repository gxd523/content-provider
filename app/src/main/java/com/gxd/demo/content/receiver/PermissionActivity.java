package com.gxd.demo.content.receiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends Activity {
    public static final int PERMISSION_RESULT_CODE = 738;
    public static final String EXTRA_PERMISSION_RESULT = "extra_permission_result";
    private static final int PERMISSION_REQUEST_CODE = 813;
    private static final String EXTRA_PERMISSIONS = "extra_permissions";
    private String[] mPermissions;

    public static void startActivityForResult(String[] permissions, Activity activity, int requestCode) {
        Intent intent = new Intent(activity, PermissionActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
        if (mPermissions != null && mPermissions.length > 0) {
            checkPermission(mPermissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    final String permission = permissions[i];
                    if (shouldShowRequestPermissionRationale(permission)) {
                        finishWithResult(false);
                    } else {// 不再询问
                        new AlertDialog.Builder(PermissionActivity.this)
                                .setTitle("您设置了不再询问此权限,应用缺少权限将无法正常运行!")
                                .setMessage("再给你一次开启权限的机会...")
                                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();

                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);

                                        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finishWithResult(!isPermissionDenied(permission));
                                    }
                                }).show();
                        return;
                    }
                }
            }
            finishWithResult(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            checkPermission(mPermissions);
        }
    }

    private void finishWithResult(boolean isAllPermissionsGranted) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PERMISSION_RESULT, isAllPermissionsGranted);
        setResult(PERMISSION_RESULT_CODE, intent);
        finish();
    }

    private void checkPermission(String[] permissions) {
        List<String> deniedPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (isPermissionDenied(permission)) {
                deniedPermissionList.add(permission);
            }
        }
        if (deniedPermissionList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] deniedPermissions = new String[deniedPermissionList.size()];
                deniedPermissionList.toArray(deniedPermissions);
                requestPermissions(deniedPermissions, PERMISSION_REQUEST_CODE);
            }
        } else {
            finishWithResult(true);
        }
    }

    private boolean isPermissionDenied(String permission) {
        return ContextCompat.checkSelfPermission(PermissionActivity.this, permission) != PackageManager.PERMISSION_GRANTED;
    }
}
