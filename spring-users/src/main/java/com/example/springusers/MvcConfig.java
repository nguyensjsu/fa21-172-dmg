package com.example.springusers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration

public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/register").setViewName("register");
        registry.addViewController("spring-books/src/main/resources/templates/catalog").setViewName("catalog");
        registry.addViewController("/creditcards").setViewName("creditcards");
        registry.addViewController("/passwordreset").setViewName("passwordreset");
        registry.addViewController("resources/templates/refund.html").setViewName("refund");
    }

}
