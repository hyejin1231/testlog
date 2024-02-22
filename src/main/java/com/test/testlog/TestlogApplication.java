package com.test.testlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.test.testlog.config.data.AppConfig;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class TestlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestlogApplication.class, args);
	}

}
