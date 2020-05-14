package com.jzkj.gyxg.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "gyxg_orders_master")
public class OrderMaster extends BaseEntity {
    @Id
    private Integer orderid;

    private Integer companyid;

    private Integer storeid;

    private Integer customerid;

    private Integer tableid;

    private String canduan;

    private Integer nums;

    private Date ordertime;

    private Double amountmoney;

    private Double seatsfee;

    private Double tablefee;

    private Double totalmoney;

    private String isall;

    public String getIsall() {
        return isall;
    }

    public void setIsall(String isall) {
        this.isall = isall;
    }

    private Double discountratio;

    private Double discountamount;

    private Double payamount;

    private Double alsoypay;

    private Date overtime;

    private Integer employeeid;

    private Integer employee1id;

    private String overtype;

    private String flag;

    private String tableids;

    private String status;

    private String memo;

    private String sendtype;

    private Integer ismultiple;

    private Integer discountid;//打折id

    public void setIsmultiple(Integer ismultiple) {
        this.ismultiple = ismultiple;
    }

    public Integer getDiscountid() {
        return discountid;
    }

    public void setDiscountid(Integer discountid) {
        this.discountid = discountid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
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

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getTableid() {
        return tableid;
    }

    public void setTableid(Integer tableid) {
        this.tableid = tableid;
    }

    public String getCanduan() {
        return canduan;
    }

    public void setCanduan(String canduan) {
        this.canduan = canduan;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public Double getAmountmoney() {
        return amountmoney;
    }

    public void setAmountmoney(Double amountmoney) {
        this.amountmoney = amountmoney;
    }

    public Double getSeatsfee() {
        return seatsfee;
    }

    public void setSeatsfee(Double seatsfee) {
        this.seatsfee = seatsfee;
    }

    public Double getTablefee() {
        return tablefee;
    }

    public void setTablefee(Double tablefee) {
        this.tablefee = tablefee;
    }

    public Double getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(Double totalmoney) {
        this.totalmoney = totalmoney;
    }

    public Double getDiscountratio() {
        return discountratio;
    }

    public void setDiscountratio(Double discountratio) {
        this.discountratio = discountratio;
    }

    public Double getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(Double discountamount) {
        this.discountamount = discountamount;
    }

    public Double getPayamount() {
        return payamount;
    }

    public void setPayamount(Double payamount) {
        this.payamount = payamount;
    }

    public Double getAlsoypay() {
        return alsoypay;
    }

    public void setAlsoypay(Double alsoypay) {
        this.alsoypay = alsoypay;
    }

    public Date getOvertime() {
        return overtime;
    }

    public void setOvertime(Date overtime) {
        this.overtime = overtime;
    }

    public Integer getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(Integer employeeid) {
        this.employeeid = employeeid;
    }

    public Integer getEmployee1id() {
        return employee1id;
    }

    public void setEmployee1id(Integer employee1id) {
        this.employee1id = employee1id;
    }

    public String getOvertype() {
        return overtype;
    }

    public void setOvertype(String overtype) {
        this.overtype = overtype;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTableids() {
        return tableids;
    }

    public void setTableids(String tableids) {
        this.tableids = tableids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSendtype() {
        return sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }

    public Integer getIsmultiple() {
        return ismultiple;
    }

    public void setIsmultiple(int ismultiple) {
        this.ismultiple = ismultiple;
    }
}