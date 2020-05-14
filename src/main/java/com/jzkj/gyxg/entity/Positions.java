package com.jzkj.gyxg.entity;

public class Positions extends BaseEntity {
    private Integer positionid;

    private Integer companyid;

    private Integer storeid;

    private String positionno;

    private String name;

    private String status;

    public Integer getPositionid() {
        return positionid;
    }

    public void setPositionid(Integer positionid) {
        this.positionid = positionid;
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

    public String getPositionno() {
        return positionno;
    }

    public void setPositionno(String positionno) {
        this.positionno = positionno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}