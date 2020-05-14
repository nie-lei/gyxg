package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.PurchaseMasters;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface PurchaseMastersMapper {
    @Delete({
        "delete from gyxg_purchase_masters",
        "where purchaseid = #{purchaseid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer purchaseid);

    @Insert({
        "insert into gyxg_purchase_masters (companyid, storeid, ",
        "code, purchasedate, ",
        "supplierid, totalmoney, ",
        "departmentid, employee1id, ",
        "employee2id, employee3id, ",
        "employee5id, intime, ",
        "checktime, ",
        "memo, flag, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{code,jdbcType=VARCHAR}, #{purchasedate,jdbcType=DATE}, ",
        "#{supplierid,jdbcType=INTEGER}, #{totalmoney,jdbcType=DECIMAL}, ",
        "#{departmentid,jdbcType=INTEGER}, #{employee1id,jdbcType=INTEGER}, ",
        "#{employee2id,jdbcType=INTEGER}, #{employee3id,jdbcType=INTEGER}, ",
        "#{employee5id,jdbcType=INTEGER}, #{intime,jdbcType=TIMESTAMP}, ",
        "#{checktime,jdbcType=TIMESTAMP}, ",
        "#{memo,jdbcType=VARCHAR}, #{flag,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="purchaseid", before=false, resultType=Integer.class)
    int insert(PurchaseMasters record);

    @Select({
        "select",
        "purchaseid, companyid, storeid, code, purchasedate, supplierid, totalmoney, ",
        "departmentid, employee1id, employee2id, employee3id, employee5id, intime, checktime, ",
        "is_pay, memo, flag, status",
        "from gyxg_purchase_masters",
        "where purchaseid = #{purchaseid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="purchaseid", property="purchaseid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="purchasedate", property="purchasedate", jdbcType=JdbcType.DATE),
        @Result(column="supplierid", property="supplierid", jdbcType=JdbcType.INTEGER),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee5id", property="employee5id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_pay", property="isPay", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    PurchaseMasters selectByPrimaryKey(Integer purchaseid);

    @Select({
        "select",
        "purchaseid, companyid, storeid, code, purchasedate, supplierid, totalmoney, ",
        "departmentid, employee1id, employee2id, employee3id, employee5id, intime, checktime, ",
        "is_pay, memo, flag, status",
        "from gyxg_purchase_masters"
    })
    @Results({
        @Result(column="purchaseid", property="purchaseid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="purchasedate", property="purchasedate", jdbcType=JdbcType.DATE),
        @Result(column="supplierid", property="supplierid", jdbcType=JdbcType.INTEGER),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee2id", property="employee2id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee3id", property="employee3id", jdbcType=JdbcType.INTEGER),
        @Result(column="employee5id", property="employee5id", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="checktime", property="checktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_pay", property="isPay", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<PurchaseMasters> selectAll();

    @Update({
        "update gyxg_purchase_masters",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "purchasedate = #{purchasedate,jdbcType=DATE},",
          "supplierid = #{supplierid,jdbcType=INTEGER},",
          "totalmoney = #{totalmoney,jdbcType=DECIMAL},",
          "departmentid = #{departmentid,jdbcType=INTEGER},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "employee2id = #{employee2id,jdbcType=INTEGER},",
          "employee3id = #{employee3id,jdbcType=INTEGER},",
          "employee5id = #{employee5id,jdbcType=INTEGER},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "checktime = #{checktime,jdbcType=TIMESTAMP},",
          "is_pay = #{isPay,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where purchaseid = #{purchaseid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(PurchaseMasters record);
}