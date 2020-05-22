package com.jzkj.gyxg.entity;

import java.util.Date;

public class OrderSlave extends BaseEntity {
    private Integer id;

    private Integer orderid;

    private Integer disheid;

    private String name;

    private Integer methodid;

    private String unit;

    private Integer amount;

    private Double price;

    private Double orderamount;

    private Double realamount;

    private String memo;

    private String flag;

    private Integer employeeid;

    private Date intime;

    private String state;

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

    public Integer getMethodid() {
        return methodid;
    }

    public void setMethodid(Integer methodid) {
        this.methodid = methodid;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(Double orderamount) {
        this.orderamount = orderamount;
    }

    public Double getRealamount() {
        return realamount;
    }

    public void setRealamount(Double realamount) {
        this.realamount = realamount;
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

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}