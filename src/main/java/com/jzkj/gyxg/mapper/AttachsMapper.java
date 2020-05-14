package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Attachs;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface AttachsMapper {
    @Delete({
        "delete from gyxg_attachs",
        "where attachid = #{attachid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer attachid);

    @Insert({
        "insert into gyxg_attachs (companyid, storeid, ",
        "name, flag, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR}, #{flag,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="attachid", before=false, resultType=Integer.class)
    int insert(Attachs record);

    @Select({
        "select",
        "attachid, companyid, storeid, name, flag, status",
        "from gyxg_attachs",
        "where attachid = #{attachid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="attachid", property="attachid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Attachs selectByPrimaryKey(Integer attachid);

    @Select({
        "select",
        "attachid, companyid, storeid, name, flag, status",
        "from gyxg_attachs"
    })
    @Results({
        @Result(column="attachid", property="attachid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Attachs> selectAll();

    @Update({
        "update gyxg_attachs",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where attachid = #{attachid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Attachs record);

    @Select("SELECT\n" +
            "\tflag,\n" +
            "\tattachid,\n" +
            "\tCASE\n" +
            "WHEN flag = '2' THEN\n" +
            "\t'通知厨房'\n" +
            "WHEN flag = '3' THEN\n" +
            "\t'退菜原因'\n" +
            "WHEN flag = '4' THEN\n" +
            "\t'赠菜原因'\n" +
            "END AS flagname ,name\n" +
            "FROM\n" +
            "\tgyxg_attachs  where companyid = #{companyid} and storeid = #{storeid} and flag in ('2','3','4')")
    List<Map> selectList(Map params);

    @Select("SELECT\n" +
            "\tflag,\n" +
            "\tattachid,\n" +
            "\tCASE\n" +
            "WHEN flag = '0' THEN\n" +
            "\t'仓库单位'\n" +
            "END AS flagname ,name\n" +
            "FROM\n" +
            "\tgyxg_attachs  where companyid = #{companyid} and storeid = #{storeid} and flag=#{flag}")
    List<Map> selectList0(Map params);
    @Select("SELECT\n" +
            "\tflag,\n" +
            "\tattachid,\n" +
            "\tCASE\n" +
            "WHEN flag = '1' THEN\n" +
            "\t'菜品单位'\n" +
            "END AS flagname ,name\n" +
            "FROM\n" +
            "\tgyxg_attachs  where companyid = #{companyid} and storeid = #{storeid} and flag=#{flag}")
    List<Map> selectList1(Map params);
    @Select(" select count(attachid) from gyxg_attachs where companyid = #{companyid} and storeid = #{storeid} and flag=#{flag} and name=#{name}")
    int checkAttachs(Attachs attachs);

    @Select("select attachid,name from gyxg_attachs where companyid = #{companyid} and storeid = #{storeid} and flag=#{flag}")
    List<Map> selectAttachs(Map params);

    @Select("select attachid,name from gyxg_attachs where companyid = #{companyid} and storeid = #{storeid} and flag in ('2','3','4')")
    List<Map> selectAttachs2(Map params);

    /**
     * 根据类型获取辅助信息
     * @param flag
     * @param storeid
     * @return
     */
    @Select("select name from gyxg_attachs where storeid=#{storeid} and flag=#{flag}")
    List<Map<String, Object>> selectByFlag(@Param("flag") String flag, @Param("storeid") int storeid);
}