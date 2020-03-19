package com.example.streamprocessor;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding({Processor.class})
@SpringBootApplication
public class StreamProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamProcessorApplication.class, args);
    }


    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public Person handle(String name) {
        System.out.println("Received: " + name);
        return new Person(name);
    }
}



@Data
@AllArgsConstructor
class Person {
    private String name;
}