package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class CustomersProvider {

    public String selectList(Map params){
        StringBuffer sql = new StringBuffer("SELECT is_vip,is_sign,\n" +
                "\tcustomerid,\n" +
                "\tname,\n" +
                "\tphone,sex,\n" +
                "\tCASE\n" +
                "WHEN sex = '0' THEN\n" +
                "\t'女'\n" +
                "ELSE\n" +
                "\t'男'\n" +
                "END AS sexname,\n" +
                " birthday,\n" +
                " nation,\n" +
                " CASE\n" +
                "WHEN is_vip = '0' THEN\n" +
                "\t'否'\n" +
                "ELSE\n" +
                "\t'是' END AS isvip,\n" +
                "\tCASE\n" +
                "WHEN is_sign = '0' THEN\n" +
                "\t'否'\n" +
                "ELSE\n" +
                "\t'是' END AS issign,\n" +
                "\tregistertime\n" +
                "FROM\n" +
                "\tgyxg_customers where 1=1 ");
        if(params.containsKey("nameorphone")){
            sql.append(" and name like '%"+params.get("nameorphone")+"%' or phone like '%"+params.get("nameorphone")+"%'");
        }
        return sql.toString();
    }


    public String selectVipList(Map params){
        StringBuffer sql = new StringBuffer("SELECT a.status AS status,\n" +
                "\ta.vipid,\n" +
                "\tb.cname as storename,\n" +
                "\tc.`name`,\n" +
                "\tc.phone,\n" +
                "\ta.cardcode,\n" +
                "\ta.begindate,\n" +
                "\ta.enddate,\n" +
                "\ta.discountrate,\n" +
                "\ta.intime,\n" +
                "\ta.employee1id,\n" +
                "\ta.employee2id,\n" +
                "\ta.employeeid,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\td.`name`\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees d\n" +
                "\t\tWHERE\n" +
                "\t\t\td.employeeid = a.employeeid\n" +
                "\t) AS empname,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\te.`name`\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees e\n" +
                "\t\tWHERE\n" +
                "\t\t\te.employeeid = a.employee1id\n" +
                "\t) AS emp1name,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tf.`name`\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees f\n" +
                "\t\tWHERE\n" +
                "\t\t\tf.employeeid = a.employee2id\n" +
                "\t) AS emp2name\n" +
                "FROM\n" +
                "\tgyxg_vip_customers a\n" +
                "LEFT JOIN gyxg_stores b ON a.storeid = b.storeid\n" +
                "LEFT JOIN gyxg_customers c ON a.customerid = c.customerid where 1=1 and a.status in ('0','1') ");
        if(params.containsKey("name")){
            sql.append(" and c.name like '%"+params.get("name")+"%'");
        }
        if(params.containsKey("storename")){
            sql.append(" and b.cname like '%"+params.get("storename")+"%'");
        }
        if(params.containsKey("phone")){
            sql.append(" and c.phone like '%"+params.get("phone")+"%'");
        }
        if(params.containsKey("cardcode")){
            sql.append(" a.cardcode like '%"+params.get("cardcode")+"%'");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }


    public String selectSignList(Map params) {
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.stauts as `status`,\n" +
                "\ta.signid,\n" +
                "\tb.cname as storename,\n" +
                "\tc.`name`,\n" +
                "\tc.phone cusphone,\n" +
                "\ta.phone,\n" +
                "\ta.sponsor,\n" +
                "\ta.daymoney,\n" +
                "\ta.oktime,\n" +
                "\ta.daymoney,\n" +
                "a.monthmoney,\n" +
                "\ta.intime,\n" +
                "\ta.employee1id,\n" +
                "\ta.employee2id,\n" +
                "\ta.employeeid,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\td.`name`\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees d\n" +
                "\t\tWHERE\n" +
                "\t\t\td.employeeid = a.employeeid\n" +
                "\t) AS empname,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\te.`name`\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees e\n" +
                "\t\tWHERE\n" +
                "\t\t\te.employeeid = a.employee1id\n" +
                "\t) AS emp1name,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tf.`name`\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees f\n" +
                "\t\tWHERE\n" +
                "\t\t\tf.employeeid = a.employee2id\n" +
                "\t) AS emp2name\n" +
                "FROM\n" +
                "\tgyxg_sign_customers a\n" +
                "LEFT JOIN gyxg_stores b ON a.storeid = b.storeid\n" +
                "LEFT JOIN gyxg_customers c ON a.customerid = c.customerid where 1=1 ");
        if(params.containsKey("name")){
            sql.append(" and c.name like '%"+params.get("name")+"%'");
        }
        if(params.containsKey("storename")){
            sql.append(" and b.cname like '%"+params.get("storename")+"%'");
        }
        if(params.containsKey("phone")){
            sql.append(" and c.phone like '%"+params.get("phone")+"%'");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }


    public String selectAcountList(Map params){
        StringBuffer sql = new StringBuffer(" SELECT\n" +
                "\ta.accountid,b.customerid,\n" +
                "\tb.`name`,\n" +
                "\tb.phone,\n" +
                "\ta.grouptotalmoney,\n" +
                "\ta.groupbalance,\n" +
                "\ta.singletotalpoint,\n" +
                "\ta.singlepointbalance,\n" +
                "\ta.lasttime,\n" +
                "\t0 as totalmoney\n" +
                "FROM\n" +
                "\tgyxg_customers_accounts a\n" +
                "LEFT JOIN gyxg_customers b ON a.customerid = b.customerid where 1=1 ");
        if(params.containsKey("name")){
            sql.append(" and b.name like '%"+params.get("name")+"%'");
        }
        if(params.containsKey("phone")){
            sql.append(" and b.phone like '%"+params.get("phone")+"%'");
        }
        sql.append(" and a.companyid="+params.get("companyid"));
        return sql.toString();
    }


    public String selectReservedsList(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.reservedid,\n" +
                "\ta.reservedperson,\n" +
                "\ta.phone,\n" +
                "a.nums,a.depositmoney,\n" +
                "a.canduan,a.tableids,a.intime,a.reservedtime,a.realitytime,a.employeeid1,c.`name` as emp1name,a.unitname,a.memo,a.`status`\n" +
                "FROM \n" +
                "\tgyxg_reserveds a LEFT JOIN gyxg_customers b on a.customerid=b.customerid\n" +
                "LEFT JOIN gyxg_employees c on a.employeeid1=c.employeeid where 1=1");
        if(params.containsKey("name")){
            sql.append(" and b.name like '%"+params.get("name")+"%'");
        }
        if(params.containsKey("phone")){
            sql.append(" and b.phone like '%"+params.get("phone")+"%'");
        }

        sql.append(" and a.companyid="+params.get("companyid"));
        return sql.toString();
    }



    public  String signListInfo(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.orderid,\n" +
                "\ta.ordertime,\n" +
                "\tc.`name` tablename,b.name,\n" +
                "\tb.phone,\n" +
                "\ta.totalmoney,\n" +
                "\ta.discountratio,\n" +
                "\ta.discountamount,\n" +
                "\ta.payamount,\n" +
                "\ta.overtime,\t\n" +
                "\ta.memo,\t\n" +
                "\ta.`status`,(select d.name from gyxg_employees d where d.employeeid=a.employee1id) as empname\n" +
                "FROM\n" +
                "\tgyxg_orders_master a\n" +
                "LEFT JOIN gyxg_customers b ON a.customerid = b.customerid\n" +
                "LEFT JOIN gyxg_tables c on a.tableid = c.tableid\n" +
                "WHERE\n" +
                "\ta.overtype = '1'");
        if(params.containsKey("nameorphone")){
            sql.append(" and (b.phone like '%"+params.get("nameorphone")+"%' or b.name like '%"+params.get("nameorphone")+"%')");
        }
        if(params.containsKey("status")){
            sql.append(" and a.status ='"+params.get("status")+"'");
        }
        if(params.containsKey("customerid")){
            sql.append(" and a.customerid="+params.get("customerid"));
        }
        if(params.containsKey("startdate")){
            sql.append(" and a.ordertime between '"+params.get("startdate")+"' and '"+params.get("enddate")+"'");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();


    }
}
