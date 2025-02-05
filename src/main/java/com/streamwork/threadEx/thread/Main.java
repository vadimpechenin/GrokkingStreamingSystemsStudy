/*
Как сказано в JavaDoc класса Thread:

When a Java Virtual Machine starts up, there is usually a single non-daemon thread

Существует 2 типа потоков: демоны и не демоны. Демон-потоки — это фоновые потоки (служебные), выполняющие какую-то работу в фоне.
Такой интересный термин — это отсылка к "демону Максвелла", о чём подробнее можно прочитать в википедии в статье про "демонов".

Как сказано в документации, JVM продолжает выполнение программы (процесса), до тех пор, пока:
    Не вызван метод Runtime.exit
    Все НЕ демон-потоки завершили свою работу (как без ошибок, так и с выбрасыванием исключений)

Отсюда и важная деталь: демон-потоки могут быть завершены на любой выполняемой команде.
Поэтому целостность данных в них не гарантируется. Поэтому, демон потоки подходят для каких-то служебных задач.
Например, в Java есть поток, который отвечает за обработку методов finalize или потоки, относящиеся к сборщику мусора (Garbage Collector, GC).

Каждый поток входит в какую-то группу (ThreadGroup).

А группы могут входит друг в друга, образовывая некоторую иерархию или структуру.

 */

package com.streamwork.threadEx.thread;

public class Main {

    public static void main(String []args){
        Thread currentThread = Thread.currentThread();
        ThreadGroup threadGroup = currentThread.getThreadGroup();
        System.out.println("Thread: " + currentThread.getName());
        System.out.println("Thread Group: " + threadGroup.getName());
        System.out.println("Parent Group: " + threadGroup.getParent().getName());

        //Группы позволяют упорядочить управление потоками и вести их учёт.
        //
        //Помимо групп, у потоков есть свой обработчик исключений. Взглянем на пример:

        currentThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Возникла ошибка: " + e.getMessage());
            }
        });
        System.out.println(2/0);

    }
}
