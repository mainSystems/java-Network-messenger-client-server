package ru.geekbrains.java3.lesson4;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Homework4 {
    private static final int threadCount = 3;
    private static final int countPrint = 5;
    private static final Object monitor = new Object();
    private static volatile char startChar = 'A';

    public static void main(String[] args) {

        Thread thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (monitor) {
                    threadWork('A', 'B');
                }
            }
        });


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (monitor) {
                    threadWork('B', 'C');
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (monitor) {
                    threadWork('C', 'A');
                }
            }
        });

        threading(thread0, thread1, thread2);



    }

    private static void threading(Thread thread0, Thread thread1, Thread thread2) {
        List<Thread> threads = new ArrayList<>();
        threads.add(thread0);
        threads.add(thread1);
        threads.add(thread2);

        for (int i = 0; i < threadCount; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threads.get(i).start();
        }
    }

    private static void threadWork(char start, char end) {
        for (int i = 0; i < countPrint; i++) {
            while (startChar != start) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(start);
            startChar = end;
            monitor.notifyAll();
        }
    }
}
