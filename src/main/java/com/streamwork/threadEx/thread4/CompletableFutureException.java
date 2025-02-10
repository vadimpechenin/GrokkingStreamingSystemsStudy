package com.streamwork.threadEx.thread4;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureException {
    public static void main(String []args) throws Exception {
        CompletableFuture.completedFuture(2L)
                .thenApply((a) -> {
                    throw new IllegalStateException("error");
                }).thenApply((a) -> 3L)

                .thenAccept(val -> System.out.println(val));
    }
}
