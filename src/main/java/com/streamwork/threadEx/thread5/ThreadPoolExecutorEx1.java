package com.streamwork.threadEx.thread5;

import java.util.concurrent.*;

public class ThreadPoolExecutorEx1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int threadBound = 2;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, threadBound,
                0L, TimeUnit.SECONDS, new SynchronousQueue<>());
        Callable<String> task = () -> {
            Thread.sleep(1000);
            return Thread.currentThread().getName();
        };
        for (int i = 0; i < threadBound + 1; i++) {
            System.out.println("Задача №" + Integer.toString(i+1));
            threadPoolExecutor.submit(task);
        }
        threadPoolExecutor.shutdown();
    }
}
