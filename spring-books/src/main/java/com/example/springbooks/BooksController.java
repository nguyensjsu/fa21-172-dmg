package com.example.springbooks;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;


@Slf4j
@RestController
@RequestMapping("/")
public class BooksController {

    @Autowired
	private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private BooksRepository books;

    @Autowired 
    private ShoppingCartRepository cartRepo;
    @Autowired
    private CartItemRepository itemRepo;

    
    @Bean
    public RabbitMqReceiver receiver() {
        return new RabbitMqReceiver();
    }
    

    @Autowired
    private RabbitMqReceiver receiver;

    // TODO: Remove
    //private Long userID = Long.valueOf(1);

    //run on docker
    private String SPRING_PAYMENTS_URI = "http://payments:8081";

    //run on localhost
//    private String SPRING_PAYMENTS_URI = "http://localhost:8081";

    // Initialize shopping cart
    // TODO: make userId match user
    //private ShoppingCart cart = new ShoppingCart(userID);

    public ArrayList<CartItem> getItems(ShoppingCart cartIn) {
        ArrayList<CartItem> items = itemRepo.findByCart(cartIn);
        return items;
    }

    public float calculateSubtotal(ShoppingCart cartIn) {
        List<CartItem> items = itemRepo.findByCart(cartIn);
        float subtotal = 0;

        for (CartItem item : items) {
            subtotal += item.getBook().getPrice() * item.getQuantity();
        }

        return subtotal;
    }


    public String clearCart(String email) {
        ShoppingCart cart = cartRepo.findByEmail(email);

        System.out.println(cart);

        ArrayList<CartItem> items = getItems(cart);


        for (CartItem item : items) {
            itemRepo.deleteById(item.getItemID());
            log.info("Removed Item " + item.getItemID());
        }
        
        return "Cart cleared";
    }


    class Ping {
        private String test;

        public Ping(String msg) {
            this.test = msg;
        }

        public String getTest() {
            return this.test;
        }
    }
    
    // For testing Kong 
    @GetMapping("/ping")
    public String ping() {
        return "Spring-Books is alive!";
    }

    // May not be needed
    // Frontend returns catalog.html
    /*
    @GetMapping("/catalog")
    public String getHome( @ModelAttribute("command") BookCommand command,
                             Model model) {
        System.out.println("Accessing catalog");
        
        // Test cartRepo
        if(cartRepo.findByCartId(cart.getCartId()) == null) {
            cartRepo.save(cart);
        }

        return "catalog";
    }
    */



    @PostMapping("/catalog")
    public ResponseEntity postAction(@RequestParam(value="bookID") String bookID,
                             @RequestParam(value="qty") String qty, @RequestParam(value="email") String email) {
        log.info( "Book ID: " + bookID);
        log.info( "Quantity: " + qty);
        //log.info("Cart ID: " + cart.getCartId());

        ShoppingCart cart = new ShoppingCart();
        
        // TODO: Find by email and initialize if doesn't exist
        if(cartRepo.findByEmail(email) == null) {
            cart = new ShoppingCart(email);
            cartRepo.save(cart);
        } else {
            cart = cartRepo.findByEmail(email);
        }

        log.info("Cart ID after save: " + cart.getCartId());

        Book cartBook = books.findByBookID(Long.valueOf(bookID));
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBook(cartBook);
        cartItem.setQuantity(Integer.valueOf(qty));
        itemRepo.save(cartItem);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("status", HttpStatus.OK + "");
        
        System.out.println("Before response creation " + responseHeaders.toString());
        ResponseEntity response = new ResponseEntity(responseHeaders, HttpStatus.OK);
        System.out.println("after response creation " + response.getHeaders().toString());

        log.info("Cart Item: " + cartItem);
        return response;
    }

    
    @GetMapping(value = "/shoppingcart")
    public ResponseEntity<ArrayList<CartItem>> getCart(@RequestParam(value="email") String email, @ModelAttribute("shoppingcart") ShoppingCart cart,
                             Model model) {
        System.out.println("Accessing shopping cart");
        
        ArrayList<CartItem> items = getItems(cartRepo.findByEmail(email));
        //model.addAttribute("items", items);

        //ArrayList<Book> books = new ArrayList<Book>();
        //ArrayList<Integer> qty = new ArrayList<Integer>();

        //for (CartItem item : items) {
        //    books.add(item.getBook());
        //    qty.add(item.getQuantity());
        //}
        ResponseEntity<ArrayList<CartItem>> response = new ResponseEntity(items, HttpStatus.OK);
        
        //float subtotal = calculateSubtotal(cartRepo.findByUserId(userID));
        //model.addAttribute("subtotal", String.valueOf(subtotal));

        //log.info("Cart Total: " + String.valueOf(subtotal));
        log.info("Response: " + response.toString());

        return response;
    }

    
    @PostMapping("/shoppingcart")
    public ResponseEntity<String> postCart(@RequestParam(value="action") String action,
                            @RequestParam(value="email") String email, 
                            Model model) {
        
        log.info( "Action: " + action);
        List<CartItem> items = getItems(cartRepo.findByEmail(email));
        
        if(action.equals("checkout")) {
            String subtotal = String.valueOf(calculateSubtotal(cartRepo.findByEmail(email)));
            ResponseEntity<String> response = restTemplate.postForEntity(SPRING_PAYMENTS_URI + "/shoppingcart?email=" + email.toString() + "&total=" + subtotal, action, String.class);
        } else if(action.equals("clear")) {
            for (CartItem item : items) {
                itemRepo.deleteById(item.getItemID());
                log.info("Removed Item " + item.getItemID());
            }
        } else {
            itemRepo.deleteById(Long.valueOf(action));
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("status", HttpStatus.OK + "");
        
        ResponseEntity<String> response = new ResponseEntity(responseHeaders, HttpStatus.OK);

        return response;
        //return "shoppingcart";
    }

    @Component
    public class RabbitMqReceiver {
        private RabbitTemplate rabbitTemplate;

        private RestTemplate restTemplate;

        /*
        @Autowired
        public RabbitMqSender(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }
        */

        @Bean
        public Queue hello() {
            return new Queue("paymentConfirmation");
        }

        @Autowired
        private Queue queue;

        /*
        public void send(String msg){
            rabbitTemplate.convertAndSend( queue.getName(),msg);

        }
        */
        
        

        @RabbitListener(queues = "paymentConfirmation")
        public void receive(String message) throws Exception {
            
            System.out.println(" Rabbit Received: " + message);

            //ResponseEntity<String> response = restTemplate.getForEntity("/rabbit?email=" + message, String.class, message);
            System.out.println(clearCart(message.replaceAll("^\"|\"$", "")));
    }

}
}
