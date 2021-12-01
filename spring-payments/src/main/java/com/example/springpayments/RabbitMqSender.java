package com.example.springpayments;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Queue;
@Service
public class RabbitMqSender {
    private RabbitTemplate rabbitTemplate;

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

    public void send(String msg){
        rabbitTemplate.convertAndSend( queue.getName(),msg);

    }

}