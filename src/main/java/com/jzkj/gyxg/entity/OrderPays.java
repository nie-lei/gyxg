package com.jzkj.gyxg.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "gyxg_order_pays")
public class OrderPays extends BaseEntity {
    @Id
    private Integer id;

    private Integer orderid;

    private Integer payid;

    private Date oktime;

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

    public Integer getPayid() {
        return payid;
    }

    public void setPayid(Integer payid) {
        this.payid = payid;
    }

    public Date getOktime() {
        return oktime;
    }

    public void setOktime(Date oktime) {
        this.oktime = oktime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}