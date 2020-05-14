package com.jzkj.gyxg.controller.app;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.pay.HttpProxy;
import com.jzkj.gyxg.service.app.AppUserService;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
@RequestMapping("/app/store")
public class StoreController extends BaseController {
    @Resource
    private AppUserService appUserService;

    /**
     * 获取店铺列表信息
     * @return
     */
    @RequestMapping("/getStores")
    public ResponseJson getStores() {
        return appUserService.getStores();
    }

    /**
     * 获取一级店铺菜品分类
     * @return
     */
    @RequestMapping("/getFirstDisheClassifys")
    public ResponseJson getFirstDisheClassifys() throws AjaxOperationFailException {
        return appUserService.getFirstDisheClassifys();
    }

    /**
     * 获取二级店铺菜品分类
     * @return
     */
    @RequestMapping("/getSecondDisheClassifys")
    public ResponseJson getSecondDisheClassifys() throws AjaxOperationFailException {
        return appUserService.getSecondDisheClassifys();
    }

    /**
     * 获取菜品分类
     * @return
     */
    @RequestMapping("/getAllDisheClassifys")
    public ResponseJson getAllDisheClassifys() throws AjaxOperationFailException {
        return appUserService.getAllDisheClassifys();
    }

    /**
     * 根据菜品分类获取菜品
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getDisheByClassifys")
    public ResponseJson getDisheByClassifys() throws AjaxOperationFailException {
        return appUserService.getDisheByClassifys();
    }

    /**
     * 根据名称搜索菜品
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getDishesBySearchName")
    public ResponseJson getDishesBySearchName() throws AjaxOperationFailException {
        return appUserService.getDishesBySearchName(0);
    }

    /**
     * 获取热门搜索
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getHotDishes")
    public ResponseJson getHotDishes() throws AjaxOperationFailException {
        return appUserService.getHotDishes();
    }

    @RequestMapping("/getPayWay")
    public void getPayWay(HttpServletResponse resp) {
        /*
        * String url = "https://qr.shouqianba.com/gateway";
        SortedMap<String, String> params = new TreeMap<String, String>();
        try {
            params.put("terminal_sn", terminal_sn);           //收钱吧终端ID
            params.put("client_sn", orderNum);  //商户系统订单号,必须在商户系统内唯一；且长度不超过32字节
            params.put("total_amount", amount);               //交易总金额
            params.put("subject", subject);                     //交易简介
            params.put("operator", operator);                     //门店操作员
            params.put("return_url", returnUrl);                     //门店操作员
            String str = createSign(params, terminal_key);
            String sign = getSign(str).toUpperCase();
            params.put("sign", sign);
            */
//        SortedMap<String, String> params = new TreeMap<String, String>();
        try {
//            String sign = StringUtil.md5(str).toUpperCase();
//            params.put("terminal_sn", "100009200009978067");           //收钱吧终端ID
//            int ordernum = StringUtil.getRandom(15);
//            System.out.println(ordernum);
//            params.put("client_sn", ordernum + "");  //商户系统订单号,必须在商户系统内唯一；且长度不超过32字节
//            params.put("total_amount", "0.01");               //交易总金额
//            params.put("subject", "支付");                     //交易简介
//            params.put("operator", "001");                     //门店操作员
//            params.put("return_url", "http://www.cqgyxg.com");                     //回调地址
//            String str = new HttpProxy("https://api.shouqianba.com").createSign(params, "1222df4864f36499a9cf5f1eb871f931");
//            String sign = new HttpProxy("https://api.shouqianba.com").getSign(str).toUpperCase();
            resp.sendRedirect(new HttpProxy("https://api.shouqianba.com")
                    .wapapipro("100009200009978012", "1", "http://www.cqgyxg.com", "订单支付",
                            "gyxg001", "100009200009978067", "54c7cb1f2ca5cf63b4305843dd9d4342"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
