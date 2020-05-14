package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Payinfo;
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
public interface PayinfoMapper {
    @Delete({
        "delete from gyxg_payinfo",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_payinfo (terminal_sn, terminal_key, ",
        "api_domain, addtime)",
        "values (#{terminalSn,jdbcType=VARCHAR}, #{terminalKey,jdbcType=VARCHAR}, ",
        "#{apiDomain,jdbcType=VARCHAR}, #{addtime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Payinfo record);

    @Select({
        "select",
        "id, terminal_sn, terminal_key, api_domain, addtime",
        "from gyxg_payinfo",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="terminal_sn", property="terminalSn", jdbcType=JdbcType.VARCHAR),
        @Result(column="terminal_key", property="terminalKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="api_domain", property="apiDomain", jdbcType=JdbcType.VARCHAR),
        @Result(column="addtime", property="addtime", jdbcType=JdbcType.TIMESTAMP)
    })
    Payinfo selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, terminal_sn, terminal_key, api_domain, addtime",
        "from gyxg_payinfo"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="terminal_sn", property="terminalSn", jdbcType=JdbcType.VARCHAR),
        @Result(column="terminal_key", property="terminalKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="api_domain", property="apiDomain", jdbcType=JdbcType.VARCHAR),
        @Result(column="addtime", property="addtime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Payinfo> selectAll();

    @Update({
        "update gyxg_payinfo",
        "set terminal_sn = #{terminalSn,jdbcType=VARCHAR},",
          "terminal_key = #{terminalKey,jdbcType=VARCHAR},",
          "api_domain = #{apiDomain,jdbcType=VARCHAR},",
          "addtime = #{addtime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Payinfo record);
}