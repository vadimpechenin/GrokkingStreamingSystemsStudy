package com.streamwork.threadEx.thread5;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceEx {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
        Callable<String> task = () -> {
            System.out.println(Thread.currentThread().getName());
            return Thread.currentThread().getName();
        };
        scheduledExecutorService.schedule(task, 5, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
        //Пример 2
        scheduledExecutorService = Executors.newScheduledThreadPool(4);
        Runnable task2 = () -> {
            System.out.println(Thread.currentThread().getName());
        };
        scheduledExecutorService.scheduleAtFixedRate(task2, 1, 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
    }
}
