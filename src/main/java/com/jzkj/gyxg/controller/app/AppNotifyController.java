package com.jzkj.gyxg.controller.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RequestMapping("/app/notify")
@RestController
public class AppNotifyController {
    @RequestMapping("/payReturn")
    public void payReturn(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("result_code:====" + req.getParameter("result_code"));
        System.out.println("error_code:=====" + req.getParameter("error_code"));
        System.out.println("error_message:===" + req.getParameter("error_message"));
        System.out.println("terminal_sn:===" + req.getParameter("terminal_sn"));
        System.out.println("sn:===" + req.getParameter("sn"));
        System.out.println("client_sn:===" + req.getParameter("client_sn"));
        System.out.println("trade_no:===" + req.getParameter("trade_no"));
        System.out.println("status:===" + req.getParameter("status"));
        System.out.println("order_status:===" + req.getParameter("order_status"));
        System.out.println("payway:===" + req.getParameter("payway"));
        System.out.println("payway_name:===" + req.getParameter("payway_name"));
        System.out.println("sub_payway:===" + req.getParameter("sub_payway"));
        System.out.println("payer_uid:===" + req.getParameter("payer_uid"));
        System.out.println("payer_login:===" + req.getParameter("payer_login"));
        System.out.println("total_amount:===" + req.getParameter("total_amount"));
        System.out.println("net_amount:===" + req.getParameter("net_amount"));
        System.out.println("subject:===" + req.getParameter("subject"));
        System.out.println("finish_time:===" + req.getParameter("finish_time"));
        System.out.println("channel_finish_time:===" + req.getParameter("channel_finish_time"));
        System.out.println("operator:===" + req.getParameter("operator"));
        System.out.println("reflect:===" + req.getParameter("reflect"));
        System.out.println("payment_list:===" + req.getParameter("payment_list"));

        PrintWriter out;
        try {
            out = resp.getWriter();
            out.write("success");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
