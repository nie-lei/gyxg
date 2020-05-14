package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.DishePages;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.DishesInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

public interface DishePagesMapper extends Mapper<DishePages> {
    
    
    DishePages findBydisheId(DishePages dishePages);
    
    @Delete({
        "delete from gyxg_dishe_pages",
        "where pageid = #{pageid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryId(Integer pageid);

    @Insert({
        "insert into gyxg_dishe_pages (companyid, storeid, ",
        "dishesid, sequence, ",
        "pagemould, disheid, ",
        "status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{dishesid,jdbcType=INTEGER}, #{sequence,jdbcType=INTEGER}, ",
        "#{pagemould,jdbcType=VARCHAR}, #{disheid,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="pageid", before=false, resultType=Integer.class)
    int save(DishePages record);

    @Select({
        "select",
        "pageid, companyid, storeid, dishesid, sequence, pagemould, disheid, status",
        "from gyxg_dishe_pages",
        "where pageid = #{pageid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="pageid", property="pageid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="dishesid", property="dishesid", jdbcType=JdbcType.INTEGER),
        @Result(column="sequence", property="sequence", jdbcType=JdbcType.INTEGER),
        @Result(column="pagemould", property="pagemould", jdbcType=JdbcType.VARCHAR),
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    DishePages selectByPrimaryId(Integer pageid);

    @Select({
        "select",
        "pageid, companyid, storeid, dishesid, sequence, pagemould, disheid, status",
        "from gyxg_dishe_pages"
    })
    @Results({
        @Result(column="pageid", property="pageid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="dishesid", property="dishesid", jdbcType=JdbcType.INTEGER),
        @Result(column="sequence", property="sequence", jdbcType=JdbcType.INTEGER),
        @Result(column="pagemould", property="pagemould", jdbcType=JdbcType.VARCHAR),
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<DishePages> findAll();

    @Update({
        "update gyxg_dishe_pages",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "dishesid = #{dishesid,jdbcType=INTEGER},",
          "sequence = #{sequence,jdbcType=INTEGER},",
          "pagemould = #{pagemould,jdbcType=VARCHAR},",
          "disheid = #{disheid,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where pageid = #{pageid,jdbcType=INTEGER}"
    })
    int updateByPrimaryId(DishePages record);


    @SelectProvider(type= DishesInfo.class,method = "selectListcaipu")
    List<Map> selectList(Map params);

    /**
     * 根据菜品分类获取标准菜谱
     * @param classifyid
     * @return
     */
    @Select("select pagemould,disheid,flag from gyxg_dishe_pages where dishesid=#{classifyid} order by sequence")
    List<Map<String, Object>> selectListByClassifys(int classifyid);

    @Select("select disheid from gyxg_dishe_pages where companyid=#{companyid} and storeid=#{storeid}")
    List<String> selectListByxxxx(Map param);
}