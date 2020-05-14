package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Role;
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
public interface RoleMapper {
    @Delete({
        "delete from gyxg_role",
        "where roleid = #{roleid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer roleid);

    @Insert({
        "insert into gyxg_role (companyid, storeid, ",
        "roleno, name, description, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{roleno,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="roleid", before=false, resultType=Integer.class)
    int insert(Role record);

    @Select({
        "select",
        "roleid, companyid, storeid, roleno, name, description, status",
        "from gyxg_role",
        "where roleid = #{roleid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="roleid", property="roleid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="roleno", property="roleno", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Role selectByPrimaryKey(Integer roleid);

    @Select({
        "select",
        "roleid, companyid, storeid, roleno, name, description, status",
        "from gyxg_role"
    })
    @Results({
        @Result(column="roleid", property="roleid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="roleno", property="roleno", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Role> selectAll();

    @Update({
        "update gyxg_role",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "roleno = #{roleno,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where roleid = #{roleid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Role record);
}