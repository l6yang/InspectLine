package com.inspect.vehicle.libs.rxjava;

import android.content.Context;

import com.loyal.rx.BaseRxServerSubscriber;
import com.loyal.rx.BaseRxSubscriber;
import com.loyal.rx.RetrofitManage;
import com.loyal.rx.impl.RxSubscriberListener;

import io.reactivex.Observable;

public class RxProgressSubscriber<T> extends BaseRxServerSubscriber<T> implements ObservableServer {

    private ObservableServer server;

    public RxProgressSubscriber(Context context) {
        super(context);
    }

    public RxProgressSubscriber(Context context, String ipAdd) {
        super(context, ipAdd);
    }

    public RxProgressSubscriber(Context context, String ipAdd, boolean showProgressDialog) {
        super(context, ipAdd, showProgressDialog);
    }

    public RxProgressSubscriber(Context context, String ipAdd, int what) {
        super(context, ipAdd, what);
    }

    public RxProgressSubscriber(Context context, String ipAdd, int what, boolean showProgressDialog, RxSubscriberListener<T> listener) {
        super(context, ipAdd, what, showProgressDialog, listener);
    }

    @Override
    public String httpOrHttps() {
        return "http";
    }

    @Override
    public BaseRxSubscriber<T> showProgressDialog(boolean showProgressDialog) {
        return super.showProgressDialog(true);
    }

    @Override
    public void createServer(RetrofitManage retrofitManage) {
        server = retrofitManage.createServer(ObservableServer.class);
    }

    @Override
    public String serverNameSpace() {
        return "mwm";
    }

    @Override
    public Observable<String> checkUpdate(String apkVersion) {
        return server.checkUpdate(apkVersion);
    }
}