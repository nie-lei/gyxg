package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.CustomersAccounts;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.CustomersProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface CustomersAccountsMapper {
    @Delete({
        "delete from gyxg_customers_accounts",
        "where accountid = #{accountid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer accountid);

    @Insert({
        "insert into gyxg_customers_accounts (customerid, companyid, ",
        "grouptotalmoney, groupbalance, ",
        "singletotalpoint, singlepointbalance, ",
        "singleconsume, lasttime)",
        "values (#{customerid,jdbcType=INTEGER}, #{companyid,jdbcType=INTEGER}, ",
        "#{grouptotalmoney,jdbcType=DECIMAL}, #{groupbalance,jdbcType=DECIMAL}, ",
        "#{singletotalpoint,jdbcType=INTEGER}, #{singlepointbalance,jdbcType=INTEGER}, ",
        "#{singleconsume,jdbcType=DECIMAL}, #{lasttime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="accountid", before=false, resultType=Integer.class)
    int insert(CustomersAccounts record);

    @Select({
        "select",
        "accountid, customerid, companyid, grouptotalmoney, groupbalance, singletotalpoint, ",
        "singlepointbalance, singleconsume, lasttime",
        "from gyxg_customers_accounts",
        "where accountid = #{accountid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="accountid", property="accountid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="grouptotalmoney", property="grouptotalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="groupbalance", property="groupbalance", jdbcType=JdbcType.DECIMAL),
        @Result(column="singletotalpoint", property="singletotalpoint", jdbcType=JdbcType.INTEGER),
        @Result(column="singlepointbalance", property="singlepointbalance", jdbcType=JdbcType.INTEGER),
        @Result(column="singleconsume", property="singleconsume", jdbcType=JdbcType.DECIMAL),
        @Result(column="lasttime", property="lasttime", jdbcType=JdbcType.TIMESTAMP)
    })
    CustomersAccounts selectByPrimaryKey(Integer accountid);

    @Select({
        "select",
        "accountid, customerid, companyid, grouptotalmoney, groupbalance, singletotalpoint, ",
        "singlepointbalance, singleconsume, lasttime",
        "from gyxg_customers_accounts"
    })
    @Results({
        @Result(column="accountid", property="accountid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="grouptotalmoney", property="grouptotalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="groupbalance", property="groupbalance", jdbcType=JdbcType.DECIMAL),
        @Result(column="singletotalpoint", property="singletotalpoint", jdbcType=JdbcType.INTEGER),
        @Result(column="singlepointbalance", property="singlepointbalance", jdbcType=JdbcType.INTEGER),
        @Result(column="singleconsume", property="singleconsume", jdbcType=JdbcType.DECIMAL),
        @Result(column="lasttime", property="lasttime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<CustomersAccounts> selectAll();

    @Update({
        "update gyxg_customers_accounts",
        "set customerid = #{customerid,jdbcType=INTEGER},",
          "companyid = #{companyid,jdbcType=INTEGER},",
          "grouptotalmoney = #{grouptotalmoney,jdbcType=DECIMAL},",
          "groupbalance = #{groupbalance,jdbcType=DECIMAL},",
          "singletotalpoint = #{singletotalpoint,jdbcType=INTEGER},",
          "singlepointbalance = #{singlepointbalance,jdbcType=INTEGER},",
          "singleconsume = #{singleconsume,jdbcType=DECIMAL},",
          "lasttime = #{lasttime,jdbcType=TIMESTAMP}",
        "where accountid = #{accountid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(CustomersAccounts record);

    @SelectProvider(type = CustomersProvider.class,method = "selectAcountList")
    List<Map> selectAcountList(Map params);

    @Select({
            "select",
            "accountid, customerid, companyid, grouptotalmoney, groupbalance, singletotalpoint, ",
            "singlepointbalance, singleconsume, lasttime",
            "from gyxg_customers_accounts",
            "where customerid = #{customerid,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="accountid", property="accountid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
            @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
            @Result(column="grouptotalmoney", property="grouptotalmoney", jdbcType=JdbcType.DECIMAL),
            @Result(column="groupbalance", property="groupbalance", jdbcType=JdbcType.DECIMAL),
            @Result(column="singletotalpoint", property="singletotalpoint", jdbcType=JdbcType.INTEGER),
            @Result(column="singlepointbalance", property="singlepointbalance", jdbcType=JdbcType.INTEGER),
            @Result(column="singleconsume", property="singleconsume", jdbcType=JdbcType.DECIMAL),
            @Result(column="lasttime", property="lasttime", jdbcType=JdbcType.TIMESTAMP)
    })
    CustomersAccounts selectByCusId(Integer customerid);

    @Select("DELETE FROM gyxg_customers_accounts WHERE customerid=#{customerid}")
    void deletebyCuid(Integer customerid);
}