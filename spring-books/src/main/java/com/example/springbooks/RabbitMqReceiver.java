package com.example.springbooks;
/*
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.amqp.core.Queue;

//@RabbitListener(queues = "paymentConfirmation")
@Component
public class RabbitMqReceiver {
    private RabbitTemplate rabbitTemplate;

    private RestTemplate restTemplate;
    

    
    @Autowired
    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    

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
    
    
    

    @RabbitListener(queues = "paymentConfirmation")
    public void receive(String message) throws Exception {
        
        System.out.println(" Rabbit Received: " + message);

        //ResponseEntity<String> response = restTemplate.getForEntity("/rabbit?email=" + message, String.class, message);
        //System.out.println(controller.clearCart(message));
    }

}
*/