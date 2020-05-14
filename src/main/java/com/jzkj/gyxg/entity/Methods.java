package com.jzkj.gyxg.entity;


public class Methods extends BaseEntity {
    private Integer methodid;

    private Integer companyid;

    private Integer storeid;

    private Integer dishesid;

    private String name;

    private double attachmoney;

    private double attachratio;

    private String status;

    public Integer getMethodid() {
        return methodid;
    }

    public void setMethodid(Integer methodid) {
        this.methodid = methodid;
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

    public Integer getDishesid() {
        return dishesid;
    }

    public void setDishesid(Integer dishesid) {
        this.dishesid = dishesid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAttachmoney() {
        return attachmoney;
    }

    public void setAttachmoney(double attachmoney) {
        this.attachmoney = attachmoney;
    }

    public double getAttachratio() {
        return attachratio;
    }

    public void setAttachratio(double attachratio) {
        this.attachratio = attachratio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}