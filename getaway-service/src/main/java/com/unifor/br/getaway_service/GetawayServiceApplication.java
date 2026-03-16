package com.unifor.br.getaway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GetawayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetawayServiceApplication.class, args);
	}

}