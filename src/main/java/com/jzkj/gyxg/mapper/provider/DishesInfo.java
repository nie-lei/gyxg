package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class DishesInfo {

    public String selectMethodsList(Map params){
        StringBuffer sql = new StringBuffer("select a.methodid,a.name,a.attachmoney,a.attachratio,a.status,a.dishesid,b.name as classfyname from gyxg_dishe_methods a left join gyxg_dishe_classifys b on a.dishesid =b.classifyid where 1=1 ");
        if(params.containsKey("name")){
            sql.append(" and a.name like '%"+params.get("name")+"%' ");
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }



    public String selectList(Map params) {
        StringBuffer sql = new StringBuffer("\n" +
                "SELECT\n a.disheid,a.is_auto as isauto,a.state," +
                "\tb.classifyid,\n" +
                "\tb. NAME AS classfyname,\n" +
                "\tCASE\n" +
                "WHEN a.is_goods = '0' THEN\n" +
                "\t'否'\n" +
                "ELSE '是' END as isgoods,\n" +
                "a.is_goods,\n" +
                "a.code,\n" +
                "a.name,\n" +
                "a.words,\n" +
                "a.pic,\n" +
                "a.methodid,\n" +
                "c.name as methodname,\n" +
                "a.specs,\n" +
                "a.unit,\n" +
                "a.price,\n" +
                "a.amount,a.pinyin,\n" +
                "a.status,\n" +
                "a.memo\n" +
                "FROM\n" +
                "\tgyxg_dishes a\n" +
                "LEFT JOIN gyxg_dishe_classifys b ON a.dishesid = b.classifyid\n" +
                "LEFT JOIN gyxg_dishe_methods c ON a.methodid = c.methodid \n" +
                "where 1=1 ");
        if(params.containsKey("name")){
            sql.append(" and (a.name like '%"+params.get("name")+"%' or a.pinyin like '%"+params.get("name")+"%')");
        }
        if(params.containsKey("isgoods")){
            sql.append(" and a.is_goods = '"+params.get("isgoods")+"'");
        }
        if(params.containsKey("dishesid")){
            sql.append(" and a.dishesid="+params.get("dishesid"));
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }

    public String selectListGuqing(Map params) {
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.disheid,\n" +
                "\tb. NAME AS classfyname,\n" +
                "\tCASE\n" +
                "WHEN a.is_goods = '0' THEN\n" +
                "\t'否'\n" +
                "ELSE '是' END as isgoods,\n" +
                "a.code,\n" +
                "a.name,\n" +
                "a.words,\n" +
                "a.pic,\n" +
                "a.specs,\n" +
                "a.unit,\n" +
                "a.price,a.pinyin,\n" +
                "a.flag_amoun,\n" +
                "a.amount,\n" +
                "a.flag_guqing,\n" +
                "a.status,\n" +
                "a.memo\n" +
                "FROM\n" +
                "\tgyxg_dishes a\n" +
                "LEFT JOIN gyxg_dishe_classifys b ON a.dishesid = b.classifyid where 1=1");
        if(params.containsKey("name")){
            sql.append(" and (a.name like '%"+params.get("name")+"%' or a.pinyin like '%"+params.get("name")+"%')");
        }
        if(params.containsKey("dishesid")){
            sql.append(" and a.dishesid="+params.get("dishesid"));
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }


    public String selectListcaipu(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                "\ta.pageid,a.dishesid,\n" +
                "\tb.`name` classfyname,\n" +
                "\ta.sequence,\n" +
                "\tCASE WHEN a.pagemould='1' THEN '一个菜'\n" +
                "\tWHEN a.pagemould='2' THEN '两个菜'\n" +
                "WHEN a.pagemould='3' THEN '三个菜'\n" +
                "WHEN a.pagemould='4' THEN '四个菜'\n" +
                "WHEN a.pagemould='6' THEN '六个菜'\n" +
                "ELSE '十四个菜' END AS pagemouldname,\n" +
                "\ta.pagemould,\n" +
                "\ta.status,a.disheid\n" +
                "FROM\n" +
                "\tgyxg_dishe_pages a\n" +
                "LEFT JOIN gyxg_dishe_classifys b ON a.dishesid = b.classifyid where 1=1 ");
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        if(params.containsKey("dishesid")){
            sql.append(" and a.dishesid="+params.get("dishesid"));
        }
        return sql.toString();
    }
}
