package com.jzkj.gyxg.entity;

import java.util.Date;

public class CustomersAccounts extends BaseEntity {
    private Integer accountid;

    private Integer customerid;

    private Integer companyid;

    private double grouptotalmoney;

    private double groupbalance;

    private Integer singletotalpoint;

    private Integer singlepointbalance;

    private double singleconsume;

    private Date lasttime;

    public Integer getAccountid() {
        return accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
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

    public double getGrouptotalmoney() {
        return grouptotalmoney;
    }

    public void setGrouptotalmoney(double grouptotalmoney) {
        this.grouptotalmoney = grouptotalmoney;
    }

    public double getGroupbalance() {
        return groupbalance;
    }

    public void setGroupbalance(double groupbalance) {
        this.groupbalance = groupbalance;
    }

    public Integer getSingletotalpoint() {
        return singletotalpoint;
    }

    public void setSingletotalpoint(Integer singletotalpoint) {
        this.singletotalpoint = singletotalpoint;
    }

    public Integer getSinglepointbalance() {
        return singlepointbalance;
    }

    public void setSinglepointbalance(Integer singlepointbalance) {
        this.singlepointbalance = singlepointbalance;
    }

    public double getSingleconsume() {
        return singleconsume;
    }

    public void setSingleconsume(double singleconsume) {
        this.singleconsume = singleconsume;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }
}