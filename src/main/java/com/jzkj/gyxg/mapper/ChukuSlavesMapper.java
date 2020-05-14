package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.ChukuSlaves;
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

public interface ChukuSlavesMapper {
    @Delete({
        "delete from gyxg_chuku_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer slaveid);

    @Insert({
        "insert into gyxg_chuku_slaves (chukuid, goodsid, ",
        "amount, unit, price, ",
        "totalmoney, memo)",
        "values (#{chukuid,jdbcType=INTEGER}, #{goodsid,jdbcType=INTEGER}, ",
        "#{amount,jdbcType=DECIMAL}, #{unit,jdbcType=VARCHAR}, #{price,jdbcType=DECIMAL}, ",
        "#{totalmoney,jdbcType=DECIMAL}, #{memo,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="slaveid", before=false, resultType=Integer.class)
    int insert(ChukuSlaves record);

    @Select({
        "select",
        "slaveid, chukuid, goodsid, amount, unit, price, totalmoney, memo",
        "from gyxg_chuku_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="chukuid", property="chukuid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    ChukuSlaves selectByPrimaryKey(Integer slaveid);

    @Select({
        "select",
        "slaveid, chukuid, goodsid, amount, unit, price, totalmoney, memo",
        "from gyxg_chuku_slaves"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="chukuid", property="chukuid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    List<ChukuSlaves> selectAll();

    @Update({
        "update gyxg_chuku_slaves",
        "set chukuid = #{chukuid,jdbcType=INTEGER},",
          "goodsid = #{goodsid,jdbcType=INTEGER},",
          "amount = #{amount,jdbcType=DECIMAL},",
          "unit = #{unit,jdbcType=VARCHAR},",
          "price = #{price,jdbcType=DECIMAL},",
          "totalmoney = #{totalmoney,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR}",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ChukuSlaves record);

    @Select("select a.goodsid,a.amount,b.name from gyxg_chuku_slaves a left join gyxg_goods b on a.goodsid=b.goodsid where a.chukuid = #{chukuid}")
    List<Map> selectByChukuid(Integer chukuid);

    @Select("SELECT a.slaveid,b.goodsid,a.memo,a.chukuid,b.`code`,b.`name`,b.specs,a.unit,a.amount,a.price,a.totalmoney from gyxg_chuku_slaves a LEFT JOIN gyxg_goods b on a.goodsid=b.goodsid where a.chukuid=#{chukuid}\n")
    List<Map> selectList(Map params);

    @Select("select count(0) from gyxg_chuku_slaves where chukuid = #{chukuid} and goodsid = #{goodsid}")
    int check(ChukuSlaves slaves);
}