package com.jzkj.gyxg.entity;

public class CheckSlaves extends BaseEntity {
    private Integer slaveid;

    private Integer checkid;

    private Integer goodsid;

    private double stock;

    private double checkstock;

    private double usednums;

    private double usedmoney;

    private String memo;

    public Integer getSlaveid() {
        return slaveid;
    }

    public void setSlaveid(Integer slaveid) {
        this.slaveid = slaveid;
    }

    public Integer getCheckid() {
        return checkid;
    }

    public void setCheckid(Integer checkid) {
        this.checkid = checkid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getCheckstock() {
        return checkstock;
    }

    public void setCheckstock(double checkstock) {
        this.checkstock = checkstock;
    }

    public double getUsednums() {
        return usednums;
    }

    public void setUsednums(double usednums) {
        this.usednums = usednums;
    }

    public double getUsedmoney() {
        return usedmoney;
    }

    public void setUsedmoney(double usedmoney) {
        this.usedmoney = usedmoney;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}