package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Positions;
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

public interface PositionsMapper {
    @Delete({
        "delete from gyxg_positions",
        "where positionid = #{positionid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer positionid);

    @Insert({
        "insert into gyxg_positions (companyid, storeid, ",
        "positionno, name, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{positionno,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="positionid", before=false, resultType=Integer.class)
    int insert(Positions record);

    @Select({
        "select",
        "positionid, companyid, storeid, positionno, name, status",
        "from gyxg_positions",
        "where positionid = #{positionid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="positionid", property="positionid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="positionno", property="positionno", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Positions selectByPrimaryKey(Integer positionid);

    @Select({
        "select",
        "positionid, companyid, storeid, positionno, name, status",
        "from gyxg_positions"
    })
    @Results({
        @Result(column="positionid", property="positionid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="positionno", property="positionno", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Positions> selectAll();

    @Update({
        "update gyxg_positions",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "positionno = #{positionno,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where positionid = #{positionid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Positions record);

    @Select("select positionid,positionno,name,status from gyxg_positions where companyid=#{companyid} and storeid=#{storeid}")
    List<Map> selectList(Map<String, String> params);

    @Select("select count(positionid) from gyxg_positions where companyid=#{companyid} and storeid=#{storeid} and (positionno=#{positionno} or name=#{name}) )")
    int checkPosition(Positions position);

    @Select("select count(positionid) from gyxg_employees where positionid=#{positionid}")
    int selectEmpCountByPositiontid(Positions positions);

    @Select("select positionid,name from gyxg_positions where companyid=#{companyid} and storeid=#{storeid}")
    List<Map> selectPosition(Map params);

    @Select("select count(positionid) from gyxg_positions where companyid=#{companyid} and storeid=#{storeid} and positionno=#{positionno}")
    int checkPositionbycode(Positions position);

    @Select("select count(positionid) from gyxg_positions where companyid=#{companyid} and storeid=#{storeid} and name=#{name}")
    int checkPositionbyname(Positions position);
}