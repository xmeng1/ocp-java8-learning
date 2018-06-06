package science.mengxin.java.ocp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

class LambdaDemoTest {

    @BeforeEach
    void setUp() {
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
        Dog boi = new Dog("boi", 30, 6);
        Dog clover = new Dog("clover", 35, 12);
        Dog zooey = new Dog("zooey", 45, 8);
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
}