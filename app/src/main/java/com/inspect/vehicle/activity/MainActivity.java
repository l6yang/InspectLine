package com.inspect.vehicle.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.inspect.vehicle.R;
import com.inspect.vehicle.adapter.LinkAdapter;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.bean.LinkBean;
import com.inspect.vehicle.bean.ResultBean;
import com.inspect.vehicle.bean.StartRecordBean;
import com.inspect.vehicle.bean.UpdateBean;
import com.inspect.vehicle.libs.rxjava.RxProgressSubscriber;
import com.inspect.vehicle.service.DownloadService;
import com.loyal.kit.GsonUtil;
import com.loyal.kit.TimeUtil;
import com.loyal.rx.RxUtil;
import com.loyal.rx.impl.RxSubscriberListener;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, RxSubscriberListener<String> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gridView)
    GridView gridView;
    private static final int whatUpdate = 2;
    private static final int whatSend = 4;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void afterOnCreate() {
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        setActionBack(false);
        gridView.setAdapter(new LinkAdapter(this, "json/link.json", true));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            /*LinkBean linkBean = (LinkBean) parent.getAdapter().getItem(position);
            String packageName = replaceNull(linkBean.getPackName());
            String actUrl = replaceNull(linkBean.getActUrl());
            //packageName = "com.inspect.vehicle";
            //actUrl=".activity.SplashActivity";
            actUrl = actUrl.startsWith(".") ? actUrl.substring(1) : actUrl;
            ComponentName component = new ComponentName(packageName, String.format("%s.%s", packageName, actUrl));
            Intent intentJump = new Intent("android.intent.action.VIEW");
            intentJump.putExtra("idNumber", "513826199707088933");
            intentJump.putExtra("userID", "admin");
            intentJump.putExtra("cylsh", "1170701111111");
            intentJump.putExtra("keystr", "1DSD0EDB00012112");
            intentJump.setComponent(component);
            startActivityForResult(intentJump, 16);*/
            RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
            subscriber.setWhat(whatSend).setDialogMessage("doing").showProgressDialog(true);
            System.out.println(subscriber.baseUrl(getIpAdd()));
            subscriber.setSubscribeListener(this);
            StartRecordBean recordBean = new StartRecordBean();
            recordBean.setClsbdh("LSB12345678912346");
            recordBean.setCycs("1");
            recordBean.setCylsh("LSH111111111111");
            recordBean.setCyqtd("1");
            recordBean.setCyqxh("1");
            recordBean.setCysj(TimeUtil.getDateTime());
            switch (position) {
                case 0:
                    recordBean.setCllx("0");
                    RxUtil.rxExecute(subscriber.sendMq(GsonUtil.bean2Json(recordBean)), subscriber);
                    break;
                case 1:
                    recordBean.setCllx("1");
                    RxUtil.rxExecute(subscriber.sendMq(GsonUtil.bean2Json(recordBean)), subscriber);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                startActivityByAct(SettingActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        checkUpdate();
    }

    private void checkUpdate() {
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
        subscriber.setWhat(whatUpdate).setSubscribeListener(this);
        RxUtil.rxExecute(subscriber.checkUpdate("2"), subscriber);
    }

    @Override
    public void onResult(int what, Object tag, String result) {
        try {
            switch (what) {
                case whatUpdate:
                    if (TextUtils.isEmpty(result)) {
                        return;
                    }
                    ResultBean<UpdateBean> resultBean = (ResultBean<UpdateBean>) GsonUtil.json2BeanObject(result, ResultBean.class, UpdateBean.class);
                    if (null == resultBean) {
                        return;
                    }
                    String code = resultBean.getCode();
                    if (!TextUtils.equals("1", code)) {
                        return;
                    }
                    UpdateBean updateBean = resultBean.getData();
                    String apkUrl = null == updateBean ? "" : replaceNull(updateBean.getDatapath());
                    showUpdateDialog(apkUrl);
                    break;
                case whatSend:
                    System.out.println("sendMq--" + result);
                    break;
            }
        } catch (Exception e) {
            onError(what, tag, e);
        }
    }

    @Override
    public void onError(int what, Object tag, Throwable e) {
        switch (what) {
            case whatUpdate:
                break;
            case whatSend:
                showErrorDialog("", e);
                break;
        }
    }

    private void showUpdateDialog(String apkUrl) {
        apkUrl = replaceNull(apkUrl);
        if (!apkUrl.endsWith(".apk")) {
            return;
        }
        intentBuilder.putExtra("apkUrl", apkUrl);
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("检测到新版本").setMessage("是否下载安装？");
        normalDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showToast("已推送至状态栏下载更新");
                if (!TextUtils.equals(State.UPDATE_ING, State.UPDATE)) {
                    State.UPDATE = State.UPDATE_ING;
                    DownloadService.startAction(MainActivity.this, intentBuilder, true);
                }
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
