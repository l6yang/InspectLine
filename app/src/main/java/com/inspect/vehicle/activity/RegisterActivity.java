package com.inspect.vehicle.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.inspect.vehicle.R;
import com.inspect.vehicle.adapter.RegSybmAdapter;
import com.inspect.vehicle.adapter.RegSyryAdapter;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.base.SybmSyryBean;
import com.inspect.vehicle.bean.ResultBean;
import com.inspect.vehicle.bean.SubmitBean;
import com.inspect.vehicle.libs.rxjava.RxProgressSubscriber;
import com.loyal.base.impl.CommandViewClickListener;
import com.loyal.base.impl.IBaseContacts;
import com.loyal.kit.DeviceUtil;
import com.loyal.kit.GsonUtil;
import com.loyal.kit.OutUtil;
import com.loyal.rx.RxUtil;
import com.loyal.rx.impl.RxSubscriberListener;

import java.util.List;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, RxSubscriberListener<String>, View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_sbbh)
    TextView textSbbh;
    @BindView(R.id.spin_sybm_1st)
    Spinner spinSybm1st;
    @BindView(R.id.spin_sybm_2nd)
    Spinner spinSybm2nd;
    @BindView(R.id.spin_syry)
    Spinner spinSyry;
    @BindView(R.id.edit_cjzbh)
    EditText editCjzbh;
    @BindView(R.id.submitView)
    View submitView;
    private RegSybmAdapter sybm1stAdapter;
    private RegSybmAdapter sybm2ndAdapter;
    private RegSyryAdapter syryAdapter;
    private static final int sybm1st = 2, sybm2nd = 4, syry = 8, submit = 9;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    public void afterOnCreate() {
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        String sbbh = DeviceUtil.deviceSerial();
        textSbbh.setText(sbbh);
        spinSybm1st.setAdapter(sybm1stAdapter = new RegSybmAdapter(this));
        spinSybm2nd.setAdapter(sybm2ndAdapter = new RegSybmAdapter(this));
        spinSyry.setAdapter(syryAdapter = new RegSyryAdapter(this));
        spinSybm1st.setOnItemSelectedListener(this);
        spinSybm2nd.setOnItemSelectedListener(this);
        submitView.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        request1st();
    }

    /*使用部门1*/
    private void request1st() {
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
        subscriber.showProgressDialog(true).setSubscribeListener(this);
        subscriber.setWhat(sybm1st).setDialogMessage("获取数据中...");
        RxUtil.rxExecute(subscriber.getGlbm("1"), subscriber);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitView:
                //注册成功之后 在本地写一个文件
                doRegister();
                break;
        }
    }

    private void doRegister() {
        final String glbm = getSpinSelectStr(spinSybm2nd, "getGlbm");
        if (TextUtils.isEmpty(glbm)) {
            showDialog("请选择使用部门！");
            return;
        }
        final String syry = getSpinSelectStr(spinSyry, "getYhdh");
        if (TextUtils.isEmpty(syry)) {
            showDialog("请选择使用人员！");
            return;
        }
       final String cjzbh = editCjzbh.getText().toString().trim();
        if (TextUtils.isEmpty(cjzbh)) {
            showToast("请填写采集站编号");
            editCjzbh.requestFocus();
            return;
        }
        showPermissionDialog(String.format("是否使用警号为\"%s\"进行注册？", syry), IBaseContacts.TypeImpl.DOUBLE,
                new String[]{"重新选择", "确定使用"},
                new CommandViewClickListener() {
                    @Override
                    public void onViewClick(DialogInterface dialog, View view, Object tag) {
                        if (null != dialog)
                            dialog.dismiss();
                        switch (view.getId()) {
                            case R.id.btn_cmd_next:
                                RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(RegisterActivity.this, getIpAdd());
                                subscriber.showProgressDialog(true).setSubscribeListener(RegisterActivity.this);
                                subscriber.setWhat(submit).setTag(syry).setDialogMessage("注册中...");

                                SubmitBean submitBean = new SubmitBean();
                                String xlh = DeviceUtil.deviceSerial();
                                submitBean.setJlybh(xlh);
                                submitBean.setCjzbh(cjzbh);//采集站编号
                                submitBean.setJlyxh(DeviceUtil.deviceModel());//记录仪型号
                                submitBean.setJlymc(DeviceUtil.deviceBrand());//记录仪名称
                                submitBean.setBdmj(syry);//绑定民警
                                submitBean.setGlbm(glbm);//管理部门
                                submitBean.setSyglbm(glbm);//使用管理部门
                                submitBean.setNbm(xlh);//内部码
                                submitBean.setSbrl("");//设备容量
                                submitBean.setSbcs(Build.MANUFACTURER);//设备厂商
                                submitBean.setPid(Build.DEVICE);//设备PID
                                submitBean.setVid(Build.PRODUCT);//设备VID
                                RxUtil.rxExecute(subscriber.register(GsonUtil.bean2Json(submitBean)), subscriber);
                        }
                    }
                }, false);
    }

    /*使用部门2*/
    private void request2st(String glbm) {
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
        subscriber.showProgressDialog(true).setSubscribeListener(this);
        subscriber.setWhat(sybm2nd).setDialogMessage("获取数据中...");
        RxUtil.rxExecute(subscriber.getGlbm(glbm), subscriber);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_sybm_1st: {
                sybm2ndAdapter.notifyList();
                syryAdapter.notifyList();
                SybmSyryBean sybmSyryBean = (SybmSyryBean) parent.getAdapter().getItem(position);
                String glbm = null == sybmSyryBean ? "" : replaceNull(sybmSyryBean.getGlbm());
                request2st(glbm);
            }
            break;
            case R.id.spin_sybm_2nd: {
                syryAdapter.notifyList();
                SybmSyryBean sybmSyryBean = (SybmSyryBean) parent.getAdapter().getItem(position);
                String glbm = null == sybmSyryBean ? "" : replaceNull(sybmSyryBean.getGlbm());
                requestSyry(glbm);
            }
            break;
        }
    }

    /*获取使用人员*/
    private void requestSyry(String glbm) {
        RxProgressSubscriber<String> subscriber = new RxProgressSubscriber<>(this, getIpAdd());
        subscriber.showProgressDialog(true).setSubscribeListener(this);
        subscriber.setWhat(syry).setDialogMessage("获取使用人员中...");
        RxUtil.rxExecute(subscriber.getSyry(glbm), subscriber);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onResult(int what, final Object tag, String result) {
        try {
            OutUtil.println(result);
            switch (what) {
                case sybm1st: {
                    if (TextUtils.isEmpty(result)) {
                        showDialog("服务器未返回数据！");
                        return;
                    }
                    ResultBean<List<SybmSyryBean>> resultBean = (ResultBean<List<SybmSyryBean>>) GsonUtil.json2BeanArray(result, ResultBean.class, SybmSyryBean.class);
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
                    List<SybmSyryBean> beanList = resultBean.getData();
                    if (TextUtils.equals("1", code)) {
                        sybm1stAdapter.notifyList(beanList);
                        if (null == beanList || beanList.isEmpty()) {
                            sybm2ndAdapter.notifyList();
                            syryAdapter.notifyList();
                            showToast("当前部门尚未维护用户，请联系系统管理员");
                        }
                    } else if (TextUtils.equals("0", code)) {
                        showToast("当前部门尚未维护用户，请联系系统管理员");
                    } else showDialog(message);
                }
                break;
                case sybm2nd: {
                    if (TextUtils.isEmpty(result)) {
                        showDialog("服务器未返回数据！");
                        return;
                    }
                    ResultBean<List<SybmSyryBean>> resultBean = (ResultBean<List<SybmSyryBean>>) GsonUtil.json2BeanArray(result, ResultBean.class, SybmSyryBean.class);
                    if (null == resultBean) {
                        showDialog("解析数据失败或数据格式错误");
                        return;
                    }
                    String code = replaceNull(resultBean.getCode());
                    String message = replaceNull(resultBean.getMessage());
                    List<SybmSyryBean> beanList = resultBean.getData();
                    if (TextUtils.equals("1", code)) {
                        sybm2ndAdapter.notifyList(beanList);
                        if (null == beanList || beanList.isEmpty())
                            showToast("尚未维护用户，请联系系统管理员");
                    } else if (TextUtils.equals("0", code)) {
                        showToast("当前部门尚未维护用户，请联系系统管理员");
                    } else showDialog(message);
                }
                break;
                case syry: {
                    if (TextUtils.isEmpty(result)) {
                        showDialog("服务器未返回数据！");
                        return;
                    }
                    ResultBean<List<SybmSyryBean>> resultBean = (ResultBean<List<SybmSyryBean>>) GsonUtil.json2BeanArray(result, ResultBean.class, SybmSyryBean.class);
                    if (null == resultBean) {
                        showDialog("解析数据失败或数据格式错误");
                        return;
                    }
                    String code = replaceNull(resultBean.getCode());
                    String message = replaceNull(resultBean.getMessage());
                    List<SybmSyryBean> beanList = resultBean.getData();
                    if (TextUtils.equals("1", code)) {
                        syryAdapter.notifyList(beanList);
                        if (null == beanList || beanList.isEmpty())
                            showToast("尚未维护用户，请联系系统管理员");
                    } else if (TextUtils.equals("0", code)) {
                        showToast("当前部门尚未维护用户，请联系系统管理员");
                    } else showDialog(message);
                }
                break;
                case submit: {
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
                    showToast("注册成功");
                    submitView.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    intentBuilder.putExtra("account", (String) tag);
                                    setResult(RESULT_OK, intentBuilder);
                                    finish();
                                }
                            }, 800);
                }
                break;
            }
        } catch (Exception e) {
            onError(what, tag, e);
        }
    }

    @Override
    public void onError(int what, Object tag, Throwable e) {
        switch (what) {
            case sybm1st:
            case sybm2nd:
                showErrorDialog("获取使用部门失败", e);
                break;
            case syry:
                showErrorDialog("获取使用人员失败", e);
                break;
            case submit:
                showErrorDialog("注册失败", e);
                break;
        }
    }
}
