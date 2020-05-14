package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Warehouses;
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

public interface WarehousesMapper {
    @Delete({
        "delete from gyxg_warehouses",
        "where warehouseid = #{warehouseid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer warehouseid);

    @Insert({
        "insert into gyxg_warehouses (warehousecode, companyid, ",
        "storeid, name, memo, ",
        "employeeid, status)",
        "values (#{warehousecode,jdbcType=VARCHAR}, #{companyid,jdbcType=INTEGER}, ",
        "#{storeid,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{memo,jdbcType=VARCHAR}, ",
        "#{employeeid,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="warehouseid", before=false, resultType=Integer.class)
    int insert(Warehouses record);

    @Select({
        "select",
        "warehouseid, warehousecode, companyid, storeid, name, memo, employeeid, status",
        "from gyxg_warehouses",
        "where warehouseid = #{warehouseid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="warehousecode", property="warehousecode", jdbcType=JdbcType.VARCHAR),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Warehouses selectByPrimaryKey(Integer warehouseid);

    @Select({
        "select",
        "warehouseid, warehousecode, companyid, storeid, name, memo, employeeid, status",
        "from gyxg_warehouses"
    })
    @Results({
        @Result(column="warehouseid", property="warehouseid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="warehousecode", property="warehousecode", jdbcType=JdbcType.VARCHAR),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Warehouses> selectAll();

    @Update({
        "update gyxg_warehouses",
        "set warehousecode = #{warehousecode,jdbcType=VARCHAR},",
          "companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where warehouseid = #{warehouseid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Warehouses record);

    @Select("select count(warehouseid) from gyxg_warehouses where companyid = #{companyid} and storeid = #{storeid} and (name = #{name} or warehousecode = #{warehousecode})")
    int checkWarehouse(Warehouses warehouses);

    @Select("select warehouseid,name from gyxg_warehouses where companyid = #{companyid} and storeid = #{storeid}")
    List<Map> select(Map params);

    @Select("SELECT a.warehouseid,\n" +
            "\ta.warehousecode," +
            " a.employeeid,\n" +
            "\ta.name,\n" +
            "\ta.memo,\n" +
            "CASE WHEN a.`status`='0' THEN '启用'\n" +
            "ELSE '禁用' END as statusname,\n" +
            "\ta.`status`,\n" +
            "\tb.`name` AS empname\n" +
            "FROM\n" +
            "\tgyxg_warehouses a\n" +
            "LEFT JOIN gyxg_employees b ON a.employeeid = b.employeeid where a.companyid = #{companyid} and a.storeid = #{storeid}")
    List<Map> selectList(Map params);

    @Select("select count(warehouseid) from gyxg_warehouses where companyid = #{companyid} and storeid = #{storeid} and name = #{name}")
    int checkWarehousebyname(Warehouses warehouses);

    @Select("select count(warehouseid) from gyxg_warehouses where companyid = #{companyid} and storeid = #{storeid} and warehousecode = #{warehousecode}")
    int checkWarehousebycode(Warehouses warehouses);
}