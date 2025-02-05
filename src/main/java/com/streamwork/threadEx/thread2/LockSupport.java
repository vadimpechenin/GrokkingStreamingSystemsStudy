package com.streamwork.threadEx.thread2;

import java.util.concurrent.Semaphore;

public class LockSupport {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(0);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("Hello, World!");
    }

}
