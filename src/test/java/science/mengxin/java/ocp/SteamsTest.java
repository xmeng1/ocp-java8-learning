package science.mengxin.java.ocp;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SteamsTest {

    @Test
    void helloWorld() {
        Integer[] myNums = {1, 2, 3};
        Stream<Integer> myStream = Arrays.stream(myNums);

        long numElements = myStream.count();
        System.out.println("Number of elements in the stream: " + numElements);
        myStream = Arrays.stream(myNums);

        long numElementsMoreThan1 = myStream.filter((i) -> i > 1).count();
        System.out.println("Number of elements > 1 in the stream: " + numElementsMoreThan1);

    }


    @Test
    void streamCreation() {
        // create from collection
        List<Double> tempsInPhoenix = Arrays.asList(123.6, 123.9);

        long x = tempsInPhoenix.stream().filter(t -> t > 100).count();
        System.out.println(x);


    }
}
