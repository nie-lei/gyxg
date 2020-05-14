package com.jzkj.gyxg.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "gyxg_dishe_pages")
public class DishePages extends BaseEntity {
    @Id
    private Integer pageid;

    private Integer companyid;//公司流水号

    private Integer storeid;//店铺流水号

    private Integer dishesid;//菜品分类

    private Integer sequence;//显示顺序

    private String pagemould;//模板：1-一个菜,2-两个菜,3-三个菜,4-四个菜

    private String disheid;//对应菜品（多个菜用","分割）

    private String status;//状态：0-开放，1-关闭

    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getPageid() {
        return pageid;
    }

    public void setPageid(Integer pageid) {
        this.pageid = pageid;
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

    public Integer getDishesid() {
        return dishesid;
    }

    public void setDishesid(Integer dishesid) {
        this.dishesid = dishesid;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getPagemould() {
        return pagemould;
    }

    public void setPagemould(String pagemould) {
        this.pagemould = pagemould;
    }

    public String getDisheid() {
        return disheid;
    }

    public void setDisheid(String disheid) {
        this.disheid = disheid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}