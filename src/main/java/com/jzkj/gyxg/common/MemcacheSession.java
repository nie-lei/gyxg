package com.jzkj.gyxg.common;

import com.jzkj.gyxg.util.ContextUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class MemcacheSession {

    private HttpSession getSession(){
        return ContextUtil.getRequest().getSession();
    }

    public Object get(String key){
        return getSession().getAttribute(key);
    }

    public void set(String key, Object value){
        getSession().setAttribute(key, value);
    }
}
