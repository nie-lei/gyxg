package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class OrderProvider {

    public String selectAlltablesByPrams(Map params){
        StringBuffer sql = new StringBuffer("SELECT tableid,minmoney,`code`,`name`,seats,case when flag='2' then '1' else '0' end as state from gyxg_tables where status='0' ");//0开放
        if(params.containsKey("areaid")){
            sql.append(" and areaid = "+params.get("areaid"));
        }
        sql.append(" and companyid="+params.get("companyid")+" and storeid="+params.get("storeid"));
        return sql.toString();
    }


    public String selectOrderInfoList(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.orderid,\n" +
                "\ta.ordertime,\n" +
                "\tb.`code`,\n" +
                "\tb.`name`,\n" +
                "\ta.flag,\n" +
                "\ta.tableids,\n" +
                "\ta.nums,\n" +
                "\ta.seatsfee,\n" +
                "\ta.tablefee,\n" +
                "\ta.amountmoney,\n" +
                "\ta.totalmoney," +
                " a.payamount,\n" +
                "\ta.discountratio,\n" +
                "\ta.discountamount,\n" +
                "\ta.overtime,\n" +
                "\ta.overtype,\n" +
                "\ta.`status`,\n" +
                "\ta.employeeid,\n" +
                "\t(SELECT c.name from gyxg_employees c where c.employeeid=a.employeeid) as empname,\n" +
                "\ta.employee1id,\n" +
                "\t(SELECT d.name from gyxg_employees d where d.employeeid=a.employee1id) as emp1name\n" +
                "FROM\n" +
                "\tgyxg_orders_master a\n" +
                "LEFT JOIN gyxg_tables b ON a.tableid = b.tableid\n" +
                "WHERE\n" +
                "\ta.`status` <> '0'  ");
//        if(params.containsKey("startdate")){
//            sql.append(" and a.overtime between '"+params.get("startdate")+"' and '"+params.get("enddate")+"'");
//        }
        if(params.containsKey("startdate")){//大于开始时间
            sql.append(" and a.overtime > '"+params.get("startdate"));
        }
        if(params.containsKey("enddate")){//小于结束时间
            sql.append(" and a.overtime <"+params.get("enddate")+"'");
        }
        if(params.containsKey("overtype")){
            sql.append(" and a.overtype='"+params.get("overtype")+"'");
        }
        if(params.containsKey("employee1id")){//筛选当前登录人
            sql.append(" and a.employee1id ="+params.get("employee1id"));
        }
        if(params.containsKey("code")){
            sql.append(" and b.code like '%"+params.get("code")+"%'");
        }

        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }
}
