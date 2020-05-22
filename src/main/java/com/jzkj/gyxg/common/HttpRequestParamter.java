package com.jzkj.gyxg.common;

import com.jzkj.gyxg.util.CollectionUtil;
import com.jzkj.gyxg.util.ContextUtil;
import com.jzkj.gyxg.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class HttpRequestParamter {
    public static String getString(String paramName) {
        return getString(paramName, true, "");
    }

    public static String getString(String paramName, boolean antiXss) {
        return getString(paramName, antiXss, "");
    }

    public static String getString(String paramName, String defaultValue) {
        String paramValue = ContextUtil.getRequest().getParameter(paramName);

        if (StringUtil.isEmpty(paramValue)) {
            return defaultValue;
        }

        return paramValue;
    }

    public static String getString(String paramName, boolean antiXss, String defaultValue) {
        String paramValue = ContextUtil.getRequest().getParameter(paramName);

        if (StringUtil.isEmpty(paramValue)) {
            return defaultValue;
        }

        return antiXss ? HtmlPurifier.clean(paramValue) : paramValue;
    }

    public static int getTableid() {
        String tableid = ContextUtil.getAttribute("tableid") + "";
        return StringUtil.isNull(tableid) ? 0 : Integer.parseInt(tableid);
    }

    public static String getToken(){
        return getString("token");
    }

    public static Integer getUid() {
        return ContextUtil.getAttribute("uid") == null ? 0 : Integer.parseInt(ContextUtil.getAttribute("uid") + "");
    }

    public static Integer getType() {
        return ContextUtil.getAttribute("type") == null ? -1 :  Integer.parseInt(ContextUtil.getAttribute("type") + "");
    }

    public static long getLong(String paramName){
        return getLong(paramName,0);
    }

    public static long getLong(String paramName, long defaultValue){
        String paramValue = ContextUtil.getRequest().getParameter(paramName);

        if (StringUtil.isNull(paramValue)) {
            return defaultValue;
        }

        return (new Double(paramValue)).longValue();
    }

    public static double getDouble(String paramName, double defaultValue){
        String paramValue = ContextUtil.getRequest().getParameter(paramName);

        if (StringUtil.isNull(paramValue)) {
            return defaultValue;
        }
        return (new Double(paramValue)).doubleValue();
    }

    public static double getDouble(String paramName){
        return getDouble(paramName,999);
    }


    public static int getInt(String paramName) {
        return getInt(paramName, 0);
    }

    public static int getInt(String paramName, int defaultValue) {
        String paramValue = ContextUtil.getRequest().getParameter(paramName);

        if (StringUtil.isNull(paramValue)) {
            return defaultValue;
        }

        return (new Double(paramValue)).intValue();
    }

    public static Byte getByte(String paramName) {
        if(StringUtil.isNull(paramName))
            return null;
        return getByte(paramName, (byte)0);
    }

    public static byte getByte(String paramName, byte defaultValue){
        String paramValue = ContextUtil.getRequest().getParameter(paramName);

        if (StringUtil.isNull(paramValue)) {
            return defaultValue;
        }

        return (new Double(paramValue)).byteValue();
    }

    public static int[] getIntegerArray(String paramName) {
        String paramValue = ContextUtil.getRequest().getParameter(paramName);

        if (StringUtil.isNull(paramValue)) {
            return new int[]{};
        }

        String[] arr = paramValue.split("[\\x20\\t]*,[\\x20\\t]*");
        List<Integer> numbers = new ArrayList<Integer>();

        for (String number : arr) {
            number = StringUtil.trim(number);

            if (!StringUtil.isNumeric(number)) {
                continue;
            }

            numbers.add(StringUtil.parseInteger(number));
        }

        if (numbers.isEmpty()) {
            return new int[]{};
        }

        return CollectionUtil.toIntArray(numbers);
    }

    public static String getJson() {
        try {
            InputStream is = ContextUtil.getRequest().getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            //读取HTTP请求内容
            String buffer = null;
            StringBuffer sb = new StringBuffer();

            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }

            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获取请求的参数
     *
     * @return
     */
    public static String getParams(HttpServletRequest request) {
        StringBuffer param = new StringBuffer();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            param.append(paraName + ": " + request.getParameter(paraName) + "，");
            System.out.println(paraName + ": " + request.getParameter(paraName));
        }
        return param.toString();
    }

    public static String getPort() {
        return ContextUtil.getRequest().getServerPort() + "";
    }
}
