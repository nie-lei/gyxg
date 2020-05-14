package com.jzkj.gyxg.entity;

import java.util.Date;

public class Invests extends BaseEntity {
    private Integer orderid;

    private Integer companyid;

    private Integer storeid;

    private Integer payid;

    private Integer customerid;

    private String investtype;

    private double investmoney;

    private double rebatemoney;

    private Integer rebatepoint;

    private Date investtime;

    private Integer employeeid;

    private Integer employee1id;

    private String status;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
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

    public Integer getPayid() {
        return payid;
    }

    public void setPayid(Integer payid) {
        this.payid = payid;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public String getInvesttype() {
        return investtype;
    }

    public void setInvesttype(String investtype) {
        this.investtype = investtype;
    }

    public double getInvestmoney() {
        return investmoney;
    }

    public void setInvestmoney(double investmoney) {
        this.investmoney = investmoney;
    }

    public double getRebatemoney() {
        return rebatemoney;
    }

    public void setRebatemoney(double rebatemoney) {
        this.rebatemoney = rebatemoney;
    }

    public Integer getRebatepoint() {
        return rebatepoint;
    }

    public void setRebatepoint(Integer rebatepoint) {
        this.rebatepoint = rebatepoint;
    }

    public Date getInvesttime() {
        return investtime;
    }

    public void setInvesttime(Date investtime) {
        this.investtime = investtime;
    }

    public Integer getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(Integer employeeid) {
        this.employeeid = employeeid;
    }

    public Integer getEmployee1id() {
        return employee1id;
    }

    public void setEmployee1id(Integer employee1id) {
        this.employee1id = employee1id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}