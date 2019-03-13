package com.inspect.vehicle.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.util.PreferUtil;

import butterknife.BindView;

//服务器地址设置页面
public class AddressActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_ipAdd)
    AppCompatEditText editIpAdd;
    @BindView(R.id.edit_port)
    AppCompatEditText editPort;
    @BindView(R.id.submitView)
    View pubSubmit;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_address;
    }

    @Override
    public void afterOnCreate() {
        toolbar.setTitle("接入地址设置");
        setSupportActionBar(toolbar);
        pubSubmit.setOnClickListener(this);
        editPort.setHint("填写范围：0～65535");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String ipAdd = PreferUtil.getString(this, "editIpAdd", StrImpl.ip_default);
        String port = PreferUtil.getString(this, "editPort", "");
        editIpAdd.setText(ipAdd);
        editIpAdd.setSelection(ipAdd.length());
        editPort.setText(port);
        editPort.setSelection(port.length());
    }

    @Override
    public void onClick(View view) {
        hideKeyBoard(view);
        switch (view.getId()) {
            case R.id.submitView:
                editPort.setError(null);
                Editable ipEdit = editIpAdd.getText();
                Editable portEdit = editPort.getText();
                if (TextUtils.isEmpty(ipEdit)) {
                    showToast("Ip地址不能为空");
                    return;
                }
                String ip = ipEdit.toString().trim();
                PreferUtil.putString(this, "editIpAdd", ip);
                String ipAdd;
                if (TextUtils.isEmpty(portEdit)) {
                    ipAdd = ip;
                } else {
                    String port = portEdit.toString().trim();
                    if (!getPort(port)) {
                        editPort.requestFocus();
                        editPort.setError("端口号填写范围：0～65535");
                        showToast("端口号填写范围：0～65535");
                        return;
                    }
                    ipAdd = ip + ":" + port;
                    PreferUtil.putString(this, "editPort", port);
                }
                PreferUtil.putString(getApplicationContext(), StrImpl.BASEURL, ipAdd);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    private boolean getPort(String port) {
        try {
            int portInt = Integer.valueOf(port);
            return (portInt >= 0 && portInt <= 65535);
        } catch (Exception e) {
            return false;
        }
    }
}
