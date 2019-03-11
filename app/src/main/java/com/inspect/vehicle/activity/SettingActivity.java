package com.inspect.vehicle.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.bean.ResultBean;
import com.inspect.vehicle.bean.UpdateBean;
import com.inspect.vehicle.libs.rxjava.RxProgressSubscriber;
import com.inspect.vehicle.service.DownloadService;
import com.loyal.kit.DeviceUtil;
import com.loyal.kit.GsonUtil;
import com.loyal.rx.RxUtil;
import com.loyal.rx.impl.RxSubscriberListener;

import butterknife.BindView;

public class SettingActivity extends BaseActivity implements View.OnClickListener, RxSubscriberListener<String> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.server)
    View server;
    @BindView(R.id.checkUpdate)
    View checkUpdate;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    public void afterOnCreate() {
        toolbar.setTitle(R.string.setting);
        setSupportActionBar(toolbar);
        version.setOnClickListener(this);
        server.setOnClickListener(this);
        checkUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version:
                showToast(DeviceUtil.apkVersion(this));
                break;
            case R.id.server:
                startActivityByAct(AddressActivity.class);
                break;
            case R.id.checkUpdate:
                checkUpdate();
                break;
        }
    }

    private void checkUpdate() {
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
        subscriber.showProgressDialog(true).setDialogMessage("检查更新中...");
        subscriber.setSubscribeListener(this);
        RxUtil.rxExecute(subscriber.checkUpdate("2"), subscriber);
    }

    @Override
    public void onResult(int what, Object tag, String result) {
        try {
            ResultBean<UpdateBean> resultBean = (ResultBean<UpdateBean>) GsonUtil.json2BeanObject(result, ResultBean.class, UpdateBean.class);
            if (null == resultBean || !TextUtils.equals("1", resultBean.getCode())) {
                showDialog("暂未发现新版本！");
                return;
            }
            UpdateBean updateBean = resultBean.getData();
            String apkUrl = null == updateBean ? "" : replaceNull(updateBean.getDatapath());
            showUpdateDialog(apkUrl);
        } catch (Exception e) {
            e.printStackTrace();
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
                        if (!TextUtils.equals(State.UPDATE_ING, State.UPDATE)) {
                            State.UPDATE = State.UPDATE_ING;
                            DownloadService.startAction(SettingActivity.this, intentBuilder, true);
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
