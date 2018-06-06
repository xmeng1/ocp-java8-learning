package science.mengxin.java.ocp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class LambdaDemoTest {

    Dog boi;
    Dog clover;
    Dog zooey;
    @BeforeEach
    void setUp() {
        boi = new Dog("boi", 30, 6);
        clover = new Dog("clover", 35, 12);
        zooey = new Dog("zooey", 45, 8);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void SupplierTest() {
        Logger logger = Logger.getLogger("Status Logger");
        logger.setLevel(Level.SEVERE);

        // fix status
/*        String currentStatus = "Everything's OK";
        logger.log(Level.INFO, currentStatus);*/

        // the status should be changed by different situation
        String host = "www.google.co.uk";
        int port = 80;

        //in case we need to check the status
        //noinspection Duplicates
        Supplier<String> status = ()-> {
            int timeout = 1000;
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), timeout);
                return "up";
            } catch (IOException e) {
                return "down";
            }
        };

        try {
            logger.log(Level.INFO, status);
            throw new Exception();
        } catch (Exception e) {
            logger.log(Level.SEVERE, status);
        }

        // why we make this design?

        // see OCP Chapter 8 Working with suppliers
        /*
         * When we make log for SEVERE log for status, we usually need to make a expensive call to check the status,
         * If we set global as INFO, we need always check status.
         *
         * Use the supplier which is similar with separate method to return the status. But Lambda is more concise.
         * */

    }


    @Test
    void consumerTest() {
        Map<String, String> env = System.getenv();

        BiConsumer<String, String> printEnv = (key, value) -> {
            System.out.println(key + ": " + value);
        };

        printEnv.accept("USER", env.get("USER"));
    }

    @Test
    void forEachTest() {
        List<String> dogNames = Arrays.asList("boi", "clover", "zooey");
        Consumer<String> printName = name -> System.out.println(name);
        dogNames.forEach(printName);

        final String[] username = new String[1];
        Map<String, String> env = System.getenv();
        BiConsumer<String, String> printEnv = (key, value) -> {
            System.out.println(key + ": " + value);
            // Compile error, username must be effectively final
            // if (key.equals("USER"))  username = value;

            //it can be cheated by
            // 1: AtomicReference<String> username;
            // 2: final String[] username = new String[1];
            if (key.equals("USER"))  username[0] = value;
        };
        env.forEach(printEnv);
        assert username[0] == null;
    }

    class User {
        String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    @Test
    void otherWayCheatLambdaSideEffect() {
        Map<String, String> env = System.getenv();

        User user = new User();
        user.setUsername("initial");
        BiConsumer<String, String> findUserName = (key, value) -> {
            if (key.equals("USER")) user.setUsername(value);
        };

        env.forEach(findUserName);
        System.out.println("Username from env: " + user.getUsername());
        assert user.getUsername().equals("initial");
    }


    @Test
    void consumerAndThen(){
        List<Dog> dogs = new ArrayList<>();

        dogs.add(boi);
        dogs.add(clover);
        dogs.add(zooey);

        Consumer<Dog> displayName = d -> System.out.println(d + " ");
        //dogs.forEach(displayName.andThen(d -> d.bark()));
        // Lambda can be change to method reference
        dogs.forEach(displayName.andThen(Dog::bark));

        //lambda inline compile error
        //dogs.forEach((d-> System.out.println(d + " ")).andThen(d->d.bark()));

        //but we can use the name inline
        Consumer<Dog> doBark = Dog::bark;

        dogs.forEach(displayName.andThen(doBark));
    }

    @Test
    void predicateTest() {
        Predicate<Dog> p = d -> d.getAge() > 9;
        System.out.println("Is Boi older than 9? " + p.test(boi));
        System.out.println("Is clover older than 9? " + p.test(clover));

        Predicate<Dog> ageEqual6 = d -> d.getAge() == 6;
        System.out.println("Is Boi Not 6? " + ageEqual6.negate().test(boi));


        Predicate<Dog> name = d -> d.getName().equals("boi");
        Predicate<Dog> nameAndAge = d -> name.and(ageEqual6).test(d);
        System.out.println("---Test name and age of boi---");
        System.out.println("Is Boi name 'boi' and age 6? " + nameAndAge.test(boi));
        boi.setAge(7);
        System.out.println("Is Boi name 'boi' and age 6? " + nameAndAge.test(boi));

    }

    @Test
    void functionTest() {
        Function<Integer, String> answer =  a -> {
            if (a == 42) return "forty-two";
            else return "No answer for you";
        };

        System.out.println(answer.apply(42));
        assert answer.apply(42).equals("forty-two");
        System.out.println(answer.apply(64));
        assert answer.apply(64).equals("No answer for you");
    }

    @Test
    void JdkMapFunctionTest() {
        Map<String, String> aprilWinder = new TreeMap<>();
        aprilWinder.put("April 2017", "Bob");
        aprilWinder.put("April 2016", "Annette");
        aprilWinder.put("April 2015", "Lamar");


        aprilWinder.forEach((k,v) -> System.out.println(k + ": " + v));
        System.out.println("=====");

        aprilWinder.putIfAbsent("April 2013", "John Doe");
        aprilWinder.computeIfAbsent("April 2014", (k) -> "John Doe");
        aprilWinder.computeIfPresent("April 2015", (k,v) -> "new 2015 winner");
        aprilWinder.forEach((k,v) -> System.out.println(k + ": " + v));
        System.out.println("=====");

        aprilWinder.replaceAll((k, v) -> v.toUpperCase());
        aprilWinder.forEach((k,v) -> System.out.println(k + ": " + v));
        System.out.println("=====");
    }

    @Test
    void operatorTest() {
        UnaryOperator<Double> log2 = v -> Math.log(v) / Math.log(2);
        System.out.println(log2.apply(8.0));


    }

    private static void printTreeStatic(String string) {
        System.out.println("Tree name: " + string);
    }

    @Test
    void methodRefTest() {

        List<String> trees = Arrays.asList("fir", "ceder", "pine");
        trees.forEach(System.out::println);
        trees.forEach(LambdaDemoTest::printTreeStatic);
    }
}