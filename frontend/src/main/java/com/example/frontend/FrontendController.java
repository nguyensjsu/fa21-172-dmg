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
public class FrontendController {

    @Autowired
	private RestTemplate restTemplate;

    //run on docker
    private String SPRING_PAYMENTS_URI = "http://payments:8081";
    private String SPRING_USERS_URI = "http://users:8082";
    private String SPRING_BOOKS_URI = "http://books:8083";

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

    /*
    Spring Users
    */

    @GetMapping
    public String getHome( @ModelAttribute("user") User user,
                           Model model) {
        System.out.println("Accessing home");
        System.out.println("frontend changed.");
        System.out.println("latest edit 12/1/2021 9:20am");
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
                                @ModelAttribute("command") BookCommand command,
                                Model model)  {
        
        command.setUser(user.getEmail());
        //System.out.println("Accessing login_suc " + command.getEmail());
        return "login_suc";
    }

    @GetMapping("/register_null")
    public String registerNull(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing register_null");
        return "register_null";
    }

    @GetMapping("/register_alr")
    public String registerAlready(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing register_alr");
        return "register_alr";
    }
    
    // @GetMapping("/reset_dne")
    // public String resetDNE(@ModelAttribute("user") User user,
    // Model model)  {
        
    //     System.out.println("Accessing reset_dne");
    //     return "reset_dne";
    // }

    // @GetMapping("/reset_inc")
    // public String resetInc(@ModelAttribute("user") User user,
    // Model model)  {
        
    //     System.out.println("Accessing reset_inc");
    //     return "reset_inc";
    // }
    
    // @GetMapping("/reset_suc")
    // public String registrationSuccessful(@ModelAttribute("user") User user,
    // Model model)  {
        
    //     System.out.println("Accessing reset_suc");
    //     return "reset_suc";
    // }

    @GetMapping("/register_suc")
    public String registerSucceeded(@ModelAttribute("user") User user,
    Model model)  {
        
        System.out.println("Accessing register_suc");
        return "register_suc";
    }

    @PostMapping("/")
    public String login(@Valid @ModelAttribute("user") User user,  
            @RequestParam(value="action", required=false) String action, 
            @ModelAttribute("command") BookCommand command,
            Errors errors, Model model, HttpServletRequest request) 
            throws RestClientException, Exception {

        log.info(" User : " + user) ;
        System.out.println("frontend/FrontendController.java");
        System.out.println("Email = " + user.getEmail() + ", Password = " + user.getPassword());

        ResponseEntity<User> response = restTemplate.getForEntity(SPRING_USERS_URI + "/users?email=" + user.getEmail() + "&password=" + user.getPassword(), User.class, user);
        //User existingUser = restTemplate.getForObject(SPRING_USERS_URI + "/users/" + user.getEmail(), User.class, toMap(user));
        //User existingUser = repository.findByEmail(user.getEmail());
        System.out.println("After sending GET to backend: ");
        System.out.println("response " + response.toString());
        System.out.println("Headers " + response.getHeaders());
        if (response.getHeaders().getFirst("status").equals(HttpStatus.BAD_REQUEST + "")) {
            //return "login_dne";
            return userDoesNotExist(user, model);
        }

        else if (response.getHeaders().getFirst("status").equals(HttpStatus.UNAUTHORIZED + "")) {
            //return "login_inc";
            return incorrectPassword(user, model);
        }
        else if (response.getHeaders().getFirst("status").equals(HttpStatus.OK + "")){
            model.addAttribute("email", user.getEmail());
            //log.info("Login email: ", command.getEmail());

            

            return loginSuccessful(user, command, model);
            //return "login_suc";
        }
        else {
            //return "home";
            return getHome(user, model);
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

    // @GetMapping("/passwordreset")
    // public String getResetPassword( @ModelAttribute("user") User user,
    //                        Model model) {
    //     System.out.println("Accessing password reset");
    //     return "passwordreset";
    // }

    // @PostMapping("/passwordreset")
    // public String resetPassword(@Valid @ModelAttribute("user") User user, 
    //     @ModelAttribute("command") UserCommand command, 
    //     @RequestParam(value="action", required=false) String action, 
    //     Errors errors, Model model, HttpServletRequest request) {
        
    //     String uri_path = SPRING_USERS_URI + "/passwordreset?email=" + user.getEmail() + 
    //     "&oldPassword=" + user.getPassword() + "&newPassword=" + user.getNewPassword();
    //     System.out.println("frontend/passwordreset");
    //    // System.out.println(uri_path);
    //     HttpEntity<User> userEntity = new HttpEntity<User>(user);
    //     ResponseEntity<User> response = restTemplate.postForEntity(
    //         uri_path, 
    //         user, User.class);

    //     System.out.println("header = " + response.getHeaders().getFirst("status"));    
    //     if (response.getHeaders().getFirst("status").equals(HttpStatus.INTERNAL_SERVER_ERROR + "")) {
    //         System.out.println("Email address not registered.");
    //         return "reset_dne";
    //     }

    //     else if (response.getHeaders().getFirst("status").equals(HttpStatus.CONFLICT + "")) {
    //         System.out.println("Email address not registered.");
    //         return "reset_inc";
    //     }
    //     else if (response.getHeaders().getFirst("status").equals(HttpStatus.OK + "")){
    //         return "reset_suc";
    //     }
    //     else {
    //         return "register";
    //     }
    // }


    /*
    Spring Books
    */

    @GetMapping("/catalog")
    public String getCatalog (@RequestParam(value="email") String email,
                            @ModelAttribute("command") BookCommand command,
                            Model model) {

        System.out.println("Accessing catalog");
        System.out.println("Email: " + email);
        //command.setEmail("");
        command.setUser(email);
        model.addAttribute("email", email);
        return "catalog";
    }

    @PostMapping("/catalog")
    public String addToCart(@ModelAttribute("command") BookCommand command, 
                            @RequestParam(value="action", required=true) String action, 
                            Model model, HttpServletRequest request) {
        log.info( "Action: " + action);
        System.out.println("Command: " + command.getUser());
        String email = command.getUser();
        System.out.println(email);
        
        // Response should check for cart
        ResponseEntity<BookCommand> response = restTemplate.postForEntity(SPRING_BOOKS_URI + "/catalog?bookID=" + action + "&qty=" + command.getQuantity(action) + "&email=" + email, command, BookCommand.class);

        return getCatalog(email, command, model);
    }

    @GetMapping("/shoppingcart") 
    public String getCart (@RequestParam(value="email") String email, Model model) {
        System.out.println("Accessing shopping cart");

        ArrayList<CartItem> items = new ArrayList<CartItem>();

        // Pass email as parameter
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(SPRING_BOOKS_URI + "/shoppingcart?email=" + email, ArrayList.class, items);
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

        double subtotal = 0;
    
        for (CartItem item : items) {
            subtotal += item.getBook().getPrice() * item.getQuantity();
        }

        subtotal = Math.round(subtotal*100.0)/100.0;

        model.addAttribute("email", email);
        model.addAttribute("items", items);
        model.addAttribute("subtotal", String.valueOf(subtotal));
        

        log.info("Frontend books: " + items.toString());
        
        return "shoppingcart";
    }

    @PostMapping("/shoppingcart")
    public void postCart(@RequestParam(value="email") String email, @RequestParam(value="action", required=true) String action, 
                        Model model) {
        
        log.info("Action: " + action);

        ResponseEntity<String> response = restTemplate.postForEntity(SPRING_BOOKS_URI + "/shoppingcart?action=" + action + "&email=" + email, action, String.class);

        getCart(email, model);
        
        /*
        if(action.equals("checkout")) {
            ResponseEntity<String> response = restTemplate.postForEntity(SPRING_PAYMENTS_URI + "/shoppingcart?email=" + email + "&total=" + subtotal, action, String.class);
        } else {
             ResponseEntity<String> response = restTemplate.postForEntity(SPRING_BOOKS_URI + "/shoppingcart?action=" + action + "&email=" + email, action, String.class);
            getCart(email, model);
        }
        */
    }

    /* 
    Spring Payments
    */

    String order_num;
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
    String userId;
    double total;

    @GetMapping("/creditcards")
//    public String getAction(@ModelAttribute("command") PaymentsCommand command, Model model) {
    public String getAction(@ModelAttribute("command") PaymentsCommand command,
                            @RequestParam(value="email") String email,
                            Model model) {
        System.out.println("Accessing get request for creditcards");

        ArrayList<CartItem> items = new ArrayList<CartItem>();
        ResponseEntity<ArrayList> cartResponse = restTemplate.getForEntity(SPRING_BOOKS_URI + "/shoppingcart?email=" + email, ArrayList.class, items);

        ObjectMapper mapper = new ObjectMapper();

        for (Object item : cartResponse.getBody())
            try{
                //JsonNode jsonNode = mapper.readTree(response.getBody().toString());
                //System.out.print("JSON: " + jsonNode);
                CartItem newItem = new CartItem();

                newItem = mapper.convertValue(item, CartItem.class);
                items.add(newItem);
            } catch ( Exception e ) {
                System.out.println( e ) ;
            }

        double total = 0;

        for (CartItem item : items) {
            total += item.getBook().getPrice() * item.getQuantity();
        }

        total = Math.round(total*100.0)/100.0;

//        ResponseEntity<PaymentsCommand> response = restTemplate.getForEntity(SPRING_PAYMENTS_URI + "/creditcards", PaymentsCommand.class);
        ResponseEntity<PaymentsCommand> response = restTemplate.getForEntity(SPRING_PAYMENTS_URI + "/creditcards?email=" + email+ "&total=" + total, PaymentsCommand.class);
        log.info("Frontend Response: " + response.toString());

        command = response.getBody();
        order_num= command.getOrderNumber();
//        userId = command.getUserId();
//        total = command.getTotal();

            model.addAttribute("order_number", order_num);
            model.addAttribute("total", total);
            model.addAttribute("fname", fname);
            model.addAttribute("lname", lname);
            model.addAttribute("address", address);
            model.addAttribute("city", city);
            model.addAttribute("state", state);
            model.addAttribute("zip", zip);
            model.addAttribute("phone", phone);
            model.addAttribute("card_num", cardnum);
            model.addAttribute("card_balance", balance);
            model.addAttribute("exp", exp);
            model.addAttribute("email", email);

        return "creditcards";
    }

    @PostMapping("/creditcards")
    public String postAction(@Valid @ModelAttribute("command") PaymentsCommand command,
                                      @RequestParam(value="action", required=true) String action,
                                      Errors errors, Model model, HttpServletRequest request) {

        System.out.println("Accessing post request for creditcards");
        ResponseEntity<PaymentsCommand> response = restTemplate.postForEntity(SPRING_PAYMENTS_URI + "/command", command, PaymentsCommand.class);
        log.info("Frontend Response : " + response.toString());

        command = response.getBody();
        fname = command.getFirstname();
        lname = command.getLastname();
        address = command.getAddress();
        city = command.getCity();
        state = command.getState();
        zip = command.getZip();
        balance = command.getTransactionAmount();
        cardnum = command.getCardnumber();
        exp = command.getExpmonth();
        exp = exp + "/";
        exp = exp + command.getExpyear();
        phone = command.getPhone();
        email = command.getEmail();

        model.addAttribute("order_number", order_num);
        model.addAttribute("total", total);
        model.addAttribute("fname", command.getFirstname() );
        model.addAttribute("lname", command.getLastname());
        model.addAttribute("address", command.getAddress() );
        model.addAttribute("city", command.getCity() );
        model.addAttribute("state", command.getState() );
        model.addAttribute("zip", command.getZip());
        model.addAttribute("phone", command.getPhone());
        model.addAttribute("userId", userId);
        model.addAttribute("card_num", command.getCardnumber());
        model.addAttribute("card_balance",  balance);
        model.addAttribute("exp", exp);

        log.info("Action: " + action);

        return "creditcards";
    }


    @GetMapping("/placeorder")
    public String getPlaceOrder( PaymentsCommand command, Model model ){
        log.info("Accessing place order get method " );


        model.addAttribute("firstname", fname);
        model.addAttribute("lastname", lname);
        model.addAttribute("address", address);
        model.addAttribute("city", city);
        model.addAttribute("state", state);
        model.addAttribute("zip", zip);
        model.addAttribute("phone", phone);

        model.addAttribute("card_num", cardnum);
        model.addAttribute("card_balance",balance);
        model.addAttribute("exp", exp);
//        model.addAttribute("email", email);
        return "placeorder";

    }

    @PostMapping("/placeorder")
    public String postOrder(@Valid @ModelAttribute("command") PaymentsCommand command,
                                     @RequestParam(value="placeorder", required=true) String action,
                                     Errors errors, Model model, HttpServletRequest request) {

        log.info("Accessing place order post method " );
        ResponseEntity<PaymentsCommand> response = restTemplate.postForEntity(SPRING_PAYMENTS_URI + "/placeorder?email=" + command.getEmail(), command,PaymentsCommand.class);
        log.info("Frontend Response : " + response.toString());
        command = response.getBody();
        balance = command.getTransactionAmount();

//        balance = balance - total;
        model.addAttribute("firstname", fname);
        model.addAttribute("lastname", lname);
        model.addAttribute("address", address);
        model.addAttribute("city", city);
        model.addAttribute("state", state);
        model.addAttribute("zip", zip);
        model.addAttribute("phone", phone);

        model.addAttribute("card_num", cardnum);
        model.addAttribute("card_balance",balance);
        model.addAttribute("exp", exp);
        model.addAttribute("userId", userId);
        log.info("Balance:"  + balance);

        return "placeorder";
    }

}
