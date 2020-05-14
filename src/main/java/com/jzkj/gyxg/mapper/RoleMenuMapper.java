package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.RoleMenu;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.RoleMenuProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuMapper {
    @Delete({
        "delete from gyxg_role_menu",
        "where roleid = #{roleid,jdbcType=INTEGER}",
          "and menuid = #{menuid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(@Param("roleid") Integer roleid, @Param("menuid") Integer menuid);

    @Insert({
        "insert into gyxg_role_menu (roleid, menuid)",
        "values (#{roleid,jdbcType=INTEGER}, #{menuid,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="roleid", before=true, resultType=Integer.class)
    int insert(RoleMenu record);

    @Select({
        "select",
        "roleid, menuid",
        "from gyxg_role_menu"
    })
    @Results({
        @Result(column="roleid", property="roleid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="menuid", property="menuid", jdbcType=JdbcType.INTEGER, id=true)
    })
    List<RoleMenu> selectAll();

    @Select("SELECT count(*) from gyxg_role_menu where roleid=#{roleid} and menuid=#{menuid};")
    Integer selectAuthUrl(@Param("menuid") Integer menuid, @Param("roleid")Integer roleid);

    @Delete("delete from gyxg_role_menu where roleid = #{roleid}")
    void deleteByRoleid(Integer roleid);

    @SelectProvider(type = RoleMenuProvider.class,method = "updateRoleMenu")
    void updateRoleMenu(Map param);
}