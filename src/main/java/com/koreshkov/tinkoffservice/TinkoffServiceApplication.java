package com.koreshkov.tinkoffservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TinkoffServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffServiceApplication.class, args);
    }

}
