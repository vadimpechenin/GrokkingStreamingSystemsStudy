package com.streamwork.threadEx.thread6;

import java.util.concurrent.SynchronousQueue;

public class ExchangerEx2 {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        Runnable task = () -> {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(task).start();
        queue.put("Message");
    }
}
