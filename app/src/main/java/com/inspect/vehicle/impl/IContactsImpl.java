package com.inspect.vehicle.impl;

import android.os.Environment;

public interface IContactsImpl {
    final class StrImpl {
        public static final String ip_default = "192.168.0.127";
        public static final String port = "9080";
        public static final String TIME_ALL = "yyyy-MM-dd HH:mm:ss";
        public static final String APK_NAME = "veh_inspect.apk";//不要更改此文件名，否则安装时会找不到文件
        public static final String BASEURL = "baseUrl";
        public static final String KEY_ACCOUNT = "account";
        public static final String KEY_PASSWORD = "password";
    }

    final class IntImpl {
        public static final int Loading = 59;
        public static final int Register = 100;
        public static final int MEMORY = 300;
        public static final int LOCATION = 301;
        public static final int REGISTER = 200;
        public static final int reqInstall = 22;
    }

    /**
     * {@link com.inspect.vehicle.util.FileUtil}
     */
    final class Path {
        public static final String MAIN = Environment.getExternalStorageDirectory().getPath();
        public static final String HOME = MAIN + "/Android/data/com.inspect.vehicle/";
        public static final String APK = HOME + "apk/";
        // apk更新下载的更新文件存放的目录
        public static final String logPath = HOME + "log/";
        public static final String[] ARRAY = {APK,logPath};
    }

    final class State {
        public static String UPDATE = "";
        public static final String UPDATE_ING = "doing";
        public static final String UPDATE_SUCCESS = "success";
        public static final String UPDATE_FAIL = "fail";
    }
}
