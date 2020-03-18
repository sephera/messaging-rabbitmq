package com.example.producer;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
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
    public Queue directQueue() {
        return new Queue("direct_queue");
    }

    @Bean
    public ApplicationRunner sendMsgToDirectQueue(RabbitTemplate template, Queue directQueue) {
        return args -> {
            template.convertAndSend(directQueue.getActualName(), "hello world!");
            log.info("Send message to queue {} completed", directQueue.getActualName());
        };

    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout.exchange", false, false);

    }

    @Bean
    public Declarables fanoutBindings(FanoutExchange fanoutExchange) {
        final Queue fanoutQueue1 = new Queue("fanout_queue_1", false, false, false);
        final Queue fanoutQueue2 = new Queue("fanout_queue_2", false, false, false );
        return new Declarables(fanoutQueue1, fanoutQueue2, fanoutExchange, BindingBuilder.bind(fanoutQueue1).to(fanoutExchange), BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
    }

    @Bean
    public ApplicationRunner sendMsgToFanout(RabbitTemplate template, FanoutExchange fanoutExchange) {
        return args -> {
            template.convertAndSend(fanoutExchange.getName(), "notify", "say hi to fanout!!!");
            log.info("Send message to fanout {} completed", fanoutExchange.getName());
        };
    }
}
