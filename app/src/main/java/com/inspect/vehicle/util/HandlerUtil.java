package com.inspect.vehicle.util;

import android.os.Handler;

public class HandlerUtil {
    public static void post(Handler handler, Runnable runnable) {
        handler.post(runnable);
    }

    public static void postDelayed(Handler handler, Runnable runnable) {
        postDelayed(handler, runnable, 1000);
    }

    public static void postDelayed(Handler handler, Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }
}
