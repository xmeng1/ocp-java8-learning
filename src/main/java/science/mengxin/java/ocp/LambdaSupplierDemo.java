package science.mengxin.java.ocp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LambdaSupplierDemo {

    static Logger logger = Logger.getLogger("Status Logger");

    //separate method

    public static String getStatus() {
        // the status should be changed by different situation
        String host = "www.google.co.uk";
        int port = 80;
        int timeout = 1000;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return "up";
        } catch (IOException e) {
            return "down";
        }
    }

    public static void main(String [] args) {
        logger.setLevel(Level.SEVERE);


        // fix status
/*        String currentStatus = "Everything's OK";
        logger.log(Level.INFO, currentStatus);*/

        // the status should be changed by different situation
        String host = "www.google.co.uk";
        int port = 80;

        //in case we need to check the status
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
//        try {
//            logger.log(Level.INFO, getStatus());
//            throw new Exception();
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, getStatus());
//        }
        // why we make this design?

        // see OCP Chapter 8 Working with suppliers
        /*
        * When we make log for SEVERE log for status, we usually need to make a expensive call to check the status,
        * If we set global as INFO, we need always check status.
        *
        * Use the supplier which is similar with separate method to return the status. But Lambda is more concise.
        * */
    }
}
