package com.example.springbooks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Slf4j
@Controller
@EnableWebMvc

public class BooksController {

        @GetMapping("/catalog")
        public String getHome( @ModelAttribute("book") Books book,
                               Model model) {
            System.out.println("Accessing catalog");
            return "catalog";
        }


    }
