package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Paytype;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.EmployeesProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

public interface PaytypeMapper extends Mapper<Paytype> {
    @Delete({
        "delete from gyxg_paytype",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryId(Integer id);

    @Insert({
        "insert into gyxg_paytype (companyid, storeid, ",
        "paycode, payname, ",
        "isquan, quanmoney, ",
        "discountmoney, minmoney, ",
        "quannum, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{paycode,jdbcType=INTEGER}, #{payname,jdbcType=VARCHAR}, ",
        "#{isquan,jdbcType=VARCHAR}, #{quanmoney,jdbcType=DECIMAL}, ",
        "#{discountmoney,jdbcType=DECIMAL}, #{minmoney,jdbcType=DECIMAL}, ",
        "#{quannum,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert1(Paytype record);

    @Select({
        "select",
        "id, companyid, storeid, paycode, payname, isquan, quanmoney, discountmoney, ",
        "minmoney, quannum, status",
        "from gyxg_paytype",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="paycode", property="paycode", jdbcType=JdbcType.INTEGER),
        @Result(column="payname", property="payname", jdbcType=JdbcType.VARCHAR),
        @Result(column="isquan", property="isquan", jdbcType=JdbcType.VARCHAR),
        @Result(column="quanmoney", property="quanmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="discountmoney", property="discountmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="minmoney", property="minmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="quannum", property="quannum", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Paytype selectByPrimaryId(Integer id);

    @Select({
        "select",
        "id, companyid, storeid, paycode, payname, isquan, quanmoney, discountmoney, ",
        "minmoney, quannum, status",
        "from gyxg_paytype"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="paycode", property="paycode", jdbcType=JdbcType.INTEGER),
        @Result(column="payname", property="payname", jdbcType=JdbcType.VARCHAR),
        @Result(column="isquan", property="isquan", jdbcType=JdbcType.VARCHAR),
        @Result(column="quanmoney", property="quanmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="discountmoney", property="discountmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="minmoney", property="minmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="quannum", property="quannum", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Paytype> findAll();

    @Update({
        "update gyxg_paytype",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "paycode = #{paycode,jdbcType=INTEGER},",
          "payname = #{payname,jdbcType=VARCHAR},",
          "isquan = #{isquan,jdbcType=VARCHAR},",
          "quanmoney = #{quanmoney,jdbcType=DECIMAL},",
          "discountmoney = #{discountmoney,jdbcType=DECIMAL},",
          "minmoney = #{minmoney,jdbcType=DECIMAL},",
          "quannum = #{quannum,jdbcType=INTEGER},",
          "status = #{status,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryId(Paytype record);

    @SelectProvider(type = EmployeesProvider.class,method = "selectPaytypeList")
    List<Paytype> selectList(Map params);

    @Select("SELECT IFNULL(MAX(paycode),0) from gyxg_paytype where companyid=#{companyid} and storeid=#{storeid}")
    int selectMaxPaycode(Map params);

    @Select("select paycode from gyxg_paytype where companyid=#{companyid} and storeid=#{storeid} and status='0' and isquan='0'")
    List<Integer> selectStatus0(Map params);

    /**
     * 查询payType列表
     * @param paytype
     * @return
     */
    List<Paytype> findPayTypeList(Paytype paytype);

    @Select("select * from gyxg_paytype where companyid=#{companyid} and storeid=#{storeid} and status='0' and isquan='1'")
    List<Paytype> selectquan(Map params);
}