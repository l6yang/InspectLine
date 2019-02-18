package com.inspect.vehicle.libs.rxjava;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ObservableServer {

    @FormUrlEncoded
    @POST("action.do?method=" + "checkUpdate")
    Observable<String> checkUpdate(@Field("apkVersion") String apkVersion);
}