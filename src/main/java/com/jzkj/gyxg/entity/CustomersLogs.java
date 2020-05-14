package com.jzkj.gyxg.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CustomersLogs extends BaseEntity {
    private Integer id;

    private Integer companyid;

    private Integer storeid;

    private Integer customerid;

    private String opeation;

    private Double amount;

    private Double yueamount;

    private String memo;

    private Date intime;

    private Integer employeeid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public String getOpeation() {
        return opeation;
    }

    public void setOpeation(String opeation) {
        this.opeation = opeation;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getYueamount() {
        return yueamount;
    }

    public void setYueamount(Double yueamount) {
        this.yueamount = yueamount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(Integer employeeid) {
        this.employeeid = employeeid;
    }
}