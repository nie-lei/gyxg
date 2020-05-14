package com.jzkj.gyxg.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.List;
import java.util.Map;

@Configuration
public class MyRedisConfig {

    @Bean
    public RedisTemplate<Object, Map<String, Object>> myRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Map<String, Object>> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<Map>(Map.class));
        return template;
    }

    @Bean
    public RedisTemplate<Object, List> myRedisListTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, List> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<List>(List.class));
        return template;
    }
}
