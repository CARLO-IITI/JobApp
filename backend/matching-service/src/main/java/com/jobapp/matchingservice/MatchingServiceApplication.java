package com.jobapp.matchingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jobapp.matchingservice", "com.jobapp.common"})
@EnableAsync
public class MatchingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchingServiceApplication.class, args);
    }
}

