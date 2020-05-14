package com.jzkj.gyxg.entity;

public class Attachs extends BaseEntity {
    private Integer attachid;

    private Integer companyid;

    private Integer storeid;

    private String name;

    private String flag;

    private String status;

    public Integer getAttachid() {
        return attachid;
    }

    public void setAttachid(Integer attachid) {
        this.attachid = attachid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}