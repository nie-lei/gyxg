package com.jzkj.gyxg.entity;

public class Printers extends BaseEntity {
    private Integer printerid;

    private Integer companyid;

    private Integer storeid;

    private String printercode;

    private String name;

    private Integer classifyid;

    private Integer areaid;

    private String printerip;

    private String memo;

    private Integer num;

    private String status;

    public Integer getPrinterid() {
        return printerid;
    }

    public void setPrinterid(Integer printerid) {
        this.printerid = printerid;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getStoreid() {
        return storeid;
    }

    public void setStoreid(Integer storeid) {
        this.storeid = storeid;
    }

    public String getPrintercode() {
        return printercode;
    }

    public void setPrintercode(String printercode) {
        this.printercode = printercode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClassifyid() {
        return classifyid;
    }

    public void setClassifyid(Integer classifyid) {
        this.classifyid = classifyid;
    }

    public Integer getAreaid() {
        return areaid;
    }

    public void setAreaid(Integer areaid) {
        this.areaid = areaid;
    }

    public String getPrinterip() {
        return printerip;
    }

    public void setPrinterip(String printerip) {
        this.printerip = printerip;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}