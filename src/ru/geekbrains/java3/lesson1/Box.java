package ru.geekbrains.java3.lesson1;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private final List<T> box = new ArrayList<>();
    private final T mainFruit;

    public Box(T fruits) {
        box.add(fruits);
        mainFruit=fruits;
    }

    public void getFruits() {
        for (T fruits : box) {
            System.out.println(fruits.getClass().getSimpleName());
        }
    }

    public void addFruits(T fruits) {
        if (mainFruit.getFruitName() == fruits.getFruitName()) {
            box.add(fruits);
        } else {
            System.out.println("Same fruit in box only!");
        }
    }

    public int getCount() {
        return (box.size());
    }

    public float getWeight() {
                return (getCount() * mainFruit.getFruitWeight());
    }

    public boolean compare(Box<?> box) {
        return this.getWeight() == box.getWeight();
    }

    public void sprinkleFruit (Box<T> box) {
        for (T fruit : this.box) {
            box.addFruits(fruit);
        }
        this.box.clear();
    }
}
