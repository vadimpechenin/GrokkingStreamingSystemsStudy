package com.streamwork.threadEx.thread2;

import java.util.concurrent.TimeUnit;

public class HelloWorldAppInterrupt {
    public static void main(String []args) {
        interrupt1();
        interrupt2();
    }

    public static void interrupt1(){
        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        thread.interrupt();
    }

    public static void interrupt2(){
        Runnable task = () -> {
            while(!Thread.currentThread().isInterrupted()) {
                System.out.println("Действие");
            }
            System.out.println("Finished");
        };
        Thread thread = new Thread(task);
        thread.start();
        thread.interrupt();
    }
}
