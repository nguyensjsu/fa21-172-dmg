package com.example.springpayments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
//@ComponentScan(basePackages= {"com.example.springusers","com.example.springbooks", "com.example.springpayments"})
//@EnableAutoConfiguration


public class SpringPaymentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPaymentsApplication.class, args);
	}

}
