package com.example.frontend;
/*
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Slf4j
@Controller
@RequestMapping("/")
public class BooksController {

    @Autowired
    private RestTemplate booksRestTemplate;

    private final String SPRING_BOOKS_URI = "http://books:8083";

    
    @Bean
    public RestTemplate booksRestTemplate() {
        return new RestTemplate();
    }
    

    @GetMapping("/catalog")
    public String getHome( @ModelAttribute("book") Books book,
                             Model model) {
        System.out.println("Accessing catalog");
        return "catalog";
    }

    @PostMapping("/catalog")
    public String addToCart(@ModelAttribute("command") BookCommand command, 
                            @RequestParam(value="action", required=true) String action, 
                            Model model, HttpServletRequest request) {
        log.info( "Action: " + action);

        ResponseEntity<BookCommand> response = booksRestTemplate.postForEntity(SPRING_BOOKS_URI + "/catalog?bookID=" + action + "&qty=" + command.getQuantity(action), command, BookCommand.class);

        return "catalog";
    }
}
*/

