package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/roles")
public class RolesController extends BaseController{

    @Autowired
    private RolesService rolesService;

    /**
     * 角色列表
     * @return
     */
    @RequestMapping("list")
    public ResponseJson list(){
        return rolesService.list();
    }

    /**
     * 角色新增
     * @return
     */
    @RequestMapping("add")
    public ResponseJson add() throws AjaxOperationFailException {
        return rolesService.add();
    }

    /**
     * 角色新增
     * @return
     */
    @RequestMapping("edit")
    public ResponseJson edit() throws AjaxOperationFailException {
        return rolesService.edit();
    }

    /**
     * 删除角色
     * @return
     */
    @RequestMapping("del")
    public ResponseJson del() throws AjaxOperationFailException {
        return rolesService.del();
    }

    /**
     * 查看角色权限
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/showrolemenu")
    public ResponseJson show_menu() throws AjaxOperationFailException {
        return rolesService.show_menu();
    }

    @RequestMapping("/updaterolemenu")
    public ResponseJson updateRoleMenu() throws AjaxOperationFailException {
        return rolesService.updateRoleMenu();
    }


}
