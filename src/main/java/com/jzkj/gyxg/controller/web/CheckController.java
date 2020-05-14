package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("api/check/")
@RestController
public class CheckController extends BaseController{

    @Autowired
    private CheckService checkService;

    /**
     * 盘点信息新增
     * @return
     */
    @RequestMapping("checkmasteradd")
    public ResponseJson checkMasterAdd() throws AjaxOperationFailException {
        return checkService.checkMasterAdd();
    }

    /**
     * 盘点信息修改
     * @return
     */
    @RequestMapping("checkmasteredit")
    public ResponseJson checkMasterEdit() throws AjaxOperationFailException {
        return checkService.checkMasterEdit();
    }

    /**
     * 盘点信息取消
     * @return
     */
    @RequestMapping("checkmastercancel")
    public ResponseJson checkMasterCancel() throws AjaxOperationFailException {
        return checkService.checkMasterCancel();
    }

    /**
     * 盘点信息列表
     * @return
     */
    @RequestMapping("checkmasterlist")
    public ResponseJson checkMasterList() throws AjaxOperationFailException {
        return checkService.checkMasterList();
    }

    /**
     * 盘点信息审核
     * @return
     */
    @RequestMapping("checkmasterauth")
    public ResponseJson checkMasterAuth() throws AjaxOperationFailException {
        return checkService.checkMasterAuth();
    }

    /**
     * 盘点原料新增
     * @return
     */
    @RequestMapping("checkslaveradd")
    public ResponseJson checkSlaverAdd() throws AjaxOperationFailException {
        return checkService.checkSlaverAdd();
    }

    /**
     * 盘点原料修改
     * @return
     */
    @RequestMapping("checkslaveredit")
    public ResponseJson checkSlaverEdit() throws AjaxOperationFailException {
        return checkService.checkSlaverEdit();
    }

    /**
     * 盘点原料删除
     * @return
     */
    @RequestMapping("checkslaverdel")
    public ResponseJson checkSlaverDel() throws AjaxOperationFailException {
        return checkService.checkSlaverDel();
    }

    /**
     * 盘点原料列表
     * @return
     */
    @RequestMapping("checkslaverlist")
    public ResponseJson checkSlaverList() throws AjaxOperationFailException {
        return checkService.checkSlaverList();
    }

    /**
     * 月末结转
     * @return
     * @throws AjaxOperationFailException
     */
    @Deprecated
//    @RequestMapping("jiezhuan")
    public ResponseJson jiezhuan() throws AjaxOperationFailException {
        return checkService.jiezhuan();
    }

    /**
     * 月末盘点
     * @return
     * @throws AjaxOperationFailException
     */
    @PostMapping("jiezhuan")
    public ResponseJson monthEndCheck()throws  AjaxOperationFailException{
        return checkService.monthEndCheck();
    }


}
