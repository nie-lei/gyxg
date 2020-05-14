package com.jzkj.gyxg.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "gyxg_paytype")
public class Paytype extends BaseEntity {
    @Id
    private Integer id;

    private Integer companyid;

    private Integer storeid;

    private Integer paycode;

    private String payname;

    private String isquan;

    private Double quanmoney;

    private Double discountmoney;

    private Double minmoney;

    private Integer quannum;

    private String status;

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

    public Integer getPaycode() {
        return paycode;
    }

    public void setPaycode(Integer paycode) {
        this.paycode = paycode;
    }

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public String getIsquan() {
        return isquan;
    }

    public void setIsQuan(String isquan) {
        this.isquan = isquan;
    }

    public Double getQuanmoney() {
        return quanmoney;
    }

    public void setQuanmoney(Double quanmoney) {
        this.quanmoney = quanmoney;
    }

    public Double getDiscountmoney() {
        return discountmoney;
    }

    public void setDiscountmoney(Double discountmoney) {
        this.discountmoney = discountmoney;
    }

    public Double getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(Double minmoney) {
        this.minmoney = minmoney;
    }

    public Integer getQuannum() {
        return quannum;
    }

    public void setQuannum(Integer quannum) {
        this.quannum = quannum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}