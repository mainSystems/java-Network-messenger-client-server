package ru.geekbrains.java3.lesson7;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Homework7 {
    public static void main(String[] args) throws BeforeAfterNotUniqex, InvocationTargetException, IllegalAccessException {
        Class<Tests> tests = Tests.class;
        Tests test = new Tests();

        Method[] methods = tests.getDeclaredMethods();
        ArrayList<Method> methodBefore = new ArrayList<>();
        ArrayList<Method> methodAfter = new ArrayList<>();

        for (Method m : methods) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                if (methodBefore.isEmpty()) {
                    methodBefore.add(m);
                } else {
                    throw new BeforeAfterNotUniqex("Before suite");
                }
            }

            if (m.isAnnotationPresent(AfterSuite.class)) {
                if (methodAfter.isEmpty()) {
                    methodAfter.add(m);
                } else {
                    throw new BeforeAfterNotUniqex("After suite");
                }
            }

        }

        methodBefore.get(0).invoke(test);

        for (int i = 0; i < 10; i++) {
            for (Method m : methods) {
                if (m.isAnnotationPresent(Test.class)) {
                    if (m.getAnnotation(Test.class).priority() == i) {
                        m.invoke(test);
                    }
                }
            }

        }

        methodAfter.get(0).invoke(test);
    }
}
