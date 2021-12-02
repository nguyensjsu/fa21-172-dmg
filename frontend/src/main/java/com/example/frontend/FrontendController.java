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

    @PostMapping
    public String login(@Valid @ModelAttribute("user") User user,  
            @RequestParam(value="action", required=false) String action, 
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
    public String getCatalog (@ModelAttribute("command") BookCommand command,
                            Model model) {
        System.out.println("Accessing catalog");
        System.out.println("Email: " + model.getAttribute("email"));
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
    public String getCart (Model model) {
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

    @PostMapping("/shoppingcart")
    public void postCart(@RequestParam(value="action", required=true) String action, 
                        Model model) {
        
        log.info("Action: " + action);

        ResponseEntity<String> response = restTemplate.postForEntity(SPRING_BOOKS_URI + "/shoppingcart?action=" + action, action, String.class);

        getCart(model);
        
        /*
        if(action.equals("checkout")) {
            
        } else if(action.equals("clear")) {
            ResponseEntity<String> response = restTemplate.postForEntity(SPRING_BOOKS_URI + "/shoppingcart", action, String.class);
        } else {
            
        }
        */
    }

    /* 
    Spring Payments
    */

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


    @GetMapping("/creditcards")
    public String getAction(@ModelAttribute("command") PaymentsCommand command,
                            Model model) {
        log.info("Command: " + command);
//        PaymentsCommand paym = restTemp.getForObject(SPRING_PAYMENTS_URI + "/creditcards", command, PaymentsCommand.class);

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
        model.addAttribute("card_balance",balance);
        model.addAttribute("exp", exp);
        model.addAttribute("email", email);
        return "creditcards";
    }
    @PostMapping("/creditcards")
    public PaymentsCommand postAction(@Valid @ModelAttribute("command") PaymentsCommand command,
                                      @RequestParam(value="action", required=true) String action,
                                      Errors errors, Model model, HttpServletRequest request) {

        log.info("Action: " + action);
        log.info("Command: " + command);
        PaymentsCommand payment = restTemplate.postForObject(SPRING_PAYMENTS_URI + "/command", command, PaymentsCommand.class);
        command.setTransactionAmount( 300.00) ;
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

        model.addAttribute("card_num", command.getCardnumber());
        model.addAttribute("card_balance", command.getTransactionAmount());
        model.addAttribute("exp", exp);


        return payment;
    }

    @Getter
    @Setter
    class Message {
        private String msg;

        public Message(String m) {
            msg = m;
        }
    }

    class ErrorMessages {
        private ArrayList<Message> messages = new ArrayList<Message>();

        public void add(String msg) {
            messages.add(new Message(msg));
        }

        public ArrayList<Message> getMessages() {
            return messages;
        }

        public void print() {
            for (Message m : messages) {
                System.out.println(m.msg);
            }
        }
    }



    @GetMapping("/placeorder")
    public String getPlaceOrder( PaymentsCommand command, Model model ){
        log.info("Accessing place order method " );

//        balance = command.getTransactionAmount();
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
        model.addAttribute("email", email);
        return "placeorder";

    }

    @PostMapping("/placeorder")
    public PaymentsCommand postOrder(@Valid @ModelAttribute("command") PaymentsCommand command,
                            @RequestParam(value="placeorder", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) {
//    public String postOrder(@Valid @ModelAttribute("command") PaymentsCommand command,
//                                     @RequestParam(value="action", required=false) String action,
//                                     Errors errors, Model model, HttpServletRequest request) {

        log.info("Accessing place order method " );
//        ResponseEntity<PaymentsCommand> res = restTemp.postForEntity(SPRING_PAYMENTS_URI + "/placeorder?email=" + command.getEmail(), command, PaymentsCommand.class);

        PaymentsCommand pay = restTemplate.postForObject(SPRING_PAYMENTS_URI + "/placeorder?email=" + command.getEmail(), command, PaymentsCommand.class);
        balance = balance - total;
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
        log.info("Balance:"  + balance);

        return pay;
    }

}
