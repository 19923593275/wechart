package com.zxx.wechart.store;

import com.zxx.wechart.store.config.TokenUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	@Component
	@Order(1)
	public class accessTokenStart implements CommandLineRunner {
		@Override
		public void run(String... args) throws Exception {
			System.out.println("执行了第一个任务");
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
