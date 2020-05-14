package com.jzkj.gyxg.service.web;

import cn.hutool.core.util.NumberUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.*;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.*;
import com.jzkj.gyxg.pay.HttpProxy;
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
public class CustomersService {

    @Autowired
    private CustomersMapper customersMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private VipCustomersMapper vipCustomersMapper;

    @Autowired
    private SignCustomersMapper signCustomersMapper;
    @Autowired
    private CustomersAccountsMapper accountsMapper;
    @Autowired
    private OrderMasterMapper masterMapper;
    @Autowired
    private PayRecordsMapper payRecordsMapper;
    @Autowired
    private InvestsMapper investsMapper;
    @Autowired
    private OrderService orderService;

    @Autowired
    private SmsService smsService;
    @Autowired
    private CustomersLogsMapper logsMapper;
    /**
     * 客户列表
     * @return
     */
    public ResponseJson customersList() {
        String nameorphone = HttpRequestParamter.getString("nameorphone",true);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isEmpty(nameorphone)){
            params.put("nameorphone",nameorphone);
        }
        PageHelper.startPage(page,size,"customerid asc");
        List<Map> list = customersMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     *
     * @return
     */
    public ResponseJson addVip() throws AjaxOperationFailException {
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        String cardcode = HttpRequestParamter.getString("cardcode",true);//卡号
        String enddate = HttpRequestParamter.getString("enddate",true);//到期时间
        Integer empid = HttpRequestParamter.getInt("employeeid",0);//业务员
        double discountrate = HttpRequestParamter.getDouble("discountrate",999);//折扣率
        if(customerid == 0 ){
            throw new AjaxOperationFailException("请选择需要升级为VIP的客户!");
        }
        if(StringUtil.isEmpty(cardcode)){
            throw new AjaxOperationFailException("请输入VIP卡号!");
        }
        if(StringUtil.isEmpty(enddate)){
            throw new AjaxOperationFailException("请选择到期时间!");
        }
        ResponseJson resp = new ResponseJson();
        Customers cus = customersMapper.selectByPrimaryKey(customerid);
        if(cus == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(cus.getIsVip().equals("1")){
            throw new AjaxOperationFailException("客户已经为vip用户，请勿重复添加!");
        }
        //修改客户表是vip
        cus.setIsVip("1");
        customersMapper.updateByPrimaryKey(cus);
        //新增vip信息
        VipCustomers vip = new VipCustomers();
        vip.setCustomerid(customerid);
        vip.setCompanyid(employeeService.getLoginUser().getCompanyid());
        vip.setStoreid(employeeService.getLoginUser().getStoreid());
        vip.setBegindate(new Date());
        vip.setCardcode(cardcode);
        vip.setEnddate(DateUtil.toDate(enddate));
        if(discountrate != 999 ){
            if(discountrate<0  || discountrate>1){
                throw new AjaxOperationFailException("折扣必须介于0和1之间!");
            }
            vip.setDiscountrate(discountrate);
            if(checkCardcode(vip)){
                throw new AjaxOperationFailException("vip卡号已经存在，添加失败!");
            }
        }else {
            throw new AjaxOperationFailException("vip卡折扣率必填!");
        }
        vip.setCompanyid(employeeService.getLoginUser().getCompanyid());
        vip.setStoreid(employeeService.getLoginUser().getStoreid());
        vip.setStatus("1");//新增后状态为不可用，审核后才可用
        vip.setCustomerid(customerid);
        vip.setIntime(new Date());
        if(empid != 0){
            vip.setEmployeeid(empid);
        }
        vip.setEmployee1id(employeeService.getLoginUser().getEmployeeid());
        int count = vipCustomersMapper.insert(vip);//插入会员列表
        resp.setMsg("新办会员提交，等待审核!");
        //客户账户信息日志
        CustomersAccounts ca = accountsMapper.selectByCusId(customerid);
        //记录日志
        CustomersLogs logs = new CustomersLogs();
        logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
        logs.setStoreid(employeeService.getLoginUser().getStoreid());
        logs.setCustomerid(customerid);
        logs.setYueamount(ca.getGroupbalance());
        logs.setOpeation("开vip卡");
        logs.setAmount(0.0);
        logs.setMemo("开通vip卡");
        logs.setIntime(new Date());
        logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
        logsMapper.insert(logs);
        return resp;
    }

    public boolean checkCardcode(VipCustomers vip){
        int count = vipCustomersMapper.selectByCardcode(vip);
        if(count > 0){
            return true;
        }else{
            return false;
        }
    }

    public ResponseJson editVip() throws AjaxOperationFailException {
        Integer vipid = HttpRequestParamter.getInt("vipid",0);
        String cardcode = HttpRequestParamter.getString("cardcode",true);//卡号
        String enddate = HttpRequestParamter.getString("enddate",true);//到期时间
        Integer empid = HttpRequestParamter.getInt("employeeid",0);//业务员
        double discountrate = HttpRequestParamter.getDouble("discountrate",999);//折扣率
        if(vipid == 0 ){
            throw new AjaxOperationFailException("id缺失!");
        }
        VipCustomers vip = vipCustomersMapper.selectByPrimaryKey(vipid);
        if(vip == null){
            throw new AjaxOperationFailException("vip信息不存在!");
        }
        if(vip.getStatus().equals("0")){
            throw new AjaxOperationFailException("已经审核，不能修改!");
        }
        if(!isPromission(vip)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(StringUtil.isEmpty(cardcode)){
            throw new AjaxOperationFailException("请输入VIP卡号!");
        }
        if(StringUtil.isEmpty(enddate)){
            throw new AjaxOperationFailException("请选择到期时间!");
        }
        if(discountrate != 999 ){
            if(discountrate<=0  || discountrate>=1){
                throw new AjaxOperationFailException("折扣必须介于0和1之间!");
            }else {
                vip.setDiscountrate(discountrate);
                if(!cardcode.equals(vip.getCardcode())){
                    if(checkCardcode(vip)){
                        throw new AjaxOperationFailException("vip卡号已经存在，修改失败!");
                    }
                }
            }
        }else {
            vip.setDiscountrate(null);
        }
        ResponseJson resp = new ResponseJson();
        Customers cus = customersMapper.selectByPrimaryKey(vip.getCustomerid());
        if(cus == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        vip.setCardcode(cardcode);
        vip.setEnddate(DateUtil.toDate(enddate));
        if(empid != 0){
            vip.setEmployeeid(empid);
        }
        vip.setEmployee1id(employeeService.getLoginUser().getEmployeeid());
        int count = vipCustomersMapper.updateByPrimaryKey(vip);
        resp.setMsg("会员信息修改成功!");
        return resp;
    }



    /**
     * vip客户列表
     * @return
     */
    public ResponseJson vipList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String storename = HttpRequestParamter.getString("storename",true);//店铺名称
        String name = HttpRequestParamter.getString("name",true);//客户名字
        String phone = HttpRequestParamter.getString("phone",true);//客户手机
        String cardcode = HttpRequestParamter.getString("cardcode",true);//vip卡号
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isEmpty(storename)){
            params.put("storename",storename);
        }
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(!StringUtil.isEmpty(phone)){
            params.put("phone",phone);
        }
        if(!StringUtil.isEmpty(cardcode)){
            params.put("cardcode",cardcode);
        }
        PageHelper.startPage(page,size,"vipid desc");
        List<Map> list = vipCustomersMapper.selectVipList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     *
     * @return
     */
    public ResponseJson delVip() throws AjaxOperationFailException {
        Integer vipid = HttpRequestParamter.getInt("vipid",0);
        if(vipid == 0 ){
            throw new AjaxOperationFailException("id缺失!");
        }
        VipCustomers vip = vipCustomersMapper.selectByPrimaryKey(vipid);
        if(vip == null){
            throw new AjaxOperationFailException("vip信息不存在!");
        }
        if(!isPromission(vip)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(vip.getStatus().equals("0")){
            throw new AjaxOperationFailException("会员已经审核，不能删除!");
        }
        Customers cus = customersMapper.selectByPrimaryKey(vip.getCustomerid());
        cus.setIsVip("0");
        customersMapper.updateByPrimaryKey(cus);//解除vip绑定
        //逻辑删除
        vip.setStatus("2");
        int count = vipCustomersMapper.updateByPrimaryKey(vip);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("会员删除成功!");
        }
        return resp;
    }

    /**
     * vip审核
     * @return
     */
    public ResponseJson AuthVip() throws AjaxOperationFailException {
        Integer vipid = HttpRequestParamter.getInt("vipid",0);
        if(vipid == 0 ){
            throw new AjaxOperationFailException("id缺失!");
        }
        VipCustomers vip = vipCustomersMapper.selectByPrimaryKey(vipid);
        if(vip == null){
            throw new AjaxOperationFailException("vip信息不存在!");
        }
        if(!isPromission(vip)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(vip.getEmployee2id() != null){
            throw new AjaxOperationFailException("请勿重复审核!");
        }
        vip.setStatus("0");//vip启用
        vip.setEmployee2id(employeeService.getLoginUser().getEmployeeid());
        //修改vip状态和添加审核人
        vipCustomersMapper.updateByPrimaryKey(vip);
        int count1 = customersMapper.updateCusIsvip(vip.getCustomerid());//修改客户表会员状态
        ResponseJson resp = new ResponseJson();
        resp.setMsg("审核成功，成为vip用户!");
        return resp;
    }

    public boolean isPromission(VipCustomers master){
        Employees employees = employeeService.getLoginUser();
        if(((int)employees.getCompanyid()==(int)master.getCompanyid()) && ((int)employees.getStoreid()==(int)master.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     *客户新增
     * @return
     */
    public ResponseJson customersAdd() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);//客户名字
        String phone = HttpRequestParamter.getString("phone",true);//客户手机
        String sex = HttpRequestParamter.getString("sex",true);//性别
        String birthday = HttpRequestParamter.getString("birthday",true);//birthday
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("姓名不能为空!");
        }
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("电话不能为空!");
        }
        if(StringUtil.isEmpty(sex)){
            throw new AjaxOperationFailException("请选择性别!");
        }
        if(checkPhone(phone)){
            throw new AjaxOperationFailException("电话号码已经存在!");
        }
        Customers cus = new Customers();
        cus.setName(name);
        cus.setPhone(phone);
        cus.setSex(sex);
        cus.setBirthday(birthday);
        cus.setRegistertime(new Date());
        cus.setPasswd(StringUtil.md5("123456"));
        cus.setIsVip("0");
        cus.setIsSign("0");
        cus.setStatus("0");
        int count = customersMapper.insert(cus);
        //新建账户信息
        CustomersAccounts accounts = accountsMapper.selectByCusId(cus.getCustomerid());
        if(accounts == null){
            CustomersAccounts accountnew = new CustomersAccounts();
            accountnew.setCustomerid(cus.getCustomerid());
            accountnew.setCompanyid(employeeService.getLoginUser().getCompanyid());
            accountnew.setSingletotalpoint(0);//本店总积分
            accountnew.setSinglepointbalance(0);//本店积分余额
            accountnew.setSingleconsume(0);//本店消费总金额
            accountnew.setLasttime(new Date());
            accountsMapper.insert(accountnew);
        }
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增客户成功!");
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

    public ResponseJson customersEdit() throws AjaxOperationFailException {
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        String name = HttpRequestParamter.getString("name",true);//客户名字
        String phone = HttpRequestParamter.getString("phone",true);//客户手机
        String sex = HttpRequestParamter.getString("sex",true);//性别
        String birthday = HttpRequestParamter.getString("birthday",true);//birthday
        if(customerid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        Customers cus = customersMapper.selectByPrimaryKey(customerid);
        if(cus == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("姓名不能为空!");
        }
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("电话不能为空!");
        }
        if(StringUtil.isEmpty(sex)){
            throw new AjaxOperationFailException("请选择性别!");
        }
        if(!cus.getPhone().equals(phone)){
            if(checkPhone(phone)){
                throw new AjaxOperationFailException("电话号码已经存在!");
            }
        }
        cus.setName(name);
        cus.setPhone(phone);
        cus.setSex(sex);
        cus.setBirthday(birthday);
        int count = customersMapper.updateByPrimaryKey(cus);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改客户信息成功!");
        }
        return resp;
    }


    public ResponseJson customersDel() throws AjaxOperationFailException {
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        if(customerid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        Customers cus = customersMapper.selectByPrimaryKey(customerid);
        if(cus == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(cus.getIsVip().equals("1")){
            throw new AjaxOperationFailException("该客户为vip客户，不能删除，如果删除请先删除vip信息!");
        }
        if(cus.getIsSign().equals("1")){
            throw new AjaxOperationFailException("该客户为签单客户，不能删除，如果删除请先删除签单信息!");
        }
        int count = customersMapper.deleteByPrimaryKey(customerid);
        accountsMapper.deletebyCuid(customerid);//删除账户信息
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }


    /**
     * 签单客户新增
     * @return
     */
    public ResponseJson addSign() throws AjaxOperationFailException {
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        String sponsor = HttpRequestParamter.getString("sponsor",true);//担保人
        String phone = HttpRequestParamter.getString("phone",true);//担保人电话
        double daymoney = HttpRequestParamter.getDouble("daymoney",0);//日签单额
        double monthmoney = HttpRequestParamter.getDouble("monthmoney",0);//月签单额
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);//业务员
        if(customerid == 0 ){
            throw new AjaxOperationFailException("请选择需要升级为签单的客户!");
        }
        Customers cus = customersMapper.selectByPrimaryKey(customerid);
        if(cus == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(cus.getIsSign().equals("1")){
            throw new AjaxOperationFailException("客户已经是签单客户了，不能重复申请!");
        }
        cus.setIsSign("1");
        customersMapper.updateByPrimaryKey(cus);
        SignCustomers sign = new SignCustomers();
        sign.setCompanyid(employeeService.getLoginUser().getCompanyid());
        sign.setStoreid(employeeService.getLoginUser().getStoreid());
        sign.setStauts("1");//待审核
        sign.setEmployee1id(employeeService.getLoginUser().getEmployeeid());
        sign.setIntime(new Date());
        sign.setCustomerid(customerid);
        sign.setSponsor(sponsor);
        sign.setPhone(phone);
        sign.setDaymoney(daymoney);
        sign.setMonthmoney(monthmoney);
        sign.setEmployeeid(employeeid);
        int count = signCustomersMapper.insert(sign);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新办签单客户提交，等待审核！");
        }
        return resp;
    }

    public ResponseJson editSign() throws AjaxOperationFailException {
        Integer signid = HttpRequestParamter.getInt("signid",0);
        String sponsor = HttpRequestParamter.getString("sponsor",true);//担保人
        String phone = HttpRequestParamter.getString("phone",true);//担保人电话
        double daymoney = HttpRequestParamter.getDouble("daymoney",0);//日签单额
        double monthmoney = HttpRequestParamter.getDouble("monthmoney",0);//月签单额
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);//业务员
        if(signid == 0 ){
            throw new AjaxOperationFailException("id缺失!");
        }
        SignCustomers sign = signCustomersMapper.selectByPrimaryKey(signid);
        if(sign == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(sign.getStauts().equals("0")){
            throw new AjaxOperationFailException("签单客户已审核，修改失败!");
        }
        sign.setIntime(new Date());
        sign.setSponsor(sponsor);
        sign.setPhone(phone);
        sign.setDaymoney(daymoney);
        sign.setMonthmoney(monthmoney);
        sign.setEmployeeid(employeeid);
        int count = signCustomersMapper.updateByPrimaryKey(sign);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改签单客户提交，等待审核！");
        }
        return resp;
    }



    public boolean isPromission(SignCustomers master){
        Employees employees = employeeService.getLoginUser();
        if(((int)employees.getCompanyid()==(int)master.getCompanyid()) && ((int)employees.getStoreid()==(int)master.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 状态改为2，逻辑删除
     * @return
     */
    public ResponseJson delSign() throws AjaxOperationFailException {
        Integer signid = HttpRequestParamter.getInt("signid",0);
        if(signid == 0 ){
            throw new AjaxOperationFailException("id缺失!");
        }
        SignCustomers sign = signCustomersMapper.selectByPrimaryKey(signid);
        if(sign == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(!isPromission(sign)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(sign.getStauts().equals("0")){
            throw new AjaxOperationFailException("签单客户已审核，删除失败!");
        }
        Customers cus = customersMapper.selectByPrimaryKey(sign.getCustomerid());
        cus.setIsSign("0");
        customersMapper.updateByPrimaryKey(cus);//解除sign标志
        sign.setStauts("2");
//        int count = signCustomersMapper.updateByPrimaryId(sign);
        int count = signCustomersMapper.deleteByPrimaryKey(signid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }


    public ResponseJson authSign() throws AjaxOperationFailException {
        Integer signid = HttpRequestParamter.getInt("signid",0);
        if(signid == 0 ){
            throw new AjaxOperationFailException("id缺失!");
        }
        SignCustomers sign = signCustomersMapper.selectByPrimaryKey(signid);
        if(sign == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(!isPromission(sign)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(sign.getStauts().equals("2")){
            throw new AjaxOperationFailException("已删除，不能审核!");
        }
        sign.setEmployee2id(employeeService.getLoginUser().getEmployeeid());
        sign.setOktime(new Date());//生效时间
        sign.setStauts("0");//启用
        signCustomersMapper.updateByPrimaryKey(sign);
        ResponseJson resp = new ResponseJson();
        resp.setMsg("审核成功!");
        return resp;
    }


    public ResponseJson listSign() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String storename = HttpRequestParamter.getString("storename",true);//店铺名称
        String name = HttpRequestParamter.getString("name",true);//客户名字
        String phone = HttpRequestParamter.getString("phone",true);//客户手机
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isEmpty(storename)){
            params.put("storename",storename);
        }
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(!StringUtil.isEmpty(phone)){
            params.put("phone",phone);
        }
        PageHelper.startPage(page,size,"signid desc");
        List<Map> list = signCustomersMapper.selectSignList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    public ResponseJson acountList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String name = HttpRequestParamter.getString("name",true);//客户名字
        String phone = HttpRequestParamter.getString("phone",true);//客户手机
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(!StringUtil.isEmpty(phone)){
            params.put("phone",phone);
        }
        PageHelper.startPage(page,size,"accountid desc");
        List<Map> list = accountsMapper.selectAcountList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * vip查询
     * @return
     */
    public ResponseJson searchVip() throws AjaxOperationFailException {
        String phone = HttpRequestParamter.getString("phone",true);
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("请输入电话号码搜索！");
        }

        Customers customers = customersMapper.selectCusByPhone(phone);
        if(customers == null){
            throw new AjaxOperationFailException("客户信息不存在！");
        }
        CustomersAccounts accounts = accountsMapper.selectByCusId(customers.getCustomerid());
        if(accounts == null){
            throw new AjaxOperationFailException("账户余额信息不存在！");
        }
        Map map = new HashMap();
        map.put("customerid",customers.getCustomerid());
        map.put("phone",customers.getPhone());
        map.put("name",customers.getName());
        map.put("groupbalance",accounts.getGroupbalance());
        VipCustomers vip = vipCustomersMapper.selectByCusid(customers.getCustomerid());
        if(vip!=null && (vip.getDiscountrate()!=null)){
            map.put("discountrate",vip.getDiscountrate());
        }else {
            map.put("discountrate",1);
        }
        ResponseJson resp = new ResponseJson();
        Map dj = payRecordsMapper.selectByCusId(customers.getCustomerid());
        if(dj!=null){
            if(StringUtil.isMapNull(dj)){
                map.put("dingjin","0");
            }else {
                map.put("dingjin",dj.get("payamount"));
            }
        }
        resp.setData(map);
        return resp;
    }

    /**
     * 签单客户查询
     * @return
     */
    public ResponseJson searchSign() throws AjaxOperationFailException {
        String phone = HttpRequestParamter.getString("phone",true);
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("请输入电话号码搜索！");
        }
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        OrderMaster orderMaster = masterMapper.selectByPrimaryId(orderid);
        if(orderMaster == null){
            throw new AjaxOperationFailException("订单信息不存在！");
        }
        Map sign = signCustomersMapper.selectByCusPhone(phone);
        ResponseJson resp = new ResponseJson();
        boolean flag = true;
        if(sign == null){
            resp.setMsg("暂无签单信息！");
            resp.setCode(10000);
            return resp;
        }
        if(Double.valueOf(sign.get("daymoney")+"")==0){//无限制
            flag = true;
            if(Double.valueOf(sign.get("daymoney")+"") < orderMaster.getTotalmoney()){
                resp.setMsg("日签额小于当前消费金额，无法签单支付！");
                resp.setCode(10000);
                flag = false;
            }
        }
        //当月签单未支付的金额
        Double monthsign = masterMapper.selectTotalMoneyByCusid(sign.get("customerid").toString());
        if(Double.valueOf(sign.get("monthmoney")+"")==0){//无限制
            flag = true;
            if(Double.valueOf(sign.get("monthmoney")+"") < monthsign){
                resp.setMsg("月签额小于当前消费金额，无法签单支付！");
                resp.setCode(10000);
                flag = false;
            }
        }
        if(flag){
            sign.put("flag","1");//可以支付
        }else {
            sign.put("flag","0");//不可以支付
        }
        resp.setData(sign);
        return resp;
    }

    /**
     * 定金缴纳
     * @return
     */
    public ResponseJson deposit() throws AjaxOperationFailException {
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        double payamount = HttpRequestParamter.getDouble("payamount",0.00);
        String code = HttpRequestParamter.getString("code",true);
        String memo = HttpRequestParamter.getString("memo",true);
        String flag = HttpRequestParamter.getString("flag",true);//1-充值 2-订金（必填）*
        double rebatemoney = HttpRequestParamter.getDouble("rebatemoney",0.00);//返利金额
        Integer rebatepoint = HttpRequestParamter.getInt("rebatepoint",0);//返利积分
        Integer paytype = HttpRequestParamter.getInt("paytype",0);//支付详细方式： //0：现金 1：支付宝  2：微信  6：余额 7 :银行卡）
        if(customerid == 0 ){
            throw new AjaxOperationFailException("客户id缺失!");
        }
        Customers cus = customersMapper.selectByPrimaryKey(customerid);
        if(cus == null){
            throw new AjaxOperationFailException("客户信息不存在!");
        }
        if(payamount == 0.00){
            throw new AjaxOperationFailException("金额为空!");
        }
        if(paytype == 1 || paytype == 2 ){
            if(StringUtil.isEmpty(code)){
                throw new AjaxOperationFailException("支付码不能为空！");
            }
        }
        if(flag.equals("2")){
            Map dj = payRecordsMapper.selectByCusId(customerid);
            if(!StringUtil.isMapNull(dj)){
                throw new AjaxOperationFailException("目前尚有一笔订金未消费，交订金失败!");
            }
        }
        ResponseJson resp = new ResponseJson();
        String uuid = StringUtil.getUUid();
        PayRecords payRecords = new PayRecords();
        payRecords.setCompanyid(employeeService.getLoginUser().getCompanyid());
        payRecords.setStoreid(employeeService.getLoginUser().getStoreid());
        payRecords.setCustomerid(customerid);
        payRecords.setPayamount(payamount);
        payRecords.setPayno(uuid);
        payRecords.setMemo(memo);
        if(paytype == 0){//现金
            payRecords.setPaytype("0");
            payRecords.setPaytypedesc("0");
            payRecords.setStatus("1");
        }
        if(paytype == 1 || paytype == 2){//支付宝或者微信
            Map map = orderService.pay(uuid,orderService.yuanTofen(payamount),code);
            if(paytype == 2){//微信
                payRecords.setPaytype("2");
                payRecords.setPaytypedesc("2");
            }else {
                payRecords.setPaytype("2");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
                payRecords.setPaytypedesc("1");//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            }
            if(map.get("result_code").equals("200") && !map.get("pay_code").equals("PAY_FAIL")){//成功
                if(map.get("order_status").equals("PAID")){
                    //生成支付记录
                    payRecords.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                    payRecords.setReturnno(map.get("sn")+"");
                    payRecords.setPaystatus(map.get("order_status")+"");
                    payRecords.setStatus("1");
                }else {
                    throw new AjaxOperationFailException("支付失败！");
                }
            }else {
                throw new AjaxOperationFailException("支付失败！");
            }
        }
        if(paytype == 6){//余额
            if(flag.equals("2")){
                payRecords.setOverflag("1");//结算标识：0-已结算，1-未结算
                CustomersAccounts accounts = accountsMapper.selectByCusId(customerid);
                if(accounts == null){
                    throw new AjaxOperationFailException("账户信息不存在!");
                }
                //余额
                double groupbalance = accounts.getGroupbalance();
                if(groupbalance == 0){
                    throw new AjaxOperationFailException("账户余额为0.00元，交订金失败!");
                }
                if(groupbalance < payamount){
                    throw new AjaxOperationFailException("账户余额为"+groupbalance+"元，小于需要交订金金额，交订金失败!");
                }
                //修改账户余额
                accounts.setGroupbalance(groupbalance-payamount);
                accountsMapper.updateByPrimaryKey(accounts);
                payRecords.setPaytype("3");
                payRecords.setPaytypedesc("6");
                payRecords.setStatus("1");
                CustomersLogs logs = new CustomersLogs();
                logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
                logs.setStoreid(employeeService.getLoginUser().getStoreid());
                logs.setCustomerid(customerid);
                logs.setYueamount(accounts.getGroupbalance());
                logs.setOpeation("交订金");
                logs.setMemo(memo);
                logs.setAmount(-payamount);//支出负
                logs.setIntime(new Date());
                logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
                logsMapper.insert(logs);
            }else {
                throw new AjaxOperationFailException("充值不能用余额支付!");
            }
        }
        if(paytype == 7){//银行卡支付
            payRecords.setPaytype("1");
            payRecords.setPaytypedesc("7");
            payRecords.setStatus("1");
        }
        payRecords.setPaytime(new Date());
        payRecords.setFlag(flag);
        payRecords.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
        if("2".equals(flag)){//订金
            payRecords.setOverflag("1");//结算标识：0-已结算，1-未结算
        }
        payRecordsMapper.insert1(payRecords);
        if("1".equals(flag)){//充值
            Invests invests = new Invests();
            invests.setCompanyid(employeeService.getLoginUser().getCompanyid());
            invests.setStoreid(employeeService.getLoginUser().getStoreid());
            invests.setPayid(payRecords.getPayid());
            invests.setCustomerid(customerid);
            invests.setInvesttype("1");
            invests.setInvestmoney(payamount);
            //暂时未定充值返现
            invests.setRebatemoney(rebatemoney);
            invests.setInvesttime(new Date());
            invests.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
            invests.setEmployee1id(employeeid);
            invests.setStatus("1");
            int i = investsMapper.insert(invests);
            //修改账户余额
            CustomersAccounts accounts = accountsMapper.selectByCusId(customerid);
            double money = 0.00;
            if(accounts == null){
                CustomersAccounts accountsnew = new CustomersAccounts();
                accountsnew.setCompanyid(employeeService.getLoginUser().getCompanyid());
                accountsnew.setCustomerid(customerid);
                accountsnew.setGrouptotalmoney(payamount);//充值总金额
                accountsnew.setGroupbalance(payamount+rebatemoney);
                accountsnew.setSingletotalpoint(rebatepoint);
                accountsnew.setSinglepointbalance(rebatepoint);
                accountsMapper.insert(accountsnew);
                resp.setMsg("充值成功!");
                money=accountsnew.getGroupbalance();
            }else {
                accounts.setGrouptotalmoney(accounts.getGrouptotalmoney()+payamount);//充值总金额
                accounts.setGroupbalance(accounts.getGroupbalance()+payamount+rebatemoney);
                accounts.setSingletotalpoint(accounts.getSingletotalpoint()+rebatepoint);
                accounts.setSinglepointbalance(accounts.getSinglepointbalance()+rebatepoint);
                accountsMapper.updateByPrimaryKey(accounts);
                resp.setMsg("充值成功!");
                money=accounts.getGroupbalance();
            }
            CustomersLogs logs = new CustomersLogs();
            logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
            logs.setStoreid(employeeService.getLoginUser().getStoreid());
            logs.setCustomerid(customerid);
            logs.setYueamount(money);
            logs.setOpeation("充值");
            logs.setMemo(memo);
            logs.setAmount(payamount);
            logs.setIntime(new Date());
            logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
            logsMapper.insert(logs);
            //发送短信通知
            smsService.sendMessage(cus.getPhone(),"您好，"+cus.getName()+"，本次充值"+payamount+"元，现有余额"+money+"元。");
        }else {
            resp.setMsg("订金缴纳成功！");
        }
        return resp;
    }

    /**
     * 定金信息查询
     * @return
     */
    public ResponseJson depositinfo() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String startdate = HttpRequestParamter.getString("startdate",true);
        String enddate = HttpRequestParamter.getString("enddate",true);
        String overflag = HttpRequestParamter.getString("overflag",true);
        String nameorphone = HttpRequestParamter.getString("nameorphone",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isEmpty(startdate) && !StringUtil.isEmpty(enddate) ){
            params.put("startdate",startdate);
            params.put("enddate",enddate);
        }
        if(!StringUtil.isEmpty(overflag)){
            params.put("overflag",overflag);
        }
        if(!StringUtil.isEmpty(nameorphone)){
            params.put("nameorphone",nameorphone);
        }
        PageHelper.startPage(page,size,"paytime desc");
        List<Map> list = payRecordsMapper.selectDingjinList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 定金转余额
     * @return
     */
    public ResponseJson depositToAccount() throws AjaxOperationFailException {
        Integer payid = HttpRequestParamter.getInt("payid",0);
        if(payid == 0){
            throw new AjaxOperationFailException("支付id不能为空!");
        }
        PayRecords records = payRecordsMapper.selectByPrimaryId(payid);
        if(records == null){
            throw new AjaxOperationFailException("订金信息不存在!");
        }
        if(records.getOverflag().equals("0")){
            throw new AjaxOperationFailException("订金已经使用，无法转余额!");
        }
        Integer customerid = records.getCustomerid();
        CustomersAccounts accounts = accountsMapper.selectByCusId(customerid);
        if(accounts == null){
            CustomersAccounts accountsnew = new CustomersAccounts();
            accountsnew.setCompanyid(employeeService.getLoginUser().getCompanyid());
            accountsnew.setCustomerid(customerid);
            accountsnew.setGrouptotalmoney(records.getPayamount());//充值总金额
            accountsnew.setGroupbalance(records.getPayamount());
            accountsnew.setSingletotalpoint(0);
            accountsnew.setSinglepointbalance(0);
            accountsMapper.insert(accountsnew);
        }else {
            accounts.setGroupbalance(accounts.getGroupbalance()+records.getPayamount());//余额增加
            accountsMapper.updateByPrimaryKey(accounts);
        }
        records.setOverflag("0");//订金使用了
        payRecordsMapper.updateByPrimaryId(records);
        CustomersAccounts ac = accountsMapper.selectByCusId(customerid);
        CustomersLogs logs = new CustomersLogs();
        logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
        logs.setStoreid(employeeService.getLoginUser().getStoreid());
        logs.setCustomerid(customerid);
        logs.setYueamount(ac.getGroupbalance());
        logs.setOpeation("订金转余额");
        logs.setMemo("订金转余额");
        logs.setAmount(records.getPayamount());
        logs.setIntime(new Date());
        logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
        logsMapper.insert(logs);
        return new ResponseJson("转余额成功!");
    }

    /**
     * 签单信息查询
     * @return
     */
    public ResponseJson signListInfo() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        String startdate = HttpRequestParamter.getString("startdate",true);
        String enddate = HttpRequestParamter.getString("enddate",true);
        String status = HttpRequestParamter.getString("status",true);
        String nameorphone = HttpRequestParamter.getString("nameorphone",true);
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
        if(!StringUtil.isEmpty(nameorphone)){
            params.put("nameorphone",nameorphone);
        }
        if(customerid!=0){
            params.put("customerid",customerid);
        }
        PageHelper.startPage(page,size,"orderid desc");
        List<Map> list = masterMapper.signListInfo(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     *订金退款
     * @return
     */
    public ResponseJson depositRefund() throws AjaxOperationFailException {
        Integer payid = HttpRequestParamter.getInt("payid",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(payid == 0){
            throw new AjaxOperationFailException("支付id不能为空!");
        }
        PayRecords records = payRecordsMapper.selectByPrimaryId(payid);
        if(records == null){
            throw new AjaxOperationFailException("订金信息不存在!");
        }
        if(records.getOverflag().equals("0")){
            throw new AjaxOperationFailException("订金已经使用，无法退款!");
        }
        ResponseJson resp = new ResponseJson();
        records.setStatus("3");//3-退款成功
        records.setOverflag("0");//已经结算
        records.setReturnamount(records.getPayamount());
        records.setReturntime(new Date());
        //现金退款
        if(records.getPaytypedesc().equals("0")){
            resp.setMsg("退款成功！现金方式支付给对方！");
        }
        //支付宝
        if(records.getPaytypedesc().equals("1")){
            //调用收钱吧退款
            records = Refund(records);
            if(records.getStatus().equals("3")){
                resp.setMsg("退款成功！请提醒客户关支护宝信是否到账！");
            }
        }
        //微信
        if(records.getPaytypedesc().equals("2")){
            //调用收钱吧退款
            records = Refund(records);
            if(records.getStatus().equals("3")){
                resp.setMsg("退款成功！请提醒客户关注微信是否到账！");
            }
        }
        if(records.getPaytypedesc().equals("7")){
            resp.setMsg("退款成功！请提醒客户关注银行账户是否到账！");
        }
        if(records.getPaytypedesc().equals("6")){
            CustomersAccounts accounts =  accountsMapper.selectByCusId(records.getCustomerid());
            //余额加回账户
            accounts.setGroupbalance(accounts.getGroupbalance()+records.getPayamount());
            accountsMapper.updateByPrimaryKey(accounts);
            //发送短信*********************
            //客户账户信息日志
            //记录日志
            CustomersLogs logs = new CustomersLogs();
            logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
            logs.setStoreid(employeeService.getLoginUser().getStoreid());
            logs.setCustomerid(accounts.getCustomerid());
            logs.setYueamount(accounts.getGroupbalance());
            logs.setOpeation("订金退款");
            logs.setAmount(records.getPayamount());
            logs.setMemo(memo);
            logs.setIntime(new Date());
            logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
            logsMapper.insert(logs);
        }
        payRecordsMapper.updateByPrimaryId(records);
        return resp;
    }

    /**
     * 充值退款
     * @return
     */
    public ResponseJson chongzhirefund() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(orderid == 0){
            throw new AjaxOperationFailException("充值记录id不能为空!");
        }
        Invests invests = investsMapper.selectByPrimaryKey(orderid);
        if(invests == null){
            throw new AjaxOperationFailException("充值信息不存在!");
        }
        if(invests.getStatus().equals("3")){
            throw new AjaxOperationFailException("已经退款不能重复操作!");
        }
        invests.setStatus("3");
        investsMapper.updateByPrimaryKey(invests);
        PayRecords records = payRecordsMapper.selectByPrimaryId(invests.getPayid());
        if(records == null){
            throw new AjaxOperationFailException("付款信息不存在!");
        }
        if(records.getStatus().equals("3")){
            throw new AjaxOperationFailException("已经退款，无法退款!");
        }
        ResponseJson resp = new ResponseJson();
        records.setStatus("3");//3-退款成功
        records.setOverflag("0");//已经结算
        records.setReturnamount(records.getPayamount());
        records.setReturntime(new Date());
        //现金退款
        if(records.getPaytypedesc().equals("0")){
            resp.setMsg("退款成功！现金方式支付给对方！");
        }
        //支付宝
        if(records.getPaytypedesc().equals("1")){
            //调用收钱吧退款
            records = Refund(records);
            if(records.getStatus().equals("3")){
                resp.setMsg("退款成功！请提醒客户关支护宝信是否到账！");
            }
        }
        //微信
        if(records.getPaytypedesc().equals("2")){
            //调用收钱吧退款
            records = Refund(records);
            if(records.getStatus().equals("3")){
                resp.setMsg("退款成功！请提醒客户关注微信是否到账！");
            }
        }
        if(records.getPaytypedesc().equals("7")){
            resp.setMsg("退款成功！请提醒客户关注银行账户是否到账！");
        }
        //余额改变
        CustomersAccounts accounts =  accountsMapper.selectByCusId(records.getCustomerid());
        accounts.setGroupbalance(accounts.getGroupbalance()-records.getPayamount());
        accounts.setGrouptotalmoney(accounts.getGrouptotalmoney()-records.getPayamount());
        accountsMapper.updateByPrimaryKey(accounts);
        //记录日志
        CustomersLogs logs = new CustomersLogs();
        logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
        logs.setStoreid(employeeService.getLoginUser().getStoreid());
        logs.setCustomerid(accounts.getCustomerid());
        logs.setYueamount(accounts.getGroupbalance());
        logs.setOpeation("充值退款");
        logs.setAmount(-records.getPayamount());//负数
        logs.setMemo(memo);
        logs.setIntime(new Date());
        logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
        logsMapper.insert(logs);
        payRecordsMapper.updateByPrimaryId(records);
        return resp;
    }


    /**
     *退款
     * @param records
     */
    public PayRecords Refund(PayRecords records) throws AjaxOperationFailException {
        HttpProxy hp = new HttpProxy("https://api.shouqianba.com");
        String terminal_sn = "100009200009978067";
        String terminal_key = "ac158be92e8b4989422182197fcc2798";
        records.setRefundno(String.valueOf(System.currentTimeMillis()));
        Map<String, Object> result = hp.refund(terminal_sn, terminal_key, records.getReturnno(), "2908947314912861",String.valueOf(orderService.yuanTofen(records.getPayamount())), records.getRefundno(), "gyxg001");   //退款
        if("200".equals(result.get("result_code"))){
            if(result.containsKey("order_status")){
                records.setStatus("3");
                records.setReturntime(new Date());
                records.setReturnamount(NumberUtil.add(records.getPayamount(),records.getReturnamount()));
            }else {
                throw new AjaxOperationFailException("退款失败");
            }
        }else {
            throw new AjaxOperationFailException("退款失败");
        }
        return records;
    }

    /**
     * 充值记录查询
     * @return
     */
    public ResponseJson investsListBycid() throws AjaxOperationFailException {
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        if(customerid == 0){
            throw new AjaxOperationFailException("客户参数缺失");
        }
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        params.put("customerid",customerid);
        PageHelper.startPage(page,size,"orderid asc");
        List<Map> list = investsMapper.investsListBycid(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    public ResponseJson consumptionListBycusid() throws AjaxOperationFailException {
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        if(customerid == 0){
            throw new AjaxOperationFailException("客户参数缺失");
        }
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        params.put("customerid",customerid);
        PageHelper.startPage(page,size,"payid desc");
        List<Map> list = investsMapper.consumptionListBycusid(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    public ResponseJson kouchu() throws AjaxOperationFailException {
        Integer payid = HttpRequestParamter.getInt("payid",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(payid == 0){
            throw new AjaxOperationFailException("缺失id");
        }
        PayRecords payRecords = payRecordsMapper.selectByPrimaryId(payid);
        if(payRecords == null){
            throw new AjaxOperationFailException("订金信息不存在");
        }
        if(payRecords.getOverflag().equals("0")){
            throw new AjaxOperationFailException("订金已经使用");
        }
        payRecords.setOverflag("0");
        payRecords.setMemo("违约扣除订金"+memo);
        payRecordsMapper.updateByPrimaryId(payRecords);
        return new ResponseJson("订金扣除成功");
    }

    /**
     * 设置客户支付码
     * @return
     */
    public ResponseJson setCuspasswd() throws AjaxOperationFailException {
        String passwd = HttpRequestParamter.getString("passwd",true);
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        if(customerid == 0){
            throw new AjaxOperationFailException("参数id缺失！");
        }
        if(StringUtil.isNull(passwd)){
            throw new AjaxOperationFailException("支付码为空！");
        }
        Customers customers = customersMapper.selectByPrimaryKey(customerid);
        if(customers == null){
            throw new AjaxOperationFailException("客户信息不存在！");
        }
        customers.setPasswd(StringUtil.md5(passwd));
        customersMapper.updateByPrimaryKey(customers);
        //发送短信
        if(!StringUtil.isNull(customers.getPhone())){
            smsService.sendMessage(customers.getPhone(),"余额支付密码设置成功,新的支付密码为"+passwd+"请妥善保管!");
        }
        return new ResponseJson("设置成功");
    }



}
