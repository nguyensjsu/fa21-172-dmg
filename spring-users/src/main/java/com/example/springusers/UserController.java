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

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUser(@PathVariable final String email, @PathVariable final String password) throws ServerException {
        
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new ServerException("User not found");
        }

        else if (!user.getPassword().equals(password)) {
            throw new ServerException("Incorrect password!");
        }
        else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        
    }

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
    public User postAction(@RequestBody final User user,  @RequestParam(value="action", required=false) String action, Errors errors, Model model, HttpServletRequest request) throws ServerException {
        log.info(" User : " + user) ;
        if (user == null) {
			throw new ServerException("The user sent is null.");
		}
        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "That email is already registered!");
        } 
        else {
            return repository.save(user);
        }         
    }
//    @GetMapping("/home")
//    public String getRegister( @ModelAttribute("user") User user,
//                               Model model) {
//        System.out.println("Accessing register");
//
//        return "home";
//    }
}
