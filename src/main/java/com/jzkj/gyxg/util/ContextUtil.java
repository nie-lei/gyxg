package com.jzkj.gyxg.util;

import com.jzkj.gyxg.common.HttpServletResponseHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Properties;

public final class ContextUtil {
    private static final Properties cfg = FileUtil.loadPropertiesFromClassPathResource("application.properties");
    public static ApplicationContext applicationContext;
    private static String applicationEnv;

    private ContextUtil() {
    }

    /**
     * 取得 HttpServletRequest 对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取sessionId
     *
     * @return
     */
    public static String getSessionId() {
        return getHttpSession().getId();
    }

    /**
     * 获取session
     *
     * @return
     */
    public static HttpSession getHttpSession() {
        return getRequest().getSession();
    }

    /**
     * 设置session 值
     * @param name
     * @param value
     */
    public static void setAttribute(String name, Object value) {
        getHttpSession().setAttribute(name, value);
    }

    /**
     * 获取session 值
     * @param name
     * @return
     */
    public static Object getAttribute(String name){
        return getHttpSession().getAttribute(name);
    }

    /**
     * set cookie
     *
     * @param cookieName
     * @param cookieValue
     */
    public static void setCookie(String cookieName, String cookieValue) {
        setCookie(cookieName, cookieValue, "", "/", -1);
    }

    /**
     * set cookie
     *
     * @param cookieName
     * @param cookieValue
     * @param maxAge
     */
    public static void setCookie(String cookieName, String cookieValue, int maxAge) {
        setCookie(cookieName, cookieValue, "", "/", maxAge);
    }

    /**
     * set cookie
     *
     * @param cookieName
     * @param cookieValue
     * @param domain
     * @param path
     * @param maxAge
     */
    public static void setCookie(String cookieName, String cookieValue, String domain, String path, int maxAge) {
        if (StringUtil.isEmpty(cookieName) || StringUtil.isEmpty(cookieValue)) {
            return;
        }

        Cookie cookie = new Cookie(cookieName, cookieValue);

        if (!StringUtil.isEmpty(domain)) {
            cookie.setDomain(domain);
        }

        if (!StringUtil.isEmpty(path)) {
            cookie.setPath(path);
        }

        cookie.setMaxAge(maxAge);

        HttpServletResponseHolder.getResponse().addCookie(cookie);
    }

    /**
     * check if cookie is exists
     *
     * @param cookieName
     * @return
     */
    public static boolean cookieExists(String cookieName) {
        Cookie[] cookies = getRequest().getCookies();

        if (cookies == null || cookies.length == 0) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * get cookie
     *
     * @param cookieName
     * @return
     */
    public static String getCookie(String cookieName) {
        Cookie[] cookies = getRequest().getCookies();

        if (cookies == null || cookies.length == 0) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return "";
    }

    /**
     * 取得 webapp 的根路径（绝对）
     *
     * @return
     */
    public static String getAbsWebRoot() {
        return getRequest().getSession().getServletContext().getRealPath("/");
    }


    /**
     * get base url
     *
     * @param
     * @return
     */
    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }

    /**
     * 获取客户端ip
     *
     * @return
     */
    public static String getClientIp() {
        HttpServletRequest req = getRequest();
        String ip = req.getHeader("X-Real-IP");

        if (!StringUtil.isEmpty(ip) && StringUtil.isNumeric(ip.replace(".", ""))) {
            return ip;
        }

        ip = req.getHeader("X-Forwarded-For");

        if (!StringUtil.isEmpty(ip) && StringUtil.isNumeric(ip.replace(".", ""))) {
            return ip;
        }

        ip = req.getRemoteAddr();

        if (!StringUtil.isEmpty(ip) && StringUtil.isNumeric(ip.replace(".", ""))) {
            return ip;
        }

        return "127.0.0.1";
    }


    public static String getProperty(String key) {
        return cfg.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return cfg.getProperty(key, defaultValue);
    }

    public static <T> T getBean(String name, Class<T> clazz){
        return (T) applicationContext.getBean(name, clazz);
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    public static String getApplicationEnv(){
        if(applicationEnv == null){
            applicationEnv = getProperty("spring.profiles.active");
        }
        return applicationEnv;
    }
}