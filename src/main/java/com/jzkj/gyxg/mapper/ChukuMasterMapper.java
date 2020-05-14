package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.ChukuMaster;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.GoodsProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface ChukuMasterMapper {
    @Delete({
        "delete from gyxg_chuku_masters",
        "where chukuid = #{chukuid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer chukuid);

    @Insert({
        "insert into gyxg_chuku_masters (companyid, storeid, ",
        "code, purchasedate, ",
        "totalmoney, warehouseid, ",
        "departmentid, employee1id, ",
        "employee2id, employee3id, ",
        "intime, checktime, ",
        "memo, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{code,jdbcType=VARCHAR}, #{purchasedate,jdbcType=DATE}, ",
        "#{totalmoney,jdbcType=DECIMAL}, #{warehouseid,jdbcType=INTEGER}, ",
        "#{departmentid,jdbcType=INTEGER}, #{employee1id,jdbcType=INTEGER}, ",
        "#{employee2id,jdbcType=INTEGER}, #{employee3id,jdbcType=INTEGER}, ",
        "#{intime,jdbcType=TIMESTAMP}, #{checktime,jdbcType=TIMESTAMP}, ",
        "#{memo,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="chukuid", before=false, resultType=Integer.class)
    int insert(ChukuMaster record);

    @Select({
        "select",
        "chukuid, companyid, storeid, code, purchasedate, totalmoney, warehouseid, departmentid, ",
        "employee1id, employee2id, employee3id, intime, checktime, memo, status",
        "from gyxg_chuku_masters",
        "where chukuid = #{chukuid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="chukuid", property="chukuid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="purchasedate", property="purchasedate", jdbcType=JdbcType.DATE),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    ChukuMaster selectByPrimaryKey(Integer chukuid);

    @Select({
        "select",
        "chukuid, companyid, storeid, code, purchasedate, totalmoney, warehouseid, departmentid, ",
        "employee1id, employee2id, employee3id, intime, checktime, memo, status",
        "from gyxg_chuku_masters"
    })
    @Results({
        @Result(column="chukuid", property="chukuid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="purchasedate", property="purchasedate", jdbcType=JdbcType.DATE),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<ChukuMaster> selectAll();

    @Update({
        "update gyxg_chuku_masters",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "purchasedate = #{purchasedate,jdbcType=DATE},",
          "totalmoney = #{totalmoney,jdbcType=DECIMAL},",
          "warehouseid = #{warehouseid,jdbcType=INTEGER},",
          "departmentid = #{departmentid,jdbcType=INTEGER},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "employee2id = #{employee2id,jdbcType=INTEGER},",
          "employee3id = #{employee3id,jdbcType=INTEGER},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "checktime = #{checktime,jdbcType=TIMESTAMP},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where chukuid = #{chukuid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ChukuMaster record);

    @SelectProvider(type = GoodsProvider.class,method = "selectChukuMasterList")
    List<Map> selectChukuMasterList(Map params);

    @SelectProvider(type = GoodsProvider.class,method = "selectRukuMasterList")
    List<Map> selectRukuMasterList(Map params);
}