package com.example.springusers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping
    public String getHome( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing home");
        return "home";
    }

//    @GetMapping("/home")
//    public String getRegister( @ModelAttribute("user") User user,
//                               Model model) {
//        System.out.println("Accessing register");
//
//        return "home";
//    }
}
