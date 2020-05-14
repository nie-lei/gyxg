package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.AttachsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/attachs")
public class AttachsController extends BaseController{

    @Autowired
    private AttachsService attachsService;


    @RequestMapping("/list0")
    public ResponseJson list0() throws AjaxOperationFailException {
        return attachsService.list();
    }


    @RequestMapping("/add0")
    public ResponseJson add0() throws AjaxOperationFailException {
        return attachsService.add();
    }

    @RequestMapping("/edit0")
    public ResponseJson edit0() throws AjaxOperationFailException {
        return attachsService.edit();
    }

    @RequestMapping("/del0")
    public ResponseJson del0() throws AjaxOperationFailException {
        return attachsService.del();
    }



    @RequestMapping("/list1")
    public ResponseJson list1() throws AjaxOperationFailException {
        return attachsService.list();
    }


    @RequestMapping("/add1")
    public ResponseJson add1() throws AjaxOperationFailException {
        return attachsService.add();
    }

    @RequestMapping("/edit1")
    public ResponseJson edit1() throws AjaxOperationFailException {
        return attachsService.edit();
    }

    @RequestMapping("/del1")
    public ResponseJson del1() throws AjaxOperationFailException {
        return attachsService.del();
    }


    @RequestMapping("/list")
    public ResponseJson list() throws AjaxOperationFailException {
        return attachsService.list();
    }


    @RequestMapping("/add")
    public ResponseJson add() throws AjaxOperationFailException {
        return attachsService.add();
    }

    @RequestMapping("/edit")
    public ResponseJson edit() throws AjaxOperationFailException {
        return attachsService.edit();
    }

    @RequestMapping("/del")
    public ResponseJson del() throws AjaxOperationFailException {
        return attachsService.del();
    }
}
