package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Sms;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface SmsMapper {
    @Delete({
        "delete from gyxg_sms",
        "where Id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_sms (content, phone, ",
        "smsnum, issend, sendtime, ",
        "reason, msgid)",
        "values (#{content,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, ",
        "#{smsnum,jdbcType=SMALLINT}, #{issend,jdbcType=CHAR}, #{sendtime,jdbcType=TIMESTAMP}, ",
        "#{reason,jdbcType=VARCHAR}, #{msgid,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Sms record);

    @Select({
        "select",
        "Id, content, phone, smsnum, issend, sendtime, reason, msgid",
        "from gyxg_sms",
        "where Id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="Id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="smsnum", property="smsnum", jdbcType=JdbcType.SMALLINT),
        @Result(column="issend", property="issend", jdbcType=JdbcType.CHAR),
        @Result(column="sendtime", property="sendtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="reason", property="reason", jdbcType=JdbcType.VARCHAR),
        @Result(column="msgid", property="msgid", jdbcType=JdbcType.VARCHAR)
    })
    Sms selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "Id, content, phone, smsnum, issend, sendtime, reason, msgid",
        "from gyxg_sms"
    })
    @Results({
        @Result(column="Id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="smsnum", property="smsnum", jdbcType=JdbcType.SMALLINT),
        @Result(column="issend", property="issend", jdbcType=JdbcType.CHAR),
        @Result(column="sendtime", property="sendtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="reason", property="reason", jdbcType=JdbcType.VARCHAR),
        @Result(column="msgid", property="msgid", jdbcType=JdbcType.VARCHAR)
    })
    List<Sms> selectAll();

    @Update({
        "update gyxg_sms",
        "set content = #{content,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "smsnum = #{smsnum,jdbcType=SMALLINT},",
          "issend = #{issend,jdbcType=CHAR},",
          "sendtime = #{sendtime,jdbcType=TIMESTAMP},",
          "reason = #{reason,jdbcType=VARCHAR},",
          "msgid = #{msgid,jdbcType=VARCHAR}",
        "where Id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Sms record);
}