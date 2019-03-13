package com.inspect.vehicle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.bean.ResultBean;
import com.inspect.vehicle.bean.UpdateBean;
import com.inspect.vehicle.libs.rxjava.RxProgressSubscriber;
import com.inspect.vehicle.service.DownloadService;
import com.inspect.vehicle.util.PreferUtil;
import com.loyal.kit.DeviceUtil;
import com.loyal.kit.GsonUtil;
import com.loyal.kit.OutUtil;
import com.loyal.rx.RxUtil;
import com.loyal.rx.impl.RxSubscriberListener;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements OnClickListener, RxSubscriberListener<String> {
    @BindView(R.id.account)
    EditText editAccount;
    @BindView(R.id.password)
    EditText editPassWord;
    @BindView(R.id.text_baseUrl)
    AppCompatTextView textBaseUrl;
    @BindView(R.id.account_clear)
    View accountClear;
    @BindView(R.id.password_clear)
    View passwordClear;
    @BindView(R.id.action_register)
    View registerView;
    @BindView(R.id.action_login)
    View loginView;
    private static final int whatLogin = 2;
    private static final int whatUpdate = 4;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void afterOnCreate() {
        textBaseUrl.setOnClickListener(this);
        accountClear.setOnClickListener(this);
        passwordClear.setOnClickListener(this);
        loginView.setOnClickListener(this);
        registerView.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String account = PreferUtil.getString(getApplicationContext(), StrImpl.KEY_ACCOUNT);
        String password = PreferUtil.getString(this, StrImpl.KEY_PASSWORD);
        String baseUrl = PreferUtil.getString(getApplicationContext(), StrImpl.BASEURL, StrImpl.ip_default);
        account = replaceNull(account);
        password = replaceNull(password);
        editAccount.setText(account);
        editAccount.setSelection(account.length());
        editPassWord.setText(password);
        editPassWord.setSelection(password.length());
        if (TextUtils.isEmpty(baseUrl)) {
            textBaseUrl.setText(StrImpl.ip_default);
        } else
            textBaseUrl.setText(baseUrl);

    }

    @Override
    public void onClick(View view) {
        hideKeyBoard(view);
        switch (view.getId()) {
            case R.id.account_clear:
                editAccount.setText("");
                break;
            case R.id.password_clear:
                editPassWord.setText("");
                break;
            case R.id.text_baseUrl:
                startActivityForResultByAct(AddressActivity.class, IntImpl.Loading);
                break;
            case R.id.action_login:
                attr2Login();
                break;
            case R.id.action_register:
                intentBuilder.putExtra("ipAdd", getIpAdd());
                startActivityForResultByAct(RegisterActivity.class, IntImpl.Register);
                break;
        }
    }

    private void attr2Login() {
        String account = editAccount.getText().toString().trim();
        String password = editPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            showToast("请输入用户代号");
            editAccount.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            editPassWord.requestFocus();
            return;
        }
        String serial = DeviceUtil.deviceSerial();
        String apkVersion = DeviceUtil.apkVersion(this);
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
        subscriber.setDialogMessage("登录中，请稍后...").showProgressDialog(true);
        subscriber.setSubscribeListener(this);
        RxUtil.rxExecute(subscriber.login(account, password, serial, apkVersion), subscriber);
    }

    private void onLoginResult(String result) {
        if (TextUtils.isEmpty(result)) {
            showDialog("服务器未返回数据！");
            return;
        }
        ResultBean<String> resultBean = (ResultBean<String>) GsonUtil.json2BeanObject(result, ResultBean.class, String.class);
        if (null == resultBean) {
            showDialog("解析数据失败或数据格式错误");
            return;
        }
        String code = replaceNull(resultBean.getCode());
        String message = replaceNull(resultBean.getMessage());
        if (!TextUtils.equals("1", code)) {
            showDialog(message);
            return;
        }
        String obj = resultBean.getData();
        checkUpdate();
    }

    private void onUpdateResult(String result) {
        if (TextUtils.isEmpty(result)) {
            start2Main();
            return;
        }
        ResultBean<UpdateBean> resultBean = (ResultBean<UpdateBean>) GsonUtil.json2BeanObject(result, ResultBean.class, UpdateBean.class);
        if (null == resultBean) {
            start2Main();
            return;
        }
        String code = resultBean.getCode();
        if (!TextUtils.equals("1", code)) {
            start2Main();
            return;
        }
        UpdateBean updateBean = resultBean.getData();
        String apkUrl = null == updateBean ? "" : replaceNull(updateBean.getDatapath());
        showUpdateDialog(apkUrl);
    }

    private void showUpdateDialog(String apkUrl) {
        apkUrl = replaceNull(apkUrl);
        if (!apkUrl.endsWith(".apk")) {
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
                            DownloadService.startAction(LoginActivity.this, intentBuilder, true);
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

    @Override
    public void onResult(int what, Object tag, String result) {
        try {
            OutUtil.println(String.valueOf(what), result);
            switch (what) {
                case whatLogin:
                    onLoginResult(result);
                    break;
                case whatUpdate:
                    onUpdateResult(result);
                    break;
            }
        } catch (Exception e) {
            onError(what, tag, e);
        }
    }

    @Override
    public void onError(int what, Object tag, Throwable e) {
        showErrorDialog("登录失败", e);
    }

    private void checkUpdate() {
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
        subscriber.setWhat(whatUpdate).setSubscribeListener(this);
        RxUtil.rxExecute(subscriber.checkUpdate("1"), subscriber);
    }

    private void start2Main() {
        String account = editAccount.getText().toString().trim();
        String password = editPassWord.getText().toString().trim();
        account = TextUtils.isEmpty(account) ? "" : account;
        password = TextUtils.isEmpty(password) ? "" : password;
        PreferUtil.putString(getApplicationContext(), StrImpl.KEY_ACCOUNT, account);
        PreferUtil.putString(this, StrImpl.KEY_PASSWORD, password);
        startActivityByAct(MainActivity.class);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case IntImpl.Loading:
                textBaseUrl.setText(getIpAdd());
                break;
            case IntImpl.Register:
                if (data != null) {
                    String account = data.getStringExtra("account");
                    editAccount.setText(replaceNull(account));
                    editPassWord.setText("");
                    PreferUtil.putString(getApplicationContext(), StrImpl.KEY_ACCOUNT, replaceNull(account));
                    editAccount.setSelection(replaceNull(account).length());
                }
                break;
        }
    }
}