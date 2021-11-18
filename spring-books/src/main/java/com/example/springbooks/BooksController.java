package com.example.springbooks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;



@Slf4j
@Controller
@RequestMapping("/")
public class BooksController {

        @GetMapping
        public String getHome( @ModelAttribute("user") Book book,
                               Model model) {
            System.out.println("Accessing catalog");
            return "catalog";
        }


    }
