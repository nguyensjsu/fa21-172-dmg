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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j
@Controller
//@RequestMapping("/")


public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public String getHome( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing home");
        return "home";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute("user") User user,  @RequestParam(value="action", required=false) String action, Errors errors, Model model, HttpServletRequest request) {
        log.info(" User : " + user) ;
        User existingUser = repository.findByEmail(user.getEmail());
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
        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "That email is already registered!");
        }
        return repository.save(user);          
    }
//    @GetMapping("/home")
//    public String getRegister( @ModelAttribute("user") User user,
//                               Model model) {
//        System.out.println("Accessing register");
//
//        return "home";
//    }
}
