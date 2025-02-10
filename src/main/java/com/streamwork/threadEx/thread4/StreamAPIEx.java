package com.streamwork.threadEx.thread4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamAPIEx {
    public static void main(String []args) {
        List<String> array = Arrays.asList("one", "two");
        Stream<String> stringStream = array.stream().map(value -> {
            System.out.println("Executed");
            return value.toUpperCase();
        });
    }
}
