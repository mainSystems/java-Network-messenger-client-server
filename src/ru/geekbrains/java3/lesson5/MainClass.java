package ru.geekbrains.java3.lesson5;


import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static CountDownLatch start = new CountDownLatch(CARS_COUNT);


    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];


        for (int i = 0; i < cars.length; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cars[finalI] = new Car(race, 20 + (int) (Math.random() * 10));
                    start.countDown();
                }
            }).start();
        }

        try {
            start.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        for (int i = 0; i < cars.length; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cars[finalI].run();

                    if (Thread.activeCount() == 3) {

                        Map.Entry<String, Long> min = (Map.Entry<String, Long>) Collections.min(Car.getFinish().entrySet(), new Comparator<Map.Entry<String, Long>>() {
                            public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
                                return entry1.getValue().compareTo(entry2.getValue());
                            }
                        });
                        System.out.printf("%s: -WIN\nВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!", min.getKey());
                    }
                }
            }).start();

        }
    }
}