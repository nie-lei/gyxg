package com.jzkj.gyxg.entity;


public class LossSlaves extends BaseEntity {
    private Integer slaveid;

    private Integer lossid;

    private Integer goodsid;

    private double lossnums;

    private double lossmoney;

    private String memo;

    public Integer getSlaveid() {
        return slaveid;
    }

    public void setSlaveid(Integer slaveid) {
        this.slaveid = slaveid;
    }

    public Integer getLossid() {
        return lossid;
    }

    public void setLossid(Integer lossid) {
        this.lossid = lossid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public double getLossnums() {
        return lossnums;
    }

    public void setLossnums(double lossnums) {
        this.lossnums = lossnums;
    }

    public double getLossmoney() {
        return lossmoney;
    }

    public void setLossmoney(double lossmoney) {
        this.lossmoney = lossmoney;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}