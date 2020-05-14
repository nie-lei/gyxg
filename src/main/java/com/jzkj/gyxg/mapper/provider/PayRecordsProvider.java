package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class PayRecordsProvider {

    public String selectDingjinList(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.payid,\n" +
                "\tb.`name`,\n" +
                "\tb.phone,\n" +
                "\ta.paytypedesc,\n" +
                "\ta.payamount,\n" +
                "\ta.payno,\n" +
                "\ta.paytime,\n" +
                "\ta.overflag,\n" +
                "\t(SELECT c.`name` from gyxg_employees c where c.employeeid=a.employeeid) as empname,a.memo\n" +
                "FROM\n" +
                "\tgyxg_payrecords a\n" +
                "LEFT JOIN gyxg_customers b ON a.customerid = b.customerid\n" +
                "WHERE\n" +
                "\ta.flag = '2' ");
        if(params.containsKey("startdate")){
            sql.append(" and a.paytime between '"+params.get("startdate")+"' and '"+params.get("enddate")+"'");
        }
        if(params.containsKey("overflag")){
            sql.append(" and a.overflag='"+params.get("overflag")+"'");
        }
        if(params.containsKey("nameorphone")){
            sql.append(" and (b.name like '%"+params.get("nameorphone")+"%' or b.phone like '%"+params.get("nameorphone")+"%')");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }
}
