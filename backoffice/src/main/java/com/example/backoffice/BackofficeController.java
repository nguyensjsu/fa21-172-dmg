package com.example.backoffice;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j
@Controller
@RequestMapping("/")
public class BackofficeController {

    @Autowired
	private RestTemplate restTemplate;

    //run on docker
    private String SPRING_PAYMENTS_URI = "http://payments:8081";
    private String SPRING_USERS_URI = "http://users:8082";
    private String SPRING_BOOKS_URI = "http://books:8083";

    //private String KONG_URI = "http://kong:8000/users";
    private String KONG_URI = "http://kong-proxy/users";
    private String apiKey = "2H3fONTa8ugl1IcVS7CjLPnPIS2Hp9dJ";

    //run locally
    //private String SPRING_PAYMENTS_URI = "http://localhost:8081";
    //private String SPRING_USERS_URI = "http://localhost:8082";
    //private String SPRING_BOOKS_URI = "http://localhost:8083";



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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

    // Kong Test
    @GetMapping("/ping")
    public String kongPing() {
        ResponseEntity<String> response = restTemplate.getForEntity(KONG_URI + "/users/ping?apikey=" + apiKey, String.class);
        System.out.println(response.getBody());

        return response.getBody();
    }

    /*
    Spring Users
    */

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


    
    @GetMapping("/reset_dne")
    public String resetDNE(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing reset_dne");
        return "reset_dne";
    }

    @GetMapping("/reset_inc")
    public String resetInc(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing reset_inc");
        return "reset_inc";
    }
    
    @GetMapping("/reset_suc")
    public String registrationSuccessful(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing reset_suc");
        return "reset_suc";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute("user") User user,  
            @RequestParam(value="action", required=false) String action, 
            Errors errors, Model model, HttpServletRequest request) 
            throws RestClientException, Exception {
        log.info(" User : " + user) ;
        System.out.println("frontend/FrontendController.java");
        System.out.println("Email = " + user.getEmail() + ", Password = " + user.getPassword());
        ResponseEntity<User> response = restTemplate.getForEntity(KONG_URI + "/users?email=" + user.getEmail() + "&password=" + user.getPassword() + "&apikey=" + apiKey, User.class, user);
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
            model.addAttribute("email", user.getEmail());
            return "login_suc";
        }
        else {
            return "home";
        }
    }
  

    @GetMapping("/passwordreset")
    public String getResetPassword( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing password reset");
        return "passwordreset";
    }

    @PostMapping("/passwordreset")
    public String resetPassword(@Valid @ModelAttribute("user") User user, 
        @ModelAttribute("command") UserCommand command, 
        @RequestParam(value="action", required=false) String action, 
        Errors errors, Model model, HttpServletRequest request) {
        
        String uri_path = KONG_URI + "/passwordreset?email=" + user.getEmail() + 
        "&oldPassword=" + user.getPassword() + "&newPassword=" + user.getNewPassword()  + "&apikey=" + apiKey;
        System.out.println("frontend/passwordreset");
       // System.out.println(uri_path);
        HttpEntity<User> userEntity = new HttpEntity<User>(user);
        ResponseEntity<User> response = restTemplate.postForEntity(
            uri_path, 
            user, User.class);

        System.out.println("header = " + response.getHeaders().getFirst("status"));    
        if (response.getHeaders().getFirst("status").equals(HttpStatus.INTERNAL_SERVER_ERROR + "")) {
            System.out.println("Email address not registered.");
            return "reset_dne";
        }

        else if (response.getHeaders().getFirst("status").equals(HttpStatus.CONFLICT + "")) {
            System.out.println("Email address not registered.");
            return "reset_inc";
        }
        else if (response.getHeaders().getFirst("status").equals(HttpStatus.OK + "")){
            return "reset_suc";
        }
        else {
            return "passwordreset";
        }
    }


    double total = 25;
    int min = 1239871 ;
    int max = 9999999 ;
    int random_int = (int) Math.floor(Math.random()*(max-min+1)+min) ;
    String order_num = String.valueOf(random_int) ;
    double balance;
    String fname;
    String lname;
    String address;
    String city;
    String state;
    String zip;
    String cardnum;
    String exp;
    String phone;
    String email;

}
