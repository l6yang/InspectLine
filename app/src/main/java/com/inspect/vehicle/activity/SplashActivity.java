package com.inspect.vehicle.activity;

import android.content.Intent;
import android.os.Handler;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.util.FileUtil;
import com.inspect.vehicle.util.HandlerUtil;
import com.loyal.base.ui.activity.ABasicPerMissionActivity;

import java.io.File;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends BaseActivity implements ABasicPerMissionActivity.onItemPermissionListener {

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public void afterOnCreate() {
        requestPermission(IntImpl.MEMORY, this, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onItemPermissionResult(int reqCode, boolean successful) {
        if (successful) {
            File apkFile = new File(Path.APK, StrImpl.APK_NAME);
            FileUtil.deleteFile(apkFile);
            FileUtil.createFileSys();
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            showToast("请开启存储权限！");
            HandlerUtil.postDelayed(new Handler(), new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    }
}
