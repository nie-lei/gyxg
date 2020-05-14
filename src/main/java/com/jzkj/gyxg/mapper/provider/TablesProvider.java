package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class TablesProvider {

    public String selectList(Map params){
        StringBuffer sql = new StringBuffer("SELECT a.tableid,a.memo,CASE\n" +
                "\t\tWHEN a.flag = '0' THEN '空闲'\n" +
                "\t\tELSE '占用中'\n" +
                "\tEND as flag,b.`name` as areaname,a.areaid,a.employeeid,d.name as empname,a.`code`,a.`name`,a.seats,a.minmoney,a.fixedfee,a.ratiofee,a.seatfee,a.status from gyxg_tables a LEFT JOIN gyxg_areas b on a.areaid=b.areaid left join gyxg_employees d on a.employeeid=d.employeeid where 1=1 ");
        if(params.containsKey("seats")){
            sql.append(" and a.seats > "+params.get("seats"));
        }
        if(params.containsKey("flag")){
            sql.append(" and a.flag='"+params.get("flag")+"'");
        }
        if(params.containsKey("areaid")){
            sql.append(" and a.areaid="+params.get("areaid"));
        }
        sql.append(" and a.companyid="+params.get("companyid")+" and a.storeid="+params.get("storeid"));
        return sql.toString();
    }
}
