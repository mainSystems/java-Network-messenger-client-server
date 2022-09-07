package ru.geekbrains.java3.lesson5;


import java.util.Map;
import java.util.TreeMap;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    public static Map<String, Long> finish = new TreeMap<>();

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
//            System.out.println("Warning: " + race.getStages().get(i));
        }
    }

    public static Map<String, Long> getFinish() {
        return finish;
    }

    public static void setFinish(String member, Long timeFinish) {
        finish.put(member, timeFinish);
    }
}
