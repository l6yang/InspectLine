package com.inspect.vehicle.bean;

public class SubmitBean {
    private String jlybh;//记录仪编号
    private String cjzbh;//采集站编号（不知道先不传）
    private String jlyxh;//记录仪序号
    private String jlymc;//记录仪名称
    private String bdmj;//绑定民警
    private String glbm;//管理部门
    private String syglbm;//使用管理部门

    private String nbm;//设备内部码
    private String sbrl;//设备容量
    private String sbcs;//设备厂商
    private String pid;//设备PID
    private String vid;//设备VID

    public SubmitBean() {
    }

    public SubmitBean(String jlybh, String cjzbh, String jlyxh, String jlymc, String bdmj, String glbm, String syglbm, String nbm, String sbrl, String sbcs, String pid, String vid) {
        this.jlybh = jlybh;
        this.cjzbh = cjzbh;
        this.jlyxh = jlyxh;
        this.jlymc = jlymc;
        this.bdmj = bdmj;
        this.glbm = glbm;
        this.syglbm = syglbm;
        this.nbm = nbm;
        this.sbrl = sbrl;
        this.sbcs = sbcs;
        this.pid = pid;
        this.vid = vid;
    }

    public String getJlybh() {
        return jlybh;
    }

    public void setJlybh(String jlybh) {
        this.jlybh = jlybh;
    }

    public String getCjzbh() {
        return cjzbh;
    }

    public void setCjzbh(String cjzbh) {
        this.cjzbh = cjzbh;
    }

    public String getJlyxh() {
        return jlyxh;
    }

    public void setJlyxh(String jlyxh) {
        this.jlyxh = jlyxh;
    }

    public String getJlymc() {
        return jlymc;
    }

    public void setJlymc(String jlymc) {
        this.jlymc = jlymc;
    }

    public String getBdmj() {
        return bdmj;
    }

    public void setBdmj(String bdmj) {
        this.bdmj = bdmj;
    }

    public String getGlbm() {
        return glbm;
    }

    public void setGlbm(String glbm) {
        this.glbm = glbm;
    }

    public String getSyglbm() {
        return syglbm;
    }

    public void setSyglbm(String syglbm) {
        this.syglbm = syglbm;
    }

    public String getNbm() {
        return nbm;
    }

    public void setNbm(String nbm) {
        this.nbm = nbm;
    }

    public String getSbrl() {
        return sbrl;
    }

    public void setSbrl(String sbrl) {
        this.sbrl = sbrl;
    }

    public String getSbcs() {
        return sbcs;
    }

    public void setSbcs(String sbcs) {
        this.sbcs = sbcs;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
