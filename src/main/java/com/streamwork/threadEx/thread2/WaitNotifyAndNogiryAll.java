package com.streamwork.threadEx.thread2;

public class WaitNotifyAndNogiryAll {
    public static void main(String []args) throws InterruptedException {
        Object lock = new Object();

        Runnable task = () -> {
            synchronized(lock) {
                try {
                    lock.wait();
                } catch(InterruptedException e) {
                    System.out.println("interrupted");
                }
            }

            System.out.println("thread");
        };
        Thread taskThread = new Thread(task);
        taskThread.start();

        Thread.currentThread().sleep(3000);
        System.out.println("main");
        synchronized(lock) {
            lock.notify();
        }
    }
}
