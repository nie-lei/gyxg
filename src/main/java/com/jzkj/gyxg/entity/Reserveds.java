package com.jzkj.gyxg.entity;

import java.util.Date;

public class Reserveds extends BaseEntity {
    private Integer reservedid;

    private Integer companyid;

    private Integer storeid;

    private Integer customerid;

    private String customerphone;

    private String reservedperson;

    private Integer payid;

    private double depositmoney;

    private String phone;

    private Integer nums;

    private String unitname;

    private String canduan;

    private Date reservedtime;

    private Date realitytime;

    private String tableids;

    private String memo;

    private Integer employeeid1;

    private Date intime;

    private String status;

    public Integer getReservedid() {
        return reservedid;
    }

    public void setReservedid(Integer reservedid) {
        this.reservedid = reservedid;
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

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public String getReservedperson() {
        return reservedperson;
    }

    public void setReservedperson(String reservedperson) {
        this.reservedperson = reservedperson;
    }

    public Integer getPayid() {
        return payid;
    }

    public void setPayid(Integer payid) {
        this.payid = payid;
    }

    public double getDepositmoney() {
        return depositmoney;
    }

    public void setDepositmoney(double depositmoney) {
        this.depositmoney = depositmoney;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getCanduan() {
        return canduan;
    }

    public void setCanduan(String canduan) {
        this.canduan = canduan;
    }

    public Date getReservedtime() {
        return reservedtime;
    }

    public void setReservedtime(Date reservedtime) {
        this.reservedtime = reservedtime;
    }

    public Date getRealitytime() {
        return realitytime;
    }

    public void setRealitytime(Date realitytime) {
        this.realitytime = realitytime;
    }

    public String getTableids() {
        return tableids;
    }

    public void setTableids(String tableids) {
        this.tableids = tableids;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getEmployeeid1() {
        return employeeid1;
    }

    public void setEmployeeid1(Integer employeeid1) {
        this.employeeid1 = employeeid1;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}