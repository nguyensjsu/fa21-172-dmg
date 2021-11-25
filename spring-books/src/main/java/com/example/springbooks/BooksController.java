package com.example.springbooks;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;


@Slf4j
@Controller
//@EnableWebMvc
public class BooksController {

    @Autowired
    private BooksRepository books;

    @Autowired 
    private ShoppingCartRepository cartRepo;
    @Autowired
    private CartItemRepository itemRepo;

    private Long userID = Long.valueOf(1);

    // Initialize shopping cart
    // TODO: make userId match user
    private ShoppingCart cart = new ShoppingCart(userID);

    public List<CartItem> getItems(ShoppingCart cartIn) {
        List<CartItem> items = itemRepo.findByCart(cartIn);
        return items;
    }

    public float calculateSubtotal(ShoppingCart cartIn) {
        List<CartItem> items = itemRepo.findByCart(cart);
        //log.info("Shopping Cart: " + items);

        float subtotal = 0;

        for (CartItem item : items) {
            subtotal += item.getBook().getPrice();
        }

        return subtotal;
    }

    @GetMapping("/catalog")
    public String getHome( @ModelAttribute("book") Book book,
                             Model model) {
        System.out.println("Accessing catalog");
        
        // Test cartRepo
        if(cartRepo.findByCartId(cart.getCartId()) == null) {
            cartRepo.save(cart);
        }
        

        return "catalog";
    }

    @PostMapping("/catalog")
    public String postAction(@RequestParam(value="action", required=true) String action, 
                            Model model) {
        log.info( "Action: " + action);
        
        //Book findByISBN = new Book();
        //findByISBN.setIsbn(action);
        //Example<Book> findByISBNExample = Example.of(findByISBN);
        //log.info("Book: " + findByISBNExample);

        //log.info("Book: " + books.findByisbn(action));

        Book cartBook = books.findByisbn(action);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBook(cartBook);
        cartItem.setQuantity(1);
        itemRepo.save(cartItem);
        
        //log.info("Cart Item: " + cartItem);
        //log.info("Cart: " + cart);

        log.info("Subtotal: " + calculateSubtotal(cart));
        //log.info("User Cart: " + cartRepo.findByUserId(userID));

        return "catalog";
    }

    @GetMapping("/shoppingcart")
    public String getCart( @ModelAttribute("shoppingcart") ShoppingCart cart,
                             Model model) {
        System.out.println("Accessing shopping cart");
        
        List<CartItem> items = getItems(cartRepo.findByUserId(userID));
        model.addAttribute("items", items);
        
        float subtotal = calculateSubtotal(cartRepo.findByUserId(userID));
        model.addAttribute("subtotal", String.valueOf(subtotal));

        log.info("Cart Total: " + String.valueOf(subtotal));

        return "shoppingcart";
    }

    @PostMapping("/shoppingcart")
    public String postCart(@RequestParam(value="action", required=true) String action, 
                            Model model) {
        
        log.info( "Action: " + action);

        return "shoppingcart";
    }

    /*
    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") GumballCommand command,  
                            @RequestParam(value="action", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) throws GumballServerError {
    
        log.info( "Action: " + action ) ;
        log.info( "Command: " + command ) ;
    
        String inputHash = command.getHash();
        String inputState = command.getState();
        String inputTime = command.getTimestamp();

        String inputText = inputState + "/" + inputTime;
        String calculatedHash = hmac_sha256(key, inputText);
        
        log.info("Input Hash: " + inputHash);
        log.info("Valid Hash: " + calculatedHash);
        
        
        if(!inputHash.equals(calculatedHash)) {
            throw new GumballServerError();
        }

        long time1 = Long.parseLong(inputTime);
        long time2 = java.lang.System.currentTimeMillis();
        long timeDiff = time2 - time1;

        log.info("Input Timestamp: " + String.valueOf(time1));
        log.info("Current Timestamp: " + String.valueOf(time2));
        log.info("Timestamp Delta: " + String.valueOf(timeDiff));

        
        if((timeDiff / 1000) > 1000) {
            throw new GumballServerError();
        }

        GumballMachine gm = new GumballMachine();
        gm.setState(inputState);

        if ( action.equals("Insert Quarter") ) {
            gm.insertQuarter() ;
        }

        if ( action.equals("Turn Crank") ) {
            command.setMessage("") ;
            gm.turnCrank() ;
        } 

        String message = gm.toString();
        String state = gm.getState().getClass().getName();
        command.setState(state);

        Long timeLong = java.lang.System.currentTimeMillis();
        String timeString = String.valueOf(timeLong);
        command.setTimestamp(timeString);

        String text = state + "/" + timeString;
        String hashString = hmac_sha256(key, text);
        command.setHash(hashString);

        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
  
        } catch (Exception e) { }
  
        model.addAttribute( "hash", hashString ) ;
        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;
     

        if (errors.hasErrors()) {
            return "gumball";
        }

        return "gumball";
    }
    */


}
