package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.OrderMaster;
import java.util.List;
import java.util.Map;

import com.jzkj.gyxg.mapper.provider.CustomersProvider;
import com.jzkj.gyxg.mapper.provider.OrderProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

public interface OrderMasterMapper extends Mapper<OrderMaster> {
    @Delete({
        "delete from gyxg_orders_master",
        "where orderid = #{orderid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryId(Integer orderid);

    @Insert({
        "insert into gyxg_orders_master (companyid, storeid, ",
        "customerid, tableid, ",
        "canduan, nums, ordertime, ",
        "amountmoney, seatsfee, ",
        "tablefee, totalmoney,isall, ",
        "discountratio, discountamount, ",
        "payamount, alsoypay, ",
        "overtime, employeeid, ",
        "employee1id, overtype, ",
        "flag, tableids, ",
        "status, memo, sendtype, ismultiple)",
        "values (#{companyid,jdbcType=INTEGER}, #{storeid,jdbcType=INTEGER}, ",
        "#{customerid,jdbcType=INTEGER}, #{tableid,jdbcType=INTEGER}, ",
        "#{canduan,jdbcType=VARCHAR}, #{nums,jdbcType=INTEGER}, #{ordertime,jdbcType=TIMESTAMP}, ",
        "#{amountmoney,jdbcType=DECIMAL}, #{seatsfee,jdbcType=DECIMAL}, ",
        "#{tablefee,jdbcType=DECIMAL}, #{totalmoney,jdbcType=DECIMAL}, #{isall,jdbcType=VARCHAR},",
        "#{discountratio,jdbcType=DECIMAL}, #{discountamount,jdbcType=DECIMAL}, ",
        "#{payamount,jdbcType=DECIMAL}, #{alsoypay,jdbcType=DECIMAL}, ",
        "#{overtime,jdbcType=TIMESTAMP}, #{employeeid,jdbcType=INTEGER}, ",
        "#{employee1id,jdbcType=INTEGER}, #{overtype,jdbcType=VARCHAR}, ",
        "#{flag,jdbcType=VARCHAR}, #{tableids,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR}, #{memo,jdbcType=VARCHAR}, #{sendtype,jdbcType=VARCHAR}, #{ismultiple,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="orderid", before=false, resultType=Integer.class)
    int insert1(OrderMaster record);

    @Select({
        "select",
        "orderid, companyid, storeid, customerid, tableid, canduan, nums, ordertime, ",
        "amountmoney, seatsfee, tablefee, totalmoney, discountratio, discountamount, ",
        "payamount, alsoypay, overtime, employeeid, employee1id, overtype, flag,isall, tableids, ",
        "status, memo, sendtype, ismultiple ,discountid",
        "from gyxg_orders_master",
        "where orderid = #{orderid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="tableid", property="tableid", jdbcType=JdbcType.INTEGER),
        @Result(column="canduan", property="canduan", jdbcType=JdbcType.VARCHAR),
        @Result(column="nums", property="nums", jdbcType=JdbcType.INTEGER),
        @Result(column="ordertime", property="ordertime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="amountmoney", property="amountmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="seatsfee", property="seatsfee", jdbcType=JdbcType.DECIMAL),
        @Result(column="tablefee", property="tablefee", jdbcType=JdbcType.DECIMAL),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="discountratio", property="discountratio", jdbcType=JdbcType.DECIMAL),
        @Result(column="discountamount", property="discountamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="payamount", property="payamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="alsoypay", property="alsoypay", jdbcType=JdbcType.DECIMAL),
        @Result(column="overtime", property="overtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="overtype", property="overtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="isall", property="isall", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="tableids", property="tableids", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="sendtype", property="sendtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="ismultiple", property="ismultiple", jdbcType=JdbcType.INTEGER),
        @Result(column="discountid", property="discountid", jdbcType=JdbcType.INTEGER)
    })
    OrderMaster selectByPrimaryId(Integer orderid);

    @Select({
        "select",
        "orderid, companyid, storeid, customerid, tableid, canduan, nums, ordertime, ",
        "amountmoney, seatsfee, tablefee, totalmoney, discountratio, discountamount, ",
        "payamount, alsoypay, overtime, employeeid, employee1id, overtype, flag,isall, tableids, ",
        "status, memo, sendtype, ismultiple,discountid",
        "from gyxg_orders_master"
    })
    @Results({
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="companyid", property="companyid", jdbcType=JdbcType.INTEGER),
        @Result(column="storeid", property="storeid", jdbcType=JdbcType.INTEGER),
        @Result(column="customerid", property="customerid", jdbcType=JdbcType.INTEGER),
        @Result(column="tableid", property="tableid", jdbcType=JdbcType.INTEGER),
        @Result(column="canduan", property="canduan", jdbcType=JdbcType.VARCHAR),
        @Result(column="nums", property="nums", jdbcType=JdbcType.INTEGER),
        @Result(column="ordertime", property="ordertime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="amountmoney", property="amountmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="seatsfee", property="seatsfee", jdbcType=JdbcType.DECIMAL),
        @Result(column="tablefee", property="tablefee", jdbcType=JdbcType.DECIMAL),
        @Result(column="totalmoney", property="totalmoney", jdbcType=JdbcType.DECIMAL),
        @Result(column="discountratio", property="discountratio", jdbcType=JdbcType.DECIMAL),
        @Result(column="discountamount", property="discountamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="payamount", property="payamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="alsoypay", property="alsoypay", jdbcType=JdbcType.DECIMAL),
        @Result(column="overtime", property="overtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="employee1id", property="employee1id", jdbcType=JdbcType.INTEGER),
        @Result(column="overtype", property="overtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="isall", property="isall", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="tableids", property="tableids", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="sendtype", property="sendtype", jdbcType=JdbcType.VARCHAR),
        @Result(column="ismultiple", property="ismultiple", jdbcType=JdbcType.INTEGER),
        @Result(column="discountid", property="discountid", jdbcType=JdbcType.INTEGER)
    })
    List<OrderMaster> findAll();

    @Update({
        "update gyxg_orders_master",
        "set companyid = #{companyid,jdbcType=INTEGER},",
          "storeid = #{storeid,jdbcType=INTEGER},",
          "customerid = #{customerid,jdbcType=INTEGER},",
          "tableid = #{tableid,jdbcType=INTEGER},",
          "canduan = #{canduan,jdbcType=VARCHAR},",
          "nums = #{nums,jdbcType=INTEGER},",
          "ordertime = #{ordertime,jdbcType=TIMESTAMP},",
          "amountmoney = #{amountmoney,jdbcType=DECIMAL},",
          "seatsfee = #{seatsfee,jdbcType=DECIMAL},",
          "tablefee = #{tablefee,jdbcType=DECIMAL},",
          "totalmoney = #{totalmoney,jdbcType=DECIMAL},",
          "discountratio = #{discountratio,jdbcType=DECIMAL},",
            "discountid = #{discountid,jdbcType=INTEGER},",
          "discountamount = #{discountamount,jdbcType=DECIMAL},",
          "payamount = #{payamount,jdbcType=DECIMAL},",
          "alsoypay = #{alsoypay,jdbcType=DECIMAL},",
          "overtime = #{overtime,jdbcType=TIMESTAMP},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "employee1id = #{employee1id,jdbcType=INTEGER},",
          "overtype = #{overtype,jdbcType=VARCHAR},",
            "isall = #{isall,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "tableids = #{tableids,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "sendtype = #{sendtype,jdbcType=VARCHAR},",
          "ismultiple = #{ismultiple,jdbcType=INTEGER},",
          "discountid = #{discountid,jdbcType=INTEGER}",
        "where orderid = #{orderid,jdbcType=INTEGER}"
    })
    int updateByPrimaryId(OrderMaster record);

    @Select("SELECT\n" +
            "\ta.orderid,a.tableid,\n" +
            "a.memo,a.sendtype," +
//            "\tIFNULL((SELECT SUM(amountmoney) as amountmoney  from (\n" +
//            "SELECT\n" +
//            "\tSUM(e.amount) * e.price  as amountmoney\n" +
//            "FROM\n" +
//            "\tgyxg_orders_slave e \n" +
//            "WHERE\n" +
//            "\te.flag IN ('0', '2')\n" +
//            "AND e.`status` IN ('0', '1')\n" +
//            "AND e.orderid =a.orderid GROUP BY e.disheid\n" +
//            ") t ) ,0) as amountmoney,\n" +
            "\t a.amountmoney,\n" +
            " IFNULL((select sum(d.amount) from gyxg_orders_slave d where d.orderid=a.orderid and d.flag in ('0','2') and d.status in ('0','1')),0) as dishecount,\n" +
            "\ta.ordertime,\n" +
            "\tb.`code`,\n" +
            "\tb.`name`,\n" +
            "\ta.nums,\n" +
            "\tc.employeeno,\n" +
            "\ta.flag,\n" +
            "\ta.memo,\n" +
            "a.tableids,\n" +
            "\tCASE\n" +
            "WHEN a.flag = '0' THEN\n" +
            "\t'独台'\n" +
            "WHEN a.flag = '1' THEN\n" +
            "\t'合台'\n" +
            "ELSE\n" +
            "\t'连台'\n" +
            "END flagname\n" +
            "FROM\n" +
            "\tgyxg_orders_master a\n" +
            "LEFT JOIN gyxg_tables b ON a.tableid = b.tableid \n" +
            "LEFT JOIN gyxg_employees c ON a.employeeid = c.employeeid WHERE a.`status` in ('0','1') and " +
            "( a.tableid=#{tableid} or (tableids like '%,${tableid}%' or  tableids like '%,${tableid},%' or tableids like '%${tableid},%'));\n" +
            "\n;\n" +
            "\n")
    Map selectByTableId(@Param("tableid") Integer tableid);



    @Select("SELECT\n" +
            "\torderid"+
            " FROM\n" +
            "\tgyxg_orders_master where `status` in ('0','1') and tableid=#{id}\n")
    Integer selectTableId(Integer id);

//    @Select("SELECT\n" +
//            "\ta.orderid,a.tableid,\n" +
//            "a.memo,a.sendtype,a.memo,a.sendtype, a.amountmoney as amountmoney," +
//            "(select sum(d.amount) from gyxg_orders_slave d where d.orderid=a.orderid) as dishecount,\n" +
//            "\ta.ordertime,\n" +
//            "\tb.`code`,\n" +
//            "\tb.`name`,\n" +
//            "\ta.nums,\n" +
//            "\tc.employeeno,\n" +
//            "\ta.flag,\n" +
//            "\ta.memo,\n" +
//            "a.tableids,\n" +
//            "\tCASE\n" +
//            "WHEN a.flag = '0' THEN\n" +
//            "\t'独台'\n" +
//            "WHEN a.flag = '1' THEN\n" +
//            "\t'合台'\n" +
//            "ELSE\n" +
//            "\t'连台'\n" +
//            "END flagname\n" +
//            "FROM\n" +
//            "\tgyxg_orders_master a\n" +
//            "LEFT JOIN gyxg_tables b ON a.tableid = b.tableid \n" +
//            "LEFT JOIN gyxg_employees c ON a.employeeid = c.employeeid WHERE a.`status`='0' and " +
//            "( a.tableid=#{tableid} or (tableids like '%,${tableid}%' or  tableids like '%,${tableid},%' or tableids like '%${tableid},%'));\n" +
//            "\n;\n" +
//            "\n")
//    Map selectByTableId(@Param("tableid") Integer tableid);

    @Select("SELECT\n" +
            "\ta.orderid,a.tableid,\n" +
            "a.memo,a.sendtype," +
//            "\tIFNULL((SELECT SUM(amountmoney) as amountmoney  from (\n" +
//            "SELECT\n" +
//            "\tSUM(e.amount) * e.price  as amountmoney\n" +
//            "FROM\n" +
//            "\tgyxg_orders_slave e \n" +
//            "WHERE\n" +
//            "\te.flag IN ('0', '2')\n" +
//            "AND e.`status` IN ('0', '1')\n" +
//            "AND e.orderid =a.orderid GROUP BY e.disheid\n" +
//            ") t ) ,0) as amountmoney,\n" +
            "\ta.amountmoney,\n" +
            "IFNULL((select sum(d.amount) from gyxg_orders_slave d where d.orderid=a.orderid and d.flag in ('0','2') and d.status in('0','1')),0) as dishecount,\n" +
            "\ta.ordertime,\n" +
            "\tb.`code`,\n" +
            "\tb.`name`,\n" +
            "\ta.nums,\n" +
            "\tc.employeeno,\n" +
            "\ta.flag,\n" +
            "\ta.memo,\n" +
            "a.tableids,\n" +
            "\tCASE\n" +
            "WHEN a.flag = '0' THEN\n" +
            "\t'独台'\n" +
            "WHEN a.flag = '1' THEN\n" +
            "\t'合台'\n" +
            "ELSE\n" +
            "\t'连台'\n" +
            "END flagname\n" +
            "FROM\n" +
            "\tgyxg_orders_master a\n" +
            "LEFT JOIN gyxg_tables b ON a.tableid = b.tableid \n" +
            "LEFT JOIN gyxg_employees c ON a.employeeid = c.employeeid WHERE a.`status` in ('0','1') and " +
            " a.tableid=#{tableid}")
    Map selectByTableId2(Integer tableid);

//    @Select("SELECT\n" +
//            "\ta.orderid,a.tableid,\n" +
//            "a.memo,a.sendtype,a.amountmoney," +
//            "(select sum(d.amount) from gyxg_orders_slave d where d.orderid=a.orderid and d.flag='0' and d.status in('0','1')) as dishecount,\n" +
//            "\ta.ordertime,\n" +
//            "\tb.`code`,\n" +
//            "\tb.`name`,\n" +
//            "\ta.nums,\n" +
//            "\tc.employeeno,\n" +
//            "\ta.flag,\n" +
//            "\ta.memo,\n" +
//            "a.tableids,\n" +
//            "\tCASE\n" +
//            "WHEN a.flag = '0' THEN\n" +
//            "\t'独台'\n" +
//            "WHEN a.flag = '1' THEN\n" +
//            "\t'合台'\n" +
//            "ELSE\n" +
//            "\t'连台'\n" +
//            "END flagname\n" +
//            "FROM\n" +
//            "\tgyxg_orders_master a\n" +
//            "LEFT JOIN gyxg_tables b ON a.tableid = b.tableid \n" +
//            "LEFT JOIN gyxg_employees c ON a.employeeid = c.employeeid WHERE a.`status`='0' and " +
//            " a.tableid=#{tableid}")
//    Map selectByTableId2(Integer tableid);

    @Update("update gyxg_orders_master set tableids=#{tableids} where orderid=#{orderid} and flag=2")
    void updateByMap(Map map);



    @Select("SELECT\n" +
            "\ta.orderid,\n" +
            "\ta.tableid,\n" +
            "\ta.flag,\n" +
            "\tDATE_FORMAT(a.ordertime, '%H:%i:%s') ordertime,\n" +
            "\ta.nums,\n" +
            "\ta.amountmoney\n" +
//            "\tIFNULL((SELECT SUM(amountmoney) as amountmoney  from (\n" +
//            "SELECT\n" +
//            "\tSUM(e.amount) * e.price  as amountmoney\n" +
//            "FROM\n" +
//            "\tgyxg_orders_slave e \n" +
//            "WHERE\n" +
//            "\te.flag IN ('0', '2')\n" +
//            "AND e.`status` IN ('0', '1')\n" +
//            "AND e.orderid =a.orderid GROUP BY e.disheid\n" +
//            ") t ) ,0) as amountmoney\n" +
            "FROM\n" +
            "\tgyxg_orders_master a \n" +
            "WHERE\n" +
            "\ta.`status` in ('0','1')\n" +
            "AND a.flag = '1'\n" +
            "AND (\n" +
            "\ta.tableids LIKE '%,${tableid}%'\n" +
            "\tOR a.tableids LIKE '%,${tableid},%'\n" +
            "\tOR a.tableids LIKE '${tableid},%'\n" +
            ")")
    Map selectTableByTableidFlag1(@Param("tableid") Integer tableid);

    @Select({
            "select",
            "orderid, companyid, storeid, customerid, tableid, canduan, nums, ordertime, ",
            "amountmoney, seatsfee, tablefee, totalmoney, discountratio, discountamount, ",
            "payamount, overtime, employeeid, employee1id, overtype, flag, tableids, status, ",
            "memo, sendtype",
            "from gyxg_orders_master",
            "where tableid = #{tableid,jdbcType=INTEGER} and flag='2' and status in ('0','1')"
    })
    OrderMaster selectMasterByTableId(Integer tableid);

//    @Select("select orderid,tablefee,nums,amountmoney,seatsfee from gyxg_orders_master where companyid=#{companyid} and storeid=#{storeid} and flag ='2' and status in ('0','1') and tableids='${tableids}'")
//    List<Map> selectBytablesids(Map tidstr);

    /**
     * 查询连台的所有订单，根据tableids
     * @param master
     * @return
     */
    List<OrderMaster> selectBytablesids(OrderMaster master);

    /**
     * 通过类型查询连台是否付过款
     * @param master
     * @return
     */
    List<OrderMaster> findOrderMasterByType(OrderMaster master);


    @Select("select IFNULL(sum(amountmoney),0) from gyxg_orders_master where customerid=#{customerid} and status='4' and date_format(ordertime,'%Y-%m') = date_format(now(),'%Y-%m')")
    Double selectTotalMoneyByCusid(String customerid);

    @SelectProvider(type = OrderProvider.class,method = "selectOrderInfoList")
    List<Map> selectOrderInfoList(Map params);


    @Select("select a.orderid from gyxg_orders_master a where a.orderid!=#{orderid} and a.tableids=(select b.tableids from gyxg_orders_master b where b.orderid=#{orderid}) ")
    List<Integer> selectByorderidGuanlian(@Param("orderid") Integer orderid);

    @Select("\n" +
            "SELECT a.id,a.`name`,a.unit,SUM(a.amount) as amountall,a.price,(a.price*SUM(a.amount)) as totalmoney,a.memo,a.state from gyxg_orders_slave a  where a.orderid=#{orderid} GROUP BY a.disheid \n")
    List<Map> selectOrderSlaveInfoList(@Param("orderid") Integer orderid);

    @Select("SELECT\n" +
            "\t\tCASE when a.flag='0' THEN '结账'\n" +
            "\tELSE '订金抵扣' END as flag,a.paytype,a.paytypedesc,b.`name`,b.phone,a.payamount,a.paytime,(SELECT c.`name` from gyxg_employees c where c.employeeid=a.employeeid) as empname,a.`status`\n" +
            "FROM\n" +
            "\tgyxg_payrecords a LEFT JOIN gyxg_customers b on a.customerid=b.customerid\n" +
            "WHERE\n" +
            "\ta.payid IN (\n" +
            "\t\tSELECT\n" +
            "\t\t\tpayid\n" +
            "\t\tFROM\n" +
            "\t\t\tgyxg_order_pays\n" +
            "\t\tWHERE\n" +
            "\t\t\torderid = #{orderid}\n" +
            "\t) and a.flag in ('0','2')\n")
    List<Map> orderPayInfoDetail(@Param("orderid") Integer orderid);


    @SelectProvider(type = CustomersProvider.class,method = "signListInfo")
    List<Map> signListInfo(Map params);

    @Select("select orderid,tablefee,nums,amountmoney,seatsfee from gyxg_orders_master where companyid=#{companyid} and storeid=#{storeid} and flag ='2' and orderid in (${orderids})")
    List<Map> selectByOtherorderStatus26(Map params);

    @Select("call sp_order_accounts_new(#{orderid})")
    void callsp_order_accounts_new(Integer orderid);
}