package com.inspect.vehicle.bean;

public class LinkBean {

    /**
     * text : 重要事项
     * url : com.mobile.police.activity.banner.important.ImportantActivity
     * icon : asset:///images/banner/important.png
     * title : 重要事项
     */

    private String text;
    private String url;
    private String icon;
    private String packName;
    private String actUrl;
    private String title;
    private String flag;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
