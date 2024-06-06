package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {
    private static final int QUEUE_CAPACITY = 10;
    private static final int PRODUCER_COUNT = 2;
    private static final int CONSUMER_COUNT = 2;
    private static final int PRODUCE_COUNT = 100;

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        Thread[] producers = new Thread[PRODUCER_COUNT];
        Thread[] consumers = new Thread[CONSUMER_COUNT];

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            producers[i] = new Thread(new Producer(queue));
            producers[i].start();
        }

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumers[i] = new Thread(new Consumer(queue));
            consumers[i].start();
        }

        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Thread consumer : consumers) {
            consumer.interrupt();
        }
    }

    static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < PRODUCE_COUNT; i++) {
                try {
                    int item = (int) (Math.random() * 100);
                    queue.put(item);
                    System.out.println("El Productor hizo el: " + item);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int item = queue.take();
                    System.out.println("El Consumidor consumió el: " + item);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
