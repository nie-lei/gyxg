package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.CheckSlaves;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.CheckProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface CheckSlavesMapper {
    @Delete({
        "delete from gyxg_checks_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer slaveid);

    @Insert({
        "insert into gyxg_checks_slaves (checkid, goodsid, ",
        "stock, checkstock, ",
        "usednums, usedmoney, ",
        "memo)",
        "values (#{checkid,jdbcType=INTEGER}, #{goodsid,jdbcType=INTEGER}, ",
        "#{stock,jdbcType=DECIMAL}, #{checkstock,jdbcType=DECIMAL}, ",
        "#{usednums,jdbcType=DECIMAL}, #{usedmoney,jdbcType=DECIMAL}, ",
        "#{memo,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="slaveid", before=false, resultType=Integer.class)
    int insert(CheckSlaves record);

    @Select({
        "select",
        "slaveid, checkid, goodsid, stock, checkstock, usednums, usedmoney, memo",
        "from gyxg_checks_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="checkid", property="checkid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="stock", property="stock", jdbcType=JdbcType.DECIMAL),
        @Result(column="checkstock", property="checkstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="usednums", property="usednums", jdbcType=JdbcType.DECIMAL),
        @Result(column="usedmoney", property="usedmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    CheckSlaves selectByPrimaryKey(Integer slaveid);

    @Select({
        "select",
        "slaveid, checkid, goodsid, stock, checkstock, usednums, usedmoney, memo",
        "from gyxg_checks_slaves"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="checkid", property="checkid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="stock", property="stock", jdbcType=JdbcType.DECIMAL),
        @Result(column="checkstock", property="checkstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="usednums", property="usednums", jdbcType=JdbcType.DECIMAL),
        @Result(column="usedmoney", property="usedmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    List<CheckSlaves> selectAll();

    @Update({
        "update gyxg_checks_slaves",
        "set checkid = #{checkid,jdbcType=INTEGER},",
          "goodsid = #{goodsid,jdbcType=INTEGER},",
          "stock = #{stock,jdbcType=DECIMAL},",
          "checkstock = #{checkstock,jdbcType=DECIMAL},",
          "usednums = #{usednums,jdbcType=DECIMAL},",
          "usedmoney = #{usedmoney,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR}",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(CheckSlaves record);

    @SelectProvider(type = CheckProvider.class,method = "selectSlaverList")
    List<Map> selectSlaverList(Integer checkid);

    @Select("select c.goodsid,c.checkstock,IFNULL(g.ycstock,0) as ycstock from" +
            " gyxg_checks_slaves c LEFT JOIN gyxg_goods_stock g ON c.goodsid=g.goodsid  " +
            "where c.checkid = #{checkid}")
    List<Map> selectBycheckid(Integer checkid);

    @Select("select count(0) from gyxg_checks_slaves where checkid = #{checkid} and goodsid = #{goodsid}")
    int check(@Param("goodsid") Integer goodsid, @Param("checkid") Integer checkid);

    @Select("select goodsid from gyxg_checks_slaves where checkid = #{checkid}")
    List<Integer> selectgoodsidBycheckid(Integer integer);
}