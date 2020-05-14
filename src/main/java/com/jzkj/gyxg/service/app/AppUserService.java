package com.jzkj.gyxg.service.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.*;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.*;
import com.jzkj.gyxg.pay.HttpProxy;
import com.jzkj.gyxg.service.BaseService;
import com.jzkj.gyxg.service.web.OrderService;
import com.jzkj.gyxg.util.DateUtil;
import com.jzkj.gyxg.util.HexString;
import com.jzkj.gyxg.util.PhoneCode;
import com.jzkj.gyxg.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class AppUserService extends BaseService {
    @Resource
    private EmployeesMapper employeesMapper;
    @Resource
    private AreasMapper areasMapper;
    @Resource
    private TablesMapper tablesMapper;
    @Resource
    private OrderMasterMapper orderMasterMapper;
    @Resource
    private OrderSlaveMapper orderSlaveMapper;
    @Resource
    private DishesMapper dishesMapper;
    @Resource
    private MethodsMapper methodsMapper;
    @Resource
    private OrderPrintSlaveMapper orderPrintSlaveMapper;
    @Resource
    private OrderPrintMasterMapper orderPrintMasterMapper;
    @Resource
    private CustomersMapper customersMapper;
    @Resource
    private StoresMapper storesMapper;
    @Resource
    private DishesClassfysMapper dishesClassfysMapper;
    @Resource
    private DishePagesMapper dishePagesMapper;
    @Resource
    private PayinfoMapper payinfoMapper;
    @Resource
    private AttachsMapper attachsMapper;
    @Resource
    private AdvertMapper advertMapper;
    @Resource
    private PayRecordsMapper payRecordsMapper;
    @Resource
    private OrderPaysMapper orderPaysMapper;
    @Resource
    private OrderService orderService;
    private Logger logger = LoggerFactory.getLogger("runtimeLogger");

    public ResponseJson login() throws AjaxOperationFailException {
        int storeid = HttpRequestParamter.getInt("storeid");    //店铺id
        String employeeno = HttpRequestParamter.getString("employeeno");   //员工编号
        String pwd = HttpRequestParamter.getString("pwd");   //密码
        if (storeid == 0) {
            throw new AjaxOperationFailException("店铺id不能为空");
        }
        if (StringUtil.isEmpty(employeeno)) {
            throw new AjaxOperationFailException("请输入员工编号");
        }
        if (StringUtil.isNull(pwd)) {
            throw new AjaxOperationFailException("请输入密码");
        }
        Employees employees = employeesMapper.selectByStoreIdAndEmployeenoAndPwd(storeid, employeeno, StringUtil.md5(pwd));
        if (employees == null) {
            throw new AjaxOperationFailException("用户名或密码错误");
        }
        String token = StringUtil.getUUid();
        token = HexString.encode(token);
        List<String> list = PhoneCode.removeUser(redisSession, employees.getEmployeeid(), 0);
        if(StringUtil.isListNull(list)) {
            list = new ArrayList<>();
        }
        list.add(token);

        Map<String, Object> tokenmap = new HashMap<String, Object>();
        tokenmap.put("uid", employees.getEmployeeid());
        tokenmap.put("type", 0);
        tokenmap.put("time", System.currentTimeMillis());

        Map<String, Object> map = new HashMap<String, Object>();
        redisSession.setMapValue(token, tokenmap, 1, TimeUnit.DAYS);
        redisSession.setListValue("userList", list, 1, TimeUnit.DAYS);
        map.put("token", token);
        map.put("employeeno", employeeno);

        //删除缓存中的验证码
        return new ResponseJson("登录成功", map);
    }

    /**
     * 下单
     * @return
     */
    public ResponseJson order() throws AjaxOperationFailException {
        String tablesname = HttpRequestParamter.getString("tablesname");  //餐桌号
        int peopleNum = HttpRequestParamter.getInt("peoplenum");   //用餐人数
        String employeeno = HttpRequestParamter.getString("employeeno");   //员工编号
        String pwd = HttpRequestParamter.getString("pwd");   //密码
        String dishes = HttpRequestParamter.getString("dishes"); //菜品
        int storeid = HttpRequestParamter.getInt("storeid");    //店铺id
        String sendtype = HttpRequestParamter.getString("snedtype");   //等叫方式（0：不等叫  1：等叫）
        if (storeid == 0) {
            throw new AjaxOperationFailException("店铺id不能为空");
        }
        if (StringUtil.isEmpty(employeeno)) {
            throw new AjaxOperationFailException("请输入员工编号");
        }
        if (StringUtil.isNull(pwd)) {
            throw new AjaxOperationFailException("请输入密码");
        }
        if (StringUtil.isNull(tablesname)) {
            throw new AjaxOperationFailException("餐桌号不能为空");
        }
        if (StringUtil.isNull(dishes)) {
            throw new AjaxOperationFailException("请选择菜后下单");
        }

        JSONArray jsonArray = JSON.parseArray(dishes);
        if (jsonArray == null || jsonArray.size() <= 0) {
            throw new AjaxOperationFailException("请选择菜后下单");
        }
        Employees employees = employeesMapper.selectByStoreIdAndEmployeenoAndPwd(storeid, employeeno, StringUtil.md5(pwd));
        if (employees == null) {
            throw new AjaxOperationFailException("用户名或密码错误");
        }

        Tables tables = tablesMapper.selectByName(tablesname, storeid);
        if (tables == null) {
            throw new AjaxOperationFailException("请输入正确的餐厅号");
        }

        OrderSlave orderSlave;
        Dishes dishesinfo;
        List<Dishes> dishesList = new ArrayList<Dishes>();
        List<OrderSlave> orderSlaves = new ArrayList<OrderSlave>();
        List<Dishes> guqingList = new ArrayList<Dishes>();
        for (int i = 0; i < jsonArray.size(); i ++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int dishesid = Integer.parseInt(jsonObject.get("disheid") + "");   //菜品id
            int num = Integer.parseInt(jsonObject.get("num") + "");   //数量
            int methodid = Integer.parseInt(StringUtil.isNull(jsonObject.get("methodid") + "") ? "0": jsonObject.get("methodid") + "");   //制作方法id
            String memo = jsonObject.get("memo") + "";   //备注
            String status = jsonObject.get("status") + "";   //等叫方式（0：不等叫   1：等叫）
            dishesinfo = dishesMapper.selectByPrimaryKey(dishesid);
            if (dishesinfo == null) {
                continue;
            }
            if ("1".equals(dishesinfo.getStatus()) || "1".equals(dishesinfo.getFlagGuqing())) {
                dishesList.add(dishesinfo);
                continue;
            }
            //判断是否是限量沽清，且判断数量是否充足
            if (!"0".equals(dishesinfo.getFlagAmoun())) {
                if (num > dishesinfo.getAmount()) {
                    dishesList.add(dishesinfo);
                    continue;
                } else {
                    guqingList.add(dishesinfo);
                }

            }
            orderSlave = new OrderSlave();
            orderSlave.setDisheid(dishesid);
            orderSlave.setName(dishesinfo.getName());
            orderSlave.setMethodid(methodid);
            orderSlave.setUnit(dishesinfo.getUnit());
            orderSlave.setAmount(num);
            orderSlave.setPrice(dishesinfo.getPrice());
            orderSlave.setMemo(memo);
            orderSlave.setState(dishesinfo.getState());
            orderSlave.setFlag("0");
            orderSlave.setEmployeeid(employees.getEmployeeid());
            if ("1".equals(sendtype)) {
                orderSlave.setStatus("1");
            } else {
                orderSlave.setStatus(status);
            }
            orderSlaves.add(orderSlave);
        }

        //判断是否有已沽清或也下架的商品
        int orderid;
        String type = "0";   //打印类型
        if (StringUtil.isListNull(dishesList) && !StringUtil.isListNull(orderSlaves)) {
            if ("0".equals(tables.getFlag())) {
                //添加点菜主表信息
                OrderMaster orderMaster = new OrderMaster();
                orderMaster.setCompanyid(employees.getCompanyid());
                orderMaster.setStoreid(employees.getStoreid());
                orderMaster.setCustomerid(0);
                orderMaster.setTableid(tables.getTableid());
                if (peopleNum != 0) {
                    orderMaster.setNums(peopleNum);
                }
                orderMaster.setOrdertime(new Date());
                orderMaster.setSeatsfee(tables.getSeatfee());
                orderMaster.setEmployee1id(employees.getEmployeeid());
                orderMaster.setFlag("0");
                orderMaster.setStatus("0");
                orderMaster.setSendtype(sendtype);
                orderMasterMapper.insertSelective(orderMaster);

                //修改餐桌为占用状态
                tables.setFlag("2");
                tablesMapper.updateByPrimaryKey(tables);
                orderid = orderMaster.getOrderid();
            } else {   //获取订单id
                type = "1";
                orderid = tablesMapper.selecOrderIdByTablesId(tables.getTableid()) == null ? 0 : tablesMapper.selecOrderIdByTablesId(tables.getTableid());
                if (peopleNum != 0) {
                    OrderMaster orderMaster = orderMasterMapper.selectByPrimaryId(orderid);
                    orderMaster.setNums(peopleNum);
                    orderMasterMapper.updateByPrimaryId(orderMaster);
                }
            }
            if (orderid != 0) {
                //添加打印信息
                OrderPrintMaster orderPrintMaster = new OrderPrintMaster();
                orderPrintMaster.setCompanyid(employees.getCompanyid());
                orderPrintMaster.setStoreid(employees.getStoreid());
                orderPrintMaster.setOrderid(orderid);
                orderPrintMaster.setCustomerid(0);
                orderPrintMaster.setCode(tables.getName());
                orderPrintMaster.setOrdertime(new Date());
                orderPrintMaster.setNums(peopleNum);
                orderPrintMaster.setEmployeeid(employees.getEmployeeid());
                orderPrintMaster.setFlag("0");
                orderPrintMaster.setStatus("0");
                orderPrintMaster.setSendtype(sendtype);
                orderPrintMaster.setMemo("");
                orderPrintMaster.setStatus("0");
                orderPrintMaster.setType(type);
                orderPrintMasterMapper.insert(orderPrintMaster);

                OrderPrintSlave orderPrintSlave;
                for (OrderSlave orderSlaveInfo: orderSlaves) {
                    dishesinfo = dishesMapper.selectByPrimaryKey(orderSlaveInfo.getDisheid());
                    if ("1".equals(dishesinfo.getFlagAmoun())) {
                        if (dishesinfo.getAmount() - orderSlaveInfo.getAmount() == 0d) {
                            dishesinfo.setFlagGuqing("1");
                        }
                        dishesinfo.setAmount(dishesinfo.getAmount() - orderSlaveInfo.getAmount());
                        dishesMapper.updateByPrimaryKey(dishesinfo);
                    }
                    orderSlaveInfo.setOrderid(orderid);
                    orderSlaveMapper.insert1(orderSlaveInfo);

                    //添加打印信息
                    orderPrintSlave = new OrderPrintSlave();
                    orderPrintSlave.setOrderid(orderPrintMaster.getPrintid());
                    orderPrintSlave.setTablesid(tables.getTableid());
                    orderPrintSlave.setDisheid(orderSlaveInfo.getDisheid());
                    orderPrintSlave.setName(orderSlaveInfo.getName());
                    orderPrintSlave.setUnit(orderSlaveInfo.getUnit());
                    orderPrintSlave.setAmount(orderSlaveInfo.getAmount());
                    orderPrintSlave.setMethodid(orderSlaveInfo.getMethodid());
                    orderPrintSlave.setPrice(orderSlaveInfo.getPrice());
                    orderPrintSlave.setMemo(orderSlaveInfo.getMemo());
                    orderPrintSlave.setFlag(orderSlaveInfo.getFlag());
                    orderPrintSlave.setEmployeeid(orderSlaveInfo.getEmployeeid());
                    orderPrintSlave.setStatus(orderSlaveInfo.getStatus());
                    orderPrintSlaveMapper.insert(orderPrintSlave);
                }
            }
        }

        String token = StringUtil.getUUid();
        token = HexString.encode(token);
        List<String> list = PhoneCode.removeUser(redisSession, employees.getEmployeeid(), 0);
        if(StringUtil.isListNull(list)) {
            list = new ArrayList<>();
        }
        list.add(token);

        Map<String, Object> tokenmap = new HashMap<String, Object>();
        tokenmap.put("uid", employees.getEmployeeid());
        tokenmap.put("type", 0);
        tokenmap.put("time", System.currentTimeMillis());

        Map<String, Object> map = new HashMap<String, Object>();
        redisSession.setMapValue(token, tokenmap, 1, TimeUnit.DAYS);
        redisSession.setListValue("userList", list, 1, TimeUnit.DAYS);
        map.put("token", token);
        map.put("employeeno", employeeno);
//        map.put("orderid", orderid);
        map.put("disheslist", StringUtil.isListNull(dishesList) ? new ArrayList<>() : dishesList);
        map.put("status", StringUtil.isListNull(dishesList) ? 1 : 0);

        return new ResponseJson(StringUtil.isListNull(dishesList) ? "下单成功": "下单失败", map);
    }

    /**
     * 获取就餐区域表
     * @return
     */
    public ResponseJson getArea() throws AjaxOperationFailException {
        int storeid = HttpRequestParamter.getInt("storeid");
        if (storeid == 0) {
            throw new AjaxOperationFailException("店铺id不能为空");
        }
        //获取区域列表
        List<Map<String, Object>> areaList = areasMapper.selectByStoreId(storeid);
        if (!StringUtil.isListNull(areaList)) {
            //获取餐台信息
            List<Map<String, Object>> tables;
            for (Map<String, Object> area: areaList) {
                tables = tablesMapper.selectByAreaId(Integer.parseInt(area.get("areaid") + ""));
                if (StringUtil.isListNull(tables)) {
                    area.put("tables", new ArrayList<Map<String, Object>>());
                } else {
                    area.put("tables", tables);
                }
            }
        }
        return new ResponseJson("获取成功", areaList);
    }

    /**
     * 获取餐桌信息
     * @return
     */
    public ResponseJson getTablesInfo(int type) throws AjaxOperationFailException {
        int tablesid;  //餐桌id
        if (type == 0) {
            tablesid = HttpRequestParamter.getInt("tablesid");
        } else {
            tablesid = HttpRequestParamter.getTableid();
        }
        if (tablesid == 0) {
            throw new AjaxOperationFailException("餐桌id不能为空");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tablesid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        if (!"2".equals(tables.getFlag() + "")) {
            throw new AjaxOperationFailException("该餐桌现为空闲状态");
        }

        Areas areas = areasMapper.selectByPrimaryKey(tables.getAreaid());
        if (areas == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        Integer orderid = tablesMapper.selecOrderIdByTablesId(tables.getTableid());
        if (orderid == null) {
            return new ResponseJson("获取成功", null);
        }

        OrderMaster orderMaster = orderMasterMapper.selectByPrimaryId(orderid);
        if (orderMaster == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }

        List<Map<String, Object>> list = dishesMapper.selectSlaveListByOrderid(orderid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("areaName", areas.getName());
        map.put("tablesName", tables.getName());
        map.put("peopleNum", orderMaster.getNums());
        map.put("memo", orderMaster.getMemo());
        addMethodValue(list, methodsMapper, map, type);
        return new ResponseJson("获取成功", map);
    }

    public static void addMethodValue(List<Map<String, Object>> list, MethodsMapper methodsMapper, Map<String, Object> map, int type) {
        List<Map<String, Object>> method;
        double price = 0;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                       List<Map<String, Object>> disheList = new ArrayList<Map<String, Object>>();
        if (!StringUtil.isListNull(list)) {
            boolean flag;
            String samount;
            String state;
            int amount;
            for (Map<String, Object> dishe: list) {
                samount = dishe.get("samount") + "";
                state = dishe.get("flag") + "";
                if (!StringUtil.isNull(samount)) {
                    amount = Integer.parseInt(samount + "");
                    price += Double.parseDouble(dishe.get("price") + "") * amount;
                    if (type == 0) {
                        amount = Math.abs(amount);
                        dishe.put("amount", amount);
                        dishe.put("num", amount);
                    } else {
                        dishe.put("amount", amount);
                        dishe.put("num", amount);
                    }
                    dishe.remove("samount");
                    if (disheList.size() > 0) {
                        flag = false;
                        for (Map<String, Object> disheinfo: disheList) {
                            if ((disheinfo.get("disheid") + "").equals(dishe.get("disheid") + "")) {
                                if (type == 0) {
                                    if (!"2".equals(state) && state.equals(disheinfo.get("flag") + "")) {
                                        disheinfo.put("amount", amount + Integer.parseInt(disheinfo.get("amount") + ""));
                                        disheinfo.put("num", amount + Integer.parseInt(disheinfo.get("num") + ""));
                                        flag = true;
                                        break;
                                    }
                                } else {
                                    disheinfo.put("amount", amount + Integer.parseInt(disheinfo.get("amount") + ""));
                                    disheinfo.put("num", amount + Integer.parseInt(disheinfo.get("num") + ""));
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if (!flag) {
                            disheList.add(dishe);
                        }
                    } else {
                        disheList.add(dishe);
                    }
                }
                method = methodsMapper.selectByDisheid(dishe.get("disheid") + "");
                if (StringUtil.isListNull(method)) {
                    dishe.put("ismethod", 0);
                    dishe.put("method", new ArrayList<>());
                } else {
                    dishe.put("ismethod", 1);
                    dishe.put("method", method);
                }
            }
            if (disheList.size() > 0) {
                map.put("list", disheList);
            } else {
                if (map != null) {
                    map.put("list", list);
                }
            }
        }
        if (map != null) {
            map.put("price", price);
        }
    }

    /**
     * 退菜
     * @return
     */
    public ResponseJson removeDishes() throws AjaxOperationFailException {
        int uid = HttpRequestParamter.getUid();
        int id = HttpRequestParamter.getInt("id");  //点菜从id
        int num = HttpRequestParamter.getInt("num");   //退菜数量
        String reason = HttpRequestParamter.getString("reason");  //退菜原因
        if (id == 0) {
            throw new AjaxOperationFailException("缺少参数：id");
        }

        if (num <= 0) {
            throw new AjaxOperationFailException("请输入需要退菜份数");
        }

        OrderSlave orderSlave = orderSlaveMapper.selectByPrimaryId(id);
        if (orderSlave == null) {
            throw new  AjaxOperationFailException("已退，请勿重复操作");
        }

        int dishesid = orderSlave.getDisheid();
        int orderid = orderSlave.getOrderid();
        Dishes dishes = dishesMapper.selectByPrimaryKey(dishesid);
        if (dishes == null) {
            throw new AjaxOperationFailException("该菜品不存在");
        }

        Integer count = orderSlaveMapper.selectDishesSumByDishesId(dishesid, orderSlave.getMethodid());
        if (count == null || count < num) {
            throw new AjaxOperationFailException("超出已点菜品数量，剩余："+count+"份");
        }

        OrderMaster orderMaster = orderMasterMapper.selectByPrimaryId(orderid);
        if (orderMaster == null) {
            throw new AjaxOperationFailException("该点菜信息不存在");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(orderMaster.getTableid());

        OrderSlave orderSlaveInfo = new OrderSlave();
        orderSlaveInfo.setOrderid(orderid);
        orderSlaveInfo.setDisheid(dishesid);
        orderSlaveInfo.setName(orderSlave.getName());
        orderSlaveInfo.setMethodid(orderSlave.getMethodid());
        orderSlaveInfo.setUnit(orderSlave.getUnit());
        orderSlaveInfo.setAmount(-num);
        orderSlaveInfo.setPrice(orderSlave.getPrice());
        orderSlaveInfo.setMemo(reason);
        orderSlaveInfo.setFlag("2");
        orderSlaveInfo.setEmployeeid(uid);
        orderSlaveInfo.setState(orderSlave.getState());
        orderSlaveInfo.setState("0");
        orderSlaveMapper.insert1(orderSlaveInfo);


        //添加打印信息
        OrderPrintMaster orderPrintMaster = new OrderPrintMaster();
        orderPrintMaster.setCompanyid(orderMaster.getCompanyid());
        orderPrintMaster.setStoreid(orderMaster.getStoreid());
        orderPrintMaster.setOrderid(orderMaster.getOrderid());
        orderPrintMaster.setCustomerid(0);
        orderPrintMaster.setCode(tables.getName());
        orderPrintMaster.setOrdertime(new Date());
        orderPrintMaster.setNums(orderMaster.getNums());
        orderPrintMaster.setEmployeeid(uid);
        orderPrintMaster.setFlag("0");
        orderPrintMaster.setStatus("0");
        orderPrintMaster.setSendtype(orderMaster.getSendtype());
        orderPrintMaster.setMemo(orderMaster.getMemo());
        orderPrintMaster.setStatus("0");
        orderPrintMaster.setType("2");
        orderPrintMasterMapper.insert(orderPrintMaster);

        OrderPrintSlave orderPrintSlave;
        orderPrintSlave = new OrderPrintSlave();
        orderPrintSlave.setOrderid(orderPrintMaster.getPrintid());
        orderPrintSlave.setTablesid(tables.getTableid());
        orderPrintSlave.setDisheid(orderSlave.getDisheid());
        orderPrintSlave.setName(orderSlave.getName());
        orderPrintSlave.setUnit(orderSlave.getUnit());
        orderPrintSlave.setAmount(orderSlave.getAmount());
        orderPrintSlave.setMethodid(orderSlave.getMethodid());
        orderPrintSlave.setPrice(orderSlave.getPrice());
        orderPrintSlave.setMemo(reason);
        orderPrintSlave.setFlag(orderSlave.getFlag());
        orderPrintSlave.setEmployeeid(orderSlave.getEmployeeid());
        orderPrintSlave.setStatus(orderSlave.getStatus());
        orderPrintSlaveMapper.insert(orderPrintSlave);
        return new ResponseJson("退菜成功");
    }


    /**
     * 修改餐桌人数
     * @return
     */
    public ResponseJson tablesNumEdit() throws AjaxOperationFailException {
        int tablesid = HttpRequestParamter.getInt("tablesid");  //餐桌id
        int num = HttpRequestParamter.getInt("num");   //人数
        if (tablesid == 0) {
            throw new AjaxOperationFailException("餐桌id不能为空");
        }
        if (num == 0) {
            throw new AjaxOperationFailException("修改人数不能为空");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tablesid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        if (!"2".equals(tables.getFlag() + "")) {
            throw new AjaxOperationFailException("该餐桌现为空闲状态，不能修改");
        }
        Integer orderid = tablesMapper.selecOrderIdByTablesId(tables.getTableid());
        if (orderid == null) {
            return new ResponseJson(new ArrayList<>());
        }
        OrderMaster orderMaster = orderMasterMapper.selectByPrimaryId(orderid);
        if (orderMaster == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        orderMaster.setNums(num);
        orderMasterMapper.updateByPrimaryId(orderMaster);
        return new ResponseJson("修改成功");
    }

    /**
     * 下单
     * @return
     */
    public ResponseJson orderDishes() throws AjaxOperationFailException {
        int tablesid = HttpRequestParamter.getTableid();  //餐桌id
        int peopleNum = HttpRequestParamter.getInt("peoplenum");   //用餐人数
        String memo = HttpRequestParamter.getString("memo");
        String dishes = HttpRequestParamter.getString("dishes"); //菜品
        int uid = HttpRequestParamter.getUid();

        if (tablesid == 0) {
            throw new AjaxOperationFailException("请重新扫描二维码进入或联系服务员");
        }
        if (StringUtil.isNull(dishes)) {
            throw new AjaxOperationFailException("请选择菜后再下单");
        }

        JSONArray jsonArray = JSON.parseArray(dishes);
        if (jsonArray == null || jsonArray.size() <= 0) {
            throw new AjaxOperationFailException("请选择菜后再下单");
        }

        Tables tables = tablesMapper.selectByPrimaryKey(tablesid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐台不存在，请联系服务员");
        }

        Stores stores = storesMapper.selectByPrimaryKey(tables.getStoreid());

        OrderSlave orderSlave;
        Dishes dishesinfo;
        List<Dishes> dishesList = new ArrayList<Dishes>();
        List<OrderSlave> orderSlaves = new ArrayList<OrderSlave>();
        for (int i = 0; i < jsonArray.size(); i ++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int dishesid = Integer.parseInt(jsonObject.get("disheid") + "");   //菜品id
            int num = Integer.parseInt(jsonObject.get("num") + "");   //数量
            int methodid = Integer.parseInt(StringUtil.isNull(jsonObject.get("methodid") + "") ? "0": jsonObject.get("methodid") + "");   //制作方法id
            dishesinfo = dishesMapper.selectByPrimaryKey(dishesid);
            if (dishesinfo == null) {
                continue;
            }
            if ("1".equals(dishesinfo.getStatus()) || "1".equals(dishesinfo.getFlagGuqing())) {
                dishesList.add(dishesinfo);
                continue;
            }
            orderSlave = new OrderSlave();
            orderSlave.setDisheid(dishesid);
            orderSlave.setName(dishesinfo.getName());
            orderSlave.setMethodid(methodid);
            orderSlave.setUnit(dishesinfo.getUnit());
            orderSlave.setAmount(num);
            orderSlave.setPrice(dishesinfo.getPrice());
            orderSlave.setMemo("");
            orderSlave.setState(dishesinfo.getState());
            orderSlave.setFlag("0");
            orderSlave.setEmployeeid(0);
            orderSlave.setStatus("0");
            orderSlaves.add(orderSlave);
        }

        //判断是否有已沽清或也下架的商品
        int orderid = 0;
        String type = "0";   //打印类型
        if (StringUtil.isListNull(dishesList) && !StringUtil.isListNull(orderSlaves)) {
            if ("0".equals(tables.getFlag())) {
                //添加点菜主表信息
                OrderMaster orderMaster = new OrderMaster();
                orderMaster.setCompanyid(tables.getCompanyid());
                orderMaster.setStoreid(tables.getStoreid());
                orderMaster.setCustomerid(uid);
                orderMaster.setTableid(tables.getTableid());
                if (peopleNum != 0) {
                    orderMaster.setNums(peopleNum);
                }
                orderMaster.setOrdertime(new Date());
                orderMaster.setSeatsfee(stores.getSeatmoney());
                orderMaster.setTablefee(tables.getFixedfee());
                orderMaster.setEmployee1id(0);
                orderMaster.setFlag("0");
                orderMaster.setStatus("0");
                orderMaster.setSendtype("0");
                orderMaster.setMemo(memo);
                orderMasterMapper.insertSelective(orderMaster);

                //修改餐桌为占用状态
                tables.setFlag("2");
                tablesMapper.updateByPrimaryKey(tables);
                orderid = orderMaster.getOrderid();
            } else {   //获取订单id
                type = "1";
                orderid = tablesMapper.selecOrderIdByTablesId(tables.getTableid()) == null ? 0 : tablesMapper.selecOrderIdByTablesId(tables.getTableid());

                OrderMaster orderMaster = orderMasterMapper.selectByPrimaryId(orderid);
                if (peopleNum != 0) {
                    orderMaster.setNums(peopleNum);
                    orderMasterMapper.updateByPrimaryId(orderMaster);
                }
            }
            if (orderid != 0) {
                //添加打印信息
                OrderPrintMaster orderPrintMaster = new OrderPrintMaster();
                orderPrintMaster.setCompanyid(tables.getCompanyid());
                orderPrintMaster.setStoreid(tables.getStoreid());
                orderPrintMaster.setOrderid(orderid);
                orderPrintMaster.setCustomerid(0);
                orderPrintMaster.setCode(tables.getName());
                orderPrintMaster.setOrdertime(new Date());
                orderPrintMaster.setNums(peopleNum);
                orderPrintMaster.setEmployeeid(0);
                orderPrintMaster.setFlag("0");
                orderPrintMaster.setStatus("0");
                orderPrintMaster.setSendtype("0");
                orderPrintMaster.setMemo(memo);
                orderPrintMaster.setStatus("0");
                orderPrintMaster.setType(type);
                orderPrintMasterMapper.insert(orderPrintMaster);

                OrderPrintSlave orderPrintSlave;
                for (OrderSlave orderSlaveInfo: orderSlaves) {
                    orderSlaveInfo.setOrderid(orderid);
                    orderSlaveMapper.insert1(orderSlaveInfo);

                    //添加打印信息
                    orderPrintSlave = new OrderPrintSlave();
                    orderPrintSlave.setOrderid(orderPrintMaster.getPrintid());
                    orderPrintSlave.setTablesid(tables.getTableid());
                    orderPrintSlave.setDisheid(orderSlaveInfo.getDisheid());
                    orderPrintSlave.setName(orderSlaveInfo.getName());
                    orderPrintSlave.setUnit(orderSlaveInfo.getUnit());
                    orderPrintSlave.setAmount(orderSlaveInfo.getAmount());
                    orderPrintSlave.setMethodid(orderSlaveInfo.getMethodid());
                    orderPrintSlave.setPrice(orderSlaveInfo.getPrice());
                    orderPrintSlave.setMemo(orderSlaveInfo.getMemo());
                    orderPrintSlave.setFlag(orderSlaveInfo.getFlag());
                    orderPrintSlave.setEmployeeid(orderSlaveInfo.getEmployeeid());
                    orderPrintSlave.setStatus(orderSlaveInfo.getStatus());
                    orderPrintSlaveMapper.insert(orderPrintSlave);
                }
            }
        }


//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("orderid", orderid);
//        map.put("disheslist", StringUtil.isListNull(dishesList) ? new ArrayList<>() : dishesList);
//        map.put("status", StringUtil.isListNull(dishesList) ? 1 : 0);

        //删除缓存中的验证码
        if (!StringUtil.isListNull(dishesList)) {
            return new ResponseJson(102,"下单失败",0 , dishesList);
        }
        return new ResponseJson("下单成功");
    }

    public Customers selectUserInfoByTypeAndOpenid(String openId) {
        return customersMapper.selectByWeixinId(openId);
    }

    public Customers insertThreeLogin(String openId, String nickName, String headimgurl, String sex) {
        Customers customers = customersMapper.selectByWeixinId(openId);
        if (customers == null) {
            customers = new Customers();
            customers.setName(nickName);
            customers.setPhone("");
            customers.setSex(sex);
            customers.setBirthday("");
            customers.setNation("");
            customers.setPortrait(headimgurl);
            customers.setCard("");
            customers.setCompany("");
            customers.setAddress("");
            customers.setLove("");
            customers.setAvoid("");
            customers.setWeixinid(openId);
            customers.setRegistertime(new Date());
            customers.setIsVip("0");
            customers.setIsSign("0");
            customers.setStatus("0");
            customersMapper.insert(customers);
        }
        System.out.println("customersInfo:=========" + customers.toString());
        return customers;
    }

    @PostConstruct
    public void init() {
        Payinfo payinfo = payinfoMapper.selectByPrimaryKey(1);
        if (payinfo == null) {
            return;
        }
        redisSession.setValue("payInfo", payinfo);
    }

    /**
     * 获取所有店铺
     * @return
     */
    public ResponseJson getStores() {
        List<Map<String, Object>> list = storesMapper.getStores();
        return new ResponseJson("获取成功", list);
    }

    /**
     * 获取一级店铺菜品分类
     * @return
     */
    public ResponseJson getFirstDisheClassifys() throws AjaxOperationFailException {
        int storeid = HttpRequestParamter.getInt("storeid");
        if (storeid == 0) {
            throw new AjaxOperationFailException("店铺id不能为空");
        }
        List<Map<String, Object>> list = dishesClassfysMapper.selectListByStoreIdAndLevel(storeid, 1);
        return new ResponseJson("获取成功", list);
    }

    /**
     * 获取二级店铺菜品分类
     * @return
     * @throws AjaxOperationFailException
     */
    public ResponseJson getSecondDisheClassifys() throws AjaxOperationFailException {
        int classifyid = HttpRequestParamter.getInt("classifyid");
        if (classifyid == 0) {
            throw new AjaxOperationFailException("一级分类id不能为空");
        }
        List<Map<String, Object>> list = dishesClassfysMapper.selectListByParentid(classifyid);
        return new ResponseJson("获取成功", list);
    }

    /**
     * 获取菜品分类
     * @return
     */
    public ResponseJson getAllDisheClassifys() throws AjaxOperationFailException {
        int storeid = HttpRequestParamter.getInt("storeid");
        if (storeid == 0) {
            throw new AjaxOperationFailException("店铺id不能为空");
        }
        List<Map<String, Object>> list = dishesClassfysMapper.selectListByStoreIdAndLevel(storeid, 1);
        List<Map<String, Object>> second;
        String classifyid;
        if (!StringUtil.isListNull(list)) {
            for (Map<String, Object> map: list) {
                classifyid = map.get("classifyid") + "";
                second = dishesClassfysMapper.selectListByParentid(Integer.parseInt(classifyid));
                map.put("second", second);
            }
        }
        return new ResponseJson("获取成功", list);
    }

    /**
     * 获取菜品列表
     * @return
     */
    public ResponseJson getDisheByClassifys() throws AjaxOperationFailException {
        int classifyid = HttpRequestParamter.getInt("classifyid");
        if (classifyid == 0) {
            throw new AjaxOperationFailException("请选择菜品分类");
        }
        List<Map<String, Object>> list = dishePagesMapper.selectListByClassifys(classifyid);
        if (!StringUtil.isListNull(list)) {
            String disheid;
            String msg;
            List<Map<String, Object>> dishes;
            for (Map<String, Object> map: list) {
                disheid = map.get("disheid") + "";
                msg = "where d.disheid in ("+disheid+")";
                dishes = dishesMapper.selectListByDisheIds(msg);
                map.put("dishe", StringUtil.isListNull(list) ? new ArrayList<>() : dishes);
                AppUserService.addMethodValue(dishes, methodsMapper, null, 1);
            }
        }
        return new ResponseJson("获取成功", list);
    }

    /**
     * 根据名称搜索菜品
     * @return
     */
    public ResponseJson getDishesBySearchName(int type) throws AjaxOperationFailException {
        int storeid;
        if (type == 0) {
            storeid = HttpRequestParamter.getInt("storeid");
        } else {
            int tableid = HttpRequestParamter.getTableid();
            if (type == 0) {
                tableid = HttpRequestParamter.getInt("");
            }
            if (tableid == 0) {
                throw new AjaxOperationFailException("请重新扫描二维码进入或联系服务员");
            }
            Tables tables = tablesMapper.selectByPrimaryKey(tableid);
            if (tables == null) {
                throw new AjaxOperationFailException("该餐桌信息不存在");
            }
            storeid = tables.getStoreid();
        }
        String searchname = HttpRequestParamter.getString("searchname");
        if (storeid == 0) {
            throw new AjaxOperationFailException("店铺id不能为空");
        }
        if (StringUtil.isEmpty(searchname)) {
            throw new AjaxOperationFailException("搜索名称不能为空");
        }
        String msg = "where d.storeid="+storeid+" and d.name like '%"+searchname+"%' ";
        List<Map<String, Object>> dishes =  dishesMapper.selectListByDisheIds(msg);
        AppUserService.addMethodValue(dishes, methodsMapper, null, 1);
        return new ResponseJson("获取成功", dishes);
    }

    /**
     * 支付签到
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePayInfo() {
        logger.info("支付签到！");
        Payinfo payinfo = (Payinfo) redisSession.getValue("payInfo");
        if (payinfo == null) {
            payinfo = payinfoMapper.selectByPrimaryKey(1);
            if (payinfo == null) {
                return;
            }
        }
        HttpProxy hp = new HttpProxy(payinfo.getApiDomain());
        String terminal_sn = payinfo.getTerminalSn();
        String terminal_key = payinfo.getTerminalKey();
        JSONObject jsonObject = hp.checkin(terminal_sn, terminal_key);
        if (jsonObject != null) {
            terminal_key = jsonObject.get("terminal_key") + "";
            terminal_sn = jsonObject.get("terminal_sn") + "";
            payinfo.setTerminalKey(terminal_key);
            payinfo.setTerminalSn(terminal_sn);
            payinfo.setAddtime(new Date());
            payinfoMapper.updateByPrimaryKey(payinfo);
            redisSession.setValue("payInfo", payinfo);
        }
    }

    /**
     * 获取热门菜
     * @return
     */
    public ResponseJson getHotDishes() throws AjaxOperationFailException {
        int storeid = HttpRequestParamter.getInt("storeid");
        if (storeid == 0) {
            throw new AjaxOperationFailException("请选择店铺");
        }
        List<Map<String, Object>> list = dishesMapper.selectHotDishesByStoreId(storeid);
        return new ResponseJson("获取成功", list);
    }

    /**
     * 获取二级分类
     * @return
     */
    public ResponseJson getSecondDisheClassifyByStoreid() throws AjaxOperationFailException {
        int tableid = HttpRequestParamter.getTableid();
        if (tableid == 0) {
            throw new AjaxOperationFailException("请重新扫描二维码进入或联系服务员");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tableid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        int storeid = tables.getStoreid();
        if (storeid == 0) {
            throw new AjaxOperationFailException("请选择店铺");
        }
        List<Map<String, Object>> list = dishesClassfysMapper.selectListByStoreIdAndLevel(storeid, 2);
        return new ResponseJson("获取成功", list);
    }

    /**
     * 根据菜品分类获取菜品列表信息
     * @return
     */
    public ResponseJson getDisheByClassifyId() throws AjaxOperationFailException {
        int classifyId = HttpRequestParamter.getInt("classifyid");
        if (classifyId == 0) {
            throw new AjaxOperationFailException("请选择菜品分类");
        }
        List<Map<String, Object>> list = dishesMapper.selectListByClassIfyId(classifyId);
        AppUserService.addMethodValue(list, methodsMapper, null, 1);
        return new ResponseJson("获取成功", list);
    }

    /**
     * 模拟登录
     * @return
     */
    public ResponseJson wechatLogin() throws AjaxOperationFailException {
        int customersid = HttpRequestParamter.getInt("customersid");
        int tableid = HttpRequestParamter.getInt("tableid");

        Customers customers = customersMapper.selectByPrimaryKey(customersid);
        if (customers == null) {
            throw new AjaxOperationFailException("该客户不存在");
        }
        String token = HexString.encode(StringUtil.getUUid());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", customersid);
        map.put("tableid", tableid);
        redisSession.setMapValue(token, map, 1, TimeUnit.DAYS);
        return new ResponseJson("登录成功", token);
    }

    /**
     * 获取餐台信息
     * @return
     */
    public ResponseJson getTablesNameById() throws AjaxOperationFailException {
        int tablesid = HttpRequestParamter.getTableid();
        if (tablesid == 0) {
            throw new AjaxOperationFailException("餐桌id不能为空");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tablesid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }

        Areas areas = areasMapper.selectByPrimaryKey(tables.getAreaid());
        if (areas == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }


        Map<String, Object> map = new HashMap<String, Object>();
        Integer orderid = tablesMapper.selecOrderIdByTablesId(tables.getTableid());
        if (orderid == null || orderid == 0) {
            map.put("areaName", areas.getName());
            map.put("tablesName", tables.getName());
            map.put("peopleNum", 0);
            map.put("memo", "");
        } else {
            OrderMaster orderMaster = orderMasterMapper.selectByPrimaryId(orderid);
            if (orderMaster == null) {
                throw new AjaxOperationFailException("该餐桌信息不存在");
            }
            map.put("areaName", areas.getName());
            map.put("tablesName", tables.getName());
            map.put("peopleNum", orderMaster.getNums());
            map.put("memo", orderMaster.getMemo());
        }
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("areaName", areas.getName());
//        map.put("tablesName", tables.getName());
        return new ResponseJson("获取成功", map);
    }

    /**
     * 获取退菜原因
     * @return
     */
    public ResponseJson getAttachs() throws AjaxOperationFailException {
        int storeid = HttpRequestParamter.getInt("storeid");
        if (storeid == 0) {
            throw new AjaxOperationFailException("请选择店铺");
        }
        return new ResponseJson("获取成功", attachsMapper.selectByFlag("3", storeid));
    }

    /**
     * 获取餐桌开台状态
     * @return
     * @throws AjaxOperationFailException
     */
    public ResponseJson getTablesStatus() throws AjaxOperationFailException {
        int tablesid = HttpRequestParamter.getTableid();
        if (tablesid == 0) {
            throw new AjaxOperationFailException("餐桌id不能为空");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tablesid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (!"2".equals(tables.getFlag() + "")) {
            map.put("stauts", 0);
        } else {
            map.put("stauts", 1);
        }
        return new ResponseJson("获取成功", map);
    }

    /**
     * 获取banner
     * @return
     */
    public ResponseJson getBanner() throws AjaxOperationFailException {
        int tablesid = HttpRequestParamter.getTableid();
        if (tablesid == 0) {
            throw new AjaxOperationFailException("餐桌id不能为空");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tablesid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        int storeid = tables.getStoreid();
        List<Map<String, Object>> advertList = advertMapper.selectByStoreId(storeid);
        return new ResponseJson("获取成功", advertList);
    }

    /**
     * 支付
     * @return
     * @param resp
     */
    public void payOrder(HttpServletResponse resp) throws AjaxOperationFailException {
        int uid = HttpRequestParamter.getUid();
        int tablesid = HttpRequestParamter.getTableid();
        if (tablesid == 0) {
            throw new AjaxOperationFailException("餐桌id不能为空");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(tablesid);
        if (tables == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        if (!"2".equals(tables.getFlag() + "")) {
            throw new AjaxOperationFailException("该餐桌现为空闲状态");
        }

        Areas areas = areasMapper.selectByPrimaryKey(tables.getAreaid());
        if (areas == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        Integer orderid = tablesMapper.selecOrderIdByTablesId(tables.getTableid());
        if (orderid == null) {
            throw new AjaxOperationFailException("该订单信息存在");
        }

        OrderMaster orderMaster = orderMasterMapper.selectByPrimaryId(orderid);
        if (orderMaster == null) {
            throw new AjaxOperationFailException("该餐桌信息不存在");
        }
        if (!"0".equals(orderMaster.getStatus()) && !"1".equals(orderMaster.getStatus())) {
            throw new AjaxOperationFailException("该订单已处理，请勿重复操作");
        }

        Stores stores = storesMapper.selectByPrimaryKey(orderMaster.getStoreid());

        List<Map<String, Object>> list = dishesMapper.selectSlaveListByOrderid(orderid);
        if (StringUtil.isListNull(list)) {
            throw  new AjaxOperationFailException("该餐桌未点餐");
        }
        List<Map<String, Object>> dishelist = new ArrayList<Map<String, Object>>();
        String disheid;
        int num;
        String flag;
        boolean istrue;
        for (Map<String, Object> map: list) {
            istrue = false;
            disheid = map.get("disheid") + "";
            flag = map.get("flag") + "";
            num = Integer.parseInt(map.get("samount") + "");
            for (Map<String, Object> dishes: dishelist) {
                if (disheid.equals(dishes.get("disheid") + "")) {
                    dishes.put("samount", "0".equals(flag) ? num + Integer.parseInt(dishes.get("samount") + "") : num + Integer.parseInt(dishes.get("samount") + ""));
                    istrue = true;
                    break;
                }
            }
            if (!istrue) {
                dishelist.add(map);
            }
        }
        double price = 0;
        for (Map<String, Object> map: dishelist) {
            price += Integer.parseInt(map.get("samount") + "") * Double.parseDouble(map.get("price") + "");
        }

        double tablefee = 0.0;
        double seatsfee = 0.0;

        double paymoney = price + tablefee + seatsfee;

        //判断是否有支付信息
        //添加支付信息
        String payno = DateUtil.format(new Date(), "yyyyMMddHHmmsssss") + StringUtil.getRandomString(7, 2);
        PayRecords payRecords = new PayRecords();
        payRecords.setCompanyid(tables.getCompanyid());
        payRecords.setStoreid(tables.getStoreid());
        payRecords.setCustomerid(uid);
        payRecords.setPayno(payno);
        payRecords.setPaytype("2");
        payRecords.setPaytypedesc("2");
        payRecords.setPayamount(paymoney);
        payRecords.setFlag("0");
        payRecords.setOverflag("1");
        payRecords.setStatus("0");
        int tag = payRecordsMapper.insert1(payRecords);
        if (tag <= 0) {
            throw new AjaxOperationFailException("录入支付信息失败，请联系工作人员");
        }
        //添加支付结账信息
        OrderPays orderPays = new OrderPays();
        orderPays.setOrderid(orderid);
        orderPays.setPayid(payRecords.getPayid());
        orderPays.setOktime(null);
        orderPays.setStatus("0");
        tag = orderPaysMapper.insert1(orderPays);
        if (tag <= 0) {
            throw new AjaxOperationFailException("录入结账信息失败，请联系工作人员");
        }

        orderMaster.setStatus("1");
        orderMaster.setAmountmoney(price);
        orderMaster.setSeatsfee(seatsfee);
        orderMaster.setTablefee(tablefee);
        orderMaster.setTotalmoney(paymoney);
        orderMaster.setDiscountamount(0.0);
        orderMaster.setPayamount(paymoney);
        tag = orderMasterMapper.updateByPrimaryId(orderMaster);
        if (tag <= 0) {
            throw new AjaxOperationFailException("修改订单信息失败，请联系工作人员");
        }
        try {
            Payinfo payinfo = (Payinfo) redisSession.getValue("payInfo");
            if (payinfo == null) {
                throw new AjaxOperationFailException("支付信息不存在，请联系工作人员");
            }
            List<String> payNoList = redisSession.getListValue("payNoList");
            if(StringUtil.isListNull(payNoList)) {
                payNoList = new ArrayList<String>();
            }
            synchronized (payNoList) {
                payNoList.add(payno);
                redisSession.setListValue("payNoList", payNoList);
            }
            resp.sendRedirect(new HttpProxy("https://api.shouqianba.com").wapapipro(payno, (long)(price * 100) + "",
                    "http://www.cqgyxg.com/dist/#/orderdetails", "订单支付", "cqgyxg", payinfo.getTerminalSn(), payinfo.getTerminalKey()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getDoubleNum(Double num) {
        return num == null ? 0.0: num;
    }

    /**
     * 预支付结果轮询
     */
    @Scheduled(fixedDelay = 5000L)
    public void updateTablesPayInfo() {
        logger.info("预支付结果轮询！！！！");
        List<String> payNoList = redisSession.getListValue("payNoList");
        List<String> list = new ArrayList<String>();
        if (StringUtil.isListNull(payNoList)) {
            return;
        }
        Payinfo payinfo = (Payinfo) redisSession.getValue("payInfo");
        if (payinfo == null) {
            return;
        }
        synchronized (payNoList) {
            PayRecords payRecords;
            OrderPays orderPays;
            OrderMaster orderMaster;
            Tables tables;
            Date date;
            Map<String, Object> result;
            OrderPrintMaster orderPrintMaster;
            for (String payNo: payNoList) {
                date = new Date();
                result = new HttpProxy("https://api.shouqianba.com").query(payinfo.getTerminalSn(),payinfo.getTerminalKey(),
                        "", payNo);
                logger.info("支付结果：" + result);
                if ("200".equals(result.get("result_code") + "") && "SUCCESS".equals(result.get("biz_result_code") + "")) {
                    if ("PAID".equals(result.get("order_status") + "")) {
                        payRecords = payRecordsMapper.selectByPayNo(payNo);
                        if (payRecords == null || !"0".equals(payRecords.getStatus())) {
                            continue;
                        }
                        orderPays = orderPaysMapper.selectByPayId(payRecords.getPayid());
                        if (orderPays == null) {
                            continue;
                        }
                        orderMaster = orderMasterMapper.selectByPrimaryId(orderPays.getOrderid());
                        if (orderMaster == null || !"1".equals(orderMaster.getStatus())) {
                            continue;
                        }
                        tables = tablesMapper.selectByPrimaryKey(orderMaster.getTableid());
                        if (tables == null) {
                            continue;
                        }
                        //1。修改餐桌信息为空闲状态
                        //todo 判断是多台还是单台
//                        tables.setFlag("0");
//                        tablesMapper.updateByPrimaryId(tables);
                        orderService.updateMasterAndTables(orderMaster, orderMaster.getIsmultiple() == null ? 1: orderMaster.getIsmultiple());
                        orderService.createOrderdetailinfo(orderMaster.getOrderid());
                        //2。修改订单状态为结账
                        orderMaster.setStatus("3");
                        orderMasterMapper.updateByPrimaryId(orderMaster);
                        //3。修改支付信息
                        payRecords.setPaytime(date);
                        payRecords.setOverflag("0");
                        payRecords.setStatus("1");
                        payRecords.setReturnno(result.get("sn") + "");
                        payRecords.setPaystatus(result.get("order_status") + "");
                        payRecordsMapper.updateByPrimaryId(payRecords);
                        //4。修改支付结账信息
                        orderPays.setOktime(date);
                        orderPaysMapper.updateByPrimaryId(orderPays);
                        //5。添加结账打印信息
                        orderPrintMaster = new OrderPrintMaster();
                        orderPrintMaster.setCompanyid(tables.getCompanyid());
                        orderPrintMaster.setStoreid(tables.getStoreid());
                        orderPrintMaster.setOrderid(orderPays.getOrderid());
                        orderPrintMaster.setCustomerid(orderMaster.getCustomerid());
                        orderPrintMaster.setCode(tables.getName());
                        orderPrintMaster.setOrdertime(orderMaster.getOrdertime());
                        orderPrintMaster.setNums(orderMaster.getNums());
                        orderPrintMaster.setEmployeeid(0);
                        orderPrintMaster.setFlag("0");
                        orderPrintMaster.setStatus("0");
                        orderPrintMaster.setSendtype(orderMaster.getSendtype());
                        orderPrintMaster.setMemo("");
                        orderPrintMaster.setStatus("0");
                        orderPrintMaster.setType("3");
                        orderPrintMasterMapper.insert(orderPrintMaster);
                    } else if("PAY_CANCELED".equals(result.get("order_status") + "")) {
                        payRecords = payRecordsMapper.selectByPayNo(payNo);
                        if (payRecords == null || !"0".equals(payRecords.getStatus())) {
                            continue;
                        }
                        orderPays = orderPaysMapper.selectByPayId(payRecords.getPayid());
                        if (orderPays == null) {
                            continue;
                        }
                        orderMaster = orderMasterMapper.selectByPrimaryId(orderPays.getOrderid());
                        if (orderMaster == null || !"1".equals(orderMaster.getStatus())) {
                            continue;
                        }
                        tables = tablesMapper.selectByPrimaryKey(orderMaster.getTableid());
                        if (tables == null) {
                            continue;
                        }
                        //2。修改订单状态为开台
                        orderMaster.setStatus("0");
                        orderMasterMapper.updateByPrimaryId(orderMaster);
                        //3。修改支付信息
                        payRecords.setPaytime(date);
                        payRecords.setOverflag("1");
                        payRecords.setStatus("2");
                        payRecords.setReturnno(result.get("sn") + "");
                        payRecords.setPaystatus(result.get("order_status") + "");
                        payRecordsMapper.updateByPrimaryId(payRecords);
                    } else {
                        list.add(payNo);
                    }
                }
            }
            if (!StringUtil.isListNull(list)) {
                redisSession.setListValue("payNoList", list);
            }
        }
    }

    /**
     * 存储支付编号，便于定时查询支付回调
     * @return
     * @throws AjaxOperationFailException
     */
    public ResponseJson redisPayNo() throws AjaxOperationFailException {
        String payno = HttpRequestParamter.getString("payno");
        if (StringUtil.isNull(payno)) {
            throw new AjaxOperationFailException("支付号不能为空");
        }
        List<String> payNoList = redisSession.getListValue("payNoList");
        if(StringUtil.isListNull(payNoList)) {
            payNoList = new ArrayList<String>();
        }
        synchronized (payNoList) {
            payNoList.add(payno);
            redisSession.setListValue("payNoList", payNoList);
        }
        return new ResponseJson("操作成功");
    }
}
