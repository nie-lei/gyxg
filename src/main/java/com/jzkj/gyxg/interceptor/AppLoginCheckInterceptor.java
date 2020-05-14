package com.jzkj.gyxg.interceptor;


import com.alibaba.fastjson.JSON;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.util.ContextUtil;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AppLoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisSession redisSession;

    private static List<String> executorList = new ArrayList<>();   //存储需验证登陆的接口地址
    static {
//        executorList.add("/getArea");          //获取就餐区域
//        executorList.add("/getTablesInfo");    //获取餐桌信息
//        executorList.add("/removeDishes");     //删除菜品
    }

    public boolean isContains(String url) {
        boolean flag = false;
        for (String str: executorList) {
            if(url.contains(str)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
        String token = req.getHeader("token");
        String url = req.getRequestURI();
        if (StringUtil.isNull(token)) {
            if (isContains(url)) {
                writeJsonOutput(resp, 0, "");
                return false;
            }
            return true;
        } else {
            if (redisSession == null) {//解决service为null无法注入问题
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext());
                redisSession = (RedisSession) factory.getBean("redisSession");
            }
            Map<String, Object> user = redisSession.getMapValue(token, 2, TimeUnit.MINUTES);
            if(StringUtil.isMapNull(user)) {
                if(isContains(url)) {
                    writeJsonOutput(resp, 0, "");
                    return false;
                }
                return true;
            }
            String loginuid = user.get("uid") + "";
            String logintime = user.get("time") + "";
            if (StringUtil.isNull(loginuid) || StringUtil.isNull(logintime)) {
                if(isContains(url)) {
                    writeJsonOutput(resp, 0, "");
                    return false;
                }
                return true;
            }
            int uid = Integer.parseInt(user.get("uid") + "");
            long nowtime = System.currentTimeMillis();

            if(uid == 0 || ((nowtime - 60000) > Long.parseLong(logintime))) {
                if(!isContains(url)) {
                    writeJsonOutput(resp, 0, "");
                    return false;
                }
                return true;
            }
            user = new HashMap<String, Object>();
            user.put("uid", uid);
            user.put("type", 0);
            user.put("time", nowtime);

            redisSession.setMapValue(token, user, 1, TimeUnit.DAYS);
            ContextUtil.setAttribute("uid", uid);
        }
        return true;
    }

    public static void writeJsonOutput(HttpServletResponse resp, int type, String token) throws IOException {
        ResponseJson responseJson = new ResponseJson();
        if(type==0){
            responseJson.setCode(101);
            responseJson.setMsg("请登录后再操作！");
            responseJson.setData(ContextUtil.getBaseUrl());
        } else if (type == 1) {
            responseJson.setCode(0);
            responseJson.setMsg("获取成功");
            responseJson.setData(token);
        }else{
            responseJson.setCode(102);
            responseJson.setMsg("对不起，您没有权限进行此操作！");
            responseJson.setData(ContextUtil.getBaseUrl());
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        resp.getWriter().write(JSON.toJSONString(responseJson));
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
