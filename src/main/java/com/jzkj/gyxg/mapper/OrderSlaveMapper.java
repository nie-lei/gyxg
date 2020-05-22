package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.OrderSlave;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface OrderSlaveMapper {
    @Delete({
        "delete from gyxg_orders_slave",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into gyxg_orders_slave (orderid, disheid, ",
        "name, methodid, ",
        "unit, amount, price, ",
        "orderamount, realamount, ",
        "memo, flag, employeeid, ",
        "intime, state, ",
        "status)",
        "values (#{orderid,jdbcType=INTEGER}, #{disheid,jdbcType=INTEGER}, ",
        "#{name,jdbcType=VARCHAR}, #{methodid,jdbcType=INTEGER}, ",
        "#{unit,jdbcType=VARCHAR}, #{amount,jdbcType=INTEGER}, #{price,jdbcType=DECIMAL}, ",
        "#{orderamount,jdbcType=DECIMAL}, #{realamount,jdbcType=DECIMAL}, ",
        "#{memo,jdbcType=VARCHAR}, #{flag,jdbcType=VARCHAR}, #{employeeid,jdbcType=INTEGER}, ",
        "#{intime,jdbcType=TIMESTAMP}, #{state,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(OrderSlave record);

    @Select({
        "select",
        "id, orderid, disheid, name, methodid, unit, amount, price, orderamount, realamount, ",
        "memo, flag, employeeid, intime, state, status",
        "from gyxg_orders_slave",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="orderamount", property="orderamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="realamount", property="realamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    OrderSlave selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, orderid, disheid, name, methodid, unit, amount, price, orderamount, realamount, ",
        "memo, flag, employeeid, intime, state, status",
        "from gyxg_orders_slave"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
        @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
        @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
        @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
        @Result(column="orderamount", property="orderamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="realamount", property="realamount", jdbcType=JdbcType.DECIMAL),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
        @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
        @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    List<OrderSlave> selectAll();

    @Update({
        "update gyxg_orders_slave",
        "set orderid = #{orderid,jdbcType=INTEGER},",
          "disheid = #{disheid,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "methodid = #{methodid,jdbcType=INTEGER},",
          "unit = #{unit,jdbcType=VARCHAR},",
          "amount = #{amount,jdbcType=INTEGER},",
          "price = #{price,jdbcType=DECIMAL},",
          "orderamount = #{orderamount,jdbcType=DECIMAL},",
          "realamount = #{realamount,jdbcType=DECIMAL},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=VARCHAR},",
          "employeeid = #{employeeid,jdbcType=INTEGER},",
          "intime = #{intime,jdbcType=TIMESTAMP},",
          "state = #{state,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(OrderSlave record);

    @Select("select a.id,a.`name`,a.memo,a.amount,a.unit,a.flag,a.price,a.`status`,b.`code` from gyxg_orders_slave a LEFT JOIN gyxg_dishes b on a.disheid=b.disheid  where a.orderid=#{orderid} and a.status in ('0','1');")
    List<Map> selectByOrderid(Integer orderid);



    @Select("\n" +
            "SELECT count(t.disheid) from (SELECT disheid,SUM(amount) as amount from gyxg_orders_slave  where orderid=#{orderid} and status in ('0','1','2')  GROUP BY disheid) t where t.amount > 0")
    int selectCountByOrderid(Integer orderid);

    @Select("SELECT * from (SELECT c.classifyid,c.`name` classfyname from gyxg_orders_slave a LEFT JOIN gyxg_dishes b on a.disheid=b.disheid\n" +
            "LEFT JOIN gyxg_dishe_classifys c on b.dishesid=c.classifyid where a.flag='0' and a.`status` in ('0','1') and a.orderid in (#{orderid})) t GROUP BY t.classifyid;\n")
    List<Map> selectClassfyBySlave(Map param);

    @Select("\n" +
            "SELECT\n" +
            "\t\t\tb.`name`,b.code,\n" +
            "\t\t\ta.disheid,a.state,\n" +
            "\t\t\tSUM(a.amount) as amount,\n" +
            "\t\t\ta.price,\n" +
            "\t\t\t(SUM(a.amount) * a.price) AS price0\n" +
            "\t\tFROM gyxg_orders_slave a\n" +
            "\t\tLEFT JOIN gyxg_dishes b ON a.disheid = b.disheid\n" +
            "\t\tWHERE\n" +
            "\t\t\ta.flag in ('0','2')\n" +
            "\t\tAND a.`status` IN ('0', '1')\n" +
            "\t\tAND a.orderid IN (${orderids})\n" +
            "\t\tGROUP BY a.disheid ")
    List<Map> selectDishesByOrderid(Map param);



//    @Select("SELECT\n" +
//            "\tSUM(c.price) as classfytotalmoney\n" +
//            "FROM\n" +
//            "\tgyxg_dishe_classifys a\n" +
//            "LEFT JOIN gyxg_dishes b  ON a.classifyid = b.dishesid\n" +
//            "LEFT JOIN gyxg_orders_slave c ON b.disheid = c.disheid\n" +
//            "WHERE c.flag='0' and c.`status` in ('0','1') \n" +
//            "\tand c.orderid in (${orderid})\n")
//    double selectDishesMoneyByOrderidAndClassfyId(Map param);

    @Select("SELECT SUM(price) totalmoney from gyxg_orders_slave where  flag='0' and `status` in ('0','1') and orderid in (${orderid})")
    Double selectTotalMoneyByOrderid(Map param);

    @Select("SELECT SUM(nums) nums from gyxg_orders_master where  orderid in (${orderids})")
    Integer selectTotalPersonByOrderid(Map param);

    @Select("\n" +
            "SELECT\n" +
            "\tIFNULL(SUM(price0),0) as top1total\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\tb.`name`,\n" +
            "\t\t\ta.disheid,\n" +
            "\t\t\tSUM(a.amount) AS amount,\n" +
            "\t\t\ta.price,\n" +
            "\t\t\t(SUM(a.amount) * a.price) AS price0\n" +
            "\t\tFROM\n" +
            "\t\t\tgyxg_orders_slave a\n" +
            "\t\tLEFT JOIN gyxg_dishes b ON a.disheid = b.disheid\n" +
            "\t\tWHERE\n" +
            "\t\t\ta.flag in ('0','2')\n" +
            "\t\tAND a.state = '${state}'\n" +
            "\t\tAND a.status in ('0','1')\n" +
            "\t\tAND a.orderid IN (${orderids})\n" +
            "\t\tGROUP BY\n" +
            "\t\t\ta.disheid\n" +
            "\t) t\n")
    Double selectTop1Total(Map param);

    /**
     * 统计打折金额
     * @param orderSlave
     * @return
     */
//    Double findTop2Total(OrderSlave orderSlave);


    /**
     * 查询当前订单的点菜详情根据打折状态和订单号查询
     * @return
     */
    List<OrderSlave> selectOrderSlaveByOrderId(OrderSlave orderSlave);

    /**
     * 根据订单号修改点菜信息
     * @param orderSlave
     * @return
     */
    int updateByOrderId(OrderSlave orderSlave);

    /**
     * 根据菜品id获取点菜总数量
     * @param disheid
     * @param methodid
     * @return
     */
    @Select("select sum(amount) from gyxg_orders_slave where disheid=#{disheid} and methodid=#{methodid}")
    Integer selectDishesSumByDishesId(@Param("disheid") Integer disheid, @Param("methodid") Integer methodid);

    @Select("select sum(amount) from gyxg_orders_slave where orderid=#{orderid} and disheid=#{disheid}")
    Integer selectByOrderidAndDishesid(@Param("orderid") Integer orderid, @Param("disheid")Integer disheid);

    /**
     * 根据结账单号获取该订单下的菜品列表信息
     * @param msg
     * @return
     */
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
            @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
            @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
            @Result(column="orderamount", property="orderamount", jdbcType=JdbcType.DECIMAL),
            @Result(column="realamount", property="realamount", jdbcType=JdbcType.DECIMAL),
            @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
            @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
            @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
            @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    @Select("select " +
            "id, orderid, disheid, name, methodid, unit, ifnull(sum(amount), 0) amount," +
            "price,orderamount,ifnull(sum(realamount), 0) realamount, memo, flag, employeeid, " +
            "intime,state, status " +
            "from gyxg_orders_slave ${msg} group by disheid")
    List<OrderSlave> selectOrderDishesCountByOrderId(@Param("msg") String msg);

    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER),
            @Result(column="disheid", property="disheid", jdbcType=JdbcType.INTEGER),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="methodid", property="methodid", jdbcType=JdbcType.INTEGER),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
            @Result(column="price", property="price", jdbcType=JdbcType.DECIMAL),
            @Result(column="orderamount", property="orderamount", jdbcType=JdbcType.DECIMAL),
            @Result(column="realamount", property="realamount", jdbcType=JdbcType.DECIMAL),
            @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
            @Result(column="flag", property="flag", jdbcType=JdbcType.VARCHAR),
            @Result(column="employeeid", property="employeeid", jdbcType=JdbcType.INTEGER),
            @Result(column="intime", property="intime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="state", property="state", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR)
    })
    @Select("SELECT * "+
            "FROM gyxg_orders_slave " +
            "WHERE flag in ('0','2') AND orderid IN (${orderid})")
    List<OrderSlave> selectOrderSlaveAllByOrderId(@Param("orderid") String orderid);
}