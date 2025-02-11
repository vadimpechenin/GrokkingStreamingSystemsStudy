package com.streamwork.threadEx.thread5;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class ForkJoinPoolExample {
    public static void main(String[] args) {
        // Создаём ForkJoinPool с указанным уровнем параллелизма
        int numberOfThreads = 1;

        //1. Выполнение в 1 поток
        ForkJoinPoolExample.newCachedThreadPoolExample(numberOfThreads);
        //2. Выполнение в 2 потока
        numberOfThreads = 2;
        ForkJoinPoolExample.newCachedThreadPoolExample(numberOfThreads);
        //3. Выполнение в 4 потока
        numberOfThreads = 4;
        ForkJoinPoolExample.newCachedThreadPoolExample(numberOfThreads);
    }

    public static void newCachedThreadPoolExample(int numberOfThreads){
        System.out.println("Работа с : " + Integer.toString(numberOfThreads));
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        // Подкладываем задачи для выполнения в пул
        long start = System.currentTimeMillis();
        ForkJoinTask<Integer> task = forkJoinPool.submit(() -> {
            int result = 0;
            for (int i = 1; i <= 10; i++) {
                result += i;
            }
            return result;
        });
        // Ждём завершения задачи
        try {
            System.out.println("Result: " + task.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Закрываем пул
        forkJoinPool.shutdown();
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("Прошло времени, мс: " + elapsed);
    }
}
