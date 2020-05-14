package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.PrintClassfy;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface PrintClassfyMapper {
    @Delete({
        "delete from gyxg_print_classifys",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer classifyid);

    @Insert({
        "insert into gyxg_print_classifys (companyid, storeid, ",
        "code, name, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{code,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="classifyid", before=false, resultType=Integer.class)
    int insert(PrintClassfy record);

    @Select({
        "select",
        "classifyid, companyid, storeid, code, name, status",
        "from gyxg_print_classifys",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    PrintClassfy selectByPrimaryKey(Integer classifyid);

    @Select({
        "select",
        "classifyid, companyid, storeid, code, name, status",
        "from gyxg_print_classifys"
    })
    @Results({
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<PrintClassfy> selectAll();

    @Update({
        "update gyxg_print_classifys",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(PrintClassfy record);

    @Select("select classifyid,code,name,status from gyxg_print_classifys where storeid = #{storeid} and companyid = #{companyid}")
    List<Map> selectPrintList(Map params);

    @Select("select count(classifyid) from gyxg_print_classifys where companyid = #{companyid} and storeid = #{storeid} and (code = #{code} or name = #{name})")
    int checkPrintClassfy(PrintClassfy printClassfy);

    @Select("select classifyid,name from gyxg_print_classifys where companyid = #{companyid} and storeid = #{storeid}")
    List<Map> selectPrintClassfy(Map params);

    @Select("select count(classifyid) from gyxg_print_classifys where companyid = #{companyid} and storeid = #{storeid} and code = #{code}")
    int checkPrintClassfybycode(PrintClassfy printClassfy);

    @Select("select count(classifyid) from gyxg_print_classifys where companyid = #{companyid} and storeid = #{storeid} and name = #{name}")
    int checkPrintClassfybyname(PrintClassfy printClassfy);
}