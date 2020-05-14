package com.jzkj.gyxg.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public final class MessageUtil {

    public static final Logger log = LoggerFactory.getLogger(MessageUtil.class);

    public static final String URL="http://61.145.229.26:8086/sms/v2/std/batch_send";
    public static final String USERID = "j94095";
    public static final String PWD = "734694";



    private static Map<Integer, String> errorMap = new HashMap<Integer, String>();


    @PostConstruct
    public void init() {
        //缓存短信模板
        errorMap.put(-100001, "鉴权不通过,请检查账号,密码,时间戳,固定串,以及MD5算法是否按照文档要求进行设置");
        errorMap.put(-100002, "用户多次鉴权不通过,请检查账号,密码,时间戳,固定串,以及MD5算法是否按照文档要求进行设置");
        errorMap.put(-100003, "用户欠费");
        errorMap.put(-100004, "custid或者exdata字段填写不合法");
        errorMap.put(-100011, "短信内容超长");
        errorMap.put(-100012, "手机号码不合法");
        errorMap.put(-100014, "手机号码超过最大支持数量（1000）");
        errorMap.put(-100029, "端口绑定失败");
        errorMap.put(-100056, "用户账号登录的连接数超限");
        errorMap.put(-100057, "用户账号登录的IP错误");
        errorMap.put(-100126, "短信有效存活时间无效");
        errorMap.put(-100252, "业务类型不合法(超长或包含非字母数字字符)");
        errorMap.put(-100253, "自定义参数超长");
        errorMap.put(-100999, "平台数据库内部错误");
    }

    public static Map<String, Object> sendSms(String mobile,String content) {
        int n = 1;
        String formatContent = content.replace("¥", "￥");
        Map<String, String> params = new HashMap<>();
        params.put("userid", USERID);
        params.put("pwd", PWD);
        params.put("mobile", mobile);
        params.put("content", formatContent);
        Map<String, Object> resultMap;
        Map<String, Object> sendresult = new HashMap<>();
        while (true) {
            String responseStr = HttpUtil.doPost(URL, params, "GBK");
            if ("".equals(responseStr)) {
                if (n == 3) {
                    log.info("梦网---->>接口无响应...");
                    return null;
                }
            } else {
                resultMap = JSON.parseObject(responseStr, Map.class);
                break;
            }
            n++;
        }
        sendresult.put("state",(Integer)resultMap.get("result"));
        sendresult.put("msg",errorMap.get((Integer)resultMap.get("result")));
        sendresult.put("msgid",resultMap.get("msgid"));
        return sendresult;
    }
}
