package science.mengxin.java.ocp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class SteamsTest {

    List<Reading> readings;


    @BeforeEach
    void setUp() {
        readings = Arrays.asList(
                new Reading(2017, 1, 1, 405.91),
                new Reading(2017, 1, 8, 405.98),
                new Reading(2017, 1, 15, 406.14),
                new Reading(2017, 1, 22, 406.48),
                new Reading(2017, 1, 29, 406.20),
                new Reading(2017, 2, 5, 407.12),
                new Reading(2017, 2, 12, 406.03)
        );
    }

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


    @Test
    void mapFilterReduceTest() {
        OptionalDouble optionalDouble = readings.stream().mapToDouble(r -> r.value)
                .filter(v -> v >= 406.0 && v < 407.00).average();

        if (optionalDouble.isPresent()) {
            System.out.println(optionalDouble.getAsDouble());
        }

        Optional<Double> sum = readings.stream().map(r -> r.value).reduce((v1, v2) -> v1 + v2);

        if (sum.isPresent()) {
            System.out.println("sum is: " + sum);
        }

        // reduce will do lambda for all item in steam and put last return as first input
        Optional<Double> sum2 =  readings.stream().map(r -> r.value).reduce((v1, v2) -> {
            System.out.println("v1:" + v1);
            System.out.println("v2:" + v2);
            return 1.000;
        });

        if (sum.isPresent()) {
            System.out.println("sum is: " + sum2);
        }

    }
}
