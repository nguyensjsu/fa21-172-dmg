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
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Value;

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
        float subtotal = 0;

        for (CartItem item : items) {
            subtotal += item.getBook().getPrice() * item.getQuantity();
        }

        return subtotal;
    }

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
    public String postAction(@ModelAttribute("command") BookCommand command,
                            @RequestParam(value="action", required=true) String action, 
                            Model model) {
        log.info( "Action: " + action);

        Book cartBook = books.findByBookID(Long.valueOf(action));
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBook(cartBook);
        cartItem.setQuantity(Integer.valueOf(command.getQuantity(action)));
        itemRepo.save(cartItem);

        log.info("Cart Item: " + cartItem);
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
