package com.jzkj.gyxg.mapper.provider;

import java.util.Map;

public class RoleMenuProvider {


    public String updateRoleMenu(Map param){
        StringBuffer sql = new StringBuffer("save into gyxg_role_menu (roleid,menuid) values");
        String str = param.get("menuidstr").toString();
        String []idstr = str.split(",");
        for ( int i=0;i<idstr.length;i++) {
            if(i==idstr.length-1){
                sql.append("("+param.get("roleid")+","+idstr[i]+")");
            }else {
                sql.append("("+param.get("roleid")+","+idstr[i]+"),");
            }

        }
        return sql.toString();
    }
}
