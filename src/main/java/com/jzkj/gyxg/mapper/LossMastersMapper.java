package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.LossMasters;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.LossProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface LossMastersMapper {
    @Delete({
        "delete from gyxg_loss_masters",
        "where lossid = #{lossid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer lossid);

    @Insert({
        "insert into gyxg_loss_masters (companyid, storeid, ",
        "warehouseid, code, ",
        "originalcode, checkdate, ",
        "totalmoney, employee1id, ",
        "employee2id, employee3id, ",
        "intime, checktime, ",
        "memo, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{warehouseid,jdbcType=INTEGER}, #{code,jdbcType=VARCHAR}, ",
        "#{originalcode,jdbcType=VARCHAR}, #{checkdate,jdbcType=DATE}, ",
        "#{totalmoney,jdbcType=DECIMAL}, #{employee1id,jdbcType=INTEGER}, ",
        "#{employee2id,jdbcType=INTEGER}, #{employee3id,jdbcType=INTEGER}, ",
        "#{intime,jdbcType=TIMESTAMP}, #{checktime,jdbcType=TIMESTAMP}, ",
        "#{memo,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="lossid", before=false, resultType=Integer.class)
    int insert(LossMasters record);

    @Select({
        "select",
        "lossid, companyid, storeid, warehouseid, code, originalcode, checkdate, totalmoney, ",
        "employee1id, employee2id, employee3id, intime, checktime, memo, status",
        "from gyxg_loss_masters",
        "where lossid = #{lossid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="lossid", property="lossid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="originalcode", property="originalcode", jdbcType=JdbcType.VARCHAR),
        @Result(column="checkdate", property="checkdate", jdbcType=JdbcType.DATE),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    LossMasters selectByPrimaryKey(Integer lossid);

    @Select({
        "select",
        "lossid, companyid, storeid, warehouseid, code, originalcode, checkdate, totalmoney, ",
        "employee1id, employee2id, employee3id, intime, checktime, memo, status",
        "from gyxg_loss_masters"
    })
    @Results({
        @Result(column="lossid", property="lossid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="originalcode", property="originalcode", jdbcType=JdbcType.VARCHAR),
        @Result(column="checkdate", property="checkdate", jdbcType=JdbcType.DATE),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<LossMasters> selectAll();

    @Update({
        "update gyxg_loss_masters",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "warehouseid = #{warehouseid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "originalcode = #{originalcode,jdbcType=VARCHAR},",
          "checkdate = #{checkdate,jdbcType=DATE},",
          "totalmoney = #{totalmoney,jdbcType=DECIMAL},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "employee2id = #{employee2id,jdbcType=INTEGER},",
          "employee3id = #{employee3id,jdbcType=INTEGER},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "checktime = #{checktime,jdbcType=TIMESTAMP},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where lossid = #{lossid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(LossMasters record);

    @SelectProvider(type = LossProvider.class,method = "selectMasterList")
    List<Map> selectMasterList(Map params);
}