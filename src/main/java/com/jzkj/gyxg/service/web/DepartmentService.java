package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Departments;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.DepartmentsMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentsMapper departmentsMapper;

    @Autowired
    private EmployeeService employeeService;
    /**
     * 部门列表
     * @return
     */
    public ResponseJson list() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        PageHelper.startPage(page,size,"departmentid asc");
        List<Map> list = departmentsMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 新增部门
     * @return
     */
    public ResponseJson add() throws AjaxOperationFailException {
        String departmentno = HttpRequestParamter.getString("departmentno");
        String name = HttpRequestParamter.getString("name");
        if(StringUtil.isEmpty(departmentno)){
            throw new AjaxOperationFailException("部门编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("部门名称不能为空!");
        }
        Employees employees = employeeService.getLoginUser();
        Departments departments = new Departments();
        departments.setDepartmentno(departmentno);
        departments.setName(name);
        departments.setStatus("0");
        departments.setStoreid(employees.getStoreid());
        departments.setCompanyid(employees.getCompanyid());
        if(checkDepartment(departments)){
            throw new AjaxOperationFailException("部门名称或者编号重复!");
        }
        ResponseJson responseJson = new ResponseJson();
        int flag = departmentsMapper.insert(departments);
        if(flag>0){
            responseJson.setMsg("新增部门成功!");
        }
        return responseJson;
    }

    /**
     * 检查是否有重复
     * @param departments
     * @return
     */
    public boolean checkDepartment(Departments departments){
        int flag = departmentsMapper.checkDepartment(departments);
        if(flag>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改部门信息
     * @return
     * @throws AjaxOperationFailException
     */
    public ResponseJson edit() throws AjaxOperationFailException {
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);
        String departmentno = HttpRequestParamter.getString("departmentno");
        String name = HttpRequestParamter.getString("name");
        if(departmentid==0){
            throw new AjaxOperationFailException("部门id不能为空!");
        }
        if(StringUtil.isEmpty(departmentno)){
            throw new AjaxOperationFailException("部门编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("部门名称不能为空!");
        }
        Departments departments = departmentsMapper.selectByPrimaryKey(departmentid);
        if(!isPromission(departments)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!departments.getDepartmentno().equals(departmentno)){
            departments.setDepartmentno(departmentno);
            int count = departmentsMapper.checkDepartmentbycode(departments);
            if(count > 0){
                throw new AjaxOperationFailException("部门编号重复!");
            }
        }
        if(!departments.getName().equals(name)) {
            departments.setName(name);
            int count = departmentsMapper.checkDepartmentbyname(departments);
            if(count > 0){
                throw new AjaxOperationFailException("部门名称重复!");
            }
        }
        ResponseJson responseJson = new ResponseJson();
        int flag = departmentsMapper.updateByPrimaryKey(departments);
        if(flag>0){
            responseJson.setMsg("修改部门成功!");
        }
        return responseJson;
    }


    public boolean isPromission(Departments departments){
        Employees employees = employeeService.getLoginUser();
        if(((int)employees.getCompanyid()==(int)departments.getCompanyid()) && ((int)employees.getStoreid()==(int)departments.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 删除部门
     * @return
     */
    public ResponseJson del() throws AjaxOperationFailException {
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);
        if(departmentid==0){
            throw new AjaxOperationFailException("部门id不能为空!");
        }
        Departments departments = departmentsMapper.selectByPrimaryKey(departmentid);
        if(departments==null){
            throw new AjaxOperationFailException("部门信息不存在!");
        }
        if(!isPromission(departments)){//检查权限，防止恶意操作
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = departmentsMapper.selectEmpCountByDeptid(departmentid);
        ResponseJson resp = new ResponseJson();
        if(count>0){
            throw new AjaxOperationFailException("该部门下面有员工"+count+"位!删除失败,请先安置好员工!");
        }else {
            departmentsMapper.deleteByPrimaryKey(departmentid);
            resp.setMsg("删除成功");
        }
        return resp;
    }

    /**
     * 部门下拉列表
     * @return
     */
    public ResponseJson selectDept() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = departmentsMapper.selectDept(params);
        resp.setData(list);
        return resp;
    }


}
