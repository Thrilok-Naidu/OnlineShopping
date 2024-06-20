package com.OnlineShopping.Thrilok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ThrilokApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThrilokApplication.class, args);
	}

}
