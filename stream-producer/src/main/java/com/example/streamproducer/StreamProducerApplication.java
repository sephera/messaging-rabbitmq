package com.example.streamproducer;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;

@Log4j2
@EnableBinding(Source.class)
@SpringBootApplication
public class StreamProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamProducerApplication.class, args);
    }

    @Bean
    public ApplicationRunner onReady(Source source) {
        return args -> {
            source.output().send(MessageBuilder.withPayload("John snow").build());
            log.info("Send event completed!");
        };
    }

}