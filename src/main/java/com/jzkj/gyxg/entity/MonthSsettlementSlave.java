package com.jzkj.gyxg.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "gyxg_monthssettlement_slave")
public class MonthSsettlementSlave extends BaseEntity   {

    private Integer settlementid;

    private Integer goodsid;

    private Integer warehouseid;

    private String unit;

    private Double stock;

    private Double stockamount;

    public Integer getSettlementid() {
        return settlementid;
    }

    public void setSettlementid(Integer settlementid) {
        this.settlementid = settlementid;
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

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getStockamount() {
        return stockamount;
    }

    public void setStockamount(Double stockamount) {
        this.stockamount = stockamount;
    }
}