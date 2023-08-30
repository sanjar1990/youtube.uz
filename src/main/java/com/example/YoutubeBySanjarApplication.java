package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class YoutubeBySanjarApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoutubeBySanjarApplication.class, args);
	}

}
