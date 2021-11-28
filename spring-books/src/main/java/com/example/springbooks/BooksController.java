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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Value;

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
    private BooksRepository books;

    @Autowired 
    private ShoppingCartRepository cartRepo;
    @Autowired
    private CartItemRepository itemRepo;

    private Long userID = Long.valueOf(1);

    // Initialize shopping cart
    // TODO: make userId match user
    private ShoppingCart cart = new ShoppingCart(userID);

    public ArrayList<CartItem> getItems(ShoppingCart cartIn) {
        ArrayList<CartItem> items = itemRepo.findByCart(cartIn);
        return items;
    }

    public float calculateSubtotal(ShoppingCart cartIn) {
        List<CartItem> items = itemRepo.findByCart(cart);
        float subtotal = 0;

        for (CartItem item : items) {
            subtotal += item.getBook().getPrice() * item.getQuantity();
        }

        return subtotal;
    }

    // Need to fix
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



    @PostMapping("/catalog")
    public ResponseEntity postAction(@RequestParam(value="bookID") String bookID,
                             @RequestParam(value="qty") String qty) {
        log.info( "Book ID: " + bookID);
        log.info( "Quantity: " + qty);
        log.info("Cart ID: " + cart.getCartId());

        if(cartRepo.findByCartId(cart.getCartId()) == null) {
            cartRepo.save(cart);
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
    public ResponseEntity<ArrayList<CartItem>> getCart( @ModelAttribute("shoppingcart") ShoppingCart cart,
                             Model model) {
        System.out.println("Accessing shopping cart");
        
        ArrayList<CartItem> items = getItems(cartRepo.findByUserId(userID));
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

    // Need to Fix Below
    @PostMapping("/shoppingcart")
    public void postCart(@RequestParam(value="action", required=true) String action, 
                            Model model) {
        
        log.info( "Action: " + action);
        List<CartItem> items = getItems(cartRepo.findByUserId(userID));
        
        if(action.equals("checkout")) {
            
        } else if(action.equals("clear")) {
            for (CartItem item : items) {
                itemRepo.deleteById(item.getItemID());
                log.info("Removed Item " + item.getItemID());
            }
        } else {
            itemRepo.deleteById(Long.valueOf(action));
        }

        getCart(cart, model);
        //return "shoppingcart";
    }
}
