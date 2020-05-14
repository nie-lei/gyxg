package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.OrderPrintMaster;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface OrderPrintMasterMapper {
    @Delete({
        "delete from gyxg_orderprint_master",
        "where printid = #{printid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer printid);

    @Insert({
        "insert into gyxg_orderprint_master (companyid, storeid, ",
        "orderid, customerid, ",
        "code, ordertime, ",
        "nums, employeeid, ",
        "flag, tablecodes, ",
        "sendtype, memo, ",
        "status, type)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{orderid,jdbcType=INTEGER}, #{customerid,jdbcType=INTEGER}, ",
        "#{code,jdbcType=VARCHAR}, #{ordertime,jdbcType=TIMESTAMP}, ",
        "#{nums,jdbcType=INTEGER}, #{employeeid,jdbcType=INTEGER}, ",
        "#{flag,jdbcType=VARCHAR}, #{tablecodes,jdbcType=VARCHAR}, ",
        "#{sendtype,jdbcType=VARCHAR}, #{memo,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="printid", before=false, resultType=Integer.class)
    int insert(OrderPrintMaster record);

    @Select({
        "select",
        "printid, companyid, storeid, orderid, customerid, code, ordertime, nums, employeeid, ",
        "flag, tablecodes, sendtype, memo, status, type",
        "from gyxg_orderprint_master",
        "where printid = #{printid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="printid", property="printid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="ordertime", property="ordertime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="nums", property="nums", jdbcType=JdbcType.INTEGER),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="tablecodes", property="tablecodes", jdbcType=JdbcType.VARCHAR),
        @Result(column="sendtype", property="sendtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR)
    })
    OrderPrintMaster selectByPrimaryKey(Integer printid);

    @Select({
        "select",
        "printid, companyid, storeid, orderid, customerid, code, ordertime, nums, employeeid, ",
        "flag, tablecodes, sendtype, memo, status, type",
        "from gyxg_orderprint_master"
    })
    @Results({
        @Result(column="printid", property="printid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="ordertime", property="ordertime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="nums", property="nums", jdbcType=JdbcType.INTEGER),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="tablecodes", property="tablecodes", jdbcType=JdbcType.VARCHAR),
        @Result(column="sendtype", property="sendtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR)
    })
    List<OrderPrintMaster> selectAll();

    @Update({
        "update gyxg_orderprint_master",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "orderid = #{orderid,jdbcType=INTEGER},",
          "customerid = #{customerid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "ordertime = #{ordertime,jdbcType=TIMESTAMP},",
          "nums = #{nums,jdbcType=INTEGER},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "tablecodes = #{tablecodes,jdbcType=VARCHAR},",
          "sendtype = #{sendtype,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=VARCHAR}",
        "where printid = #{printid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(OrderPrintMaster record);
}