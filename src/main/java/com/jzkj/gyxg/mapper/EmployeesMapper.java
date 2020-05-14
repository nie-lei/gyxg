package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Employees;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.EmployeesProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface EmployeesMapper {
    @Delete({
        "delete from gyxg_employees",
        "where employeeid = #{employeeid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer employeeid);

    @Insert({
        "insert into gyxg_employees (companyid, storeid, ",
        "departmentid, employeeno, ",
        "positionid, name, ",
        "phone, pwd, discountamount,roleid, ",
        "opentime, status)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{departmentid,jdbcType=INTEGER}, #{employeeno,jdbcType=VARCHAR}, ",
        "#{positionid,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{phone,jdbcType=VARCHAR}, #{pwd,jdbcType=VARCHAR}, #{discountamount,jdbcType=DECIMAL},#{roleid,jdbcType=INTEGER}, ",
        "#{opentime,jdbcType=TIMESTAMP}, #{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="employeeid", before=false, resultType=Integer.class)
    int insert(Employees record);

    @Select({
        "select",
        "employeeid, companyid, storeid, departmentid, employeeno, positionid, name, ",
        "phone, pwd, discountamount,roleid, opentime, status",
        "from gyxg_employees",
        "where employeeid = #{employeeid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employeeno", property="employeeno", jdbcType=JdbcType.VARCHAR),
        @Result(column="positionid", property="positionid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="pwd", property="pwd", jdbcType=JdbcType.VARCHAR),
            @Result(column="discountamount", property="discountamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="roleid", property="roleid", jdbcType=JdbcType.INTEGER),
        @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Employees selectByPrimaryKey(Integer employeeid);

    @Select({
        "select",
        "employeeid, companyid, storeid, departmentid, employeeno, positionid, name, ",
        "phone, pwd, discountamount,roleid, opentime, status",
        "from gyxg_employees"
    })
    @Results({
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
        @Result(column="employeeno", property="employeeno", jdbcType=JdbcType.VARCHAR),
        @Result(column="positionid", property="positionid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="pwd", property="pwd", jdbcType=JdbcType.VARCHAR), @Result(column="discountamount", property="discountamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="roleid", property="roleid", jdbcType=JdbcType.INTEGER),
        @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<Employees> selectAll();

    @Update({
        "update gyxg_employees",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "departmentid = #{departmentid,jdbcType=INTEGER},",
          "employeeno = #{employeeno,jdbcType=VARCHAR},",
          "positionid = #{positionid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "pwd = #{pwd,jdbcType=VARCHAR},",
            "discountamount = #{discountamount,jdbcType=DECIMAL},",
          "roleid = #{roleid,jdbcType=INTEGER},",
          "opentime = #{opentime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=VARCHAR}",
        "where employeeid = #{employeeid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Employees record);

    @Select("select * from gyxg_employees where phone=#{phone} and employeeno=#{employeeno}")
    Employees selectUserByPhoneOrNo(@Param("phone") String phone, @Param("employeeno") String employeeno);

    @SelectProvider(type = EmployeesProvider.class,method ="selectList")
    List<Map> selectList(Map params);

    @Select("select count(employeeid) from gyxg_employees where employeeno=#{employeeno}")
    int isExistEmployee(Employees employees);

    @Select("select employeeid,name from gyxg_employees where companyid = #{companyid} and storeid = #{storeid}")
    List<Map> selectEmp(Map params);

    /**
     * 根据店铺id，员工编号和密码获取员工信息
     * @param storeid
     * @param employeeno
     * @param pwd
     * @return
     */
    @Select({
            "select",
            "employeeid, companyid, storeid, departmentid, employeeno, positionid, name, ",
            "phone, pwd, roleid, opentime, status",
            "from gyxg_employees",
            "where storeid = #{storeid,jdbcType=INTEGER} and employeeno=#{employeeno,jdbcType=INTEGER} and pwd=#{pwd,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
            @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
            @Result(column="departmentid", property="departmentid", jdbcType=JdbcType.INTEGER),
            @Result(column="employeeno", property="employeeno", jdbcType=JdbcType.VARCHAR),
            @Result(column="positionid", property="positionid", jdbcType=JdbcType.INTEGER),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
            @Result(column="pwd", property="pwd", jdbcType=JdbcType.VARCHAR),
            @Result(column="roleid", property="roleid", jdbcType=JdbcType.INTEGER),
            @Result(column="opentime", property="opentime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    Employees selectByStoreIdAndEmployeenoAndPwd(@Param("storeid") int storeid, @Param("employeeno") String employeeno, @Param("pwd") String pwd);

    @Select("SELECT  IFNULL(SUM(nums),0) pcount,COUNT(orderid) tcount,IFNULL(SUM(totalmoney),0) mcount from gyxg_orders_master where TO_DAYS(ordertime) = TO_DAYS(NOW()) and `status` in ('3','4','5') and companyid=#{companyid} and storeid=#{storeid}")
    Map selectTodayData(Map param);

    @Select("SELECT  IFNULL(SUM(nums),0) pcount,COUNT(orderid) tcount,IFNULL(SUM(totalmoney),0) mcount from gyxg_orders_master where TO_DAYS( NOW( ) ) - TO_DAYS( ordertime) = 1 and `status` in ('3','4','5')and companyid=#{companyid} and storeid=#{storeid}")
    Map selectYesterdayData(Map param);

    @Select("SELECT\n" +
            "\ta.`code`,a.`name`,a.unit,a.price,t.amount\n" +
            "FROM\n" +
            "\tgyxg_dishes a\n" +
            "RIGHT JOIN (\n" +
            "\tSELECT\n" +
            "\t\tdisheid,\n" +
            "\t\tSUM(amount) amount\n" +
            "\tFROM\n" +
            "\t\tgyxg_orders_slave\n" +
            "\tGROUP BY\n" +
            "\t\tdisheid\n" +
            "\tORDER BY\n" +
            "\t\tamount DESC\n" +
            "\tLIMIT 5\n" +
            ") t on a.disheid=t.disheid\n" +
            "where a.companyid=#{companyid} and a.storeid=#{storeid} ORDER BY t.amount DESC")
    List<Map> selectDishesList(Map param);

    @Select("SELECT\n" +
            "\ta.`code`,\n" +
            "\ta.`name`,\n" +
            "\tc.`name` hname, \n" +
            "\ta.unit,\n" +
            "\tb.stock,\n" +
            "\ta.minstock,\n" +
            "\ta.price\n" +
            "FROM\n" +
            "\tgyxg_goods a\n" +
            "LEFT JOIN gyxg_goods_stock b ON a.goodsid = b.goodsid\n" +
            "LEFT JOIN gyxg_warehouses c on b.warehouseid=c.warehouseid\n" +
            "WHERE\n" +
            "\tb.stock < a.minstock and a.companyid=#{companyid} and a.storeid=#{storeid}\n" +
            "LIMIT 5")
    List<Map> selectGoodsStock(Map param);

    @Select("select count(employeeid) from gyxg_employees where employeeno=#{employeeno}")
    int isExistEmployeebyno(Employees employees);
    @Select("select count(employeeid) from gyxg_employees where companyid = #{companyid} and storeid = #{storeid} and  phone=#{phone}")
    int isExistEmployeebyphone(Employees employees);
}