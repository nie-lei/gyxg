package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Suppliers;
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

public interface SuppliersMapper {
    @Delete({
        "delete from gyxg_suppliers",
        "where supplierid = #{supplierid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer supplierid);

    @Insert({
        "insert into gyxg_suppliers (companyid, storeid, ",
        "name, legalperson, ",
        "contact, phone, ",
        "fax, address, intime, ",
        "mainproducts, employeeid, ",
        "status, brief)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR}, #{legalperson,jdbcType=VARCHAR}, ",
        "#{contact,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, ",
        "#{fax,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, #{intime,jdbcType=TIMESTAMP}, ",
        "#{mainproducts,jdbcType=VARCHAR}, #{employeeid,jdbcType=INTEGER}, ",
        "#{status,jdbcType=VARCHAR}, #{brief,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="supplierid", before=false, resultType=Integer.class)
    int insert(Suppliers record);

    @Select({
        "select",
        "supplierid, companyid, storeid, name, legalperson, contact, phone, fax, address, ",
        "intime, mainproducts, employeeid, status, brief",
        "from gyxg_suppliers",
        "where supplierid = #{supplierid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="supplierid", property="supplierid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="legalperson", property="legalperson", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact", property="contact", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="fax", property="fax", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="mainproducts", property="mainproducts", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="brief", property="brief", jdbcType=JdbcType.LONGVARCHAR)
    })
    Suppliers selectByPrimaryKey(Integer supplierid);

    @Select({
        "select",
        "supplierid, companyid, storeid, name, legalperson, contact, phone, fax, address, ",
        "intime, mainproducts, employeeid, status, brief",
        "from gyxg_suppliers"
    })
    @Results({
        @Result(column="supplierid", property="supplierid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="legalperson", property="legalperson", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact", property="contact", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="fax", property="fax", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="mainproducts", property="mainproducts", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="brief", property="brief", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<Suppliers> selectAll();

    @Update({
        "update gyxg_suppliers",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "legalperson = #{legalperson,jdbcType=VARCHAR},",
          "contact = #{contact,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "fax = #{fax,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "mainproducts = #{mainproducts,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR},",
          "brief = #{brief,jdbcType=LONGVARCHAR}",
        "where supplierid = #{supplierid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Suppliers record);

    @Select("select count(supplierid) from gyxg_suppliers where name = #{name} and companyid = #{companyid} and storeid = #{storeid} and status='0'")
    int checkSuppliers(Suppliers suppliers);

    @Select("\n" +
            "SELECT a.supplierid,a.`name`,a.contact,a.legalperson,a.phone,a.fax,a.address,a.mainproducts,b.`name` as empname,a.intime,a.brief from gyxg_suppliers a LEFT JOIN gyxg_employees b on a.employeeid=b.employeeid " +
            " where a.companyid = #{companyid} and a.storeid = #{storeid} and a.status='0'")
    List<Map> selectList(Map params);

    @Select("select supplierid,name from gyxg_suppliers where companyid = #{companyid} and storeid = #{storeid} and status='0'")
    List<Map> select(Map params);
}