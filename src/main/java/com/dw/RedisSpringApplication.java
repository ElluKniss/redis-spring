package com.dw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisSpringApplication.class, args);
    }

    @Bean
    public Object cc(){
        return new Object();
    }

}
