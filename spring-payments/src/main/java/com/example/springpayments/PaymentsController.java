package com.example.springpayments;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.springcybersource.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;


@Slf4j
//@Controller
@RestController
@RequestMapping("/")
public class PaymentsController {

    @Autowired
    private PaymentsCommandRepository repository;

    PaymentsController(PaymentsCommandRepository repository) {
        this.repository = repository;
    }

    private static boolean DEBUG = true;
    private CyberSourceAPI api = new CyberSourceAPI();

    @Value("${cybersource.apihost}")
    private String apiHost;
    @Value("${cybersource.merchantkeyid}")
    private String merchantKeyId;
    @Value("${cybersource.merchantsecretkey}")
    private String merchantsecretKey;
    @Value("${cybersource.merchantid}")
    private String merchantId;

    private static HashMap<String, String> months = new HashMap<>();

    static {
        months.put("January", "01");
        months.put("February", "02");
        months.put("March", "03");
        months.put("April", "04");
        months.put("May", "05");
        months.put("June", "06");
        months.put("July", "07");
        months.put("August", "08");
        months.put("September", "09");
        months.put("October", "10");
        months.put("November", "11");
        months.put("December", "12");
    }

    private static HashMap<String, String> states = new HashMap<>();

    static {
        states.put("AL", "Alabama");
        states.put("AK", "Alaska");
        states.put("AZ", "Arizona");
        states.put("AR", "Arkansas");
        states.put("CA", "California");
        states.put("CO", "Colorado");
        states.put("CT", "Connecticut");
        states.put("DE", "Delaware");
        states.put("FL", "Florida");
        states.put("GA", "Georgia");
        states.put("HI", "Hawaii");
        states.put("ID", "Idaho");
        states.put("IL", "Illinois");
        states.put("IN", "Indiana");
        states.put("IA", "Iowa");
        states.put("KS", "Kansas");
        states.put("KY", "Kentucky");
        states.put("LA", "Louisiana");
        states.put("ME", "Maine");
        states.put("MD", "Maryland");
        states.put("MA", "Massachusetts");
        states.put("MI", "Michigan");
        states.put("MN", "Minnesota");
        states.put("MS", "Mississippi");
        states.put("MO", "Missouri");
        states.put("MT", "Montana");
        states.put("NE", "Nebraska");
        states.put("NV", "Nevada");
        states.put("NH", "New Hampshire");
        states.put("NJ", "New Jersey");
        states.put("NM", "New Mexico");
        states.put("NY", "New York");
        states.put("NC", "North Carolina");
        states.put("ND", "North Dakota");
        states.put("OH", "Ohio");
        states.put("OK", "Oklahoma");
        states.put("OR", "Oregon");
        states.put("PA", "Pennsylvania");
        states.put("RI", "Rhode Island");
        states.put("SC", "South Carolina");
        states.put("SD", "South Dakota");
        states.put("TN", "Tennessee");
        states.put("TX", "Texas");
        states.put("UT", "Utah");
        states.put("VT", "Vermont");
        states.put("VA", "Virginia");
        states.put("WA", "Washington");
        states.put("WV", "West Virginia");
        states.put("WI", "Wisconsin");
        states.put("WY", "Wyoming");

    }

    double total = 25;
    int min = 1239871;
    int max = 9999999;
    int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
    String order_num = String.valueOf(random_int);
    double balance;
//    String fname;
//    String lname;
//    String address;
//    String city;
//    String state;
//    String zip;
//    String cardnum;
//    String exp;
//    String phone;

//    @PostMapping("/command")
//    public PaymentsCommand postAction(@RequestBody final PaymentsCommand command,  @RequestParam(value="action", required=false) String action,
//                                      Errors errors, Model model, HttpServletRequest request) {
//        log.info( "Action: " + action ) ;
//        log.info( "Command: " + command ) ;

//    @GetMapping("/command")
//    public PaymentsCommand getAction(@ModelAttribute("command") PaymentsCommand command,
//                            Model model) {
//        log.info("Command: " + command);
//
//
//        model.addAttribute("order_number", order_num);
//        model.addAttribute("total", total);
//        return "creditcards";
//    }


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


    @PostMapping("/command")
    public ResponseEntity<PaymentsCommand> postAction(@RequestBody final PaymentsCommand command,
                                                      @RequestParam(value = "action", required = false) String action, Errors errors, Model model, HttpServletRequest request) throws ServerException {

        HttpHeaders respHeaders = new HttpHeaders();
        log.info("Action: " + action);
        log.info("Command: " + command);


        if (errors.hasErrors()) {
            respHeaders.set("status", HttpStatus.INTERNAL_SERVER_ERROR + "");
            ResponseEntity response = new ResponseEntity(respHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

        CyberSourceAPI.setHost(apiHost);
        CyberSourceAPI.setKey(merchantKeyId);
        CyberSourceAPI.setSecret(merchantsecretKey);
        CyberSourceAPI.setMerchant(merchantId);
        CyberSourceAPI.debugConfig();

        ErrorMessages msgs = new ErrorMessages();

        boolean hasErrors = false;
        if (command.firstname().equals("")) {
            hasErrors = true;
            msgs.add("First Name Required.");
        }
        if (command.lastname().equals("")) {
            hasErrors = true;
            msgs.add("Last Name Required.");
        }
        if (command.address().equals("")) {
            hasErrors = true;
            msgs.add("Address Required.");
        }
        if (command.city().equals("")) {
            hasErrors = true;
            msgs.add("City Required.");
        }
        if (command.state().equals("")) {
            hasErrors = true;
            msgs.add("State Required.");
        }
        if (command.zip().equals("")) {
            hasErrors = true;
            msgs.add("Zip Required.");
        }
        if (command.phone().equals("")) {
            hasErrors = true;
            msgs.add("Phone Required.");
        }
        if (command.cardnumber().equals("")) {
            hasErrors = true;
            msgs.add("Credit Card Number Required.");
        }
        if (command.expmonth().equals("")) {
            hasErrors = true;
            msgs.add("Credit Card Expiration Month Required.");
        }
        if (command.expyear().equals("")) {
            hasErrors = true;
            msgs.add("Credit Card Expiration Year Required.");
        }
        if (command.cvv().equals("")) {
            hasErrors = true;
            msgs.add("Credit Card CVV Required.");
        }
        if (command.email().equals("")) {
            hasErrors = true;
            msgs.add("Email Address Required.");
        }

        if (!command.zip().matches("\\d{5}")) {
            hasErrors = true;
            msgs.add("Invalid Zip.");
        }
        if (!command.phone().matches("[(]\\d{3}[)]-\\d{3}-\\d{4}")) {
            hasErrors = true;
            msgs.add("Invalid Phone.");
        }
        if (!command.cardnumber().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            hasErrors = true;
            msgs.add("Invalid Card Number.");
        }
        if (!command.expyear().matches("\\d{4}")) {
            hasErrors = true;
            msgs.add("Invalid Card Expiration Year.");
        }
        if (!command.cvv().matches("\\d{3}")) {
            hasErrors = true;
            msgs.add("Invalid Card CVV.");
        }

        if (months.get(command.expmonth()) == null) {
            hasErrors = true;
            msgs.add("Invalid Card Expiration Month: " + command.expmonth());
        }
        if (states.get(command.state()) == null) {
            hasErrors = true;
            msgs.add("Invalid State: " + command.state());
        }

        if (hasErrors) {
            msgs.print();
            ArrayList<Message> error = msgs.getMessages();
            respHeaders.set("status", String.valueOf(error));
            ResponseEntity response = new ResponseEntity(respHeaders, HttpStatus.BAD_REQUEST);
            return response;
//            model.addAttribute ( "messages", msgs.getMessages() ) ;
//            return "creditcards" ;
        }
//        int min = 1239871 ;
//        int max = 9999999 ;
//        int random_int = (int) Math.floor(Math.random()*(max-min+1)+min) ;
//        String order_num = String.valueOf(random_int) ;
        AuthRequest auth = new AuthRequest();
//        auth.reference = order_num ;
        auth.billToFirstName = command.firstname();
        auth.billToLastName = command.lastname();
        auth.billToAddress = command.address();
        auth.billToCity = command.city();
        auth.billToState = command.state();
        auth.billToZipCode = command.zip();
        auth.billToPhone = command.phone();
        auth.billToEmail = command.email();
        auth.transactionAmount = "300.00";
        auth.transactionCurrency = "USD";
        auth.cardNumber = command.cardnumber();
        auth.cardExpMonth = months.get(command.expmonth());
        auth.cardExpYear = command.expyear();
        auth.cardCVV = command.cvv();
        auth.cardType = CyberSourceAPI.getCardType(auth.cardNumber);
        if (auth.cardType.equals("ERROR")) {
            System.out.println("Unsupported Credit Card Type.");
//            model.addAttribute( "message", "Unsupported Credit Card Type.") ;
//            return "creditcards";
        }
        boolean authValid = true;
        AuthResponse authResponse = new AuthResponse();
        System.out.println("\n\nAuth Request: " + auth.toJson());
        authResponse = api.authorize(auth);
        System.out.println("\n\nAuth Response " + authResponse.toJson());

        if (!authResponse.status.equals("AUTHORIZED")) {
            authValid = false;
            System.out.println(authResponse.message);
            model.addAttribute("message", authResponse.message);
//            return "creditcards" ;
        }

        boolean captureValid = true;
        CaptureRequest capture = new CaptureRequest();
        CaptureResponse captureResponse = new CaptureResponse();
        if (authValid) {
//            capture.reference = order_num ;
            capture.paymentId = authResponse.id;
            capture.transactionAmount = "300.00";
            capture.transactionCurrency = "USD";
            System.out.println("\n\nCapture Request: " + capture.toJson());
            captureResponse = api.capture(capture);
            System.out.println("\n\nCapture Response: " + captureResponse.toJson());

            if (!captureResponse.status.equals("PENDING")) {
                captureValid = false;
                System.out.println(captureResponse.message);
                model.addAttribute("message", captureResponse.message);
//                return "creditcards";
            }
        }

        if (authValid && captureValid) {
//            command.setOrderNumber( order_num ) ;
            command.setTransactionAmount(300.00);
            command.setTransactionCurrency("USD");
            command.setAuthId(authResponse.id);
            command.setAuthStatus(authResponse.status);
            command.setCaptureId(captureResponse.id);
            command.setCaptureStatus(captureResponse.status);

//            repository.save ( command ) ;
//        fname = command.getFirstname();
//        lname = command.getLastname();
//        address = command.getAddress();
//        city = command.getCity();
//        state = command.getState();
//        zip = command.getZip();
//        balance = command.getTransactionAmount();
//        cardnum = command.getCardnumber();
//        exp = command.getExpmonth();
//        exp = exp + "/";
//        exp = exp + command.getExpyear();
//        phone = command.getPhone();


        }
        repository.save(command);
        respHeaders.set("status", HttpStatus.OK + "");
        ResponseEntity response = new ResponseEntity(respHeaders, HttpStatus.OK);
        return response;

//        return repository.save(command);
    }


//    @GetMapping("/place_order")
//    public String getPlaceOrder(@ModelAttribute("command") PaymentsCommand command,
//                        Model model) {


//public PaymentsCommand postAction(@RequestBody final PaymentsCommand command,  @RequestParam(value="action", required=false) String action,
//                         Errors errors, Model model, HttpServletRequest request) {
//@GetMapping(value ="/place_order")
//public ResponseEntity<PaymentsCommand> getPlaceOrder( @ModelAttribute("place_order")PaymentsCommand command, Model model){
//
//
//        log.info("Accessing get place order method " );
//        if(balance < total){
//            System.out.println("Cannot process payment. Insufficient funds");
//            String error = "Cannot process payment. Insufficient funds";
//            ResponseEntity response = new ResponseEntity(error, HttpStatus.OK);
//            return response;
//        }
//        double new_balance  = balance - total;
//        command.setTransactionAmount(new_balance);
//        repository.save(command);
//        ResponseEntity response = new ResponseEntity(new_balance, HttpStatus.OK);

//        HttpHeaders respHeaders = new HttpHeaders();
//        log.info("Accessing get place order method " );
//        if(balance < total){
//            System.out.println("Cannot process payment. Insufficient funds");
//            respHeaders.set("status", HttpStatus.BAD_REQUEST + "Cannot process payment. Insufficient funds");
//            ResponseEntity response = new ResponseEntity(respHeaders, HttpStatus.BAD_REQUEST);
//            return response;
//        }
//        double new_balance  = balance - total;
//        command.setTransactionAmount(new_balance);
//        repository.save(command);
//        respHeaders.set("status", HttpStatus.OK + "Thank you for your payment");
//        ResponseEntity response = new ResponseEntity(respHeaders, HttpStatus.OK);
//        return response;
//    return response;
//
//    }

    @PostMapping("/place_order")
    public ResponseEntity<PaymentsCommand> postOrder(@RequestBody final PaymentsCommand command,
                                                      @RequestParam(value = "action", required = false) String action, Errors errors, Model model, HttpServletRequest request) throws ServerException {

        log.info("Accessing get place order method ");
        if (balance < total) {
            System.out.println("Cannot process payment. Insufficient funds");
            String error = "Cannot process payment. Insufficient funds";
            ResponseEntity response = new ResponseEntity(error, HttpStatus.OK);
            return response;
        }
        double new_balance = balance - total;
        command.setTransactionAmount(new_balance);
        repository.save(command);
        ResponseEntity response = new ResponseEntity(new_balance, HttpStatus.OK);
        return response;

    }
}




