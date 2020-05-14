package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Goods;
import com.jzkj.gyxg.entity.GoodsStock;
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
import tk.mybatis.mapper.common.Mapper;

public interface GoodsStockMapper /*extends Mapper<GoodsStock>*/ {

    Map findGoodsStockByGoodsId(Integer goodsid);

    @Delete({
        "delete from gyxg_goods_stock",
        "where goodsstockid = #{goodsstockid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer goodsstockid);

    @Insert({
        "insert into gyxg_goods_stock (companyid, storeid, ",
        "goodsid, warehouseid, ",
        "unit, ycstock,stock, lasttime,stockamount)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{goodsid,jdbcType=INTEGER}, #{warehouseid,jdbcType=INTEGER}, ",
        "#{unit,jdbcType=INTEGER}, #{ycstock,jdbcType=DECIMAL},#{stock,jdbcType=DECIMAL}, #{lasttime,jdbcType=TIMESTAMP},#{stockamount,jdbcType=DECIMAL})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="goodsstockid", before=false, resultType=Integer.class)
    int insert(GoodsStock record);

    @Select({
        "select",
        "goodsstockid, companyid, storeid, goodsid, warehouseid, unit, ycstock,stock, lasttime",
        "from gyxg_goods_stock",
        "where goodsstockid = #{goodsstockid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="goodsstockid", property="goodsstockid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="unit", property="unit", jdbcType=JdbcType.INTEGER),
            @Result(column="ycstock", property="ycstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="stock", property="stock", jdbcType=JdbcType.DECIMAL),
        @Result(column="lasttime", property="lasttime", jdbcType=JdbcType.TIMESTAMP)
    })
    GoodsStock selectByPrimaryKey(Integer goodsstockid);

    @Select({
        "select",
        "goodsstockid, companyid, storeid, goodsid, warehouseid, unit, ycstock,stock, lasttime",
        "from gyxg_goods_stock"
    })
    @Results({
        @Result(column="goodsstockid", property="goodsstockid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER),
        @Result(column="unit", property="unit", jdbcType=JdbcType.INTEGER),
            @Result(column="ycstock", property="ycstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="stock", property="stock", jdbcType=JdbcType.DECIMAL),
        @Result(column="lasttime", property="lasttime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<GoodsStock> selectAll();

    /*@Update({
        "update gyxg_goods_stock",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "goodsid = #{goodsid,jdbcType=INTEGER},",
          "warehouseid = #{warehouseid,jdbcType=INTEGER},",
          "unit = #{unit,jdbcType=INTEGER},",
            "ycstock = #{ycstock,jdbcType=DECIMAL},",
          "stock = #{stock,jdbcType=DECIMAL},",
          "stockamount = #{stockamount,jdbcType=DECIMAL},",
          "lasttime = #{lasttime,jdbcType=TIMESTAMP}",
        "where goodsstockid = #{goodsstockid,jdbcType=INTEGER}"
    })*/
    int updateByPrimaryKey(GoodsStock record);

    @Select("select count(goodsstockid) from gyxg_goods_stock where warehouseid = #{warehouseid}")
    int selectCountByWarehouseid(Integer warehouseid);


    @Select("select count(goodsstockid) from gyxg_goods_stock where goodsid = #{goodsid}")
    int selectCountByGoodsid(Integer goodsid);


    @Select("select * from gyxg_goods_stock where goodsid = #{goodsid}")
    GoodsStock selectByGoodsid(Map goods);


    @Select("select * from gyxg_goods_stock where goodsid = #{goodsid}")
    GoodsStock selectByGoodsid1(Goods goods);


    @Select("select * from gyxg_goods_stock where  goodsid = #{goodsid}")
    GoodsStock selectBygoodsid(Integer goodsid);
}