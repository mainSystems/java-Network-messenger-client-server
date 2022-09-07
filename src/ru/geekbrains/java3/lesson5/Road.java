package ru.geekbrains.java3.lesson5;


public class Road extends Stage {

    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);
            System.out.println(c.getName() + " закончил этап: " + description);
            if (description.equals("Дорога 40 метров")) {
                c.setFinish(c.getName(), System.currentTimeMillis() / 1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
