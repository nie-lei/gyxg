package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class LossProvider {

    public String selectMasterList(Map params){
        StringBuffer sql = new StringBuffer(" SELECT a.`code`,a.memo,\n" +
                "\ta.lossid,\n" +
                "\ta.checkdate,\n" +
                "\ta.originalcode,\n" +
                "\ta.totalmoney,\n" +
                "\ta.warehouseid,\n" +
                "\tb.`name` AS warehousename,\n" +
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
                "\t) AS emp3name,\n" +
                "\ta.`status`\n" +
                "FROM\n" +
                "\tgyxg_loss_masters a\n" +
                "LEFT JOIN gyxg_warehouses b ON a.warehouseid = b.warehouseid where  1=1");
        if(params.containsKey("startdate")){
            sql.append(" and a.intime between '"+params.get("startdate")+"' and '"+params.get("enddate")+"'");
        }
        if(params.containsKey("status")){
            sql.append(" and a.status='"+params.get("status")+"'");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }
}
