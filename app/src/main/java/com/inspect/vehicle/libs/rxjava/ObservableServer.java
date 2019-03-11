package com.inspect.vehicle.libs.rxjava;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ObservableServer {

    /**
     * 检查更新
     *
     * @param applx 1:执法记录仪
     *              2:中间键
     *              default=2
     */
    @FormUrlEncoded
    @POST("app.do?method=" + "getAppInfo")
    Observable<String> checkUpdate(@Field("applx") String applx);
}