package com.example.producer;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
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
        final Queue fanoutQueue2 = new Queue("fanout_queue_2", false, false, false);
        return new Declarables(fanoutQueue1, fanoutQueue2, fanoutExchange, BindingBuilder.bind(fanoutQueue1).to(fanoutExchange), BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
    }

    @Bean
    public ApplicationRunner sendMsgToFanout(RabbitTemplate template, FanoutExchange fanoutExchange) {
        return args -> {
            template.convertAndSend(fanoutExchange.getName(), "notify", "say hi to fanout!!!");
            log.info("Send message to fanout {} completed", fanoutExchange.getName());
        };
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange");
    }

    @Bean
    public Declarables topicBindings(TopicExchange topicExchange) {
        final Queue topicQueue1 = new Queue("topic_queue_1", false, false, false);
        final Queue topicQueue2 = new Queue("topic_queue_2", false, false, false);

        return new Declarables(topicQueue1, topicQueue2, topicExchange,
                BindingBuilder.bind(topicQueue1).to(topicExchange).with("*.notify.*"),
                BindingBuilder.bind(topicQueue2).to(topicExchange).with("*.error.*"));

    }

    @Bean
    public ApplicationRunner sendMsgToTopic(RabbitTemplate template, TopicExchange topicExchange) {
        return args -> {
            template.convertAndSend(topicExchange.getName(), "topic.notify.exchange", "say notify to topic!!!");
            template.convertAndSend(topicExchange.getName(), "topic.error.exchange", "say error to topic!!!");
            log.info("Send message to topic {} completed", topicExchange.getName());
        };
    }
}
