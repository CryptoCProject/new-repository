package auctioneum.network.common;



import auctioneum.network.management.Regulator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Configuration Settings of Nodes.
 */
public class Settings {

    public static InetAddress IP;

    public static int TRANSACTIONS_PORT;

    public static int VALIDATIONS_PORT;
    
    public static int CONNECTION_PORT;
    public static int REGISTRATION_PORT;

    public static List<Node> PEERS;

    public static Regulator REGULATOR;


    static {
        try {
            IP = InetAddress.getLocalHost();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        TRANSACTIONS_PORT = 4572;
        VALIDATIONS_PORT = 8338;
        CONNECTION_PORT = 5555;
        REGISTRATION_PORT = 9999;
        PEERS = new ArrayList<>();
    }

}
