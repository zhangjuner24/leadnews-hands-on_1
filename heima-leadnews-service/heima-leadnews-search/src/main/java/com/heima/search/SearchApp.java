package com.heima.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class SearchApp {
    public static void main(String[] args) {
        SpringApplication.run(SearchApp.class, args);
    }
}
