/*
Первый — создать своего наследника
Как видим, запуск задачи выполняется в методе run, а запуск потока в методе start.
Не стоит их путать, т.к. если мы запустим метод run напрямую, никакой новый поток не будет запущен.
Именно метод start просит JVM создать новый поток.

Вариант с потомком от Thread плох уже тем, что мы в иерархию классов включаем Thread.
Второй минус — мы начинаем нарушать принцип "Единственной ответственности" SOLID, т.к. наш класс становится одновременно ответственным и за управление потоком и за некоторую задачу, которая должна выполняться в этом потоке.

Как же правильно? Ответ находится в том самом методе run, который мы переопределяем:

 */

package com.streamwork.threadEx.thread;

public class HelloWorld {
    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello, World!");
        }
    }

    public static void main(String []args){
        Thread thread = new MyThread();
        thread.start();
    }

}
