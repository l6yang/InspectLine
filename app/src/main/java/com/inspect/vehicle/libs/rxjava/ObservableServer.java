package com.inspect.vehicle.libs.rxjava;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ObservableServer {

    @FormUrlEncoded
    @POST("app.do?method=login")
    Observable<String> login(@Field("yhdh") String yhdh, @Field("mm") String yhmm,
                             @Field("sbbh") String sbbh, @Field("ver") String ver);

    //注册页面获取使用部门

    /**
     * @param bz 1:获取最上级管理部门
     */
    @FormUrlEncoded
    @POST("app.do?method=getGlbm")
    Observable<String> getGlbm(@Field("bz") String bz);

    //注册页面获取使用人员
    @FormUrlEncoded
    @POST("app.do?method=getUser")
    Observable<String> getSyry(@Field("glbm") String glbm);

    //注册页面注册设备
    @FormUrlEncoded
    @POST("app.do?method=saveZfjlySb")
    Observable<String> register(@Field("beanJson") String beanJson);

    /**
     * 检查更新
     *
     * @param applx 1:执法记录仪
     *              2:中间键
     *              default=2
     */
    @FormUrlEncoded
    @POST("app.do?method=getAppInfo")
    Observable<String> checkUpdate(@Field("applx") String applx);
    @FormUrlEncoded
    @POST("app.do?method=sendmq")
    Observable<String> sendMq(@Field("beanJson") String beanJson);
}