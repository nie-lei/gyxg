package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.PaytypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/paytype/")
public class PaytypeController extends BaseController{

    @Autowired
    private PaytypeService paytypeService;


    /**
     * 支付信息列表
     * @return
     */
    @RequestMapping("list")
    public ResponseJson list(){
        return paytypeService.list();
    }


    /**
     * 支付信息新增
     * @return
     */
    @RequestMapping("add")
    public ResponseJson add() throws AjaxOperationFailException {
        return paytypeService.add();
    }

    /**
     * 支付信息修改
     * @return
     */
    @RequestMapping("edit")
    public ResponseJson edit() throws AjaxOperationFailException {
        return paytypeService.edid();
    }


    /**
     * 支付信息上下线设置
     * @return
     */
    @RequestMapping("changestatus")
    public ResponseJson changestatus() throws AjaxOperationFailException {
        return paytypeService.changestatus();
    }


    /**
     * 删除
     * @return
     */
    @RequestMapping("del")
    public ResponseJson del() throws AjaxOperationFailException {
        return paytypeService.del();
    }

    /**
     * 获取支付方式
     * @return
     */
    @RequestMapping("selectallquan")
    public ResponseJson selectallquan() throws AjaxOperationFailException {
        return paytypeService.selectallquan();
    }


    /**
     * 获取支付方式(充值，订金)
     * @return
     */
    @RequestMapping("selectpay")
    public ResponseJson selectpay() throws AjaxOperationFailException {
        return paytypeService.selectpay();
    }


}
