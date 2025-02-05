package com.streamwork.threadEx.thread2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockSupport2 {
    public static void main(String []args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Runnable task = () -> {
            lock.lock();
            System.out.println("Thread");
            lock.unlock();
        };
        lock.lock();

        Thread th = new Thread(task);
        th.start();
        System.out.println("main");
        Thread.currentThread().sleep(2000);
        lock.unlock();
    }
}
