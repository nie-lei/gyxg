package com.jzkj.gyxg.entity;

import java.util.Date;

public class GoodsStock extends BaseEntity {
    private Integer goodsstockid;

    private Integer companyid;

    private Integer storeid;

    private Integer goodsid;

    private Integer warehouseid;

    private String unit;

    private double ycstock;
    private double stockamount;//库存金额

    public double getStockamount() {
        return stockamount;
    }

    public void setStockamount(double stockamount) {
        this.stockamount = stockamount;
    }

    public double getYcstock() {
        return ycstock;
    }

    public void setYcstock(double ycstock) {
        this.ycstock = ycstock;
    }

    private double stock;

    private Date lasttime;

    public Integer getGoodsstockid() {
        return goodsstockid;
    }

    public void setGoodsstockid(Integer goodsstockid) {
        this.goodsstockid = goodsstockid;
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

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(Integer warehouseid) {
        this.warehouseid = warehouseid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }
}