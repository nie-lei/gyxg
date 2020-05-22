package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.OrderPrintSlave;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface OrderPrintSlaveMapper {
    @Delete({
        "delete from gyxg_orderprint_slave",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_orderprint_slave (orderid, tablesid, ",
        "disheid, name, unit, ",
        "amount, methodid, ",
        "price, refundmoney, ",
        "memo, flag, employeeid, ",
        "status)",
        "values (#{orderid,jdbcType=INTEGER}, #{tablesid,jdbcType=INTEGER}, ",
        "#{disheid,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{unit,jdbcType=VARCHAR}, ",
        "#{amount,jdbcType=INTEGER}, #{methodid,jdbcType=INTEGER}, ",
        "#{price,jdbcType=DECIMAL}, #{refundmoney,jdbcType=DECIMAL}, ",
        "#{memo,jdbcType=VARCHAR}, #{flag,jdbcType=VARCHAR}, #{employeeid,jdbcType=INTEGER}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(OrderPrintSlave record);

    @Select({
        "select",
        "id, orderid, tablesid, disheid, name, unit, amount, methodid, price, refundmoney, ",
        "memo, flag, employeeid, status",
        "from gyxg_orderprint_slave",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="tablesid", property="tablesid", jdbcType=JdbcType.INTEGER),
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="refundmoney", property="refundmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    OrderPrintSlave selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, orderid, tablesid, disheid, name, unit, amount, methodid, price, refundmoney, ",
        "memo, flag, employeeid, status",
        "from gyxg_orderprint_slave"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="tablesid", property="tablesid", jdbcType=JdbcType.INTEGER),
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="refundmoney", property="refundmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<OrderPrintSlave> selectAll();

    @Update({
        "update gyxg_orderprint_slave",
        "set orderid = #{orderid,jdbcType=INTEGER},",
          "tablesid = #{tablesid,jdbcType=INTEGER},",
          "disheid = #{disheid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "unit = #{unit,jdbcType=VARCHAR},",
          "amount = #{amount,jdbcType=INTEGER},",
          "methodid = #{methodid,jdbcType=INTEGER},",
          "price = #{price,jdbcType=DECIMAL},",
          "refundmoney = #{refundmoney,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(OrderPrintSlave record);
}