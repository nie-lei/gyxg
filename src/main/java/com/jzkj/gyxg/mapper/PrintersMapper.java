package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Printers;
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
import org.springframework.stereotype.Service;

public interface PrintersMapper {
    @Delete({
        "delete from gyxg_printers",
        "where printerid = #{printerid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer printerid);

    @Insert({
        "insert into gyxg_printers (companyid, storeid, ",
        "printercode, name, ",
        "classifyid, areaid, ",
        "printerip, memo, ",
        "num, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{printercode,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{classifyid,jdbcType=INTEGER}, #{areaid,jdbcType=INTEGER}, ",
        "#{printerip,jdbcType=VARCHAR}, #{memo,jdbcType=VARCHAR}, ",
        "#{num,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="printerid", before=false, resultType=Integer.class)
    int insert(Printers record);

    @Select({
        "select",
        "printerid, companyid, storeid, printercode, name, classifyid, areaid, printerip, ",
        "memo, num, status",
        "from gyxg_printers",
        "where printerid = #{printerid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="printerid", property="printerid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="printercode", property="printercode", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER),
        @Result(column="areaid", property="areaid", jdbcType=JdbcType.INTEGER),
        @Result(column="printerip", property="printerip", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="num", property="num", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Printers selectByPrimaryKey(Integer printerid);

    @Select({
        "select",
        "printerid, companyid, storeid, printercode, name, classifyid, areaid, printerip, ",
        "memo, num, status",
        "from gyxg_printers"
    })
    @Results({
        @Result(column="printerid", property="printerid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="printercode", property="printercode", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="classifyid", property="classifyid", jdbcType=JdbcType.INTEGER),
        @Result(column="areaid", property="areaid", jdbcType=JdbcType.INTEGER),
        @Result(column="printerip", property="printerip", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="num", property="num", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Printers> selectAll();

    @Update({
        "update gyxg_printers",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "printercode = #{printercode,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "classifyid = #{classifyid,jdbcType=INTEGER},",
          "areaid = #{areaid,jdbcType=INTEGER},",
          "printerip = #{printerip,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "num = #{num,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where printerid = #{printerid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Printers record);


    @Select("SELECT\n" +
            "\ta.printerid,\n" +
            "\ta.printercode,\n" +
            "\ta.classifyid,\n" +
            "\tb.`name` as classfyname,\n" +
            "\ta.areaid,\n" +
            "\tc.`name` as areaname,\n" +
            "a.`name`,\n" +
            "\ta.printerip,\n" +
            "\ta.num,\n" +
            "\ta.memo,a.status\n" +
            "FROM\n" +
            "\tgyxg_printers a\n" +
            "LEFT JOIN gyxg_print_classifys b ON a.classifyid = b.classifyid\n" +
            "LEFT JOIN gyxg_areas c ON a.areaid = c.areaid ")
    List<Map> selectPrinterList(Map params);

    @Select("select count(printerid) from gyxg_printers where storeid = #{storeid} and companyid = #{companyid} and (printercode=#{printercode} or name = #{name} )")
    int checkPrinter(Printers printers);

    @Select("select count(printerid) from gyxg_printers where classifyid=#{classifyid}")
    Integer selectByClassfyid(Integer classifyid);
    @Select("select count(printerid) from gyxg_printers where storeid = #{storeid} and companyid = #{companyid} and printercode=#{printercode}")
    int checkPrinterbycode(Printers printers);

    @Select("select count(printerid) from gyxg_printers where storeid = #{storeid} and companyid = #{companyid} and name=#{name}")
    int checkPrinterbyname(Printers printers);
}