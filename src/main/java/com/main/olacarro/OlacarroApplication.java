package com.main.olacarro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OlacarroApplication {

	public static void main(String[] args) {
		String mongo_ip = System.getenv("MONGO_IP") == null ? "localhost" : System.getenv("MONGO_IP");
		
		System.out.println(mongo_ip);
		
		SpringApplication.run(OlacarroApplication.class, args);
	}

}
