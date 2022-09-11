package ru.geekbrains.java3.lesson6;


import java.util.Arrays;

public class Homework6 {

    public static void main(String[] args) {

    }

    public int[] arraySearch4(int[] array) throws RuntimeException {
        int seek = 4;
        int index = -1;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == seek) {
                index = i + 1;
            }
        }

        if (index < 0) {
            throw new RuntimeException("there are no: " + seek);
        }

        return Arrays.copyOfRange(array, index, array.length);
    }

    public boolean arrayCheck14(int[] array) {
        for (int seek : array) {
            if (seek != 1 && seek != 4) {
                return false;
            }
        }
        return true;
    }

}
