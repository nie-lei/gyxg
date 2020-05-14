package com.jzkj.gyxg.entity;

import java.util.Date;

public class SignCustomers extends BaseEntity {
    private Integer signid;

    private Integer customerid;

    private Integer companyid;

    private Integer storeid;

    private String sponsor;

    private String phone;

    private double daymoney;

    private double monthmoney;

    private Date oktime;

    private Date intime;

    private Integer employeeid;

    private Integer employee1id;

    private Integer employee2id;

    private String stauts;

    public Integer getSignid() {
        return signid;
    }

    public void setSignid(Integer signid) {
        this.signid = signid;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
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

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getDaymoney() {
        return daymoney;
    }

    public void setDaymoney(double daymoney) {
        this.daymoney = daymoney;
    }

    public double getMonthmoney() {
        return monthmoney;
    }

    public void setMonthmoney(double monthmoney) {
        this.monthmoney = monthmoney;
    }

    public Date getOktime() {
        return oktime;
    }

    public void setOktime(Date oktime) {
        this.oktime = oktime;
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

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }
}