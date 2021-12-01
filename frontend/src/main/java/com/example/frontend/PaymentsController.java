package com.example.frontend;

/*
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@Controller
@RequestMapping("/")
public class PaymentsController {

    @Autowired
    private RestTemplate restTemp;
    //run on Docker
//    private String SPRING_PAYMENTS_URI = "http://payments:8081";

    //run locally
        private String SPRING_PAYMENTS_URI = "http://localhost:8081";
    @Bean
    public RestTemplate restTemp() {
        return new RestTemplate();
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
        PaymentsCommand payment = restTemp.postForObject(SPRING_PAYMENTS_URI + "/command", command, PaymentsCommand.class);
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

        PaymentsCommand pay = restTemp.postForObject(SPRING_PAYMENTS_URI + "/placeorder?email=" + command.getEmail(), command, PaymentsCommand.class);
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


}
*/




