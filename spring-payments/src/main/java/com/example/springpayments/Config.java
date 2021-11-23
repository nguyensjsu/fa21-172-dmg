package com.example.springpayments;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@EnableWebMvc
@Configuration
public class Config implements WebMvcConfigurer{

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/process_payment").setViewName("process_payment");

        registry.addViewController("/place_order").setViewName("place_order");
//        registry.addViewController("/catalog").setViewName("catalog");
        registry.addViewController("/creditcards").setViewName("creditcards");


    }
}
