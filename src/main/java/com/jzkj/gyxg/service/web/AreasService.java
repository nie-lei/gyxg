package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Areas;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.AreasMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AreasService {

    @Autowired
    private AreasMapper areasMapper;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 区域信息列表
     * @return
     */
    public ResponseJson areasList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        PageHelper.startPage(page,size,"areaid asc");
        List<Map> list = areasMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 区域信息新增
     * @return
     */
    public ResponseJson areasAdd() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name");
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("区域名称不能为空!");
        }
        Employees employees = employeeService.getLoginUser();
        Areas areas = new Areas();
        areas.setName(name);
        if(employeeid!=0){
            areas.setEmployeeid(employeeid);
        }
        areas.setStoreid(employees.getStoreid());
        areas.setCompanyid(employees.getCompanyid());
        areas.setStatus("0");
        if(checkAreas(areas)){
            throw new AjaxOperationFailException("区域名称重复!");
        }
        ResponseJson responseJson = new ResponseJson();
        int flag = areasMapper.insert(areas);
        if(flag>0){
            responseJson.setMsg("新增区域成功!");
        }
        return responseJson;
    }

    public boolean checkAreas(Areas areas){
        int count = areasMapper.checkAreas(areas);
        if(count>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改区域信息
     * @return
     */
    public ResponseJson areasEdit() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name");
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        Integer areaid = HttpRequestParamter.getInt("areaid",0);

        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("区域名称不能为空!");
        }
        if(areaid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        Areas areas = areasMapper.selectByPrimaryKey(areaid);
        if(employeeid!=0){
            areas.setEmployeeid(employeeid);
        }
        if(areas == null){
            throw new AjaxOperationFailException("需要修改的区域信息不存在!");
        }
        if(!isPomissionAreas(areas)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!name.equals(areas.getName())){
            areas.setName(name);
            if(checkAreas(areas)){
                throw new AjaxOperationFailException("区域名称重复!");
            }
        }
        ResponseJson responseJson = new ResponseJson();
        int flag = areasMapper.updateByPrimaryKey(areas);
        if(flag>0){
            responseJson.setMsg("修改区域成功!");
        }
        return responseJson;
    }

    public boolean isPomissionAreas(Areas area){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)area.getCompanyid()) && ((int)loginuser.getStoreid()==(int)area.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 区域信息下拉列表
     * @return
     */
    public ResponseJson selectAreas() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = areasMapper.selectRoles(params);
        resp.setData(list);
        return resp;
    }

    /**
     * 删除区域信息
     * @return
     */
    public ResponseJson areasDel() throws AjaxOperationFailException {
        Integer areaid = HttpRequestParamter.getInt("areaid",0);
        if(areaid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        Areas areas = areasMapper.selectByPrimaryKey(areaid);
        if(!isPomissionAreas(areas)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count  = areasMapper.selectTableCountByAreaid(areaid);
        if(count>0){
            throw new AjaxOperationFailException("目前有"+count+"个餐桌信息绑定了该就餐区域，删除失败!");
        }
        int count1 = areasMapper.deleteByPrimaryKey(areaid);
        ResponseJson resp = new ResponseJson();
        if(count1>0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }

    /**
     * 开启关闭区域
     * @return
     */
    public ResponseJson areasChange() throws AjaxOperationFailException {
        Integer areaid = HttpRequestParamter.getInt("areaid",0);
        String status = HttpRequestParamter.getString("status",true);
        if(areaid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(StringUtil.isEmpty(status)){
            throw new AjaxOperationFailException("开启关闭状态不能为空!");
        }
        Areas areas = areasMapper.selectByPrimaryKey(areaid);
        if(areas==null){
            throw new AjaxOperationFailException("区域信息不存在!");
        }
        if(!isPomissionAreas(areas)){
            throw new AjaxOperationFailException("没有权限!");
        }
        areas.setStatus(status);
        int count = areasMapper.updateByPrimaryKey(areas);
        ResponseJson resp = new ResponseJson();
        if(count>0){
            if(status.equals("0")){
                resp.setMsg("开启成功!");
            }else {
                resp.setMsg("关闭成功!");
            }
        }
        return resp;
    }
}
