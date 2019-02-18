package com.inspect.vehicle.util;

import android.text.TextUtils;

import com.inspect.vehicle.impl.IContactsImpl;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ConnectUtil implements IContactsImpl {

    public static String getError(Throwable throwable) {
        if (null == throwable)
            return "未知错误";
        if (throwable instanceof ConnectException)
            return "连接服务器失败";
        else if (throwable instanceof SocketTimeoutException)
            return "连接超时";
        else if (throwable instanceof UnknownHostException)
            return "网络未连接或者当前网络地址未被识别";
        else if (throwable instanceof SocketException)
            return "网络服务出现问题";
        else return throwable.getMessage();
    }

    /**
     * @param ipAdd 默认端口9080
     *              1、（ip地址中包含端口号）192.168.1.15:9081
     *              2、（ip地址中不含端口号，需要加上默认端口9080）192.168.1.15
     * @return 1、192.168.1.15:9081
     * 2、192.168.1.15:9080
     */
    public static String getIpAddress(String ipAdd) {
        String address;
        if (TextUtils.isEmpty(ipAdd)) {
            address = StrImpl.ipadd + ":" + StrImpl.port;
            return address;
        }
        try {
            if (ipAdd.contains(":")) {
                address = ipAdd;
            } else {
                address = ipAdd + ":" + StrImpl.port;
            }
        } catch (Exception e) {
            address = ipAdd + ":" + StrImpl.port;
        }
        return address;
    }
}
