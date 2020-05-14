package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Employeelog;
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
public interface EmployeelogMapper {
    @Delete({
        "delete from gyxg_employee_log",
        "where logid = #{logid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer logid);

    @Insert({
        "insert into gyxg_employee_log (employeeid, ip, ",
        "actiontime, memo)",
        "values (#{employeeid,jdbcType=INTEGER}, #{ip,jdbcType=VARCHAR}, ",
        "#{actiontime,jdbcType=TIMESTAMP}, #{memo,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="logid", before=false, resultType=Integer.class)
    int insert(Employeelog record);

    @Select({
        "select",
        "logid, employeeid, ip, actiontime, memo",
        "from gyxg_employee_log",
        "where logid = #{logid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="logid", property="logid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="ip", property="ip", jdbcType=JdbcType.VARCHAR),
        @Result(column="actiontime", property="actiontime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    Employeelog selectByPrimaryKey(Integer logid);

    @Select({
        "select",
        "logid, employeeid, ip, actiontime, memo",
        "from gyxg_employee_log"
    })
    @Results({
        @Result(column="logid", property="logid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="ip", property="ip", jdbcType=JdbcType.VARCHAR),
        @Result(column="actiontime", property="actiontime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR)
    })
    List<Employeelog> selectAll();

    @Update({
        "update gyxg_employee_log",
        "set employeeid = #{employeeid,jdbcType=INTEGER},",
          "ip = #{ip,jdbcType=VARCHAR},",
          "actiontime = #{actiontime,jdbcType=TIMESTAMP},",
          "memo = #{memo,jdbcType=VARCHAR}",
        "where logid = #{logid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Employeelog record);
}