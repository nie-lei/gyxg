package com.jzkj.gyxg.util;

import com.google.gson.Gson;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.entity.ReplyTextMessage;
import com.jzkj.gyxg.entity.ReplyTuwenMessage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 这个类都是用于测试的，正式代码基本不用到
 *
 * @author shichenwei
 */
public class WeiXinUtils {
    public static String user_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPEN_ID&lang=zh_CN";
    public static String customer_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    public static String ACCESS_TOKEN = null;
    //	public static String token_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
//			"&appid="++"&secret="+APPSECRET;
    public static String create_menu_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    public static String template_news = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    public static String wx_param_ewm_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKENPOST";


    public static Map getUserInfo(String accesstoken, String openid) {
        String user_info = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//		String user_info="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKENS&openid=OPENIDS&lang=zh_CN";
        user_info = user_info.replace("ACCESS_TOKEN", accesstoken);
        user_info = user_info.replace("OPENID", openid);
//		System.out.println("getUserInfo url:"+user_info);
        String resp = HttpsUtils.httpRequest(user_info, "GET", null);
        if (null != resp) {
            Map map = JsonUtils.deserializeJson(resp);
            return map;
//		    	Object obj=map.get("openid");
//		        if (obj==null) {
//		        	System.out.println(openid+" getUserInfo创建失败:"+resp);
//		        }else{
//		        	return map;
//		        }
        }
        return null;
    }

    //网页授权获取code后 获取 access_token 跟openid
    public static Map getMapByCode(String code, String appid, String appsecret) {
        String code_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret + "&code=CODES&grant_type=authorization_code";
        code_access_token = code_access_token.replace("CODES", code);
//		System.out.println("getAccess_token:"+code_access_token);
        String resp = HttpsUtils.httpRequest(code_access_token, "GET", null);
        if (null != resp) {
            Map map = JsonUtils.deserializeJson(resp);
            Object obj = map.get("openid");
            if (obj == null) {
//		        	System.out.println(code+" getMapByCode创建失败:"+resp);
            } else {
//					  System.out.println("getMapByCode:"+resp);
                return map;
            }
        }
        return null;
    }


    /**
     * 获取access_token
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    public static String getAccessToken(String appid, String appsecret, RedisSession redisSession) {
        Map<String, Object> accessTokenMap = redisSession.getMapValue("wxinfo");
        long nowtime = new Date().getTime();
        if (!StringUtil.isMapNull(accessTokenMap)) {
            Long time = Long.parseLong(accessTokenMap.get("time") + "");
//            String access_token = accessTokenMap.get("access_token") + "";
            if (time != null && time + 90 * 60 * 1000 <= nowtime) {
                return accessTokenMap.get("access_token") + "";
            }
        } else
            accessTokenMap = new HashMap<String, Object>();
        String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + appsecret;
        String resp = HttpsUtils.httpRequest(token_url, "GET", null);
        // 如果请求成功
        if (null != resp) {
//	    		System.out.println(resp);
            Map map = JsonUtils.deserializeJson(resp);
            if (!StringUtil.isMapNull(map)) {
                String access_token = map.get("access_token") + "";
                accessTokenMap.put("access_token", access_token);
                accessTokenMap.put("time", nowtime + "");
                redisSession.setMapValue("wxinfo", accessTokenMap);
                return access_token;
            }
        }
        return null;
    }

    public static Map getWXUserInfo(String openid, String wxid) {
        String sql = "select * from wx_params where wxid=?";
        Map<String, Object> map = ApplicationContextUtils.getJdbcTemplate().queryForList(sql, wxid).get(0);
        String appid = map.get("appid").toString();
        String appsecret = map.get("appsecret").toString();
        Object accesstoken = map.get("accesstoken");
        String token;
        if (accesstoken == null) {
            token = getToken(appid, appsecret);
            sql = "update wx_params set accesstoken=? where wxid=?";
            ApplicationContextUtils.getJdbcTemplate().update(sql, token, wxid);
        } else {
            token = accesstoken.toString();
        }
        String user_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPEN_ID&lang=zh_CN";
        String url = user_url.replace("ACCESS_TOKEN", token).replace("OPEN_ID", openid);
        String resp = HttpsUtils.httpRequest(url, "GET", null);
        Map<String, Object> usermap = null;
        Integer result;
        if (null != resp) {
            usermap = JsonUtils.deserializeJson(resp);
            if (usermap.get("errcode") != null) {
                result = Integer.valueOf(usermap.get("errcode").toString());
                if (0 != result) {
                    token = getToken(appid, appsecret);
                    sql = "update wx_params set accesstoken=? where wxid=?";
                    ApplicationContextUtils.getJdbcTemplate().update(sql, token, wxid);
                    user_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPEN_ID&lang=zh_CN";
                    url = user_url.replace("ACCESS_TOKEN", token).replace("OPEN_ID", openid);
                    resp = HttpsUtils.httpRequest(url, "GET", null);
                    usermap = JsonUtils.deserializeJson(resp);
                }
            }
        }
        return usermap;
    }


    private static String getTokenByWxid(String wxid) {
        String sql = "select * from wx_params where wxid=?";
        Map<String, Object> map = ApplicationContextUtils.getJdbcTemplate().queryForList(sql, wxid).get(0);
        String appid = map.get("appid").toString();
        String appsecret = map.get("appsecret").toString();
        Object accesstoken = map.get("accesstoken");
        String token;

        token = getToken(appid, appsecret);
        sql = "update wx_params set accesstoken=? where wxid=?";
        ApplicationContextUtils.getJdbcTemplate().update(sql, token, wxid);

        return token;
    }


    public static String getToken(String appid, String appsecret) {
        String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
        token_url = token_url.replace("APPID", appid);
        token_url = token_url.replace("SECRET", appsecret);
//		System.out.println(token_url);
        String resp = HttpsUtils.httpRequest(token_url, "GET", null);
        // 如果请求成功
        if (null != resp) {
//	    		System.out.println(resp);
            Map map = JsonUtils.deserializeJson(resp);
            String access_token = map.get("access_token").toString();
            return access_token;
        }
        return "";
    }

    /**
     * 创建二维码ticket
     *
     * @param access_token
     * @return
     */
    public static Map<String, Object> createTicket(String access_token, String secne_str) {
        String token_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
//		System.out.println(token_url);
        TreeMap<String, String> token = new TreeMap<String, String>();
        token.put("access_token", access_token);

        Map<String, Object> secne = new HashMap<String, Object>();
        secne.put("scene_str", secne_str);
        Map<String, Object> actioninfo = new HashMap<String, Object>();
        actioninfo.put("scene", secne);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action_name", "QR_LIMIT_STR_SCENE");
        params.put("action_info", actioninfo);
        Gson gson = new Gson();
        String data = gson.toJson(params);

        String resp = HttpRequestUtil.HttpsDefaultExecute("POST", token_url, token, data);

//        String resp = HttpsUtils.httpRequest(token_url, "POST", data);
        // 如果请求成功
        if (null != resp) {
//	    		System.out.println(resp);
            Map map = JsonUtils.deserializeJson(resp);
//            String access_token = map.get("access_token").toString();
            return map;
        }
        return null;
    }

    public static String showqrcode(String ticket) {

        Map<String, String> params = new TreeMap<String, String>();
        params.put("ticket", HttpRequestUtil.urlEncode(ticket, HttpRequestUtil.DEFAULT_CHARSET));
        try {
            String resp = HttpRequestUtil.setParmas(params, "https://mp.weixin.qq.com/cgi-bin/showqrcode", "");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


//        String token_url = null;
//        try {
//            token_url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(ticket, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
////		System.out.println(token_url);
//        String resp = HttpsUtils.httpRequest(token_url, "GET", null);
//        // 如果请求成功
//        if (null != resp) {
//            System.out.println(resp);
//            Map map = JsonUtils.deserializeJson(resp);
//            return  map;
//        }
//        return null;
    }

    public static Map<String, Object>  getticket(String access_token, RedisSession redisSession) {
        Map<String, Object> accessTokenMap = redisSession.getMapValue("wxinfo");
        long nowtime = new Date().getTime();
        if (!StringUtil.isMapNull(accessTokenMap)) {
            Long tickettime = Long.parseLong(accessTokenMap.get("tickettime") + "");
            if (tickettime != null)
//            String access_token = accessTokenMap.get("access_token") + "";
                if (tickettime + 90 * 60 * 1000 <= nowtime)
                    return accessTokenMap;
        } else
            accessTokenMap = new HashMap<String, Object>();

        String token_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
//		System.out.println(token_url);
        String resp = HttpsUtils.httpRequest(token_url, "GET", null);
        // 如果请求成功
        if (null != resp) {
//	    		System.out.println(resp);
            Map map = JsonUtils.deserializeJson(resp);
            if (!StringUtil.isMapNull(map)) {
                String ticket = map.get("ticket") + "";
                accessTokenMap.put("ticket", ticket);
                accessTokenMap.put("tickettime", nowtime + "");
                redisSession.setMapValue("wxinfo", accessTokenMap);
            }
//            String access_token = map.get("access_token").toString();
            return accessTokenMap;
        }

        return null;
    }

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return Map
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map parseXml(HttpServletRequest request) {
        // 将解析结果存储在HashMap中
        Map map = new HashMap();

        // 从request中取得输入流
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream == null)
                return null;
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();

            // 遍历所有子节点
            for (Element e : elementList)
                map.put(e.getName(), e.getText());

            // 释放资源
            inputStream.close();
            inputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // 读取输入流

        return map;
    }

    /**
     * 文本消息转化为xml
     *
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(ReplyTextMessage textMessage) {
        XStream xstream = new XStream();
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }


    /**
     * 图文消息转化为xml
     *
     * @param replyTuwenMessage
     * @return
     */
    public static String tuwenMessageToXml(ReplyTuwenMessage replyTuwenMessage) {
        XStream xstream = new XStream();
        xstream.alias("xml", replyTuwenMessage.getClass());
        return xstream.toXML(replyTuwenMessage);
    }

    public static void main(String[] args) {
//        String access_token = "KDM_G9GmD5epXLFbY9HdtC_PcHGtcPlofqvBoOkHDdHK1rlCExfZ1Kos-rva479dPbdj3VdYzoLMsOcfIGCrN7ZBx2GVQMQHEqKLirBZFK7w462zis88UgKvJO2rl8uAKLJjABASTR";
//        Map<String, Object> getticket = getticket(access_token);
//        System.out.println(getticket);
//		System.out.println(Sha1Util.getNonceStr());
//        String m = "12";
//        String item = "except";
//        m = HexString.insertstr(m, 2, item);
//        m = HexString.encode(m);
//        System.out.println(m);
//        m = HexString.decode(m);
//        m = HexString.removestr(m, 2, item, "");
//        System.out.println(m);
        String token = WXInfo.getToken();
        Map<String, Object> map = createTicket(token, "1_1_101");
        String str = WeiXinUtils.showqrcode(map.get("ticket") + "");
        System.out.println(str);
    }
}
