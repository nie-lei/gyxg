package com.jzkj.gyxg.interceptor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截

        registry.addInterceptor(new OriginCheckInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new HtmlLoginCheckInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(new AppLoginCheckInterceptor()).addPathPatterns("/app/**");
        registry.addInterceptor(new WechatLoginCheckInterceptor()).addPathPatterns("/wechat/**");
        super.addInterceptors(registry);
    }
}