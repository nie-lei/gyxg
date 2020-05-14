package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.WhiteMac;
import org.apache.ibatis.annotations.Select;

public interface WhiteMacMapper {

    @Select("select id,companyid,storeid,mac,intime,employeeid,memo,status "+
            "from gyxg_white_mac where "+
            "mac = #{mac,jdbcType=VARCHAR}"
    )
    WhiteMac findByMac(String mac);
}
