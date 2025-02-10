package com.streamwork.threadEx.thread4;


import java.util.concurrent.CompletableFuture;

public class CompletableFutureEx {
    public static void main(String []args) throws Exception {

        CompletableFuture<String> completed;
        completed = CompletableFuture.completedFuture("Просто значение");

        CompletableFuture<Void> voidCompletableFuture;
        voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("run " + Thread.currentThread().getName());
        });

        CompletableFuture<String> supplier;
        supplier = CompletableFuture.supplyAsync(() -> {
            System.out.println("supply " + Thread.currentThread().getName());
            return "Значение";
        });
    }
}
