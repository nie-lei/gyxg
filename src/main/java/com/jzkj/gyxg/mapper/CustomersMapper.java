package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Customers;
import java.util.List;
import java.util.Map;
import java.util.spi.CurrencyNameProvider;

import com.jzkj.gyxg.mapper.provider.CustomersProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface CustomersMapper {
    @Delete({
        "delete from gyxg_customers",
        "where customerid = #{customerid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer customerid);

    @Insert({
        "insert into gyxg_customers (name, phone, ",
        "sex, birthday, nation, ",
        "portrait, card, ",
        "company, address, ",
        "love, avoid, weixinid, ",
        "registertime, is_vip, ",
        "is_sign, status,passwd)",
        "values (#{name,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, ",
        "#{sex,jdbcType=VARCHAR}, #{birthday,jdbcType=VARCHAR}, #{nation,jdbcType=VARCHAR}, ",
        "#{portrait,jdbcType=VARCHAR}, #{card,jdbcType=VARCHAR}, ",
        "#{company,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{love,jdbcType=VARCHAR}, #{avoid,jdbcType=VARCHAR}, #{weixinid,jdbcType=VARCHAR}, ",
        "#{registertime,jdbcType=TIMESTAMP}, #{isVip,jdbcType=VARCHAR}, ",
        "#{isSign,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR},#{passwd,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="customerid", before=false, resultType=Integer.class)
    int insert(Customers record);

    @Select({
        "select",
        "customerid, name, phone,passwd,sex, birthday, nation, portrait, card, company, address, ",
        "love, avoid, weixinid, registertime, is_vip, is_sign, status",
        "from gyxg_customers",
        "where customerid = #{customerid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
        @Result(column="birthday", property="birthday", jdbcType=JdbcType.VARCHAR),
        @Result(column="nation", property="nation", jdbcType=JdbcType.VARCHAR),
        @Result(column="portrait", property="portrait", jdbcType=JdbcType.VARCHAR),
        @Result(column="card", property="card", jdbcType=JdbcType.VARCHAR),
        @Result(column="company", property="company", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="love", property="love", jdbcType=JdbcType.VARCHAR),
        @Result(column="avoid", property="avoid", jdbcType=JdbcType.VARCHAR),
        @Result(column="weixinid", property="weixinid", jdbcType=JdbcType.VARCHAR),
        @Result(column="registertime", property="registertime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_vip", property="isVip", jdbcType=JdbcType.VARCHAR),
            @Result(column="passwd", property="passwd", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_sign", property="isSign", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Customers selectByPrimaryKey(Integer customerid);

    @Select({
        "select",
        "customerid, name, phone, sex, birthday, nation, portrait, card, company, address, ",
        "love, avoid, weixinid, registertime, is_vip, is_sign, status",
        "from gyxg_customers"
    })
    @Results({
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
        @Result(column="birthday", property="birthday", jdbcType=JdbcType.VARCHAR),
        @Result(column="nation", property="nation", jdbcType=JdbcType.VARCHAR),
        @Result(column="portrait", property="portrait", jdbcType=JdbcType.VARCHAR),
        @Result(column="card", property="card", jdbcType=JdbcType.VARCHAR),
        @Result(column="company", property="company", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="love", property="love", jdbcType=JdbcType.VARCHAR),
        @Result(column="avoid", property="avoid", jdbcType=JdbcType.VARCHAR),
        @Result(column="weixinid", property="weixinid", jdbcType=JdbcType.VARCHAR),
        @Result(column="registertime", property="registertime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="passwd", property="passwd", jdbcType=JdbcType.VARCHAR),
            @Result(column="is_vip", property="isVip", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_sign", property="isSign", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Customers> selectAll();

    @Update({
        "update gyxg_customers",
        "set name = #{name,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "sex = #{sex,jdbcType=VARCHAR},",
          "birthday = #{birthday,jdbcType=VARCHAR},",
          "nation = #{nation,jdbcType=VARCHAR},",
          "portrait = #{portrait,jdbcType=VARCHAR},",
          "card = #{card,jdbcType=VARCHAR},",
          "company = #{company,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "love = #{love,jdbcType=VARCHAR},",
          "avoid = #{avoid,jdbcType=VARCHAR},",
          "weixinid = #{weixinid,jdbcType=VARCHAR},",
          "registertime = #{registertime,jdbcType=TIMESTAMP},",
            "passwd = #{passwd,jdbcType=VARCHAR},",
          "is_vip = #{isVip,jdbcType=VARCHAR},",
          "is_sign = #{isSign,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where customerid = #{customerid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Customers record);

    @SelectProvider(type = CustomersProvider.class,method = "selectList")
    List<Map> selectList(Map params);

    @Update("update gyxg_customers set is_vip = '1' where customerid = #{customerid}")
    int updateCusIsvip(Integer customerid);


    @Select("select count(customerid) from gyxg_customers where phone = #{phone}")
    int selectByPhone(String phone);


    @Select({
            "select",
            "customerid, name, phone, sex, birthday, nation, portrait, card, company, address, ",
            "love, avoid, weixinid, registertime, is_vip, is_sign, status",
            "from gyxg_customers",
            "where phone = #{phone,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
            @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
            @Result(column="birthday", property="birthday", jdbcType=JdbcType.VARCHAR),
            @Result(column="nation", property="nation", jdbcType=JdbcType.VARCHAR),
            @Result(column="portrait", property="portrait", jdbcType=JdbcType.VARCHAR),
            @Result(column="card", property="card", jdbcType=JdbcType.VARCHAR),
            @Result(column="company", property="company", jdbcType=JdbcType.VARCHAR),
            @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
            @Result(column="love", property="love", jdbcType=JdbcType.VARCHAR),
            @Result(column="avoid", property="avoid", jdbcType=JdbcType.VARCHAR),
            @Result(column="weixinid", property="weixinid", jdbcType=JdbcType.VARCHAR),
            @Result(column="registertime", property="registertime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="is_vip", property="isVip", jdbcType=JdbcType.VARCHAR),
            @Result(column="is_sign", property="isSign", jdbcType=JdbcType.VARCHAR),
            @Result(column="passwd", property="passwd", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Customers selectCusByPhone(String phone);

    /**
     * 根据微信id获取客户信息
     * @param openId
     * @return
     */
    @Select({
            "select",
            "customerid, name, phone, sex, birthday, nation, portrait, card, company, address, ",
            "love, avoid, weixinid, registertime, is_vip, is_sign, status",
            "from gyxg_customers",
            "where weixinid = #{openId,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
            @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
            @Result(column="birthday", property="birthday", jdbcType=JdbcType.VARCHAR),
            @Result(column="nation", property="nation", jdbcType=JdbcType.VARCHAR),
            @Result(column="portrait", property="portrait", jdbcType=JdbcType.VARCHAR),
            @Result(column="card", property="card", jdbcType=JdbcType.VARCHAR),
            @Result(column="company", property="company", jdbcType=JdbcType.VARCHAR),
            @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
            @Result(column="love", property="love", jdbcType=JdbcType.VARCHAR),
            @Result(column="avoid", property="avoid", jdbcType=JdbcType.VARCHAR),
            @Result(column="weixinid", property="weixinid", jdbcType=JdbcType.VARCHAR),
            @Result(column="registertime", property="registertime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="is_vip", property="isVip", jdbcType=JdbcType.VARCHAR),
            @Result(column="is_sign", property="isSign", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Customers selectByWeixinId(String openId);

}