package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class ReportProvider {


    public String kaorderdetail(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\tb.customerid,\n" +
                "\tb.`name`,\n" +
                "\tCASE b.is_vip\n" +
                "WHEN '0' THEN\n" +
                "\t'会员卡'\n" +
                "ELSE\n" +
                "\t'VIP卡'\n" +
                "END AS ka,\n" +
                " a.opeation,\n" +
                " b.phone,\n" +
                " c.cardcode,\n" +
                " a.amount,\n" +
                " a.yueamount,\n" +
                " (\n" +
                "\tSELECT\n" +
                "\t\tNAME\n" +
                "\tFROM\n" +
                "\t\tgyxg_employees\n" +
                "\tWHERE\n" +
                "\t\temployeeid = a.employeeid\n" +
                ") AS empname,\n" +
                " a.id,\n" +
                " a.intime,\n" +
                " a.memo\n" +
                "FROM\n" +
                "\tgyxg_customers_logs a\n" +
                "LEFT JOIN gyxg_customers b ON a.customerid = b.customerid\n" +
                "LEFT JOIN gyxg_vip_customers c ON a.customerid = c.customerid where a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        if(params.containsKey("phone")){
            sql.append(" and (b.phone like '%"+params.get("phone")+"%' or b.name like '%"+params.get("phone")+"%')");
        }
        sql.append(" and DATE_FORMAT(a.intime, '%Y-%m-%d') >= '"+params.get("begin_date")+ "' and DATE_FORMAT(a.intime, '%Y-%m-%d') <='"+params.get("end_date")+"'");
        sql.append(" order by a.intime asc");
        return sql.toString();
    }
}
