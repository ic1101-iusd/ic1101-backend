package com.icp;

import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start icp-liquidator");
        Timer t = new Timer();
        LiquidatorTask taskLiquidate = new LiquidatorTask();
        t.scheduleAtFixedRate(taskLiquidate, 0, 10000);
    }

}
