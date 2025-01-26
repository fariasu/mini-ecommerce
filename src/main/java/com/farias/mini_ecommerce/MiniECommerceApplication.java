package com.farias.mini_ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.farias.mini_ecommerce.domain")
public class MiniECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniECommerceApplication.class, args);
	}

}
