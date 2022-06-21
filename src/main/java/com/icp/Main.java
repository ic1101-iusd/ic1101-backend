package com.icp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        System.out.println("Start icp-liquidator");
        SpringApplication.run(Main.class);
    }
}
