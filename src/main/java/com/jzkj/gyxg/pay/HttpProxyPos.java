package com.jzkj.gyxg.pay;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jzkj.gyxg.util.StringUtil;

import java.security.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ssddp
 * @ClassNameHttpProxyPos
 * @Description: a
 * @date 2019/7/12 13:37
 * @Version 1.0
 **/
public class HttpProxyPos {

    private static String api_domain = "https://vapi.shouqianba.com/";
    private static String appid = "28lpm0000002";
    private static String store_sn = "Gyxg";
    private static String brand_code = "999888";
    //    private String privateKey = "MIIEowIBAAKCAQEAnv4/EglSYJLqt1RdtExQorlNc9a3flV5XxdHZGgSscpJfjMWlULHRkHpXTzFg7exkxAGk4HGwGmsU9UtbFErc/UzZdNo4LrYsmfueYhK+OuJeGU1VQUJNCpQ36gG/UouqFsNhJ79tGxI7YGVqYIsm1ur2OuEiLS7Y+xuXrQhSeDOKhjgDacfVBDdn0LgxU0j2xkCotBffN3mkwXY680RK+T1s+C+DO41OkkEoBLHvZooYfb8FrfnfQn37vqJ1MTwL1teCXupFt1gO0tL8sk7AFHHsrIKrzimUIaBLPi3oPQ0Q+1BsIp7k3NwPakmCBgG/Fdp29K9D+V7IJlExJlWZwIDAQABAoIBAH9k+ORa08bN8YQz9WEiRPodwBGxWhXAJyAqZuns+g27KGgWR2IRv9prtbEfoKOJ7J8JmDtZIQfLpGkHxv2jjE29ixFr8/0ducQ5wrtembQaBmyRq8oT3uwNe/+5guAxMHF1g79gJakLmuOzkVfhFhzltyx4ihl/tya5qzX23zVxxcHUO3oByJgm2nlYH1AWN4FDwdGJ8n93trn690L1eUOezVAseWL2Ij+844+f7hZO+nBgwvQpB9C7WdrmDMjLceYiE5kITtvReyGOxJjsBE2EvWlu7HekSDFOjyf0HFHs+IkyC6jaheno2cdmlkh/M/n4BUlW5+gKOXVC6wqYZQECgYEA5Xcwou5a7Dk+MP4N8b+7DPn3o/HuKkkM6/uRr9g6Bkfxqr6w3fwYkf1bWLUg6SXt0jzhGBMUcnnlpVobmmtgIguPVC8FSVpSap304ki+IKlMBaX4xTD+vuSh30WjGMYmzUgfRXzgQIF3Bfg+CCExFbEHNUwk5igeWRqY3nEYeoUCgYEAsWDhE2ZyuDRVAGbtXUaEysqwaLovuKqj8SUseKXXs7f/EQICbpOlGHH5MxxhbCJfqFx9Yc5iHJbcWpXOrGoOP++YRrfZjZTCskjkWE97IEyup8Wutmgl772HB1n69le3Cv14FQf0QolpcV44lVOH9EqhfHjD6GH+0YdYYjRJPvsCgYEAvYXJLPkzLaJF5I8hE0ephZk72TPr4w781jes55Dus9teFglz6ZTa8lFQzh6j9Q03tQpFW+3+WGKnsv+OhuciulvT4NMJScGJCrg112P/bNiHiq6/npbOAPqzW8aXY9HdoHVuJqVyTrTfipWzHmHTubfCXVnrrBD8p9mY4ziD4EUCgYBiB+Tcz/X/EA0aV8g/kMW7PiIY/y14pfZNQ/o8A4we60WwpKerbTYFOJg9QyYkmSq85cD12RYoLshB2CGM8GBHvacvDlTSBrFDzz1EAUlPJJIybvKMJSsyQFDsIzKsCvZCwKspFGhOjZsU6Lnk7XFp9gUhwaykNeSa8G5MeBEzGQKBgDPv3JTAbYejXJRX+SyBnPCV3eO4CNjG4JvGtOyPWlcOxa6Tj8sifWGcB90F2KEPTJ92pXKHlPAVbPRVtvvDNwydG7rL5MZhfQALfuahbBY95oth1uU9lVr/tBJRU+lHq+5NtFpi+GxR3NXajNL+c+P1NKbyKQgU/Zb9NOMiWyG8";
    private static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCe/j8SCVJgkuq3VF20TFCiuU1z1rd+VXlfF0dkaBKxykl+MxaVQsdGQeldPMWDt7GTEAaTgcbAaaxT1S1sUStz9TNl02jgutiyZ+55iEr464l4ZTVVBQk0KlDfqAb9Si6oWw2Env20bEjtgZWpgiybW6vY64SItLtj7G5etCFJ4M4qGOANpx9UEN2fQuDFTSPbGQKi0F983eaTBdjrzREr5PWz4L4M7jU6SQSgEse9mihh9vwWt+d9Cffu+onUxPAvW14Je6kW3WA7S0vyyTsAUceysgqvOKZQhoEs+Leg9DRD7UGwinuTc3A9qSYIGAb8V2nb0r0P5XsgmUTEmVZnAgMBAAECggEAf2T45FrTxs3xhDP1YSJE+h3AEbFaFcAnICpm6ez6DbsoaBZHYhG/2mu1sR+go4nsnwmYO1khB8ukaQfG/aOMTb2LEWvz/R25xDnCu16ZtBoGbJGryhPe7A17/7mC4DEwcXWDv2AlqQua47ORV+EWHOW3LHiKGX+3JrmrNfbfNXHFwdQ7egHImCbaeVgfUBY3gUPB0Ynyf3e2ufr3QvV5Q57NUCx5YvYiP7zjj5/uFk76cGDC9CkH0LtZ2uYMyMtx5iITmQhO29F7IY7EmOwETYS9aW7sd6RIMU6PJ/QcUez4iTILqNqF6ejZx2aWSH8z+fgFSVbn6Ao5dULrCphlAQKBgQDldzCi7lrsOT4w/g3xv7sM+fej8e4qSQzr+5Gv2DoGR/GqvrDd/BiR/VtYtSDpJe3SPOEYExRyeeWlWhuaa2AiC49ULwVJWlJqnfTiSL4gqUwFpfjFMP6+5KHfRaMYxibNSB9FfOBAgXcF+D4IITEVsQc1TCTmKB5ZGpjecRh6hQKBgQCxYOETZnK4NFUAZu1dRoTKyrBoui+4qqPxJSx4pdezt/8RAgJuk6UYcfkzHGFsIl+oXH1hzmIcltxalc6sag4/75hGt9mNlMKySORYT3sgTK6nxa62aCXvvYcHWfr2V7cK/XgVB/RCiWlxXjiVU4f0SqF8eMPoYf7Rh1hiNEk++wKBgQC9hcks+TMtokXkjyETR6mFmTvZM+vjDvzWN6znkO6z214WCXPplNryUVDOHqP1DTe1CkVb7f5YYqey/46G5yK6W9Pg0wlJwYkKuDXXY/9s2IeKrr+els4A+rNbxpdj0d2gdW4mpXJOtN+KlbMeYdO5t8JdWeusEPyn2ZjjOIPgRQKBgGIH5NzP9f8QDRpXyD+Qxbs+Ihj/LXil9k1D+jwDjB7rRbCkp6ttNgU4mD1DJiSZKrzlwPXZFiguyEHYIYzwYEe9py8OVNIGsUPPPUQBSU8kkjJu8owlKzJAUOwjMqwK9kLAqykUaE6NmxToueTtcWn2BSHBrKQ15Jrwbkx4ETMZAoGAM+/clMBth6NclFf5LIGc8JXd47gI2Mbgm8a07I9aVw7FrpOPyyJ9YZwH3QXYoQ9Mn3alcoeU8BVs9FW2+8M3DJ0busvkxmF9AAt+5qFsFj3mi2HW5T2VWv+0ElFT6Uer7k20WmL4bFHc1dqM0v5z4/U0pvIpCBT9lv004yJbIbw=";

    //获取时间
    public static String date() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String sales_time1 = formatter.format(date);
        return sales_time1;
    }


    /**
     * 发起支付请求
     *
     * @param store_name     商户门店名称
     * @param workstation_sn 门店收银编号 如果没有传入0
     * @param check_sn       商户订单号 在商户系统中唯一
     * @param amount         订单价格 精确到分
     * @param currency       币种 如“156”for CNY
     * @param subject        订单主题
     * @param description    订单描述
     * @param operator       操作员
     * @param customer       客户信息
     * @param industry_code  行业代码 0:零售 1:酒店 2:餐饮 4:教育
     * @param pos_info       本接口对接的对端信息
     * @param resolution     是否支持拆单 1:不支持 2:支持
     * @param request_id
     * @return
     */
    public static Map<String, Object> pos(String store_name, String workstation_sn,
                             String check_sn, String amount, String currency,
                             String subject, String description, String operator, String customer, String industry_code,
                             String pos_info, String resolution, String request_id, String reflect) throws JSONException {
        String url = api_domain + "api/lite-pos/v1/sales/purchase";

        //item
        JSONObject itemsJson = new JSONObject();
        List<JSONObject> items = new ArrayList<>();

        //tenders
        JSONObject tendersJson = new JSONObject();
        List<JSONObject> tenders = new ArrayList<>();

        try {
            //head
            JSONObject headJson = new JSONObject();
            headJson.put("version", "1.0.0");
            headJson.put("appid", appid);
            headJson.put("request_time", date());
            // headJson.put("reserve","{}");

            //body
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("brand_code", brand_code);
            bodyJson.put("store_sn", store_sn);
            bodyJson.put("store_name", store_name);
            bodyJson.put("workstation_sn", workstation_sn);
            bodyJson.put("check_sn", check_sn);
            bodyJson.put("sales_time", date());
            bodyJson.put("amount", amount);
            bodyJson.put("currency", currency);
            bodyJson.put("subject", subject);
            bodyJson.put("description", description);
            bodyJson.put("operator", operator);
            bodyJson.put("customer", customer);
            bodyJson.put("industry_code", industry_code);
            bodyJson.put("pos_info", pos_info);
            bodyJson.put("resolution", resolution);
            bodyJson.put("request_id", request_id);
            bodyJson.put("reflect", reflect);


            //request
            JSONObject requestJson = new JSONObject();
            requestJson.put("head", headJson);
            requestJson.put("body", bodyJson);

            //全部
            JSONObject request = new JSONObject();
            request.put("request", requestJson);

            String sign = RSASignature.sign(requestJson.toString(), privateKey, "UTF-8");//使用私钥签名，并转成BASE64编码
            request.put("signature", sign);

            String result = HttpUtil.httpPostPos(url, request.toString(), sign); //请求
            Map<String, Object> map = new HashMap<String, Object>();

            JSONObject jsonObject = JSON.parseObject(result).getJSONObject("response").getJSONObject("body");
            if ("200".equals(jsonObject.get("result_code") + "")) {
                JSONObject biz_response = jsonObject.getJSONObject("biz_response");
                if ("200".equals(biz_response.get("result_code") + "")) {
                    map.put("result_code", biz_response.get("result_code") + "");
                    map.put("order_sn", biz_response.getJSONObject("data").get("order_sn") + "");
                } else {
                    map.put("result_code", biz_response.get("result_code") + "");
                    map.put("error_code", biz_response.get("error_code") + "");
                    map.put("error_message", biz_response.get("error_message") + "");
                }
            } else {
                map.put("result_code", jsonObject.get("result_code") + "");
                map.put("error_code", jsonObject.get("error_code") + "");
                map.put("error_message", jsonObject.get("error_message") + "");
            }

            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpProxyPos httpProxyPos1 = new HttpProxyPos();
//        String orderNum = "pHRSAXQ5YeqSH38";
//        String requestid = "HrqcrfMdgv9FMKDk";
        String orderNum = StringUtil.getRandomString(15);
        String requestid = StringUtil.getRandomString(16);
        System.out.println(orderNum + ":" + requestid);
//        Map<String, Object> map = httpProxyPos1.pos("古渝雄关", "0", orderNum, "1", "156",
//                "买单", "古渝雄关", "001", "001", "2", "半夜鸡叫",
//                "2", requestid, "");
//        System.out.println(map);
//        System.out.println(httpProxyPos1.cancle("0", "Pbf9qBYMcxGCKxd",
//                "200107999888000005", "", requestid));
        System.out.println(httpProxyPos1.query("MYnrRBmT5vUrJJJ", "200114999888000002"));
//        System.out.println(httpProxyPos1.refund(orderNum, "-1", requestid,
//                "WctnTUX8FTNYtFa", "620011400000011469"));
    }

    /**
     * 退款
     *
     * @param check_sn
     * @param amount
     * @return
     */
    public static Map<String, Object> refund(String check_sn, String amount, String request_id, String transaction_sn, String original_tender_sn) {
        String url = api_domain + "/api/lite-pos/v1/sales/refund";


        JSONObject tendersJson = new JSONObject();
        tendersJson.put("transaction_sn", transaction_sn);
        tendersJson.put("amount", "-1");
        tendersJson.put("pay_status", "0");
        tendersJson.put("create_time", date());
        tendersJson.put("original_tender_sn", original_tender_sn);

        List<JSONObject> tenders = new ArrayList<>();
        tenders.add(tendersJson);

        JSONObject headJson = new JSONObject();
        headJson.put("version", "2.0.0");
        headJson.put("appid", appid);
        headJson.put("request_time", date());
        headJson.put("reserve", "{}");
        //body
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("brand_code", brand_code);
        bodyJson.put("store_sn", store_sn);
        bodyJson.put("store_name", "gyxg");
        bodyJson.put("workstation_sn", "0");
        bodyJson.put("check_sn", check_sn);
        bodyJson.put("sales_time", date());
        bodyJson.put("request_id", request_id);
        bodyJson.put("amount", amount);
        bodyJson.put("currency", "156");
        bodyJson.put("subject", "古渝雄关退款");
        bodyJson.put("description", "退款");
        bodyJson.put("operator", "gyxg001");
        bodyJson.put("customer", "gyxg001");
        bodyJson.put("industry_code", "2");
        bodyJson.put("pos_info", "gyxg");
        bodyJson.put("tenders", tenders);


        //request
        JSONObject requestJson = new JSONObject();
        requestJson.put("head", headJson);
        requestJson.put("body", bodyJson);
        //全部
        JSONObject request = new JSONObject();
        request.put("request", requestJson);
        String sign = RSASignature.sign(requestJson.toString(), privateKey, "UTF-8");//使用私钥签名，并转成BASE64编码
        request.put("signature", sign);
        String result = null; //请求
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            result = HttpUtil.httpPostPos(url, request.toString(), sign);

            JSONObject jsonObject = JSON.parseObject(result).getJSONObject("response").getJSONObject("body");
            if ("200".equals(jsonObject.get("result_code") + "")) {
                JSONObject biz_response = jsonObject.getJSONObject("biz_response");
                if ("200".equals(biz_response.get("result_code") + "")) {
                    map.put("result_code", biz_response.get("result_code") + "");
                    map.put("order_sn", biz_response.getJSONObject("data").get("order_sn") + "");
                } else {
                    map.put("result_code", biz_response.get("result_code") + "");
                    map.put("error_code", biz_response.get("error_code") + "");
                    map.put("error_message", biz_response.get("error_message") + "");
                }
            } else {
                map.put("result_code", jsonObject.get("result_code") + "");
                map.put("error_code", jsonObject.get("error_code") + "");
                map.put("error_message", jsonObject.get("error_message") + "");
            }

        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 订单取消
     *
     * @param original_workstation_sn 原始门店收银编号，如果没有请传入0
     * @param original_check_sn       商户订单号
     * @param original_order_sn       本系统为该订单生成的订单序列号
     * @param reflect                 反射参数，可以在订单结果通知中返回
     * @return
     */
    public static String cancle(String original_workstation_sn, String original_check_sn,
                              String original_order_sn, String reflect, String request_id){
        String url = api_domain + "/api/lite-pos/v1/sales/void";
        //head
        JSONObject headJson = new JSONObject();
        headJson.put("version", "2.0.0");
        headJson.put("appid", appid);
        headJson.put("request_time", date());
        headJson.put("reserve", "{}");

        //body
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("brand_code", brand_code);
        bodyJson.put("original_workstation_sn", original_workstation_sn);
        bodyJson.put("original_store_sn", store_sn);
        bodyJson.put("original_check_sn", original_check_sn);
        bodyJson.put("original_order_sn", original_order_sn);
        bodyJson.put("reflect", reflect);
        bodyJson.put("request_id", request_id);


        //request
        JSONObject requestJson = new JSONObject();
        requestJson.put("head", headJson);
        requestJson.put("body", bodyJson);

        //全部
        JSONObject request = new JSONObject();
        request.put("request", requestJson);

        String sign = RSASignature.sign(requestJson.toString(), privateKey, "UTF-8");//使用私钥签名，并转成BASE64编码
        request.put("signature", sign);

        String result = null; //请求
        try {
            result = HttpUtil.httpPostPos(url, request.toString(), sign);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询
     *
     * @param check_sn
     * @param order_sn
     * @return
     * @throws JSONException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     */
    public static Map<String, Object> query(String check_sn, String order_sn){
        String url = api_domain + "/api/lite-pos/v1/sales/query";

        //head
        JSONObject headJson = new JSONObject();
        headJson.put("version", "2.0.0");
        headJson.put("appid", appid);
        headJson.put("request_time", date());
        headJson.put("reserve", "{}");

        //body
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("brand_code", brand_code);
        bodyJson.put("store_sn", store_sn);
        bodyJson.put("workstation_sn", "0");
        bodyJson.put("check_sn", check_sn);
        bodyJson.put("order_sn", order_sn);

        //request
        JSONObject requestJson = new JSONObject();
        requestJson.put("head", headJson);
        requestJson.put("body", bodyJson);

        //全部
        JSONObject request = new JSONObject();
        request.put("request", requestJson);

        String sign = RSASignature.sign(requestJson.toString(), privateKey, "UTF-8");//使用私钥签名，并转成BASE64编码
        request.put("signature", sign);
        System.out.println("request.toString():" + request.toString());
        String result = null; //请求
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            result = HttpUtil.httpPostPos(url, request.toString(), sign);
            System.out.println(result);
            JSONObject jsonObject = JSON.parseObject(result).getJSONObject("response").getJSONObject("body");
            if ("200".equals(jsonObject.get("result_code") + "")) {
                JSONObject biz_response = jsonObject.getJSONObject("biz_response");
                if ("200".equals(biz_response.get("result_code") + "")) {
                    JSONArray tenders = biz_response.getJSONObject("data").getJSONArray("tenders");
                    map.put("result_code", biz_response.get("result_code") + "");
                    map.put("order_status", biz_response.getJSONObject("data").get("order_status") + "");
                    if ("4".equals(biz_response.getJSONObject("data").get("order_status") + "")) {
                        if (tenders != null && tenders.size() > 0) {
                            JSONObject info = tenders.getJSONObject(0);
                            //支付/退款成功后，轻 POS 生成的唯一流水号
                            map.put("tender_sn", info.get("tender_sn") + "");
                            //支付状态, 1:待操作; 2:支付中; 3:支付成功; 4:退款中; 5:退款成功;6:退款失败;7:支付失败;8:未知状态;
                            map.put("pay_status", info.get("pay_status") + "");
                            map.put("tender_type", info.get("tender_type") + "");  //支付方式类型：0-其他，1-预授权完成，2-银行卡，3-QRCode，4-分期，99-外部
                            /*
                            二级支付方式类型：
                            001-现金，如需在轻 POS 录入其他支付方
                            式，在对接时与收钱吧沟通配置；
                            101-银行卡预授权完成；
                            201-银行卡；
                            301-微信，302-支付宝；
                            401-银行卡分期
                            */
                            map.put("sub_tender_type", info.get("sub_tender_type") + "");
                        }
                    }
                } else {
                    map.put("result_code", biz_response.get("result_code") + "");
                    map.put("error_code", biz_response.get("error_code") + "");
                    map.put("error_message", biz_response.get("error_message") + "");
                }
            } else {
                map.put("result_code", jsonObject.get("result_code") + "");
                map.put("error_code", jsonObject.get("error_code") + "");
                map.put("error_message", jsonObject.get("error_message") + "");
            }

        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        System.out.println("result:" + result);

        return map;
    }


}

