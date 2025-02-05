/*
Вариант с потомком от Thread плох уже тем, что мы в иерархию классов включаем Thread.
Второй минус — мы начинаем нарушать принцип "Единственной ответственности" SOLID, т.к. наш класс становится одновременно
 ответственным и за управление потоком и за некоторую задачу, которая должна выполняться в этом потоке.

Как же правильно? Ответ находится в том самом методе run, который мы переопределяем:
public void run() {
	if (target != null) {
		target.run();
	}
}
Здесь target — это некоторый java.lang.Runnable, который мы можем передать для Thread при создании экземпляра класса.
Поэтому, мы можем сделать так:
 */

package com.streamwork.threadEx.thread;

public class HelloWorld2 {
    public static void main(String []args){
        Runnable task = new Runnable() {
            public void run() {
                System.out.println("Hello, World!");
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
