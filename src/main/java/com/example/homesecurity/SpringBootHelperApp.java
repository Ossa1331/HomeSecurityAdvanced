package com.example.homesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.homesecurity"})
public class SpringBootHelperApp {

    public static void main(String[] args){
        SpringApplication.run(SpringBootHelperApp.class, args);
    }
}
