//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jzkj.gyxg.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;

public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    public ApplicationContextUtils() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static JdbcTemplate getJdbcTemplate() {
        return (JdbcTemplate)context.getBean("jdbcTemplate", JdbcTemplate.class);
    }
}
