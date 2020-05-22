package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Paytype;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.ReportMapper;
import com.jzkj.gyxg.util.DateUtil;
import com.jzkj.gyxg.util.MessageUtil;
import com.jzkj.gyxg.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 退菜明细
     * @return
     */
    public ResponseJson tuicaimingxi() throws AjaxOperationFailException {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Integer classifyid = HttpRequestParamter.getInt("classifyid");
        String dishname = HttpRequestParamter.getString("dishname",true);

        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        if(classifyid == 0){
            params.put("classifyid",null);
        }else {
            params.put("classifyid",classifyid);
        }
        params.put("dishname",dishname);
        List<Map> list = reportMapper.tuicaimingxi(params);
        Set<String> classfyset = new HashSet<String>();
        //分类
        for (Map map: list) {
            classfyset.add(map.get("classifyname").toString());
        }
        double dishenamecountall = 0;//统计（占位dishename）
        double amountall = 0;//amount
        double totalmoneyall = 0;//totalmoney
        List resultlist = new ArrayList();
        Map statisticsall = new HashMap();//合计
        Iterator<String>  it = classfyset.iterator();
        while (it.hasNext()){
            Map classfynamemap = new HashMap();//分类
            Map statistics = new HashMap();//小计
            double dishenamecount = 0;//统计（占位dishename）
            double amount = 0;//amount
            double totalmoney = 0;//totalmoney
            String classifyname = it.next();
            //菜分类
            classfynamemap.put("code","菜品分类:"+classifyname);
            classfynamemap.put("flag","1");//需要合并
            resultlist.add(classfynamemap);
            for (Map map : list) {
                if(classifyname.equals(map.get("classifyname"))){
                    resultlist.add(map);
                    dishenamecount+=1;
                    amount+=Double.valueOf(map.get("amount")+"");
                    totalmoney+=Double.valueOf(map.get("totalmoney")+"");
                }
            }
            statistics.put("code","小计");
            statistics.put("dishename",sq(dishenamecount));
            statistics.put("amount",sq(amount));
            statistics.put("totalmoney",sq(totalmoney));
            statistics.put("color","1");//前端方便改颜色
            dishenamecountall+=dishenamecount;
            amountall+=amount;
            totalmoneyall+=totalmoney;
            resultlist.add(statistics);
        }
        statisticsall.put("dishename",sq(dishenamecountall));
        statisticsall.put("amount",sq(amountall));
        statisticsall.put("totalmoney",sq(totalmoneyall));
        statisticsall.put("code","合计");//前端方便改颜色
        statisticsall.put("color","1");
        resultlist.add(statisticsall);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(resultlist);
        return responseJson;
    }


    /**
     * 赠菜明细
     * @return
     */
    public ResponseJson zengcaimingxi() throws AjaxOperationFailException {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Integer classifyid = HttpRequestParamter.getInt("classifyid");
        String dishname = HttpRequestParamter.getString("dishname",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        if(classifyid == 0){
            params.put("classifyid",null);
        }else {
            params.put("classifyid",classifyid);
        }
        params.put("dishname",dishname);
        List<Map> list = reportMapper.zengcaimingxi(params);
        Set<String> classfyset = new HashSet<String>();
        //分类
        for (Map map: list) {
            classfyset.add(map.get("classifyname").toString());
        }
        double dishenamecountall = 0;//统计（占位dishename）
        double amountall = 0;//amount
        double totalmoneyall = 0;//totalmoney
        List resultlist = new ArrayList();
        Map statisticsall = new HashMap();//合计
        Iterator<String>  it = classfyset.iterator();
        while (it.hasNext()){
            Map classfynamemap = new HashMap();//分类
            Map statistics = new HashMap();//小计
            double dishenamecount = 0;//统计（占位dishename）
            double amount = 0;//amount
            double totalmoney = 0;//totalmoney
            String classifyname = it.next();
            //菜分类
            classfynamemap.put("code","菜品分类:"+classifyname);
            classfynamemap.put("flag","1");//需要合并
            resultlist.add(classfynamemap);
            for (Map map : list) {
                if(classifyname.equals(map.get("classifyname"))){
                    resultlist.add(map);
                    dishenamecount+=1;
                    amount+=Double.valueOf(map.get("amount")+"");
                    totalmoney+=Double.valueOf(map.get("totalmoney")+"");
                }
            }
            statistics.put("code","小计");
            statistics.put("dishename",sq(dishenamecount));
            statistics.put("amount",sq(amount));
            statistics.put("totalmoney",sq(totalmoney));
            statistics.put("color","1");//前端方便改颜色
            dishenamecountall+=dishenamecount;
            amountall+=amount;
            totalmoneyall+=totalmoney;
            resultlist.add(statistics);
        }
        statisticsall.put("dishename",sq(dishenamecountall));
        statisticsall.put("amount",sq(amountall));
        statisticsall.put("totalmoney",sq(totalmoneyall));
        statisticsall.put("code","合计");//前端方便改颜色
        statisticsall.put("color","1");
        resultlist.add(statisticsall);
        return new ResponseJson(resultlist);
    }


    /**
     * 菜品销售统计
     */
    public ResponseJson caipinxiaoshoutongji() throws AjaxOperationFailException {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Integer classifyid = HttpRequestParamter.getInt("classifyid");
        String dishname = HttpRequestParamter.getString("dishname",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        if(classifyid == 0){
            params.put("classifyid",null);
        }else {
            params.put("classifyid",classifyid);
        }
        params.put("dishname",dishname);
        List<Map> list = reportMapper.caipinxiaoshoutongji(params);
        Set<String> classfyset = new HashSet<String>();
        //分类
        for (Map map: list) {
            classfyset.add(map.get("classifyname").toString());
        }
        int  dishenamecountall = 0;
        double diancai_amountall = 0.00;
        double diancai_totalamountall = 0.00;
        double tuicai_amountall = 0.00;
        double tuicai_totalamountall = 0.00;
        double dishenumall = 0.00;
        double saleamountall = 0.00;
        double zengcai_amountall = 0.00;
        double zengcai_totalamountall = 0.00;
        double discount_amountall = 0.00;
        double realsaleamountall = 0.00;
        List resultlist = new ArrayList();
        Map statisticsall = new HashMap();//合计
        Iterator<String>  it = classfyset.iterator();
        while (it.hasNext()){
            Map classfynamemap = new HashMap();//分类
            Map statistics = new HashMap();//小计
            int dishenamecount = 0;//统计（占位dishename）
            double diancai_amount = 0.00;
            double diancai_totalamount = 0.00;
            double tuicai_amount = 0.00;
            double tuicai_totalamount = 0.00;
            double dishenum = 0.00;
            double saleamount = 0.00;
            double zengcai_amount = 0.00;
            double zengcai_totalamount = 0.00;
            double discount_amount = 0.00;
            double realsaleamount = 0.00;
            String classifyname = it.next();
            //菜分类
            classfynamemap.put("code","菜品分类:"+classifyname);
            classfynamemap.put("flag","1");//需要合并
            resultlist.add(classfynamemap);
            for (Map map : list) {
                if(classifyname.equals(map.get("classifyname"))){
                    resultlist.add(map);
                    dishenamecount+=1;
                    diancai_amount+=Double.valueOf(map.get("diancai_amount")+"");
                    diancai_totalamount+=Double.valueOf(map.get("diancai_totalamount")+"");
                    tuicai_amount+=Double.valueOf(map.get("tuicai_amount")+"");
                    tuicai_totalamount+=Double.valueOf(map.get("tuicai_totalamount")+"");
                    dishenum+=Double.valueOf(map.get("dishenum")+"");
                    saleamount+=Double.valueOf(map.get("saleamount")+"");
                    zengcai_amount+=Double.valueOf(map.get("zengcai_amount")+"");
                    zengcai_totalamount+=Double.valueOf(map.get("zengcai_totalamount")+"");
                    discount_amount+=Double.valueOf(map.get("discount_amount")+"");
                    realsaleamount+=Double.valueOf(map.get("realsaleamount")+"");
                }
            }
            statistics.put("diancai_amount",sq(diancai_amount));
            statistics.put("diancai_totalamount",sq(diancai_totalamount));
            statistics.put("tuicai_amount",sq(tuicai_amount));
            statistics.put("tuicai_totalamount", sq(tuicai_totalamount) );
            statistics.put("dishenum",sq(dishenum));
            statistics.put("saleamount",sq(saleamount));
            statistics.put("zengcai_amount",sq(zengcai_amount));
            statistics.put("zengcai_totalamount",sq(zengcai_totalamount));
            statistics.put("discount_amount",sq(discount_amount));
            statistics.put("realsaleamount",sq(realsaleamount));
            statistics.put("code","小计");
            statistics.put("name",dishenamecount);
            statistics.put("color","1");//前端方便改颜色
            dishenamecountall+=dishenamecount;
            diancai_amountall+=diancai_amount;
            diancai_totalamountall+=diancai_totalamount;
            tuicai_amountall+=tuicai_amount;
            tuicai_totalamountall+=tuicai_totalamount;
            dishenumall+=dishenum;
            saleamountall+=saleamount;
            zengcai_amountall+=zengcai_amount;
            zengcai_totalamountall+=zengcai_totalamount;
            discount_amountall+=discount_amount;
            realsaleamountall+=realsaleamount;
            resultlist.add(statistics);
        }
        statisticsall.put("diancai_amount",sq(diancai_amountall));
        statisticsall.put("diancai_totalamount",sq(diancai_totalamountall));
        statisticsall.put("tuicai_amount",sq(tuicai_amountall));
        statisticsall.put("tuicai_totalamount",sq(tuicai_totalamountall));
        statisticsall.put("dishenum",sq(dishenumall));
        statisticsall.put("saleamount",sq(saleamountall));
        statisticsall.put("zengcai_amount",sq(zengcai_amountall));
        statisticsall.put("zengcai_totalamount",sq(zengcai_totalamountall));
        statisticsall.put("discount_amount",sq(discount_amountall));
        statisticsall.put("realsaleamount",sq(realsaleamountall));
        statisticsall.put("code","合计:");
        statisticsall.put("name",dishenamecountall);
        statisticsall.put("color","1");
        resultlist.add(statisticsall);
        return new ResponseJson(resultlist);
    }


    /**
     * 酒水单
     * @return
     */
    public ResponseJson jiushuidan() {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        List<Map> list = reportMapper.jiushuidan(params);
        Set<String> classfyset = new HashSet<String>();
        //分类
        for (Map map: list) {
            classfyset.add(map.get("classifyname").toString());
        }
        double dishenamecountall = 0;//统计（占位dishename）
        double amountall = 0;//amount
        double totalamountall = 0;//totalmoney
        List resultlist = new ArrayList();
        Map statisticsall = new HashMap();//合计
        Iterator<String>  it = classfyset.iterator();
        while (it.hasNext()){
            Map classfynamemap = new HashMap();//分类
            Map statistics = new HashMap();//小计
            double dishenamecount = 0;//统计（占位dishename）
            double amount = 0;//amount
            double totalamount = 0;//totalmoney
            String classifyname = it.next();
            //菜分类
            classfynamemap.put("code","菜品分类:"+classifyname);
            classfynamemap.put("flag","1");//需要合并
            resultlist.add(classfynamemap);
            for (Map map : list) {
                if(classifyname.equals(map.get("classifyname"))){
                    resultlist.add(map);
                    dishenamecount+=1;
                    amount+=Double.valueOf(map.get("amount")+"");
                    totalamount+=Double.valueOf(map.get("totalamount")+"");
                }
            }
            statistics.put("code","小计");
            statistics.put("name",sq(dishenamecount));
            statistics.put("amount",sq(amount));
            statistics.put("totalamount",sq(totalamount));
            statistics.put("color","1");//前端方便改颜色
            dishenamecountall+=dishenamecount;
            amountall+=amount;
            totalamountall+=totalamount;
            resultlist.add(statistics);
        }
        statisticsall.put("name",sq(dishenamecountall));
        statisticsall.put("amount",sq(amountall));
        statisticsall.put("totalamount",sq(totalamountall));
        statisticsall.put("code","合计");//前端方便改颜色
        statisticsall.put("color","1");
        resultlist.add(statisticsall);
        return new ResponseJson(resultlist);
    }




    public double sq(double num){
        return new BigDecimal(num).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 签单月份汇总报表
     * @return
     */
    public ResponseJson qiandan() {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        List<Map> list45 = reportMapper.qiandan45(params);//所有的
        List<Map> list5 = reportMapper.qiandan5(params);//已经结算的
        int count = 0;
        double payamounttotal = 0.00;
        double payamount = 0.00;
        for(Map map: list45){
            count+=1;
            payamounttotal+=Double.valueOf(map.get("payamounttotal")+"");
            for (Map map1 : list5){
                if(map.get("customerid") == map1.get("customerid")){
                    map.put("payamount",map1.get("payamounttotal"));
                    payamount+=Double.valueOf(map1.get("payamounttotal")+"");
                    map.put("qiankuan",sq(Double.valueOf(map.get("payamounttotal")+"")-Double.valueOf(map1.get("payamounttotal")+"")));
                }
            }
        }
        Map lastmap = new HashMap();
        lastmap.put("name","合计："+count);
        lastmap.put("payamounttotal",payamounttotal);
        lastmap.put("payamount",payamount);
        lastmap.put("color","1");
        lastmap.put("qiankuan",sq(payamounttotal-payamount));
        list45.add(lastmap);
        return new ResponseJson(list45);
    }

    /**
     * 账单付款明细查询
     * @return
     */
    public ResponseJson zhangdanfukuanmingxi() {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        List<Map> list = reportMapper.zhangdanfukuanmingxi(params);//所有的
        int tablenameall = 0;
        double numsall = 0.00;
        double totalmoneyall = 0.00;
        double discountamountall = 0.00;
        double freeamountall = 0.00;
        double payamountall = 0.00;
        double rmbamountall = 0.00;
        double bankamountall = 0.00;
        double zhifubaoamountall = 0.00;
        double weixinamountall = 0.00;
        double youhuiquanjineall = 0.00;
        double youhuiquannumall = 0.00;
        double tuangouyouhuiall = 0.00;
        double qiandanamountall = 0.00;
        double yueamountall = 0.00;
        double sanbeiamountall = 0.00;
        double bawangamountall = 0.00;
        double miandanamountall = 0.00;
        for (Map map : list){
            tablenameall+=1;
            numsall+=  map.containsKey("nums") ? Double.valueOf(map.get("nums")+"") : 0.00;
            totalmoneyall+=  map.containsKey("totalmoney") ? Double.valueOf(map.get("totalmoney")+"") : 0.00;
            discountamountall+=  map.containsKey("discountamount") ? Double.valueOf(map.get("discountamount")+"") : 0.00;
            freeamountall+=  map.containsKey("freeamount") ? Double.valueOf(map.get("freeamount")+"") : 0.00;
            payamountall+=  map.containsKey("payamount") ? Double.valueOf(map.get("payamount")+"") : 0.00 ;
            rmbamountall+=  map.containsKey("rmbamount") ? Double.valueOf(map.get("rmbamount")+"") : 0.00 ;
            bankamountall+=  map.containsKey("bankamount") ? Double.valueOf(map.get("bankamount")+"") : 0.00 ;
            zhifubaoamountall+=  map.containsKey("zhifubaoamount") ? Double.valueOf(map.get("zhifubaoamount")+"") : 0.00 ;
            weixinamountall+=  map.containsKey("weixinamount") ? Double.valueOf(map.get("weixinamount")+"") : 0.00 ;
            youhuiquanjineall+=  (map.containsKey("youhuiquannum") ? Double.valueOf(map.get("youhuiquannum")+"") :0.00 )*(map.containsKey("youhuiquanjine") ? Double.valueOf(map.get("youhuiquanjine")+"") :0.00);
            youhuiquannumall+=  map.containsKey("youhuiquannum") ? Double.valueOf(map.get("youhuiquannum")+"") :0.00 ;
            tuangouyouhuiall+=  map.containsKey("tuangouyouhui") ? Double.valueOf(map.get("tuangouyouhui")+"") : 0.00 ;
            qiandanamountall+=  map.containsKey("qiandanamount") ? Double.valueOf(map.get("qiandanamount")+"") : 0.00 ;
            yueamountall+=  map.containsKey("yueamount") ? Double.valueOf(map.get("yueamount")+"") : 0.00 ;
            sanbeiamountall+=  map.containsKey("sanbeiamount") ? Double.valueOf(map.get("sanbeiamount")+"") : 0.00 ;
            bawangamountall+=  map.containsKey("bawangamount") ? Double.valueOf(map.get("bawangamount")+"") : 0.00 ;
            miandanamountall+=  map.containsKey("miandanamount") ? Double.valueOf(map.get("miandanamount")+"") : 0.00 ;
        }
        Map lastmap = new HashMap();
        lastmap.put("orderid","合计：");
        lastmap.put("tablename",tablenameall);
        lastmap.put("nums",sq(numsall));
        lastmap.put("totalmoney",sq(totalmoneyall));
        lastmap.put("discountamount",sq(discountamountall));
        lastmap.put("freeamount",sq(freeamountall));
        lastmap.put("payamount",sq(payamountall));
        lastmap.put("rmbamount",sq(rmbamountall));
        lastmap.put("bankamount",sq(bankamountall));
        lastmap.put("zhifubaoamount",sq(zhifubaoamountall));
        lastmap.put("weixinamount",sq(weixinamountall));
        lastmap.put("youhuiquanjine",sq(youhuiquanjineall));
        lastmap.put("youhuiquannum",sq(youhuiquannumall));
        lastmap.put("youhuiquan",sq(youhuiquanjineall)+"("+youhuiquannumall+"张)");
        lastmap.put("tuangouyouhui",sq(tuangouyouhuiall));
        lastmap.put("qiandanamount",sq(qiandanamountall));
        lastmap.put("yueamount",sq(yueamountall));
        lastmap.put("sanbeiamount",sq(sanbeiamountall));
        lastmap.put("bawangamount",sq(bawangamountall));
        lastmap.put("miandanamount",sq(miandanamountall));
        lastmap.put("color","1");
        list.add(lastmap);
        return new ResponseJson(list);
    }


    /**
     * 结算方式汇总
     * @return
     */
    public ResponseJson jiesuanfangshihuizongchaxun() {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        List<Map> list = reportMapper.jiesuanfangshihuizongchaxun(params);//所有的
        int count = 0;
        double totalmoneyall = 0.00;
        double discountamountall = 0.00;
        double freeamountall = 0.00;
        double payamountall = 0.00;
        double rmbamountall = 0.00;
        double bankamountall = 0.00;
        double zhifubaoamountall = 0.00;
        double weixinamountall = 0.00;
        double youhuiquanjineall = 0.00;
        double tuangouyouhuiall = 0.00;
        double qiandanamountall = 0.00;
        double yueamountall = 0.00;
        double sanbeiamountall = 0.00;
        double bawangamountall = 0.00;
        double miandanamountall = 0.00;
        double incomeall = 0.00;
        double freeincomeall = 0.00;
        for (Map map : list){
            count+=1;
            totalmoneyall+=  map.containsKey("totalmoney") ? Double.valueOf(map.get("totalmoney")+"") : 0.00;
            discountamountall+=  map.containsKey("discountamount") ? Double.valueOf(map.get("discountamount")+"") : 0.00;
            freeamountall+=  map.containsKey("freeamount") ? Double.valueOf(map.get("freeamount")+"") : 0.00;
            payamountall+=  map.containsKey("payamount") ? Double.valueOf(map.get("payamount")+"") : 0.00 ;
            rmbamountall+=  map.containsKey("rmbamount") ? Double.valueOf(map.get("rmbamount")+"") : 0.00 ;
            bankamountall+=  map.containsKey("bankamount") ? Double.valueOf(map.get("bankamount")+"") : 0.00 ;
            zhifubaoamountall+=  map.containsKey("zhifubaoamount") ? Double.valueOf(map.get("zhifubaoamount")+"") : 0.00 ;
            weixinamountall+=  map.containsKey("weixinamount") ? Double.valueOf(map.get("weixinamount")+"") : 0.00 ;
            youhuiquanjineall+=  map.containsKey("youhuiquanjine") ? Double.valueOf(map.get("youhuiquanjine")+"") : 0.00;
            tuangouyouhuiall+=  map.containsKey("tuangouyouhui") ? Double.valueOf(map.get("tuangouyouhui")+"") : 0.00 ;
            qiandanamountall+=  map.containsKey("qiandanamount") ? Double.valueOf(map.get("qiandanamount")+"") : 0.00 ;
            yueamountall+=  map.containsKey("yueamount") ? Double.valueOf(map.get("yueamount")+"") : 0.00 ;
            sanbeiamountall+=  map.containsKey("sanbeiamount") ? Double.valueOf(map.get("sanbeiamount")+"") : 0.00 ;
            bawangamountall+=  map.containsKey("bawangamount") ? Double.valueOf(map.get("bawangamount")+"") : 0.00 ;
            miandanamountall+=  map.containsKey("miandanamount") ? Double.valueOf(map.get("miandanamount")+"") : 0.00 ;
            Date overtime = (Date)DateUtil.toDate(map.get("overtime")+"");
            map.put("overtime",DateUtil.format(overtime,"yyyy-MM-dd")+"("+DateUtil.getWeekName(overtime)+")");
            incomeall+=  map.containsKey("income") ? Double.valueOf(map.get("income")+"") : 0.00 ;
            freeincomeall+=  map.containsKey("freeincome") ? Double.valueOf(map.get("freeincome")+"") : 0.00 ;
        }
        Map lastmap = new HashMap();
        lastmap.put("overtime","合计："+count);
        lastmap.put("tablename",count);
        lastmap.put("totalmoney",sq(totalmoneyall));
        lastmap.put("discountamount",sq(discountamountall));
        lastmap.put("freeamount",sq(freeamountall));
        lastmap.put("payamount",sq(payamountall));
        lastmap.put("rmbamount",sq(rmbamountall));
        lastmap.put("bankamount",sq(bankamountall));
        lastmap.put("zhifubaoamount",sq(zhifubaoamountall));
        lastmap.put("weixinamount",sq(weixinamountall));
        lastmap.put("youhuiquanjine",sq(youhuiquanjineall));
        lastmap.put("tuangouyouhui",sq(tuangouyouhuiall));
        lastmap.put("qiandanamount",sq(qiandanamountall));
        lastmap.put("yueamount",sq(yueamountall));
        lastmap.put("sanbeiamount",sq(sanbeiamountall));
        lastmap.put("bawangamount",sq(bawangamountall));
        lastmap.put("miandanamount",sq(miandanamountall));
        lastmap.put("income",sq(incomeall));
        lastmap.put("freeincome",sq(freeincomeall));
        lastmap.put("color","1");
        list.add(lastmap);
        return new ResponseJson(list);

    }

    /**
     * 营业汇总
     * @return
     */
    public ResponseJson yingyehuizong() {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        List<Map> list = reportMapper.yingyehuizong(params);//所有的
        for (Map map : list){
            Date overtime = (Date)DateUtil.toDate(map.get("overtime")+"");
            map.put("overtime",DateUtil.format(overtime,"yyyy-MM-dd")+"("+DateUtil.getWeekName(overtime)+")");
        }
        Map totalmap = reportMapper.yingyehuizongtotal(params);//所有的
        if(totalmap != null){
            totalmap.put("color","1");//前端判断展示位红色
            totalmap.put("overtime","合计：");//前端判断展示位红色
            list.add(totalmap);
        }
        return new ResponseJson(list);
    }

    /**
     * 优惠卡往来账目报表
     * @return
     */
    public ResponseJson kaorderdetail() {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        String phone = HttpRequestParamter.getString("phone",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(begin_date) &&  !StringUtil.isNull(end_date)){
            params.put("begin_date",begin_date);
            params.put("end_date",end_date);
        }else {
            params.put("begin_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            params.put("end_date",DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        if(!StringUtil.isNull(phone)){
            params.put("phone",phone);
        }
        List<Map> list = reportMapper.kaorderdetail(params);
        double total = 0.00;
        int count = 0;
        for (Map map : list){
            count+=1;
            total+=Double.valueOf(map.get("amount")+"");
        }
        Map lastmap = new HashMap();
        lastmap.put("color","1");
        lastmap.put("customerid","合计：");
        lastmap.put("name",count);
        lastmap.put("amount",sq(total));
        list.add(lastmap);
        return new ResponseJson(list);
    }

    public ResponseJson invoicing() throws AjaxOperationFailException {
        String begin_date = HttpRequestParamter.getString("begin_date",true);
        String end_date = HttpRequestParamter.getString("end_date",true);
        Integer warehouseid = HttpRequestParamter.getInt("warehouseid", 0);

        if (StringUtil.isNull(begin_date) || StringUtil.isNull(end_date)) {
            throw new AjaxOperationFailException("请选择时间段");
        }
        if (warehouseid == 0) {
            warehouseid = null;
        }

        Employees employees = employeeService.getLoginUser();
        int companyid = employees.getCompanyid();
        int storeid = employees.getStoreid();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("companyid", companyid);
        map.put("storeid", storeid);
        map.put("warehouseid", warehouseid);
        map.put("begin_date", begin_date);
        map.put("end_date", end_date);
        List<Map<String, Object>> list = reportMapper.invoicing(map);

        return new ResponseJson(list);

    }
}
