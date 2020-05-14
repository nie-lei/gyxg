package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Tables;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.OrderProvider;
import com.jzkj.gyxg.mapper.provider.TablesProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface TablesMapper {
    @Delete({
        "delete from gyxg_tables",
        "where tableid = #{tableid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer tableid);

    @Insert({
        "insert into gyxg_tables (companyid, storeid, ",
        "areaid, code, name, ",
        "seats, minmoney, ",
        "fixedfee, ratiofee, ",
        "seatfee, memo, flag, ",
        "employeeid, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{areaid,jdbcType=INTEGER}, #{code,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{seats,jdbcType=INTEGER}, #{minmoney,jdbcType=DECIMAL}, ",
        "#{fixedfee,jdbcType=DECIMAL}, #{ratiofee,jdbcType=DECIMAL}, ",
        "#{seatfee,jdbcType=DECIMAL}, #{memo,jdbcType=VARCHAR}, #{flag,jdbcType=VARCHAR}, ",
        "#{employeeid,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="tableid", before=false, resultType=Integer.class)
    int insert(Tables record);

    @Select({
        "select",
        "tableid, companyid, storeid, areaid, code, name, seats, minmoney, fixedfee, ",
        "ratiofee, seatfee, memo, flag, employeeid, status",
        "from gyxg_tables",
        "where tableid = #{tableid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="tableid", property="tableid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="areaid", property="areaid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="seats", property="seats", jdbcType=JdbcType.INTEGER),
        @Result(column="minmoney", property="minmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="fixedfee", property="fixedfee", jdbcType=JdbcType.DECIMAL),
        @Result(column="ratiofee", property="ratiofee", jdbcType=JdbcType.DECIMAL),
        @Result(column="seatfee", property="seatfee", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Tables selectByPrimaryKey(Integer tableid);

    @Select({
        "select",
        "tableid, companyid, storeid, areaid, code, name, seats, minmoney, fixedfee, ",
        "ratiofee, seatfee, memo, flag, employeeid, status",
        "from gyxg_tables"
    })
    @Results({
        @Result(column="tableid", property="tableid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="areaid", property="areaid", jdbcType=JdbcType.INTEGER),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="seats", property="seats", jdbcType=JdbcType.INTEGER),
        @Result(column="minmoney", property="minmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="fixedfee", property="fixedfee", jdbcType=JdbcType.DECIMAL),
        @Result(column="ratiofee", property="ratiofee", jdbcType=JdbcType.DECIMAL),
        @Result(column="seatfee", property="seatfee", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Tables> selectAll();

    @Update({
        "update gyxg_tables",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "areaid = #{areaid,jdbcType=INTEGER},",
          "code = #{code,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "seats = #{seats,jdbcType=INTEGER},",
          "minmoney = #{minmoney,jdbcType=DECIMAL},",
          "fixedfee = #{fixedfee,jdbcType=DECIMAL},",
          "ratiofee = #{ratiofee,jdbcType=DECIMAL},",
          "seatfee = #{seatfee,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where tableid = #{tableid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Tables record);

    @Select("select count(tableid) from gyxg_tables where companyid = #{companyid} and storeid = #{storeid} and (code = #{code} or name = #{name})")
    int checkTables(Tables tables);

    @SelectProvider(type = TablesProvider.class,method = "selectList")
    List<Map> selectList(Map params);

    @Select("select tableid,code from gyxg_tables where companyid = #{companyid} and storeid = #{storeid}")
    List<Map> selectAlltablesByCsId(Map params);

    @SelectProvider(type = OrderProvider.class,method = "selectAlltablesByPrams")
    List<Map> selectAlltablesByPrams(Map params);


    @Select("SELECT\n" +
            "\ta.orderid,\n" +
            "\ta.tableid,\n" +
            "\ta.flag,\n" +
            "\tDATE_FORMAT(a.ordertime, '%H:%i:%s') ordertime,\n" +
            "\ta.nums,\n" +
            "\ta.amountmoney\n" +
//            "\tIFNULL((SELECT SUM(amountmoney) as amountmoney  from (\n" +
//            "SELECT\n" +
//            "\tSUM(e.amount) * e.price  as amountmoney\n" +
//            "FROM\n" +
//            "\tgyxg_orders_slave e \n" +
//            "WHERE\n" +
//            "\te.flag IN ('0', '2')\n" +
//            "AND e.`status` IN ('0', '1')\n" +
//            "AND e.orderid =a.orderid GROUP BY e.disheid\n" +
//            ") t ) ,0) as amountmoney\n" +
            "FROM\n" +
            "\tgyxg_orders_master a\n" +
            "WHERE\n" +
            "\tstatus in ('0','1')")
    List<Map> selectTableStatus0();

    @Select("select tableid,code,name from gyxg_tables where tableid = #{tableid}")
    Map selectByTableId(Integer id);

    /**
     * 根据区域id获取餐桌信息
     * @param areaid
     * @return
     */
    @Select("select tableid,name,flag from gyxg_tables where areaid=#{areaid} and status=0")
    List<Map<String, Object>> selectByAreaId(int areaid);

    /**
     * 根据餐桌名获取餐桌信息
     * @param tablesname
     * @return
     */
    @Select({
            "select",
            "tableid, companyid, storeid, areaid, code, name, seats, minmoney, fixedfee, ",
            "ratiofee, seatfee, memo, flag, employeeid, status",
            "from gyxg_tables",
            "where name = #{tablesname,jdbcType=VARCHAR} and storeid=#{storeid, jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="tableid", property="tableid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
            @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
            @Result(column="areaid", property="areaid", jdbcType=JdbcType.INTEGER),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="seats", property="seats", jdbcType=JdbcType.INTEGER),
            @Result(column="minmoney", property="minmoney", jdbcType=JdbcType.DECIMAL),
            @Result(column="fixedfee", property="fixedfee", jdbcType=JdbcType.DECIMAL),
            @Result(column="ratiofee", property="ratiofee", jdbcType=JdbcType.DECIMAL),
            @Result(column="seatfee", property="seatfee", jdbcType=JdbcType.DECIMAL),
            @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
            @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
            @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Tables selectByName(@Param("tablesname") String tablesname, @Param("storeid") int storeid);

    @Select("select orderid from gyxg_orders_master " +
            "where " +
            "status in (0,1) and flag=0 and tableid=#{tablesid} " +
            "or " +
            "status in (0,1) and flag!=0 and (" +
            "tableids like '${tablesid},%' " +
            "or " +
            "tableids like '%,${tablesid},%' " +
            "or " +
            "tableids like '%,${tablesid}') " +
            "order by ordertime desc " +
            "limit 1")
    Integer selecOrderIdByTablesId(@Param("tablesid") Integer tableid);


    @Select("select count(tableid) from gyxg_tables where companyid = #{companyid} and storeid = #{storeid} and code = #{code}")
    int checkTablesbycode(Tables tables);

    @Select("select count(tableid) from gyxg_tables where companyid = #{companyid} and storeid = #{storeid} and name = #{name}")
    int checkTablesbyname(Tables tables);
}