package com.example.frontend;

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
public class UserController {

    @Autowired
	private RestTemplate restTemplate;

    //run on docker
    private String SPRING_USERS_URI = "http://users:8082";
    private String SPRING_BOOKS_URI = "http://books:8083";

    //run locally
//    private String SPRING_USERS_URI = "http://localhost:8082";
//    private String SPRING_BOOKS_URI = "http://localhost:8083";



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

    @GetMapping("/register_null")
    public String registerNull(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing register_null");
        return "register_null";
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

    @GetMapping("/register_suc")
    public String registerSucceeded(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing register_suc");
        return "register_suc";
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
    public String postAction(@Valid @ModelAttribute("user") User user,  @RequestParam(value="action", required=false) String action, Errors errors, Model model, HttpServletRequest request) {
        log.info(" User : " + user) ;
        ResponseEntity<User> response = restTemplate.postForEntity(SPRING_USERS_URI + "/users", user, User.class);
        // if (existingUser != null) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "That email is already registered!");
        // }
        System.out.println("frontend/register");
        System.out.println("response " + response.toString());
        System.out.println("Headers " + response.getHeaders());
        if (response.getHeaders().getFirst("status").equals(HttpStatus.INTERNAL_SERVER_ERROR + "")) {
            return "register_null";
        }

        else if (response.getHeaders().getFirst("status").equals(HttpStatus.CONFLICT + "")) {
            return "register_alr";
        }
        else if (response.getHeaders().getFirst("status").equals(HttpStatus.OK + "")){
            return "register_suc";
        }
        else {
            return "register";
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
        @RequestParam(value="action", required=false) String action, 
        Errors errors, Model model, HttpServletRequest request) {
        
        String uri_path = SPRING_USERS_URI + "/passwordreset?email=" + user.getEmail() + 
        "&oldPassword=" + user.getPassword() + "&newPassword=" + user.getNewPassword();
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
            return "register";
        }
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


// spring-books requests
    @GetMapping("/catalog")
    public String getCatalog (@ModelAttribute("command") BookCommand command,
                            Model model) {
        System.out.println("Accessing catalog");
        return "catalog";
    }

    @PostMapping("/catalog")
    public String addToCart(@ModelAttribute("command") BookCommand command, 
                            @RequestParam(value="action", required=true) String action, 
                            Model model, HttpServletRequest request) {
        log.info( "Action: " + action);

        ResponseEntity<BookCommand> response = restTemplate.postForEntity(SPRING_BOOKS_URI + "/catalog?bookID=" + action + "&qty=" + command.getQuantity(action), command, BookCommand.class);

        return "catalog";
    }

    @GetMapping("/shoppingcart") 
    public String getCart (@ModelAttribute("book") Book book, Model model) {
        System.out.println("Accessing shopping cart");

        ArrayList<CartItem> items = new ArrayList<CartItem>();

        ResponseEntity<ArrayList> response = restTemplate.getForEntity(SPRING_BOOKS_URI + "/shoppingcart", ArrayList.class, items);
        log.info("Frontend Response: " + response.toString());
        
        ObjectMapper mapper = new ObjectMapper();

        for (Object item : response.getBody())
            try{
                //JsonNode jsonNode = mapper.readTree(response.getBody().toString());
                //System.out.print("JSON: " + jsonNode);
                CartItem newItem = new CartItem();

                newItem = mapper.convertValue(item, CartItem.class);
                items.add(newItem);
            } catch ( Exception e ) { 
                System.out.println( e ) ; 
            }


        
        
        float subtotal = 0;
    
        for (CartItem item : items) {
            subtotal += item.getBook().getPrice() * item.getQuantity();
        }

        model.addAttribute("items", items);
        model.addAttribute("subtotal", String.valueOf(subtotal));
        

        log.info("Frontend books: " + items.toString());
        
        return "shoppingcart";
    }
}
