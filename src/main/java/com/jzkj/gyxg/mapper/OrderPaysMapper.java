package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.OrderPays;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

public interface OrderPaysMapper extends Mapper<OrderPays>  {
    @Delete({
        "delete from gyxg_order_pays",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryId(Integer id);

    @Insert({
        "insert into gyxg_order_pays (orderid, payid, ",
        "oktime, status)",
        "values (#{orderid,jdbcType=INTEGER}, #{payid,jdbcType=INTEGER}, ",
        "#{oktime,jdbcType=TIMESTAMP}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert1(OrderPays record);

    @Select({
        "select",
        "id, orderid, payid, oktime, status",
        "from gyxg_order_pays",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER),
        @Result(column="oktime", property="oktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    OrderPays selectByPrimaryId(Integer id);

    @Select({
        "select",
        "id, orderid, payid, oktime, status",
        "from gyxg_order_pays"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER),
        @Result(column="oktime", property="oktime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<OrderPays> findAll();

    @Update({
        "update gyxg_order_pays",
        "set orderid = #{orderid,jdbcType=INTEGER},",
          "payid = #{payid,jdbcType=INTEGER},",
          "oktime = #{oktime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryId(OrderPays record);

    @Select({
            "select",
            "id, orderid, payid, oktime, status",
            "from gyxg_order_pays where orderid=#{orderid}"
    })
    List<OrderPays> selectByOrderid(Integer orderid);

    @Select({
            "select",
            "id, orderid, payid, oktime, status",
            "from gyxg_order_pays",
            "where payid = #{payid,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
            @Result(column="payid", property="payid", jdbcType=JdbcType.INTEGER),
            @Result(column="oktime", property="oktime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    OrderPays selectByPayId(Integer payid);
}