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
import com.jzkj.gyxg.util.Pinyin;
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
public class GoodsService {

    @Autowired
    private WarehousesMapper warehousesMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private GoodsStockMapper goodsStockMapper;
    @Autowired
    private MaterialClassfyMapper materialClassfyMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private ChukuMasterMapper chukuMasterMapper;
    @Autowired
    private ChukuSlavesMapper slavesMapper;
    @Autowired
    private PurchaseMastersMapper purchaseMastersMapper;
    @Autowired
    private PurchaseSlavesMapper purchaseSlavesMapper;
    /**
     * 仓库信息列表
     * @return
     */
    public ResponseJson warehouseList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        PageHelper.startPage(page,size,"warehouseid asc");
        List<Map> list = warehousesMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    /**
     * 仓库新增
     * @return
     */
    public ResponseJson warehouseAdd() throws AjaxOperationFailException {
        String warehousecode = HttpRequestParamter.getString("warehousecode",true);//仓库编号
        String name = HttpRequestParamter.getString("name",true);//仓库名称
        String memo = HttpRequestParamter.getString("memo",true);//备注
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);//仓库管理员
        if(StringUtil.isEmpty(warehousecode)){
            throw new AjaxOperationFailException("仓库编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("仓库名称不能为空!");
        }
        if(StringUtil.isEmpty(memo)){
            throw new AjaxOperationFailException("备注信息不能为空!");
        }
        if(employeeid == 0){
            throw new AjaxOperationFailException("仓库管理员不能为空!");
        }
        Warehouses warehouses = new Warehouses();
        warehouses.setCompanyid(employeeService.getLoginUser().getCompanyid());
        warehouses.setStoreid(employeeService.getLoginUser().getStoreid());
        warehouses.setWarehousecode(warehousecode);
        warehouses.setName(name);
        warehouses.setMemo(memo);
        warehouses.setEmployeeid(employeeid);
        warehouses.setStatus("0");

        if(checkWarehouse(warehouses)){
            throw new AjaxOperationFailException("仓库名称或者编号重复!");
        }
        int count = warehousesMapper.insert(warehouses);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增仓库成功!");
        }
        return resp;
    }


    private boolean checkWarehouse(Warehouses warehouses) {
        int count = warehousesMapper.checkWarehouse(warehouses);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 仓库修改
     * @return
     */
    public ResponseJson warehouseEdit() throws AjaxOperationFailException {
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);//仓库id
        String warehousecode = HttpRequestParamter.getString("warehousecode",true);//仓库编号
        String name = HttpRequestParamter.getString("name",true);//仓库名称
        String memo = HttpRequestParamter.getString("memo",true);//备注
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);//仓库管理员
        if(warehouseid==0){
            throw new AjaxOperationFailException("仓id不能为空!");
        }
        if(StringUtil.isEmpty(warehousecode)){
            throw new AjaxOperationFailException("仓库编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("仓库名称不能为空!");
        }
        if(StringUtil.isEmpty(memo)){
            throw new AjaxOperationFailException("备注信息不能为空!");
        }
        if(employeeid == 0){
            throw new AjaxOperationFailException("仓库管理员不能为空!");
        }
        Warehouses warehouses = warehousesMapper.selectByPrimaryKey(warehouseid);
        if(warehouses == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(warehouses)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!warehousecode.equals(warehouses.getWarehousecode())){
            warehouses.setWarehousecode(warehousecode);
            int count = warehousesMapper.checkWarehousebycode(warehouses);
            if(count > 0){
                throw new AjaxOperationFailException("名称已经存在!");
            }
        }
        if(!name.equals(warehouses.getName())){
            warehouses.setName(name);
            int count = warehousesMapper.checkWarehousebyname(warehouses);
            if(count > 0){
                throw new AjaxOperationFailException("名称已经存在!");
            }
        }
        warehouses.setMemo(memo);
        warehouses.setEmployeeid(employeeid);
        int count = warehousesMapper.updateByPrimaryKey(warehouses);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改仓库成功!");
        }
        return resp;
    }


    private boolean isPromission(Warehouses warehouses) {
        Employees loginUser = employeeService.getLoginUser();
        if(((int)loginUser.getCompanyid()==(int)warehouses.getCompanyid()) && ((int)loginUser.getStoreid()==(int)warehouses.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 仓库删除
     * @return
     */
    public ResponseJson warehouseDel() throws AjaxOperationFailException {
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);//仓库id
        if(warehouseid==0){
            throw new AjaxOperationFailException("仓id不能为空!");
        }
        Warehouses warehouses = warehousesMapper.selectByPrimaryKey(warehouseid);
        if(warehouses == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(warehouses)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count1 = goodsStockMapper.selectCountByWarehouseid(warehouseid);
        if(count1 > 0){
            throw new AjaxOperationFailException("仓库绑定有商品库存"+count1+"个!删除失败!");
        }
        int count = warehousesMapper.deleteByPrimaryKey(warehouseid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功");
        }
        return resp;
    }

    /**
     * 原料分类列表
     * @return
     */
    public ResponseJson materialClassifysList() {
        Integer level = HttpRequestParamter.getInt("level",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);//
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        if(classifyid!=0){
            params.put("parentid",classifyid);
        }
        if(level!=0){
            params.put("level",level);
        }
        PageHelper.startPage(page,size,"classifyid asc");
        List<Map> list = materialClassfyMapper.selectMaterialClassfyList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 原料分类新增
     * @return
     */
    public ResponseJson materialClassifysAdd() throws AjaxOperationFailException {
        String code = HttpRequestParamter.getString("code",true);
        String name = HttpRequestParamter.getString("name",true);
        Integer parentid = HttpRequestParamter.getInt("parentid",0);
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("分类编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("分类名称不能为空!");
        }
        MaterialClassfy m = new MaterialClassfy();
        m.setCompanyid(employeeService.getLoginUser().getCompanyid());
        m.setStatus("0");
        m.setStoreid(employeeService.getLoginUser().getStoreid());
        m.setCode(code);
        m.setName(name);
        if(checkMaterialClassfy(m)){
            throw new AjaxOperationFailException("编号已经存在!");
        }
        if(parentid==0){//默认为一级，父级为0
            m.setParentid(0);
            m.setLevel(1);
        }else {
            MaterialClassfy p = materialClassfyMapper.selectByPrimaryKey(parentid);
            if(p==null){
                throw new AjaxOperationFailException("父级不存在!");
            }else {
                m.setParentid(parentid);
                m.setLevel(p.getLevel()+1);
            }
        }
        int count = materialClassfyMapper.insert(m);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增成功!");
        }
        return resp;
    }


    /**
     * 原料分类修改
     * @return
     */
    public ResponseJson materialClassifysEdit() throws AjaxOperationFailException {
        String code = HttpRequestParamter.getString("code",true);
        String name = HttpRequestParamter.getString("name",true);
        Integer parentid = HttpRequestParamter.getInt("parentid",0);
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        if(classifyid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("分类编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("分类名称不能为空!");
        }
        MaterialClassfy m = materialClassfyMapper.selectByPrimaryKey(classifyid);
        if(m==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionMaterialClassfy(m)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!code.equals(m.getCode())){
            m.setCode(code);
            int count = materialClassfyMapper.checkMaterialClassfybycode(m);
            if(count > 0){
                throw new AjaxOperationFailException("分类编号已经存在!");
            }
        }
        if(!name.equals(m.getName())){
            m.setName(name);
            int count = materialClassfyMapper.checkMaterialClassfybyname(m);
            if(count > 0){
                throw new AjaxOperationFailException("分类名称已经存在!");
            }
        }
        if(parentid==0){//默认为一级，父级为0
            m.setParentid(0);
            m.setLevel(1);
        }else {
            MaterialClassfy p = materialClassfyMapper.selectByPrimaryKey(parentid);
            if(p==null){
                throw new AjaxOperationFailException("父级不存在!");
            }else {
                m.setParentid(parentid);
                m.setLevel(p.getLevel()+1);
            }
        }
        int count = materialClassfyMapper.updateByPrimaryKey(m);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改成功!");
        }
        return resp;
    }

    public boolean checkMaterialClassfy(MaterialClassfy m){
        int count = materialClassfyMapper.checkMaterialClassfy(m);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPomissionMaterialClassfy(MaterialClassfy m){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)m.getCompanyid()) && ((int)loginuser.getStoreid()==(int)m.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    public ResponseJson materialClassifysDel() throws AjaxOperationFailException {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        if(classifyid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        MaterialClassfy m = materialClassfyMapper.selectByPrimaryKey(classifyid);
        if(m==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionMaterialClassfy(m)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = goodsMapper.selectCountByclassfyid(classifyid);
        if(count > 0){
            throw new AjaxOperationFailException("该分类下有商品原料"+count+"个，删除失败!");
        }
        int i = materialClassfyMapper.deleteByPrimaryKey(classifyid);
        ResponseJson resp = new ResponseJson();
        if(i > 0){
            resp.setMsg("删除成功");
        }
        return resp;
    }

    /**
     * 原料新增
     * @return
     */
    public ResponseJson goodsAdd() throws AjaxOperationFailException {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);//辅助单位比例
        String code = HttpRequestParamter.getString("code",true);
        String name = HttpRequestParamter.getString("name",true);
        String specs = HttpRequestParamter.getString("specs",true);
        String unit = HttpRequestParamter.getString("unit",true);
//        double price = HttpRequestParamter.getInt("price",0);
        double maxstock = HttpRequestParamter.getInt("maxstock",0);//库存上限
        double minstock = HttpRequestParamter.getInt("minstock",0);//库存下限
        if(classifyid == 0){
            throw new AjaxOperationFailException("请选择原料分类!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("原料编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("原料名称不能为空!");
        }
        if(StringUtil.isEmpty(specs)){
            throw new AjaxOperationFailException("原料规格不能为空!");
        }
        if(StringUtil.isEmpty(unit)){
            throw new AjaxOperationFailException("原料单位不能为空!");
        }
        Goods goods = new Goods();
        goods.setCompanyid(employeeService.getLoginUser().getCompanyid());
        goods.setStatus("0");
        goods.setStoreid(employeeService.getLoginUser().getStoreid());
        goods.setName(name);
        goods.setPinyin(Pinyin.getPinYinHeadChar(name));
        goods.setCode(code);
        goods.setSpecs(specs);
        goods.setUnit(unit);
//        goods.setPrice(price);
        goods.setMaxstock(maxstock);
        goods.setMinstock(minstock);
        goods.setClassifyid(classifyid);
        if(checkGoods(goods)){
            throw new AjaxOperationFailException("原料编号重复!");
        }
        int count = goodsMapper.insert(goods);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增原料成功!");
        }
        return resp;
    }

    private boolean checkGoods(Goods goods) {
        int i = goodsMapper.checkGoods(goods);
        if(i > 0){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 原料修改
     * @return
     */
    public ResponseJson goodsEdit() throws AjaxOperationFailException {
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);//原料id
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);//辅助单位比例
        String code = HttpRequestParamter.getString("code",true);
        String name = HttpRequestParamter.getString("name",true);
        String specs = HttpRequestParamter.getString("specs",true);
        String unit = HttpRequestParamter.getString("unit",true);
//        double price = HttpRequestParamter.getInt("price",0);//销售价格
        double maxstock = HttpRequestParamter.getInt("maxstock",0);//库存上限
        double minstock = HttpRequestParamter.getInt("minstock",0);//库存下限
        if(goodsid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        if(classifyid == 0){
            throw new AjaxOperationFailException("请选择原料分类!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("原料编号不能为空!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("原料名称不能为空!");
        }
        if(StringUtil.isEmpty(specs)){
            throw new AjaxOperationFailException("原料规格不能为空!");
        }
        if(StringUtil.isEmpty(unit)){
            throw new AjaxOperationFailException("原料单位不能为空!");
        }
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(goods == null){
            throw new AjaxOperationFailException("原料不存在!");
        }
        if(!isPomissionGoods(goods)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!code.equals(goods.getCode())){//修改编号得查重
            goods.setCode(code);
            int i = goodsMapper.checkGoodsbycode(goods);
            if (i > 0){
                throw new AjaxOperationFailException("编号已经存在!");
            }
        }
        if(!name.equals(goods.getName())){
            goods.setName(name);
            int i = goodsMapper.checkGoodsbyname(goods);
            if (i > 0){
                throw new AjaxOperationFailException("名称已经存在!");
            }

        }
        goods.setName(name);
        goods.setPinyin(Pinyin.getPinYinHeadChar(name));
        goods.setCode(code);
        goods.setSpecs(specs);
        goods.setUnit(unit);
//        goods.setPrice(price);
        goods.setMaxstock(maxstock);
        goods.setMinstock(minstock);
        goods.setClassifyid(classifyid);
        int count = goodsMapper.updateByPrimaryKey(goods);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改原料成功!");
        }
        return resp;
    }
    public boolean isPomissionGoods(Goods m){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)m.getCompanyid()) && ((int)loginuser.getStoreid()==(int)m.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 原料删除
     * @return
     */
    public ResponseJson goodsDel() throws AjaxOperationFailException {
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);//原料id
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(goods == null){
            throw new AjaxOperationFailException("原料不存在!");
        }
        if(!isPomissionGoods(goods)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = goodsStockMapper.selectCountByGoodsid(goodsid);
        if(count > 0){
            throw new AjaxOperationFailException("该原料尚有库存，删除失败!");
        }
        int flag = goodsMapper.deleteByPrimaryKey(goodsid);
        ResponseJson resp = new ResponseJson();
        if(flag > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }


    /**
     * 原料列表
     * @return
     */
    public ResponseJson goodsList() {
        String name = HttpRequestParamter.getString("name",true);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        Map params = new HashMap<>();
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(classifyid!=0){
            params.put("classifyid",classifyid);
        }
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        PageHelper.startPage(page,size,"goodsid asc");
        List<Map> list = goodsMapper.selectGoodsList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }



    /**
     * 新增出库
     * @return
     */
    public ResponseJson chuKuAdd() throws AjaxOperationFailException {
        String code = HttpRequestParamter.getString("code",true);
        String purchasedate = HttpRequestParamter.getString("purchasedate",true);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(StringUtil.isEmpty(code)){
            code = DateUtil.format(new Date(), "yyMMdd-HHmm");
//            throw new AjaxOperationFailException("单据编号不能为空!");
        }
        if(StringUtil.isEmpty(purchasedate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
        if(warehouseid==0){
            throw new AjaxOperationFailException("请选择仓库!");
        }
        if(departmentid==0){
            throw new AjaxOperationFailException("请选择领料部门!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
        ChukuMaster master = new ChukuMaster();
        master.setCode(code);
        master.setPurchasedate(DateUtil.toDate(purchasedate));
        master.setTotalmoney(0.00);
        master.setWarehouseid(warehouseid);
        master.setDepartmentid(departmentid);
        master.setEmployee1id(employee1id);//经办人
        master.setMemo(memo);

        master.setEmployee2id(employeeService.getLoginUser().getEmployeeid());//操作员
        master.setCompanyid(employeeService.getLoginUser().getCompanyid());
        master.setStoreid(employeeService.getLoginUser().getStoreid());
        master.setIntime(new Date());
        master.setStatus("0");//状态：0-录入 1-已审核 2-已取消
        int count = chukuMasterMapper.insert(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增成功!");
        }
        return resp;
    }

    /**
     * 出库信息修改
     * @return
     */
    public ResponseJson chukuEdit() throws AjaxOperationFailException {
        Integer chukuid = HttpRequestParamter.getInt("chukuid",0);
        String code = HttpRequestParamter.getString("code",true);
        String purchasedate = HttpRequestParamter.getString("purchasedate",true);
        double totalmoney = HttpRequestParamter.getDouble("totalmoney",0);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(chukuid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("单据编号不能为空!");
        }
        if(StringUtil.isEmpty(purchasedate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
        if(totalmoney==0){
            throw new AjaxOperationFailException("单据金额不能为空!");
        }
        if(warehouseid==0){
            throw new AjaxOperationFailException("请选择仓库!");
        }
        if(departmentid==0){
            throw new AjaxOperationFailException("请选择领料部门!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
        ChukuMaster master = chukuMasterMapper.selectByPrimaryKey(chukuid);
        if(master == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionChukuMaster(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){//已审核的单据不能再修改或调整
            throw new AjaxOperationFailException("单据已经审核，修改失败!");
        }
        master.setCode(code);
        master.setPurchasedate(DateUtil.toDate(purchasedate));
        master.setTotalmoney(totalmoney);
        master.setWarehouseid(warehouseid);
        master.setDepartmentid(departmentid);
        master.setEmployee1id(employee1id);//经办人
        master.setMemo(memo);

        master.setEmployee2id(employeeService.getLoginUser().getEmployeeid());//操作员
        master.setStatus("0");//状态：0-录入 1-已审核 2-已取消
        int count = chukuMasterMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改成功!");
        }
        return resp;
    }

    public ResponseJson chukuCancel() throws AjaxOperationFailException {
        Integer chukuid = HttpRequestParamter.getInt("chukuid",0);
        if(chukuid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        ChukuMaster master = chukuMasterMapper.selectByPrimaryKey(chukuid);
        if(master == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionChukuMaster(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){//已审核的单据不能再修改或调整
            throw new AjaxOperationFailException("单据已经审核，取消失败!");
        }
        master.setStatus("2");//取消
        int count = chukuMasterMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("取消成功!");
        }
        return resp;
    }

    public boolean isPomissionChukuMaster(ChukuMaster m){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)m.getCompanyid()) && ((int)loginuser.getStoreid()==(int)m.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPomissionChukuMaster(PurchaseMasters m){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)m.getCompanyid()) && ((int)loginuser.getStoreid()==(int)m.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     *  出库信息列表
     * @return
     */
    public ResponseJson chukuList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String startdate = HttpRequestParamter.getString("startdate",true);
        String enddate = HttpRequestParamter.getString("enddate",true);
        String status = HttpRequestParamter.getString("status",true);
        String code = HttpRequestParamter.getString("code",true);
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
        if(!StringUtil.isEmpty(code)){
            params.put("code",code);
        }
        PageHelper.startPage(page,size);
        List<Map> list = chukuMasterMapper.selectChukuMasterList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }



    @Transactional
    public ResponseJson chukuAuth() throws AjaxOperationFailException {
        Integer chukuid = HttpRequestParamter.getInt("chukuid",0);
        if(chukuid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        ChukuMaster master = chukuMasterMapper.selectByPrimaryKey(chukuid);
        if(master == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionChukuMaster(master)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("单据已经审核过了!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("单据已取消，审核失败!");
        }
        master.setStatus("1");//审核
        master.setChecktime(new Date());
        master.setEmployee3id(employeeService.getLoginUser().getEmployeeid());
        //修改库存（）
        List<Map> listgoodsid = slavesMapper.selectByChukuid(chukuid);
        for (Map goods : listgoodsid) {
            GoodsStock gs = goodsStockMapper.selectByGoodsid(goods);
            double chukuAmount = Double.parseDouble(goods.get("amount")+"");
            if(gs!=null && (gs.getStock()>chukuAmount)){
                gs.setStock(gs.getStock()-chukuAmount);
                gs.setStockamount(NumberUtil.sub(gs.getStockamount(),NumberUtil.mul(chukuAmount,gs.getYcstock())));
                gs.setLasttime(new Date());
                goodsStockMapper.updateByPrimaryKey(gs);
            }else {
                throw new AjaxOperationFailException("原料名为"+goods.get("name")+"的商品储备低于出库的数量!审核不通过!");
            }
        }
        ResponseJson resp = new ResponseJson();
        int count = chukuMasterMapper.updateByPrimaryKey(master);
        if(count > 0){
            resp.setMsg("审核成功!");
        }
        return resp;
    }

    /**
     * 原料出库新增
     * @return
     */
    public ResponseJson goodsChukuAdd() throws AjaxOperationFailException {
        Integer chukuid = HttpRequestParamter.getInt("chukuid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double amount = HttpRequestParamter.getDouble("amount",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(chukuid==0){
            throw new AjaxOperationFailException("出库id不能为空!");
        }
        if(goodsid==0){
            throw new AjaxOperationFailException("原料id不能为空!");
        }
        if(amount==0){
            throw new AjaxOperationFailException("数量不能为空!");
        }
        ChukuMaster master = chukuMasterMapper.selectByPrimaryKey(chukuid);
        if(master==null){
            throw new AjaxOperationFailException("出库信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("出库已经审核，不能新增!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("出库已经取消，不能新增!");
        }
        GoodsStock goodsStock = goodsStockMapper.selectBygoodsid(goodsid);//仓库库存
        if(goodsStock==null || goodsStock.getStock()<=0){
            throw new AjaxOperationFailException("暂无库存原料!");
        }
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);//原料信息
        if(goods==null){
            throw new AjaxOperationFailException("原料信息不存在!");
        }
        ChukuSlaves slaves = new ChukuSlaves();
        slaves.setAmount(amount);
        slaves.setGoodsid(goodsid);
        slaves.setMemo(memo);
        slaves.setChukuid(chukuid);
        slaves.setUnit(goods.getUnit());
        slaves.setPrice(goods.getPrice());
        slaves.setTotalmoney(amount*(goods.getPrice()));
        int num = slavesMapper.check(slaves);
        if(num > 0){
            throw new AjaxOperationFailException("原料编号为"+goods.getCode()+"的原料重复了，请核对");
        }
        int count = slavesMapper.insert(slaves);
        master.setTotalmoney(master.getTotalmoney()+slaves.getTotalmoney());
        chukuMasterMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增原料成功!");
        }
        return resp;
    }

    /**
     * 原料出库修改
     * @return
     */
    public ResponseJson goodsChukuEdit() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        Integer chukuid = HttpRequestParamter.getInt("chukuid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double amount = HttpRequestParamter.getDouble("amount",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(slaveid==0){
            throw new AjaxOperationFailException("原料出库id不能为空!");
        }
        if(chukuid==0){
            throw new AjaxOperationFailException("出库id不能为空!");
        }
        if(goodsid==0){
            throw new AjaxOperationFailException("原料id不能为空!");
        }
        if(amount==0){
            throw new AjaxOperationFailException("数量不能为空!");
        }
        ChukuMaster master = chukuMasterMapper.selectByPrimaryKey(chukuid);
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(master==null){
            throw new AjaxOperationFailException("出库信息不存在!");
        }
        if(goods==null){
            throw new AjaxOperationFailException("原料信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("出库已经审核，不能修改!");
        }
        ChukuSlaves slaves = slavesMapper.selectByPrimaryKey(slaveid);
        if(slaves==null){
            throw new AjaxOperationFailException("从表原料信息不存在!");
        }
        slaves.setAmount(amount);
        slaves.setGoodsid(goodsid);
        slaves.setMemo(memo);
        slaves.setChukuid(chukuid);
        slaves.setUnit(goods.getUnit());
        slaves.setPrice(goods.getPrice());
        master.setTotalmoney(master.getTotalmoney()-slaves.getTotalmoney()+(amount*(goods.getPrice())));
        chukuMasterMapper.updateByPrimaryKey(master);
        slaves.setTotalmoney(amount*(goods.getPrice()));
        int count = slavesMapper.updateByPrimaryKey(slaves);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改原料成功!");
        }
        return resp;
    }

    /**
     * 原料出库删除
     * @return
     */
    public ResponseJson goodsChukuDel() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        if(slaveid==0){
            throw new AjaxOperationFailException("原料出库id不能为空!");
        }
        ChukuSlaves slaves = slavesMapper.selectByPrimaryKey(slaveid);
        if(slaves==null){
            throw new AjaxOperationFailException("从表原料信息不存在!");
        }
        ChukuMaster master = chukuMasterMapper.selectByPrimaryKey(slaves.getChukuid());
        if(master==null){
            throw new AjaxOperationFailException("出库信息不存在!");
        }
        if("1".equals(master.getStatus())){
            throw new AjaxOperationFailException("出库已经审核，删除失败!");
        }
        int count = slavesMapper.deleteByPrimaryKey(slaveid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }

    /**
     * 通过原料id获取原料单位价格
     * @return
     */
    public ResponseJson selectbyGoodsid() throws AjaxOperationFailException {
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        if(goodsid==0){
            throw new AjaxOperationFailException("原料id不能为空!");
        }
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(goods==null){
            throw new AjaxOperationFailException("原料信息不存在!");
        }
        ResponseJson resp = new ResponseJson();
        Map map = new HashMap();
        map.put("unit",goods.getUnit());
        map.put("price",goods.getPrice());
        map.put("specs",goods.getSpecs());
        resp.setData(map);
        return resp;
    }

    /**
     * 通过原料id获取库存原料单位价格
     * @return
     */
    public ResponseJson findKucunGoodsByGoodsid() throws AjaxOperationFailException {
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        if(goodsid==0){
            throw new AjaxOperationFailException("原料id不能为空!");
        }
        Map map = goodsStockMapper.findGoodsStockByGoodsId(goodsid);
        if(map==null){
            throw new AjaxOperationFailException("库存原料信息不存在!");
        }
        double stockamount = Double.valueOf(map.get("stockamount")+"");//总库存金额
        double stock = Double.valueOf(map.get("stock")+"");//库存数量
        double price = NumberUtil.round(NumberUtil.div(stockamount,stock),2).doubleValue();//单价=总金额/库存数量
        map.put("price",price);

        ResponseJson resp = new ResponseJson();
        resp.setData(map);
        return resp;
    }

    /**
     * 原料出库列表
     * @return
     */
    public ResponseJson goodsChukuList() throws AjaxOperationFailException {
        Integer chukuid = HttpRequestParamter.getInt("chukuid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        if(chukuid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        ChukuMaster master = chukuMasterMapper.selectByPrimaryKey(chukuid);
        if(master == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        Map params = new HashMap();
        params.put("chukuid",chukuid);
        PageHelper.startPage(page,size,"slaveid asc");
        List<Map> list = slavesMapper.selectList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;
    }

    /**
     * 直拨/入库
     * @return
     */
    public ResponseJson rukuAdd(String flag) throws AjaxOperationFailException {
        String code = HttpRequestParamter.getString("code",true);//单据编号
        String purchasedate = HttpRequestParamter.getString("purchasedate",true);//单据日期
        Integer supplierid = HttpRequestParamter.getInt("supplierid",0);//供应商id
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);//flag为0时候下拉选择部门id，为1时候选择仓库id
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);//采购人（下拉选择员工），为1时候选择仓库id
        Integer employee2id = HttpRequestParamter.getInt("employee2id",0);//经办人
//        String is_pay = HttpRequestParamter.getString("is_pay",true);//结账：0-未结账 1-结账
        String memo = HttpRequestParamter.getString("memo",true);//备注
        if(StringUtil.isEmpty(code)){
            code = DateUtil.format(new Date(),"yyMMdd-HHmm");
//            throw new AjaxOperationFailException("单据编号不能为空!");
        }
        if(StringUtil.isEmpty(purchasedate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
        if(supplierid==0){
            throw new AjaxOperationFailException("请选择供应商!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择采购人!");
        }
        if(employee2id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
//        if(StringUtil.isEmpty(is_pay)){
//            throw new AjaxOperationFailException("请选择是否结账!");
//        }
        PurchaseMasters master = new PurchaseMasters();
        master.setCode(code);
        master.setPurchasedate(DateUtil.toDate(purchasedate));
        master.setSupplierid(supplierid);
        master.setEmployee1id(employee1id);
        master.setEmployee2id(employee2id);
//        master.setIsPay(is_pay);
        master.setIntime(new Date());
        master.setStatus("0");
        master.setCompanyid(employeeService.getLoginUser().getCompanyid());
        master.setStoreid(employeeService.getLoginUser().getStoreid());
        master.setEmployee3id(employeeService.getLoginUser().getEmployeeid());
        master.setMemo(memo);
        if(flag.equals("0")){//直拨
            if(departmentid==0){
                throw new AjaxOperationFailException("请选择部门!");
            }
            master.setFlag("0");
            master.setDepartmentid(departmentid);
        }else {//入库
            if(warehouseid==0){
                throw new AjaxOperationFailException("请选择仓库!");
            }
            master.setFlag("1");
            master.setDepartmentid(warehouseid);
        }

        int count = purchaseMastersMapper.insert(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增入库成功");
        }
        return resp;
    }

    /**
     * 入库修改
     * @param flag
     * @return
     */
    public ResponseJson rukuEdit(String flag) throws AjaxOperationFailException {
        Integer purchaseid = HttpRequestParamter.getInt("purchaseid",0);
        String code = HttpRequestParamter.getString("code",true);//单据编号
        String purchasedate = HttpRequestParamter.getString("purchasedate",true);//单据日期
        Integer supplierid = HttpRequestParamter.getInt("supplierid",0);//供应商id
        double totalmoney = HttpRequestParamter.getDouble("totalmoney",0);//单据金额
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);//flag为0时候下拉选择部门id，为1时候选择仓库id
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);
        Integer employee1id = HttpRequestParamter.getInt("employee1id",0);//采购人（下拉选择员工），为1时候选择仓库id
        Integer employee2id = HttpRequestParamter.getInt("employee2id",0);//经办人
        String is_pay = HttpRequestParamter.getString("is_pay",true);//结账：0-未结账 1-结账
        String memo = HttpRequestParamter.getString("memo",true);//备注
        if(purchaseid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        PurchaseMasters master = purchaseMastersMapper.selectByPrimaryKey(purchaseid);
        if(master==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("已经审核，不能修改!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("单据编号不能为空!");
        }
        if(StringUtil.isEmpty(purchasedate)){
            throw new AjaxOperationFailException("单据日期不能为空!");
        }
        if(supplierid==0){
            throw new AjaxOperationFailException("请选择供应商!");
        }
        if(totalmoney==0){
            throw new AjaxOperationFailException("请输入单据总金额!");
        }
        if(employee1id==0){
            throw new AjaxOperationFailException("请选择采购人!");
        }
        if(employee2id==0){
            throw new AjaxOperationFailException("请选择经办人!");
        }
        if(StringUtil.isEmpty(is_pay)){
            throw new AjaxOperationFailException("请选择是否结账!");
        }
        master.setCode(code);
        master.setPurchasedate(DateUtil.toDate(purchasedate));
        master.setTotalmoney(totalmoney);
        master.setSupplierid(supplierid);
        master.setEmployee1id(employee1id);
        master.setEmployee2id(employee2id);
        master.setIsPay(is_pay);
        master.setEmployee3id(employeeService.getLoginUser().getEmployeeid());
        master.setMemo(memo);
        if(flag.equals("0")){//直拨
            if(departmentid==0){
                throw new AjaxOperationFailException("请选择部门!");
            }
            master.setDepartmentid(departmentid);
        }else {//入库
            if(warehouseid==0){
                throw new AjaxOperationFailException("请选择仓库!");
            }
            master.setDepartmentid(warehouseid);
        }

        int count = purchaseMastersMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改入库成功");
        }
        return resp;
    }

    /**
     * 入库取消
     * @return
     */
    public ResponseJson rukuCancel() throws AjaxOperationFailException {
        Integer purchaseid = HttpRequestParamter.getInt("purchaseid",0);
        if(purchaseid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        PurchaseMasters master = purchaseMastersMapper.selectByPrimaryKey(purchaseid);
        if(master==null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("信息已经审核，取消失败!");
        }
        master.setStatus("2");
        int count = purchaseMastersMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("取消成功!");
        }
        return resp;
    }

    /**
     * 入库操作
     * @param flag 0直拨入库，1原料入库
     * @return
     */
    public ResponseJson rukuList(String flag) {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String startdate = HttpRequestParamter.getString("startdate",true);
        String enddate = HttpRequestParamter.getString("enddate",true);
        String status = HttpRequestParamter.getString("status",true);
        String code = HttpRequestParamter.getString("code",true);
        Integer supplierid = HttpRequestParamter.getInt("supplierid",0);//供应商id
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
        if(!StringUtil.isEmpty(code)){
            params.put("code",code);
        }
        if(supplierid!=0){
            params.put("supplierid",supplierid);
        }
        params.put("flag",flag);
        PageHelper.startPage(page,size);
        List<Map> list = chukuMasterMapper.selectRukuMasterList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 原料入库新增
     * @return
     */
    public ResponseJson goodsRukuAdd() throws AjaxOperationFailException {
        Integer purchaseid = HttpRequestParamter.getInt("purchaseid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double amount = HttpRequestParamter.getDouble("amount",0);
        double totalmoney = HttpRequestParamter.getDouble("totalmoney",0);
        double price = HttpRequestParamter.getDouble("price",0);//采购价格
        String memo = HttpRequestParamter.getString("memo",true);

        if(purchaseid==0){
            throw new AjaxOperationFailException("入库id不能为空!");
        }
        if(goodsid==0){
            throw new AjaxOperationFailException("原料id不能为空!");
        }
        if(amount==0){
            throw new AjaxOperationFailException("数量不能为空!");
        }
        if(price==0){
            throw new AjaxOperationFailException("采购单价不能为空!");
        }
        if(totalmoney==0){
            throw new AjaxOperationFailException("采购总价不能为空!");
        }
        PurchaseSlaves ps = new PurchaseSlaves();
        ps.setPurchaseid(purchaseid);
        ps.setGoodsid(goodsid);
//        List<PurchaseSlaves> list = purchaseSlavesMapper.findByPurchaseIdAndGoodsId(ps);
//        if(list!=null && list.size()>0){
//            throw new AjaxOperationFailException("已有采购项目，请勿重复添加!");
//        }

        PurchaseMasters master = purchaseMastersMapper.selectByPrimaryKey(purchaseid);
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(master==null){
            throw new AjaxOperationFailException("入库信息不存在!");
        }
        if(goods==null){
            throw new AjaxOperationFailException("原料信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("出库已经审核，不能新增!");
        }
        if(master.getStatus().equals("2")){
            throw new AjaxOperationFailException("出库已经取消，不能新增!");
        }
        PurchaseSlaves slaves = new PurchaseSlaves();
        slaves.setAmount(amount);
        slaves.setGoodsid(goodsid);
        slaves.setMemo(memo);
        slaves.setPurchaseid(purchaseid);

        slaves.setUnit(goods.getUnit());
        slaves.setPrice(price);
        slaves.setTotalmoney(totalmoney);
        int num =  purchaseSlavesMapper.check(slaves);
        if(num > 0){
            throw new AjaxOperationFailException("本次入库单已经包含了编号为"+goods.getCode()+"的原料！");
        }
        int count = purchaseSlavesMapper.insert(slaves);
        master.setTotalmoney(master.getTotalmoney()+totalmoney);//修改单据金额
        purchaseMastersMapper.updateByPrimaryKey(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增原料入库成功!");
        }
        return resp;
    }

    /**
     * 原料入库修改
     * @return
     */
    public ResponseJson goodsRukuEdit() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        Integer goodsid = HttpRequestParamter.getInt("goodsid",0);
        double amount = HttpRequestParamter.getDouble("amount",0);
        double totalmoney = HttpRequestParamter.getDouble("totalmoney",0);
        double price = HttpRequestParamter.getDouble("price",0);//采购价格
        String memo = HttpRequestParamter.getString("memo",true);

        if(slaveid==0){
            throw new AjaxOperationFailException("原料入库id不能为空!");
        }
        PurchaseSlaves slaves = purchaseSlavesMapper.selectByPrimaryId(slaveid);
        if(slaves==null){
            throw new AjaxOperationFailException("原料入库信息不存在!");
        }
        if(goodsid==0){
            throw new AjaxOperationFailException("原料id不能为空!");
        }
        if(amount==0){
            throw new AjaxOperationFailException("数量不能为空!");
        }
        if(price==0){
            throw new AjaxOperationFailException("采购单价不能为空!");
        }
        if(totalmoney==0){
            throw new AjaxOperationFailException("采购总价不能为空!");
        }
        PurchaseMasters master = purchaseMastersMapper.selectByPrimaryKey(slaves.getPurchaseid());
        if(master==null){
            throw new AjaxOperationFailException("主表信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("入库信息已经审核，不能修改!");
        }
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        if(goods==null){
            throw new AjaxOperationFailException("原料信息不存在!");
        }
        slaves.setAmount(amount);
        slaves.setGoodsid(goodsid);
        slaves.setMemo(memo);
        slaves.setUnit(goods.getUnit());
        slaves.setPrice(price);
        master.setTotalmoney(master.getTotalmoney()-slaves.getTotalmoney()+totalmoney);
        purchaseMastersMapper.updateByPrimaryKey(master);
        slaves.setTotalmoney(totalmoney);
        int count = purchaseSlavesMapper.updateByPrimaryKey(slaves);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改原料入库成功!");
        }
        return resp;
    }

    /**
     * 原料入库删除
     * @return
     */
    public ResponseJson goodsRukuDel() throws AjaxOperationFailException {
        Integer slaveid = HttpRequestParamter.getInt("slaveid",0);
        if(slaveid==0){
            throw new AjaxOperationFailException("原料入库id不能为空!");
        }
        PurchaseSlaves slaves = purchaseSlavesMapper.selectByPrimaryId(slaveid);
        if(slaves==null){
            throw new AjaxOperationFailException("入库信息不存在!");
        }
        PurchaseMasters master = purchaseMastersMapper.selectByPrimaryKey(slaves.getPurchaseid());
        if(master==null){
            throw new AjaxOperationFailException("主表信息不存在!");
        }
        if(master.getStatus().equals("1")){
            throw new AjaxOperationFailException("入库信息已经审核，不能删除!");
        }
        int count = purchaseSlavesMapper.deleteByPrimaryId(slaveid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }

    /**
     * 原料入库明细列表
     * @return
     */
    public ResponseJson goodsRukuList() throws AjaxOperationFailException {
        Integer purchaseid = HttpRequestParamter.getInt("purchaseid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        if(purchaseid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        PurchaseMasters master = purchaseMastersMapper.selectByPrimaryKey(purchaseid);
        if(master == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        Map params = new HashMap();
        params.put("purchaseid",purchaseid);
        PageHelper.startPage(page,size,"slaveid asc");
        List<Map> list = purchaseSlavesMapper.selectList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;
    }


    /**
     * 入库审核
     * @return
     */
    public ResponseJson rukuAuth() throws AjaxOperationFailException {
        Integer purchaseid = HttpRequestParamter.getInt("purchaseid",0);
        if(purchaseid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        PurchaseMasters master = purchaseMastersMapper.selectByPrimaryKey(purchaseid);
        if(master == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionChukuMaster(master)){
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
        master.setEmployee5id(employeeService.getLoginUser().getEmployeeid());
        int count = purchaseMastersMapper.updateByPrimaryKey(master);
        if(master.getFlag().equals("1")){//入库（直拨不需要更好库存）
            //修改库存（）
            GoodsStock goodsStock = new GoodsStock();
            List<Map> listgoodsid = purchaseSlavesMapper.selectByPurchaseid(purchaseid);
            for (Map goods : listgoodsid) {
                GoodsStock gs = goodsStockMapper.selectByGoodsid(goods);
                double totalMoney = Double.valueOf(goods.get("totalmoney")+"");//总金额
                double amount = Double.valueOf(goods.get("amount")+"");//数量
                if(gs==null){//库存信息不存在
                    goodsStock.setCompanyid(master.getCompanyid());
                    goodsStock.setStoreid(master.getStoreid());
                    goodsStock.setGoodsid(Integer.valueOf(goods.get("goodsid")+""));
                    goodsStock.setWarehouseid(master.getDepartmentid());
                    goodsStock.setUnit(goods.get("unit")+"");
                    goodsStock.setLasttime(new Date());
                    goodsStock.setStock(amount);
                    goodsStock.setStockamount(totalMoney);//入库总金额
                    goodsStock.setYcstock(NumberUtil.round(NumberUtil.div(totalMoney,amount),2).doubleValue());//均价=入库总金额/入库数量
                    goodsStockMapper.insert(goodsStock);//新插入库存数据
                }else {
                    amount = NumberUtil.add(gs.getStock(),amount);
                    totalMoney = NumberUtil.add(gs.getStockamount(),totalMoney);
                    gs.setStock(amount);
                    gs.setStockamount(totalMoney);
                    gs.setYcstock(NumberUtil.round(NumberUtil.div(totalMoney,amount),2).doubleValue());
                    gs.setLasttime(new Date());
                    goodsStockMapper.updateByPrimaryKey(gs);//修改库存
                }
            }
        }
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("审核成功!");
        }
        return resp;
    }


    /**
     *
     * 库存管理库存查询
     * @return
     */
    public ResponseJson goodStockList() {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid",0);
        String nameorcode = HttpRequestParamter.getString("nameorcode",true);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        if(!StringUtil.isEmpty(nameorcode)){
            params.put("nameorcode",nameorcode);
        }
        if(classifyid!=0){
            params.put("classifyid",classifyid);
        }
        if(warehouseid!=0){
            params.put("warehouseid",warehouseid);
        }
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        PageHelper.startPage(page,size,"goodsstockid asc");
        List<Map> list = purchaseSlavesMapper.goodStockList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;

    }

    /**
     * 通过首拼查询原料
     * @return
     */
    public ResponseJson selectbyPinyin() {
        String pinyin = HttpRequestParamter.getString("pinyin",true);
        if(StringUtil.isEmpty(pinyin)){
            return new ResponseJson();
        }
        Map param = new HashMap();
        param.put("companyid",employeeService.getLoginUser().getCompanyid());
        param.put("storeid",employeeService.getLoginUser().getStoreid());
        param.put("pinyin",pinyin);
        List<Map> list = goodsMapper.selectbyPinyin(param);
        return new ResponseJson(list);
    }


    public ResponseJson aa() {
        List<Goods> list = goodsMapper.selectAll();
        for (Goods goods : list) {
            goods.setPinyin(Pinyin.getPinYinHeadChar(goods.getName()));
            goodsMapper.updateByPrimaryKey(goods);
        }
        return new ResponseJson();
    }
}
