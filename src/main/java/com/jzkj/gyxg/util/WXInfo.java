package com.jzkj.gyxg.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jzkj.gyxg.common.RedisSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by xufan on 2017/8/29.
 */
public class WXInfo {
    public static String APP_ID = "wx0f3eededf226f82a";
    public static String APP_SECRET = "4aefef2cdb02175ef0cbc3711290fa95";

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
//    public static String getToken(RedisSession redisSession) {
        long nowtime = new Date().getTime();
//        Map<String, Object> tokeninfo = redisSession.getMapValue("wxtoken");
//        if(StringUtil.isMapNull(tokeninfo))
//            tokeninfo = new HashMap<String, Object>();
//        else {
//            String tokentime = tokeninfo.get("tokentime") + "";
//            if(!StringUtil.isNull(tokentime)) {
//                Long time = Long.parseLong(tokentime);
//                if(time != null && time + 100 * 60 * 1000 >= nowtime) {
//                    String access_token = tokeninfo.get("token") + "";
//                    if(!StringUtil.isNull(access_token))
//                        return access_token;
//                }
//            }
//        }

        Map<String, Object> tokeninfo = new HashMap<String, Object>();
        String url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential&appid=" + APP_ID + "&secret=" + APP_SECRET;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            response.addHeader("Content-Type", "text/html;charset=UTF-8");
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String inputline = "";
            while ((inputline = rd.readLine()) != null) {
                result.append(inputline);
            }
            JSONObject jsonObject = JSON.parseObject(result + "");
            String access_token = jsonObject.getString("access_token");
            if(!StringUtil.isNull(access_token)) {
                tokeninfo.put("tokentime", nowtime + "");
                tokeninfo.put("token", access_token);
//                redisSession.setMapValue("wxtoken", tokeninfo);
            }
            return access_token;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

//    public static String getticket(String access_token, RedisSession redisSession) {
    public static String getticket(String access_token) {
        long nowtime = new Date().getTime();
//        Map<String, Object> tokeninfo = redisSession.getMapValue("wxtoken");
//        if(StringUtil.isMapNull(tokeninfo))
//            tokeninfo = new HashMap<String, Object>();
//        else {
//            String time = tokeninfo.get("tickettime") + "";
//            if(!StringUtil.isNull(time)) {
//                Long tickettime = Long.parseLong(time);
//                if(tickettime != null && tickettime + 100 * 60 * 1000 >= nowtime) {
//                    String ticket = tokeninfo.get("ticket") + "";
//                    if(!StringUtil.isNull(ticket))
//                        return ticket;
//                }
//            }
//        }

        Map<String, Object> tokeninfo = new HashMap<String, Object>();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        HttpPost httpPost = new HttpPost(url);
        try {
            response = httpClient.execute(httpPost);
            response.addHeader("Content-Type", "text/html;charset=UTF-8");
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String inputline = "";
            while ((inputline = rd.readLine()) != null) {
                result.append(inputline);
            }
            System.out.println(result + "我是返回值：");
            JSONObject jsonObject = JSON.parseObject(result + "");
            String ticket = jsonObject.getString("ticket");
            if(!StringUtil.isNull(ticket)) {
                tokeninfo.put("tickettime", nowtime + "");
                tokeninfo.put("ticket", ticket);
//                redisSession.setMapValue("wxtoken", tokeninfo);
            }
            return ticket;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String jsapi_ticket, String url, String nonce_str, String indenttime) {
        Map<String, String> ret = new HashMap<String, String>();
//        String nonce_str = create_nonce_str();
//        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + indenttime + "&url=" + url;
        System.out.println(string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        ret.put("signature", signature);

        return signature;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static void main(String[] args) {
//        System.out.println(getToken());
        String token = "29_AL_2DvGCiR99K-sTd8YOvb29vEDAWk0D950LXHhEXxCIphYmPKs6b_AGMs7DmH7xkn-ricWfIJPMgUCA3e7sZSe52ZzHi3HJDsJs0zeFE066CJ8TRuOfOm0CYQv-SioWl5Ph0-0TonCInb4lLHFiAGABXV";
        System.out.println(getticket(token));
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
