package com.jzkj.gyxg.entity;

import java.math.BigDecimal;

public class OrderPrintSlave extends BaseEntity {
    private Integer id;

    private Integer orderid;

    private Integer tablesid;

    private Integer disheid;

    private String name;

    private String unit;

    private Integer amount;

    private Integer methodid;

    private Double price;

    private Double refundmoney;

    private String memo;

    private String flag;

    private Integer employeeid;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getTablesid() {
        return tablesid;
    }

    public void setTablesid(Integer tablesid) {
        this.tablesid = tablesid;
    }

    public Integer getDisheid() {
        return disheid;
    }

    public void setDisheid(Integer disheid) {
        this.disheid = disheid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getMethodid() {
        return methodid;
    }

    public void setMethodid(Integer methodid) {
        this.methodid = methodid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getRefundmoney() {
        return refundmoney;
    }

    public void setRefundmoney(Double refundmoney) {
        this.refundmoney = refundmoney;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}