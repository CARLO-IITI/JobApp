package com.jobapp.resumeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jobapp.resumeservice", "com.jobapp.common"})
public class ResumeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResumeServiceApplication.class, args);
    }
}

