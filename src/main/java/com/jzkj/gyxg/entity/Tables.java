package com.jzkj.gyxg.entity;


public class Tables extends BaseEntity {
    private Integer tableid;

    private Integer companyid;

    private Integer storeid;

    private Integer areaid;

    private String code;

    private String name;

    private Integer seats;

    private Double minmoney;

    private Double fixedfee;

    private Double ratiofee;

    private Double seatfee;

    private String memo;

    private String flag;

    private Integer employeeid;

    private String status;

    public Integer getTableid() {
        return tableid;
    }

    public void setTableid(Integer tableid) {
        this.tableid = tableid;
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

    public Integer getAreaid() {
        return areaid;
    }

    public void setAreaid(Integer areaid) {
        this.areaid = areaid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Double getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(Double minmoney) {
        this.minmoney = minmoney;
    }

    public Double getFixedfee() {
        return fixedfee;
    }

    public void setFixedfee(Double fixedfee) {
        this.fixedfee = fixedfee;
    }

    public Double getRatiofee() {
        return ratiofee;
    }

    public void setRatiofee(Double ratiofee) {
        this.ratiofee = ratiofee;
    }

    public Double getSeatfee() {
        return seatfee;
    }

    public void setSeatfee(Double seatfee) {
        this.seatfee = seatfee;
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