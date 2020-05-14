package com.jzkj.gyxg.service.web;

import cn.hutool.core.util.NumberUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.*;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.*;
import com.jzkj.gyxg.util.DateUtil;
import com.jzkj.gyxg.util.StringUtil;
import org.omg.CORBA.MARSHAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CheckService {

    @Autowired
    private CheckMasterMapper checkMasterMapper;
    @Autowired
    private CheckSlavesMapper checkSlavesMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsStockMapper goodsStockMapper;
    @Autowired
    private MonthSeettlementMasterMapper monthSeettlementMasterMapper;
    @Autowired
    private MonthSsettlementSlaveMapper monthSsettlementSlaveMapper;

    /**
     * 盘点信息新增
     * @return
     */
    public ResponseJson checkMasterAdd() throws AjaxOperationFailException {
        String code = HttpRequestParamter.getString("code",true);
        String checkdate = HttpRequestParamter.getString("checkdate",true);
//        double totalmoney = HttpRequestParamter.getDouble("totalmoney",0);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);//仓库id
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);//盘点部门id
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);
        String memo = HttpRequestParamter.getString("memo",true);
//        String originalcode = HttpRequestParamter.getString("originalcode",true);//原始凭证
        if(StringUtil.isEmpty(code)){
            code = DateUtil.format(new Date(),"yyMMdd-HHmm");
//            throw new AjaxOperationFailException("单据编号不能为空!");
        }
        if(StringUtil.isEmpty(checkdate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
        if(warehouseid==0){
            throw new AjaxOperationFailException("请选择仓库!");
        }
        if(departmentid==0){
            throw new AjaxOperationFailException("请选择耗料部门!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
        CheckMaster master = new CheckMaster();
        master.setCode(code);
//        master.setOriginalcode(originalcode);
        master.setCheckdate(DateUtil.toDate(checkdate));
//        master.setTotalmoney(totalmoney);
        master.setWarehouseid(warehouseid);
        master.setDepartmentid(departmentid);
        master.setEmployee1id(employee1id);
        master.setEmployee2id(employeeService.getLoginUser().getEmployeeid());
        master.setMemo(memo);
        master.setStatus("0");
        master.setIntime(new Date());
        master.setCompanyid(employeeService.getLoginUser().getCompanyid());
        master.setStoreid(employeeService.getLoginUser().getStoreid());
        int count = checkMasterMapper.insert(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增盘点成功");
        }
        return resp;
    }


    /**
     * 盘点信息修改
     * @return
     */
    public ResponseJson checkMasterEdit() throws AjaxOperationFailException {
        Integer checkid = HttpRequestParamter.getInt("checkid",0);//id
        String code = HttpRequestParamter.getString("code",true);
        String checkdate = HttpRequestParamter.getString("checkdate",true);
//        double totalmoney = HttpRequestParamter.getDouble("totalmoney",0);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);//仓库id
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);//耗材部门id
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);
        String memo = HttpRequestParamter.getString("memo",true);
        String originalcode = HttpRequestParamter.getString("originalcode",true);//原始凭证

        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("单据编号不能为空!");
        }
        if(StringUtil.isEmpty(checkdate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
//        if(totalmoney==0){
//            throw new AjaxOperationFailException("单据金额不能为空!");
//        }
        if(warehouseid==0){
            throw new AjaxOperationFailException("请选择仓库!");
        }
        if(departmentid==0){
            throw new AjaxOperationFailException("请选择耗料部门!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
        if(checkid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        CheckMaster master = checkMasterMapper.selectByPrimaryKey(checkid);
        if(master==null){
            throw new AjaxOperationFailException("修改的信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("信息已经审核，不能修改!");
        }
        if(master.getStatus().equals("3")){
            throw new AjaxOperationFailException("该单据为历史单据，不能修改!");
        }
        master.setCode(code);
        master.setOriginalcode(originalcode);
        master.setCheckdate(DateUtil.toDate(checkdate));
//        master.setTotalmoney(totalmoney);
        master.setWarehouseid(warehouseid);
        master.setDepartmentid(departmentid);
        master.setEmployee1id(employee1id);
        master.setEmployee2id(employeeService.getLoginUser().getEmployeeid());
        master.setMemo(memo);
        int count = checkMasterMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改盘点成功");
        }
        return resp;
    }


    public boolean isPromission(CheckMaster master){
        Employees employees = employeeService.getLoginUser();
        if(((int)employees.getCompanyid()==(int)master.getCompanyid()) && ((int)employees.getStoreid()==(int)master.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 盘点取消
     * @return
     */
    public ResponseJson checkMasterCancel() throws AjaxOperationFailException {
        Integer checkid = HttpRequestParamter.getInt("checkid",0);//id
        if(checkid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        CheckMaster master = checkMasterMapper.selectByPrimaryKey(checkid);
        if(master==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("信息已经审核，不能取消!");
        }
        if(master.getStatus().equals("3")){
            throw new AjaxOperationFailException("该单据为历史单据，不能取消!");
        }
        master.setStatus("2");
        int count = checkMasterMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("取消成功!");
        }
        return resp;
    }

    /**
     *盘点信息审核
     * @return
     */
    public ResponseJson checkMasterAuth() throws AjaxOperationFailException {
        Integer checkid = HttpRequestParamter.getInt("checkid",0);//id
        if(checkid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        CheckMaster master = checkMasterMapper.selectByPrimaryKey(checkid);
        if(master==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("信息已经审核，不能重复审核!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("信息已经取消，审核失败!");
        }
        if(master.getStatus().equals("3")){
            throw new AjaxOperationFailException("该单据为历史单据，审核失败!");
        }
        //修改
        master.setStatus("1");
        master.setEmployee3id(employeeService.getLoginUser().getEmployeeid());
        master.setChecktime(new Date());
        int count = checkMasterMapper.updateByPrimaryKey(master);
        List<Map> list = checkSlavesMapper.selectBycheckid(checkid);
        //修改对应库存
        for (Map goods : list) {
            GoodsStock gs = goodsStockMapper.selectByGoodsid(goods);
            double checkStock = Double.valueOf(goods.get("checkstock")+"");
            double ycstock = Double.valueOf(goods.get("ycstock")+"");//均价、单价
            double totalMoney = NumberUtil.max(checkStock,ycstock);//库存总金额
            if(gs == null){
                GoodsStock goodsStock = new GoodsStock();
                goodsStock.setCompanyid(master.getCompanyid());
                goodsStock.setStoreid(master.getStoreid());
                goodsStock.setGoodsid(Integer.valueOf(goods.get("goodsid")+""));
                goodsStock.setWarehouseid(master.getWarehouseid());
                Goods gds = goodsMapper.selectByPrimaryKey(Integer.valueOf(goods.get("goodsid")+""));
                goodsStock.setUnit(gds.getUnit());
                goodsStock.setLasttime(new Date());
                goodsStock.setStock(checkStock);
                //库存总金额，
                goodsStock.setStockamount(totalMoney);
                goodsStockMapper.insert(goodsStock);//新插入库存数据
            }else {
                gs.setStock(checkStock);
                gs.setStockamount(totalMoney);
                gs.setLasttime(new Date());
                goodsStockMapper.updateByPrimaryKey(gs);
            }
        }
        ResponseJson resp = new ResponseJson();
        resp.setMsg("审核成功!");
        return resp;
    }


    /**
     * 盘点信息列表
     * @return
     */
    public ResponseJson checkMasterList() {
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
        PageHelper.startPage(page,size);
        List<Map> list = checkMasterMapper.selectMasterList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 盘点原料新增
     * @return
     */
    public ResponseJson checkSlaverAdd() throws AjaxOperationFailException {
        Integer checkid = HttpRequestParamter.getInt("checkid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double checkstock = HttpRequestParamter.getDouble("checkstock",0.00);
        String memo = HttpRequestParamter.getString("memo",true);
        if(checkid == 0){
            throw new AjaxOperationFailException("盘点信息id不能为空!");
        }
        if(goodsid == 0){
            throw new AjaxOperationFailException("请选择原料!");
        }
        if(checkstock == 0){
            throw new AjaxOperationFailException("盘点数量不能为空!");
        }
        CheckMaster master = checkMasterMapper.selectByPrimaryKey(checkid);
        if(master==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("信息已经审核，不能新增!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("单据已经取消，不能新增!");
        }
        if(master.getStatus().equals("3")){
            throw new AjaxOperationFailException("单据为历史单据，不能新增!");
        }
        Goods goods =  goodsMapper.selectByPrimaryKey(goodsid);
        if(goods == null){
            throw new AjaxOperationFailException("原料信息不存在!");
        }
        int count = checkSlavesMapper.check(goodsid,checkid);
        if(count > 0){
            throw new AjaxOperationFailException("该原料已经盘点，请勿重复添加!");
        }
        GoodsStock gs = goodsStockMapper.selectByGoodsid1(goods);
        CheckSlaves slaves = new CheckSlaves();
        //表单数据
        slaves.setCheckid(checkid);
        slaves.setMemo(memo);
        slaves.setGoodsid(goodsid);
        slaves.setCheckstock(checkstock);
        //后台生成
        if(gs == null){
            slaves.setStock(0.00);
        }else {
            slaves.setStock(gs.getStock());
        }
        slaves.setUsednums(0.00);//消耗数量
        slaves.setUsedmoney(0.00);
        int count1 = checkSlavesMapper.insert(slaves);
        ResponseJson resp = new ResponseJson();
        if(count1 > 0){
            resp.setMsg("新增盘点原料成功!");
        }
        return resp;
    }


    /**
     * 盘点原料信息修改
     * @return
     */
    public ResponseJson checkSlaverEdit() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double checkstock = HttpRequestParamter.getDouble("checkstock",0.00);
        String memo = HttpRequestParamter.getString("memo",true);
        if(slaveid==0){
            throw new AjaxOperationFailException("id不能为空!");
        }
        CheckSlaves slaves = checkSlavesMapper.selectByPrimaryKey(slaveid);
        if(slaves == null){
            throw new AjaxOperationFailException("修改的信息不存在!");
        }
        if(goodsid == 0){
            throw new AjaxOperationFailException("请选择原料!");
        }
        if(checkstock == 0){
            throw new AjaxOperationFailException("盘点数量不能为空!");
        }
        CheckMaster master = checkMasterMapper.selectByPrimaryKey(slaves.getCheckid());
        if(master==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("信息已经审核，不能修改!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("单据已经取消，不能新增!");
        }
        if(master.getStatus().equals("3")){
            throw new AjaxOperationFailException("单据为历史单据，不能新增!");
        }
        Goods goods =  goodsMapper.selectByPrimaryKey(goodsid);
        if(goods == null){
            throw new AjaxOperationFailException("原料信息不存在!");
        }
        GoodsStock gs = goodsStockMapper.selectByGoodsid1(goods);
        //表单数据
        slaves.setMemo(memo);
        slaves.setGoodsid(goodsid);
        slaves.setCheckstock(checkstock);
        //后台生成
        if(gs == null){
            slaves.setStock(0.00);
        }else {
            slaves.setStock(gs.getStock());
        }
        slaves.setUsednums(0.00);//消耗数量
        slaves.setUsedmoney(0.00);
        int count = checkSlavesMapper.updateByPrimaryKey(slaves);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改盘点原料成功!");
        }
        return resp;
    }

    /**
     * 盘点原料信息删除
     * @return
     */
    public ResponseJson checkSlaverDel() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        if(slaveid==0){
            throw new AjaxOperationFailException("id不能为空!");
        }
        CheckSlaves slaves = checkSlavesMapper.selectByPrimaryKey(slaveid);
        if(slaves == null){
            throw new AjaxOperationFailException("修改的信息不存在!");
        }
        CheckMaster master = checkMasterMapper.selectByPrimaryKey(slaves.getCheckid());
        if(master==null){
            throw new AjaxOperationFailException("上级盘点信息不存在!");
        }
        if(!isPromission(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("信息已经审核，不能删除!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("单据已经取消，不能新增!");
        }
        if(master.getStatus().equals("3")){
            throw new AjaxOperationFailException("单据为历史单据，不能新增!");
        }
        int count = checkSlavesMapper.deleteByPrimaryKey(slaveid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }

    /**
     * 盘点原料列表
     * @return
     */
    public ResponseJson checkSlaverList() {
        Integer checkid = HttpRequestParamter.getInt("checkid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        PageHelper.startPage(page,size,"slaveid desc");
        List<Map> list = checkSlavesMapper.selectSlaverList(checkid);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    /**
     * 盘点单据月末结转
     * @return
     */
    public ResponseJson jiezhuan() throws AjaxOperationFailException {
        String checkidstr = HttpRequestParamter.getString("checkidstr",true);
        if(StringUtil.isNull(checkidstr)){
            throw new AjaxOperationFailException("请选择单据");
        }
        for (String checkid : checkidstr.split(",")) {
            CheckMaster master = checkMasterMapper.selectByPrimaryKey(Integer.valueOf(checkid));
            if(master==null){
                throw new AjaxOperationFailException("某个单据信息不存在!");
            }
            if(!master.getStatus().equals("1")){
                throw new AjaxOperationFailException("只有单据为已经审核状态才能使用月末结转!");
            }
            if(master.getStatus().equals("3")){
                throw new AjaxOperationFailException("所选单据包含了历史单据，历史单据不能结转!");
            }
            //获取原料信息并且更改对应月初库存（）
            List<Integer> goodsidlist = checkSlavesMapper.selectgoodsidBycheckid(Integer.valueOf(checkid));
            for (Integer goodsid : goodsidlist) {
                //查询一个修改一个库存
                GoodsStock goodsStock = goodsStockMapper.selectBygoodsid(goodsid);
                //修改月初值
                goodsStock.setYcstock(goodsStock.getStock());
                goodsStockMapper.updateByPrimaryKey(goodsStock);
            }
            master.setStatus("3");//历史单据
            checkMasterMapper.updateByPrimaryKey(master);
        }
        return new ResponseJson("结转成功");
    }


    /**
     * 盘点单据月末结转
     * @return
     */
    public ResponseJson monthEndCheck() throws AjaxOperationFailException {
        //1.判断是否有未审核的盘点单
        int count = checkMasterMapper.findCountByStatus("0");
        if(count>0)throw new AjaxOperationFailException("还有未审核数据，不能直接月末盘点！");
        //2.查询当前登录用户的信息作为此次月末盘点的操作人，插入一条盘点单到月末结算主表中
        Employees user = employeeService.getLoginUser();
        MonthSeettlementMaster msm = new MonthSeettlementMaster();
        msm.setEmployeeid(user.getEmployeeid());//操作人
        msm.setCompanyid(user.getCompanyid());//公司流水id
        msm.setStoreid(user.getStoreid());//店铺流水id
        int id = monthSeettlementMasterMapper.insertSelective(msm);
        //3.将库存盘点到月末结算从表中
        List<GoodsStock> list = goodsStockMapper.selectAll();
        list.forEach(g->{
            MonthSsettlementSlave mss = new MonthSsettlementSlave();
            mss.setSettlementid(msm.getSettlementid());//结算流水号
            mss.setGoodsid(g.getGoodsid());//原料流水号
            mss.setWarehouseid(g.getWarehouseid());//库房编号
            mss.setUnit(g.getUnit());//基本单位
            mss.setStock(g.getStock());//结算库存
            mss.setStockamount(g.getStockamount());//期初金额
            monthSsettlementSlaveMapper.insertSelective(mss);
        });

        return new ResponseJson("月末盘点完成！");
    }

}
