package com.inspect.vehicle.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.inspect.vehicle.impl.IContactsImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeviceUtil implements IContactsImpl {

    /**
     * 安装程序
     * <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
     *
     * @param uriFile 安装文件存放路径
     */
    public static void install(Context context, File uriFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, getProviderPath(context), uriFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(uriFile), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProviderPath(Context context) {
        return context.getPackageName() + ".provider";
    }

    //系统版本号
    public static String getVersion() {
        return Build.VERSION.RELEASE;
    }

    //手机型号
    public static String getModel() {
        return Build.MODEL;
    }

    //手机厂商-设备品牌
    public static String getBrand() {
        return Build.BRAND;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceSerial() {
        return Build.SERIAL;
    }

    /**
     * format时间格式转换为long
     *
     * @param time 默认格式为yyyy-MM-dd HH:mm:ss
     */
    public static long time2LongDate(String time) {
        return time2LongDate(time, StrImpl.TIME_ALL);
    }

    /**
     * @param time   必须和所传的时间格式相匹配
     * @param format 默认yyyy-MM-dd HH:mm:ss
     */
    public static long time2LongDate(String time, String format) {
        try {
            if (TextUtils.isEmpty(time))
                return 0;
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
            Date start = sdf.parse(time);
            return start.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
