package com.inspect.vehicle.bean;

public class StartRecordBean {
    private String cylsh;//查验流水号
    private String clsbdh;//车辆识别代号
    private String cyqxh;//查验区序号
    private String cyqtd;//查验区通道
    private String cllx;//处理类型 0-查验开始；1-查验结束
    private String cysj;//查验时间
    private String cycs;//查验次数
    //可空参数
    private String sfzmhm;//身份证明号码
    private String hpzl;//号牌种类
    private String hphm;//号牌号码

    public String getCylsh() {
        return cylsh;
    }

    public void setCylsh(String cylsh) {
        this.cylsh = cylsh;
    }

    public String getClsbdh() {
        return clsbdh;
    }

    public void setClsbdh(String clsbdh) {
        this.clsbdh = clsbdh;
    }

    public String getCyqxh() {
        return cyqxh;
    }

    public void setCyqxh(String cyqxh) {
        this.cyqxh = cyqxh;
    }

    public String getCyqtd() {
        return cyqtd;
    }

    public void setCyqtd(String cyqtd) {
        this.cyqtd = cyqtd;
    }

    public String getCllx() {
        return cllx;
    }

    public void setCllx(String cllx) {
        this.cllx = cllx;
    }

    public String getCysj() {
        return cysj;
    }

    public void setCysj(String cysj) {
        this.cysj = cysj;
    }

    public String getCycs() {
        return cycs;
    }

    public void setCycs(String cycs) {
        this.cycs = cycs;
    }

    public String getSfzmhm() {
        return sfzmhm;
    }

    public void setSfzmhm(String sfzmhm) {
        this.sfzmhm = sfzmhm;
    }

    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }
}
