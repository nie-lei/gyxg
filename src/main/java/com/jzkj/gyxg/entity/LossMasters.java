package com.jzkj.gyxg.entity;

import java.util.Date;

public class LossMasters extends BaseEntity {
    private Integer lossid;

    private Integer companyid;

    private Integer storeid;

    private Integer warehouseid;

    private String code;

    private String originalcode;

    private Date checkdate;

    private double totalmoney;

    private Integer employee1id;

    private Integer employee2id;

    private Integer employee3id;

    private Date intime;

    private Date checktime;

    private String memo;

    private String status;

    public Integer getLossid() {
        return lossid;
    }

    public void setLossid(Integer lossid) {
        this.lossid = lossid;
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

    public Integer getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(Integer warehouseid) {
        this.warehouseid = warehouseid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOriginalcode() {
        return originalcode;
    }

    public void setOriginalcode(String originalcode) {
        this.originalcode = originalcode;
    }

    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public double getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(double totalmoney) {
        this.totalmoney = totalmoney;
    }

    public Integer getEmployee1id() {
        return employee1id;
    }

    public void setEmployee1id(Integer employee1id) {
        this.employee1id = employee1id;
    }

    public Integer getEmployee2id() {
        return employee2id;
    }

    public void setEmployee2id(Integer employee2id) {
        this.employee2id = employee2id;
    }

    public Integer getEmployee3id() {
        return employee3id;
    }

    public void setEmployee3id(Integer employee3id) {
        this.employee3id = employee3id;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}