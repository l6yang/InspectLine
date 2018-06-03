package com.inspect.vehicle.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class InspectApp extends Application {
    private static InspectApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Fresco.initialize(this);
    }

    public static InspectApp getInstance() {
        return mInstance;
    }
}
