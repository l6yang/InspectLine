package com.inspect.vehicle.util;

import android.os.Build;
import android.text.TextUtils;

import com.inspect.vehicle.impl.IContactsImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeviceUtil implements IContactsImpl {

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
