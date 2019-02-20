package com.inspect.vehicle.libs.rxjava;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpManager {
    private static OkHttpManager mInstance;
    private OkHttpClient client;

    private OkHttpManager() {
        client = new OkHttpClient();
    }

    public static OkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }

    public ResponseBody downLoad(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
            return response.body();
        else throw new IOException("Unexpected code" + response);
    }

    public void downApk(String url, Callback callback)   {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }
}