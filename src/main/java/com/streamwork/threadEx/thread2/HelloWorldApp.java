package com.streamwork.threadEx.thread2;

import java.util.concurrent.TimeUnit;

public class HelloWorldApp {
    public static void main(String []args) {
        int randomInteger= (int)(Math.random()*10)+1;
        Runnable task = () -> {
            try {
                if (randomInteger<5) {
                    int secToWait = 1000 * 6;
                    Thread.currentThread().sleep(secToWait);
                }else {
                    System.out.println("Альтернатива");
                    TimeUnit.SECONDS.sleep(6);
                }
                System.out.println("Waked up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
