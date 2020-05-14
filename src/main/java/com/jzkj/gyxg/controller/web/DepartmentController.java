package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/departments")
public class DepartmentController extends BaseController{

    @Autowired
    private DepartmentService departmentService;


    @RequestMapping("/list")
    public ResponseJson list(){
        return departmentService.list();
    }


    @RequestMapping("/add")
    public ResponseJson add() throws AjaxOperationFailException {
        return departmentService.add();
    }

    @RequestMapping("/edit")
    public ResponseJson edit() throws AjaxOperationFailException {
        return departmentService.edit();
    }
    @RequestMapping("/del")
    public ResponseJson del() throws AjaxOperationFailException {
        return departmentService.del();
    }
}
