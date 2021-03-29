package com.techgoons.rabbitmq.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class RabbitMqListener {

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "#{queue.getName()}")  // Dynamically reading the queue name using SpEL from the "queue" object.
    public void receive(final String message) {
        log.info("Listening messages from the queue!!");
        log.info("Received the following message from the queue= " + message);
        log.info("Message received successfully from the queue.");
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
