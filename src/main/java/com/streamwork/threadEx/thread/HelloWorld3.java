/*
А ещё Runnable является функциональным интерфейсом начиная с Java 1.8.
Это позволяет писать код задач для потоков ещё красивее:
 */

package com.streamwork.threadEx.thread;

public class HelloWorld3 {
    public static void main(String []args){
        Runnable task = () -> {
            System.out.println("Hello, World!");
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
