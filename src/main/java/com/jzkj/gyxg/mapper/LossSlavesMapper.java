package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.LossSlaves;
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

public interface LossSlavesMapper {
    @Delete({
        "delete from gyxg_loss_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer slaveid);

    @Insert({
        "insert into gyxg_loss_slaves (lossid, goodsid, ",
        "lossnums, lossmoney, ",
        "memo)",
        "values (#{lossid,jdbcType=INTEGER}, #{goodsid,jdbcType=INTEGER}, ",
        "#{lossnums,jdbcType=DECIMAL}, #{lossmoney,jdbcType=DECIMAL}, ",
        "#{memo,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="slaveid", before=false, resultType=Integer.class)
    int insert(LossSlaves record);

    @Select({
        "select",
        "slaveid, lossid, goodsid, lossnums, lossmoney, memo",
        "from gyxg_loss_slaves",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="lossid", property="lossid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="lossnums", property="lossnums", jdbcType=JdbcType.DECIMAL),
        @Result(column="lossmoney", property="lossmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    LossSlaves selectByPrimaryKey(Integer slaveid);

    @Select({
        "select",
        "slaveid, lossid, goodsid, lossnums, lossmoney, memo",
        "from gyxg_loss_slaves"
    })
    @Results({
        @Result(column="slaveid", property="slaveid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="lossid", property="lossid", jdbcType=JdbcType.INTEGER),
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER),
        @Result(column="lossnums", property="lossnums", jdbcType=JdbcType.DECIMAL),
        @Result(column="lossmoney", property="lossmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    List<LossSlaves> selectAll();

    @Update({
        "update gyxg_loss_slaves",
        "set lossid = #{lossid,jdbcType=INTEGER},",
          "goodsid = #{goodsid,jdbcType=INTEGER},",
          "lossnums = #{lossnums,jdbcType=DECIMAL},",
          "lossmoney = #{lossmoney,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR}",
        "where slaveid = #{slaveid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(LossSlaves record);

    @Select("SELECT\n" +
            "\ta.slaveid,\n" +
            "\ta.goodsid,\n" +
            "\tb.`code`,\n" +
            "\tb.`name`,\n" +
            "\tb.specs,\n" +
            "\tb.unit,\n" +
            "\tb.price,\n" +
            "\ta.lossnums,\n" +
            "\ta.lossmoney,\n" +
            "\ta.memo\n" +
            "FROM\n" +
            "\tgyxg_loss_slaves a\n" +
            "LEFT JOIN gyxg_goods b ON a.goodsid = b.goodsid where a.lossid=#{lossid}")
    List<Map> selectSlavesList(Integer lossid);

    @Select("select goodsid,lossnums from gyxg_loss_slaves where lossid=#{lossid}")
    List<Map> selectByLossid(Integer lossid);
}