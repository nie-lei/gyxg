package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.*;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.GoodsMapper;
import com.jzkj.gyxg.mapper.GoodsStockMapper;
import com.jzkj.gyxg.mapper.LossMastersMapper;
import com.jzkj.gyxg.mapper.LossSlavesMapper;
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
public class LossService {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LossMastersMapper lossMastersMapper;
    @Autowired
    private LossSlavesMapper lossSlavesMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsStockMapper goodsStockMapper;


    /**
     * 报损新增
     * @return
     */
    public ResponseJson lossAdd() throws AjaxOperationFailException {
        String code = HttpRequestParamter.getString("code",true);
        String originalcode = HttpRequestParamter.getString("originalcode",true);//原始凭证
        String checkdate = HttpRequestParamter.getString("checkdate",true);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);//仓库id
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);
        String memo = HttpRequestParamter.getString("memo",true);

        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("单据编号不能为空!");
        }
        if(StringUtil.isEmpty(checkdate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
        if(warehouseid==0){
            throw new AjaxOperationFailException("请选择仓库!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
        LossMasters master = new LossMasters();
        master.setCode(code);
        master.setOriginalcode(originalcode);
        master.setCheckdate(DateUtil.toDate(checkdate));
        master.setTotalmoney(0.00);
        master.setWarehouseid(warehouseid);
        master.setEmployee1id(employee1id);
        master.setEmployee2id(employeeService.getLoginUser().getEmployeeid());
        master.setMemo(memo);
        master.setStatus("0");
        master.setIntime(new Date());
        master.setCompanyid(employeeService.getLoginUser().getCompanyid());
        master.setStoreid(employeeService.getLoginUser().getStoreid());
        int count = lossMastersMapper.insert(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增报损成功");
        }
        return resp;
    }

    /**
     * 报损修改
     * @return
     */
    public ResponseJson lossEdit() throws AjaxOperationFailException {
        Integer lossid = HttpRequestParamter.getInt("lossid",0);//仓库id
        String code = HttpRequestParamter.getString("code",true);
        String originalcode = HttpRequestParamter.getString("originalcode",true);//原始凭证
        String checkdate = HttpRequestParamter.getString("checkdate",true);
        double totalmoney = HttpRequestParamter.getDouble("totalmoney",0);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);//仓库id
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(lossid==0){
            throw new AjaxOperationFailException("单据id不能为空!");
        }
        LossMasters master = lossMastersMapper.selectByPrimaryKey(lossid);
        if(master == null){
            throw new AjaxOperationFailException("单据信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("单据已经审核，不能修改!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("单据编号不能为空!");
        }

        if(StringUtil.isEmpty(checkdate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
        if(totalmoney==0){
            throw new AjaxOperationFailException("单据金额不能为空!");
        }
        if(warehouseid==0){
            throw new AjaxOperationFailException("请选择仓库!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
        master.setCode(code);
        master.setOriginalcode(originalcode);
        master.setCheckdate(DateUtil.toDate(checkdate));
        master.setTotalmoney(totalmoney);
        master.setWarehouseid(warehouseid);
        master.setEmployee1id(employee1id);
        master.setEmployee2id(employeeService.getLoginUser().getEmployeeid());
        master.setMemo(memo);
        int count = lossMastersMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改报损成功");
        }
        return resp;
    }

    public boolean isPromission(LossMasters master){
        Employees employees = employeeService.getLoginUser();
        if(((int)employees.getCompanyid()==(int)master.getCompanyid()) && ((int)employees.getStoreid()==(int)master.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 报损取消
     * @return
     */
    public ResponseJson lossCancel() throws AjaxOperationFailException {
        Integer lossid = HttpRequestParamter.getInt("lossid",0);//仓库id
        if(lossid==0){
            throw new AjaxOperationFailException("单据id不能为空!");
        }
        LossMasters master = lossMastersMapper.selectByPrimaryKey(lossid);
        if(master == null){
            throw new AjaxOperationFailException("单据信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("单据已经审核，不能取消!");
        }
        master.setStatus("2");
        int count = lossMastersMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("取消报损成功");
        }
        return resp;
    }

    public ResponseJson lossAuth() throws AjaxOperationFailException {
        Integer lossid = HttpRequestParamter.getInt("lossid",0);//仓库id
        if(lossid==0){
            throw new AjaxOperationFailException("单据id不能为空!");
        }
        LossMasters master = lossMastersMapper.selectByPrimaryKey(lossid);
        if(master == null){
            throw new AjaxOperationFailException("单据信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("单据已经审核过了!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("单据已取消，审核失败!");
        }
        master.setStatus("1");
        master.setChecktime(new Date());
        master.setEmployee3id(employeeService.getLoginUser().getEmployeeid());
        int count = lossMastersMapper.updateByPrimaryKey(master);
        //修改库存
        List<Map> lossList = lossSlavesMapper.selectByLossid(lossid);
        for (Map map : lossList) {
            GoodsStock gs = goodsStockMapper.selectByGoodsid(map);
            gs.setLasttime(new Date());
            gs.setStock(gs.getStock()-Double.valueOf(map.get("lossnums")+""));
            goodsStockMapper.updateByPrimaryKey(gs);
        }
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("审核成功");
        }
        return resp;
    }

    /**
     * 报损列表
     * @return
     */
    public ResponseJson lossList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String startdate = HttpRequestParamter.getString("startdate",true);
        String enddate = HttpRequestParamter.getString("enddate",true);
        String status = HttpRequestParamter.getString("status",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isEmpty(startdate) && !StringUtil.isEmpty(enddate) ){
            params.put("startdate",startdate);
            params.put("enddate",enddate);
        }
        if(!StringUtil.isEmpty(status)){
            params.put("status",status);
        }
        PageHelper.startPage(page,size,"lossid desc");
        List<Map> list = lossMastersMapper.selectMasterList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 报损原料新增
     * @return
     */
    public ResponseJson lossGoodsAdd() throws AjaxOperationFailException {
        Integer lossid = HttpRequestParamter.getInt("lossid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double lossnums = HttpRequestParamter.getDouble("lossnums",0);
        String memo = HttpRequestParamter.getString("memo",true);

        if(lossid==0){
            throw new AjaxOperationFailException("报损信息id缺失!");
        }
        if(goodsid==0){
            throw new AjaxOperationFailException("请选择原料!");
        }
        if(lossnums==0){
            throw new AjaxOperationFailException("请填写报损数量!");
        }
        LossMasters master = lossMastersMapper.selectByPrimaryKey(lossid);
        if(master == null){
            throw new AjaxOperationFailException("报损信息不存在!");
        }
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(goods == null){
            throw new AjaxOperationFailException("商品信息不存在!");
        }
        LossSlaves slaves = new LossSlaves();
        slaves.setMemo(memo);
        slaves.setGoodsid(goodsid);
        slaves.setLossid(lossid);
        slaves.setLossmoney(goods.getPrice()*lossnums);
        slaves.setLossnums(lossnums);
        int count = lossSlavesMapper.insert(slaves);
        master.setTotalmoney(master.getTotalmoney()+slaves.getLossmoney());
        lossMastersMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("报损原料新增成功");
        }
        return resp;
    }


    public ResponseJson lossGoodsEdit() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double lossnums = HttpRequestParamter.getDouble("lossnums",0);
        String memo = HttpRequestParamter.getString("memo",true);

        if(slaveid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        LossSlaves slaves = lossSlavesMapper.selectByPrimaryKey(slaveid);
        if(slaves == null){
            throw new AjaxOperationFailException("修改的信息不存在!");
        }
        if(goodsid==0){
            throw new AjaxOperationFailException("请选择原料!");
        }
        if(lossnums==0){
            throw new AjaxOperationFailException("请填写报损数量!");
        }
        LossMasters master = lossMastersMapper.selectByPrimaryKey(slaves.getLossid());
        if(master == null){
            throw new AjaxOperationFailException("报损信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("报损信息已经审核，拒绝修改!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(goods == null){
            throw new AjaxOperationFailException("商品信息不存在!");
        }
        slaves.setMemo(memo);
        slaves.setGoodsid(goodsid);
        master.setTotalmoney(master.getTotalmoney()-slaves.getLossmoney()+(goods.getPrice()*lossnums));
        lossMastersMapper.updateByPrimaryKey(master);
        slaves.setLossmoney(goods.getPrice()*lossnums);
        slaves.setLossnums(lossnums);
        int count = lossSlavesMapper.updateByPrimaryKey(slaves);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("报损原料修改成功");
        }
        return resp;
    }

    /**
     * 报损原料删除
     * @return
     */
    public ResponseJson lossGoodsDel() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        if(slaveid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        LossSlaves slaves = lossSlavesMapper.selectByPrimaryKey(slaveid);
        if(slaves == null){
            throw new AjaxOperationFailException("修改的信息不存在!");
        }
        LossMasters master = lossMastersMapper.selectByPrimaryKey(slaves.getLossid());
        if(master == null){
            throw new AjaxOperationFailException("报损信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("报损信息已经审核，拒绝删除!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = lossSlavesMapper.deleteByPrimaryKey(slaveid);
        ResponseJson resp =new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }


    public ResponseJson lossGoodsList() {
        Integer lossid = HttpRequestParamter.getInt("lossid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        PageHelper.startPage(page,size,"slaveid desc");
        List<Map> list = lossSlavesMapper.selectSlavesList(lossid);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }



}
