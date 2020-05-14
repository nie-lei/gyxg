package com.jzkj.gyxg.entity;

import java.util.Date;

public class WhiteMac {

    private Integer id;//主键
    private Integer companyid;//公司流水号
    private Integer storeid;//店铺流水号
    private String mac;//mac地址
    private Date intime;//录入日期
    private Integer employeeid;//操作员id
    private String memo;//备注
    private String status;//状态，0有效，1无效


    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public java.lang.Integer getStoreid() {
        return storeid;
    }

    public void setStoreid(java.lang.Integer storeid) {
        this.storeid = storeid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public java.lang.Integer getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(java.lang.Integer employeeid) {
        this.employeeid = employeeid;
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
