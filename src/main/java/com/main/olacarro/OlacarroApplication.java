package com.main.olacarro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OlacarroApplication {

	public static void main(String[] args) {
		String mongo_ip = System.getenv("MONGO_IP") == null ? "localhost" : System.getenv("MONGO_IP");
		
		SpringApplication.run(OlacarroApplication.class, args);
	}

}
