package com.inspect.vehicle.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.inspect.vehicle.R;
import com.inspect.vehicle.adapter.LinkAdapter;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.bean.LinkBean;
import com.inspect.vehicle.bean.ResultBean;
import com.inspect.vehicle.libs.rxjava.RxProgressSubscriber;
import com.inspect.vehicle.service.DownloadService;
import com.loyal.kit.DeviceUtil;
import com.loyal.kit.GsonUtil;
import com.loyal.rx.RxUtil;
import com.loyal.rx.impl.RxSubscriberListener;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, RxSubscriberListener<String> {
    @BindView(R.id.gridView)
    GridView gridView;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void afterOnCreate() {
        setActionBack(false);
        gridView.setAdapter(new LinkAdapter(this, "json/link.json", true));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            LinkBean linkBean = (LinkBean) parent.getAdapter().getItem(position);
            String packageName = replaceNull(linkBean.getPackName());
            String actUrl = replaceNull(linkBean.getActUrl());
            //packageName = "com.inspect.vehicle";
            //actUrl=".activity.SplashActivity";
            actUrl = actUrl.startsWith(".") ? actUrl.substring(1) : actUrl;
            String flag = replaceNull(linkBean.getFlag());
            if (TextUtils.equals("checkUpdate", flag)) {
                checkUpdate();
                return;
            }
            ComponentName component = new ComponentName(packageName, String.format("%s.%s", packageName, actUrl));
            Intent intentJump = new Intent("android.intent.action.VIEW");
            intentJump.putExtra("idNumber", "513826199707088933");
            intentJump.putExtra("userID", "admin");
            intentJump.putExtra("cylsh", "1170701111111");
            intentJump.putExtra("keystr", "1DSD0EDB00012112");
            intentJump.setComponent(component);
            startActivityForResult(intentJump, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkUpdate() {
        String ipAdd = "192.168.20.96:8080";
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, ipAdd);
        subscriber.setSubscribeListener(this);
        RxUtil.rxExecute(subscriber.checkUpdate(DeviceUtil.apkVersion(this)), subscriber);
    }

    @Override
    public void onResult(int what, Object tag, String result) {
        try {
            ResultBean<String> resultBean = (ResultBean<String>) GsonUtil.json2BeanObject(result, ResultBean.class, String.class);
            if (null == resultBean || !TextUtils.equals("1", resultBean.getCode())) {
                showDialog("暂未发现新版本！");
                return;
            }
            String apkUrl = replaceNull(resultBean.getObj());
            showUpdateDialog(apkUrl);
        } catch (Exception e) {
            onError(what, tag, e);
        }
    }

    @Override
    public void onError(int what, Object tag, Throwable e) {
        showErrorDialog("检查更新失败", e);
    }

    private void showUpdateDialog(String apkUrl) {
        apkUrl = replaceNull(apkUrl);
        if (!apkUrl.endsWith(".apk")) {
            showDialog("无效的更新下载地址");
            return;
        }
        intentBuilder.putExtra("apkUrl", apkUrl);
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("检测到新版本").setMessage("是否下载安装？");
        normalDialog.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showToast("已推送至状态栏下载更新");
                        DownloadService.startAction(MainActivity.this, intentBuilder, true);
                    }
                });
        normalDialog.setNeutralButton("下次再说",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // 创建实例并显示
        normalDialog.show();
    }
}
