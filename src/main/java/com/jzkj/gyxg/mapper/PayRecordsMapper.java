package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.PayRecords;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.entity.bo.BPayRecords;
import com.jzkj.gyxg.mapper.provider.PayRecordsProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

public interface PayRecordsMapper extends Mapper<PayRecords> {
    @Delete({
        "delete from gyxg_payrecords",
        "where payid = #{payid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryId(Integer payid);

    @Insert({
        "insert into gyxg_payrecords (companyid, storeid, ",
        "customerid, payno, ",
        "paytype, paytypedesc, ",
        "payamount, paytime, ",
        "returnamount, returntime, ",
        "memo, flag, employeeid, ",
        "overflag, status, ",
        "returnno, refundno, ",
        "paystatus)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{customerid,jdbcType=INTEGER}, #{payno,jdbcType=VARCHAR}, ",
        "#{paytype,jdbcType=VARCHAR}, #{paytypedesc,jdbcType=VARCHAR}, ",
        "#{payamount,jdbcType=DECIMAL}, #{paytime,jdbcType=TIMESTAMP}, ",
        "#{returnamount,jdbcType=DECIMAL}, #{returntime,jdbcType=TIMESTAMP}, ",
        "#{memo,jdbcType=VARCHAR}, #{flag,jdbcType=VARCHAR}, #{employeeid,jdbcType=INTEGER}, ",
        "#{overflag,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR}, ",
        "#{returnno,jdbcType=VARCHAR}, #{refundno,jdbcType=VARCHAR}, ",
        "#{paystatus,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="payid", before=false, resultType=Integer.class)
    int insert1(PayRecords record);

    @Select({
        "select",
        "payid, companyid, storeid, customerid, payno, paytype, paytypedesc, payamount, ",
        "paytime, returnamount, returntime, memo, flag, employeeid, overflag, status, ",
        "returnno, refundno, paystatus",
        "from gyxg_payrecords",
        "where payid = #{payid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="payno", property="payno", jdbcType=JdbcType.VARCHAR),
        @Result(column="paytype", property="paytype", jdbcType=JdbcType.VARCHAR),
        @Result(column="paytypedesc", property="paytypedesc", jdbcType=JdbcType.VARCHAR),
        @Result(column="payamount", property="payamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="paytime", property="paytime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="returnamount", property="returnamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="returntime", property="returntime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="overflag", property="overflag", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="returnno", property="returnno", jdbcType=JdbcType.VARCHAR),
        @Result(column="refundno", property="refundno", jdbcType=JdbcType.VARCHAR),
        @Result(column="paystatus", property="paystatus", jdbcType=JdbcType.VARCHAR)
    })
    PayRecords selectByPrimaryId(Integer payid);

    /**
     * 查询收钱吧支付记录（微信，支付宝，"1,2"）
     * @param bPayRecords
     * @return
     */
    List<PayRecords> findSQBRecordsByPayType(BPayRecords bPayRecords);
    /**
     * 查询其他支付记录（除开微信，支付宝，"1,2"）
     * @param bPayRecords
     * @return
     */
    List<PayRecords> findOtherRecordsByPayType(BPayRecords bPayRecords);

    @Select({
        "select",
        "payid, companyid, storeid, customerid, payno, paytype, paytypedesc, payamount, ",
        "paytime, returnamount, returntime, memo, flag, employeeid, overflag, status, ",
        "returnno, refundno, paystatus",
        "from gyxg_payrecords"
    })
    @Results({
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="payno", property="payno", jdbcType=JdbcType.VARCHAR),
        @Result(column="paytype", property="paytype", jdbcType=JdbcType.VARCHAR),
        @Result(column="paytypedesc", property="paytypedesc", jdbcType=JdbcType.VARCHAR),
        @Result(column="payamount", property="payamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="paytime", property="paytime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="returnamount", property="returnamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="returntime", property="returntime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="overflag", property="overflag", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="returnno", property="returnno", jdbcType=JdbcType.VARCHAR),
        @Result(column="refundno", property="refundno", jdbcType=JdbcType.VARCHAR),
        @Result(column="paystatus", property="paystatus", jdbcType=JdbcType.VARCHAR)
    })
    List<PayRecords> findAll();

    @Update({
        "update gyxg_payrecords",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "customerid = #{customerid,jdbcType=INTEGER},",
          "payno = #{payno,jdbcType=VARCHAR},",
          "paytype = #{paytype,jdbcType=VARCHAR},",
          "paytypedesc = #{paytypedesc,jdbcType=VARCHAR},",
          "payamount = #{payamount,jdbcType=DECIMAL},",
          "paytime = #{paytime,jdbcType=TIMESTAMP},",
          "returnamount = #{returnamount,jdbcType=DECIMAL},",
          "returntime = #{returntime,jdbcType=TIMESTAMP},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "overflag = #{overflag,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "returnno = #{returnno,jdbcType=VARCHAR},",
          "refundno = #{refundno,jdbcType=VARCHAR},",
          "paystatus = #{paystatus,jdbcType=VARCHAR}",
        "where payid = #{payid,jdbcType=INTEGER}"
    })
    int updateByPrimaryId(PayRecords record);

    @Select("select payid,payamount from gyxg_payrecords where customerid=#{customerid} and flag='2' and overflag='1'")
    Map selectByCusId(Integer customerid);

    @SelectProvider(type = PayRecordsProvider.class,method = "selectDingjinList")
    List<Map> selectDingjinList(Map params);

    /**
     * 根据支付单号获取支付信息
     * @param payNo
     * @return
     */
    @Select({
            "select",
            "payid, companyid, storeid, customerid, payno, paytype, paytypedesc, payamount, ",
            "paytime, returnamount, returntime, memo, flag, employeeid, overflag, status, ",
            "returnno, refundno, paystatus",
            "from gyxg_payrecords",
            "where payno = #{payNo,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
            @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
            @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
            @Result(column="payno", property="payno", jdbcType=JdbcType.VARCHAR),
            @Result(column="paytype", property="paytype", jdbcType=JdbcType.VARCHAR),
            @Result(column="paytypedesc", property="paytypedesc", jdbcType=JdbcType.VARCHAR),
            @Result(column="payamount", property="payamount", jdbcType=JdbcType.DECIMAL),
            @Result(column="paytime", property="paytime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="returnamount", property="returnamount", jdbcType=JdbcType.DECIMAL),
            @Result(column="returntime", property="returntime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
            @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
            @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
            @Result(column="overflag", property="overflag", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
            @Result(column="returnno", property="returnno", jdbcType=JdbcType.VARCHAR),
            @Result(column="refundno", property="refundno", jdbcType=JdbcType.VARCHAR),
            @Result(column="paystatus", property="paystatus", jdbcType=JdbcType.VARCHAR)
    })
    PayRecords selectByPayNo(String payNo);
}