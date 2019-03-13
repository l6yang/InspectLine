package com.inspect.vehicle.libs.rxjava;

import android.content.Context;

import com.loyal.rx.BaseRxServerSubscriber;
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
    public void createServer(RetrofitManage retrofitManage) {
        server = retrofitManage.createServer(ObservableServer.class);
    }

    @Override
    public String serverNameSpace() {
        return "video";
    }

    @Override
    public Observable<String> login(String yhdh, String yhmm, String sbbh, String ver) {
        return server.login(yhdh, yhmm, sbbh, ver);
    }

    @Override
    public Observable<String> getGlbm(String bz) {
        return server.getGlbm(bz);
    }

    @Override
    public Observable<String> getSyry(String glbm) {
        return server.getSyry(glbm);
    }

    @Override
    public Observable<String> register(String beanJson) {
        return server.register(beanJson);
    }

    @Override
    public Observable<String> checkUpdate(String applx) {
        return server.checkUpdate(applx);
    }

    @Override
    public Observable<String> sendMq(String beanJson) {
        return server.sendMq(beanJson);
    }
}