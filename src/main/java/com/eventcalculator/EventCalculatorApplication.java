package com.eventcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eventcalculator")
public class EventCalculatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventCalculatorApplication.class, args);
    }
}