package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.OrderPrint;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface OrderPrintMapper {
    @Delete({
        "delete from gyxg_orderprint",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_orderprint (orderid, tablesid, ",
        "disheid, name, unit, ",
        "amount, methodid, ",
        "price, memo, flag, ",
        "employeeid, status, ",
        "state, type)",
        "values (#{orderid,jdbcType=INTEGER}, #{tablesid,jdbcType=INTEGER}, ",
        "#{disheid,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{unit,jdbcType=VARCHAR}, ",
        "#{amount,jdbcType=INTEGER}, #{methodid,jdbcType=INTEGER}, ",
        "#{price,jdbcType=DECIMAL}, #{memo,jdbcType=VARCHAR}, #{flag,jdbcType=VARCHAR}, ",
        "#{employeeid,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR}, ",
        "#{state,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(OrderPrint record);

    @Select({
        "select",
        "id, orderid, tablesid, disheid, name, unit, amount, methodid, price, memo, flag, ",
        "employeeid, status, state, type",
        "from gyxg_orderprint",
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
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR)
    })
    OrderPrint selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, orderid, tablesid, disheid, name, unit, amount, methodid, price, memo, flag, ",
        "employeeid, status, state, type",
        "from gyxg_orderprint"
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
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR)
    })
    List<OrderPrint> selectAll();

    @Update({
        "update gyxg_orderprint",
        "set orderid = #{orderid,jdbcType=INTEGER},",
          "tablesid = #{tablesid,jdbcType=INTEGER},",
          "disheid = #{disheid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "unit = #{unit,jdbcType=VARCHAR},",
          "amount = #{amount,jdbcType=INTEGER},",
          "methodid = #{methodid,jdbcType=INTEGER},",
          "price = #{price,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR},",
          "state = #{state,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(OrderPrint record);
}