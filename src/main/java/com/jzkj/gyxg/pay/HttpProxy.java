package com.jzkj.gyxg.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.entity.Payinfo;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class HttpProxy {
    private String api_domain;
    private final static String CHARSET_UTF8 = "utf8";
    @Resource
    private RedisSession redisSession;

    public HttpProxy(String domain) {
        api_domain = domain;
    }

    /**
     * 计算字符串的MD5值
     *
     * @param signStr:签名字符串
     * @return
     */
    public String getSign(String signStr) {
        try {
            String md5 = MD5Util.encryptMd5(signStr);
            return md5;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getClient_Sn(int codeLenth) {
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < codeLenth; i++) {
                if (i == 0)
                    sb.append(new Random().nextInt(9) + 1); // first field will not start with 0.
                else
                    sb.append(new Random().nextInt(10));
            }
            return sb.toString();
        }
    }

    /**
     * 终端激活
     *
     * @param code:激活码
     * @param vendor_sn:服务商序列号
     * @param vendor_key:服务商密钥
     * @param appid:应用编号
     * @return {terminal_sn:"$终端号",terminal_key:"$终端密钥"}
     */
    public JSONObject activate(String vendor_sn, String vendor_key, String appid, String code) {
        String url = api_domain + "/terminal/activate";
        JSONObject params = new JSONObject();
        try {
            params.put("appid", appid);                                   //appid，必填
            params.put("code", code);                                     //激活码，必填
            params.put("device_id", "CNHM0001POS01");                     //客户方收银终端序列号，需保证同一appid下唯一，必填。为方便识别，建议格式为“品牌名+门店编号+‘POS’+POS编号“
            params.put("client_sn", "POS01");                             //客户方终端编号，一般客户方或系统给收银终端的编号，必填
            params.put("name", "1号款台");                                 //客户方终端名称，必填
            params.put("os_info", "Mac OS");
            params.put("sdk_version", "Java SDK v1.0");     //SDK版本

            String sign = getSign(params.toString() + vendor_key);
            String result = HttpUtil.httpPost(url, params.toString(), sign, vendor_sn);
            JSONObject retObj = JSONObject.parseObject(result);
            String resCode = retObj.get("result_code").toString();
            if (!resCode.equals("200"))
                return null;
            String responseStr = retObj.get("biz_response").toString();
            JSONObject terminal = JSONObject.parseObject(responseStr);
            if (terminal.get("terminal_sn") == null || terminal.get("terminal_key") == null)
                return null;
            return terminal;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 终端签到
     *
     * @param terminal_sn:终端号
     * @param terminal_key:终端密钥
     * @return {terminal_sn:"$终端号",terminal_key:"$终端密钥"}
     */
    public JSONObject checkin(String terminal_sn, String terminal_key) {
        String url = api_domain + "/terminal/checkin";
        JSONObject params = new JSONObject();
        try {
            params.put("terminal_sn", terminal_sn);
            params.put("device_id", "CNHM0001POS01");
            params.put("os_info", "Mac OS");
            params.put("sdk_version", "Java SDK v1.0");
            String sign = getSign(params.toString() + terminal_key);
            String result = HttpUtil.httpPost(url, params.toString(), sign, terminal_sn);
            JSONObject retObj = JSONObject.parseObject(result);
            String resCode = retObj.get("result_code").toString();
            if (!resCode.equals("200"))
                return null;
            String responseStr = retObj.get("biz_response").toString();
            JSONObject terminal = JSONObject.parseObject(responseStr);
            if (terminal.get("terminal_sn") == null || terminal.get("terminal_key") == null)
                return null;
            return terminal;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 付款
     *
     * @param terminal_sn:终端号
     * @param terminal_key:终端密钥
     * @return
     */
    public Map<String, Object> pay(String terminal_sn, String terminal_key, String orderNum, String amount,
                                   String payway, String dynamicId, String notify_url, String subject, String operator) {
        String url = api_domain + "/upay/v2/pay";
        JSONObject params = new JSONObject();
        try {
            params.put("terminal_sn", terminal_sn);           //终端号
            params.put("client_sn", orderNum);  //商户系统订单号,必须在商户系统内唯一；且长度不超过64字节
            params.put("total_amount", amount);               //交易总金额,以分为单位
            params.put("payway", payway);                         //支付方式,1:支付宝 3:微信 4:百付宝 5:京东钱包
            params.put("dynamic_id", dynamicId);                   //条码内容
            params.put("subject", subject);                     //交易简介
            params.put("operator", operator);                     //门店操作员
            params.put("notify_url", notify_url);              //回调地址

            String sign = getSign(params.toString() + terminal_key);
            String result = HttpUtil.httpPost(url, params.toString(), sign, terminal_sn);
            JSONObject jsonObject = JSON.parseObject(result);
            String resCode = jsonObject.get("result_code").toString();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result_code", jsonObject.get("result_code"));
            if (!resCode.equals("200")) {
                map.put("error_code", jsonObject.get("error_code"));
                map.put("error_message", jsonObject.get("error_message"));
                return map;
            }
            JSONObject bizResponse = jsonObject.getJSONObject("biz_response");
            map.put("pay_code", bizResponse.get("result_code") + "");
            if ("PAY_FAIL".equals(bizResponse.get("result_code") + "")) {
                map.put("error_code", bizResponse.get("error_code"));
                map.put("error_message", bizResponse.get("error_message"));
            } else {
                JSONObject data = bizResponse.getJSONObject("data");
                map.put("sn", data.get("sn"));
                map.put("order_status", data.get("order_status"));
                map.put("payway", data.get("payway"));
                map.put("sub_payway", data.get("sub_payway"));
            }
            System.out.println("支付信息：" + map);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 退款
     *
     * @param terminal_sn:终端号
     * @param terminal_key:终端密钥
     * @return
     */
    public Map<String, Object> refund(String terminal_sn, String terminal_key, String sn, String orderNum, String amount, String refundNo, String operator) {
        String url = api_domain + "/upay/v2/refund";
        JSONObject params = new JSONObject();
        try {
            params.put("terminal_sn", terminal_sn);            //收钱吧终端ID
            params.put("sn", sn);              //收钱吧系统内部唯一订单号
            params.put("client_sn", orderNum);   //商户系统订单号,必须在商户系统内唯一；且长度不超过64字节
            params.put("refund_amount", amount);               //退款金额
            params.put("refund_request_no", refundNo);          //商户退款所需序列号,表明是第几次退款
            params.put("operator", operator);                      //门店操作员

            String sign = getSign(params.toString() + terminal_key);
            String result = HttpUtil.httpPost(url, params.toString(), sign, terminal_sn);
            JSONObject jsonObject = JSON.parseObject(result);
            String resCode = jsonObject.get("result_code").toString();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result_code", jsonObject.get("result_code"));
            if (!resCode.equals("200")) {
                map.put("error_code", jsonObject.get("error_code"));
                map.put("error_message", jsonObject.get("error_message"));
                return map;
            }
            JSONObject bizResponse = jsonObject.getJSONObject("biz_response");
            if ("REFUND_SUCCESS".equals(bizResponse.get("result_code") + "")) {
                JSONObject data = bizResponse.getJSONObject("data");
                map.put("order_status", data.get("order_status"));
            } else {
                map.put("result_code", bizResponse.get("result_code"));
                map.put("error_code", bizResponse.get("error_code"));
                map.put("error_message", bizResponse.get("error_message"));
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询
     *
     * @param terminal_sn:终端号
     * @param terminal_key:终端密钥
     * @param sn                收钱吧系统内部唯一订单号
     * @param orderNum          商户系统订单号,必须在商户系统内唯一；且长度不超过64字节
     * @return
     */
    public Map<String, Object> query(String terminal_sn, String terminal_key, String sn, String orderNum) {
        String url = api_domain + "/upay/v2/query";
        JSONObject params = new JSONObject();
        //PAID              订单支付成功
        //PAY_CANCELED      支付失败并且已经成功充正
        //REFUNDED          已成功全额退款
        //PARTIAL_REFUNDED  已成功部分退款
        //CANCELED          客户端发起的撤单已成功
        return getString(terminal_sn, terminal_key, sn, orderNum, url, params, "SUCCESS");
    }

    /**
     * 自动撤单
     *
     * @param terminal_sn:终端号
     * @param terminal_key:终端密钥
     * @return
     */
    public Map<String, Object> cancel(String terminal_sn, String terminal_key, String sn, String orderNum) {
        String url = api_domain + "/upay/v2/cancel";
        JSONObject params = new JSONObject();
        return getString(terminal_sn, terminal_key, sn, orderNum, url, params, ",CANCELED,");
    }

    private Map<String, Object> getString(String terminal_sn, String terminal_key, String sn, String orderNum,
                                          String url, JSONObject params, String status) {
        try {
            params.put("terminal_sn", terminal_sn);           //终端号
            params.put("sn", sn);             //收钱吧系统内部唯一订单号
            params.put("client_sn", orderNum);  //商户系统订单号,必须在商户系统内唯一；且长度不超过64字节
            String sign = getSign(params.toString() + terminal_key);
            String result = HttpUtil.httpPost(url, params.toString(), sign, terminal_sn);
            JSONObject jsonObject = JSON.parseObject(result);
            String resCode = jsonObject.get("result_code").toString();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result_code", jsonObject.get("result_code"));
            if (!resCode.equals("200")) {
                map.put("error_code", jsonObject.get("error_code"));
                map.put("error_message", jsonObject.get("error_message"));
                return map;
            }
            JSONObject bizResponse = jsonObject.getJSONObject("biz_response");
            map.put("biz_result_code", bizResponse.get("result_code"));
            if (status.equals(bizResponse.get("result_code") + "")) {
                JSONObject data = bizResponse.getJSONObject("data");
                map.put("order_status", data.get("order_status"));
                map.put("sn", data.get("sn"));
            } else {
                map.put("biz_result_code", bizResponse.get("result_code"));
                map.put("error_code", bizResponse.get("error_code"));
                map.put("error_message", bizResponse.get("error_message"));
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 预下单
     * @param terminal_sn
     * @param terminal_key
     * @param OrderNum
     * @param amount
     * @param payway
     * @param subject
     * @param operator
     * @param subPayway
     * @return
     */
    public String precreate(String terminal_sn, String terminal_key, String OrderNum, String amount, String payway, String subject, String operator, String subPayway) {
        String url = api_domain + "/upay/v2/precreate";
        JSONObject params = new JSONObject();
        try {
            params.put("terminal_sn", terminal_sn);           //收钱吧终端ID
            params.put("client_sn", OrderNum);  //商户系统订单号,必须在商户系统内唯一；且长度不超过32字节
            params.put("total_amount", amount);               //交易总金额
            params.put("payway", payway);                         //支付方式
            params.put("subject", subject);                     //交易简介
            params.put("operator", operator);                     //门店操作员
            if (!StringUtil.isNull(subPayway)) {
                params.put("sub_payway", subPayway);                     //二级支付方式
            }

            String sign = getSign(params.toString() + terminal_key);
            String result = HttpUtil.httpPost(url, params.toString(), sign, terminal_sn);

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public String createSign(SortedMap<String, String> packageParams, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        return sb.toString();

    }

    public String getWay(String terminal_sn, String terminal_key, String orderNum, String amount, String subject, String operator, String returnUrl) {
        String url = "https://qr.shouqianba.com/gateway";
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
            String result = com.jzkj.gyxg.util.HttpUtil.doPost(url, params, "utf-8");

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encodeURIComponent(String component) {
        try {
            return URLEncoder.encode(component, "UTF-8")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return component;
        }
    }


    /**
     * 排序
     *
     * @param params:签名字符串
     * @return
     */
    public static List<Map.Entry<String, String>> sortMap(final Map<String, String> params) {
        final List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(params.entrySet());

        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
            public int compare(final Map.Entry<String, String> o1, final Map.Entry<String, String> o2) {
                return (o1.getKey().toString().compareTo(o2.getKey()));
            }
        });
        return infos;
    }

    /**
     *  微信支付
     * @param clientSn      订单号
     * @param totalAmount   价格
     * @param returnUrl     页面跳转同步通知页面路径
     * @param subject       交易概述
     * @param operator      门店操作员
     * @param terminalSn    sn
     * @param terminalKey   key
     * @return
     * @throws JSONException
     */
    public String wapapipro(String clientSn, String totalAmount, String returnUrl, String subject, String operator, String terminalSn, String terminalKey) throws JSONException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("description", "订单支付");  //商品详情
        params.put("client_sn", clientSn);
        //params.put("notify_url", "http://llp-love-llx.com/shouqianba/callback/orderdetail"); // 	服务器异步回调 url
        params.put("total_amount", totalAmount);  //交易总金额
        params.put("return_url", returnUrl);  //页面跳转同步通知页面路径
        params.put("terminal_sn", terminalSn);
        //params.put("payway","3");
        params.put("subject", subject);  //交易概述
        params.put("operator", operator);  //门店操作员
        // params.put("reflect","302房间");

        //签名
        final List<Map.Entry<String, String>> list = sortMap(params);
        StringBuffer str = new StringBuffer();
        //拼接参数字符串
        for (final Map.Entry<String, String> m : list) {
            String ss = m.getKey() + "=" + m.getValue() + "&";
            str.append(ss);
        }

        // 拼接密钥
        String psign = str.toString() + "key=" + terminalKey;
        String sign = getSign(psign).toUpperCase();        //加密转大写   getSign为获取加密后的内容

        //转译
        final Map<String, String> paramsEncode = new HashMap<String, String>();
        StringBuffer strEncode = new StringBuffer();

        Set<Map.Entry<String, String>> entrys = params.entrySet();  //此行可省略，直接将map.entrySet()写在for-each循环的条件中
        for (Map.Entry<String, String> entry : entrys) {
            paramsEncode.put(entry.getKey(), encodeURIComponent(entry.getValue()));
        }
        final List<Map.Entry<String, String>> listEncode = sortMap(paramsEncode);
        //拼接参数字符串
        for (final Map.Entry<String, String> m : listEncode) {
            String ss = m.getKey() + "=" + m.getValue() + "&";
            strEncode.append(ss);
        }


        StringBuffer newStr = new StringBuffer("https://qr.shouqianba.com/gateway?");
        newStr.append(strEncode + "sign=" + sign);
        return newStr.toString();
    }


}
