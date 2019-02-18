package com.inspect.vehicle.impl;

import android.os.Environment;

public interface IContactsImpl {
    final class StrImpl {
        public static final String ipadd = "";
        public static final String port = "";
        public static final String TIME_ALL = "yyyy-MM-dd HH:mm:ss";
        public static final String APK_NAME = "veh_inspect.apk";//不要更改此文件名，否则安装时会找不到文件
    }

    final class IntImpl {
        public static final int MEMORY = 300;
        public static final int LOCATION = 301;
        public static final int REGISTER = 200;
    }

    /**
     * {@link com.inspect.vehicle.util.FileUtil}
     */
    final class Path {
        public static final String MAIN = Environment.getExternalStorageDirectory().getPath();
        public static final String HOME = MAIN + "/Android/data/com.inspect.vehicle/";
        public static final String APK = HOME + "apk/";
        public static final String[] ARRAY = {APK};
    }
}
