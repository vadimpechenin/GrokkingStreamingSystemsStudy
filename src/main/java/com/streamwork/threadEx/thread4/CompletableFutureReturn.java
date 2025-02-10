package com.streamwork.threadEx.thread4;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureReturn {
    public static void main(String []args) throws Exception {
        CompletableFuture.completedFuture(2L)
                .thenCompose((val) -> CompletableFuture.completedFuture(val + 2))
                .thenAccept(result -> System.out.println(result));
    }
}
