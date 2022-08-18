package ru.geekbrains.java3.lesson1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Homework1 {
    public static void main(String[] args) {

        Integer[] str0 = {1, 2, 3, 4, 5};
        Double[] str1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        String[] str2 = {"1", "2", "3", "4", "5"};

        //task1
        chArray(str0);
        chArray(str1);
        chArray(str2);

        //task2
        toArrayList(str0);
        toArrayList(str1);
        toArrayList(str2);

        //task3
        //making fruits
        Fruit apple1 = new Apple(FruitsType.Apple);
        Fruit apple2 = new Apple(FruitsType.Apple);
        Fruit apple3 = new Apple(FruitsType.Apple);
        Fruit orange1 = new Orange(FruitsType.Orange);
        Fruit orange2 = new Orange(FruitsType.Orange);

        //making boxes
        Box<Fruit> box1 = new Box<>(apple1);
        Box<Fruit> box2 = new Box<>(orange1);
        Box<Fruit> box3 = new Box<>(apple2);

        //fill the boxes
        box1.addFruits(orange1);
        box1.addFruits(apple2);
        box1.addFruits(apple3);
        box1.getFruits();
        System.out.println(box1.getCount());
        box2.addFruits(orange2);
        box2.getFruits();
        System.out.println(box1.getWeight());
        System.out.println(box2.getWeight());

        //compare
        System.out.println(box1.compare(box2));

        //switch between boxes
        box1.sprinkleFruit(box2);
        box2.getFruits();
    }



    public static <T> void chArray(T[] arr) {
        T tmp = arr[0];
        arr[0] = arr[arr.length - 1];
        arr[arr.length - 1] = tmp;
        System.out.println(Arrays.toString(arr));
    }

    public static <T> void toArrayList(T[] arr) {
        List<T> targetList = new ArrayList<T>(Arrays.asList(arr));
        System.out.println(targetList);
    }



}
