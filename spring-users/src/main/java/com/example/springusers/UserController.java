package com.example.springusers;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j

@RestController
@RequestMapping("/")

public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public ResponseEntity<User> getUser(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) throws ServerException {
        
        User user = repository.findByEmail(email);
        System.out.println("getUser in spring-users/UserController.java");
        System.out.println("Email = " + email + ", Password = " + password);
        HttpHeaders responseHeaders = new HttpHeaders();
        //HashMap<String, String> headers = new HashMap<String, String>();
        if (user == null) {
            responseHeaders.set("status", HttpStatus.BAD_REQUEST + "");
        }

        else if (!user.getPassword().equals(password)) {
            responseHeaders.set("status",  HttpStatus.UNAUTHORIZED + "");
        }
        else {
            responseHeaders.set("status",  HttpStatus.OK + "");
        }

        System.out.println("Before response creation " + responseHeaders.toString());
        ResponseEntity response = new ResponseEntity(responseHeaders, HttpStatus.OK);
        System.out.println("after response creation " + response.getHeaders().toString());
        return response;
    }
   

    // @GetMapping("/users/{email}")
    // public ResponseEntity<User> getUser(@RequestBody final User loginInfo) 
    //     throws ServerException {
        
    //     User user = repository.findByEmail(loginInfo.getEmail());
    //     System.out.println("getUser in spring-users/UserController.java");
    //     System.out.println("Email = " + loginInfo.getEmail() + ", Password = " + loginInfo.getPassword());
    //     if (user == null) {
    //         throw new ServerException("User not found");
    //     }

    //     else if (!user.getPassword().equals(loginInfo.getPassword())) {
    //         throw new ServerException("Incorrect password!");
    //     }
    //     else {
    //         return new ResponseEntity<>(user, HttpStatus.CREATED);
    //     }
        
    // }

    // @PostMapping("/users/{email}")
    // public ResponseEntity<User> login(@PathVariable final String email, @PathVariable final String password) {
    //     log.info(" Email : " + email + ", Password: " + password) ;
        
    // }

    // @GetMapping("/register")
    // public String getRegister( @ModelAttribute("user") User user,
    //                        Model model) {
    //     System.out.println("Accessing registration");
    //     return "register";
    // }
    
    @PostMapping("/users")
    public ResponseEntity<User> postAction(@RequestBody final User user,  @RequestParam(value="action", required=false) String action, Errors errors, Model model, HttpServletRequest request) throws ServerException {
        log.info(" User : " + user) ;
        HttpHeaders responseHeaders = new HttpHeaders();
        if (user == null) {
			responseHeaders.set("status", HttpStatus.INTERNAL_SERVER_ERROR + "");
		}
        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null) {
            responseHeaders.set("status", HttpStatus.CONFLICT + "");
        } 
        else {
            repository.save(user);
            responseHeaders.set("status", HttpStatus.OK + "");
        }   
        System.out.println("Before response creation " + responseHeaders.toString());
        ResponseEntity response = new ResponseEntity(responseHeaders, HttpStatus.OK);
        System.out.println("after response creation " + response.getHeaders().toString());
        return response;      
    }

    @PostMapping("/passwordreset")
    public ResponseEntity<String> resetPassword(@RequestParam(value = "email") String email, 
        @RequestParam(value = "oldPassword") String oldPassword,
        @RequestParam(value = "newPassword") String newPassword) throws ServerException {
        System.out.println("backend/passwordreset");
        System.out.println("Email = " + email + ", Old password = " + oldPassword + ", New Password = " + newPassword);
        HttpHeaders responseHeaders = new HttpHeaders();

        User user = repository.findByEmail(email);
        if (user == null) {
			responseHeaders.set("status", HttpStatus.INTERNAL_SERVER_ERROR + "");
		}
        else if (!user.getPassword().equals(oldPassword)) {
            responseHeaders.set("status", HttpStatus.CONFLICT + "");
        } 
        else {
            user.setPassword(newPassword);
            repository.save(user);
            responseHeaders.set("status", HttpStatus.OK + "");
        } 
        System.out.println("Before response creation " + responseHeaders.toString());
        ResponseEntity response = new ResponseEntity(responseHeaders, HttpStatus.OK);
        System.out.println("after response creation " + response.getHeaders().toString());
        return response;        
    }
//    @GetMapping("/home")
//    public String getRegister( @ModelAttribute("user") User user,
//                               Model model) {
//        System.out.println("Accessing register");
//
//        return "home";
//    }
}
