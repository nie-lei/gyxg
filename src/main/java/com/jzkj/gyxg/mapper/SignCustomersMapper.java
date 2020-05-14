package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.SignCustomers;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.CustomersProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface SignCustomersMapper {
    @Delete({
        "delete from gyxg_sign_customers",
        "where signid = #{signid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer signid);

    @Insert({
        "insert into gyxg_sign_customers (customerid, companyid, ",
        "storeid, sponsor, ",
        "phone, daymoney, ",
        "monthmoney, oktime, ",
        "intime, employeeid, ",
        "employee1id, employee2id, ",
        "stauts)",
        "values (#{customerid,jdbcType=INTEGER}, #{companyid,jdbcType=INTEGER}, ",
        "#{storeid,jdbcType=INTEGER}, #{sponsor,jdbcType=VARCHAR}, ",
        "#{phone,jdbcType=VARCHAR}, #{daymoney,jdbcType=DECIMAL}, ",
        "#{monthmoney,jdbcType=DECIMAL}, #{oktime,jdbcType=DATE}, ",
        "#{intime,jdbcType=TIMESTAMP}, #{employeeid,jdbcType=INTEGER}, ",
        "#{employee1id,jdbcType=INTEGER}, #{employee2id,jdbcType=INTEGER}, ",
        "#{stauts,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="signid", before=false, resultType=Integer.class)
    int insert(SignCustomers record);

    @Select({
        "select",
        "signid, customerid, companyid, storeid, sponsor, phone, daymoney, monthmoney, ",
        "oktime, intime, employeeid, employee1id, employee2id, stauts",
        "from gyxg_sign_customers",
        "where signid = #{signid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="signid", property="signid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="sponsor", property="sponsor", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="daymoney", property="daymoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="monthmoney", property="monthmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="oktime", property="oktime", jdbcType=JdbcType.DATE),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="stauts", property="stauts", jdbcType=JdbcType.VARCHAR)
    })
    SignCustomers selectByPrimaryKey(Integer signid);

    @Select({
        "select",
        "signid, customerid, companyid, storeid, sponsor, phone, daymoney, monthmoney, ",
        "oktime, intime, employeeid, employee1id, employee2id, stauts",
        "from gyxg_sign_customers"
    })
    @Results({
        @Result(column="signid", property="signid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="sponsor", property="sponsor", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="daymoney", property="daymoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="monthmoney", property="monthmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="oktime", property="oktime", jdbcType=JdbcType.DATE),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="stauts", property="stauts", jdbcType=JdbcType.VARCHAR)
    })
    List<SignCustomers> selectAll();

    @Update({
        "update gyxg_sign_customers",
        "set customerid = #{customerid,jdbcType=INTEGER},",
          "companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "sponsor = #{sponsor,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "daymoney = #{daymoney,jdbcType=DECIMAL},",
          "monthmoney = #{monthmoney,jdbcType=DECIMAL},",
          "oktime = #{oktime,jdbcType=DATE},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "employee2id = #{employee2id,jdbcType=INTEGER},",
          "stauts = #{stauts,jdbcType=VARCHAR}",
        "where signid = #{signid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SignCustomers record);

    @Select("select count(signid) from gyxg_sign_customers where customerid = #{customerid}")
    int checkIssign(Integer customerid);

    @SelectProvider(type = CustomersProvider.class,method = "selectSignList")
    List<Map> selectSignList(Map params);

    @Select("SELECT a.customerid,b.phone,b.`name`,a.daymoney,a.monthmoney from gyxg_sign_customers a LEFT JOIN gyxg_customers b on a.customerid=b.customerid\n" +
            "where b.phone ='${phone}' and a.stauts='0';\n")
    Map selectByCusPhone(@Param("phone") String phone);
}