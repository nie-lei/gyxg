package com.jzkj.gyxg.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description: 自定义资源映射
 * <p>
 *   设置虚拟路径，访问绝对路径下资源
 * </p>
 * @author: Yangxf
 * @date: 2019/4/17 18:17
 */
@ComponentScan
@Configuration
public class WebConfigurer extends WebMvcConfigurerAdapter {
//public class WebConfigurer extends WebMvcConfigurationSupport {

  @Autowired
  PreReadUploadConfig uploadConfig;
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("file:///"+uploadConfig.getUploadPath());
  }
}