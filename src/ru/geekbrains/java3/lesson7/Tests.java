package ru.geekbrains.java3.lesson7;


public class Tests {
    @BeforeSuite
    public void BeforeSuit() {
        System.out.println("Before all");
    }

    @Test(priority=3)
    public void test0() {
        System.out.println("test0");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("test1");
    }

    @Test(priority = 2)
    public void test2() {
        System.out.println("test2");
    }

    @AfterSuite
    public void AfterSuit() {
        System.out.println("After all");
    }
}
