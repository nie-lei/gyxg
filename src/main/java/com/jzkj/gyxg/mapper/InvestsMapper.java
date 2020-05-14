package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Invests;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface InvestsMapper {
    @Delete({
        "delete from gyxg_invests",
        "where orderid = #{orderid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer orderid);

    @Insert({
        "insert into gyxg_invests (companyid, storeid, ",
        "payid, customerid, ",
        "investtype, investmoney, ",
        "rebatemoney, rebatepoint, ",
        "investtime, employeeid, ",
        "employee1id, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{payid,jdbcType=INTEGER}, #{customerid,jdbcType=INTEGER}, ",
        "#{investtype,jdbcType=VARCHAR}, #{investmoney,jdbcType=DECIMAL}, ",
        "#{rebatemoney,jdbcType=DECIMAL}, #{rebatepoint,jdbcType=INTEGER}, ",
        "#{investtime,jdbcType=TIMESTAMP}, #{employeeid,jdbcType=INTEGER}, ",
        "#{employee1id,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="orderid", before=false, resultType=Integer.class)
    int insert(Invests record);

    @Select({
        "select",
        "orderid, companyid, storeid, payid, customerid, investtype, investmoney, rebatemoney, ",
        "rebatepoint, investtime, employeeid, employee1id, status",
        "from gyxg_invests",
        "where orderid = #{orderid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="investtype", property="investtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="investmoney", property="investmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="rebatemoney", property="rebatemoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="rebatepoint", property="rebatepoint", jdbcType=JdbcType.INTEGER),
        @Result(column="investtime", property="investtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Invests selectByPrimaryKey(Integer orderid);

    @Select({
        "select",
        "orderid, companyid, storeid, payid, customerid, investtype, investmoney, rebatemoney, ",
        "rebatepoint, investtime, employeeid, employee1id, status",
        "from gyxg_invests"
    })
    @Results({
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="investtype", property="investtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="investmoney", property="investmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="rebatemoney", property="rebatemoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="rebatepoint", property="rebatepoint", jdbcType=JdbcType.INTEGER),
        @Result(column="investtime", property="investtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Invests> selectAll();

    @Update({
        "update gyxg_invests",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "payid = #{payid,jdbcType=INTEGER},",
          "customerid = #{customerid,jdbcType=INTEGER},",
          "investtype = #{investtype,jdbcType=VARCHAR},",
          "investmoney = #{investmoney,jdbcType=DECIMAL},",
          "rebatemoney = #{rebatemoney,jdbcType=DECIMAL},",
          "rebatepoint = #{rebatepoint,jdbcType=INTEGER},",
          "investtime = #{investtime,jdbcType=TIMESTAMP},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where orderid = #{orderid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Invests record);

    @Select("SELECT\n" +
            "\ta.investtype,a.orderid,\n" +
            "a.investmoney,\n" +
            "a.rebatemoney,\n" +
            "a.rebatepoint,\n" +
            "a.investtime,\n" +
            "(SELECT b.`name` from gyxg_employees b where b.employeeid=a.employeeid) as empname,\n" +
            "(SELECT c.`name` from gyxg_employees c where c.employeeid=a.employeeid) as emp1name,\n" +
            "\ta.`status`\n" +
            "FROM\n" +
            "\tgyxg_invests a where a.customerid = #{customerid} and a.companyid=#{companyid} and a.storeid=#{storeid}")
    List<Map> investsListBycid(Map params);

    @Select("SELECT\n" +
            "\tCASE\n" +
            "WHEN paytype = '0' THEN\n" +
            "\t'现金'\n" +
            "WHEN paytype = '1' THEN\n" +
            "\t'pos机'\n" +
            "WHEN paytype = '2' THEN\n" +
            "\t'收钱吧'\n" +
            "ELSE\n" +
            "\t'余额'\n" +
            "END AS paytypename,\n" +
            " CASE\n" +
            "WHEN paytypedesc = '0' THEN\n" +
            "\t'现金'\n" +
            "WHEN paytypedesc = '1' THEN\n" +
            "\t'支付宝'\n" +
            "WHEN paytypedesc = '2' THEN\n" +
            "\t'微信'\n" +
            "WHEN paytypedesc = '3' THEN\n" +
            "\t'百度钱包'\n" +
            "WHEN paytypedesc = '4' THEN\n" +
            "\t'京东钱包'\n" +
            "WHEN paytypedesc = '5' THEN\n" +
            "\t'qq钱包'\n" +
            "WHEN paytypedesc = '6' THEN\n" +
            "\t'余额'\n" +
            "WHEN paytypedesc = '8' THEN\n" +
            "\t'优惠券'\n" +
            "ELSE\n" +
            "\t'银行卡'\n" +
            "END AS paytypedescname,\n" +
            " payamount,\n" +
            " paytime,\n" +
            " memo,\n" +
            " CASE\n" +
            "WHEN flag = '0' THEN\n" +
            "\t'结账'\n" +
            "WHEN flag = '1' THEN\n" +
            "\t'充值'\n" +
            "ELSE\n" +
            "\t'订金'\n" +
            "END AS flagname,\n" +
            " CASE\n" +
            "WHEN `status` = '0' THEN\n" +
            "\t'录入'\n" +
            "WHEN `status` = '1' THEN\n" +
            "\t'支付成功'\n" +
            "WHEN `status` = '2' THEN\n" +
            "\t'支付失败'\n" +
            "WHEN `status` = '3' THEN\n" +
            "\t'退款成功'\n" +
            "ELSE\n" +
            "\t'退款失败'\n" +
            "END AS statusname\n" +
            "FROM\n" +
            "\tgyxg_payrecords where customerid=#{customerid};\n" +
            "\n")
    List<Map> consumptionListBycusid(Map params);
}