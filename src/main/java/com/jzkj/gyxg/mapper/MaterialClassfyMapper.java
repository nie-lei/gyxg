package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.MaterialClassfy;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.GoodsProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface MaterialClassfyMapper {
    @Delete({
        "delete from gyxg_material_classifys",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer classifyid);

    @Insert({
        "insert into gyxg_material_classifys (companyid, storeid, ",
        "code, name, level, ",
        "parentid, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{code,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{level,jdbcType=VARCHAR}, ",
        "#{parentid,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="classifyid", before=false, resultType=Integer.class)
    int insert(MaterialClassfy record);

    @Select({
        "select",
        "classifyid, companyid, storeid, code, name, level, parentid, status",
        "from gyxg_material_classifys",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="level", property="level", jdbcType=JdbcType.VARCHAR),
        @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    MaterialClassfy selectByPrimaryKey(Integer classifyid);

    @Select({
        "select",
        "classifyid, companyid, storeid, code, name, level, parentid, status",
        "from gyxg_material_classifys"
    })
    @Results({
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="level", property="level", jdbcType=JdbcType.VARCHAR),
        @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<MaterialClassfy> selectAll();

    @Update({
        "update gyxg_material_classifys",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "level = #{level,jdbcType=VARCHAR},",
          "parentid = #{parentid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(MaterialClassfy record);

    @Select("select classifyid,name from gyxg_material_classifys where companyid = #{companyid} and storeid = #{storeid} and level = #{level}")
    List<Map> selecmMaterialclassifys(Map params);

    @Select("select count(classifyid) from gyxg_material_classifys where companyid = #{companyid} and storeid = #{storeid} and (code=#{code} or name=#{name} )")
    int checkMaterialClassfy(MaterialClassfy m);

    @SelectProvider(type = GoodsProvider.class ,method = "selectMaterialClassfyList")
    List<Map> selectMaterialClassfyList(Map params);



    @Select("select classifyid value,name label from gyxg_material_classifys where companyid = #{companyid} and storeid = #{storeid} AND level=1")
    List<Map> selecmMaterialclassifys1(Map params);

    @Select("select classifyid value,name label from gyxg_material_classifys where companyid = #{companyid} and storeid = #{storeid} AND level=2 and parentid=#{pid}")
    List<Map> selecmMaterialclassifys2(Map params);



    @Select("select count(classifyid) from gyxg_material_classifys where companyid = #{companyid} and storeid = #{storeid} and code=#{code}")
    int checkMaterialClassfybycode(MaterialClassfy m);

    @Select("select count(classifyid) from gyxg_material_classifys where companyid = #{companyid} and storeid = #{storeid} and name=#{name} ")
    int checkMaterialClassfybyname(MaterialClassfy m);
}