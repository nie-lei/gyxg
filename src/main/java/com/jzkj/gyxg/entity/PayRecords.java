package com.jzkj.gyxg.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PayRecords extends BaseEntity {
    private Integer payid;

    private Integer companyid;

    private Integer storeid;

    private Integer customerid;

    private String payno;

    private Integer paytype;

    private String paytypedesc;

    private Integer paynum;

    private Double payamount;

    private Date paytime;

    private Double returnamount;

    private Date returntime;

    private String memo;

    private String flag;

    private Integer employeeid;

    private String overflag;

    private String status;

    private String returnno;

    private String refundno;

    private String paystatus;

    public Integer getPayid() {
        return payid;
    }

    public void setPayid(Integer payid) {
        this.payid = payid;
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

    public String getPayno() {
        return payno;
    }

    public void setPayno(String payno) {
        this.payno = payno;
    }

    public Integer getPaytype() {
        return paytype;
    }

    public void setPaytype(Integer paytype) {
        this.paytype = paytype;
    }

    public String getPaytypedesc() {
        return paytypedesc;
    }

    public void setPaytypedesc(String paytypedesc) {
        this.paytypedesc = paytypedesc;
    }

    public Integer getPaynum() {
        return paynum;
    }

    public void setPaynum(Integer paynum) {
        this.paynum = paynum;
    }

    public Double getPayamount() {
        return payamount;
    }

    public void setPayamount(Double payamount) {
        this.payamount = payamount;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    public Double getReturnamount() {
        return returnamount;
    }

    public void setReturnamount(Double returnamount) {
        this.returnamount = returnamount;
    }

    public Date getReturntime() {
        return returntime;
    }

    public void setReturntime(Date returntime) {
        this.returntime = returntime;
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

    public Integer getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(Integer employeeid) {
        this.employeeid = employeeid;
    }

    public String getOverflag() {
        return overflag;
    }

    public void setOverflag(String overflag) {
        this.overflag = overflag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnno() {
        return returnno;
    }

    public void setReturnno(String returnno) {
        this.returnno = returnno;
    }

    public String getRefundno() {
        return refundno;
    }

    public void setRefundno(String refundno) {
        this.refundno = refundno;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }
}