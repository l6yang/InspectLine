package com.inspect.vehicle.impl;

import android.os.Environment;

public interface ContactsImpl {
    interface StrImpl {
        String ipadd = "";
        String port = "";
        String TIME_ALL = "yyyy-MM-dd HH:mm:ss";
        String APK_NAME = "veh_inspect.apk";//不要更改此文件名，否则安装时会找不到文件
    }
    interface IntImpl {
        int MEMORY = 300;
        int LOCATION = 301;
        int REGISTER = 200;
    }

    /**
     * {@link com.inspect.vehicle.util.FileUtil}
     */
    interface Path {
        String MAIN = Environment.getExternalStorageDirectory().getPath();
        String HOME = MAIN + "/Android/data/com.inspect.vehicle/";
        String APK = HOME + "apk/";
        String[] ARRAY = {APK};
    }
}
