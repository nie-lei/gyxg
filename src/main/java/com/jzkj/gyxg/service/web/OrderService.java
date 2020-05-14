package com.jzkj.gyxg.service.web;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.*;
import com.jzkj.gyxg.entity.bo.BPayRecords;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.*;
import com.jzkj.gyxg.pay.HttpProxy;
import com.jzkj.gyxg.util.DateUtil;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

@Service
@Transactional
public class OrderService {

    private static EmployeeService employeeService;
    private static TablesMapper tablesMapper;
    private static OrderSlaveMapper orderSlaveMapper;
    private static StoresMapper storesMapper;
    @Autowired
    public void setEmployeeService(EmployeeService employeeService){
        OrderService.employeeService = employeeService;
    }
    @Autowired
    public void setTablesMapper(TablesMapper tablesMapper){
        OrderService.tablesMapper =tablesMapper;
    }
    @Autowired
    public void setOrderSlaveMapper(OrderSlaveMapper orderSlaveMapper){
        OrderService.orderSlaveMapper = orderSlaveMapper;
    }
    @Autowired
    public void setStoresMapper(StoresMapper storesMapper){
        OrderService.storesMapper = storesMapper;
    }

    @Autowired
    private AreasMapper areasMapper;
    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private DishesClassfysMapper dishesClassfysMapper;
    @Autowired
    private DishesMapper dishesMapper;

    @Autowired
    private PayRecordsMapper payRecordsMapper;
    @Autowired
    private CustomersAccountsMapper accountsMapper;
    @Autowired
    private OrderPaysMapper orderPaysMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsStockMapper goodsStockMapper;
    @Autowired
    private OrderPrintMasterMapper printMasterMapper;
    @Autowired
    private OrderPrintSlaveMapper printSlaveMapper;
    @Autowired
    private CustomersService customersService;
    @Autowired
    private OrderFlag2RecordsMapper orderFlag2RecordsMapper;
    @Autowired
    private RedisSession redisSession;
    @Autowired
    private CustomersMapper customersMapper;
    @Autowired
    private OrderPrintMasterMapper printMaster;
    @Autowired
    private SmsService smsService;
    @Autowired
    private PaytypeService paytypeService;
    @Autowired
    private PaytypeMapper paytypeMapper;
    @Autowired
    private CustomersLogsMapper logsMapper;
    @Autowired
    private DiscountMapper discountMapper;


    public ResponseJson selectTables() throws AjaxOperationFailException {
        Integer areaid = HttpRequestParamter.getInt("areaid",0);
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(areaid!=0){
            params.put("areaid",areaid);
        }
        ResponseJson resp = new ResponseJson();
        //所有餐桌
        List<Map> tablesall = tablesMapper.selectAlltablesByPrams(params);
        //此时开台的餐桌
        List<Map> tables0 = tablesMapper.selectTableStatus0();
        if(tables0 != null && tables0.size()>0){
            for (Map map : tablesall) {
                for (Map map0 : tables0) {
                    if((map0.get("tableid")+"").equals(map.get("tableid")+"")){
                        map.put("ordertime",map0.get("ordertime"));
                        map.put("amountmoney",map0.get("amountmoney"));
                        map.put("nums",map0.get("nums"));
                        map.put("flag",map0.get("flag"));
                        map.put("orderid",map0.get("orderid"));
                    }
                }
                //专门针对合台的（当前桌子不是生成订单的餐桌情况）
                if(map.get("state").equals("1") && !map.containsKey("flag")){
                    Map table1 = orderMasterMapper.selectTableByTableidFlag1(Integer.valueOf(map.get("tableid")+""));
                    if(table1==null)continue;
                    map.put("ordertime",table1.get("ordertime"));
                    map.put("amountmoney",table1.get("amountmoney"));
                    map.put("nums",table1.get("nums"));
                    map.put("flag",table1.get("flag"));
                    map.put("orderid",table1.get("orderid"));
                }

            }
        }
        resp.setData(tablesall);
        return resp;
    }

    public ResponseJson selectAreas() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        //所有餐桌
        Map area = new HashMap();
        area.put("areaid",0);
        area.put("name","全部");
        List<Map> areas = new ArrayList<>();
        areas.add(area);
        List<Map> areas1 = areasMapper.selectAllSelect(params);
        areas.addAll(areas1);
        resp.setData(areas);
        return resp;
    }


    /**
     * 当独台或者合台时候可以查找到（注意：联台时候不一定找得到，得去tablesid字段中取查找）
     * @return
     * @throws AjaxOperationFailException
     */
    public ResponseJson tableDetail() throws AjaxOperationFailException {
        Integer tableid = HttpRequestParamter.getInt("tableid",0);
        String flag = HttpRequestParamter.getString("flag",true);
        if(StringUtil.isEmpty(flag)){
            throw new AjaxOperationFailException("flag缺失!");
        }
        if(tableid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        Map master = new HashMap();
        if(flag.equals("2")){//联台
            master = orderMasterMapper.selectByTableId2(tableid);
        }else if(flag.equals("0")){//独台
            master = orderMasterMapper.selectByTableId2(tableid);
        }else {
            master = orderMasterMapper.selectByTableId(tableid);
            Map map = tablesMapper.selectByTableId(tableid);
            master.put("code",map.get("code"));
            master.put("name",map.get("name"));
            master.put("tableid",map.get("tableid"));
        }
        if(master == null){
            throw new AjaxOperationFailException("餐台订单信息不存在!");
        }
        List<Map> toplist = new ArrayList<Map>();
        if(!"0".equals (master.get("flag")+"")){
            String[] ids = master.get("tableids").toString().split(",");
            for (String id : ids) {
                Map table = tablesMapper.selectByTableId(Integer.valueOf(id));
                toplist.add(table);
            }
            master.put("tablelist",toplist);
        }
        List<Map> dishes = orderSlaveMapper.selectByOrderid((Integer) master.get("orderid"));
        Map info = new HashMap();
        info.put("top",master);
        info.put("list",dishes);
        ResponseJson resp = new ResponseJson();
        resp.setData(info);
        return resp;
    }

    /**
     * 开台
     * @return
     */
    public ResponseJson addOrderInfo() throws AjaxOperationFailException {
        Integer tableid = HttpRequestParamter.getInt("tableid",0);
        Integer nums = HttpRequestParamter.getInt("nums",0);
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        String memo = HttpRequestParamter.getString("memo",true);
        String sendtype = HttpRequestParamter.getString("sendtype",true,"0");//等叫方式（0：不等叫   1：整单等叫）
        if(tableid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        if(employeeid == 0){
            throw new AjaxOperationFailException("服务员必选!");
        }
        OrderMaster master = new OrderMaster();
        Tables table = tablesMapper.selectByPrimaryKey(tableid);
        if(table.getFlag().equals("2")){
            throw new AjaxOperationFailException("该餐台占用中，不能开台!");
        }
        Map t = orderMasterMapper.selectByTableId(tableid);
        if(t != null){
            throw new AjaxOperationFailException("该餐台未结账，不能开台!");
        }


        master.setCompanyid(employeeService.getLoginUser().getCompanyid());
        master.setStoreid(employeeService.getLoginUser().getStoreid());
        master.setTableid(tableid);
        master.setSendtype(sendtype);
        master.setNums(nums);
        master.setOrdertime(new Date());
//        master.setSeatsfee(table.getSeatfee()*nums);//餐位费，
        master.setSeatsfee(0.00);//（改为）免单金额
        //餐台服务费
        Stores stores = storesMapper.selectByPrimaryKey(employeeService.getLoginUser().getStoreid());
        Double tablefee = 0.00;//服务费
        if("1".equals(stores.getServicer_flag())){//收取服务费
            tablefee = table.getFixedfee();
        }
        master.setTablefee(tablefee);//服务费（包间费）
        master.setAlsoypay(tablefee);//未支付的金额
        master.setAmountmoney(0.00);
        master.setTotalmoney(tablefee);//消费总金额
        master.setPayamount(tablefee);//应付金额
        master.setFlag("0");
        master.setIsall("0");
        master.setStatus("0");
        master.setEmployeeid(employeeid);//服务员
        master.setEmployee1id(employeeService.getLoginUser().getEmployeeid());//操作员（开台人员）
        master.setMemo(memo);
        int count  = orderMasterMapper.insertSelective(master);
        table.setFlag("2");
        int count1 = tablesMapper.updateByPrimaryKey(table);
        ResponseJson resp = new ResponseJson();
        if(count > 0 && count1>0){
            resp.setMsg("开台成功!");
        }
        return resp;
    }

    /**
     * 就餐人数修改
     * @return
     */
    public ResponseJson editNums() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        Integer nums = HttpRequestParamter.getInt("nums",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在!");
        }
        if(master.getNums() == 0){
            master.setSeatsfee(0.0);
        }else {
            master.setSeatsfee((master.getSeatsfee()/master.getNums())*nums);//修改餐位费
        }
        master.setNums(nums);
        int count = orderMasterMapper.updateByPrimaryId(master);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("就餐人数修改成功");
        }
        return resp;
    }

    /**
     * 合台
     * @return
     */
    public ResponseJson hetai() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        String flag = HttpRequestParamter.getString("flag",true);
        String ids = HttpRequestParamter.getString("ids",true);
        if(orderid == 0){
            throw new AjaxOperationFailException("id缺失!");
        }
        if(StringUtil.isEmpty(flag)){
            throw new AjaxOperationFailException("连台合台标志不能为空!");
        }else {
            if(!flag.equals("1") && !flag.equals("2") ){
                throw new AjaxOperationFailException("flag参数错误!");
            }
        }
        if(StringUtil.isEmpty(ids)){
            throw new AjaxOperationFailException("连台或者合台餐桌信息不能为空!");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在!");
        }
        String [] idarray = ids.split(",");
        for (String id : idarray) {
            Tables table = tablesMapper.selectByPrimaryKey(Integer.valueOf(id));
            if(table == null){
                throw new AjaxOperationFailException("所选餐台信息不存在!");
            }else {
                if(table.getFlag().equals("2")){
                    throw new AjaxOperationFailException("所选餐台"+table.getCode()+"占用中!");
                }
            }
            table.setFlag("2");//改为使用中
            tablesMapper.updateByPrimaryKey(table);//改为使用中
        }
        ResponseJson resp = new ResponseJson();
        if(flag.equals("1")){//合台
            if(master.getFlag().equals("0")){
                master.setTableids(master.getTableid()+","+ids);
            }else {
                master.setTableids(master.getTableids()+","+ids);
            }
            double fee = master.getTablefee();//最开始桌子的服务费
            double seatfee = master.getSeatsfee();
            for (String id : idarray) {
                Tables table = tablesMapper.selectByPrimaryKey(Integer.valueOf(id));
                fee += table.getFixedfee();//服务费
                seatfee+=table.getSeatfee()*table.getSeats();
            }
            master.setTablefee(fee);
            master.setSeatsfee(seatfee);
            master.setFlag("1");
            int count = orderMasterMapper.updateByPrimaryId(master);//直接修改
            resp.setMsg("合台成功!");
        }else {//联台
            if(master.getFlag().equals("0")){
                master.setTableids(master.getTableid()+","+ids);
                master.setFlag("2");
                orderMasterMapper.updateByPrimaryId(master);
            }else {
                //修改
                for (String id : master.getTableids().split(",")) {
                    OrderMaster master1 = orderMasterMapper.selectMasterByTableId(Integer.valueOf(id));
                    master1.setTableids(master.getTableids()+","+ids);
                    orderMasterMapper.updateByPrimaryId(master1);
                }
            }
            //追加生成
            for (String id : idarray) {
                OrderMaster master1 = orderMasterMapper.selectByPrimaryId(orderid);
                Tables table = tablesMapper.selectByPrimaryKey(Integer.valueOf(id));
//                OrderMaster master1 = new OrderMaster();
                master1.setOrderid(null);
                master1.setFlag("2");
                master1.setTableids(master.getTableids());
                master1.setTablefee(table.getFixedfee());//服务费
                master1.setTableid(Integer.valueOf(id));
                master1.setNums(1);
                master1.setOrdertime(new Date());
                master1.setSeatsfee(0.00);//（改为）免单金额
                //餐台服务费
                Stores stores = storesMapper.selectByPrimaryKey(employeeService.getLoginUser().getStoreid());
                Double tablefee = 0.00;//服务费
                if("1".equals(stores.getServicer_flag())){//收取服务费
                    tablefee = table.getFixedfee();
                }
                master1.setTablefee(tablefee);//服务费（包间费）
                master1.setAlsoypay(tablefee);//未支付的金额
                master1.setAmountmoney(0.00);
                master1.setTotalmoney(tablefee);//消费总金额
                master1.setPayamount(tablefee);//应付金额
                master1.setFlag("0");
                master1.setIsall("0");
                master1.setStatus("0");
                orderMasterMapper.insertSelective(master1);
            }
            resp.setMsg("连台成功!");
        }
        return resp;
    }

    public ResponseJson change() throws AjaxOperationFailException {
        Integer oldtableid = HttpRequestParamter.getInt("oldtableid",0);
        Integer newtableid = HttpRequestParamter.getInt("newtableid",0);
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失!");
        }
        Tables oldtable = null;
        if(oldtableid == 0){
            throw new AjaxOperationFailException("原来的餐桌号缺失!");
        }else {
            oldtable=tablesMapper.selectByPrimaryKey(oldtableid);
            if(oldtable == null){
                throw new AjaxOperationFailException("原来的餐桌信息不存在!");
            }
        }
        Tables newtable = null;
        if(newtableid == 0){
            throw new AjaxOperationFailException("需要替换的餐桌号缺失!");
        }else {
            newtable=tablesMapper.selectByPrimaryKey(newtableid);
            if(newtable == null){
                throw new AjaxOperationFailException("需要替换的餐桌信息不存在!");
            }
            if(newtable.getFlag().equals("2")){
                throw new AjaxOperationFailException("需要替换的餐桌占用中!");
            }
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在!");
        }

        //原来桌子改为空闲
        oldtable.setFlag("0");
        tablesMapper.updateByPrimaryKey(oldtable);

        //新桌子状态变更
        newtable.setFlag("2");
        tablesMapper.updateByPrimaryKey(newtable);

        if(master.getFlag().equals("0")){//独台
            //订单主表信息修改
            master.setTableid(newtableid);//更换新桌子
            master.setSeatsfee(newtable.getSeatfee()*master.getNums());//餐位费变更
            master.setTablefee(newtable.getFixedfee());//餐位费变更
            orderMasterMapper.updateByPrimaryId(master);
        }else if (master.getFlag().equals("1")){//合台（只有一条订单信息）
            String idsnew = replaceOldid(master,oldtableid,newtableid);
            master.setTableids(idsnew);//绑定的桌子修改
            //如果当前桌子就是点餐的那桌（则修改当前订单tableid）
            if(master.getTableid() == oldtableid){
                master.setTableid(newtableid);//更换新桌子
            }
            master.setTablefee(master.getTablefee()-oldtable.getFixedfee()+newtable.getSeatfee());//餐位费变更
            master.setSeatsfee(master.getSeatsfee()-(oldtable.getSeatfee()*master.getNums())+(newtable.getSeatfee()*master.getNums()));
            orderMasterMapper.updateByPrimaryId(master);
        }else {//联台（多条订单信息，多条订单都要修改）
            //重新组合绑定tableid信息
            String idsnew = replaceOldid(master,oldtableid,newtableid);
            //关联台订单信息修改
            String[] ids = master.getTableids().split(",");
            for (String id : ids) {
                Map map  = orderMasterMapper.selectByTableId2(Integer.valueOf(id));
                if(map == null){
                    throw new AjaxOperationFailException("关联台订单信息不存在!");
                }else {
                    map.put("tableids",idsnew);
                    orderMasterMapper.updateByMap(map);
                }
            }
            //订单主表信息修改
            master.setTableid(newtableid);//更换新桌子
            master.setTableids(idsnew);//绑定的桌子修改
            master.setSeatsfee(master.getNums()*newtable.getSeatfee());//餐位费变更
            master.setTablefee(newtable.getFixedfee());//餐台服务费
            orderMasterMapper.updateByPrimaryId(master);
        }
        ResponseJson resp = new ResponseJson();
        resp.setMsg("转台成功!");
        return resp;
    }


    public String replaceOldid(OrderMaster master,Integer oldtableid,Integer newtableid){
        String [] ids = master.getTableids().split(",");
        String idsnew = new String();
        for(int i=0;i<ids.length;i++) {
            if (oldtableid == Integer.valueOf(ids[i])){
                ids[i]=String.valueOf(newtableid);//(替换掉原来的id)
            }
        }
        for (int i=0;i<ids.length;i++) {
            if (i<ids.length-1) {
                idsnew+=ids[i]+",";
            }else {
                idsnew+=ids[i];
            }
        }
        return idsnew;
    }

    /**
     * 取消开台(独台取消，联台取消当前台，合台全部取消)
     * @return
     */
    public ResponseJson cancel() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失!");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在!");
        }
        if(!master.getStatus().equals("0")){
            throw new AjaxOperationFailException("开台状态已变更，不能取消!");
        }
        int count = orderSlaveMapper.selectCountByOrderid(orderid);
        if(count > 0){
            throw new AjaxOperationFailException("当前桌子已点菜，不能取消，如果非要取消，请联系管理员!");
        }
        if(master.getFlag().equals("0")){//独台
            Tables tables = tablesMapper.selectByPrimaryKey(master.getTableid());
            tables.setFlag("0");//空闲
            tablesMapper.updateByPrimaryKey(tables);

        }else if(master.getFlag().equals("1")){//合台
            String [] ids  = master.getTableids().split(",");
            for (String id : ids) {
                Tables tables = tablesMapper.selectByPrimaryKey(Integer.valueOf(id));
                tables.setFlag("0");//空闲
                tablesMapper.updateByPrimaryKey(tables);
            }
        }else {//联台
            Tables tables = tablesMapper.selectByPrimaryKey(master.getTableid());
            tables.setFlag("0");//空闲
            tablesMapper.updateByPrimaryKey(tables);
            String [] ids = master.getTableids().split(",");
            List<Integer> listid = orderMasterMapper.selectByorderidGuanlian(master.getOrderid());
            if(ids.length == 2){//2台连台情况下，结账一台，另一台自动变独台
                Integer anotherid = listid.get(0);
                OrderMaster anotherorder = orderMasterMapper.selectByPrimaryId(anotherid);
                anotherorder.setTableids(null);
                anotherorder.setFlag("0");
                orderMasterMapper.updateByPrimaryId(anotherorder);
            }
            if(ids.length > 2){//多台关掉一台，另外几台还是连台
                for (int i=0;i<ids.length;i++) {
                    //追加修改
                    if(master.getTableid() == Integer.valueOf(ids[i])){
                        ids[i]=ids[ids.length-1];
                    }
                }
                ids= Arrays.copyOf(ids,ids.length-1);
                String tableids = "";
                for (int i=0;i<ids.length;i++){
                    if(i<ids.length-1){
                        tableids+=ids[i]+",";
                    }else {
                        tableids+=ids[i];
                    }
                }
                for (String id : ids) {
                    OrderMaster master1 = orderMasterMapper.selectMasterByTableId(Integer.valueOf(id));
                    master1.setTableids(tableids);
                    orderMasterMapper.updateByPrimaryId(master1);
                }
            }
        }
        orderMasterMapper.deleteByPrimaryId(orderid);//删除当前订单
        ResponseJson resp = new ResponseJson();
        resp.setMsg("取消成功!");
        return resp;
    }

    /**
     * 加菜
     * [{
     "disheid": 1,
     "status": "1",
     "amount": 1
     }, {
     "disheid": 5,
     "status": "0",
     "amount": 2
     },
     {
     "disheid": 17,
     "status": "0",
     "amount": 1
     }
     ]
     * @return
     */
    public ResponseJson addDishes(Integer tag) throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        String dishes = HttpRequestParamter.getString("dishes",true);
        if(StringUtil.isEmpty(dishes)){
            throw new AjaxOperationFailException("加菜参数缺失!");
        }
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失!");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if( master == null ){
            throw new AjaxOperationFailException("订单信息不存在!");
        }
        Tables tables = tablesMapper.selectByPrimaryKey(master.getTableid());
        List<Map> list = JSON.parseArray(dishes,Map.class);
        ResponseJson resp = new ResponseJson();
        OrderSlave slave = null;
        Integer empid = employeeService.getLoginUser().getEmployeeid();
        int count = 0;
        //判断是否为已付款订单
        Double discount = 1.0;//折扣率
        boolean flag = false;//是否打折
        if(orderIsSettle(master)){
            discount = master.getDiscountratio();
            flag = true;
        }
        String isAll = master.getIsall();//是否整单打折，1是0否

        //消费消费金额
        Double payamount =0.0;
        Double amountmoney = 0.0;//点菜金额

        //循环加入订单id
        Map param = new HashMap();
        param.put("companyid",employeeService.getLoginUser().getCompanyid());
        param.put("storeid",employeeService.getLoginUser().getStoreid());
        List<OrderSlave> orderSlaves = new ArrayList<OrderSlave>();
        for (Map map : list) {
            Dishes dishe = dishesMapper.selectByPrimaryKey((Integer)map.get("disheid"));
            slave = new OrderSlave();
            slave.setOrderid(orderid);
            slave.setStatus(map.get("status")+"");//状态：0-正常 1-等叫 2-已结账
            slave.setDisheid((Integer) map.get("disheid"));
            slave.setName(dishe.getName());
            slave.setMethodid(dishe.getMethodid());
            slave.setUnit(dishe.getUnit());
            slave.setAmount((Integer) map.get("amount"));
            slave.setPrice(dishe.getPrice());
            slave.setState(dishe.getState());//是否打折，1是0否
            slave.setMemo(map.get("memo")+"");
            slave.setFlag(map.get("flag")+"");
            slave.setEmployeeid(empid);

            double amoney = (dishe.getPrice())*((Integer) map.get("amount"));
            if("0".equals(slave.getFlag())){//点菜

            }else if("1".equals(slave.getFlag())){//赠菜
                amoney = 0.00;
            }else { //退菜
                amoney = -amoney;
            }

            slave.setOrderamount(amoney);//点菜金额
            Double realMoney = amoney;//付款金额
            if((flag && "1".equals(slave.getState())) || "1".equals(isAll) ){//打折
                realMoney = NumberUtil.mul(realMoney,discount);
            }
            slave.setRealamount(realMoney);//付款金额
            //点菜金额加上去
            amountmoney+= amoney;
            //付款金额加上去
            payamount = NumberUtil.add(payamount,realMoney);
            if(dishe.getIsGoods().equals("1")){//是原料，修改库存
                if(dishe.getIsAuto().equals("1")){//自动销售
                    param.put("code",dishe.getCode());
                    Goods goods = goodsMapper.selectBycode(param);
                    param.put("goodsid",goods.getGoodsid());
                    GoodsStock gs = goodsStockMapper.selectByGoodsid(param);
                    gs.setLasttime(new Date());
                    if(gs == null){
                        throw new AjaxOperationFailException(goods.getName()+"库存不存在，操作失败!");
                    }
                    if("2".equals(slave.getFlag())){//退菜，
                        gs.setStock(gs.getStock()+slave.getAmount());
                    }else {//点菜或赠菜
                        if(slave.getAmount() < gs.getStock()){
                            gs.setStock(gs.getStock()-slave.getAmount());
                        }else{
                            throw new AjaxOperationFailException(goods.getName()+"库存不足，点菜失败!");
                        }
                    }
                    goodsStockMapper.updateByPrimaryKey(gs);
                }

            }
            if(dishe.getFlagAmoun().equals("1")) {//限量估清
                if(dishe.getAmount()<(Integer.valueOf(map.get("amount")+""))){
                    throw new AjaxOperationFailException(dishe.getName()+"数量只有"+dishe.getAmount()+"不够，无法点餐！");
                }
                dishe.setAmount(dishe.getAmount()-Integer.valueOf(map.get("amount")+""));
                if(dishe.getAmount() == 0){//数量为0时候
                    dishe.setFlagGuqing("1");
                }
            }
            if(dishe.getFlagGuqing().equals("1")){//沽清
                count=count+1;//沽清的菜数量
            }
            dishesMapper.updateByPrimaryKey(dishe);
//            int i = orderSlaveMapper.insert1(slave);
            orderSlaveMapper.insertSelective(slave);
            orderSlaves.add(slave);
        }
        master.setAmountmoney(NumberUtil.add(master.getAmountmoney(),amountmoney));//点菜金额
        master.setTotalmoney(NumberUtil.add(master.getTotalmoney(),amountmoney));//消费总金额
        master.setPayamount(NumberUtil.add(master.getPayamount(),payamount));//应支付金额
        master.setAlsoypay(NumberUtil.add(master.getAlsoypay(),payamount));//未支付金额
        orderMasterMapper.updateByPrimaryKeySelective(master);//点菜后对应金额增加
        if(tag == 1){
            //添加打印信息
            addPrintMessage(master,orderSlaves);
        }

        if(count > 0){
            resp.setMsg("部分加菜成功"+count+"个菜已经售罄!");
        }else {
            resp.setMsg("加菜成功!");
        }
        return resp;
    }

    /**
     * 点菜和退菜添加打印信息
     * @param master 订单信息
     * @param orderSlaves 操作的菜品
     */
    public void addPrintMessage(OrderMaster master,List<OrderSlave> orderSlaves){
        OrderPrintMaster orderPrintMaster = new OrderPrintMaster();
        orderPrintMaster.setCompanyid(master.getCompanyid());
        orderPrintMaster.setStoreid(master.getStoreid());
        orderPrintMaster.setOrderid(master.getOrderid());
        orderPrintMaster.setCustomerid(master.getCustomerid());
        Tables tables = tablesMapper.selectByPrimaryKey(master.getTableid());
        orderPrintMaster.setCode(tables.getName());
        orderPrintMaster.setOrdertime(new Date());
        orderPrintMaster.setNums(master.getNums());
        orderPrintMaster.setEmployeeid(master.getEmployeeid());
        orderPrintMaster.setFlag(master.getFlag());//标识：0-单独 1-合台  2-连台
        orderPrintMaster.setSendtype(master.getSendtype());//等叫方式（0：不等叫   1：整单等叫）
        orderPrintMaster.setMemo("");
        orderPrintMaster.setStatus("0");//状态：0-未打印,1-已打印
        orderPrintMaster.setType("1");//类型（0：点菜  1：加菜  2：退菜  3：结账）
        int printid =  printMasterMapper.insert(orderPrintMaster);
        OrderPrintSlave orderPrintSlave;
        for (OrderSlave orderSlaveInfo: orderSlaves) {
            orderSlaveInfo.setOrderid(master.getOrderid());
//                orderSlaveMapper.save(orderSlaveInfo);
            //添加打印信息
            orderPrintSlave = new OrderPrintSlave();
            orderPrintSlave.setOrderid(orderPrintMaster.getPrintid());
            orderPrintSlave.setTablesid(master.getTableid());
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
            printSlaveMapper.insert(orderPrintSlave);
        }
    }

    /**
     * 点菜(头部分类只有第2级)
     * @return
     */
    public ResponseJson dishesClassfy2() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = dishesClassfysMapper.selecClassfys2(params);
        resp.setData(list);
        return resp;
    }

    /**
     * 底部菜品列表
     * @return
     */
    public ResponseJson dishesList() throws AjaxOperationFailException {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        if(classifyid == 0){
            throw new AjaxOperationFailException("分类id缺失!");
        }
        Employees employees = employeeService.getLoginUser();
        Map params = new HashMap();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        params.put("classifyid",classifyid);
        List<Map> list = dishesMapper.selectByClassifyId(params);
        ResponseJson resp = new ResponseJson();
        resp.setData(list);
        return resp;
    }


    /**
     * 判断订单是否结过账
     * @param master
     * @return true:付过款，false：未付过款
     */
    public boolean orderIsSettle(OrderMaster master){
        OrderPays op = new OrderPays();
        op.setOrderid(master.getOrderid());
        List opList = orderPaysMapper.select(op);//查询是否有支付记录，（只用查支付关联表就行）
        if(opList!=null && opList.size()>0){
            return true;//表示已经结过账，有结账记录
        }
        //查询连台
        if(master.getTableids()!=null && !"".equals(master.getTableids().trim())){//是连台
            List<OrderMaster> omList =  orderMasterMapper.selectBytablesids(master);
            for (OrderMaster om:omList){
                op.setOrderid(om.getOrderid());
                opList = orderPaysMapper.select(op);
                if(opList!=null && opList.size()>0){
                    return true;//表示已经结过账，有结账记录
                }
            }
        }
        return false;
    }

    /**
     * 获取打折后的金额
     * @param master 订单主表
     * @param discountratio 折扣率
     * @param isall 是否整单 1是0否
     * @return
     */
    public static Double getOrderDiscountMoney(OrderMaster master,Double discountratio,String isall){
        Double total = 0.00;//打折后金额
        OrderSlave slave = new OrderSlave();
        slave.setOrderid(master.getOrderid());
        List<OrderSlave> list = orderSlaveMapper.selectOrderSlaveByOrderId(slave);//查询当前订单的所有点菜
        /*
        计算打折后所有菜品的价格，并且修改订单从表的的付款金额
         */
        for(OrderSlave os : list){
            if("2".equals(os.getStatus())){//已结账直接跳过
                continue;
            }
            if("0".equals(os.getState()) ){//不可以打折的菜品
                if("1".equals(isall)){//整单打折
                    os.setRealamount(NumberUtil.mul(os.getOrderamount(),discountratio));
                    orderSlaveMapper.updateByPrimaryKeySelective(os);
                }
            }else{//可以打折的菜品
                os.setRealamount(NumberUtil.mul(os.getOrderamount(),discountratio));
                orderSlaveMapper.updateByPrimaryKeySelective(os);
            }
            total = NumberUtil.add(os.getRealamount(),total);
        }
        //计算其他费用（餐位费+服务费）
        Stores stores = storesMapper.selectByPrimaryKey(employeeService.getLoginUser().getStoreid());
        //统计餐台服务费
        Double tablefee=tablesMapper.selectByPrimaryKey(master.getTableid()).getFixedfee();
        if("0".equals(stores.getServicer_flag())){//不收收取服务费
            tablefee =0.00;
        }
        if("1".equals(isall)){//整单打折
            total = NumberUtil.add(total,Double.valueOf(NumberUtil.mul(tablefee,discountratio)));
        }else {//非整单打折
            total = NumberUtil.add(total,tablefee);
        }
        return total;
    }

    /**
     * 获取订单打折后的金额(并且修改从表每个菜的付款金额)
     * @param orderId 订单主表id
     * @param discountratio 折扣比例
     * @param isall 是否整单 1是0否
     * @param settleType 结算类型，1单台，2，连台
     * @return
     * @throws AjaxOperationFailException
     */
    public ResponseJson getOrderDiscountAmount(Integer orderId,Double discountratio,String isall,Integer settleType) throws AjaxOperationFailException {
        if(orderId == null)throw new AjaxOperationFailException("订单id缺失!");
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderId);//获取订单信息
        if( master == null )throw new AjaxOperationFailException("订单信息不存在!");
        if(orderIsSettle(master)){//支付金额大于0，说明已经付过款，不能再选折扣
            throw new AjaxOperationFailException("订单在付款中，不能选择折扣!");
        }
        Double total = getAndUpdateOrderMasterByDicount(discountratio,isall,settleType,master);//打折后金额
        ResponseJson resJson = new ResponseJson();
        resJson.setMsg("获取成功！");
        resJson.setData(total);
        return resJson;
    }

    /**
     * 获取打折后金额，并修改打折订单相关信息
     * @param discountratio 折扣率
     * @param isall 是否整单
     * @param settleType 结算类型，1单台，2，连台
     * @param master 订单
     * @throws AjaxOperationFailException
     * return
     */
    public Double getAndUpdateOrderMasterByDicount(Double discountratio,String isall,Integer settleType,OrderMaster master) throws AjaxOperationFailException {
        Double total = 0.00;//打折后金额
        if(settleType==1){
            total = getOrderDiscountMoney(master,discountratio,isall);//打折后金额
        }else {//连台
            //判断是否为一结算连台
//            master.setIsmultiple(2);//连台结算
            List<OrderMaster> masterList = orderMasterMapper.findOrderMasterByType(master);
            if(masterList!=null){//
                throw new AjaxOperationFailException("连台订单在付款中，不能选择折扣!");
            }
            //查询到连台的几张桌子的订单
            List<OrderMaster> list = orderMasterMapper.selectBytablesids(master);
            for(OrderMaster os: list){
                total = NumberUtil.add(total,getOrderDiscountMoney(os,discountratio,isall));
            }
        }
        master.setPayamount(total);//应付金额
        master.setAlsoypay(total);//未付金额
//        master.setDiscountid(discountId);//折扣id
        master.setDiscountratio(discountratio);//折扣比例
        master.setDiscountamount(NumberUtil.sub(master.getTotalmoney(),total));//优惠金额
        master.setIsall(isall);//是否整单
        orderMasterMapper.updateByPrimaryKeySelective(master);
        return total;
    }

    /**
     * 获取单个订单的菜品集合
     * @param master
     * @return
     */
    public static Map<String,Object> getSettOrderSlaveList(OrderMaster master){
        Map param = new HashMap();
        param.put("orderids",master.getOrderid());
        List<Map> dishes = orderSlaveMapper.selectDishesByOrderid(param);//查询所有菜品
        Iterator it = dishes.iterator();
        while (it.hasNext()){
            Map map = (Map) it.next();
            if (Integer.valueOf(map.get("amount")+"")==0) {
                it.remove();
            }
        }
        Map top1 = new HashMap();
        top1.put("tablename",tablesMapper.selectByPrimaryKey(master.getTableid()).getCode());
        top1.put("dishes",dishes);
        return top1;
    }

    /**
     * 预结菜单(单台)
     * @return
     */
    public ResponseJson orderDishesList() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失!");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if( master == null ){
            throw new AjaxOperationFailException("订单信息不存在!");
        }
        if(!orderIsSettle(master)){//没结过账
            Double discountratio = master.getDiscountratio();
            if(master.getDiscountratio()!=0.00){
                discountratio = 1.0;
            }
            getAndUpdateOrderMasterByDicount(discountratio,master.getIsall(),1,master);
        }

        String orderids = "";
        orderids=String.valueOf(orderid);
        Map resultMap = new HashMap();
        Map param = new HashMap();
        param.put("orderids",orderids);
//        //查询所点菜的分类
//        //查询分类下的菜品
        //获取菜品信息,查询所点菜的分类
        List<Map> top1list = new ArrayList<>();
        Map top1 = getSettOrderSlaveList(master);
        top1list.add(top1);
        resultMap.put("top1",top1list);
        param.put("state","0");//不可打折的
        Double top0total = orderSlaveMapper.selectTop1Total(param);
        param.put("state","1");//可以打折的
        Double top1total = orderSlaveMapper.selectTop1Total(param);
        Stores stores = storesMapper.selectByPrimaryKey(employeeService.getLoginUser().getStoreid());
        Map bottom = new HashMap();
        int nums = orderSlaveMapper.selectTotalPersonByOrderid(param);
        double setfee = stores.getSeatmoney();
        bottom.put("nums",nums);//就餐人数合计
        bottom.put("top0total",top0total);//不可打折菜品合计
        bottom.put("top1total",top1total);//可打折菜品合计
        bottom.put("alsoypay",master.getAlsoypay());//未支付金额
        bottom.put("payamount",master.getPayamount());//应付金额
        bottom.put("tablefee",master.getTablefee());//餐台服务费
        bottom.put("discountratio",master.getDiscountratio());//折扣比例
        bottom.put("isall",master.getIsall());//是否整单，1是0否
        bottom.put("totalMoney",master.getTotalmoney());//消费总金额

        resultMap.put("bottom",bottom);
        resultMap.put("ispay",orderIsSettle(master)?1:0);
        resultMap.put("payinfo",paytypeService.selectpaytype().getData());

//        master.setTablefee(tablesMapper.selectByPrimaryId(master.getTableid()).getFixedfee());
//        master.setAmountmoney(top0total+top1total);
//        master.setTotalmoney(master.getAmountmoney()+master.getTablefee());
//        orderMasterMapper.updateByPrimaryId(master);

        ResponseJson resp = new ResponseJson();
        resp.setData(resultMap);
        return resp;
    }

    /**
     * 预结菜单(连台多台)
     * @return
     */
    public ResponseJson orderDishesListAll() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失!");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if( master == null ){
            throw new AjaxOperationFailException("订单信息不存在!");
        }
        if(!"2".equals(master.getFlag())){
            throw new AjaxOperationFailException("订单类型为连台时候才可以多台结算!");
        }
        Integer ismultiple = master.getIsmultiple();
//        master.setIsmultiple(2);//合台结算
        List<OrderMaster> masterList = orderMasterMapper.findOrderMasterByType(master);
        if(masterList!=null){//表示多台已经在结算中
            for(OrderMaster os :masterList){
                if(os.getIsmultiple()==2){
                    master = os;
                    break;
                }
            }
        }else{
            master.setIsmultiple(ismultiple);
        }
        String orderids = "";
        Double tablefee = 0.00;//餐台服务费
        Double alsoypay = 0.00;//未支付金额
        Double totalMoney = 0.0;//消费总金额
        //付过一次款后连台数据统一，没付款时各管各的
        Double payamount = 0.00;//应付金额

        //查询到连台的几张桌子的订单
        List<Map> top1list = new ArrayList<>();
        List<OrderMaster> list = orderMasterMapper.selectBytablesids(master);
        for(OrderMaster os : list){
            top1list.add(getSettOrderSlaveList(os));//查询分类下的菜品
            orderids+=os.getOrderid()+",";//拼接id
            if(!orderIsSettle(os) && master.getDiscountratio()!=0){//没结过账,打过折
                //清空折扣
                os.setDiscountid(0);
                os.setIsall("0");
                os.setDiscountamount(0.0);
                os.setDiscountratio(0.0);
                os.setPayamount(os.getTotalmoney());
                os.setAlsoypay(os.getTotalmoney());
                orderMasterMapper.updateByPrimaryKeySelective(os);
                //清空折扣
                OrderSlave slave = new OrderSlave();
                slave.setOrderid(master.getOrderid());
                List<OrderSlave> slaveList = orderSlaveMapper.selectOrderSlaveByOrderId(slave);//查询当前订单的所有点菜
                for (OrderSlave s:slaveList){
                    s.setRealamount(s.getOrderamount());
//                    s.setState("0");//0不打折，1打折
                    orderSlaveMapper.updateByPrimaryKeySelective(s);
                }
            }
            totalMoney = NumberUtil.add(totalMoney,os.getTotalmoney());
            tablefee = NumberUtil.add(tablefee,os.getTablefee());
            alsoypay = NumberUtil.add(alsoypay,os.getAlsoypay());
            payamount = NumberUtil.add(payamount,os.getPayamount());
        }

        if(orderIsSettle(master)){//已经付过款，就拿当前订单做计算
            tablefee = master.getTablefee();
            alsoypay = master.getAlsoypay();
            payamount = master.getPayamount();
            totalMoney = master.getTotalmoney();
        }
        orderids = StrUtil.removeSuffix(orderids,",");//使ids变成("","","")格式

        Map param = new HashMap();
        param.put("orderids",orderids);
        param.put("state","0");//不可打折的
        Double top0total = orderSlaveMapper.selectTop1Total(param);
        param.put("state","1");//可以打折的
        Double top1total = orderSlaveMapper.selectTop1Total(param);

        int nums = orderSlaveMapper.selectTotalPersonByOrderid(param);
        Map bottom = new HashMap();
        bottom.put("nums",nums);//就餐人数合计

        bottom.put("totalMoney",totalMoney);//消费总金额
        bottom.put("payamount",payamount);//应付金额
        bottom.put("alsoypay",alsoypay);//未支付金额
        bottom.put("tablefee",tablefee);//餐台服务费

        bottom.put("discountratio",master.getDiscountratio());//折扣比例
        bottom.put("isall",master.getIsall());//是否整单，1是0否
//        bottom.put("discountid",master.getDiscountid());//折扣id

        bottom.put("top0total",top0total);//不可打折菜品合计
        bottom.put("top1total",top1total);//可打折菜品合计

        Map resultMap = new HashMap();//返回结果
        resultMap.put("bottom",bottom);
        resultMap.put("isPay",orderIsSettle(master)?1:0);
        resultMap.put("top1",top1list);//菜品列表
        resultMap.put("payinfo",paytypeService.selectpaytype().getData());
        ResponseJson resp = new ResponseJson();
        resp.setData(resultMap);
        return resp;
    }

    public String getLianTaiOrderId(OrderMaster master){
        String orderids = "";
        List<OrderMaster> list = orderMasterMapper.selectBytablesids(master);
        for(OrderMaster os : list){
            orderids+=os.getOrderid()+",";//拼接id
        }
        orderids = StrUtil.removeSuffix(orderids,",");//使ids变成("","","")格式
        return orderids;
    }

//    /**
//     * 预结菜单(连台多台)
//     * @return
//     */
//    public ResponseJson orderDishesListAll() throws AjaxOperationFailException {
//        Integer orderid = HttpRequestParamter.getInt("orderid",0);
//        if(orderid == 0){
//            throw new AjaxOperationFailException("订单id缺失!");
//        }
//        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
//        if( master == null ){
//            throw new AjaxOperationFailException("订单信息不存在!");
//        }
//        master.setTotalmoney(master.getAmountmoney()+master.getTablefee());
//        orderMasterMapper.updateByPrimaryId(master);
//        String orderids = "";
//        double tablefee = 0.00;//餐台服务费
//        Stores stores = storesMapper.selectByPrimaryId(employeeService.getLoginUser().getStoreid());
//        double setfee = stores.getSeatmoney();
//
//        if(master.getFlag().equals("2")){//查询到连台的几张桌子的订单id拼接
//            Map p = new HashMap();
//            p.put("companyid",employeeService.getLoginUser().getCompanyid());
//            p.put("storeid",employeeService.getLoginUser().getStoreid());
//            p.put("tableids",master.getTableids());
//            List<Map> list = null;
//            //反结情况查菜
//            if(master.getStatus().equals("2") || master.getStatus().equals("6")){
//                orderids = orderFlag2RecordsMapper.selectByOrderid1(orderid);
//                orderids = orderids.substring(0,orderids.lastIndexOf(","));
//                p.put("orderids",orderids);
//                list = orderMasterMapper.selectByOtherorderStatus26(p);
//            }else {
//                list = orderMasterMapper.selectBytablesids(p);
//            }
//            orderids = "";
//            for (int i =0;i<list.size();i++) {
//                OrderMaster master1 = orderMasterMapper.selectByPrimaryId(Integer.valueOf(list.get(i).get("orderid")+""));
//                tablefee+=master1.getTablefee();
//                master1.setSeatsfee(setfee*master1.getNums());//餐位费
//                master1.setTotalmoney(master1.getSeatsfee()+master1.getTablefee()+master1.getAmountmoney());
//                orderMasterMapper.updateByPrimaryId(master1);
//                if(i < list.size()-1){
//                    orderids+=list.get(i).get("orderid")+",";
//                }else {
//                    orderids+=list.get(i).get("orderid");
//                }
//            }
//        }else {
//            throw new AjaxOperationFailException("订单类型为连台时候才可以多台结算!");
//        }
//        Map param = new HashMap();
//        param.put("orderids",orderids);
//        Map resultMap = new HashMap();
//        //查询所点菜的分类
//        List<Map> top1list = new ArrayList<>();
//        if(master.getStatus().equals("2") || master.getStatus().equals("6")) {
//            for (String id : orderids.split(",")) {
//                Map top1 = new HashMap();
//                List<Map> dishes = null;
//                top1.put("tablename",tablesMapper.selectByPrimaryId(orderMasterMapper.selectByPrimaryId(Integer.valueOf(id)).getTableid()).getCode());
//                param.put("orderids",Integer.valueOf(id));
//                //查询分类下的菜品
//                dishes = orderSlaveMapper.selectDishesByOrderid(param);
//                Iterator it = dishes.iterator();
//                while (it.hasNext()){
//                    Map map = (Map) it.next();
//                    if (Integer.valueOf(map.get("amount")+"")==0) {
//                        it.remove();
//                    }
//                }
//                top1.put("dishes",dishes);
//                top1list.add(top1);
//            }
//        }else {
//            for (String id : master.getTableids().split(",")){
//                Map top1 = new HashMap();
//                List<Map> dishes = null;
//                top1.put("tablename",tablesMapper.selectByPrimaryId(Integer.valueOf(id)).getCode());
//                param.put("orderids",orderMasterMapper.selectTableId(Integer.valueOf(id)));
//                //查询分类下的菜品
//                dishes = orderSlaveMapper.selectDishesByOrderid(param);
//                Iterator it = dishes.iterator();
//                while (it.hasNext()){
//                    Map map = (Map) it.next();
//                    if (Integer.valueOf(map.get("amount")+"")==0) {
//                        it.remove();
//                    }
//                }
//                top1.put("dishes",dishes);
//                top1list.add(top1);
//            }
//        }
//        param.put("orderids",orderids);
//        resultMap.put("top1",top1list);
//        param.put("state","0");//不可打折的
//        Double top0total = orderSlaveMapper.selectTop1Total(param);
//        param.put("state","1");//可以打折的
//        Double top1total = orderSlaveMapper.selectTop1Total(param);
//
//        Map bottom = new HashMap();
//        param.put("orderids",orderids);
//        int nums = orderSlaveMapper.selectTotalPersonByOrderid(param);
//        double setfeetotal = setfee*nums;
//        bottom.put("nums",nums);//就餐人数合计
//        bottom.put("setfee",setfee);//餐位费
//        bottom.put("setfeetotal",setfeetotal);//餐位费合计
//
//        bottom.put("top0total",top0total);//不可打折菜品合计
//        bottom.put("top1total",top1total);//可打折菜品合计
//        //统计餐台服务费
//        if(stores.getServicer_flag().equals("0")){
//            bottom.put("tablefee",0);
//        }else {
//            bottom.put("tablefee",tablefee);
//        }
//        resultMap.put("bottom",bottom);
//        resultMap.put("payinfo",paytypeService.selectpaytype().getData());
//        ResponseJson resp = new ResponseJson();
//        resp.setData(resultMap);
//        return resp;
//    }


    /**
     * 余额结账
     * @return
     */
    @Transactional
    public ResponseJson useAccount() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        Integer tag = HttpRequestParamter.getInt("tag",1);//1表示单台，2表示多台
        Integer djflag = HttpRequestParamter.getInt("djflag",0);//0表示不使用定金，1表示使用定金
        // 0：现金 1：支付宝  2：微信  6：余额 7 :银行卡
        //支付详细方式：1：微信  2：支付宝  3：现金  4：银行卡 5：余额 6 :签单 7：三倍充值免单 8：霸王餐 9：其他（优惠券）
        Integer paytype = HttpRequestParamter.getInt("paytype",0);
        Integer customerid = HttpRequestParamter.getInt("customerid",0);//客户id(有则为会员结账)
        Integer paytwo = HttpRequestParamter.getInt("paytwo",1);//1表示第一次支付。2表示第二次支付
        String code = HttpRequestParamter.getString("code",true);//支付码
        String passwd = HttpRequestParamter.getString("passwd",true);
        Double payamount = HttpRequestParamter.getDouble("payamount",0.00);//（支付金额）抵扣完（定金+优惠券）后的金额
        Double discountamount = HttpRequestParamter.getDouble("discountamount",0.00);//
        Double discountamount1 = HttpRequestParamter.getDouble("discountamount1",0.00);//免单金额
        Double discountratio = HttpRequestParamter.getDouble("discountratio",1.00);
        Integer quanid = HttpRequestParamter.getInt("quanid",0);
        Integer quannum = HttpRequestParamter.getInt("quannum",0);
        String isall = HttpRequestParamter.getString("isall",true,"0");
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在，请联系管理员！");
        }

        if(tag == 2 ){//连台结账
            //查看是否多次结账、
            Integer ismultiple = master.getIsmultiple();
            master.setIsmultiple(2);//合台结算
            List<OrderMaster> masterList = orderMasterMapper.findOrderMasterByType(master);
            if(masterList!=null){//表示多台已经在结算中,抽取出正在结算的那条订单作为结账
                for(OrderMaster os :masterList){
                    if(os.getIsmultiple()==2){
                        master = os;
                        orderid=master.getOrderid();
                        break;
                    }
                }
            }else{
                master.setIsmultiple(ismultiple);//还原
            }
        }

        if(master.getFlag().equals("2")){//连台相关结账
            updateLinkTable(master,tag);//修改其他连台信息
        }

        master.setStatus("1");//部分结账，如果完全结账，后面会修改
        master.setOvertype("0");
        Employees loginuser = employeeService.getLoginUser();
        if(discountamount1 > loginuser.getDiscountamount()){
            throw new AjaxOperationFailException("你的免单上限为！"+loginuser.getDiscountamount()+"元，如要优惠更多请联系管理员!");
        }
        if("1".equals(paytype) || "2".equals(paytype) || "3".equals(paytype) || "5".equals(paytype)){
            if(StringUtil.isEmpty(code)){
                throw new AjaxOperationFailException("支付码不能为空！");
            }
        }

        //生成支付记录
        PayRecords payRecordsnew = new PayRecords();

        //获取预结算信息
//        if(tag == 2 ){
//            Map resulmap = (Map) orderDishesListAll().getData();
//            Map map0 = (Map) resulmap.get("bottom");
//            master.setNums(Integer.valueOf(map0.get("nums")+""));//人数
//            master.setTotalmoney(Double.valueOf(map0.get("totalMoney")+""));//消费总金额
//            master.setPayamount(Double.valueOf(map0.get("payamount")+""));//应付金额
//            master.setAlsoypay(Double.valueOf(map0.get("alsoypay")+""));//未付付金额
//            master.setTablefee(Double.valueOf(map0.get("tablefee")+""));//餐台服务费
////            master.setSeatsfee(Double.valueOf(map0.get("setfeetotal")+""));//免单金额
//        }
        if(orderIsSettle(master) ){//说明没有支付过，可以修改比例和金额
            master.setSeatsfee(discountamount1);
            master.setDiscountamount(NumberUtil.sub(master.getTotalmoney(),master.getPayamount(),master.getSeatsfee()).doubleValue());
            master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),discountamount1));
            master.setPayamount(NumberUtil.sub(master.getPayamount(),discountamount1));
        }


        ResponseJson resp = new ResponseJson();
        resp.setData(0);
        if(customerid!=0) {
            master.setCustomerid(customerid);
            if (djflag == 1) {//定金抵扣
                Map map = payRecordsMapper.selectByCusId(customerid);
                PayRecords records = payRecordsMapper.selectByPrimaryId(Integer.valueOf(map.get("payid") + ""));
                if(records == null){
                    throw new AjaxOperationFailException("订金信息不存在！");
                }
                records.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                records.setOverflag("0");//结算标识：0-已结算，1-未结算
                payRecordsMapper.updateByPrimaryId(records);
                //插入支付记录绑定
                OrderPays orderPays = new OrderPays();
                orderPays.setOktime(new Date());
                orderPays.setStatus("0");
                orderPays.setOrderid(orderid);
                orderPays.setPayid(records.getPayid());
                orderPaysMapper.insert1(orderPays);
                master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),Double.valueOf(records.getPayamount())));
                payamount = NumberUtil.sub(payamount.doubleValue(),records.getPayamount());
                orderMasterMapper.updateByPrimaryKeySelective(master);
                if(master.getAlsoypay()<=0){
                    //订单完成把餐桌信息改成未占用
                    master = updateMasterAndTables(master, tag);
                    master.setOvertime(new Date());
                    orderMasterMapper.updateByPrimaryKeySelective(master);
                    createOrderdetailinfo(orderid);
                    return new ResponseJson("支付成功",1);
                }
            }
            if (paytype == 6) {
                if(StringUtil.isNull(passwd)){
                    throw new AjaxOperationFailException("支付密码不能为空！");
                }
                Customers cus = customersMapper.selectByPrimaryKey(customerid);//客户信息
                if(!cus.getPasswd().equals(StringUtil.md5(passwd))){
                    throw new AjaxOperationFailException("支付密码错误请重新输入！");
                }
                CustomersAccounts accounts = accountsMapper.selectByCusId(customerid);
                //余额
                double groupbalance = accounts.getGroupbalance();
                if (groupbalance > payamount) {
                    groupbalance = groupbalance - payamount;
                    payRecordsnew.setPayamount(payamount);//
                    accounts.setGroupbalance(groupbalance);
                    master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),payamount));
                    //订单完成把餐桌信息改成未占用
                    orderMasterMapper.updateByPrimaryKeySelective(master);
//                    if(master.getAlsoypay()<=0){//已支付完毕，清理
//                        master = updateMasterAndTables(master, tag);
//                    }
                    resp.setMsg("余额足够支付，支付成功!");
                    //发送短信通知
                    smsService.sendMessage(cus.getPhone(),"您好，"+cus.getName()+"，本次消费"+payamount+"元，现有余额"+groupbalance+"元。");
                    resp.setData(0);
                }else {
                    throw new AjaxOperationFailException("余额不足，可以先充值也可以换其他方式支付！");
                }
                //修改账户余额
                accountsMapper.updateByPrimaryKey(accounts);
                //生成支付记录
                payRecordsnew.setPaytype("5");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
                payRecordsnew.setPaytypedesc("5");//付详细方式：1：微信  2：支付宝  3：现金  4：银行卡 5：余额 6 :签单 7：三倍充值免单 8：霸王餐 9：其他（优惠券）
                payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                createPayInfo(payRecordsnew, customerid, orderid);
                //客户账户信息日志
                CustomersLogs logs = new CustomersLogs();
                logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
                logs.setStoreid(employeeService.getLoginUser().getStoreid());
                logs.setCustomerid(customerid);
                logs.setYueamount(accounts.getGroupbalance());
                logs.setOpeation("消费");
                logs.setAmount(-payamount);//负数
                logs.setMemo("消费"+payamount+"元");
                logs.setIntime(new Date());
                logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
                logsMapper.insert(logs);

            }

        }
        master.setCustomerid(customerid);
        //优惠券抵扣
        if(quanid!=0){
            Paytype quan = paytypeMapper.selectByPrimaryId(quanid);
            if(quan == null){
                throw new AjaxOperationFailException("使用优惠券不存在！");
            }
            if(quan.getStatus().equals("1")){
                throw new AjaxOperationFailException("优惠券活动已过！");
            }
            //消费金额小于最小用券金额
            if(quan.getMinmoney() > master.getTotalmoney()){
                throw new AjaxOperationFailException("消费金额不足无法使用优惠券！");
            }
            if(quannum==0){
                throw new AjaxOperationFailException("使用优惠券数量不能为空！");
            }else {
                //查询已经使用的优惠券张数 TODO
                //使用张数限制小于实际张数
                if(quan.getQuannum() < quannum){
                    throw new AjaxOperationFailException("此优惠券最多使用"+quan.getQuannum()+"张");
                }
            }
            Double quanMoney = NumberUtil.mul(quannum,quan.getQuanmoney()).doubleValue();
            if(quanMoney>master.getAlsoypay()){
                master.setAlsoypay(0.0);
            }else{
                master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),quanMoney));
            }
            orderMasterMapper.updateByPrimaryKeySelective(master);
            //生成支付记录
            payRecordsnew.setPaytype("9");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额 4-优惠券
            payRecordsnew.setPaytypedesc("9");//付详细方式：1：微信  2：支付宝  3：现金  4：银行卡 5：余额 6 :签单 7：三倍充值免单 8：霸王餐 9：其他（优惠券）
            payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
            payRecordsnew.setPayno(quanid+"");//存储优惠券id
            payRecordsnew.setPayamount(quannum);
            createPayInfo(payRecordsnew,customerid,orderid);
            //当抵扣完后就直接返回了
            if(master.getAlsoypay() <= 0){
                //订单完成把餐桌信息改成未占用
                master = updateMasterAndTables(master, tag);
                master.setOvertime(new Date());
                orderMasterMapper.updateByPrimaryKeySelective(master);
                createOrderdetailinfo(orderid);
                return new ResponseJson("支付成功",1);
            }
        }
        //现金，收钱吧（微信，支付宝），pos机
        payRecordsnew.setPayno(StringUtil.getUUid());
        if(paytype == 0){//现金
            payRecordsnew.setPayamount(payamount);
            payRecordsnew.setPaytype("3");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额

            //生成支付记录
            payRecordsnew.setPaytypedesc(/*paytype+*/"3");//付详细方式：1：微信  2：支付宝  3：现金  4：银行卡 5：余额 6 :签单 7：三倍充值免单 8：霸王餐 9：其他（优惠券）
            payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
            createPayInfo(payRecordsnew,customerid,orderid);
            //修改订单的未结算金额
            if(payamount>=master.getAlsoypay()){
                master.setAlsoypay(0.0);
            }else {
                master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),payamount));
            }
            orderMasterMapper.updateByPrimaryKeySelective(master);
        }else if(paytype == 1 || paytype == 2){//微信
            payRecordsnew.setPayamount(payamount);
            //支付
            Map map = pay(payRecordsnew.getPayno(),yuanTofen(payRecordsnew.getPayamount()),code);
            payRecordsnew.setPaytype(paytype+"");//付详细方式：1：微信  2：支付宝  3：现金  4：银行卡 5：余额 6 :签单 7：三倍充值免单 8：霸王餐 9：其他（优惠券）
            if(map.get("result_code").equals("200") && !map.get("pay_code").equals("PAY_FAIL")){//成功
                //结账完成
                if(map.get("order_status").equals("PAID")){
                    //生成支付记录
                    payRecordsnew.setReturnno(map.get("sn")+"");
                    payRecordsnew.setPaystatus(map.get("order_status")+"");
                    //生成支付记录
                    payRecordsnew.setPaytypedesc(paytype+"");//付详细方式：1：微信  2：支付宝  3：现金  4：银行卡 5：余额 6 :签单 7：三倍充值免单 8：霸王餐 9：其他（优惠券）
                    payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                    createPayInfo(payRecordsnew,customerid,orderid);
                    //修改订单的未结算金额
                    master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),payamount));
                    orderMasterMapper.updateByPrimaryKeySelective(master);
                }else {
                    throw new AjaxOperationFailException("支付失败！");
                }
            }else {
                throw new AjaxOperationFailException("支付失败！");
            }
        }else if(paytype == 7){//信用卡
            payRecordsnew.setPayamount(payamount);
            payRecordsnew.setPaytype("4");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额

            //生成支付记录
            payRecordsnew.setPaytypedesc("4");//付详细方式：1：微信  2：支付宝  3：现金  4：银行卡 5：余额 6 :签单 7：三倍充值免单 8：霸王餐 9：其他（优惠券）
            payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
            createPayInfo(payRecordsnew,customerid,orderid);
            //修改订单的未结算金额
            master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),payamount));
            orderMasterMapper.updateByPrimaryKeySelective(master);
        }

        if(master.getAlsoypay()<=0){
            //结账完成,修改开台状态
            master = updateMasterAndTables(master,tag);
            createOrderdetailinfo(orderid);
            resp.setData(1);
        }
        master.setOvertime(new Date());
        orderMasterMapper.updateByPrimaryKeySelective(master);
        resp.setMsg("支付成功！");
        return resp;
    }


    /**
     * 余额结账
     * @return
     */
    /*@Transactional
    public ResponseJson useAccount() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        Integer tag = HttpRequestParamter.getInt("tag",1);//1表示单台，2表示多台
        Integer djflag = HttpRequestParamter.getInt("djflag",0);//0表示不使用定金，1表示使用定金
        Integer paytype = HttpRequestParamter.getInt("paytype",0);//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
        Integer customerid = HttpRequestParamter.getInt("customerid",0);//客户id(有则为会员结账)
        Integer paytwo = HttpRequestParamter.getInt("paytwo",1);//1表示第一次支付。2表示第二次支付
        String code = HttpRequestParamter.getString("code",true);//支付码
        String passwd = HttpRequestParamter.getString("passwd",true);
        Double payamount = HttpRequestParamter.getDouble("payamount",0.00);//抵扣完（定金+优惠券）后的金额
        Double discountamount = HttpRequestParamter.getDouble("discountamount",0.00);
        Double discountamount1 = HttpRequestParamter.getDouble("discountamount1",0.00);
        Double discountratio = HttpRequestParamter.getDouble("discountratio",1.00);
        Integer quanid = HttpRequestParamter.getInt("quanid",0);
        Integer quannum = HttpRequestParamter.getInt("quannum",0);
        String isall = HttpRequestParamter.getString("isall",true,"0");
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        master.setOvertype("0");
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在，请联系管理员！");
        }
        Employees loginuser = employeeService.getLoginUser();
        if(discountamount1 > loginuser.getDiscountamount()){
            throw new AjaxOperationFailException("你的免单上限为！"+loginuser.getDiscountamount()+"元，如要优惠更多请联系管理员!");
        }
        if("1".equals(paytype) || "2".equals(paytype) || "3".equals(paytype) || "5".equals(paytype)){
            if(StringUtil.isEmpty(code)){
                throw new AjaxOperationFailException("支付码不能为空！");
            }
        }
        //生成支付记录
        PayRecords payRecordsnew = new PayRecords();
        if(paytwo == 1){
            master.setPayamount(master.getTotalmoney()-discountamount);
            master.setDiscountamount(discountamount);//优惠金额
            master.setIsall(isall);//是否整单
            master.setDiscountratio(discountratio);//优惠比例
        }
        //获取预结算信息
        Map resulmap = null;
        if(tag == 2 ){
            resulmap = (Map) orderDishesListAll().getData();
        }else {
            resulmap = (Map) orderDishesList().getData();
        }
        Map map0 = (Map) resulmap.get("bottom");
        master.setNums(Integer.valueOf(map0.get("nums")+""));//人数
        //消费总金额
        master.setTotalmoney(Double.valueOf(map0.get("setfeetotal")+"")+Double.valueOf(map0.get("top0total")+"")+Double.valueOf(map0.get("tablefee")+"")+Double.valueOf(map0.get("top1total")+""));

        master.setTablefee(Double.valueOf(map0.get("tablefee")+""));//餐台服务费
        master.setSeatsfee(Double.valueOf(map0.get("setfeetotal")+""));//免单金额
        ResponseJson resp = new ResponseJson();

        if(customerid!=0) {
            master.setCustomerid(customerid);
            if (paytype == 6) {
                if(StringUtil.isNull(passwd)){
                    throw new AjaxOperationFailException("支付密码不能为空！");
                }
                Customers cus = customersMapper.selectByPrimaryId(customerid);
                if(!cus.getPasswd().equals(StringUtil.md5(passwd))){
                    throw new AjaxOperationFailException("支付密码错误请重新输入！");
                }
                CustomersAccounts accounts = accountsMapper.selectByCusId(customerid);
                //余额
                double groupbalance = accounts.getGroupbalance();
                if (groupbalance > payamount) {
                    groupbalance = groupbalance - payamount;
                    payRecordsnew.setPayamount(payamount);//
                    accounts.setGroupbalance(groupbalance);
                    //订单完成把餐桌信息改成未占用
                    master = updateMasterAndTables(master, tag);
                    resp.setMsg("余额足够支付，支付成功!");
                    //发送短信通知
                    smsService.sendMessage(cus.getPhone(),"您好，"+cus.getName()+"，本次消费"+payamount+"元，现有余额"+groupbalance+"元。");
                    resp.setData(0);
                }else {
                    throw new AjaxOperationFailException("余额不足，可以先充值也可以换其他方式支付！");
                }
                //修改账户余额
                accountsMapper.updateByPrimaryId(accounts);
                //生成支付记录
                payRecordsnew.setPaytype("3");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
                payRecordsnew.setPaytypedesc("6");//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
                payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                createPayInfo(payRecordsnew, customerid, orderid);
                //客户账户信息日志
                CustomersLogs logs = new CustomersLogs();
                logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
                logs.setStoreid(employeeService.getLoginUser().getStoreid());
                logs.setCustomerid(customerid);
                logs.setYueamount(accounts.getGroupbalance());
                logs.setOpeation("消费");
                logs.setAmount(-payamount);//负数
                logs.setMemo("消费"+payamount+"元");
                logs.setIntime(new Date());
                logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
                logsMapper.insert1(logs);

            }
            if (djflag == 1) {//定金抵扣
                Map map = payRecordsMapper.selectByCusId(customerid);
                PayRecords records = payRecordsMapper.selectByPrimaryId(Integer.valueOf(map.get("payid") + ""));
                if(records == null){
                    throw new AjaxOperationFailException("订金信息不存在！");
                }
                records.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                records.setOverflag("0");//结算标识：0-已结算，1-未结算
                payRecordsMapper.updateByPrimaryId(records);
                //插入支付记录绑定
                OrderPays orderPays = new OrderPays();
                orderPays.setOktime(new Date());
                orderPays.setStatus("0");
                orderPays.setOrderid(orderid);
                orderPays.setPayid(records.getPayid());
                orderPaysMapper.insert1(orderPays);
            }
        }
        master.setCustomerid(customerid);
        //优惠券抵扣
        if(quanid!=0){
            Paytype quan = paytypeMapper.selectByPrimaryId(quanid);
            if(quan == null){
                throw new AjaxOperationFailException("使用优惠券不存在！");
            }
            if(quan.getStatus().equals("1")){
                throw new AjaxOperationFailException("优惠券活动已过！");
            }
            //消费金额小于最小用券金额
            if(quan.getMinmoney() > master.getTotalmoney()){
                throw new AjaxOperationFailException("消费金额不足无法使用优惠券！");
            }
            if(quannum==0){
                throw new AjaxOperationFailException("使用优惠券数量不能为空！");
            }else {
                //使用张数限制小于实际张数
                if(quan.getQuannum() < quannum){
                    throw new AjaxOperationFailException("此优惠券最多使用"+quan.getQuannum()+"张");
                }
            }
            //生成支付记录
            payRecordsnew.setPaytype("4");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额 4-优惠券
            payRecordsnew.setPaytypedesc("8");//支付详细方式： 0：现金 1：支付宝  2：微信 6：余额 7 :银行卡 8:优惠券
            payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
            payRecordsnew.setPayno(quanid+"");//存储优惠券id
            payRecordsnew.setPayamount(quannum);
            createPayInfo(payRecordsnew,customerid,orderid);
            //当抵扣完后就直接返回了
            if(payamount == 0){
                //订单完成把餐桌信息改成未占用
                master = updateMasterAndTables(master, tag);
                master.setOvertime(new Date());
                orderMasterMapper.updateByPrimaryId(master);
                createOrderdetailinfo(orderid);
                return new ResponseJson("支付成功");
            }
        }
        payRecordsnew.setPayno(StringUtil.getUUid());
        if(paytype == 0){//现金
            if(paytwo == 2){
                payRecordsnew.setPayamount(master.getAlsoypay());
            }else {
                payRecordsnew.setPayamount(payamount);
            }
            //结账完成
            master = updateMasterAndTables(master,tag);
            //生成支付记录
            payRecordsnew.setPaytype("0");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
            payRecordsnew.setPaytypedesc("0");//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
            createPayInfo(payRecordsnew,customerid,orderid);
        }else if(paytype == 1 || paytype == 2){//微信
            if(paytwo == 2){
                payRecordsnew.setPayamount(master.getAlsoypay());
            }else {
                payRecordsnew.setPayamount(payamount);
            }
            Map map = pay(payRecordsnew.getPayno(),yuanTofen(payRecordsnew.getPayamount()),code);
            if(paytype == 1){
                payRecordsnew.setPaytype("2");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
                payRecordsnew.setPaytypedesc("1");//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            }else {
                payRecordsnew.setPaytype("2");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
                payRecordsnew.setPaytypedesc("2");//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            }
            if(map.get("result_code").equals("200") && !map.get("pay_code").equals("PAY_FAIL")){//成功
                //结账完成
                if(map.get("order_status").equals("PAID")){

                    //生成支付记录
                    payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                    payRecordsnew.setReturnno(map.get("sn")+"");
                    payRecordsnew.setPaystatus(map.get("order_status")+"");
                    createPayInfo(payRecordsnew,customerid,orderid);
                    master = updateMasterAndTables(master,tag);
                }else {
                    throw new AjaxOperationFailException("支付失败！");
                }
            }else {
                throw new AjaxOperationFailException("支付失败！");
            }
        }else if(paytype == 7){//信用卡
            if(paytwo == 2){
                payRecordsnew.setPayamount(master.getAlsoypay());
            }else {
                payRecordsnew.setPayamount(payamount);
            }
            //结账完成
            master = updateMasterAndTables(master,tag);
            //生成支付记录
            payRecordsnew.setPaytype("1");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
            payRecordsnew.setPaytypedesc("7");//支付详细方式： 0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            payRecordsnew.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
            createPayInfo(payRecordsnew,customerid,orderid);
//            master = updateMasterAndTables(master,tag);
        }
        if(master.getAlsoypay()==0){
            //结账完成,修改开台状态
            master = updateMasterAndTables(master,tag);
        }
        master.setOvertime(new Date());
        orderMasterMapper.updateByPrimaryId(master);
        createOrderdetailinfo(orderid);
        return resp;
    }*/

    public Map pay(String orderid, int amount, String dynamicId){
        HttpProxy hp = new HttpProxy("https://api.shouqianba.com");
        Payinfo pageInfo = (Payinfo) redisSession.getValue("payInfo");
        String terminal_sn = pageInfo.getTerminalSn();
        String terminal_key = pageInfo.getTerminalKey();
        Map<String, Object> result = hp.pay(terminal_sn, terminal_key, orderid + "", String.valueOf(amount), "",dynamicId, "", "结账", "001");
        return result;
    }


    /**
     * 新增支付记录
     * @param payRecordsnew
     * @param customerid
     * @param orderid
     */
    @Transactional
    public void createPayInfo(PayRecords payRecordsnew,Integer customerid,Integer orderid){
        Employees employees = employeeService.getLoginUser();
        payRecordsnew.setCompanyid(employees.getCompanyid());
        payRecordsnew.setStoreid(employees.getStoreid());
        payRecordsnew.setCustomerid(customerid);
        payRecordsnew.setPaytime(new Date());
        payRecordsnew.setFlag("0");//标识：0-结账 1-充值 2-订金
        payRecordsnew.setEmployeeid(employees.getEmployeeid());
        payRecordsMapper.insert1(payRecordsnew);
        OrderPays orderPays = new OrderPays();
        orderPays.setPayid(payRecordsnew.getPayid());
        orderPays.setOktime(new Date());
        orderPays.setStatus("0");//状态：0-收款，1-退款
        orderPays.setOrderid(orderid);
        orderPays.setPayid(payRecordsnew.getPayid());
        orderPaysMapper.insert1(orderPays);//插入绑定表
    }


    public void createOrderdetailinfo(Integer orderid){
        orderMasterMapper.callsp_order_accounts_new(orderid);
    }

    /**
     * 根据订单id修改订单从表点菜和退菜为已结账
     * @param orderid
     */
    public void updateOrderSlaveStatus(Integer orderid){
        //修改点菜为已结账
        OrderSlave os = new OrderSlave();
        os.setOrderid(orderid);
        os.setStatus("2");
        orderSlaveMapper.updateByOrderId(os);
    }

    /**
     * 支付成功后修改订单状态，已经将餐桌设置空闲
     * @param master
     * @param tag
     */
    @Transactional
    public OrderMaster updateMasterAndTables(OrderMaster master,Integer tag){
        master.setStatus("3");//完全结账
        //查找是否有记录，用于反结
        OrderFlag2Records records = orderFlag2RecordsMapper.selectByOrderid(master.getOrderid());
        //标明是第一次结账
        if(records == null){
            if(master.getOvertype().equals("1")){//是否签单 结算状态：0-正常结算 1-签单  2-三倍充值免单 3-霸王餐  4-免单
                master.setStatus("4");
            }else {
                master.setStatus("3");
            }
            if(master.getFlag().equals("2")){//连台
                if(tag == 1){//只结算当前台(结算后，分离变为单台)
                    master.setIsmultiple(1);//是否是多台（1：单台2：多台）

                    updateLinkTable(master,tag);//修改其他连台信息

                    master.setFlag("0");//单台
                    master.setTableids(null);//关联餐台清空
                    Tables tb = tablesMapper.selectByPrimaryKey(master.getTableid());
                    tb.setFlag("0");//空闲
                    tablesMapper.updateByPrimaryKey(tb);
                    //修改点菜为已结账
                    updateOrderSlaveStatus(master.getOrderid());
                }else {//所有结算
                    master.setIsmultiple(2);//是否是多台（1：否 2：是）
                    String [] tableids = master.getTableids().split(",");
                    String otherorderids = "";
                    for (String id : tableids) {
                        Tables tb = tablesMapper.selectByPrimaryKey(Integer.valueOf(id));
                        tb.setFlag("0");//空闲
                        tablesMapper.updateByPrimaryKey(tb);
                        OrderMaster master1 = orderMasterMapper.selectMasterByTableId(Integer.valueOf(id));
                        if(master.getOvertype().equals("1")){//是否签单 结算状态：0-正常结算 1-签单  2-三倍充值免单 3-霸王餐  4-免单
                            master1.setStatus("4");
                        }else {
                            master1.setStatus("3");
                        }
                        otherorderids+=master1.getOrderid()+",";
//                        if(master.getOrderid() != master1.getOrderid()){
//                            //多台买单，支付金额计入当前买单台。其他台支付金额为0
//                            master1.setPayamount(0.00);
//                        }
//                        master1.setIsall(master.getIsall());
                        orderMasterMapper.updateByPrimaryKeySelective(master1);

                        ////修改点菜为已结账
                        updateOrderSlaveStatus(master1.getOrderid());
                    }
                    updateLinkTable(master,tag);//修改其他连台信息
                    //存下关联结账的orderid
                    OrderFlag2Records records1 = new OrderFlag2Records();
                    records1.setOrderid(master.getOrderid());
                    records1.setOtherorderid(otherorderids);
                    orderFlag2RecordsMapper.insert(records1);

                }
            }else if(master.getFlag().equals("0")){//独台统一结算
                master.setIsmultiple(1);
                Tables tb = tablesMapper.selectByPrimaryKey(master.getTableid());
                tb.setFlag("0");//空闲
                tablesMapper.updateByPrimaryKey(tb);
                //修改点菜为已结账
                updateOrderSlaveStatus(master.getOrderid());

            }else {//合台
                master.setIsmultiple(1);
                String [] tableids = master.getTableids().split(",");
                for (String id : tableids) {
                    Tables tb = tablesMapper.selectByPrimaryKey(Integer.valueOf(id));
                    tb.setFlag("0");//空闲
                    tablesMapper.updateByPrimaryKey(tb);
                }
                //修改点菜为已结账
                updateOrderSlaveStatus(master.getOrderid());
            }
            orderMasterMapper.updateByPrimaryKeySelective(master);
            //打印
            printAllDishes(master,"3");
        }
        return master;
    }


    /**
     * 第一次结账修改连台的其他台的关系
     * @param master
     */
    public void updateLinkTable(OrderMaster master,Integer tag){
        if(master.getTableids()==null || "".equals(master.getTableids().trim())){
            return;
        }
        String [] ids = master.getTableids().split(",");//获取连台的餐桌id数组

        if(tag==1){//单台结账
            List<String> list = new ArrayList<>();
            List<Integer> listid = orderMasterMapper.selectByorderidGuanlian(master.getOrderid());
            for (String id : ids) {
                list.add(id);
            }
            if(ids.length == 2){//2台连台情况下，结账一台，另一台自动变独台
                Integer anotherid = listid.get(0);
                OrderMaster anotherorder = orderMasterMapper.selectByPrimaryId(anotherid);
                anotherorder.setTableids(null);
                anotherorder.setFlag("0");
                orderMasterMapper.updateByPrimaryKeySelective(anotherorder);
            }
            if(ids.length > 2){//多台连台，结账一台，另外的继续是连台
                String idsnew = new String();
                for(int i = 0 ;i < list.size();i++) {
                    if (master.getTableid() == Integer.valueOf(list.get(i)+"")){
                        list.remove(i);//(删除掉原来的id)
                    }
                }
                for (int i = 0 ;i < list.size();i++) {//重新排列
                    if (i<list.size()-1) {
                        idsnew+=list.get(i)+",";
                    }else {
                        idsnew+=list.get(i);
                    }
                }
                for (Integer id : listid){
                    OrderMaster order = orderMasterMapper.selectByPrimaryId(id);
                    order.setTableids(idsnew);
                    orderMasterMapper.updateByPrimaryKeySelective(order);
                }
            }
            master.setFlag("0");
            master.setTableids(null);
            orderMasterMapper.updateByPrimaryKeySelective(master);
        }else{//联台结账
//            for (String id : ids) {
            for(int i=0;i<ids.length;i++){
                String id = ids[i];
                OrderMaster master1 = orderMasterMapper.selectMasterByTableId(Integer.valueOf(id));
                if(master1==null) continue;
                if(master.getOrderid() != master1.getOrderid()){
                    //多台买单，支付金额计入当前买单台(在结算的时候已经修改)。其他台支付金额为0
//                    if(master.getPayamount()==master.getAlsoypay()){//未付过款
//                        master.setPayamount(NumberUtil.add(master.getPayamount(),master1.getPayamount()));
//                        master.setAlsoypay(NumberUtil.add(master.getAlsoypay(),master1.getAlsoypay()));
//                        master.setTotalmoney(NumberUtil.add(master.getTotalmoney(),master1.getTotalmoney()));
//                    }
                    master1.setPayamount(0.00);
                    master1.setAlsoypay(0.00);
                }
                master1.setIsall(master.getIsall());
                orderMasterMapper.updateByPrimaryKeySelective(master1);
            }
//            orderMasterMapper.updateByPrimaryId(master);
        }

    }


    /**
     * 转菜
     * @return
     */
    @Transactional
    public ResponseJson dishesChangeOrder() throws AjaxOperationFailException {
        String idstr = HttpRequestParamter.getString("idstr",true);
        Integer oldorderid = HttpRequestParamter.getInt("oldorderid",0);
        Integer neworderid = HttpRequestParamter.getInt("neworderid",0);
        if(StringUtil.isEmpty(idstr)){
            throw new AjaxOperationFailException("菜id缺失");
        }
        if(oldorderid == 0){
            throw new AjaxOperationFailException("当前订单id缺失");
        }
        if(neworderid == 0){
            throw new AjaxOperationFailException("需要转到的餐桌订单id缺失");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(oldorderid);
        OrderMaster master1 = orderMasterMapper.selectByPrimaryId(neworderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在");
        }
        if(master1 == null){
            throw new AjaxOperationFailException("需要转到的餐台订单信息不存在");
        }
        String [] array = idstr.split(",");
        for (String id : array) {
            OrderSlave slave = orderSlaveMapper.selectByPrimaryId(Integer.valueOf(id));
            slave.setOrderid(neworderid);
            //旧订单把点菜金额减下来
            master.setAmountmoney(master.getAmountmoney()-(slave.getPrice()*slave.getAmount()));
            //新订单把点菜金额加上去
            master1.setAmountmoney(master1.getAmountmoney()+(slave.getPrice()*slave.getAmount()));
            orderSlaveMapper.updateByPrimaryId(slave);
        }
        orderMasterMapper.updateByPrimaryId(master);
        orderMasterMapper.updateByPrimaryId(master1);
        ResponseJson resp = new ResponseJson();
        resp.setMsg("转菜成功！");
        return resp;
    }


    @Transactional
    public int yuanTofen(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        price = Double.valueOf(df.format(price));
        int money = (int) (price * 100);
        return money;
    }


    /**
     * 1-签单 2-三倍充值免单 3-霸王餐  4-免单
     * @return
     */
    @Transactional
    public ResponseJson useAccountOvertype() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        Integer tag = HttpRequestParamter.getInt("tag",1);//1表示单台，2表示多台（2只针对连台）
        String overtype = HttpRequestParamter.getString("overtype",true);
        Integer customerid = HttpRequestParamter.getInt("customerid",0);//客户id(有则为会员结账)
        Double payamount = HttpRequestParamter.getDouble("payamount",0.00);//抵扣完定金后的金额
        Double discountamount = HttpRequestParamter.getDouble("discountamount",0.00);
        Double discountratio = HttpRequestParamter.getDouble("discountratio",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在！");
        }
        if(StringUtil.isEmpty(overtype)){
            throw new AjaxOperationFailException("支付状态不能为空！");
        }

//        getAndUpdateOrderMasterByDicount(master.getDiscountratio(),master.getIsall(),2,master);
        //最终结算数据填写进来（本来是在结账浏览接口填写，现改过来此处填写）
//        Map resulmap = null;
//        if(tag == 2 ){
//            resulmap = (Map) orderDishesListAll().getData();
//        }else {
//            resulmap = (Map) orderDishesList().getData();
//        }
//        Map map = (Map) resulmap.get("bottom");
//        master.setNums(Integer.valueOf(map.get("nums")+""));
//        master.setTotalmoney(Double.valueOf(map.get("setfeetotal")+"")+Double.valueOf(map.get("top0total")+"")+Double.valueOf(map.get("tablefee")+"")+Double.valueOf(map.get("top1total")+""));
//        master.setTablefee(Double.valueOf(map.get("tablefee")+""));
//        master.setSeatsfee(Double.valueOf(map.get("setfeetotal")+""));
        //获取人数
        Map param = new HashMap();
        if(tag==2){
            param.put("orderids",getLianTaiOrderId(master));
        }else {
            param.put("orderids",master.getOrderid());
        }
        int nums = orderSlaveMapper.selectTotalPersonByOrderid(param);
        master.setNums(nums);
        master.setOvertype(overtype);
        master.setOvertime(new Date());
        master.setSeatsfee(discountamount);

//        master.setDiscountamount(NumberUtil.sub(master.getTotalmoney(),master.getPayamount(),master.getSeatsfee()).doubleValue());
//        master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),discountamount));
//        master.setPayamount(NumberUtil.sub(master.getPayamount(),discountamount));

        if(overtype.equals("1")){//签单
            if(payamount == 0){
                throw new AjaxOperationFailException("应付金额不能为空！");
            }
            master.setDiscountamount(NumberUtil.add(master.getDiscountamount(),discountamount));
            master.setDiscountratio(discountratio);
            master.setCustomerid(customerid);//签单用户
            master.setPayamount(NumberUtil.sub(master.getPayamount(),discountamount));
            master.setEmployee1id(employeeService.getLoginUser().getEmployeeid());
            //释放餐桌占用
            master = updateMasterAndTables(master,tag);
            master.setStatus("4");
        }
        if(overtype.equals("2")){//三倍充值免单
            if(customerid == 0){
                throw new AjaxOperationFailException("会员id缺失！");
            }
            master.setCustomerid(customerid);//签单用户
            master.setEmployee1id(employeeService.getLoginUser().getEmployeeid());
            //释放餐桌占用
            master = updateMasterAndTables(master,tag);
        }
        if(overtype.equals("3") || overtype.equals("4")){
            //释放餐桌占用
            master.setEmployee1id(employeeService.getLoginUser().getEmployeeid());
            master = updateMasterAndTables(master,tag);
        }
        orderMasterMapper.updateByPrimaryKeySelective(master);
        createOrderdetailinfo(orderid);
        return new ResponseJson();
    }

    @Transactional
    public ResponseJson useAccountOvertype2() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        String overtype = HttpRequestParamter.getString("overtype",true);
        Integer customerid = HttpRequestParamter.getInt("customerid",0);//客户id(有则为会员结账)
        Double payamount = HttpRequestParamter.getDouble("payamount",0.00);//抵扣完定金后的金额
        Double discountamount = HttpRequestParamter.getDouble("discountamount",0.00);
        Double discountratio = HttpRequestParamter.getDouble("discountratio",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在！");
        }
        if(StringUtil.isEmpty(overtype)){
            throw new AjaxOperationFailException("支付状态不能为空！");
        }

        master.setOvertype(overtype);
        master.setOvertime(new Date());
        if(overtype.equals("1")){//签单
            if(payamount == 0){
                throw new AjaxOperationFailException("应付金额不能为空！");
            }
            master.setPayamount(payamount);
            master.setDiscountamount(discountamount);
            master.setDiscountratio(discountratio);
            master.setCustomerid(customerid);//签单用户
            master.setEmployee1id(employeeService.getLoginUser().getEmployeeid());
            master.setStatus("4");
        }
        if(overtype.equals("2")){//三倍充值免单
            if(customerid == 0){
                throw new AjaxOperationFailException("会员id缺失！");
            }
            master.setCustomerid(customerid);//签单用户
        }
        if(overtype.equals("3") || overtype.equals("4")){
        }
        orderMasterMapper.updateByPrimaryId(master);
        createOrderdetailinfo(orderid);
        return new ResponseJson();
    }


    /**
     * 消费信息查询
     * @return
     */
    public ResponseJson selectOrderInfoList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String startdate = HttpRequestParamter.getString("startdate",true);
        String enddate = HttpRequestParamter.getString("enddate",true);
        String overtype = HttpRequestParamter.getString("overtype",true);
        String code = HttpRequestParamter.getString("code",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        params.put("employee1id",employees.getEmployeeid());//操作人
        if(StringUtil.isEmpty(startdate) && StringUtil.isEmpty(enddate) ){//两个时间都为空，默认为当天时间
            params.put("startdate",DateUtil.format(new Date(),"yyyy-MM-dd")+" 00:00:00");
            params.put("enddate",DateUtil.format(new Date(),"yyyy-MM-dd")+" 23:59:59");
        }
        if(!StringUtil.isEmpty(startdate)){//开始时间
            params.put("startdate",startdate);
        }
        if(!StringUtil.isEmpty(enddate)){//结束时间
            params.put("enddate",enddate);
        }

        if(!StringUtil.isEmpty(overtype)){
            params.put("overtype",overtype);
        }
        if(!StringUtil.isEmpty(code)){
            params.put("code",code);
        }
        PageHelper.startPage(page,size,"orderid desc");
        List<Map> list = orderMasterMapper.selectOrderInfoList(params);
        for (Map map : list) {
            if(!(map.get("flag").equals("0"))){
                String[] idarray = map.get("tableids").toString().split(",");
                String tablesstr = "";
                for (int i=0;i<idarray.length;i++){
                    Tables tables = tablesMapper.selectByPrimaryKey(Integer.valueOf(idarray[i]));
                    if(i<idarray.length-1){
                        tablesstr+=tables.getCode()+",";
                    }else {
                        tablesstr+=tables.getCode();
                    }
                }
                map.put("tables",tablesstr);
            }else {
                map.put("tables","");
            }
        }
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 消费明细查询
     * @return
     */
    public ResponseJson orderInfoDetail() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在！");
        }
        PageHelper.startPage(1,1000,"id desc");
        List<Map> list = orderMasterMapper.selectOrderSlaveInfoList(orderid);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 支付明细查询
     * @return
     */
    public ResponseJson orderPayInfoDetail() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在！");
        }
        PageHelper.startPage(1,1000,"payid desc");
        List<Map> list = orderMasterMapper.orderPayInfoDetail(orderid);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 退菜
     * @return
     */
    @Transactional
    public ResponseJson dishesDel() throws AjaxOperationFailException {
        String ids = HttpRequestParamter.getString("ids",true);
        Integer count = HttpRequestParamter.getInt("count",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(StringUtil.isEmpty(ids)){
            throw new AjaxOperationFailException("id缺失！");
        }
        if(count == 0){
            throw new AjaxOperationFailException("退菜数量必填！");
        }
        OrderMaster master = null;
        String [] idarray = ids.split(",");
        List<OrderSlave> orderSlaves = new ArrayList<>();
        for(String id : idarray){
            OrderSlave slave = orderSlaveMapper.selectByPrimaryId(Integer.valueOf(id));
            if(slave.getFlag().equals("2")){
                throw new AjaxOperationFailException("此菜已退，不能再退！");
            }
            master = orderMasterMapper.selectByPrimaryId(slave.getOrderid());
            if("1".equals(master.getStatus())){
                throw new AjaxOperationFailException("订单正在结账中，请结完后在处理！");
            }
            Integer tcount = orderSlaveMapper.selectByOrderidAndDishesid(slave.getOrderid(),slave.getDisheid());
            if(tcount > 0 ){
                if(tcount < count){
                    throw new AjaxOperationFailException("退菜数量大于点菜数量，退菜失败！");
                }
            }else {
                throw new AjaxOperationFailException("此菜已全部退，不能再退！");
            }
//            master.setAmountmoney(master.getAmountmoney()-(count*slave.getPrice()));
            Double amountMoney = NumberUtil.mul(count.doubleValue(),slave.getPrice());
            master.setAmountmoney(NumberUtil.sub(master.getAmountmoney(),amountMoney));//点菜总金额
            master.setTotalmoney(NumberUtil.sub(master.getTotalmoney(),slave.getRealamount()));//消费总金额减去
            //重新计算，先用付款金额出去
            Double dis = master.getDiscountratio();
            Double realamount = amountMoney;
            if(dis!=0){
                realamount = NumberUtil.mul(realamount,dis);
            }
            master.setPayamount(NumberUtil.sub(master.getPayamount(),realamount));//应付金额
            master.setAlsoypay(NumberUtil.sub(master.getAlsoypay(),realamount));//未付款减去
            OrderSlave slavenew = slave;
            slavenew.setFlag("2");
            slavenew.setAmount(-count);
            slavenew.setMemo(memo);
            slavenew.setRealamount(-realamount);

            orderSlaves.add(slavenew);//添加到list用作保存打印信息

            int i = orderSlaveMapper.insert1(slavenew);//保存新的记录
            Dishes dishe = dishesMapper.selectByPrimaryKey(slave.getDisheid());
            if(dishe.getIsGoods().equals("1")){
                if(dishe.getIsAuto().equals("1")){//自动销售
                    Map param = new HashMap();
                    param.put("companyid",employeeService.getLoginUser().getCompanyid());
                    param.put("storeid",employeeService.getLoginUser().getStoreid());
                    param.put("code",dishe.getCode());
                    Goods goods = goodsMapper.selectBycode(param);
                    param.put("goodsid",goods.getGoodsid());
                    GoodsStock goodsStock = goodsStockMapper.selectByGoodsid(param);
                    goodsStock.setStock(goodsStock.getStock()+slave.getAmount());
                    goodsStock.setLasttime(new Date());
                    goodsStockMapper.updateByPrimaryKey(goodsStock);
                }
            }
            orderMasterMapper.updateByPrimaryId(master);
        }
        //添加打印信息
        addPrintMessage(master,orderSlaves);
        ResponseJson resp = new ResponseJson();
        resp.setMsg("删除成功！");
        return resp;
    }

    /**
     * 签单支付
     * @return
     */
    @Transactional
    public ResponseJson signPay() throws AjaxOperationFailException {
        double payamount = HttpRequestParamter.getDouble("payamount",0.00);
        String code = HttpRequestParamter.getString("code",true);
        String memo = HttpRequestParamter.getString("memo",true);
        Integer paytype = HttpRequestParamter.getInt("paytype",0);//支付详细方式： //0：现金 1：支付宝  2：微信  6：余额 7 :银行卡）
        String orderidstr = HttpRequestParamter.getString("orderidstr",true);
        if(StringUtil.isEmpty(orderidstr)){
            throw new AjaxOperationFailException("请选择订单！");
        }
        if(payamount == 0.00){
            throw new AjaxOperationFailException("支付金额不能为空！");
        }
        Integer customerid = getCusIdByOrderSign( orderidstr.split(","));
        PayRecords records = new PayRecords();
        records.setPayno(StringUtil.getUUid());
        records.setCompanyid(employeeService.getLoginUser().getCompanyid());
        records.setStoreid(employeeService.getLoginUser().getStoreid());
        records.setCustomerid(customerid);
        records.setMemo(memo);
        records.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
        records.setPayamount(payamount);//
        records.setPaytime(new Date());
        records.setFlag("0");
        records.setOverflag("0");
        if(paytype == 0 ){//现金
            records.setPaytype("0");
            records.setPaytypedesc("0");
            //支付完成修改订单状态
            createSignPay(orderidstr.split(","),records);
        }
        if(paytype == 1 || paytype == 2){//1支付宝，2微信
            if(StringUtil.isEmpty(code)){
                throw new AjaxOperationFailException("支付码不能为空！");
            }
            Map map = pay(records.getPayno(),yuanTofen(records.getPayamount()),code);
            if(paytype == 1){
                records.setPaytype("2");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
                records.setPaytypedesc("2");//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            }else {
                records.setPaytype("2");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
                records.setPaytypedesc("1");//支付详细方式： //0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            }
            if(map.get("result_code").equals("200")){//成功
                //结账完成
                if(map.get("order_status").equals("PAID")){
                    //生成支付记录
                    records.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
                    records.setReturnno(map.get("sn")+"");
                    records.setPaystatus(map.get("order_status")+"");
                    //支付完成修改订单状态
                    createSignPay(orderidstr.split(","),records);
                }else {
                    throw new AjaxOperationFailException("支付失败！");
                }
            }else {
                throw new AjaxOperationFailException("支付失败！");
            }
        }
        if(paytype == 6){//余额
            records.setStatus("1");
            records.setPaytype("3");
            records.setPaytypedesc("6");
            CustomersAccounts accounts = accountsMapper.selectByCusId(customerid);
            //余额
            double groupbalance = accounts.getGroupbalance();
            if (groupbalance > payamount) {//余额足够支付
                groupbalance = groupbalance - payamount;
                accounts.setGroupbalance(groupbalance);
                accountsMapper.updateByPrimaryKey(accounts);
                //支付完成修改订单状态
                createSignPay(orderidstr.split(","),records);
            }else {
                throw new AjaxOperationFailException("余额不足支付，请先充值！");
            }
        }
        if(paytype == 7){
            //生成支付记录
            records.setPaytype("1");//支付方式：0-现金 1-pos机  2-收钱吧 3-余额
            records.setPaytypedesc("7");//支付详细方式： 0：现金 1：支付宝  2：微信 6：余额 7 :银行卡
            records.setStatus("1");//状态：0-录入，1-支付成功，2-支付失败，3-退款成功，4-退款失败
            //支付完成修改订单状态
            createSignPay(orderidstr.split(","),records);
        }
        ResponseJson resp = new ResponseJson();
        resp.setMsg("支付成功！");
        return  resp;
    }



    @Transactional
    public void createSignPay(String [] ids,PayRecords records){
        Integer payid = payRecordsMapper.insert1(records);
        for (String id : ids) {
            OrderMaster master = orderMasterMapper.selectByPrimaryId(Integer.valueOf(id));
            master.setStatus("5");//签单已经支付
            orderMasterMapper.updateByPrimaryId(master);
            //新增支付绑定记录
            OrderPays pays = new OrderPays();
            pays.setOrderid(Integer.valueOf(id));
            pays.setPayid(payid);
            pays.setOktime(new Date());
            pays.setStatus("0");//状态：0-收款，1-退款
            orderPaysMapper.insert1(pays);
        }
    }



    public Integer getCusIdByOrderSign(String [] ids) throws AjaxOperationFailException {
        Integer customerid = 0;
        for (int i =0;i<ids.length;i++) {
            OrderMaster master = orderMasterMapper.selectByPrimaryId(Integer.valueOf(ids[i]));
            if(i==0){
                customerid=master.getCustomerid();
            }
            if(customerid!=master.getCustomerid()){
                throw new AjaxOperationFailException("所选择签单信息不属于同一个人的签单信息，结账失败！");
            }
            if(master.getStatus().equals("5")){
                throw new AjaxOperationFailException("所结账订单包含已经结账的，结账失败，请重新勾选！");
            }
        }
        return customerid;
    }

    /**
     * 反结第一步（退款，返回消费明细）
     * @return
     */
    public ResponseJson fanjieFirst() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        List<OrderSlave> list = new ArrayList<>();
        Map map = new HashMap();
        map.put("nums",master.getNums());
        if(master.getFlag().equals("2")){
            //查找是否有记录，用于反结
            OrderFlag2Records records = orderFlag2RecordsMapper.selectByOrderid(master.getOrderid());
            if(records == null){
                throw new AjaxOperationFailException("没有连台支付记录");
            }
            String [] ids = records.getOtherorderid().split(",");
            for (String id : ids){
//                List<Map> dishes = orderSlaveMapper.selectByOrderid(Integer.valueOf(id));
                OrderSlave os = new OrderSlave();
                os.setOrderid(Integer.valueOf(id));
                List<OrderSlave> dishes = orderSlaveMapper.selectOrderSlaveByOrderId(os);
                list.addAll(dishes);
            }
            map.put("list",list);
        }else {
//            List<Map> dishes = orderSlaveMapper.selectByOrderid(orderid);
            OrderSlave os = new OrderSlave();
            os.setOrderid(orderid);
            List<OrderSlave> dishes = orderSlaveMapper.selectOrderSlaveByOrderId(os);
            map.put("list",dishes);
        }
        return new ResponseJson(map);
    }



    public ResponseJson fanjieSecond() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        ResponseJson resp = new ResponseJson();
        String ordertime = DateUtil.format(master.getOrdertime(), "yyyy-MM-dd");
        if(!ordertime.equals(DateUtil.format(new Date(), "yyyy-MM-dd"))){
            throw new AjaxOperationFailException("当前结账已过可以反结期限！反结只针对当日订单！反结失败！");
        }
        //查询消费记录（把以前的接口数据返回去）
        int count = orderFlag2RecordsMapper.selectCountByOrderid(orderid);
        if(count == 1){
            resp.setData(orderDishesListAll().getData());
        }else {
            resp.setData(orderDishesList().getData());
        }
        return resp;
    }


    /**
     * 退款
     * @return
     */
    @Transactional
    public ResponseJson fanjieThird() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        if(master == null){
            throw new AjaxOperationFailException("未查询到订单");
        }
        if("0".equals(master.getStatus()) || "1".equals(master.getStatus()) ){
            throw new AjaxOperationFailException("订单正在进行中，不能退款！");
        }
        if(master.getStatus().equals("6")){//反结已经退款
            throw  new AjaxOperationFailException("已经退款，请勿重复操作");
        }
        if(!"3".equals(master.getStatus())) {//正常结算，才能退款，
            throw  new AjaxOperationFailException("订单未支付完，不能退款");
        }

        ResponseJson resp = new ResponseJson();
        String ordertime = DateUtil.format(master.getOrdertime(), "yyyy-MM-dd");
        if(!ordertime.equals(DateUtil.format(new Date(), "yyyy-MM-dd"))){
            throw new AjaxOperationFailException("当前结账已过可以反结期限！反结只针对当日订单！反结失败！");
        }
        String msg = "";
        Double alsoypay = Math.abs(master.getAlsoypay());//退款金额,前面点菜和退菜时已经计算出来了(取绝对值)
        ////签单已支付应该是只有一笔支付，余额，支付宝，微信，银行卡（退款重新结算）(未支付则直接返回菜单重新结算)
        if("3".equals(master.getStatus())){//正常结算，才能退款，
            //退款顺序，先移动，后现金
            BPayRecords bPayRecords = new BPayRecords();
            bPayRecords.setOrderid(orderid);
            List<PayRecords> prList = payRecordsMapper.findSQBRecordsByPayType(bPayRecords);
            for(PayRecords pr : prList){
                Double payPayamount = pr.getPayamount();
                if(payPayamount==0)continue;
                if(alsoypay>payPayamount){//退款金额，大于当前支付记录支付金额
                    alsoypay = NumberUtil.sub(alsoypay,payPayamount);
                }else{
                    pr.setPayamount(alsoypay);
                    alsoypay = 0.0;
                }

                //微信
                if("1".equals(pr.getPaytypedesc())){
                    pr = customersService.Refund(pr);
                    msg+="微信"+pr.getPayamount()+"元退款成功!";
                }
                //支付宝
                if("2".equals(pr.getPaytypedesc())){
                    pr = customersService.Refund(pr);
                    msg+="支付宝"+pr.getPayamount()+"元退款成功!";
                }
                pr.setPayamount(NumberUtil.sub(payPayamount,alsoypay));
                payRecordsMapper.updateByPrimaryKeySelective(pr);//修改支付记录
                if(alsoypay==0)break;
            }
            //其他退款
            prList = payRecordsMapper.findOtherRecordsByPayType(bPayRecords);
            for(PayRecords pr : prList){
                if(alsoypay==0)break;
                Double payPayamount = pr.getPayamount();
                if(payPayamount==0)continue;
                if(alsoypay>payPayamount){//退款金额，大于当前支付记录支付金额
                    alsoypay = NumberUtil.sub(alsoypay,payPayamount);
                }else{
                    pr.setPayamount(alsoypay);
                    alsoypay = 0.0;
                }
                //结账
                if(pr.getFlag().equals("0")){
                    if("3".equals(pr.getPaytypedesc())){//现金
                        pr.setStatus("3");
                        pr.setReturntime(new Date());
                        pr.setReturnamount(pr.getPayamount());
                        msg+="现金"+pr.getPayamount()+"元手动退款!";
                    }
                    if("5".equals(pr.getPaytypedesc())){//余额
                        pr.setStatus("3");
                        pr.setReturntime(new Date());
                        pr.setReturnamount(pr.getPayamount());
                        CustomersAccounts accounts =  accountsMapper.selectByCusId(master.getCustomerid());
                        //余额加回账户
                        accounts.setGroupbalance(accounts.getGroupbalance()+pr.getPayamount());
                        accountsMapper.updateByPrimaryKey(accounts);
                        msg+="余额"+pr.getPayamount()+"元退款成功!";
                        //发送短信*********************88
                        //客户账户信息日志
                        //记录日志
                        CustomersLogs logs = new CustomersLogs();
                        logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
                        logs.setStoreid(employeeService.getLoginUser().getStoreid());
                        logs.setCustomerid(accounts.getCustomerid());
                        logs.setYueamount(accounts.getGroupbalance());
                        logs.setOpeation("退款");
                        logs.setAmount(pr.getPayamount());
                        logs.setMemo("反结退款");
                        logs.setIntime(new Date());
                        logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
                        logsMapper.insert(logs);
                    }
                    if(pr.getPaytypedesc().equals("4")){//银行卡
                    }
                }
                if(pr.getFlag().equals("2")){//订金
                    pr.setOverflag("1");
                    OrderPays pays = new OrderPays();
                    pays.setOrderid(orderid);
                    pays.setPayid(pr.getPayid());
                    pays = orderPaysMapper.selectOne(pays);
                    orderPaysMapper.deleteByPrimaryId(pays.getId());
                    msg+="订金已退"+pr.getPayamount()+"元，可以下次使用!";
                }
                pr.setPayamount(NumberUtil.sub(payPayamount,alsoypay));
                payRecordsMapper.updateByPrimaryKeySelective(pr);//修改支付记录
                if(alsoypay==0)break;
            }
            updateOrderSlaveStatus(master.getOrderid());
        }
        resp.setMsg(msg);
//        master.setStatus("6");
        orderMasterMapper.updateByPrimaryKeySelective(master);
        return resp;
    }

    /**
     * 调整点菜
     * @return
     * @throws AjaxOperationFailException
     */
    @Transactional
    public ResponseJson changeDishesAmount() throws AjaxOperationFailException {
        Integer id = HttpRequestParamter.getInt("id",0);
        Integer amount = HttpRequestParamter.getInt("amount",0);
        if(id == 0){
            throw new AjaxOperationFailException("缺少id");
        }
        OrderSlave slave = orderSlaveMapper.selectByPrimaryId(id);
        if(slave == null){
            throw new AjaxOperationFailException("点菜不存在");
        }
        Integer oldcount = slave.getAmount();
        if(oldcount == amount){
            return  new ResponseJson("修改成功");
        }
        Dishes dishe = dishesMapper.selectByPrimaryKey(slave.getDisheid());
        if(dishe == null){
            throw new AjaxOperationFailException("菜品不存在");
        }
        orderSlaveMapper.updateByPrimaryId(slave);
        if(oldcount > amount){
            OrderSlave slave1 = slave;
            slave1.setFlag("2");
            slave1.setAmount(oldcount-amount);
            orderSlaveMapper.insert1(slave1);
        }
        if(dishe.getIsGoods().equals("1")){
            if(dishe.getIsAuto().equals("1")){
                Map param = new HashMap();
                param.put("companyid",employeeService.getLoginUser().getCompanyid());
                param.put("storeid",employeeService.getLoginUser().getStoreid());
                param.put("code",dishe.getCode());
                Goods goods = goodsMapper.selectBycode(param);
                param.put("goodsid",goods.getGoodsid());
                GoodsStock goodsStock = goodsStockMapper.selectByGoodsid(param);
                if(oldcount > amount){
                    goodsStock.setStock(goodsStock.getStock()+(oldcount-amount));
                }
                if(oldcount < amount){
                    goodsStock.setStock(goodsStock.getStock()-(amount-oldcount));
                }
                goodsStock.setLasttime(new Date());
                goodsStockMapper.updateByPrimaryKey(goodsStock);
            }
        }
        return  new ResponseJson("调整成功");
    }


    @Transactional
    public ResponseJson useAccount2() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        Integer djflag = HttpRequestParamter.getInt("djflag",0);
        Integer paytype = HttpRequestParamter.getInt("paytype",0);
        Integer customerid = HttpRequestParamter.getInt("customerid",0);
        String code = HttpRequestParamter.getString("code",true);
        Double payamount = HttpRequestParamter.getDouble("payamount",0.00);
        Double discountamount = HttpRequestParamter.getDouble("discountamount",0.00);
        Double discountratio = HttpRequestParamter.getDouble("discountratio",0.00);
        Double discountamount1 = HttpRequestParamter.getDouble("discountamount1",0.00);
        String isall = HttpRequestParamter.getString("isall",true,"0");

        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失！");
        }
        Employees loginuser = employeeService.getLoginUser();
        if(discountamount1 > loginuser.getDiscountamount()){
            throw new AjaxOperationFailException("你的免单上限为！"+loginuser.getDiscountamount()+"元，如要优惠更多请联系管理员!");
        }
        if("1".equals(paytype) || "2".equals(paytype) || "3".equals(paytype) || "5".equals(paytype)){
            if(StringUtil.isEmpty(code)){
                throw new AjaxOperationFailException("支付码不能为空！");
            }
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
        master.setOvertype("0");
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在，请联系管理员！");
        }
        master.setPayamount(master.getTotalmoney()-discountamount);
        master.setDiscountamount(discountamount);
        master.setDiscountratio(discountratio);
        master.setIsall(isall);
        ResponseJson resp = new ResponseJson();
        PayRecords payRecordsnew = new PayRecords();
        payRecordsnew.setPayno(StringUtil.getUUid());
        if(customerid!=0) {
            master.setCustomerid(customerid);
            if (djflag == 1) {
                Map map = payRecordsMapper.selectByCusId(customerid);
                PayRecords records = payRecordsMapper.selectByPrimaryId(Integer.valueOf(map.get("payid") + ""));
                if(records == null){
                    throw new AjaxOperationFailException("订金信息不存在！");
                }
                records.setStatus("1");
                records.setOverflag("0");
                payRecordsMapper.updateByPrimaryId(records);
                OrderPays orderPays = new OrderPays();
                orderPays.setOktime(new Date());
                orderPays.setStatus("0");
                orderPays.setOrderid(orderid);
                orderPays.setPayid(records.getPayid());
                orderPaysMapper.insert1(orderPays);
            }
            if (paytype == 6) {
                CustomersAccounts accounts = accountsMapper.selectByCusId(customerid);
                double groupbalance = accounts.getGroupbalance();
                if (groupbalance > payamount) {
                    groupbalance = groupbalance - payamount;
                    payRecordsnew.setPayamount(payamount);
                    accounts.setGroupbalance(groupbalance);
                    resp.setMsg("余额足够支付，支付成功!");
                    resp.setData(0);
                }
                if (groupbalance == 0) {
                    throw new AjaxOperationFailException("余额为0，支付失败");
                }
                if (groupbalance < payamount) {
                    throw new AjaxOperationFailException("余额不足，请先充值后支付！");
                }
                accountsMapper.updateByPrimaryKey(accounts);
                payRecordsnew.setPaytype("3");
                payRecordsnew.setPaytypedesc("6");
                payRecordsnew.setStatus("1");
                createPayInfo(payRecordsnew, customerid, orderid);
                CustomersLogs logs = new CustomersLogs();
                logs.setCompanyid(employeeService.getLoginUser().getCompanyid());
                logs.setStoreid(employeeService.getLoginUser().getStoreid());
                logs.setCustomerid(customerid);
                logs.setYueamount(accounts.getGroupbalance());
                logs.setOpeation("消费");
                logs.setAmount(-payamount);
                logs.setMemo("消费"+payamount+"元");
                logs.setIntime(new Date());
                logs.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
                logsMapper.insert(logs);
            }
        }
        master.setCustomerid(customerid);
        if(paytype == 0){
            payRecordsnew.setPayamount(payamount);
            payRecordsnew.setPaytype("0");
            payRecordsnew.setPaytypedesc("0");
            payRecordsnew.setStatus("1");
            createPayInfo(payRecordsnew,customerid,orderid);
        }
        if(paytype == 1 || paytype == 2){
            payRecordsnew.setPayamount(payamount);
            Map map = pay(payRecordsnew.getPayno(),yuanTofen(payRecordsnew.getPayamount()),code);
            if(paytype == 1){
                payRecordsnew.setPaytype("2");
                payRecordsnew.setPaytypedesc("2");
            }else {
                payRecordsnew.setPaytype("2");
                payRecordsnew.setPaytypedesc("1");
            }
            if(map.get("result_code").equals("200") && !map.get("pay_code").equals("PAY_FAIL")){
                if(map.get("order_status").equals("PAID")){
                    payRecordsnew.setStatus("1");
                    payRecordsnew.setReturnno(map.get("sn")+"");
                    payRecordsnew.setPaystatus(map.get("order_status")+"");
                    createPayInfo(payRecordsnew,customerid,orderid);
                }else {
                    throw new AjaxOperationFailException("支付失败！");
                }
            }else {
                throw new AjaxOperationFailException("支付失败！");
            }
        }
        if(paytype == 7){
            payRecordsnew.setPayamount(payamount);
            payRecordsnew.setPaytype("1");
            payRecordsnew.setPaytypedesc("7");
            payRecordsnew.setStatus("1");
            createPayInfo(payRecordsnew,customerid,orderid);
        }
        master.setOvertime(new Date());
        master.setStatus("3");
        orderMasterMapper.updateByPrimaryId(master);
        createOrderdetailinfo(orderid);
        return resp;
    }


    public ResponseJson selectdishesBypinyin() {
        String pinyin = HttpRequestParamter.getString("pinyin",true);
        if(StringUtil.isEmpty(pinyin)){
            return new ResponseJson();
        }
        Map param = new HashMap();
        param.put("companyid",employeeService.getLoginUser().getCompanyid());
        param.put("storeid",employeeService.getLoginUser().getStoreid());
        param.put("pinyin",pinyin);
        List<Map> list = dishesMapper.selectdishesBypinyin(param);
        return new ResponseJson(list);
    }

    /**
     * 补打印
     * @return
     */
    public ResponseJson printDishes() throws AjaxOperationFailException {
        String ids = HttpRequestParamter.getString("ids",true);
        String memo = HttpRequestParamter.getString("memo",true);
        if(StringUtil.isEmpty(ids)){
            throw new AjaxOperationFailException("id缺失！");
        }
        String [] idarray = ids.split(",");
        OrderSlave slave = orderSlaveMapper.selectByPrimaryId(Integer.valueOf(idarray[0]));
        OrderMaster master = orderMasterMapper.selectByPrimaryId(slave.getOrderid());
        //添加打印信息
        OrderPrintMaster orderPrintMaster = new OrderPrintMaster();
        orderPrintMaster.setCompanyid(master.getCompanyid());
        orderPrintMaster.setStoreid(master.getStoreid());
        orderPrintMaster.setOrderid(master.getOrderid());
        orderPrintMaster.setCustomerid(master.getCustomerid());
        orderPrintMaster.setCode(tablesMapper.selectByPrimaryKey(master.getTableid()).getName());
        orderPrintMaster.setOrdertime(new Date());
        orderPrintMaster.setNums(master.getNums());
        orderPrintMaster.setEmployeeid(master.getEmployeeid());
        orderPrintMaster.setFlag(master.getFlag());
        orderPrintMaster.setSendtype(master.getSendtype());
        orderPrintMaster.setMemo(memo);
        orderPrintMaster.setStatus("0");
        orderPrintMaster.setType("1");
        int i =  printMasterMapper.insert(orderPrintMaster);
        OrderPrintSlave orderPrintSlave;
        for (String id: idarray) {
            OrderSlave orderSlaveInfo = orderSlaveMapper.selectByPrimaryId(Integer.valueOf(id));
            orderPrintSlave = new OrderPrintSlave();
            orderPrintSlave.setOrderid(orderPrintMaster.getPrintid());
            orderPrintSlave.setTablesid(master.getTableid());
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
            printSlaveMapper.insert(orderPrintSlave);
        }
        return new ResponseJson("打印成功");
    }

    public ResponseJson fanjie() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失");
        }
        //查询消费记录（把以前的接口数据返回去）
//        int count = orderFlag2RecordsMapper.selectCountByOrderid(orderid);
//        if(count == 1){
//            String otherorderidstr = orderFlag2RecordsMapper.selectByOrderid1(orderid);
//            for (String orderid1 : otherorderidstr.split(",")) {
//                OrderMaster master = orderMasterMapper.selectByPrimaryId(Integer.valueOf(orderid1));
//                master.setStatus("2");
//                orderMasterMapper.updateByPrimaryId(master);
//            }
//        }else {
            OrderMaster master = orderMasterMapper.selectByPrimaryId(orderid);
            master.setStatus("2");
            orderMasterMapper.updateByPrimaryId(master);
//        }

        return new ResponseJson("状态已经更改");
    }

    /**
     * 菜单打印
     * @return
     */
    public ResponseJson printAllDishes(OrderMaster master,String type) {
        //添加打印信息
        OrderPrintMaster orderPrintMaster = new OrderPrintMaster();
        orderPrintMaster.setCompanyid(master.getCompanyid());
        orderPrintMaster.setStoreid(master.getStoreid());
        orderPrintMaster.setOrderid(master.getOrderid());
        orderPrintMaster.setCustomerid(master.getCustomerid());
        Tables tb = tablesMapper.selectByPrimaryKey(master.getTableid());
        orderPrintMaster.setCode(tb.getCode());
        orderPrintMaster.setOrdertime(new Date());
        orderPrintMaster.setNums(master.getNums());
        orderPrintMaster.setEmployeeid(master.getEmployeeid());
        orderPrintMaster.setFlag(master.getFlag());
        orderPrintMaster.setSendtype(master.getSendtype());
        orderPrintMaster.setMemo("");
        orderPrintMaster.setStatus("0");
        orderPrintMaster.setType(type);
        printMaster.insert(orderPrintMaster);
        return new ResponseJson("打印成功");
    }
}
