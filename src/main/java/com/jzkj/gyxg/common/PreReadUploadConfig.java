package com.jzkj.gyxg.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 文件上传路径配置
 * @author: Yangxf
 * @date: 2019/4/17 17:50
 */
@Component
@ConfigurationProperties(prefix="preread")
public class PreReadUploadConfig {
  
  //上传路径
  private String uploadPath;
  
  public String getUploadPath() {
    return uploadPath;
  }
  
  public void setUploadPath(String uploadPath) {
    this.uploadPath = uploadPath;
  }
}