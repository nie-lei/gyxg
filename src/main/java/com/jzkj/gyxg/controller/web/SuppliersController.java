package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.SuppliersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/suppliers")
@RestController
public class SuppliersController extends BaseController {

    @Autowired
    private SuppliersService suppliersService;

    /**
     * 新增供应商
     * @return
     */
    @RequestMapping("/add")
    public ResponseJson add() throws AjaxOperationFailException {
        return suppliersService.add();
    }

    /**
     * 修改供应商
     * @return
     */
    @RequestMapping("/edit")
    public ResponseJson edit() throws AjaxOperationFailException {
        return suppliersService.edit();
    }

    /**
     * 删除供应商
     * @return
     */
    @RequestMapping("/del")
    public ResponseJson del() throws AjaxOperationFailException {
        return suppliersService.del();
    }

    /**
     * 供应商列表
     * @return
     */
    @RequestMapping("/list")
    public ResponseJson list() throws AjaxOperationFailException {
        return suppliersService.list();
    }
}
