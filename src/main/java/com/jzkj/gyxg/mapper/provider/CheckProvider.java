package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class CheckProvider {

    public String selectMasterList(Map params){
        StringBuffer sql = new StringBuffer("SELECT a.memo,\n" +
                "\ta.checkid,\n" +
                "\ta.checkdate,\n" +
                "\ta.`code`,\n" +
                "\ta.originalcode,\n" +
                "\ta.totalmoney,\n" +
                "\ta.warehouseid,\n" +
                "\tb. NAME AS warehousename,\n" +
                "\ta.departmentid,\n" +
                "\tb.`name` AS deptname,\n" +
                "\ta.`status`,\n" +
                "\ta.employee1id,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\td. NAME\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees d\n" +
                "\t\tWHERE\n" +
                "\t\t\td.employeeid = a.employee1id\n" +
                "\t) AS emp1name,\n" +
                "\ta.employee2id,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\te. NAME\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees e\n" +
                "\t\tWHERE\n" +
                "\t\t\te.employeeid = a.employee2id\n" +
                "\t) AS emp2name,\n" +
                "\ta.employee3id,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tf. NAME\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees f\n" +
                "\t\tWHERE\n" +
                "\t\t\tf.employeeid = a.employee3id\n" +
                "\t) AS emp3name\n" +
                "FROM\n" +
                "\tgyxg_check_masters a\n" +
                "LEFT JOIN gyxg_warehouses b ON a.warehouseid = b.warehouseid\n" +
                "LEFT JOIN gyxg_departments c ON a.departmentid = c.departmentid  where  1=1");
        if(params.containsKey("startdate")){
            sql.append(" and a.intime between '"+params.get("startdate")+"' and '"+params.get("enddate")+"'");
        }
        if(params.containsKey("status")){
            sql.append(" and a.status='"+params.get("status")+"'");
        }else{
            sql.append(" and a.status!='2'");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        sql.append(" order by a.checkdate desc");
        return sql.toString();
    }

    public String selectSlaverList(Integer checkid){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.slaveid,\n" +
                "\ta.goodsid,\n" +
                "\tb.`code`,\n" +
                "\tb.`name`,\n" +
                "\tb.specs,\n" +
                "\tb.unit,\n" +
//                "\tb.price,\n" +
                "\tc.stock,\n" +
                "\tc.ycstock as price,\n" +
                "\ta.checkstock,\n" +
                "\ta.usednums,\n" +
                "\ta.usedmoney,\n" +
                "\ta.memo\n" +
                "FROM\n" +
                "\tgyxg_checks_slaves a\n" +
                "LEFT JOIN gyxg_goods b ON a.goodsid = b.goodsid\n" +
                "LEFT JOIN gyxg_goods_stock c ON a.goodsid = c.goodsid  where  1=1");
        sql.append(" and a.checkid="+checkid);
        return sql.toString();
    }
}
