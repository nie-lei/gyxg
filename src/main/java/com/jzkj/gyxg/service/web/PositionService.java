package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Positions;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.PositionsMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PositionService {

    @Autowired
    private PositionsMapper positionMapper;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 查询职位列表
     * @return
     */
    public ResponseJson list(){
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        PageHelper.startPage(page,size,"positionid asc");
        List<Map> list = positionMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 新增职位信息
     * @return
     */
    public ResponseJson add() throws AjaxOperationFailException {
        String positionno = HttpRequestParamter.getString("positionno");
        String name = HttpRequestParamter.getString("name");
        if(StringUtil.isEmpty(positionno)){
            throw new AjaxOperationFailException("职位编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("职位名称不能为空!");
        }
        Employees employees = employeeService.getLoginUser();
        Positions position = new Positions();
        position.setName(name);
        position.setPositionno(positionno);
        position.setCompanyid(employees.getCompanyid());
        position.setStoreid(employees.getStoreid());
        position.setStatus("0");
        if(checkPosition(position)){
            throw new AjaxOperationFailException("职位名称或者编号重复!");
        }
        ResponseJson responseJson = new ResponseJson();
        int flag = positionMapper.insert(position);
        if(flag>0){
            responseJson.setMsg("新增职位成功!");
        }
        return responseJson;
    }

    /**
     * 修改职位信息
     * @return
     */
    public ResponseJson edit() throws AjaxOperationFailException {
        Integer positionid = HttpRequestParamter.getInt("positionid",0);
        String positionno = HttpRequestParamter.getString("positionno");
        String name = HttpRequestParamter.getString("name");
        if(positionid==0){
            throw new AjaxOperationFailException("职位id不能为空!");
        }
        if(StringUtil.isEmpty(positionno)){
            throw new AjaxOperationFailException("职位编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("职位名称不能为空!");
        }
        Positions position = positionMapper.selectByPrimaryKey(positionid);
        if(!isPromission(position)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!position.getPositionno().equals(positionno)){
            position.setPositionno(positionno);
            int count = positionMapper.checkPositionbycode(position);
            if(count > 0){
                throw new AjaxOperationFailException("职位编号重复!");
            }
        }
        if(!position.getName().equals(name)){
            position.setName(name);
            int count = positionMapper.checkPositionbyname(position);
            if(count > 0){
                throw new AjaxOperationFailException("职位名称重复!");
            }
        }
        ResponseJson responseJson = new ResponseJson();
        int flag = positionMapper.updateByPrimaryKey(position);
        if(flag>0){
            responseJson.setMsg("修改职位成功!");
        }
        return responseJson;
    }


    /**
     * 检查是否有重复
     * @param position
     * @return
     */
    public boolean checkPosition(Positions position){
        int flag = positionMapper.checkPosition(position);
        if(flag>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPromission(Positions position){
        Employees employees = employeeService.getLoginUser();
        if(((int)employees.getCompanyid()==(int)position.getCompanyid()) && ((int)employees.getStoreid()==(int)position.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 删除职位信息
     * @return
     */
    public ResponseJson del() throws AjaxOperationFailException {
        Integer positionid = HttpRequestParamter.getInt("positionid",0);
        if(positionid==0){
            throw new AjaxOperationFailException("职位id不能为空!");
        }
        Positions positions = positionMapper.selectByPrimaryKey(positionid);
        if(positions==null){
            throw new AjaxOperationFailException("职位信息不存在!");
        }
        if(!isPromission(positions)){//检查权限，防止恶意操作
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = positionMapper.selectEmpCountByPositiontid(positions);
        ResponseJson resp = new ResponseJson();
        if(count>0){
            throw new AjaxOperationFailException("该职位下面有员工"+count+"位!删除失败,请先安置好员工!");
        }else {
            positionMapper.deleteByPrimaryKey(positionid);
            resp.setMsg("删除成功");
        }
        return resp;
    }


    /**
     * 职位下拉列表数据
     * @return
     */
    public ResponseJson selectPosition() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = positionMapper.selectPosition(params);
        resp.setData(list);
        return resp;
    }
}
