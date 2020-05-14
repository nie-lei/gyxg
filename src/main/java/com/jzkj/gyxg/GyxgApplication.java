package com.jzkj.gyxg;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "com.jzkj.gyxg.mapper")
@ServletComponentScan
public class GyxgApplication {

	public static void main(String[] args) {
		SpringApplication.run(GyxgApplication.class, args);
	}

}
