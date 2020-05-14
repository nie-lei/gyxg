package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Stores;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface StoresMapper {
    @Delete({
        "delete from gyxg_stores",
        "where storeid = #{storeid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer storeid);

    @Insert({
        "insert into gyxg_stores (companyid, storeno, ",
        "cname, ename, address, ",
        "telephone, fax, ",
        "opentime, status, ",
        "ycjz, yczs, fjtc, ",
        "fyjzs, fyjtc, fjhyh, ",
        "kxgcs, diczje, dcxfje, ",
        "phones, vendor_sn, ",
        "vendor_key, appid, ",
        "seatmoney, servicer_flag)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeno,jdbcType=VARCHAR}, ",
        "#{cname,jdbcType=VARCHAR}, #{ename,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{telephone,jdbcType=VARCHAR}, #{fax,jdbcType=VARCHAR}, ",
        "#{opentime,jdbcType=TIMESTAMP}, #{status,jdbcType=VARCHAR}, ",
        "#{ycjz,jdbcType=VARCHAR}, #{yczs,jdbcType=VARCHAR}, #{fjtc,jdbcType=VARCHAR}, ",
        "#{fyjzs,jdbcType=VARCHAR}, #{fyjtc,jdbcType=VARCHAR}, #{fjhyh,jdbcType=VARCHAR}, ",
        "#{kxgcs,jdbcType=INTEGER}, #{diczje,jdbcType=DECIMAL}, #{dcxfje,jdbcType=DECIMAL}, ",
        "#{phones,jdbcType=VARCHAR}, #{vendor_sn,jdbcType=VARCHAR}, ",
        "#{vendor_key,jdbcType=VARCHAR}, #{appid,jdbcType=VARCHAR}, ",
        "#{seatmoney,jdbcType=DECIMAL}, #{servicer_flag,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="storeid", before=false, resultType=Integer.class)
    int insert(Stores record);

    @Select({
        "select",
        "storeid, companyid, storeno, cname, ename, address, telephone, fax, opentime, ",
        "status, ycjz, yczs, fjtc, fyjzs, fyjtc, fjhyh, kxgcs, diczje, dcxfje, phones, ",
        "vendor_sn, vendor_key, appid, seatmoney, servicer_flag",
        "from gyxg_stores",
        "where storeid = #{storeid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeno", property="storeno", jdbcType=JdbcType.VARCHAR),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ename", property="ename", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="telephone", property="telephone", jdbcType=JdbcType.VARCHAR),
        @Result(column="fax", property="fax", jdbcType=JdbcType.VARCHAR),
        @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="ycjz", property="ycjz", jdbcType=JdbcType.VARCHAR),
        @Result(column="yczs", property="yczs", jdbcType=JdbcType.VARCHAR),
        @Result(column="fjtc", property="fjtc", jdbcType=JdbcType.VARCHAR),
        @Result(column="fyjzs", property="fyjzs", jdbcType=JdbcType.VARCHAR),
        @Result(column="fyjtc", property="fyjtc", jdbcType=JdbcType.VARCHAR),
        @Result(column="fjhyh", property="fjhyh", jdbcType=JdbcType.VARCHAR),
        @Result(column="kxgcs", property="kxgcs", jdbcType=JdbcType.INTEGER),
        @Result(column="diczje", property="diczje", jdbcType=JdbcType.DECIMAL),
        @Result(column="dcxfje", property="dcxfje", jdbcType=JdbcType.DECIMAL),
        @Result(column="phones", property="phones", jdbcType=JdbcType.VARCHAR),
        @Result(column="vendor_sn", property="vendor_sn", jdbcType=JdbcType.VARCHAR),
        @Result(column="vendor_key", property="vendor_key", jdbcType=JdbcType.VARCHAR),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="seatmoney", property="seatmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="servicer_flag", property="servicer_flag", jdbcType=JdbcType.VARCHAR)
    })
    Stores selectByPrimaryKey(Integer storeid);

    @Select({
        "select",
        "storeid, companyid, storeno, cname, ename, address, telephone, fax, opentime, ",
        "status, ycjz, yczs, fjtc, fyjzs, fyjtc, fjhyh, kxgcs, diczje, dcxfje, phones, ",
        "vendor_sn, vendor_key, appid, seatmoney, servicer_flag",
        "from gyxg_stores"
    })
    @Results({
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeno", property="storeno", jdbcType=JdbcType.VARCHAR),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ename", property="ename", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="telephone", property="telephone", jdbcType=JdbcType.VARCHAR),
        @Result(column="fax", property="fax", jdbcType=JdbcType.VARCHAR),
        @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="ycjz", property="ycjz", jdbcType=JdbcType.VARCHAR),
        @Result(column="yczs", property="yczs", jdbcType=JdbcType.VARCHAR),
        @Result(column="fjtc", property="fjtc", jdbcType=JdbcType.VARCHAR),
        @Result(column="fyjzs", property="fyjzs", jdbcType=JdbcType.VARCHAR),
        @Result(column="fyjtc", property="fyjtc", jdbcType=JdbcType.VARCHAR),
        @Result(column="fjhyh", property="fjhyh", jdbcType=JdbcType.VARCHAR),
        @Result(column="kxgcs", property="kxgcs", jdbcType=JdbcType.INTEGER),
        @Result(column="diczje", property="diczje", jdbcType=JdbcType.DECIMAL),
        @Result(column="dcxfje", property="dcxfje", jdbcType=JdbcType.DECIMAL),
        @Result(column="phones", property="phones", jdbcType=JdbcType.VARCHAR),
        @Result(column="vendor_sn", property="vendor_sn", jdbcType=JdbcType.VARCHAR),
        @Result(column="vendor_key", property="vendor_key", jdbcType=JdbcType.VARCHAR),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="seatmoney", property="seatmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="servicer_flag", property="servicer_flag", jdbcType=JdbcType.VARCHAR)
    })
    List<Stores> selectAll();

    @Update({
        "update gyxg_stores",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeno = #{storeno,jdbcType=VARCHAR},",
          "cname = #{cname,jdbcType=VARCHAR},",
          "ename = #{ename,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "telephone = #{telephone,jdbcType=VARCHAR},",
          "fax = #{fax,jdbcType=VARCHAR},",
          "opentime = #{opentime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=VARCHAR},",
          "ycjz = #{ycjz,jdbcType=VARCHAR},",
          "yczs = #{yczs,jdbcType=VARCHAR},",
          "fjtc = #{fjtc,jdbcType=VARCHAR},",
          "fyjzs = #{fyjzs,jdbcType=VARCHAR},",
          "fyjtc = #{fyjtc,jdbcType=VARCHAR},",
          "fjhyh = #{fjhyh,jdbcType=VARCHAR},",
          "kxgcs = #{kxgcs,jdbcType=INTEGER},",
          "diczje = #{diczje,jdbcType=DECIMAL},",
          "dcxfje = #{dcxfje,jdbcType=DECIMAL},",
          "phones = #{phones,jdbcType=VARCHAR},",
          "vendor_sn = #{vendor_sn,jdbcType=VARCHAR},",
          "vendor_key = #{vendor_key,jdbcType=VARCHAR},",
          "appid = #{appid,jdbcType=VARCHAR},",
          "seatmoney = #{seatmoney,jdbcType=DECIMAL},",
          "servicer_flag = #{servicer_flag,jdbcType=VARCHAR}",
        "where storeid = #{storeid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Stores record);

    @Select({
            "select",
            "storeid, companyid, storeno, cname, ename, address, telephone, fax, opentime, ",
            "status, ycjz, yczs, fjtc, fyjzs, fyjtc, fjhyh, kxgcs, diczje, dcxfje, phones, ",
            "vendor_sn, vendor_key, appid, seatmoney, servicer_flag",
            "from gyxg_stores",
            "where storeid = #{storeid} and companyid=#{companyid}"
    })
    @Results({
            @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
            @Result(column="storeno", property="storeno", jdbcType=JdbcType.VARCHAR),
            @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
            @Result(column="ename", property="ename", jdbcType=JdbcType.VARCHAR),
            @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
            @Result(column="telephone", property="telephone", jdbcType=JdbcType.VARCHAR),
            @Result(column="fax", property="fax", jdbcType=JdbcType.VARCHAR),
            @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
            @Result(column="ycjz", property="ycjz", jdbcType=JdbcType.VARCHAR),
            @Result(column="yczs", property="yczs", jdbcType=JdbcType.VARCHAR),
            @Result(column="fjtc", property="fjtc", jdbcType=JdbcType.VARCHAR),
            @Result(column="fyjzs", property="fyjzs", jdbcType=JdbcType.VARCHAR),
            @Result(column="fyjtc", property="fyjtc", jdbcType=JdbcType.VARCHAR),
            @Result(column="fjhyh", property="fjhyh", jdbcType=JdbcType.VARCHAR),
            @Result(column="kxgcs", property="kxgcs", jdbcType=JdbcType.INTEGER),
            @Result(column="diczje", property="diczje", jdbcType=JdbcType.DECIMAL),
            @Result(column="dcxfje", property="dcxfje", jdbcType=JdbcType.DECIMAL),
            @Result(column="phones", property="phones", jdbcType=JdbcType.VARCHAR),
            @Result(column="vendor_sn", property="vendor_sn", jdbcType=JdbcType.VARCHAR),
            @Result(column="vendor_key", property="vendor_key", jdbcType=JdbcType.VARCHAR),
            @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
            @Result(column="seatmoney", property="seatmoney", jdbcType=JdbcType.DECIMAL),
            @Result(column="servicer_flag", property="servicer_flag", jdbcType=JdbcType.VARCHAR)
    })
    Stores selectByPrimaryKey1(@Param("storeid") Integer storeid,@Param("companyid") Integer companyid);



    @Select({
            "select",
            " ycjz, yczs, fjtc, fyjzs, fyjtc, fjhyh, kxgcs, diczje, dcxfje, phones, ",
            "vendor_sn, vendor_key, appid, seatmoney, servicer_flag",
            " from gyxg_stores",
            " where storeid = #{storeid} and companyid=#{companyid}"
    })
    Stores selectByPrimaryKey2(@Param("storeid") Integer storeid, @Param("companyid") Integer companyid);

    @Select("select storeid,cname from gyxg_stores")
    List<Map<String, Object>> getStores();
}