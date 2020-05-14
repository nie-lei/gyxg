package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Areas;
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

public interface AreasMapper {
    @Delete({
        "delete from gyxg_areas",
        "where areaid = #{areaid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer areaid);

    @Insert({
        "insert into gyxg_areas (companyid, storeid, ",
        "name, employeeid, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR}, #{employeeid,jdbcType=INTEGER}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="areaid", before=false, resultType=Integer.class)
    int insert(Areas record);

    @Select({
        "select",
        "areaid, companyid, storeid, name, employeeid, status",
        "from gyxg_areas",
        "where areaid = #{areaid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="areaid", property="areaid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Areas selectByPrimaryKey(Integer areaid);

    @Select({
        "select",
        "areaid, companyid, storeid, name, employeeid, status",
        "from gyxg_areas"
    })
    @Results({
        @Result(column="areaid", property="areaid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Areas> selectAll();

    @Update({
        "update gyxg_areas",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where areaid = #{areaid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Areas record);

    @Select("select a.areaid,a.name,a.status,b.`name` as empname,a.employeeid from gyxg_areas a LEFT JOIN gyxg_employees b on a.employeeid=b.employeeid where a.storeid = #{storeid} and a.companyid = #{companyid}")
    List<Map> selectList(Map params);

    @Select("select count(areaid) from gyxg_areas where storeid = #{storeid} and companyid = #{companyid} and name=#{name}")
    int checkAreas(Areas areas);

    @Select("select areaid,name from gyxg_areas where storeid = #{storeid} and companyid = #{companyid}")
    List<Map> selectRoles(Map params);

    @Select("select count(areaid) from gyxg_tables where areaid=#{areaid}")
    int selectTableCountByAreaid(Integer areaid);

    @Select("select areaid,name from gyxg_areas where storeid = #{storeid} and companyid = #{companyid}")
    List<Map> selectAreaInfo(Map params);

    @Select("select areaid,name from gyxg_areas where storeid = #{storeid} and companyid = #{companyid} and status='0'")
    List<Map> selectAllSelect(Map params);

    /**
     * 根据店铺id获取就餐区域信息
     * @param storeid
     * @return
     */
    @Select("select areaid,name from gyxg_areas where storeid=#{storeid} and status=0")
    List<Map<String, Object>> selectByStoreId(int storeid);
}