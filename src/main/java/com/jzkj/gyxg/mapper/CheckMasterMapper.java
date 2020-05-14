package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.CheckMaster;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.CheckProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface CheckMasterMapper {
    @Delete({
        "delete from gyxg_check_masters",
        "where checkid = #{checkid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer checkid);

    @Insert({
        "insert into gyxg_check_masters (companyid, storeid, ",
        "warehouseid, code, ",
        "checkdate, originalcode, ",
        "totalmoney, departmentid, ",
        "employee1id, employee2id, ",
        "employee3id, intime, ",
        "checktime, memo, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{warehouseid,jdbcType=INTEGER}, #{code,jdbcType=VARCHAR}, ",
        "#{checkdate,jdbcType=DATE}, #{originalcode,jdbcType=VARCHAR}, ",
        "#{totalmoney,jdbcType=DECIMAL}, #{departmentid,jdbcType=INTEGER}, ",
        "#{employee1id,jdbcType=INTEGER}, #{employee2id,jdbcType=INTEGER}, ",
        "#{employee3id,jdbcType=INTEGER}, #{intime,jdbcType=TIMESTAMP}, ",
        "#{checktime,jdbcType=TIMESTAMP}, #{memo,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="checkid", before=false, resultType=Integer.class)
    int insert(CheckMaster record);

    @Select({
        "select",
        "checkid, companyid, storeid, warehouseid, code, checkdate, originalcode, totalmoney, ",
        "departmentid, employee1id, employee2id, employee3id, intime, checktime, memo, ",
        "status",
        "from gyxg_check_masters",
        "where checkid = #{checkid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="checkid", property="checkid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="checkdate", property="checkdate", jdbcType=JdbcType.DATE),
        @Result(column="originalcode", property="originalcode", jdbcType=JdbcType.VARCHAR),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    CheckMaster selectByPrimaryKey(Integer checkid);

    @Select({
        "select",
        "checkid, companyid, storeid, warehouseid, code, checkdate, originalcode, totalmoney, ",
        "departmentid, employee1id, employee2id, employee3id, intime, checktime, memo, ",
        "status",
        "from gyxg_check_masters"
    })
    @Results({
        @Result(column="checkid", property="checkid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="checkdate", property="checkdate", jdbcType=JdbcType.DATE),
        @Result(column="originalcode", property="originalcode", jdbcType=JdbcType.VARCHAR),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<CheckMaster> selectAll();

    @Update({
        "update gyxg_check_masters",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "warehouseid = #{warehouseid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "checkdate = #{checkdate,jdbcType=DATE},",
          "originalcode = #{originalcode,jdbcType=VARCHAR},",
          "totalmoney = #{totalmoney,jdbcType=DECIMAL},",
          "departmentid = #{departmentid,jdbcType=INTEGER},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "employee2id = #{employee2id,jdbcType=INTEGER},",
          "employee3id = #{employee3id,jdbcType=INTEGER},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "checktime = #{checktime,jdbcType=TIMESTAMP},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where checkid = #{checkid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(CheckMaster record);

    @SelectProvider(type = CheckProvider.class,method = "selectMasterList")
    List<Map> selectMasterList(Map params);

    int findCountByStatus(String status);
}