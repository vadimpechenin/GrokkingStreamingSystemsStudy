package com.streamwork.threadEx.thread6;

import java.util.concurrent.Phaser;

public class PhaserEx {
    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser();

        phaser.register();
        System.out.println("Phasecount is " + phaser.getPhase());
        testPhaser(phaser);
        testPhaser(phaser);
        testPhaser(phaser);

        Thread.sleep(3000);
        phaser.arriveAndDeregister();
        System.out.println("Phasecount is " + phaser.getPhase());
    }

    private static void testPhaser(final Phaser phaser) {

        phaser.register();

        new Thread(() -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + " arrived");
            phaser.arriveAndAwaitAdvance();
            System.out.println(name + " after passing barrier");
        }).start();
    }
}
