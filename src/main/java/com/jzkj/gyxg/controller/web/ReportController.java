package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("api/report/")
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;


    /**
     * 退菜明细
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("tuicaimingxi")
    public ResponseJson tuicaimingxi() throws AjaxOperationFailException {
        return reportService.tuicaimingxi();
    }


    /**
     * 赠菜明细
     */
    @RequestMapping("zengcaimingxi")
    public ResponseJson zengcaimingxi() throws AjaxOperationFailException {
        return reportService.zengcaimingxi();
    }

    /**
     * 菜品销售统计
     */
    @RequestMapping("caipinxiaoshoutongji")
    public ResponseJson caipinxiaoshoutongji() throws AjaxOperationFailException {
        return reportService.caipinxiaoshoutongji();
    }



    /**
     * 酒水单
     */
    @RequestMapping("jiushuidan")
    public ResponseJson jiushuidan() throws AjaxOperationFailException {
        return reportService.jiushuidan();
    }

    /**
     * 签单月份汇总报告
     */
    @RequestMapping("qiandan")
    public ResponseJson qiandan() throws AjaxOperationFailException {
        return reportService.qiandan();
    }


    /**
     * 账单付款明细查询
     */
    @RequestMapping("zhangdanfukuanmingxi")
    public ResponseJson zhangdanfukuanmingxi() throws AjaxOperationFailException {
        return reportService.zhangdanfukuanmingxi();
    }


    /**
     * 结算方式汇总
     */
    @RequestMapping("jiesuanfangshihuizongchaxun")
    public ResponseJson jiesuanfangshihuizongchaxun() throws AjaxOperationFailException {
        return reportService.jiesuanfangshihuizongchaxun();
    }


    /**
     * 营业汇总
     */
    @RequestMapping("yingyehuizong")
    public ResponseJson yingyehuizong() throws AjaxOperationFailException {
        return reportService.yingyehuizong();
    }


    /**
     * 优惠卡往来账目报表
     */
    @RequestMapping("kaorderdetail")
    public ResponseJson kaorderdetail() throws AjaxOperationFailException {
        return reportService.kaorderdetail();
    }

    /**
     * 原料进销存报表
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("invoicing")
    public ResponseJson invoicing () throws AjaxOperationFailException {
        return reportService.invoicing();
    }


}
