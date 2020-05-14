package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.DishesClassfys;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface DishesClassfysMapper {
    @Delete({
        "delete from gyxg_dishe_classifys",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer classifyid);

    @Insert({
        "insert into gyxg_dishe_classifys (companyid, storeid, ",
        "name, printid, is_show, ",
        "level, parentid, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR}, #{printid,jdbcType=INTEGER}, #{isShow,jdbcType=VARCHAR}, ",
        "#{level,jdbcType=VARCHAR}, #{parentid,jdbcType=INTEGER}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="classifyid", before=false, resultType=Integer.class)
    int insert(DishesClassfys record);

    @Select({
        "select",
        "classifyid, companyid, storeid, name, printid, is_show, level, parentid, status",
        "from gyxg_dishe_classifys",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="printid", property="printid", jdbcType=JdbcType.INTEGER),
        @Result(column="is_show", property="isShow", jdbcType=JdbcType.VARCHAR),
        @Result(column="level", property="level", jdbcType=JdbcType.VARCHAR),
        @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    DishesClassfys selectByPrimaryKey(Integer classifyid);

    @Select({
        "select",
        "classifyid, companyid, storeid, name, printid, is_show, level, parentid, status",
        "from gyxg_dishe_classifys"
    })
    @Results({
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="printid", property="printid", jdbcType=JdbcType.INTEGER),
        @Result(column="is_show", property="isShow", jdbcType=JdbcType.VARCHAR),
        @Result(column="level", property="level", jdbcType=JdbcType.VARCHAR),
        @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<DishesClassfys> selectAll();

    @Update({
        "update gyxg_dishe_classifys",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "printid = #{printid,jdbcType=INTEGER},",
          "is_show = #{isShow,jdbcType=VARCHAR},",
          "level = #{level,jdbcType=VARCHAR},",
          "parentid = #{parentid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where classifyid = #{classifyid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(DishesClassfys record);



    @Select("SELECT\n" +
            "\ta.parentid,\n" +
            "\ta.`level`,\n" +
            "\ta.classifyid,\n" +
            "\ta.`name`,\n" +
            "\ta.`status`,\n" +
            "\ta.is_show isshow,\n" +
            "\ta.printid,\n" +
            "\tb.`name` AS printclassfyname,\n" +
            "\t(SELECT c.name from gyxg_dishe_classifys c where c.classifyid=a.parentid) as parentname\n" +
            "FROM\n" +
            "\tgyxg_dishe_classifys a\n" +
            "LEFT JOIN gyxg_print_classifys b ON a.printid = b.classifyid")
    List<Map> selectDishesClassfyList(Map params);

    @Select("select count(classifyid) from gyxg_dishe_classifys where companyid = #{companyid} and storeid = #{storeid} and name = #{name}")
    int checkDishesClassfy(DishesClassfys dishesClassfys);

    @Select("select classifyid value,name label from gyxg_dishe_classifys where companyid = #{companyid} and storeid = #{storeid}  and level=1")
    List<Map> selectDishesClassfy(Map params);

    @Select("select classifyid value,name label from gyxg_dishe_classifys where companyid = #{companyid} and storeid = #{storeid} and level=2 and parentid=#{pid}")
    List<Map> selectDishesClassfy2(Map params);

    @Update("update gyxg_dishe_classifys set is_show = #{isShow} where classifyid = #{classifyid}")
    int updateIsshow(DishesClassfys dishesClassfys);

    @Update("update gyxg_dishe_classifys set status = #{status} where classifyid = #{classifyid}")
    int updateStatus(DishesClassfys dishesClassfys);

    /**
     * 根据店铺id和级别获取店铺菜品分类
     * @param storeid
     * @param level
     * @return
     */
    @Select("select classifyid,name from gyxg_dishe_classifys where storeid=#{storeid} and level=#{level}")
    List<Map<String, Object>> selectListByStoreIdAndLevel(@Param("storeid") int storeid, @Param("level") int level);

    /**
     * 根据上级id获取店铺菜品分类
     * @param classifyid
     * @return
     */
    @Select("select classifyid,name from gyxg_dishe_classifys where parentid=#{classifyid}")
    List<Map<String, Object>> selectListByParentid(int classifyid);


    @Select("select classifyid,name from gyxg_dishe_classifys where companyid=#{companyid} and storeid=#{storeid} and level='2' ")
    List<Map> selecClassfys2(Map params);
}