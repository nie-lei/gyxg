package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Menu;
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
import org.springframework.stereotype.Repository;

@Repository
public interface MenuMapper {
    @Delete({
        "delete from gyxg_menu",
        "where menuid = #{menuid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer menuid);

    @Insert({
        "insert into gyxg_menu (name, url, ",
        "parentid, icon, ",
        "level)",
        "values (#{name,jdbcType=VARCHAR}, #{url,jdbcType=VARCHAR}, ",
        "#{parentid,jdbcType=INTEGER}, #{icon,jdbcType=VARCHAR}, ",
        "#{level,jdbcType=TINYINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="menuid", before=false, resultType=Integer.class)
    int insert(Menu record);

    @Select({
        "select",
        "menuid, name, url, parentid, icon, level",
        "from gyxg_menu",
        "where menuid = #{menuid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="menuid", property="menuid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR),
        @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER),
        @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
        @Result(column="level", property="level", jdbcType=JdbcType.TINYINT)
    })
    Menu selectByPrimaryKey(Integer menuid);

    @Select({
        "select",
        "menuid, name, url, parentid, icon, level",
        "from gyxg_menu"
    })
    @Results({
        @Result(column="menuid", property="menuid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR),
        @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER),
        @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
        @Result(column="level", property="level", jdbcType=JdbcType.TINYINT)
    })
    List<Menu> selectAll();

    @Update({
        "update gyxg_menu",
        "set name = #{name,jdbcType=VARCHAR},",
          "url = #{url,jdbcType=VARCHAR},",
          "parentid = #{parentid,jdbcType=INTEGER},",
          "icon = #{icon,jdbcType=VARCHAR},",
          "level = #{level,jdbcType=TINYINT}",
        "where menuid = #{menuid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Menu record);

    @Select({
            "select",
            "menuid, name, url, parentid, icon, level ",
            " from gyxg_menus",
            "where url = #{requestUrl}"
    })
    @Results({
            @Result(column="menuid", property="menuid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR),
            @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER),
            @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
            @Result(column="level", property="level", jdbcType=JdbcType.TINYINT)
    })
    List<Menu> selectByUrl(String requestUrl);

    @Select("SELECT a.* from gyxg_menus a LEFT JOIN gyxg_role_menu b on a.menuid=b.menuid where a.`level` in (1,2) and b.roleid=#{roleid}")
    List<Menu> selectByRoleid(Integer roleid);


    @Select("select * from gyxg_menus")
    List<Map> searchMenuAll();

    @Select("SELECT menuid id,name label from gyxg_menus where `level`=-1; ")
    Map searchMenuAll0();

    @Select("SELECT menuid id,name label,parentid pid from gyxg_menus where `level`=1; ")
    List<Map> searchMenuAll1();
    @Select("SELECT menuid id,name label,parentid pid from gyxg_menus where `level`=2; ")
    List<Map> searchMenuAll2();
    @Select("SELECT menuid id,name label,parentid pid from gyxg_menus where `level`=3; ")
    List<Map> searchMenuAll3();

    @Select("SELECT a.menuid from gyxg_role_menu a left join gyxg_menus b on a.menuid=b.menuid where a.roleid=#{roleid} and b.level=3")
    List<Integer> selectMenuidByRoleid(Integer roleid);
}