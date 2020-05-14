package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.OrderFlag2Records;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface OrderFlag2RecordsMapper {
    @Delete({
        "delete from gyxg_orderflag2_records",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_orderflag2_records (orderid, otherorderid)",
        "values (#{orderid,jdbcType=INTEGER}, #{otherorderid,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(OrderFlag2Records record);

    @Select({
        "select",
        "id, orderid, otherorderid",
        "from gyxg_orderflag2_records",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="otherorderid", property="otherorderid", jdbcType=JdbcType.VARCHAR)
    })
    OrderFlag2Records selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, orderid, otherorderid",
        "from gyxg_orderflag2_records"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="otherorderid", property="otherorderid", jdbcType=JdbcType.VARCHAR)
    })
    List<OrderFlag2Records> selectAll();

    @Update({
        "update gyxg_orderflag2_records",
        "set orderid = #{orderid,jdbcType=INTEGER},",
          "otherorderid = #{otherorderid,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(OrderFlag2Records record);

    @Select({
            "select",
            "id, orderid, otherorderid",
            "from gyxg_orderflag2_records where orderid=#{orderid}"
    })
    OrderFlag2Records selectByOrderid(@Param("orderid") Integer orderid);

    @Select("select otherorderid from gyxg_orderflag2_records where orderid=#{orderid}")
    String selectByOrderid1(Integer orderid);

    @Select("select count(0) from gyxg_orderflag2_records where orderid=#{orderid}")
    int selectCountByOrderid(Integer orderid);
}