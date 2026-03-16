package com.unifor.br.server_replica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ServerReplicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerReplicaApplication.class, args);
	}

}
