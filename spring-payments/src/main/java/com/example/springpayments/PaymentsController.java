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

@RestController
@RequestMapping("/")
public class PaymentsController {

    @Autowired
    private PaymentsCommandRepository repository;


    private RabbitMqSender rabbitMqSender;

    @Autowired
    public PaymentsController(RabbitMqSender rabbitMqSender) {
        this.rabbitMqSender = rabbitMqSender;
    }

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


    int min = 1239871;
    int max = 9999999;
    int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
    String order_num = String.valueOf(random_int);
    double balance;
    String userId ;
    double total;
    String tempemail;
    double temptotal;







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

    @GetMapping("/ping")
    public ResponseEntity<PaymentsCommand> ping() {
        String msg = "Spring-Payments is alive!";
        log.info("Ping: " + msg);
        return new ResponseEntity(msg, HttpStatus.OK) ;
    }



//    @PostMapping("/shoppingcart")
//    public ResponseEntity<PaymentsCommand> shoppingCart(@RequestParam(value="email") String email,
//                                                        @RequestParam(value="total") String total) throws ServerException {
//
//        tempemail = email;
//        temptotal = total;
//        log.info("Params from spring-books: Email = " + tempemail + "Total= " + temptotal);
//
//        return new ResponseEntity("Received", HttpStatus.OK) ;
//    }


    @GetMapping("/creditcards")
//    public ResponseEntity<PaymentsCommand> getCard() {
    public ResponseEntity<PaymentsCommand> shoppingCart(@RequestParam(value="email") String email,
                                                        @RequestParam(value="total", required = false) double subtotal) throws ServerException {
        System.out.println("Assessing get creditcards");
        PaymentsCommand command = new PaymentsCommand();

        //get email and total from springbook here
        tempemail = email;
        temptotal = subtotal;
        command.setUserId(tempemail);
        command.setTotal(temptotal);


//        command.setUserId("jonh@gmail.com");
//        command.setTotal(89.79);
        command.setOrderNumber(order_num);
        userId = command.getUserId();
        total = command.getTotal();
        repository.save(command);
        String smg =" Got shopping cart Info";
        System.out.println(smg);
        log.info("Command: " + command);
        return  ResponseEntity.accepted().body(command);
    }

    @PostMapping("/command")
    public ResponseEntity<PaymentsCommand> postAction(@RequestBody PaymentsCommand command,
             @RequestParam(value = "action", required = false) String action,Errors errors, Model model, HttpServletRequest request) throws ServerException {

        log.info("Action: " + action);
        log.info("Command: " + command);

        if (errors.hasErrors()) {
            System.out.println("Cannot connect to post request /command");
            return new ResponseEntity("Cannot connect to post request", HttpStatus.INTERNAL_SERVER_ERROR) ;
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
            System.out.println("Invalid card inputs");
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST) ;
        }

        AuthRequest auth = new AuthRequest();
        auth.reference = order_num ;
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
            return new ResponseEntity("Unsupported Credit Card Type.", HttpStatus.BAD_REQUEST) ;
        }
        boolean authValid = true;
        AuthResponse authResponse = new AuthResponse();
        System.out.println("\n\nAuth Request: " + auth.toJson());
        authResponse = api.authorize(auth);
        System.out.println("\n\nAuth Response " + authResponse.toJson());

        if (!authResponse.status.equals("AUTHORIZED")) {
            authValid = false;
            System.out.println(authResponse.message);
            return new ResponseEntity(authResponse.message, HttpStatus.BAD_REQUEST) ;
        }

        boolean captureValid = true;
        CaptureRequest capture = new CaptureRequest();
        CaptureResponse captureResponse = new CaptureResponse();
        if (authValid) {
            capture.reference = order_num ;
            capture.paymentId = authResponse.id;
            capture.transactionAmount = "300.00";
            capture.transactionCurrency = "USD";
            System.out.println("\n\nCapture Request: " + capture.toJson());
            captureResponse = api.capture(capture);
            System.out.println("\n\nCapture Response: " + captureResponse.toJson());

            if (!captureResponse.status.equals("PENDING")) {
                captureValid = false;
                System.out.println(captureResponse.message);
                return new ResponseEntity(captureResponse.message, HttpStatus.BAD_REQUEST) ;
            }
        }

        if (authValid && captureValid) {
            command.setOrderNumber( order_num ) ;
            command.setTransactionAmount(300.00);
            command.setTransactionCurrency("USD");
            command.setAuthId(authResponse.id);
            command.setAuthStatus(authResponse.status);
            command.setCaptureId(captureResponse.id);
            command.setCaptureStatus(captureResponse.status);
            balance = command.getTransactionAmount();
            repository.save(command);
        }
        return  ResponseEntity.accepted().body(command);

    }


//      @PostMapping("/placeorder")
//      public ResponseEntity<PaymentsCommand> placeOrder( @RequestBody PaymentsCommand command, @RequestParam(value="email") String email,
//                                                         @RequestParam(value="placeorder", required=false) String placeorder) throws ServerException{

    @PostMapping("/placeorder")
    public ResponseEntity<PaymentsCommand> placeOrder(PaymentsCommand command, @RequestParam(value="email") String email,
                                                       @RequestParam(value="placeorder", required=false) String placeorder) throws ServerException{
          log.info("Accessing place order method ");

          command = repository.findByEmail(email);
          balance = command.getTransactionAmount();
          if (balance < total) {
              System.out.println("Cannot process payment. Insufficient funds");
              return new ResponseEntity("Cannot process payment. Insufficient funds", HttpStatus.BAD_REQUEST) ;

          }
          double new_balance = balance - total;
          command.setTransactionAmount(new_balance);

          /*
          String msg = "Payment Successful for userId:" + userId + " and total:" + total ;
          rabbitMqSender.send(msg);
          String ms= "Message has been sent Successfully to paymentConfirmation queue";
          System.out.println(ms);
          */
          log.info("Action: " + placeorder);
          repository.save(command);
          return  ResponseEntity.accepted().body(command);

      }



    //this method used to test RabbitMQ using Postman
//    @PostMapping(value = "paymentConfirmation")
//    public String publishUserDetails() {
//        String msg = "Payment Successful for cartId:" + cartId + " and subtotal :" + subtotal ;
//        rabbitMqSender.send(msg);
//        String ms= "Message has been sent Successfully to paymentConfirmation queue";
//        System.out.println(ms);
//        return ms;
//    }


}




