package com.ferdev83.clonit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ClonitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClonitApplication.class, args);
	}

}
