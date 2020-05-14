package com.jzkj.gyxg.entity;


public class Discount extends BaseEntity {
    private Integer id;

    private Integer companyid;

    private Integer storeid;

    private String discountname;

    private Double discountratio;

    private String isall;

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

    public String getDiscountname() {
        return discountname;
    }

    public String getIsall() {
        return isall;
    }

    public void setIsall(String isall) {
        this.isall = isall;
    }

    public void setDiscountname(String discountname) {
        this.discountname = discountname;
    }

    public Double getDiscountratio() {
        return discountratio;
    }

    public void setDiscountratio(Double discountratio) {
        this.discountratio = discountratio;
    }

}