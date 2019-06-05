package com.zhjs.transfer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.zhjs.transfer"},exclude = {})
@ImportResource("classpath:spring-config.xml")
//@MapperScan("com.zhjs.transfer.dao")
public class AccountMoneyApplication{

	public static void main(String[] args) {
		SpringApplication.run(AccountMoneyApplication.class, args);
	}

}
