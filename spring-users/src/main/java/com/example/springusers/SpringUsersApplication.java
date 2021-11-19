package com.example.springusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.example.springpayments","com.example.springbooks","com.example.springusers"})

public class SpringUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringUsersApplication.class, args);
	}

}
