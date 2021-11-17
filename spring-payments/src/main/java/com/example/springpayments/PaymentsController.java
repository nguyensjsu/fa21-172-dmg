package com.example.springpayments;

import com.example.springcybersource.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j
@Controller
@RequestMapping("/")
public class PaymentsController { 
    
    @Value("${cybersource.apihost}") String apiHost; 
    @Value("${cybersource.merchantkeyid}") String merchantKeyId; 
    @Value("${cybersource.merchantsecretkey}") String merchantSecretKey; 
    @Value("${cybersource.merchantid}") String merchantID; 

    private CyberSourceAPI api = new CyberSourceAPI();

    private static Map<String, String> months = new HashMap<String, String>();

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
    private static Map<String, String> states = new HashMap<String, String>();
    static {
        states.put("AL", "Alabama");
        states.put("AK", "Alaska");
        states.put("AB", "Alberta");
        states.put("AZ", "Arizona");
        states.put("AR", "Arkansas");
        states.put("BC", "British Columbia");
        states.put("CA", "California");
        states.put("CO", "Colorado");
        states.put("CT", "Connecticut");
        states.put("DE", "Delaware");
        states.put("DC", "District Of Columbia");
        states.put("FL", "Florida");
        states.put("GA", "Georgia");
        states.put("GU", "Guam");
        states.put("HI", "Hawaii");
        states.put("ID", "Idaho");
        states.put("IL", "Illinois");
        states.put("IN", "Indiana");
        states.put("IA", "Iowa");
        states.put("KS", "Kansas");
        states.put("KY", "Kentucky");
        states.put("LA", "Louisiana");
        states.put("ME", "Maine");
        states.put("MB", "Manitoba");
        states.put("MD", "Maryland");
        states.put("MA", "Massachusetts");
        states.put("MI", "Michigan");
        states.put("MN", "Minnesota");
        states.put("MS", "Mississippi");
        states.put("MO", "Missouri");
        states.put("MT", "Montana");
        states.put("NE", "Nebraska");
        states.put("NV", "Nevada");
        states.put("NB", "New Brunswick");
        states.put("NH", "New Hampshire");
        states.put("NJ", "New Jersey");
        states.put("NM", "New Mexico");
        states.put("NY", "New York");
        states.put("NF", "Newfoundland");
        states.put("NC", "North Carolina");
        states.put("ND", "North Dakota");
        states.put("NT", "Northwest Territories");
        states.put("NS", "Nova Scotia");
        states.put("NU", "Nunavut");
        states.put("OH", "Ohio");
        states.put("OK", "Oklahoma");
        states.put("ON", "Ontario");
        states.put("OR", "Oregon");
        states.put("PA", "Pennsylvania");
        states.put("PE", "Prince Edward Island");
        states.put("PR", "Puerto Rico");
        states.put("QC", "Quebec");
        states.put("RI", "Rhode Island");
        states.put("SK", "Saskatchewan");
        states.put("SC", "South Carolina");
        states.put("SD", "South Dakota");
        states.put("TN", "Tennessee");
        states.put("TX", "Texas");
        states.put("UT", "Utah");
        states.put("VT", "Vermont");
        states.put("VI", "Virgin Islands");
        states.put("VA", "Virginia");
        states.put("WA", "Washington");
        states.put("WV", "West Virginia");
        states.put("WI", "Wisconsin");
        states.put("WY", "Wyoming");
        states.put("YT", "Yukon Territory");
    }

    @Autowired 
    private PaymentsCommandRepository repository; 

    @Getter
    @Setter
    class Message {
        private String msg;
        public Message(String m) { msg = m; }
    }

    class ErrorMessages {
        private ArrayList<Message> messages = new ArrayList<Message>();
        public void add( String msg ) { messages.add(new Message(msg)); }
        public ArrayList<Message> getMessages() { return messages; }
        public void print() {
            for ( Message m : messages) {
                System.out.println(m.msg );
            }
        }
    }

    @GetMapping
    public String getHome( @ModelAttribute("command") PaymentsCommand command,
                            Model model) {
       return "home";
    }
   @GetMapping("/register")
   public String getRegister( @ModelAttribute("command") PaymentsCommand command,
                            Model model) {

        return "register";
   }

   @GetMapping("/catalog")
   public String getCatalog( @ModelAttribute("command") PaymentsCommand command,
                           Model model) {

       return "catalog";
   }

    @GetMapping("/passwordreset")
    public String getPasswordReset( @ModelAttribute("command") PaymentsCommand command,
                              Model model) {

        return "passwordreset";
    }

    @GetMapping("/refund")
    public String getRefund( @ModelAttribute("command") PaymentsCommand command,
                              Model model) {
                                  
        System.out.println("Accessing refund page");
        return "refund";
    }

    @GetMapping("/creditcards")
    public String getCreditcards( @ModelAttribute("command") PaymentsCommand command,
                              Model model) {

        return "creditcards";
    }


    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") PaymentsCommand command,  
                            @RequestParam(value="action", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) {
    
        log.info( "Action: " + action ) ;
        log.info( "Command: " + command ) ;

        CyberSourceAPI.setHost( apiHost );
        CyberSourceAPI.setKey( merchantKeyId );
        CyberSourceAPI.setSecret( merchantSecretKey) ;
        CyberSourceAPI.setMerchant( merchantID );

        CyberSourceAPI.debugConfig(); 


        ErrorMessages msgs = new ErrorMessages() ;

        boolean hasErrors = false;
        if (command.getFirstname().equals(""))      { hasErrors = true; msgs.add("First Name Required."); }
        if (command.getLastname().equals(""))       { hasErrors = true; msgs.add("Last Name Required."); }
        if (command.getAddress().equals(""))        { hasErrors = true; msgs.add("Address Required."); }
        if (command.getCity().equals(""))           { hasErrors = true; msgs.add("City Required."); }
        if (command.getState().equals(""))          { hasErrors = true; msgs.add("State Required."); }
        if (command.getZip().equals(""))            { hasErrors = true; msgs.add("Zip Required."); }
        if (command.getPhone().equals(""))          { hasErrors = true; msgs.add("Phone Required."); }
        if (command.getCardnum().equals(""))        { hasErrors = true; msgs.add("Credit Card Number Required."); }
        if (command.getCardexpmon().equals(""))     { hasErrors = true; msgs.add("Credit Card Expiration Month Required."); }
        if (command.getCardexpyear().equals(""))    { hasErrors = true; msgs.add("Credit Card Expiration Year Required."); }
        if (command.getCardcvv().equals(""))        { hasErrors = true; msgs.add("Credit Card CVV Required."); }
        if (command.getEmail().equals(""))          { hasErrors = true; msgs.add("Email Address Required."); }

        // state should be two-letter abbrev
        if (!states.containsKey(command.getState())) { hasErrors = true; msgs.add("Invalid State."); }

        String regex = "\\d{5}";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(command.getZip()).matches()) { hasErrors = true; msgs.add("Invalid ZIP Code.");  }

        regex = "\\(\\d{3}\\)\\s\\d{3}-\\d{4}";
        pattern = Pattern.compile(regex);
        if (!pattern.matcher(command.getPhone()).matches()) { hasErrors = true; msgs.add("Invalid Phone Number.");  }

        regex = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
        pattern = Pattern.compile(regex);
        if (!pattern.matcher(command.getCardnum()).matches()) {hasErrors = true; msgs.add("Invalid Credit Card Number."); }
        
        if (!months.containsKey(command.getCardexpmon())) {hasErrors = true; msgs.add("Invalid Credit Card Expiration Month."); }

        regex = "^20(1[0-9]|[2-9][0-9])$";
        pattern = Pattern.compile(regex);
        if (!pattern.matcher(command.getCardexpyear()).matches()) {hasErrors = true; msgs.add("Invalid Credit Card Expiration Year."); }

        regex = "\\d{3}";
        pattern = Pattern.compile(regex);
        if (!pattern.matcher(command.getCardcvv()).matches()) {hasErrors = true; msgs.add("Invalid Credit Card CVV."); }
 
        if (hasErrors) {
            msgs.print();
            model.addAttribute("messages", msgs.getMessages());
            return "creditcards"; 
        }

        int min = 1239871;
        int max = 9999999;
        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        String order_num = String.valueOf(random_int);
        AuthRequest auth = new AuthRequest();
        auth.reference = order_num;
        auth.billToFirstName = command.getFirstname();
        auth.billToLastName = command.getLastname();
        auth.billToAddress = command.getAddress();
        auth.billToCity = command.getCity();
        auth.billToState = command.getState();
        auth.billToZipCode = command.getZip();
        auth.billToPhone = command.getPhone();
        auth.billToEmail = command.getEmail();
        auth.transactionAmount = "30.00";
        auth.transactionCurrency = "USD";
        auth.cardNumber = command.getCardnum();
        auth.cardExpMonth = months.get(command.getCardexpmon());
        auth.cardExpYear = command.getCardexpyear();
        auth.cardCVV = command.getCardcvv();
        auth.cardType = CyberSourceAPI.getCardType( auth.cardNumber );
        if (auth.cardType.equals("ERROR")) {
            System.out.println( "Unsupported Credit Card Type.");
            model.addAttribute("message", "Unsupported Credit Card Type.");
            return "creditcards";
        }

        boolean authValid = true;
        AuthResponse authResponse = new AuthResponse() ;
        System.out.println("\n\nAuth Request: " + auth.toJson());
        authResponse = api.authorize(auth);
        System.out.println("\n\nAuth Response: " + authResponse.toJson());
        
        if ( !authResponse.status.equals("AUTHORIZED")) {
            authValid = false;
            System.out.println(authResponse.message);
            model.addAttribute("message", authResponse.message);
            return "creditcards";
        }

        boolean captureValid = true;
        CaptureRequest capture = new CaptureRequest();
        CaptureResponse captureResponse = new CaptureResponse();
        if ( authValid ) {
            capture.reference = order_num;
            capture.paymentId = authResponse.id;
            capture.transactionAmount = "30.00";
            capture.transactionCurrency = "USD";
            System.out.println("\n\nCapture Request: " + capture.toJson());
            captureResponse = api.capture(capture);
            System.out.println("\\nCapture Response: " + captureResponse.toJson());
            if ( !captureResponse.status.equals("PENDING")) {
                captureValid = false;
                System.out.println( captureResponse.message );
                model.addAttribute("message", captureResponse.message);
                return "creditcards";
            }
        }

        if ( authValid && captureValid ) {
            command.setOrderNumber(order_num);
            command.setTransactionAmount("30.00");
            command.setTransactionCurrency("USD");
            command.setAuthID( authResponse.id);
            command.setAuthStatus( authResponse.status);
            command.setCaptureId( captureResponse.id );
            command.setCaptureStatus( captureResponse.status );

            repository.save( command );

            System.out.println( "Thank you for your Payment! Your Order Number is: " + order_num);
            model.addAttribute("message", "Thank you for your Payment! Your Order Number is: " + order_num);
        }
        

        /* Render View */
        //model.addAttribute( "message", "Thank You for Your Payment!" ) ;
     

        return "creditcards";
    }

}