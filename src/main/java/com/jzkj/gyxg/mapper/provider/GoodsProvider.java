package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class GoodsProvider {

    public String selectMaterialClassfyList(Map params){
        StringBuffer sql = new StringBuffer("SELECT a.code,a.name,a.`level`,a.classifyid,a.parentid,(select name from gyxg_material_classifys b where b.classifyid=a.parentid) as parentname from gyxg_material_classifys a where  1=1");
        if(params.containsKey("level")){
            sql.append(" and a.level="+params.get("level"));
        }
        if(params.containsKey("parentid")){
            sql.append(" and a.parentid="+params.get("parentid"));
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }



    public String selectGoodsList(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.goodsid,\n" +
                "\ta.classifyid,\n" +
                "\tb. NAME AS classfyname,\n" +
                "\ta.code,\n" +
                "\ta.name,\n" +
                "\ta.specs,a.pinyin,\n" +
                "\ta.unit,\n" +
                "\ta.price,\n" +
                "\ta.maxstock,\n" +
                "\ta.minstock\n" +
                "FROM \n" +
                "\tgyxg_goods a\n" +
                "LEFT JOIN gyxg_material_classifys b ON a.classifyid = b.classifyid where  1=1");
        if(params.containsKey("name")){
            sql.append(" and (a.name like '%"+params.get("name")+"%' or a.pinyin like '%'"+params.get("name")+"%'");
        }
        if(params.containsKey("classifyid")){
            sql.append(" and a.classifyid = "+params.get("classifyid"));
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }


    public String selectChukuMasterList(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.chukuid,a.memo,\n" +
                "\ta.`code`,\n" +
                "\ta.purchasedate,\n" +
                "\ta.totalmoney,\n" +
                "\ta.warehouseid,\n" +
                "\tb.`name` AS warehousename,\n" +
                "\ta.departmentid,\n" +
                "\tc.`name` AS deptname,\n" +
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
                "\tgyxg_chuku_masters a\n" +
                " LEFT JOIN gyxg_warehouses b ON a.warehouseid = b.warehouseid\n" +
                " LEFT JOIN gyxg_departments c ON a.departmentid = c.departmentid  where  1=1");
        if(params.containsKey("startdate")){
            sql.append(" and a.intime between '"+params.get("startdate")+"' and '"+params.get("enddate")+"'");
        }
        if(params.containsKey("status")){
            sql.append(" and a.status='"+params.get("status")+"'");
        }else{
            sql.append(" and a.status!='2'");
        }
        if(params.containsKey("code")){
            sql.append(" and a.code like '%"+params.get("code")+"%'");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        sql.append(" order by a.purchasedate desc");
        return sql.toString();
    }



    public String selectRukuMasterList(Map params){
        StringBuffer sql = new StringBuffer("SELECT a.memo,a.is_pay,\n" +
                "\ta.purchaseid,\n" +
                "\ta. `code`,\n" +
                "\ta.purchasedate,\n" +
                "\ta.supplierid,\n" +
                "\tb.`name` AS supname,\n" +
                "\ta.totalmoney,\n" +
                "\ta.departmentid,\n" +
                "\tc.`name` as dname,\n" +
                "\ta.employee1id,\n" +
                "\t\t(\n" +
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
                "\ta.employee5id,\n" +
                "(\n" +
                "\t\tSELECT\n" +
                "\t\t\tg. NAME\n" +
                "\t\tFROM\n" +
                "\t\t\tgyxg_employees g\n" +
                "\t\tWHERE\n" +
                "\t\t\tg.employeeid = a.employee5id\n" +
                "\t) AS emp5name,a.`status`\n" +
                "\n" +
                "FROM\n" +
                "\tgyxg_purchase_masters a\n" +
                "LEFT JOIN gyxg_suppliers b ON a.supplierid = b.supplierid\n" +
                "LEFT JOIN gyxg_warehouses c on a.departmentid=c.warehouseid  where  1=1");
        if(params.containsKey("startdate")){
            sql.append(" and a.intime between '"+params.get("startdate")+"' and '"+params.get("enddate")+"'");
        }
        if(params.containsKey("status")){
            sql.append(" and a.status='"+params.get("status")+"'");
        }else{
            sql.append(" and a.status!='2'");//不查询已取消
        }
        if(params.containsKey("code")){
            sql.append(" and a.code like '%"+params.get("code")+"%'");
        }
        if(params.containsKey("supplierid")){
            sql.append(" and a.supplierid="+params.get("supplierid"));
        }
        sql.append(" and a.flag='"+params.get("flag")+"' and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        sql.append(" order by a.purchasedate desc");
        return sql.toString();
    }



    public String goodStockList(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\tb.`name` AS warehousename,\n" +
                "\td.`name` AS classfyname,\n" +
                "\tc.`code`,\n" +
                "\tc.`name`,\n" +
                "\tc.specs,\n" +
                "\ta.unit,\n" +
                "\tc.maxstock,\n" +
                "\tc.minstock,\n" +
                "\ta.ycstock,\n" +
                "\ta.stock,\n" +
                "\tc.price,\n" +
                "\ta.lasttime\n" +
                "FROM\n" +
                "\tgyxg_goods_stock a\n" +
                "LEFT JOIN gyxg_warehouses b ON a.warehouseid = b.warehouseid\n" +
                "LEFT JOIN gyxg_goods c ON c.goodsid = a.goodsid\n" +
                "LEFT JOIN gyxg_material_classifys d ON c.classifyid = d.classifyid where 1 = 1");
        if(params.containsKey("classifyid")){
            sql.append(" and c.classifyid="+params.get("classifyid"));
        }
        if(params.containsKey("warehouseid")){
            sql.append(" and a.warehouseid="+params.get("warehouseid"));
        }
        if(params.containsKey("nameorcode")){
            sql.append(" and (c.code like '%"+params.get("nameorcode")+"%' or c.name like '%"+params.get("nameorcode")+"%')");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }
}
