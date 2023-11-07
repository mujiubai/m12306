package com.mujiubai.train.batch.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("com.mujiubai")
@MapperScan("com.mujiubai.train.batch.mapper")
public class BatchApplication {
	private static final Logger LOG = LoggerFactory.getLogger(BatchApplication.class);
	public static void main(String[] args) {
		// SpringApplication.run(BusinessApplication.class, args);
		SpringApplication app= new SpringApplication(BatchApplication.class);
		Environment env = app.run(args).getEnvironment();
		LOG.info("batch 启动成功！");
		LOG.info("地址: http://127.0.0.1:{}{}/hello",env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));
	}

}
