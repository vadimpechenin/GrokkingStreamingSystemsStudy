package com.streamwork.threadEx.thread5;

import java.util.concurrent.Executor;

public class ExecutorEx {
    public static void main(String []args) throws Exception {
        Runnable task = () -> System.out.println("Task executed");
        Executor executor = (runnable) -> {
            new Thread(runnable).start();
        };
        executor.execute(task);
    }
}
