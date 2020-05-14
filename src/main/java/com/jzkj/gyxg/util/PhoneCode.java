package com.jzkj.gyxg.util;

import com.alibaba.fastjson.JSON;
import com.jzkj.gyxg.common.RedisSession;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneCode {

    /**
     * 根据用户id删除缓存中存在的用户
     * @param redisSession
     * @param userid
     * @param type
     * @return
     */
    public static List<String> removeUser(RedisSession redisSession, Integer userid, Integer type) {
        List<String> list = redisSession.getListValue("userList");
        List<String> resultList = new ArrayList<>();
        if(!StringUtil.isListNull(list)) {
            for (String str: list) {
                Map<String, Object> map = redisSession.getMapValue(str);
                if(!StringUtil.isMapNull(map)) {
                    if(userid.equals(Integer.parseInt(map.get("uid") + "")) && type.equals(Integer.parseInt(map.get("type") + ""))) {
                        redisSession.unlock(str, 1);
                    } else {
                        resultList.add(str);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 根据用户id获取token
     * @param userid 用户id
     * @return
     */
    public static String getTokenByUid(RedisSession redisSession, Integer userid, Integer type) {
        List<String> list = redisSession.getListValue("userList");
        String token = "";
        if (!StringUtil.isListNull(list)) {
            for (String str: list) {
                Map<String, Object> map = redisSession.getMapValue(str);
                if (!StringUtil.isMapNull(map)) {
                    if (userid.equals(Integer.parseInt(map.get("uid") + "")) && type.equals(Integer.parseInt(map.get("type") + ""))) {
                        return str;
                    }
                }
            }
        }
        return token;
    }
}
