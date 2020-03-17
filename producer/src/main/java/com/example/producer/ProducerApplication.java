package com.example.producer;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Log4j2
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Bean
	public Queue myQueue() {
    	return new Queue("my_queue");
	}
    @Bean
    public CommandLineRunner onReady(RabbitTemplate template) {
        return args -> {
            template.convertAndSend("my_queue", "hello world!");
            log.info("Completed send event!!!");
        };

    }
}
