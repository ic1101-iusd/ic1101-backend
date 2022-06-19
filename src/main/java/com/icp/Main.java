package com.icp;

import java.util.Timer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Start icp-liquidator");
        Timer t = new Timer();
        LiquidatorTask taskLiquidate = new LiquidatorTask();
        t.scheduleAtFixedRate(taskLiquidate, 0, 10000);
        SpringApplication.run(Main.class);
    }

}
