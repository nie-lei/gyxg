package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/customers")
public class CustomersController extends BaseController{

    @Autowired
    private CustomersService customersService;

    /**
     * 客户列表
     * @return
     */
    @RequestMapping("customerslist")
    public ResponseJson customersList(){
        return customersService.customersList();
    }

    /**
     * 账户管理
     * @return
     */
    @RequestMapping("acountlist")
    public ResponseJson acountList(){
        return customersService.acountList();
    }

    /**
     * 客户新增
     * @return
     */
    @RequestMapping("customersadd")
    public ResponseJson customersAdd() throws AjaxOperationFailException {
        return customersService.customersAdd();
    }

    /**
     * 客户修改
     * @return
     */
    @RequestMapping("customersedit")
    public ResponseJson customersEdit() throws AjaxOperationFailException {
        return customersService.customersEdit();
    }

    /**
     * 客户删除
     * @return
     */
    @RequestMapping("customersdel")
    public ResponseJson customersDel() throws AjaxOperationFailException {
        return customersService.customersDel();
    }


    /**
     * vip客户新增
     * @return
     */
    @RequestMapping("addvip")
    public ResponseJson addVip() throws AjaxOperationFailException {
        return customersService.addVip();
    }

    /**
     * vip客户修改
     * @return
     */
    @RequestMapping("editvip")
    public ResponseJson editVip() throws AjaxOperationFailException {
        return customersService.editVip();
    }


    /**
     * vip客户删除
     * @return
     */
    @RequestMapping("delvip")
    public ResponseJson delVip() throws AjaxOperationFailException {
        return customersService.delVip();
    }

    /**
     * vip客户审核
     * @return
     */
    @RequestMapping("authvip")
    public ResponseJson AuthVip() throws AjaxOperationFailException {
        return customersService.AuthVip();
    }

    /**
     * vip客户列表
     * @return
     */
    @RequestMapping("viplist")
    public ResponseJson vipList() throws AjaxOperationFailException {
        return customersService.vipList();
    }

    /**
     * 签单客户新增
     * @return
     */
    @RequestMapping("addsign")
    public ResponseJson addSign() throws AjaxOperationFailException {
        return customersService.addSign();
    }

    /**
     * 签单客户修改
     * @return
     */
    @RequestMapping("editsign")
    public ResponseJson editSign() throws AjaxOperationFailException {
        return customersService.editSign();
    }

    /**
     * 签单客户列表
     * @return
     */
    @RequestMapping("listsign")
    public ResponseJson listSign() throws AjaxOperationFailException {
        return customersService.listSign();
    }

    /**
     * 签单客户删除
     * @return
     */
    @RequestMapping("delsign")
    public ResponseJson delSign() throws AjaxOperationFailException {
        return customersService.delSign();
    }

    /**
     * 签单客户审核
     * @return
     */
    @RequestMapping("authsign")
    public ResponseJson authSign() throws AjaxOperationFailException {
        return customersService.authSign();
    }

    /**
     * 会员查询
     * @return
     */
    @RequestMapping("searchvip")
    public ResponseJson searchVip() throws AjaxOperationFailException {
        return customersService.searchVip();
    }

    /**
     * 设置支付码
     * @return
     */
    @RequestMapping("setcuspasswd")
    public ResponseJson setCuspasswd() throws AjaxOperationFailException {
        return customersService.setCuspasswd();
    }

    /**
     * 签单客户查询
     * @return
     */
    @RequestMapping("searchsign")
    public ResponseJson searchSign() throws AjaxOperationFailException {
        return customersService.searchSign();
    }

    /**
     * 订金（或者充值）缴纳
     * @return
     */
    @RequestMapping("deposit")
    public ResponseJson deposit() throws AjaxOperationFailException {
        return customersService.deposit();
    }



    /**
     * 定金信息查询
     * @return
     */
    @RequestMapping("depositinfo")
    public ResponseJson depositinfo() throws AjaxOperationFailException {
        return customersService.depositinfo();
    }

    /**
     * 定金转余额
     * @return
     */
    @RequestMapping("deposittoaccount")
    public ResponseJson depositToAccount() throws AjaxOperationFailException {
        return customersService.depositToAccount();
    }

    /**
     * 定金退款
     * @return
     */
    @RequestMapping("depositrefund")
    public ResponseJson depositRefund() throws AjaxOperationFailException {
        return customersService.depositRefund();
    }

    /**
     * 充值退款
     * @return
     */
    @RequestMapping("chongzhirefund")
    public ResponseJson chongzhirefund() throws AjaxOperationFailException {
        return customersService.chongzhirefund();
    }

    /**
     * 签单信息
     * @return
     */
    @RequestMapping("signlistinfo")
    public ResponseJson signListInfo() throws AjaxOperationFailException {
        return customersService.signListInfo();
    }



    /**
     * 充值记录查询
     * @return
     */
    @RequestMapping("investslistbycid")
    public ResponseJson investsListBycid() throws AjaxOperationFailException {
        return customersService.investsListBycid();
    }



    /**
     * 消费记录查询
     * @return
     */
    @RequestMapping("consumptionlistbycusid")
    public ResponseJson consumptionListBycusid() throws AjaxOperationFailException {
        return customersService.consumptionListBycusid();
    }

    /**
     * 违约扣除订金
     * @return
     */
    @RequestMapping("kouchu")
    public ResponseJson kouchu() throws AjaxOperationFailException {
        return customersService.kouchu();
    }

}
