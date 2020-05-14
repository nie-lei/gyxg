package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.VipCustomers;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.CustomersProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface VipCustomersMapper {
    @Delete({
        "delete from gyxg_vip_customers",
        "where vipid = #{vipid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer vipid);

    @Insert({
        "insert into gyxg_vip_customers (companyid, storeid, ",
        "customerid, cardcode, ",
        "begindate, enddate, discountrate, ",
        "intime, employeeid, ",
        "employee1id, employee2id, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{customerid,jdbcType=INTEGER}, #{cardcode,jdbcType=VARCHAR}, ",
        "#{begindate,jdbcType=DATE}, #{enddate,jdbcType=DATE}, #{discountrate,jdbcType=DECIMAL}, ",
        "#{intime,jdbcType=TIMESTAMP}, #{employeeid,jdbcType=INTEGER}, ",
        "#{employee1id,jdbcType=INTEGER}, #{employee2id,jdbcType=INTEGER}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="vipid", before=false, resultType=Integer.class)
    int insert(VipCustomers record);

    @Select({
        "select",
        "vipid, companyid, storeid, customerid, cardcode, ",
        "enddate, discountrate, intime, employeeid, employee1id, employee2id, status",
        "from gyxg_vip_customers",
        "where vipid = #{vipid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="vipid", property="vipid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="cardcode", property="cardcode", jdbcType=JdbcType.VARCHAR),
        @Result(column="begindate", property="begindate", jdbcType=JdbcType.DATE),
        @Result(column="enddate", property="enddate", jdbcType=JdbcType.DATE),
        @Result(column="discountrate", property="discountrate", jdbcType=JdbcType.DECIMAL),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    VipCustomers selectByPrimaryKey(Integer vipid);

    @Select({
        "select",
        "vipid, companyid, storeid, customerid, cardcode, begindate, ",
        "enddate, discountrate, intime, employeeid, employee1id, employee2id, status",
        "from gyxg_vip_customers"
    })
    @Results({
        @Result(column="vipid", property="vipid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="cardcode", property="cardcode", jdbcType=JdbcType.VARCHAR),
        @Result(column="begindate", property="begindate", jdbcType=JdbcType.DATE),
        @Result(column="enddate", property="enddate", jdbcType=JdbcType.DATE),
        @Result(column="discountrate", property="discountrate", jdbcType=JdbcType.DECIMAL),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<VipCustomers> selectAll();

    @Update({
        "update gyxg_vip_customers",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "customerid = #{customerid,jdbcType=INTEGER},",
          "cardcode = #{cardcode,jdbcType=VARCHAR},",
          "begindate = #{begindate,jdbcType=DATE},",
          "enddate = #{enddate,jdbcType=DATE},",
          "discountrate = #{discountrate,jdbcType=DECIMAL},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "employee2id = #{employee2id,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where vipid = #{vipid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(VipCustomers record);

    @SelectProvider(type = CustomersProvider.class,method = "selectVipList")
    List<Map> selectVipList(Map params);


    @Select("select count(vipid) from gyxg_vip_customers where companyid = #{companyid} and storeid = #{storeid} and cardcode = #{cardcode}")
    int selectByCardcode(VipCustomers vip);


    @Select({
            "select",
            "vipid, companyid, storeid, customerid, cardcode, begindate, ",
            "enddate, discountrate, intime, employeeid, employee1id, employee2id, status",
            "from gyxg_vip_customers where customerid=#{customerid} and discountrate is not null"
    })
    VipCustomers selectByCusid(Integer customerid);

}