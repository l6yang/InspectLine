package com.inspect.vehicle.bean;

import com.google.gson.annotations.SerializedName;
import com.loyal.kit.PatternBean;

public class ResultBean<T> extends PatternBean<T> {

    /**
     * code : -99
     * mess : org．apache．ibatis．binding．BindingException： Invalid bound statement （not found）： com．zlkj．video．dao．AndroidDao．applogin
     * data : null
     */

    private String code;
    @SerializedName(value = "mess",alternate = "msg")
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
