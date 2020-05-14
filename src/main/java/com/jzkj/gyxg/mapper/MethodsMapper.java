package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Methods;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.DishesInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface MethodsMapper {
    @Delete({
        "delete from gyxg_dishe_methods",
        "where methodid = #{methodid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer methodid);

    @Insert({
        "insert into gyxg_dishe_methods (companyid, storeid, ",
        "dishesid, name, ",
        "attachmoney, attachratio, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{dishesid,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{attachmoney,jdbcType=DECIMAL}, #{attachratio,jdbcType=DECIMAL}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="methodid", before=false, resultType=Integer.class)
    int insert(Methods record);

    @Select({
        "select",
        "methodid, companyid, storeid, dishesid, name, attachmoney, attachratio, status",
        "from gyxg_dishe_methods",
        "where methodid = #{methodid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="dishesid", property="dishesid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="attachmoney", property="attachmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="attachratio", property="attachratio", jdbcType=JdbcType.DECIMAL),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Methods selectByPrimaryKey(Integer methodid);

    @Select({
        "select",
        "methodid, companyid, storeid, dishesid, name, attachmoney, attachratio, status",
        "from gyxg_dishe_methods"
    })
    @Results({
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="dishesid", property="dishesid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="attachmoney", property="attachmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="attachratio", property="attachratio", jdbcType=JdbcType.DECIMAL),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Methods> selectAll();

    @Update({
        "update gyxg_dishe_methods",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "dishesid = #{dishesid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "attachmoney = #{attachmoney,jdbcType=DECIMAL},",
          "attachratio = #{attachratio,jdbcType=DECIMAL},",
          "status = #{status,jdbcType=VARCHAR}",
        "where methodid = #{methodid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Methods record);

    @SelectProvider(type = DishesInfo.class,method = "selectMethodsList")
    List<Map> selectMethodsList(Map params);

    @Select(" select count(methodid) from gyxg_dishe_methods where (name = #{name}) and companyid = #{companyid} and storeid = #{storeid}")
    int checkMethods(Methods methods);

    @Select("select methodid,name from gyxg_dishe_methods where companyid = #{companyid} and storeid = #{storeid}")
    List<Map> selectDishesMethods(Map params);

    /**
     * 根据菜品id获取制作方法
     * @param disheid
     * @return
     */
    @Select("select methodid,name from gyxg_dishe_methods where dishesid=#{disheid} and status=0")
    List<Map<String, Object>> selectByDisheid(String disheid);

    @Select({
            "select",
            "methodid, companyid, storeid, dishesid, name, attachmoney, attachratio, status",
            "from gyxg_dishe_methods",
            "where dishesid = #{dishesid}"
    })
    @Results({
            @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
            @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
            @Result(column="dishesid", property="dishesid", jdbcType=JdbcType.INTEGER),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="attachmoney", property="attachmoney", jdbcType=JdbcType.DECIMAL),
            @Result(column="attachratio", property="attachratio", jdbcType=JdbcType.DECIMAL),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Methods> selectAllByMid(Integer disheid);
}