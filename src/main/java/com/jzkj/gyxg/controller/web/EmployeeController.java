package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("api/employees")
public class EmployeeController extends BaseController {

    @Autowired
    private EmployeeService employeeService;


    @RequestMapping("/login")
    public ResponseJson ajaxLogin() throws AjaxOperationFailException {
        return employeeService.ajaxLogin();
    }

    @RequestMapping("/menu")
    public ResponseJson menu() throws AjaxOperationFailException {
        return employeeService.menu();
    }

    @RequestMapping("/updatepassword")
    public ResponseJson updatePassword(HttpServletRequest req) throws AjaxOperationFailException {
        return employeeService.updatePassword(req);
    }
    @RequestMapping("/logout")
    public ResponseJson ajaxLogout(HttpServletRequest req) throws AjaxOperationFailException {
        return employeeService.ajaxLogout(req);
    }

    @RequestMapping("/list")
    public ResponseJson list() throws AjaxOperationFailException {
        return employeeService.list();
    }

    @RequestMapping("/add")
    public ResponseJson add() throws AjaxOperationFailException {
        return employeeService.add();
    }

    @RequestMapping("/edit")
    public ResponseJson edit() throws AjaxOperationFailException {
        return employeeService.edit();
    }

    @RequestMapping("/editpasswd")
    public ResponseJson editpasswd() throws AjaxOperationFailException {
        return employeeService.editPasswd();
    }

    @RequestMapping("/editdiscountamount")
    public ResponseJson editDiscountamount() throws AjaxOperationFailException {
        return employeeService.editDiscountamount();
    }

    @RequestMapping("/del")
    public ResponseJson del() throws AjaxOperationFailException {
        return employeeService.del();
    }

    @RequestMapping("/home")
    public ResponseJson home() throws AjaxOperationFailException {
        return employeeService.home();
    }

}
