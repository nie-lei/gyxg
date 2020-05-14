package com.jzkj.gyxg.entity;

import java.util.Date;

public class VipCustomers extends BaseEntity {
    private Integer vipid;

    private Integer companyid;

    private Integer storeid;

    private Integer customerid;

    private String cardcode;

    private Date begindate;

    private Date enddate;

    private Double discountrate;

    private Date intime;

    private Integer employeeid;

    private Integer employee1id;

    private Integer employee2id;

    private String status;

    public Integer getVipid() {
        return vipid;
    }

    public void setVipid(Integer vipid) {
        this.vipid = vipid;
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

    public String getCardcode() {
        return cardcode;
    }

    public void setCardcode(String cardcode) {
        this.cardcode = cardcode;
    }


    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Double getDiscountrate() {
        return discountrate;
    }

    public void setDiscountrate(Double discountrate) {
        this.discountrate = discountrate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}