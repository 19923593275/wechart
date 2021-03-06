package com.zxx.wechart.store;

import com.zxx.wechart.store.config.TokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.zxx.wechart.store")
public class StoreApplication {
    Logger logger = LogManager.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	@Component
	@Order(1)
	public class accessTokenStart implements CommandLineRunner {
		@Override
		public void run(String... args) throws Exception {
            logger.info("log4j =======执行了第一个任务");
			TokenUtil.startTask();
		}
	}

	@Component
	@Order(2)
	public class createMenuStart implements  CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			System.out.println("第二个任务开始执行======================");
		}
	}

}
