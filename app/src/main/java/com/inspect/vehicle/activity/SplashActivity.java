package com.inspect.vehicle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseFullScreenActivity;
import com.inspect.vehicle.util.FileUtil;
import com.loyal.base.impl.CommandViewClickListener;
import com.loyal.rx.impl.MultiplePermissionsListener;

import java.io.File;

public class SplashActivity extends BaseFullScreenActivity implements MultiplePermissionsListener {
    private StringBuilder builder = new StringBuilder();

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public void afterOnCreate() {
        multiplePermissions(this, PerMission.READ_PHONE_STATE,
                PerMission.WRITE_EXTERNAL_STORAGE, PerMission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onMultiplePermissions(@NonNull String permissionName, boolean successful, boolean shouldShow) {
        permissionName = replaceNull(permissionName);
        System.out.println(permissionName + "：" + successful);
        if (!successful) {
            switch (permissionName) {
                case PerMission.READ_PHONE_STATE:
                    builder.append("手机状态权限、");
                    break;
                case PerMission.READ_EXTERNAL_STORAGE:
                    builder.append("存储权限、");
                    break;
            }
        }
        if (!TextUtils.isEmpty(builder)) {
            showPermissionNextDialog("请开启" + builder.toString(), new CommandViewClickListener() {
                @Override
                public void onViewClick(DialogInterface dialog, View view, Object tag) {
                    if (null != dialog)
                        dialog.dismiss();
                    startActivity(new Intent(Settings.ACTION_SETTINGS)); //直接进入手机中设置界面
                    finish();
                }
            }, false);
            return;
        }
        switch (permissionName) {
            case PerMission.READ_PHONE_STATE:
                break;
            case PerMission.READ_EXTERNAL_STORAGE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                    if (!hasInstallPermission) {
                        showPermissionNextDialog("请勾选安装应用权限", new CommandViewClickListener() {
                            @Override
                            public void onViewClick(DialogInterface dialog, View view, Object tag) {
                                //注意这个是8.0新API
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);
                                finish();
                                /*Intent mIntent = new Intent();
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (Build.VERSION.SDK_INT >= 9) {
                                    mIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    mIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                } else if (Build.VERSION.SDK_INT <= 8) {
                                    mIntent.setAction(Intent.ACTION_VIEW);
                                    mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                                    mIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                                }
                                startActivityForResult(mIntent, IntImpl.reqInstall);*/
                            }
                        }, false);
                        return;
                    }
                    start2Next();
                } else {
                    start2Next();
                }
                break;
        }
    }

    private void start2Next() {
        File apkFile = new File(Path.APK, StrImpl.APK_NAME);
        FileUtil.deleteFile(apkFile);
        FileUtil.createFileSys();
        Intent intent = getIntent();
        System.out.println(null == intent);
        //hasIntentParams(true);
        startActivityByAct(MainActivity.class);
        finish();
    }
}
