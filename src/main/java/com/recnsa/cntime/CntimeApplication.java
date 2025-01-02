package com.recnsa.cntime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CntimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CntimeApplication.class, args);
	}

}
