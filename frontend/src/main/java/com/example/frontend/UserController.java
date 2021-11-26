package com.example.frontend;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j
@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
	private RestTemplate restTemplate;

    private String SPRING_USERS_URI = "http://localhost:8082";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping
    public String getHome( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing home");
        System.out.println("frontend changed.");
        System.out.println("latest edit 2.");
        return "home";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute("user") User user,  
            @RequestParam(value="action", required=false) String action, 
            Errors errors, Model model, HttpServletRequest request) 
            throws RestClientException, Exception {
        log.info(" User : " + user) ;
        System.out.println("frontend/UserController.java");
        System.out.println("Email = " + user.getEmail() + ", Password = " + user.getPassword());
        User existingUser = restTemplate.getForObject(SPRING_USERS_URI + "/users/{email}", User.class, toMap(user));
        //User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser == null) {
            log.info("User does not exist!");
        }

        else if (!existingUser.getPassword().equals(user.getPassword())) {
            log.info("Incorrect password!");
        }
        else {
            log.info(" User exists!");
        }
        return "home";       
    }

    @GetMapping("/register")
    public String getRegister( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing registration");
        return "register";
    }
    
    @PostMapping("/register")
    public User postAction(@Valid @ModelAttribute("user") User user,  @RequestParam(value="action", required=false) String action, Errors errors, Model model, HttpServletRequest request) {
        log.info(" User : " + user) ;
        User createdUser = restTemplate.postForObject(SPRING_USERS_URI + "/users", user, User.class);
        // if (existingUser != null) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "That email is already registered!");
        // }
        return createdUser;          
    }

    public static Map<String, Object> toMap( Object object ) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        for ( Field field : object.getClass().getDeclaredFields() )
        {
            field.setAccessible( true );
            map.put( field.getName(), field.get( object ) );
        }
        return map;
    }
//    @GetMapping("/home")
//    public String getRegister( @ModelAttribute("user") User user,
//                               Model model) {
//        System.out.println("Accessing register");
//
//        return "home";
//    }
}
