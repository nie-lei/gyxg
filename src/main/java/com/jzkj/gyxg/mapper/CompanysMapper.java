package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Companys;
import java.util.List;
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
public interface CompanysMapper {
    @Delete({
        "delete from gyxg_companys",
        "where companyid = #{companyid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer companyid);

    @Insert({
        "insert into gyxg_companys (cname, ename, ",
        "taxno, address, ",
        "openbank, accountno, ",
        "telephone, fax, ",
        "logo, opentime, ",
        "status)",
        "values (#{cname,jdbcType=VARCHAR}, #{ename,jdbcType=VARCHAR}, ",
        "#{taxno,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{openbank,jdbcType=VARCHAR}, #{accountno,jdbcType=VARCHAR}, ",
        "#{telephone,jdbcType=VARCHAR}, #{fax,jdbcType=VARCHAR}, ",
        "#{logo,jdbcType=VARCHAR}, #{opentime,jdbcType=TIMESTAMP}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="companyid", before=false, resultType=Integer.class)
    int insert(Companys record);

    @Select({
        "select",
        "companyid, cname, ename, taxno, address, openbank, accountno, telephone, fax, ",
        "logo, opentime, status",
        "from gyxg_companys",
        "where companyid = #{companyid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ename", property="ename", jdbcType=JdbcType.VARCHAR),
        @Result(column="taxno", property="taxno", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="openbank", property="openbank", jdbcType=JdbcType.VARCHAR),
        @Result(column="accountno", property="accountno", jdbcType=JdbcType.VARCHAR),
        @Result(column="telephone", property="telephone", jdbcType=JdbcType.VARCHAR),
        @Result(column="fax", property="fax", jdbcType=JdbcType.VARCHAR),
        @Result(column="logo", property="logo", jdbcType=JdbcType.VARCHAR),
        @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Companys selectByPrimaryKey(Integer companyid);

    @Select({
        "select",
        "companyid, cname, ename, taxno, address, openbank, accountno, telephone, fax, ",
        "logo, opentime, status",
        "from gyxg_companys"
    })
    @Results({
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="cname", property="cname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ename", property="ename", jdbcType=JdbcType.VARCHAR),
        @Result(column="taxno", property="taxno", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="openbank", property="openbank", jdbcType=JdbcType.VARCHAR),
        @Result(column="accountno", property="accountno", jdbcType=JdbcType.VARCHAR),
        @Result(column="telephone", property="telephone", jdbcType=JdbcType.VARCHAR),
        @Result(column="fax", property="fax", jdbcType=JdbcType.VARCHAR),
        @Result(column="logo", property="logo", jdbcType=JdbcType.VARCHAR),
        @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Companys> selectAll();

    @Update({
        "update gyxg_companys",
        "set cname = #{cname,jdbcType=VARCHAR},",
          "ename = #{ename,jdbcType=VARCHAR},",
          "taxno = #{taxno,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "openbank = #{openbank,jdbcType=VARCHAR},",
          "accountno = #{accountno,jdbcType=VARCHAR},",
          "telephone = #{telephone,jdbcType=VARCHAR},",
          "fax = #{fax,jdbcType=VARCHAR},",
          "logo = #{logo,jdbcType=VARCHAR},",
          "opentime = #{opentime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=VARCHAR}",
        "where companyid = #{companyid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Companys record);
}