package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Departments;
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

public interface DepartmentsMapper {
    @Delete({
        "delete from gyxg_departments",
        "where departmentid = #{departmentid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer departmentid);

    @Insert({
        "insert into gyxg_departments (companyid, storeid, ",
        "departmentno, name, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{departmentno,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="departmentid", before=false, resultType=Integer.class)
    int insert(Departments record);

    @Select({
        "select",
        "departmentid, companyid, storeid, departmentno, name, status",
        "from gyxg_departments",
        "where departmentid = #{departmentid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="departmentno", property="departmentno", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Departments selectByPrimaryKey(Integer departmentid);

    @Select({
        "select",
        "departmentid, companyid, storeid, departmentno, name, status",
        "from gyxg_departments"
    })
    @Results({
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="departmentno", property="departmentno", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Departments> selectAll();

    @Update({
        "update gyxg_departments",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "departmentno = #{departmentno,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where departmentid = #{departmentid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Departments record);

    @Select("select departmentid,departmentno,name,status from gyxg_departments where companyid=#{companyid} and storeid=#{storeid} ")
    List<Map> selectList(Map params);

    @Select("select count(departmentid) from gyxg_departments where companyid=#{companyid} and storeid=#{storeid} and (departmentno=#{departmentno} or name=#{name})")
    int checkDepartment(Departments departments);

    @Select("select count(employeeid) from gyxg_employees where departmentid=#{departmentid}")
    int selectEmpCountByDeptid(Integer departmentid);

    @Select("select departmentid,name from gyxg_departments where companyid=#{companyid} and storeid=#{storeid}")
    List<Map> selectDept(Map params);

    @Select("select count(departmentid) from gyxg_departments where companyid=#{companyid} and storeid=#{storeid} and departmentno=#{departmentno}")
    int checkDepartmentbycode(Departments departments);

    @Select("select count(departmentid) from gyxg_departments where companyid=#{companyid} and storeid=#{storeid} and name=#{name}")
    int checkDepartmentbyname(Departments departments);
}