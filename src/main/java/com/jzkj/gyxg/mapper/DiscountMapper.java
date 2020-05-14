package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Discount;
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

public interface DiscountMapper {
    @Delete({
        "delete from gyxg_setdiscount",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_setdiscount (companyid, storeid, ",
        "discountname, discountratio, ",
        "isall)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{discountname,jdbcType=VARCHAR}, #{discountratio,jdbcType=DECIMAL}, ",
        "#{isall,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Discount record);

    @Select({
        "select",
        "id, companyid, storeid, discountname, discountratio, isall",
        "from gyxg_setdiscount",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="discountname", property="discountname", jdbcType=JdbcType.VARCHAR),
        @Result(column="discountratio", property="discountratio", jdbcType=JdbcType.DECIMAL),
        @Result(column="isall", property="isall", jdbcType=JdbcType.VARCHAR)
    })
    Discount selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, companyid, storeid, discountname, discountratio, isall",
        "from gyxg_setdiscount"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="discountname", property="discountname", jdbcType=JdbcType.VARCHAR),
        @Result(column="discountratio", property="discountratio", jdbcType=JdbcType.DECIMAL),
        @Result(column="isall", property="isall", jdbcType=JdbcType.VARCHAR)
    })
    List<Discount> selectAll();

    @Update({
        "update gyxg_setdiscount",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "discountname = #{discountname,jdbcType=VARCHAR},",
          "discountratio = #{discountratio,jdbcType=DECIMAL},",
          "isall = #{isall,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Discount record);

    @Select("select * from gyxg_setdiscount where companyid = #{companyid} and storeid = #{storeid}")
    List<Discount> select(Map params);
}