package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Roles;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.MenuMapper;
import com.jzkj.gyxg.mapper.RoleMenuMapper;
import com.jzkj.gyxg.mapper.RolesMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RolesService {

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 角色列表
     * @return
     */
    public ResponseJson list() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        PageHelper.startPage(page,size,"roleid asc");
        List<Map> list = rolesMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    public ResponseJson selectRoles() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = rolesMapper.selectRoles(params);
        resp.setData(list);
        return resp;
    }

    /**
     * 角色新增
     * @return
     */
    public ResponseJson add() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name");
        String description = HttpRequestParamter.getString("description");
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("角色名称必填");
        }
        Map param = new HashMap();
        param.put("companyid",employeeService.getLoginUser().getCompanyid());
        param.put("storeid",employeeService.getLoginUser().getStoreid());
        param.put("name",name);
        Integer count = rolesMapper.selectByName(param);
        if(count > 0){
            throw new AjaxOperationFailException("角色名称重复");
        }
        Roles roles = new Roles();
        roles.setCompanyid(employeeService.getLoginUser().getCompanyid());
        roles.setStoreid(employeeService.getLoginUser().getStoreid());
        roles.setName(name);
        roles.setStatus("0");
        roles.setDescription(description);
        rolesMapper.insert(roles);
        return new ResponseJson("新增成功!");
    }


    public ResponseJson edit() throws AjaxOperationFailException {
        Integer roleid = HttpRequestParamter.getInt("roleid",0);
        String name = HttpRequestParamter.getString("name");
        String description = HttpRequestParamter.getString("description");
        if(roleid == 0){
            throw new AjaxOperationFailException("id缺失");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("角色名称必填");
        }
        Roles roles = rolesMapper.selectByPrimaryKey(roleid);
        if(roles != null){
            if(!name.equals(roles.getName())){
                Map param = new HashMap();
                param.put("companyid",employeeService.getLoginUser().getCompanyid());
                param.put("storeid",employeeService.getLoginUser().getStoreid());
                param.put("name",name);
                Integer count = rolesMapper.selectByName(param);
                if(count > 0){
                    throw new AjaxOperationFailException("角色名称重复");
                }
            }
        }else {
            throw new AjaxOperationFailException("修改的角色不存在");
        }
        roles.setName(name);
        roles.setDescription(description);
        rolesMapper.updateByPrimaryKey(roles);
        return new ResponseJson("修改成功!");
    }

    public ResponseJson del() throws AjaxOperationFailException {
        Integer roleid = HttpRequestParamter.getInt("roleid",0);
        if(roleid == 0){
            throw new AjaxOperationFailException("id缺失");
        }
        Roles roles = rolesMapper.selectByPrimaryKey(roleid);
        if(roles == null){
            throw new AjaxOperationFailException("角色信息不存在！");
        }
        Integer count = rolesMapper.selectEmpCountByRoleid(roleid);
        if(count > 0){
            throw new AjaxOperationFailException("该角色绑定"+count+"个员工，不能删除，如果删除请先解绑!");
        }
        rolesMapper.deleteByPrimaryKey(roleid);
        return new ResponseJson("删除成功！");
    }

    public ResponseJson show_menu() throws AjaxOperationFailException {
        Integer roleid = HttpRequestParamter.getInt("roleid",0);//角色id
        if(roleid==0){
            throw new AjaxOperationFailException("缺少角色id");
        }
        Map menulist0 = menuMapper.searchMenuAll0(); //查询出 所有的启用菜单
        List<Map> menulist1 = menuMapper.searchMenuAll1(); //查询出 所有的启用菜单
        List<Map> menulist2 = menuMapper.searchMenuAll2(); //查询出 所有的启用菜单
        List<Map> menulist3 = menuMapper.searchMenuAll3(); //查询出 所有的启用菜单
        List<Integer> list = menuMapper.selectMenuidByRoleid(roleid);//通过角色id查询角色拥有菜单
        menulist0.put("children",menulist1);
        List menulist = new ArrayList();
        menulist.add(menulist0);
        Map resultMap = new HashMap();
        resultMap.put("menu",menulist);
        resultMap.put("list",list);
        for (Map menu1 :menulist1) {
            List<Map> menu1list = new ArrayList<>();
            for (Map menu2 :menulist2) {
                    List<Map> menu2list = new ArrayList<>();
                    if(menu2.get("pid").equals(menu1.get("id"))){
                        menu1list.add(menu2);
                    }
                    for (Map menu3 :menulist3) {
                        if(menu3.get("pid").equals(menu2.get("id"))){
                            menu2list.add(menu3);
                        }
                    }
                menu2.put("children",menu2list);
            }
            menu1.put("children",menu1list);
            menu1.remove("pid");
        }
        return new ResponseJson(resultMap);
    }

    public ResponseJson updateRoleMenu() throws AjaxOperationFailException {
        Integer roleid = HttpRequestParamter.getInt("roleid",0);
        String menuidstr = HttpRequestParamter.getString("menuidstr",true);
        if(roleid == 0){
            throw new AjaxOperationFailException("id缺失");
        }
        if(roleid==1){
            throw new AjaxOperationFailException("超级管理员权限不能修改");
        }
        Roles roles = rolesMapper.selectByPrimaryKey(roleid);
        if(roles == null){
            throw new AjaxOperationFailException("角色信息不存在！");
        }
        Employees loginuser = employeeService.getLoginUser();
        if(loginuser.getRoleid().equals(roleid)){
            throw new AjaxOperationFailException("超级管理员不能修改自己的权限，当前拥有所有权限！");
        }
        //删除之前的权限
        roleMenuMapper.deleteByRoleid(roleid);
        //重新更新权限
        Map param = new HashMap();
        param.put("roleid",roleid);
        param.put("menuidstr",menuidstr);
        roleMenuMapper.updateRoleMenu(param);
        return new ResponseJson("修改成功");
    }
}
