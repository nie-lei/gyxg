package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Reserveds;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.CustomersProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface ReservedsMapper {
    @Delete({
        "delete from gyxg_reserveds",
        "where reservedid = #{reservedid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer reservedid);

    @Insert({
        "insert into gyxg_reserveds (companyid, storeid, ",
        "customerid, customerphone, ",
        "reservedperson, payid, ",
        "depositmoney, phone, ",
        "nums, unitname, ",
        "canduan, reservedtime, ",
        "realitytime, tableids, ",
        "memo, employeeid1, ",
        "intime, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{customerid,jdbcType=INTEGER}, #{customerphone,jdbcType=VARCHAR}, ",
        "#{reservedperson,jdbcType=VARCHAR}, #{payid,jdbcType=INTEGER}, ",
        "#{depositmoney,jdbcType=DECIMAL}, #{phone,jdbcType=VARCHAR}, ",
        "#{nums,jdbcType=INTEGER}, #{unitname,jdbcType=VARCHAR}, ",
        "#{canduan,jdbcType=VARCHAR}, #{reservedtime,jdbcType=TIMESTAMP}, ",
        "#{realitytime,jdbcType=TIMESTAMP}, #{tableids,jdbcType=VARCHAR}, ",
        "#{memo,jdbcType=VARCHAR}, #{employeeid1,jdbcType=INTEGER}, ",
        "#{intime,jdbcType=TIMESTAMP}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="reservedid", before=false, resultType=Integer.class)
    int insert(Reserveds record);

    @Select({
        "select",
        "reservedid, companyid, storeid, customerid, customerphone, reservedperson, payid, ",
        "depositmoney, phone, nums, unitname, canduan, reservedtime, realitytime, tableids, ",
        "memo, employeeid1, intime, status",
        "from gyxg_reserveds",
        "where reservedid = #{reservedid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="reservedid", property="reservedid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerphone", property="customerphone", jdbcType=JdbcType.VARCHAR),
        @Result(column="reservedperson", property="reservedperson", jdbcType=JdbcType.VARCHAR),
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER),
        @Result(column="depositmoney", property="depositmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="nums", property="nums", jdbcType=JdbcType.INTEGER),
        @Result(column="unitname", property="unitname", jdbcType=JdbcType.VARCHAR),
        @Result(column="canduan", property="canduan", jdbcType=JdbcType.VARCHAR),
        @Result(column="reservedtime", property="reservedtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="realitytime", property="realitytime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="tableids", property="tableids", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid1", property="employeeid1", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Reserveds selectByPrimaryKey(Integer reservedid);

    @Select({
        "select",
        "reservedid, companyid, storeid, customerid, customerphone, reservedperson, payid, ",
        "depositmoney, phone, nums, unitname, canduan, reservedtime, realitytime, tableids, ",
        "memo, employeeid1, intime, status",
        "from gyxg_reserveds"
    })
    @Results({
        @Result(column="reservedid", property="reservedid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerphone", property="customerphone", jdbcType=JdbcType.VARCHAR),
        @Result(column="reservedperson", property="reservedperson", jdbcType=JdbcType.VARCHAR),
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER),
        @Result(column="depositmoney", property="depositmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="nums", property="nums", jdbcType=JdbcType.INTEGER),
        @Result(column="unitname", property="unitname", jdbcType=JdbcType.VARCHAR),
        @Result(column="canduan", property="canduan", jdbcType=JdbcType.VARCHAR),
        @Result(column="reservedtime", property="reservedtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="realitytime", property="realitytime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="tableids", property="tableids", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid1", property="employeeid1", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Reserveds> selectAll();

    @Update({
        "update gyxg_reserveds",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "customerid = #{customerid,jdbcType=INTEGER},",
          "customerphone = #{customerphone,jdbcType=VARCHAR},",
          "reservedperson = #{reservedperson,jdbcType=VARCHAR},",
          "payid = #{payid,jdbcType=INTEGER},",
          "depositmoney = #{depositmoney,jdbcType=DECIMAL},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "nums = #{nums,jdbcType=INTEGER},",
          "unitname = #{unitname,jdbcType=VARCHAR},",
          "canduan = #{canduan,jdbcType=VARCHAR},",
          "reservedtime = #{reservedtime,jdbcType=TIMESTAMP},",
          "realitytime = #{realitytime,jdbcType=TIMESTAMP},",
          "tableids = #{tableids,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "employeeid1 = #{employeeid1,jdbcType=INTEGER},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=VARCHAR}",
        "where reservedid = #{reservedid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Reserveds record);

    @SelectProvider(type = CustomersProvider.class,method = "selectReservedsList")
    List<Map> selectReservedsList(Map params);
}