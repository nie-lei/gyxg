package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Goods;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.GoodsProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface GoodsMapper{
    @Delete({
        "delete from gyxg_goods",
        "where goodsid = #{goodsid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer goodsid);

    @Insert({
        "insert into gyxg_goods (companyid, storeid, ",
        "classifyid, code, ",
        "name, specs, unit, ",
        " pinyin, maxstock, ",
        "minstock, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{classifyid,jdbcType=INTEGER}, #{code,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{specs,jdbcType=VARCHAR}, #{unit,jdbcType=VARCHAR}, ",
        "#{pinyin,jdbcType=VARCHAR}, #{maxstock,jdbcType=DECIMAL}, ",
        "#{minstock,jdbcType=DECIMAL}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="goodsid", before=false, resultType=Integer.class)
    int insert(Goods record);

    @Select({
        "select",
        "goodsid, companyid, storeid, classifyid, code, name, specs, unit, price, pinyin, ",
        "maxstock, minstock, status",
        "from gyxg_goods",
        "where goodsid = #{goodsid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="specs", property="specs", jdbcType=JdbcType.VARCHAR),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="pinyin", property="pinyin", jdbcType=JdbcType.VARCHAR),
        @Result(column="maxstock", property="maxstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="minstock", property="minstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Goods selectByPrimaryKey(Integer goodsid);

    @Select({
        "select",
        "goodsid, companyid, storeid, classifyid, code, name, specs, unit, price, pinyin, ",
        "maxstock, minstock, status",
        "from gyxg_goods"
    })
    @Results({
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="specs", property="specs", jdbcType=JdbcType.VARCHAR),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="pinyin", property="pinyin", jdbcType=JdbcType.VARCHAR),
        @Result(column="maxstock", property="maxstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="minstock", property="minstock", jdbcType=JdbcType.DECIMAL),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Goods> selectAll();

    @Update({
        "update gyxg_goods",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "classifyid = #{classifyid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "specs = #{specs,jdbcType=VARCHAR},",
          "unit = #{unit,jdbcType=VARCHAR},",
//          "price = #{price,jdbcType=DECIMAL},",
          "pinyin = #{pinyin,jdbcType=VARCHAR},",
          "maxstock = #{maxstock,jdbcType=DECIMAL},",
          "minstock = #{minstock,jdbcType=DECIMAL},",
          "status = #{status,jdbcType=VARCHAR}",
        "where goodsid = #{goodsid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Goods record);


    @Select("select count(goodsid) from  gyxg_goods where classifyid = #{classifyid}")
    int selectCountByclassfyid(Integer classifyid);

    @Select("select count(goodsid) from gyxg_goods where companyid = #{companyid} and storeid = #{storeid} and (code = #{code} or name=#{name})")
    int checkGoods(Goods goods);

    @SelectProvider(type = GoodsProvider.class,method = "selectGoodsList")
    List<Map> selectGoodsList(Map params);

    @Select("select code as value,name label from gyxg_goods where companyid = #{companyid} and storeid = #{storeid} and classifyid = #{cid}")
    List<Map> selectGoods(Map params);
    @Select("select goodsid as value,name label,pinyin from gyxg_goods where companyid = #{companyid} and storeid = #{storeid} and classifyid = #{cid}")
    List<Map> selectGoods2(Map params);

    @Select("select * from gyxg_goods where companyid = #{companyid} and storeid = #{storeid} and code = #{code}")
    Goods selectBycode(Map params);

    @Select("select goodsid value,name label from gyxg_goods where companyid = #{companyid} and storeid = #{storeid} and pinyin like '%${pinyin}%'")
    List<Map> selectbyPinyin(Map param);


    @Select("select count(goodsid) from gyxg_goods where companyid = #{companyid} and storeid = #{storeid} and code = #{code}")
    int checkGoodsbycode(Goods goods);

    @Select("select count(goodsid) from gyxg_goods where companyid = #{companyid} and storeid = #{storeid} and name=#{name}")
    int checkGoodsbyname(Goods goods);
}