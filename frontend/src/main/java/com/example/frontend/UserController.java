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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    private String SPRING_USERS_URI = "http://users:8082";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping
    public String getHome( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing home");
        System.out.println("frontend changed.");
        System.out.println("latest edit 11/26/2021 11:44am");
        return "home";
    }

    @GetMapping("/login_dne")
    public String userDoesNotExist(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing login_dne");
        return "login_dne";
    }

    @GetMapping("/login_inc")
    public String incorrectPassword(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing login_inc");
        return "login_inc";
    }

    @GetMapping("/login_suc")
    public String loginSuccessful(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing login_suc");
        return "login_suc";
    }
    

    @PostMapping
    public String login(@Valid @ModelAttribute("user") User user,  
            @RequestParam(value="action", required=false) String action, 
            Errors errors, Model model, HttpServletRequest request) 
            throws RestClientException, Exception {
        log.info(" User : " + user) ;
        System.out.println("frontend/UserController.java");
        System.out.println("Email = " + user.getEmail() + ", Password = " + user.getPassword());
        ResponseEntity<User> response = restTemplate.getForEntity(SPRING_USERS_URI + "/users?email=" + user.getEmail() + "&password=" + user.getPassword(), User.class, user);
        //User existingUser = restTemplate.getForObject(SPRING_USERS_URI + "/users/" + user.getEmail(), User.class, toMap(user));
        //User existingUser = repository.findByEmail(user.getEmail());
        System.out.println("After sending GET to backend: ");
        System.out.println("response " + response.toString());
        System.out.println("Headers " + response.getHeaders());
        if (response.getHeaders().getFirst("status").equals(HttpStatus.BAD_REQUEST + "")) {
            return "login_dne";
        }

        else if (response.getHeaders().getFirst("status").equals(HttpStatus.UNAUTHORIZED + "")) {
            return "login_inc";
        }
        else if (response.getHeaders().getFirst("status").equals(HttpStatus.OK + "")){
            return "login_suc";
        }
        else {
            return "home";
        }
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

    @GetMapping("/passwordreset")
    public String getResetPassword( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing password reset");
        return "passwordreset";
    }

    @PostMapping("/passwordreset")
    public User resetPassword(@Valid @ModelAttribute("user") User user,  
        @RequestParam(value="action", required=false) String action, 
        Errors errors, Model model, HttpServletRequest request) {
        
        String uri_path = SPRING_USERS_URI + "/passwordreset?email=" + user.getEmail() + 
        "&oldPassword=" + user.getPassword() + "&newPassword=" + user.getNewPassword();
        System.out.println("frontend/passwordreset");
        System.out.println(uri_path);
        HttpEntity<User> userEntity = new HttpEntity<User>(user);
        User createdUser = restTemplate.postForObject(
            uri_path, 
            user, User.class);
        
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
