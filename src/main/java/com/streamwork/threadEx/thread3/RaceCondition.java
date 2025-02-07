package com.streamwork.threadEx.thread3;

public class RaceCondition {
    public static int value = 0;
    public static int threadNumber = 1;

    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.format("Начался поток %d!%n",
                    threadNumber++);
            for (int i = 0; i < 10000; i++) {
                int oldValue = value;
                int newValue = ++value;
                if (oldValue + 1 != newValue) {
                    throw new IllegalStateException(oldValue + " + 1 = " + newValue);
                }
            }
        };
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }
}
