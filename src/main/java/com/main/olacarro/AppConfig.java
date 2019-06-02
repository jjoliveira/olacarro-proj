package com.main.olacarro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration 
public class AppConfig {
	
	//@Autowired String mongo_ip = System.getenv("MONGO_IP") == null ? "localhost" : System.getenv("MONGO_IP");
	@Autowired Environment env;
	
    public @Bean MongoClient mongo() throws Exception {
	    String mongo_ip = env.getProperty("MONGO_IP", "localhost");   	 
	    System.out.println("Mongo " + mongo_ip );
    	return new MongoClient(mongo_ip);
    }

    public @Bean MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "olacarro_db");
    }
}