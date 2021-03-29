package com.techgoons.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class MessageController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    Binding binding;

    @GetMapping(value = "/send/{msg}")@ResponseStatus(code = HttpStatus.OK)
    public String send(@PathVariable("msg") final String message) {
        log.info("Sending message to the queue.");
        rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
        log.info("Message sent successfully to the queue, sending back the response to the user.");
        return "Message sent successfully to the queue.";
    }
}
