package com.jzkj.gyxg.mapper;

import com.jzkj.gyxg.entity.Paytype;
import com.jzkj.gyxg.mapper.provider.ReportProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    @Select("call sp_tuicaimingxi(#{companyid},#{storeid},#{begin_date},#{end_date},#{classifyid},#{dishname})")
    List<Map> tuicaimingxi(Map params);

    @Select("call sp_zengcaimingxi (#{companyid},#{storeid},#{begin_date},#{end_date},#{classifyid},#{dishname})")
    List<Map> zengcaimingxi(Map params);

    @Select("call sp_caipinxiaoshoutongjibiao (#{companyid},#{storeid},#{begin_date},#{end_date},#{classifyid},#{dishname})")
    List<Map> caipinxiaoshoutongji(Map params);


    @Select("call sp_jiushuidan (#{companyid},#{storeid},#{begin_date},#{end_date})")
    List<Map> jiushuidan(Map params);

    @Select("SELECT\n" +
            "\ta.customerid,(select b.`name` from gyxg_customers b where a.customerid=b.customerid) name,\n" +
            "\tSUM(payamount) payamounttotal\n" +
            "FROM\n" +
            "\tgyxg_orders_master a\n" +
            "WHERE\n" +
            "\ta.`status` IN ('4', '5') and a.storeid = #{storeid} and a.companyid=#{companyid} and DATE_FORMAT(a.ordertime,'%Y-%m-%d') >= #{begin_date} and DATE_FORMAT(a.ordertime,'%Y-%m-%d') <=  #{end_date}\n" +
            "GROUP BY\n" +
            "\ta.customerid;")
    List<Map> qiandan45(Map params);

    @Select("SELECT\n" +
            "\ta.customerid,(select b.`name` from gyxg_customers b where a.customerid=b.customerid) name,\n" +
            "\tSUM(payamount) payamounttotal\n" +
            "FROM\n" +
            "\tgyxg_orders_master a\n" +
            "WHERE\n" +
            "\ta.`status` ='5' and a.storeid = #{storeid} and a.companyid=#{companyid} and DATE_FORMAT(a.ordertime,'%Y-%m-%d') >= #{begin_date} and DATE_FORMAT(a.ordertime,'%Y-%m-%d') <=  #{end_date}\n" +
            "GROUP BY\n" +
            "\ta.customerid;")
    List<Map> qiandan5(Map params);

    @Select("select * from gyxg_orders_accounts where companyid=#{companyid} and storeid=#{storeid} and DATE_FORMAT(ordertime,'%Y-%m-%d') >= #{begin_date} and DATE_FORMAT(ordertime,'%Y-%m-%d') <=  #{end_date}")
    List<Map> zhangdanfukuanmingxi(Map params);

    @Select("call sp_jiesuanfangshihuizongchaxun (#{companyid},#{storeid},#{begin_date},#{end_date})")
    List<Map> jiesuanfangshihuizongchaxun(Map params);

    @Select("SELECT\n" +
            "\tDATE_FORMAT(t.overtime, '%Y-%m-%d') AS overtime,\n" +
            "\tSUM(t.payamount) payamount,\n" +
            "\tSUM(t.amount) amount,\n" +
            "\tSUM(t.nums) nums,\n" +
            "\tSUM(t.lunchamount) lunchamount,\n" +
            "\tSUM(t.dinneramount) dinneramount,\n" +
            "\tSUM(t.lunchamount) + SUM(t.dinneramount) AS canduanheji,\n" +
            "\tSUM(t.gdlamount) gdlamount,\n" +
            "\tSUM(t.hclamount) hclamount,\n" +
            "\tSUM(t.sclamount) sclamount,\n" +
            "\tSUM(t.xclamount) xclamount,\n" +
            "\tSUM(t.jslamount) jslamount,\n" +
            "\tSUM(t.qtlamount) qtlamount,\n" +
            "\tSUM(t.gdlamount) + SUM(t.hclamount) + SUM(t.sclamount) + SUM(t.xclamount) + SUM(t.jslamount) + SUM(t.qtlamount) AS yingyeheji,\n" +
            "\tSUM(t.zhifubaoamount) zhifubaoamount,\n" +
            "\tSUM(t.weixinamount) weixinamount,\n" +
            "\tSUM(t.quanjin) quanjin,\n" +
            "\tSUM(t.yueamount) yueamount,\n" +
            "\tSUM(t.miandanamount) miandanamount,\n" +
            "\tSUM(t.tgyouhui) tgyouhui,\n" +
            "\tSUM(t.discountamount) discountamount,\n" +
            "\tSUM(t.rmbamount) rmbamount,\n" +
            "\tSUM(t.bankamount) bankamount,\n" +
            "\tSUM(t.qiandanamount) qiandanamount,\n" +
            "\tSUM(t.freeamount) freeamount,\n" +
            "\tSUM(t.sanbeiamount) sanbeiamount,\n" +
            "\tSUM(t.payamount) AS heji\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\ta.*, (\n" +
            "\t\t\t\t(\n" +
            "\t\t\t\t\ta.youhuiquanjine - a.tuangouyouhui\n" +
            "\t\t\t\t) * a.youhuiquannum\n" +
            "\t\t\t) AS quanjin,\n" +
            "\t\t\t(\n" +
            "\t\t\t\ta.tuangouyouhui * a.youhuiquannum\n" +
            "\t\t\t) AS tgyouhui\n" +
            "\t\tFROM\n" +
            "\t\t\tgyxg_orders_accounts a\n" +
            "\t\tWHERE\n" +
            "\t\t\tDATE_FORMAT(a.overtime, '%Y-%m-%d') >= #{begin_date}\n" +
            "\t\tAND DATE_FORMAT(a.overtime, '%Y-%m-%d') <= #{end_date}\n" +
            "\t\tAND a.companyid = #{companyid}\n" +
            "\t\tAND a.storeid = #{storeid}\n" +
            "\t) t\n" +
            "GROUP BY\n" +
            "\tDATE_FORMAT(overtime, '%Y-%m-%d')\n" +
            "ORDER BY\n" +
            "\tovertime ASC;\n" +
            "\n")
    List<Map> yingyehuizong(Map params);

    @Select("SELECT\n" +
            "\tSUM(b.lunchamount) lunchamount,\n" +
            "\tSUM(b.payamount) payamount,\n" +
            "\tSUM(b.amount) amount,\n" +
            "\tSUM(b.nums) nums,\n" +
            "\tSUM(b.dinneramount) dinneramount,\n" +
            "\tSUM(b.canduanheji) canduanheji,\n" +
            "\tSUM(b.gdlamount) gdlamount,\n" +
            "\tSUM(b.hclamount) hclamount,\n" +
            "\tSUM(b.sclamount) sclamount,\n" +
            "\tSUM(b.xclamount) xclamount,\n" +
            "\tSUM(b.jslamount) jslamount,\n" +
            "\tSUM(b.qtlamount) qtlamount,\n" +
            "SUM(b.yingyeheji) yingyeheji,\n" +
            "\t\tSUM(b.zhifubaoamount) zhifubaoamount,\n" +
            "SUM(b.weixinamount) weixinamount,\n" +
            "\tSUM(b.quanjin) quanjin,\n" +
            "SUM(b.yueamount) yueamount,\n" +
            "\tSUM(b.miandanamount) miandanamount,\n" +
            "SUM(b.tgyouhui) tgyouhui,\n" +
            "SUM(b.discountamount) discountamount,\n" +
            "SUM(b.rmbamount) rmbamount,\n" +
            "SUM(b.bankamount) bankamount,\n" +
            "SUM(b.qiandanamount) qiandanamount,\n" +
            "SUM(b.freeamount) freeamount,\n" +
            "SUM(b.sanbeiamount) sanbeiamount,\n" +
            "SUM(b.heji) heji\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\tDATE_FORMAT(t.overtime, '%Y-%m-%d') AS overtime,\n" +
            "\t\t\tSUM(t.payamount) payamount,\n" +
            "\t\t\tSUM(t.amount) amount,\n" +
            "\t\t\tSUM(t.nums) nums,\n" +
            "\t\t\tSUM(t.lunchamount) lunchamount,\n" +
            "\t\t\tSUM(t.dinneramount) dinneramount,\n" +
            "\t\t\tSUM(t.lunchamount) + SUM(t.dinneramount) AS canduanheji,\n" +
            "\t\t\tSUM(t.gdlamount) gdlamount,\n" +
            "\t\t\tSUM(t.hclamount) hclamount,\n" +
            "\t\t\tSUM(t.sclamount) sclamount,\n" +
            "\t\t\tSUM(t.xclamount) xclamount,\n" +
            "\t\t\tSUM(t.jslamount) jslamount,\n" +
            "\t\t\tSUM(t.qtlamount) qtlamount,\n" +
            "\t\t\tSUM(t.gdlamount) + SUM(t.hclamount) + SUM(t.sclamount) + SUM(t.xclamount) + SUM(t.jslamount) + SUM(t.qtlamount) AS yingyeheji,\n" +
            "\t\t\tSUM(t.zhifubaoamount) zhifubaoamount,\n" +
            "\t\t\tSUM(t.weixinamount) weixinamount,\n" +
            "\t\t\tSUM(t.quanjin) quanjin,\n" +
            "\t\t\tSUM(t.yueamount) yueamount,\n" +
            "\t\t\tSUM(t.miandanamount) miandanamount,\n" +
            "\t\t\tSUM(t.tgyouhui) tgyouhui,\n" +
            "\t\t\tSUM(t.discountamount) discountamount,\n" +
            "\t\t\tSUM(t.rmbamount) rmbamount,\n" +
            "\t\t\tSUM(t.bankamount) bankamount,\n" +
            "\t\t\tSUM(t.qiandanamount) qiandanamount,\n" +
            "\t\t\tSUM(t.freeamount) freeamount,\n" +
            "\t\t\tSUM(t.sanbeiamount) sanbeiamount,\n" +
            "\t\t\tSUM(t.payamount) AS heji\n" +
            "\t\tFROM\n" +
            "\t\t\t(\n" +
            "\t\t\t\tSELECT\n" +
            "\t\t\t\t\ta.*, (\n" +
            "\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\ta.youhuiquanjine - a.tuangouyouhui\n" +
            "\t\t\t\t\t\t) * a.youhuiquannum\n" +
            "\t\t\t\t\t) AS quanjin,\n" +
            "\t\t\t\t\t(\n" +
            "\t\t\t\t\t\ta.tuangouyouhui * a.youhuiquannum\n" +
            "\t\t\t\t\t) AS tgyouhui\n" +
            "\t\t\t\tFROM\n" +
            "\t\t\t\t\tgyxg_orders_accounts a\n" +
            "\t\t\t\tWHERE\n" +
            "\t\t\t\t\tDATE_FORMAT(a.overtime, '%Y-%m-%d') >= #{begin_date}\n" +
            "\t\t\t\tAND DATE_FORMAT(a.overtime, '%Y-%m-%d') <= #{end_date}\n" +
            "\t\t\t\tAND a.companyid = #{companyid}\n" +
            "\t\t\t\tAND a.storeid = #{storeid}\n" +
            "\t\t\t) t\n" +
            "\t\tGROUP BY\n" +
            "\t\t\tDATE_FORMAT(overtime, '%Y-%m-%d')\n" +
            "\t) b")
    Map yingyehuizongtotal(Map params);

    @SelectProvider(type = ReportProvider.class,method = "kaorderdetail")
    List<Map> kaorderdetail(Map params);

    /**
     * 原料进销存报表
     * @param map
     * @return
     */
    @Select("call sp_kucunjinxiaocunchaxun (#{companyid},#{storeid},#{warehouseid},#{begin_date},#{end_date})")
    List<Map<String,Object>> invoicing(Map<String, Object> map);
}
