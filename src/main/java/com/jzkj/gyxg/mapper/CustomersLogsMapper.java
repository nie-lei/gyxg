package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.CustomersLogs;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface CustomersLogsMapper {
    @Delete({
        "delete from gyxg_customers_logs",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_customers_logs (companyid, storeid, ",
        "customerid, opeation, ",
        "amount, yueamount, ",
        "memo, intime, ",
        "employeeid)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{customerid,jdbcType=INTEGER}, #{opeation,jdbcType=VARCHAR}, ",
        "#{amount,jdbcType=DECIMAL}, #{yueamount,jdbcType=DECIMAL}, ",
        "#{memo,jdbcType=VARCHAR}, #{intime,jdbcType=TIMESTAMP}, ",
        "#{employeeid,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(CustomersLogs record);

    @Select({
        "select",
        "id, companyid, storeid, customerid, opeation, amount, yueamount, memo, intime, ",
        "employeeid",
        "from gyxg_customers_logs",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="opeation", property="opeation", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="yueamount", property="yueamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER)
    })
    CustomersLogs selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, companyid, storeid, customerid, opeation, amount, yueamount, memo, intime, ",
        "employeeid",
        "from gyxg_customers_logs"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="opeation", property="opeation", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="yueamount", property="yueamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER)
    })
    List<CustomersLogs> selectAll();

    @Update({
        "update gyxg_customers_logs",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "customerid = #{customerid,jdbcType=INTEGER},",
          "opeation = #{opeation,jdbcType=VARCHAR},",
          "amount = #{amount,jdbcType=DECIMAL},",
          "yueamount = #{yueamount,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "employeeid = #{employeeid,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(CustomersLogs record);
}