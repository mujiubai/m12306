package com.mujiubai.train.business.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("com.mujiubai")
@MapperScan("com.mujiubai.train.member.mapper")
public class BusinessApplication {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessApplication.class);
	public static void main(String[] args) {
		// SpringApplication.run(BusinessApplication.class, args);
		SpringApplication app= new SpringApplication(BusinessApplication.class);
		Environment env = app.run(args).getEnvironment();
		LOG.info("business 启动成功！");
		LOG.info("地址: http://127.0.0.1:{}{}/hello",env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));
	}

}
