package com.jzkj.gyxg.interceptor;

import com.alibaba.fastjson.JSON;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.entity.Customers;
import com.jzkj.gyxg.service.app.AppUserService;
import com.jzkj.gyxg.util.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WechatLoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisSession redisSession;
    @Autowired
    private AppUserService appUserService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
        String token = req.getHeader("token");

        int tableid = HttpRequestParamter.getInt("tablesid");
        String url = req.getRequestURL().toString().replace("localhost:8080", "api.cqgyxg.com");
        if (url.contains("/uploadImg")) {
            return true;
        }
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext());
        if (redisSession == null) {//解决service为null无法注入问题
            redisSession = (RedisSession) factory.getBean("redisSession");
        }
        if (appUserService == null) {//解决service为null无法注入问题
            appUserService = (AppUserService) factory.getBean("appUserService");
        }
        if (StringUtil.isNull(token) && url.contains("/wechat/store/preOrder")) {
            token = HttpRequestParamter.getString("token");
        }
        if ((isWechatbrowse(req) && StringUtil.isNull(token)) || url.contains("/getToken")) {
            String code = req.getParameter("code");

            if (StringUtil.isNull(code)) {
                resp.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                        + WXInfo.APP_ID
                        + "&redirect_uri="
                        + buildFullRequestUrl(req, code)
                        + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
                System.out.println("https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                        + WXInfo.APP_ID
                        + "&redirect_uri="
                        + buildFullRequestUrl(req, code)
                        + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
                return false;
            }
            //获取openid和access_token
            String responStr = HttpUtil.doGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
                    WXInfo.APP_ID + "&secret=" + WXInfo.APP_SECRET + "&code=" + code + "&grant_type=authorization_code");
            Map<String, Object> result = JSON.parseObject(responStr, Map.class);
            if (StringUtil.isMapNull(result) || result.containsKey("errcode")) {
                resp.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                        + WXInfo.APP_ID
                        + "&redirect_uri="
                        + buildFullRequestUrl(req, code)
                        + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
                return false;
            }

            String openId = result.get("openid") + "";

            Customers customers = appUserService.selectUserInfoByTypeAndOpenid(openId);
            if (customers == null) {
                //openId和access_token获取用户个人信息
//                    String access_token = WeiXinUtils.getAccessToken(WXInfo.APP_ID, WXInfo.APP_SECRET, redisSession);
//                    String subscribe = "0";
//                    if (!StringUtil.isNull(access_token)) {
//                        responStr = HttpUtil.doGet("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openId + "&&lang=zh_CN");
//                        result = JSON.parseObject(responStr, Map.class);
//                        logger.info("result_info:" + result);
//                        if(StringUtil.isMapNull(result) || !result.containsKey("errcode")) {
//                            subscribe = result.get("subscribe") + "";
//                        }
//                    }

                String access_token = result.get("access_token") + "";
                responStr = HttpUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openId);
                result = JSON.parseObject(responStr, Map.class);
                if (StringUtil.isMapNull(result) || result.containsKey("errcode")) {
                    resp.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                            + WXInfo.APP_ID
                            + "&redirect_uri="
                            + buildFullRequestUrl(req, code)
                            + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
                    return false;
                }
                String nickName = dofilter(result.get("nickname") + "");
                String headimgurl = result.get("headimgurl") + "";
                String sex = result.get("sex") + "";
                customers = appUserService.insertThreeLogin(openId, nickName, headimgurl, sex);
            }
            ContextUtil.setAttribute("uid", customers.getCustomerid());
            if (tableid != 0) {
                ContextUtil.setAttribute("tableid", tableid);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", customers.getCustomerid());
            map.put("tableid", tableid);
            token = HexString.encode(StringUtil.getUUid());
            redisSession.setMapValue(token, map, 1, TimeUnit.DAYS);
            if (url.contains("/getToken")) {
                AppLoginCheckInterceptor.writeJsonOutput(resp, 1, token);
                return false;
            }
        } else {
            if (StringUtil.isNull(token)) {
                if (isNotContainsUrl(url)) {
                    AppLoginCheckInterceptor.writeJsonOutput(resp, 0, "");
                    return false;
                } else {
                    return true;
                }
            } else {
                Map<String, Object> map = redisSession.getMapValue(token);
                if (StringUtil.isMapNull(map) && isNotContainsUrl(url)) {
                    AppLoginCheckInterceptor.writeJsonOutput(resp, 0, "");
                    return false;
                }
                int uid = map.get("uid") == null ? 0: Integer.parseInt(map.get("uid") + "");
                if (uid == 0 && isNotContainsUrl(url)) {
                    AppLoginCheckInterceptor.writeJsonOutput(resp, 0, "");
                    return false;
                }
                if (tableid == 0) {
                    tableid = StringUtil.isNull(map.get("tableid") + "")? 0: Integer.parseInt(map.get("tableid") + "");
                }
                if (tableid == 0) {
                    if (isNotContainsUrl(url)) {
                        AppLoginCheckInterceptor.writeJsonOutput(resp, 0, "");
                        return false;
                    }
                }
                map = new HashMap<String, Object>();
                map.put("uid", uid);
                map.put("tableid", tableid);
                redisSession.setMapValue(token, map, 1, TimeUnit.DAYS);

                if (url.contains("/getToken")) {
                    AppLoginCheckInterceptor.writeJsonOutput(resp, 1, token);
                    return false;
                }
                ContextUtil.setAttribute("tableid", tableid);
                ContextUtil.setAttribute("uid", uid);
                return true;
            }
        }
        return true;
    }

    private boolean isNotContainsUrl(String url) {
        return !url.contains("/login") && !url.contains("/wechatinfo") && !url.contains("getToken");
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 判断是否是微信浏览器访问
     * @param req
     * @return
     */
    public boolean isWechatbrowse(HttpServletRequest req) {
        if (req.getHeader("user-agent") == null) {
            return true;
        }
        String ua = req.getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
            return true;
        }
        return false;
    }

    /**
     * 去掉特殊字符
     * @param str
     * @return
     */
    public static String dofilter(String str) {
        String str_Result = "", str_OneStr = "";
        for (int z = 0; z < str.length(); z++) {
            str_OneStr = str.substring(z, z + 1);
            if (str_OneStr.matches("[\u4e00-\u9fa5]+") || str_OneStr.matches("[\\x00-\\x7F]+")) {
                str_Result = str_Result + str_OneStr;
            }
        }
        return str_Result.replaceAll("'", "").replaceAll("\"", "");
    }

    /**
     * 添加cookie
     * @param resp
     * @param key      键
     * @param value    值
     * @param maxAge   失效时长
     */
    public static void addSession(HttpServletRequest req, HttpServletResponse resp, String key, String value, int maxAge) {
        req.getSession().setAttribute(key, value);
    }

    public static String buildFullRequestUrl(HttpServletRequest r, String code) {
//        String url = UrlUtils.buildFullRequestUrl(r.getScheme(), r.getServerName(), r.getServerPort(), r.getRequestURI(), r.getQueryString());
//        return url;
        String url = UrlUtils.buildFullRequestUrl(r.getScheme(), r.getServerName().replace("localhost", "www.cqgyxg.com"), r.getServerPort(), r.getRequestURI(), r.getQueryString());
        url = url.replace("&code=" + code + "&state=123", "");
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
