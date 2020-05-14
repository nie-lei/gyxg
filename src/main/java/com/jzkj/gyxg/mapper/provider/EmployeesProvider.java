package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class EmployeesProvider {

    public String selectList(Map params){
        StringBuffer sql = new StringBuffer("SELECT\n" +
                " a.employeeid,a.discountamount," +
                "\ta.employeeno,\n" +
                "\ta. name,\n" +
                "\ta.phone,\n" +
                "\ta.opentime,\n" +
                "\ta.`status`,\n" +
                "\tb.`name` departmentname,\n" +
                "\tc.`name` positionname,\n" +
                "\td.name rolename\n" +
                "FROM\n" +
                "\tgyxg_employees a\n" +
                "LEFT JOIN gyxg_departments b ON a.departmentid = b.departmentid\n" +
                "LEFT JOIN gyxg_positions c ON c.positionid=a.positionid\n" +
                "LEFT JOIN gyxg_roles d on d.roleid=a.roleid where 1=1 ");
        if(params.containsKey("phone")){
            sql.append(" and a.phone like '%"+params.get("phone")+"%'");
        }
        if(params.containsKey("name")){
            sql.append(" and a.name like '%"+params.get("name")+"%'");
        }
        if(params.containsKey("employeeno")){
            sql.append(" and a.employeeno like '%"+params.get("employeeno")+"%'");
        }
        if(params.containsKey("starttime")){
            sql.append(" and a.starttime between '"+params.get("starttime")+"' and '"+params.get("endtime")+"'");
        }
        sql.append(" and a.companyid="+params.get("companyid")+ " and a.storeid="+params.get("storeid"));
        return sql.toString();
    }



    public String selectPaytypeList(Map params){
        StringBuffer sql = new StringBuffer(" select * from gyxg_paytype where 1=1 ");
        if(params.containsKey("companyid")){
            sql.append(" and companyid="+params.get("companyid")+"");
        }
        if(params.containsKey("storeid")){
            sql.append(" and storeid="+params.get("storeid")+"");
        }
        if(params.containsKey("is_quan")){
            sql.append(" and is_quan='"+params.get("is_quan")+"'");
        }
        return sql.toString();
    }
}
