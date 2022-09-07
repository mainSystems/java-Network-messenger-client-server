import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.geekbrains.java3.lesson6.Homework6;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeworkTest {
    Homework6 homework6 = new Homework6();
    int[] arrEx = {1, 2, 3, 1, 5, 6, 7, 8};

    int[] arr0 = {8};
    int[] arr0Test = {1, 2, 3, 4, 5, 6, 4, 8};

    int[] arr1 = {2, 3, 1, 5, 6, 7, 8};
    int[] arr1Test = {4, 2, 3, 1, 5, 6, 7, 8};


    int[] arr2 = {1};
    int[] arr2Test = {1, 1, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1};

    int[] arr14 = {1, 1, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1};
    int[] arr24 = {1, 1, 4};
    int[] arr34 = {4, 1};

    //arraySearch4
    @Test
    void test1arraySearch4() {
        Assertions.assertArrayEquals(arr0, homework6.arraySearch4(arr0Test));
    }

    @Test
    void test26arraySearch4() {
        Assertions.assertArrayEquals(arr1, homework6.arraySearch4(arr1Test));
    }

    @Test
    void test3arraySearch4() {
        Assertions.assertArrayEquals(arr2, homework6.arraySearch4(arr2Test));
    }

    @Test
    void exceptionTesting() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> homework6.arraySearch4(arrEx), "Expected doThing() to throw, but it didn't");
        assertTrue(thrown.toString().contains("RuntimeException"));
    }

    //arrayCheck14
    @Test
    void test1arrayCheck14() {
        Assertions.assertTrue(homework6.arrayCheck14(arr14));
    }

    @Test
    void test2arrayCheck14() {
        Assertions.assertTrue(homework6.arrayCheck14(arr24));
    }

    @Test
    void test3arrayCheck14() {
        Assertions.assertTrue(homework6.arrayCheck14(arr34));
    }
}
