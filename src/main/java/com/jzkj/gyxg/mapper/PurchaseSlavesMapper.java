package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.PurchaseSlaves;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.GoodsProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

public interface PurchaseSlavesMapper extends Mapper<PurchaseSlaves> {
    @Delete({
        "delete from gyxg_purchase_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryId(Integer slaveid);

    @Insert({
        "insert into gyxg_purchase_slaves (purchaseid, goodsid, ",
        "amount, unit, price, ",
        "totalmoney, memo)",
        "values (#{purchaseid,jdbcType=INTEGER}, #{goodsid,jdbcType=INTEGER}, ",
        "#{amount,jdbcType=DECIMAL}, #{unit,jdbcType=VARCHAR}, #{price,jdbcType=DECIMAL}, ",
        "#{totalmoney,jdbcType=DECIMAL}, #{memo,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="slaveid", before=false, resultType=Integer.class)
    int insert(PurchaseSlaves record);

    @Select({
        "select",
        "slaveid, purchaseid, goodsid, amount, unit, price, totalmoney, memo",
        "from gyxg_purchase_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="purchaseid", property="purchaseid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    PurchaseSlaves selectByPrimaryId(Integer slaveid);

    @Select({
        "select",
        "slaveid, purchaseid, goodsid, amount, unit, price, totalmoney, memo",
        "from gyxg_purchase_slaves"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="purchaseid", property="purchaseid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    List<PurchaseSlaves> selectAll();

    @Update({
        "update gyxg_purchase_slaves",
        "set purchaseid = #{purchaseid,jdbcType=INTEGER},",
          "goodsid = #{goodsid,jdbcType=INTEGER},",
          "amount = #{amount,jdbcType=DECIMAL},",
          "unit = #{unit,jdbcType=VARCHAR},",
          "price = #{price,jdbcType=DECIMAL},",
          "totalmoney = #{totalmoney,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR}",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(PurchaseSlaves record);

    @Select("SELECT\n" +
            "\ta.slaveid,a.purchaseid,a.goodsid,a.memo,b.`code`,b.`name`,b.specs,b.unit,a.amount,a.price,a.totalmoney\n" +
            "FROM\n" +
            "\tgyxg_purchase_slaves a\n" +
            "LEFT JOIN gyxg_goods b ON a.goodsid = b.goodsid where purchaseid=#{purchaseid}")
    List<Map> selectList(Map params);

    @Select("select a.goodsid,a.amount,b.name,a.unit,a.totalmoney from gyxg_purchase_slaves a left join gyxg_goods b on a.goodsid=b.goodsid where purchaseid=#{purchaseid}")
    List<Map> selectByPurchaseid(Integer purchaseid);

    @SelectProvider(type = GoodsProvider.class,method = "goodStockList")
    List<Map> goodStockList(Map params);

    @Select("select count(0) from gyxg_purchase_slaves where purchaseid=#{purchaseid} and goodsid=#{goodsid}")
    int check(PurchaseSlaves slaves);

    List<PurchaseSlaves> findByPurchaseIdAndGoodsId(PurchaseSlaves purchaseSlaves);
}