package com.streamwork.threadEx.thread4;


import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ServiceWithCompletableFuture {

    public static class NewService {
        public static String getMessage() {
            try {
                Thread.currentThread().sleep(3000);
                return "Message";
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    public static void main(String []args) throws Exception {
    Supplier newsSupplier = () -> NewService.getMessage();

    CompletableFuture<String> reader = CompletableFuture.supplyAsync(newsSupplier);
    CompletableFuture.completedFuture("!!")
            .thenCombine(reader, (a, b) ->b +a)
            .thenAccept(result ->System.out.println(result))
            .get();
    }
}
