package ru.geekbrains.java3.lesson7;

public class BeforeAfterNotUniqex extends Exception {
    BeforeAfterNotUniqex(String str) {
        super(str +  "- not unique");
    }
}
