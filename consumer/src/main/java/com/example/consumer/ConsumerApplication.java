package com.example.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @RabbitListener(queues = "direct_queue")
    public void onDirectQueue(String msg) {
    	log.info("Receive: {}", msg);
    }

    @RabbitListener(queues = "fanout_queue_1")
    public void onFanoutQueue1(String msg) {
        log.info("Receive: {}", msg);
    }


    @RabbitListener(queues = "fanout_queue_2")
    public void onFanoutQueue2(String msg) {
        log.info("Receive: {}", msg);
    }

}
