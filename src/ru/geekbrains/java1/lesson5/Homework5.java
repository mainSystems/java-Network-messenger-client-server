package ru.geekbrains.java1.lesson5;

import java.util.Random;

public class Homework5 {
    public static final int COUNT_EMPLOYEE = 5;
    public static final int SEARCH_AGE = 40;
    public static final int MIN_AGE = 16;
    public static final int MAX_AGE = 99;


    public static void main(String[] args) {
        Random randomAge = new Random();
        int diff = MAX_AGE - MIN_AGE;

        Staff[] staffArray = new Staff[COUNT_EMPLOYEE];

        Staff staff1 = new Staff("Dn", "Engeneer", "neverfaten@gamial.com", 8888888, 8888888, 88);
        staff1.printInfo();

        for (int i = 0; i < staffArray.length; i++) {
            staffArray[i] = new Staff("ru/geekbrains/Test" + i, "Test Position", "test@company.com", i, i, randomAge.nextInt(diff + 1));
        }

        for (Staff employee : staffArray) {
            if (employee.getAge() > SEARCH_AGE) {
                employee.printInfo();
            }
        }
    }

}
