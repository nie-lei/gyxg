package com.jzkj.gyxg.entity;

import java.util.Date;

public class PurchaseMasters extends BaseEntity {
    private Integer purchaseid;

    private Integer companyid;

    private Integer storeid;

    private String code;

    private Date purchasedate;

    private Integer supplierid;

    private double totalmoney;

    private Integer departmentid;

    private Integer employee1id;

    private Integer employee2id;

    private Integer employee3id;

    private Integer employee5id;

    private Date intime;

    private Date checktime;

    private String isPay;

    private String memo;

    private String flag;

    private String status;

    public Integer getPurchaseid() {
        return purchaseid;
    }

    public void setPurchaseid(Integer purchaseid) {
        this.purchaseid = purchaseid;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getPurchasedate() {
        return purchasedate;
    }

    public void setPurchasedate(Date purchasedate) {
        this.purchasedate = purchasedate;
    }

    public Integer getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(Integer supplierid) {
        this.supplierid = supplierid;
    }

    public double getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(double totalmoney) {
        this.totalmoney = totalmoney;
    }

    public Integer getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(Integer departmentid) {
        this.departmentid = departmentid;
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

    public Integer getEmployee5id() {
        return employee5id;
    }

    public void setEmployee5id(Integer employee5id) {
        this.employee5id = employee5id;
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

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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