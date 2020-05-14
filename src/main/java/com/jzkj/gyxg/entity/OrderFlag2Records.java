package com.jzkj.gyxg.entity;

public class OrderFlag2Records extends BaseEntity {
    private Integer id;

    private Integer orderid;

    private String otherorderid;

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

    public String getOtherorderid() {
        return otherorderid;
    }

    public void setOtherorderid(String otherorderid) {
        this.otherorderid = otherorderid;
    }
}