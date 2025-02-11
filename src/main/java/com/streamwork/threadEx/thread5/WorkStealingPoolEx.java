package com.streamwork.threadEx.thread5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkStealingPoolEx {
    public static void main(String[] args) {
        //1. newCachedThreadPool - выполнение в 5 потоков
        WorkStealingPoolEx.newCachedThreadPoolExample();
        //2. newWorkStealingPool - выполнение менее чем в 5 потоков (в 2) - запустить в режиме отладчика, иначе не видно почему то
        WorkStealingPoolEx.newWorkStealingPool();

    }

    public static void newCachedThreadPoolExample(){
        Object lock = new Object();

        Callable<String> task = () -> {
            System.out.println(Thread.currentThread().getName());
            lock.wait(2000);
            System.out.println("Finished");
            return "result";
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.submit(task);
        }
        executorService.shutdown();
    }

    public static void newWorkStealingPool(){
        Object lock = new Object();

        Callable<String> task = () -> {
            System.out.println(Thread.currentThread().getName());
            lock.wait(2000);
            System.out.println("Finished");
            return "result";
        };

        ExecutorService executorService = Executors.newWorkStealingPool();
        for (int i = 0; i < 5; i++) {
            executorService.submit(task);
        }
        executorService.shutdown();
    }
}
