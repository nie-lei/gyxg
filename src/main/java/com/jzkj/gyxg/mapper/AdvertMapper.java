package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Advert;
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

public interface AdvertMapper {
    @Delete({
        "delete from gyxg_advert",
        "where adid = #{adid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer adid);

    @Insert({
        "insert into gyxg_advert (companyid, storeno, ",
        "flag, name, type, ",
        "pic, link, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeno,jdbcType=VARCHAR}, ",
        "#{flag,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR}, ",
        "#{pic,jdbcType=VARCHAR}, #{link,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="adid", before=false, resultType=Integer.class)
    int insert(Advert record);

    @Select({
        "select",
        "adid, companyid, storeno, flag, name, type, pic, link, status",
        "from gyxg_advert",
        "where adid = #{adid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="adid", property="adid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeno", property="storeno", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic", property="pic", jdbcType=JdbcType.VARCHAR),
        @Result(column="link", property="link", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Advert selectByPrimaryKey(Integer adid);

    @Select({
        "select",
        "adid, companyid, storeno, flag, name, type, pic, link, status",
        "from gyxg_advert"
    })
    @Results({
        @Result(column="adid", property="adid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeno", property="storeno", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="pic", property="pic", jdbcType=JdbcType.VARCHAR),
        @Result(column="link", property="link", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Advert> selectAll();

    @Update({
        "update gyxg_advert",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeno = #{storeno,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=VARCHAR},",
          "pic = #{pic,jdbcType=VARCHAR},",
          "link = #{link,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where adid = #{adid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Advert record);

    /**
     * 根据店铺id获取店铺banner
     * @param storeid
     * @return
     */
    @Select("select adid,name,type,pic,ifnull(link, '') link from gyxg_advert where storeno=#{storeid} and status=1")
    List<Map<String, Object>> selectByStoreId(int storeid);
}