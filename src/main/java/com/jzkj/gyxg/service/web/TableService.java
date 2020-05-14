package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Tables;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.StoresMapper;
import com.jzkj.gyxg.mapper.TablesMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TableService {

    @Autowired
    private TablesMapper tablesMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private StoresMapper storesMapper;


    /**
     * 餐桌信息新增
     * @return
     */
    public ResponseJson tableAdd() throws AjaxOperationFailException {
        String code = HttpRequestParamter.getString("code");//餐台编号
        String name = HttpRequestParamter.getString("name");//餐台名称
        Integer seats = HttpRequestParamter.getInt("seats",0);//座位数量
        Integer areaid = HttpRequestParamter.getInt("areaid",0);//区域id
        double minmoney = HttpRequestParamter.getDouble("minmoney",0.00);//最低消费
        double fixedfee = HttpRequestParamter.getDouble("fixedfee",0.00);//按金额收取服务费
        String memo = HttpRequestParamter.getString("memo",true);//备注
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("餐台编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("餐台名称不能为空!");
        }
        if(seats==0){
            throw new AjaxOperationFailException("座位数不能为空!");
        }
        if(areaid == 0){
            throw new AjaxOperationFailException("请选择餐桌所在区域!");
        }
        Tables tables = new Tables();
        tables.setName(name);
        tables.setAreaid(areaid);
        tables.setCode(code);
        tables.setCompanyid(employeeService.getLoginUser().getCompanyid());
        tables.setStoreid(employeeService.getLoginUser().getStoreid());
        tables.setSeats(seats);
        tables.setFlag("0");
        if(!StringUtil.isEmpty(memo)){
            tables.setMemo(memo);
        }
        if(employeeid!=0){
            tables.setEmployeeid(employeeid);
        }
        tables.setMinmoney(minmoney);
        tables.setFixedfee(fixedfee);
        tables.setSeatfee(storesMapper.selectByPrimaryKey(employeeService.getLoginUser().getStoreid()).getSeatmoney());
        tables.setStatus("0");
        if(checkTables(tables)){
            throw new AjaxOperationFailException("餐台编号或者名称重复!");
        }
        int count = tablesMapper.insert(tables);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增成功!");
        }
        return resp;
    }


    /**
     * 餐桌信息修改
     * @return
     */
    public ResponseJson tableEdit() throws AjaxOperationFailException {
        Integer tableid = HttpRequestParamter.getInt("tableid",0);
        String code = HttpRequestParamter.getString("code");//餐台编号
        String name = HttpRequestParamter.getString("name");//餐台名称
        Integer seats = HttpRequestParamter.getInt("seats",0);//座位数量
        Integer areaid = HttpRequestParamter.getInt("areaid",0);//区域id
        double minmoney = HttpRequestParamter.getDouble("minmoney",0.00);//最低消费
        double fixedfee = HttpRequestParamter.getDouble("fixedfee",0.00);//按金额收取服务费
        String memo = HttpRequestParamter.getString("memo",true);//备注
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("餐台编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("餐台名称不能为空!");
        }
        if(seats==0){
            throw new AjaxOperationFailException("座位数不能为空!");
        }
        if(areaid == 0){
            throw new AjaxOperationFailException("请选择餐桌所在区域!");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tableid);
        if(tables==null){
            throw new AjaxOperationFailException("餐桌信息不存在!");
        }
        if(!isPomissionTables(tables)){
            throw new AjaxOperationFailException("没有权限!");
        }
        tables.setAreaid(areaid);
        tables.setSeats(seats);
        if(!StringUtil.isEmpty(memo)){
            tables.setMemo(memo);
        }
        if(employeeid!=0){
            tables.setEmployeeid(employeeid);
        }
        tables.setMinmoney(minmoney);
        tables.setFixedfee(fixedfee);
        tables.setSeatfee(storesMapper.selectByPrimaryKey(employeeService.getLoginUser().getStoreid()).getSeatmoney());
        if(!code.equals(tables.getCode())){
            tables.setCode(code);
            int count = tablesMapper.checkTablesbycode(tables);
            if(count > 0){
                throw new AjaxOperationFailException("餐台编号重复!");
            }
        }
        if(!name.equals(tables.getName())) {
            tables.setName(name);
            int count = tablesMapper.checkTablesbyname(tables);
            if(count > 0){
                throw new AjaxOperationFailException("餐台名称重复!");
            }
        }
        int count = tablesMapper.updateByPrimaryKey(tables);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改成功!");
        }
        return resp;
    }

    /**
     * 检查是否重复
     * @param tables
     * @return
     */
    public boolean checkTables(Tables tables){
        int count = tablesMapper.checkTables(tables);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 餐桌信息列表
     * @return
     */
    public ResponseJson tablesList() {
        Integer areaid = HttpRequestParamter.getInt("areaid",0);//区域id
        String flag = HttpRequestParamter.getString("flag",true);//标识：0-空闲 1-预定 2-占用
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Integer seats = HttpRequestParamter.getInt("seats",0);//座位数量
        Map params = new HashMap();
        Employees loginuser = employeeService.getLoginUser();
        params.put("companyid",loginuser.getCompanyid());
        params.put("storeid",loginuser.getStoreid());
        if(areaid != 0){
            params.put("areaid",areaid);
        }
        if(flag != null &&("0".equals(flag) || "1".equals(flag) || "2".equals(flag))){
            params.put("flag",flag);
        }
        if(seats!=0){
            params.put("seats",seats);
        }
        PageHelper.startPage(page,size," tableid asc");
        List<Map> list = tablesMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 开启关闭餐桌
     * @return
     */
    public ResponseJson tableChange() throws AjaxOperationFailException {
        Integer tableid = HttpRequestParamter.getInt("tableid",0);
        String status = HttpRequestParamter.getString("status",true);
        if(tableid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(StringUtil.isEmpty(status)){
            throw new AjaxOperationFailException("开启关闭状态不能为空!");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tableid);
        if(tables==null){
            throw new AjaxOperationFailException("餐桌信息不存在!");
        }
        if(!isPomissionTables(tables)){
            throw new AjaxOperationFailException("没有权限!");
        }
        tables.setStatus(status);
        int count = tablesMapper.updateByPrimaryKey(tables);
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


    public boolean isPomissionTables(Tables tables){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)tables.getCompanyid()) && ((int)loginuser.getStoreid()==(int)tables.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    public ResponseJson tablesDel() throws AjaxOperationFailException {
        Integer tableid = HttpRequestParamter.getInt("tableid",0);
        if(tableid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tableid);
        if(tables==null){
            throw new AjaxOperationFailException("餐桌信息不存在!");
        }
        if(!isPomissionTables(tables)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!"0".equals(tables.getFlag())){
            throw new AjaxOperationFailException("餐桌占用中，无法删除!");
        }
        int count = tablesMapper.deleteByPrimaryKey(tableid);
        ResponseJson resp = new ResponseJson();
        if(count>0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }

}
