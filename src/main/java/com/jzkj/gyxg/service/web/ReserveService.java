package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Customers;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Reserveds;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.AreasMapper;
import com.jzkj.gyxg.mapper.CustomersMapper;
import com.jzkj.gyxg.mapper.ReservedsMapper;
import com.jzkj.gyxg.mapper.TablesMapper;
import com.jzkj.gyxg.util.DateUtil;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReserveService {

    @Autowired
    private ReservedsMapper reservedsMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomersMapper customersMapper;
    @Autowired
    private TablesMapper tablesMapper;
    @Autowired
    private AreasMapper areasMapper;
    /**
     * 预定新增
     * @return
     */
    public ResponseJson reservedAdd() throws AjaxOperationFailException {
        //预订人*必填
        String reservedperson = HttpRequestParamter.getString("reservedperson",true);
        //联系电话*必填
        String phone = HttpRequestParamter.getString("phone",true);
        //中餐/晚餐*必填
        String canduan = HttpRequestParamter.getString("canduan",true);
        //预订到达时间*必填
        String reservedtime = HttpRequestParamter.getString("reservedtime",true);
        //人数*必填
        Integer nums = HttpRequestParamter.getInt("nums",0);
        //单位名称
        String unitname = HttpRequestParamter.getString("unitname",true);
        //备注
        String memo = HttpRequestParamter.getString("memo",true);
        //餐台流水号(多个餐台/分割)
        String tableid = HttpRequestParamter.getString("tableid",true);
        if(StringUtil.isEmpty(reservedperson)){
            throw new AjaxOperationFailException("预订人必填!");
        }
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("预订人电话必填!");
        }
        if(StringUtil.isEmpty(canduan)){
            throw new AjaxOperationFailException("餐段必填!");
        }
        if(StringUtil.isEmpty(reservedtime)){
            throw new AjaxOperationFailException("预计到达时间必填!");
        }
        if(nums == 0){
            throw new AjaxOperationFailException("用餐人数必填!");
        }
        Customers cus = customersMapper.selectCusByPhone(phone);
        Integer customerid = 0;
        if(cus == null){//如果为空，则新增一个客户返回id
            Customers cusnew = new Customers();
            //新增客户
            cusnew.setName(reservedperson);
            cusnew.setPhone(phone);
            cusnew.setRegistertime(new Date());
            cusnew.setIsVip("0");
            cusnew.setIsSign("0");
            cusnew.setStatus("0");
            customerid = customersMapper.insert(cusnew);
        }else {//不为空则取出id
            customerid = cus.getCustomerid();
        }

        //新增预定记录
        Reserveds res = new Reserveds();
        res.setCompanyid(employeeService.getLoginUser().getCompanyid());
        res.setStoreid(employeeService.getLoginUser().getStoreid());
        res.setCustomerid(customerid);
        res.setCustomerphone(phone);
        res.setReservedperson(reservedperson);
        res.setDepositmoney(0.00);
        res.setPhone(phone);
        res.setNums(nums);
        res.setUnitname(unitname);
        res.setCanduan(canduan);
        res.setIntime(new Date());
        res.setReservedtime(DateUtil.toDate(reservedtime,"yyyy-MM-dd HH:mm:ss"));
        res.setTableids(tableid);
        res.setMemo(memo);
        res.setEmployeeid1(employeeService.getLoginUser().getEmployeeid());
        res.setStatus("0");
        int count = reservedsMapper.insert(res);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增预定成功!");
        }
        return resp;
    }



    public boolean checkPhone(String phone){
        int count = customersMapper.selectByPhone(phone);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }


    public ResponseJson reservedEdit() throws AjaxOperationFailException {
        Integer reservedid = HttpRequestParamter.getInt("reservedid",0);
        //预订人*必填
        String reservedperson = HttpRequestParamter.getString("reservedperson",true);
        //联系电话*必填
        String phone = HttpRequestParamter.getString("phone",true);
        //中餐/晚餐*必填
        String canduan = HttpRequestParamter.getString("canduan",true);
        //预订到达时间*必填
        String reservedtime = HttpRequestParamter.getString("reservedtime",true);
        //人数*必填
        Integer nums = HttpRequestParamter.getInt("nums",0);
        //单位名称
        String unitname = HttpRequestParamter.getString("unitname",true);
        //备注
        String memo = HttpRequestParamter.getString("memo",true);
        //餐台流水号(多个餐台/分割)
        String tableid = HttpRequestParamter.getString("tableid",true);
        if(StringUtil.isEmpty(reservedperson)){
            throw new AjaxOperationFailException("预订人必填!");
        }
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("预订人电话必填!");
        }
        if(StringUtil.isEmpty(canduan)){
            throw new AjaxOperationFailException("餐段必填!");
        }
        if(StringUtil.isEmpty(reservedtime)){
            throw new AjaxOperationFailException("预计到达时间必填!");
        }
        if(nums == 0){
            throw new AjaxOperationFailException("用餐人数必填!");
        }
        if(reservedid == 0){
            throw new AjaxOperationFailException("参数id缺失");
        }
        Reserveds res = reservedsMapper.selectByPrimaryKey(reservedid);
        if(res == null){
            throw new AjaxOperationFailException("预定信息不存在!");
        }
        if(res.getStatus().equals("1")){
            throw new AjaxOperationFailException("顾客已到达，不能修改!");
        }
        if(res.getStatus().equals("2")){
            throw new AjaxOperationFailException("预约已取消，不能修改!");
        }
        if(!isPromission(res)){
            throw new AjaxOperationFailException("没有权限!");
        }

        Customers cus = customersMapper.selectByPrimaryKey(res.getCustomerid());
        Integer customerid = 0;
        if(reservedperson.equals(res.getReservedperson()) && phone.equals(res.getPhone())){
        }else {
            if(checkPhone(phone)){//如果新修改的号码能在客户表查到，则查出该客户表的id
                Customers cus1 = customersMapper.selectCusByPhone(phone);
                customerid=cus1.getCustomerid();
                res.setCustomerid(customerid);
            }else {
                cus.setPhone(phone);
                cus.setName(reservedperson);
                //修改客户表信息
                int count  = customersMapper.updateByPrimaryKey(cus);
            }
            res.setCustomerphone(phone);
            res.setReservedperson(reservedperson);
        }
        res.setPhone(phone);
        res.setNums(nums);
        res.setUnitname(unitname);
        res.setCanduan(canduan);
        res.setReservedtime(DateUtil.toDate(reservedtime,"yyyy-MM-dd HH:mm:ss"));
        res.setTableids(tableid);
        res.setMemo(memo);
        int count = reservedsMapper.updateByPrimaryKey(res);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改预定成功!");
        }
        return resp;
    }


    public ResponseJson reservedList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String reservedperson = HttpRequestParamter.getString("reservedperson",true);//客户名字
        String phone = HttpRequestParamter.getString("phone",true);//客户手机
        String canduan = HttpRequestParamter.getString("canduan",true);
        String stattime = HttpRequestParamter.getString("stattime",true);
        String endtime = HttpRequestParamter.getString("endtime",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isEmpty(reservedperson)){
            params.put("name",reservedperson);
        }
        if(!StringUtil.isEmpty(phone)){
            params.put("phone",phone);
        }
        if(!StringUtil.isEmpty(canduan)){
            params.put("canduan",canduan);
        }
        if(!StringUtil.isEmpty(stattime)){
            params.put("stattime",stattime);
        }
        if(!StringUtil.isEmpty(endtime)){
            params.put("endtime",endtime);
        }
        PageHelper.startPage(page,size,"reservedid desc");
        List<Map> list = reservedsMapper.selectReservedsList(params);
        List<Map> tablelist = tablesMapper.selectAlltablesByCsId(params);
        String tables = "";
        for (Map  reserveds : list) {
            if(!StringUtil.isEmpty(reserveds.get("tableids").toString())){
                String[] tableids = (reserveds.get("tableids").toString()).split(",");
                if(tableids.length > 0){
                    for (String tableid : tableids) {
                        for (Map table : tablelist) {
                            if(tableid.equals(table.get("tableid").toString())){
                                tables+=table.get("code")+",";
                            }
                        }
                    }
                }
                reserveds.put("tables",tables);
            }else {
                reserveds.put("tables","");
            }
        }

        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    public ResponseJson reservedCancel() throws AjaxOperationFailException {
        Integer reservedid = HttpRequestParamter.getInt("reservedid",0);
        if(reservedid == 0){
            throw new AjaxOperationFailException("参数id缺失");
        }
        Reserveds reserveds = reservedsMapper.selectByPrimaryKey(reservedid);
        if(reserveds == null){
            throw new AjaxOperationFailException("预定信息不存在!");
        }
        if(!isPromission(reserveds)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(reserveds.getStatus().equals("1")){
            throw new AjaxOperationFailException("就餐人已经到达，不能取消!");
        }
        if(reserveds.getStatus().equals("2")){
            throw new AjaxOperationFailException("预定已经取消，不能重复操作!");
        }
        reserveds.setStatus("2");
        int count = reservedsMapper.updateByPrimaryKey(reserveds);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("取消成功!");
        }
        return resp;
    }


    public boolean isPromission(Reserveds master){
        Employees employees = employeeService.getLoginUser();
        if(((int)employees.getCompanyid()==(int)master.getCompanyid()) && ((int)employees.getStoreid()==(int)master.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    public ResponseJson reservedArrive() throws AjaxOperationFailException {
        Integer reservedid = HttpRequestParamter.getInt("reservedid",0);
        if(reservedid == 0){
            throw new AjaxOperationFailException("参数id缺失");
        }
        Reserveds reserveds = reservedsMapper.selectByPrimaryKey(reservedid);
        if(reserveds == null){
            throw new AjaxOperationFailException("预定信息不存在!");
        }
        if(!isPromission(reserveds)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(reserveds.getStatus().equals("1")){
            throw new AjaxOperationFailException("已经到达，不能重复操作!");
        }
        if(reserveds.getStatus().equals("2")){
            throw new AjaxOperationFailException("预定已经取消，不能操作!");
        }
        reserveds.setStatus("1");//已经到达
        reserveds.setRealitytime(new Date());

        int count = reservedsMapper.updateByPrimaryKey(reserveds);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("操作成功!");
        }
        return resp;
    }

}
