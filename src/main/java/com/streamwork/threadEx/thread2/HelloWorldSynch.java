package com.streamwork.threadEx.thread2;

public class HelloWorldSynch {
    public static void main(String []args){
        Object object = new Object();
        synchronized(object) {
            System.out.println("Hello World");
        }
    }
}
