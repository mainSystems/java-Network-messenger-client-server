package ru.geekbrains.java3.lesson1;

public class Apple extends Fruit {
    private final FruitsType fruitName;
    private final  float weight = 1.0f;

    public Apple(FruitsType fruitName) {
        this.fruitName = fruitName;
    }

    public FruitsType getFruitName() {
        return fruitName;
    }
    public float getFruitWeight() {
        return weight;
    }
}
