package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Dishes;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.DishesInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface DishesMapper {
    @Delete({
        "delete from gyxg_dishes",
        "where disheid = #{disheid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer disheid);

    @Insert({
        "insert into gyxg_dishes (companyid, storeid, ",
        "dishesid, is_goods, ",
        "code, name, pinyin, ",
        "specs, methodid, ",
        "unit, price, amount, ",
        "words, pic, intime, ",
        "flag_amoun, flag_guqing, ",
        "is_auto, memo, status, ",
        "state)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{dishesid,jdbcType=INTEGER}, #{isGoods,jdbcType=VARCHAR}, ",
        "#{code,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{pinyin,jdbcType=VARCHAR}, ",
        "#{specs,jdbcType=VARCHAR}, #{methodid,jdbcType=INTEGER}, ",
        "#{unit,jdbcType=VARCHAR}, #{price,jdbcType=DECIMAL}, #{amount,jdbcType=DECIMAL}, ",
        "#{words,jdbcType=VARCHAR}, #{pic,jdbcType=VARCHAR}, #{intime,jdbcType=TIMESTAMP}, ",
        "#{flagAmoun,jdbcType=VARCHAR}, #{flagGuqing,jdbcType=VARCHAR}, ",
        "#{isAuto,jdbcType=VARCHAR}, #{memo,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR}, ",
        "#{state,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="disheid", before=false, resultType=Integer.class)
    int insert(Dishes record);

    @Select({
        "select",
        "disheid, companyid, storeid, dishesid, is_goods, code, name, pinyin, specs, ",
        "methodid, unit, price, amount, words, pic, intime, flag_amoun, flag_guqing, ",
        "is_auto, memo, status, state",
        "from gyxg_dishes",
        "where disheid = #{disheid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="dishesid", property="dishesid", jdbcType=JdbcType.INTEGER),
        @Result(column="is_goods", property="isGoods", jdbcType=JdbcType.VARCHAR),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="pinyin", property="pinyin", jdbcType=JdbcType.VARCHAR),
        @Result(column="specs", property="specs", jdbcType=JdbcType.VARCHAR),
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="words", property="words", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic", property="pic", jdbcType=JdbcType.VARCHAR),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="flag_amoun", property="flagAmoun", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag_guqing", property="flagGuqing", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_auto", property="isAuto", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR)
    })
    Dishes selectByPrimaryKey(Integer disheid);

    @Select({
        "select",
        "disheid, companyid, storeid, dishesid, is_goods, code, name, pinyin, specs, ",
        "methodid, unit, price, amount, words, pic, intime, flag_amoun, flag_guqing, ",
        "is_auto, memo, status, state",
        "from gyxg_dishes"
    })
    @Results({
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="dishesid", property="dishesid", jdbcType=JdbcType.INTEGER),
        @Result(column="is_goods", property="isGoods", jdbcType=JdbcType.VARCHAR),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="pinyin", property="pinyin", jdbcType=JdbcType.VARCHAR),
        @Result(column="specs", property="specs", jdbcType=JdbcType.VARCHAR),
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="words", property="words", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic", property="pic", jdbcType=JdbcType.VARCHAR),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="flag_amoun", property="flagAmoun", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag_guqing", property="flagGuqing", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_auto", property="isAuto", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR)
    })
    List<Dishes> selectAll();

    @Update({
        "update gyxg_dishes",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "dishesid = #{dishesid,jdbcType=INTEGER},",
          "is_goods = #{isGoods,jdbcType=VARCHAR},",
          "code = #{code,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "pinyin = #{pinyin,jdbcType=VARCHAR},",
          "specs = #{specs,jdbcType=VARCHAR},",
          "methodid = #{methodid,jdbcType=INTEGER},",
          "unit = #{unit,jdbcType=VARCHAR},",
          "price = #{price,jdbcType=DECIMAL},",
          "amount = #{amount,jdbcType=DECIMAL},",
          "words = #{words,jdbcType=VARCHAR},",
          "pic = #{pic,jdbcType=VARCHAR},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "flag_amoun = #{flagAmoun,jdbcType=VARCHAR},",
          "flag_guqing = #{flagGuqing,jdbcType=VARCHAR},",
          "is_auto = #{isAuto,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "state = #{state,jdbcType=VARCHAR}",
        "where disheid = #{disheid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Dishes record);


    @Select("select count(disheid) from gyxg_dishes where dishesid=#{classifyid}")
    int selectCountByClassfy(Integer classifyid);

    @SelectProvider(type = DishesInfo.class, method = "selectList")
    List<Map> selectList(Map params);

    @Select("select count(disheid) from gyxg_dishes where companyid = #{companyid} and storeid = #{storeid} and  (code=#{code} or name=#{name})")
    int checkDishes(Dishes dishes);

    @SelectProvider(type = DishesInfo.class, method = "selectListGuqing")
    List<Map> selectListGuqing(Map params);

    @Select("select disheid , name from gyxg_dishes where companyid = #{companyid} and storeid = #{storeid} ")
    List<Map> selectAllidname(Map params);

    /**
     * 根据多个id获取菜品信息
     * @param msg
     * @return
     */
    @Select("select d.disheid,d.name,d.specs,d.unit,d.price,d.pic,d.flag_guqing,d.status,d.methodid," +
            "d.amount,d.flag_amoun flagAmoun,ifnull(m.name, '') methodname " +
            "from gyxg_dishes d " +
            "left join gyxg_dishe_methods m on d.methodid=m.methodid " +
            "${msg}")
    List<Map<String, Object>> selectListByDisheIds(@Param("msg") String msg);

    @Select("SELECT disheid,name,price,pic,unit,flag_guqing,'0' as status,1 as amount1,0 as flag,'' as memo from gyxg_dishes where " +
            " companyid = #{companyid} and storeid = #{storeid} and dishesid=#{classifyid} and status='0'")
    List<Map> selectByClassifyId(Map params);

    /**
     * 根据点菜id获取点菜菜品信息
     * @param orderid
     * @return
     */
    @Select("select s.id,s.disheid,s.name,s.price,s.memo,d.status,ifnull(s.methodid, 0) methodid,s.amount samount,ifnull(d.pic, '') pic," +
            "d.unit,d.flag_guqing,s.status state,s.flag " +
            "from gyxg_orders_slave s " +
            "left join gyxg_dishes d on d.disheid=s.disheid " +
            "where s.orderid=#{orderid} and s.flag in ('0','2') order by s.flag")
    List<Map<String, Object>> selectSlaveListByOrderid(Integer orderid);

    /**
     * 根据店铺id获取热门搜索
     * @param storeid
     * @return
     */
    @Select("select o.name from view_order_slave o " +
            "left join gyxg_dishes d on o.disheid=d.disheid " +
            "where d.storeid=#{storeid} " +
            "order by count desc LIMIT 0,10")
    List<Map<String, Object>> selectHotDishesByStoreId(int storeid);

    /**
     * 根据菜品分类id获取菜品列表信息
     * @param classifyId
     * @return
     */
    @Select("select disheid,name,specs,unit,price,pic,flag_guqing,status from gyxg_dishes where dishesid=#{classifyId}")
    List<Map<String, Object>> selectListByClassIfyId(int classifyId);

    @Select("SELECT disheid,name,price,pic,unit,pinyin,flag_guqing,'0' as status,1 as amount1,0 as flag,'' as memo   from gyxg_dishes where " +
            " companyid = #{companyid} and storeid = #{storeid} and (pinyin like '%${pinyin}%' OR name like '%${pinyin}%') and status='0'")
    List<Map> selectdishesBypinyin(Map param);

    @Select("select count(disheid) from gyxg_dishes where companyid = #{companyid} and storeid = #{storeid} and code=#{code}")
    int checkDishescode(Dishes dishes);

    @Select("select count(disheid) from gyxg_dishes where companyid = #{companyid} and storeid = #{storeid} and name=#{name}")
    int checkDishesname(Dishes dishes);
}