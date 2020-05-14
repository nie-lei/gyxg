package com.jzkj.gyxg.service;

import com.jzkj.gyxg.common.RedisSession;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseService {
    @Autowired
    public RedisSession redisSession;
}
