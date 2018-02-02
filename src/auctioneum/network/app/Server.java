package auctioneum.network.app;

import auctioneum.network.app.User;
import auctioneum.database.Database;
import auctioneum.network.app.T;
import auctioneum.utils.otp.OtpStats;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class Server extends Thread {

    private ServerSocket providerSocket; 
    private Socket connection;
    private Thread actionsForClient;
    private Database db;
    private HashMap <String, OtpStats> otpUsers;

    public Server() {
        otpUsers = new HashMap();
        db = new Database();
        T.AUCTIONS = new HashMap();
    }

    @Override
    public void run() {
        try {
            createServerSocket();
            try {
                while (true) {
                    waitForConnection();
                    actionsForClient = new User(connection, db, otpUsers);
                    actionsForClient.start();
                }
            } catch (Exception e) {
                System.out.println("Server closed by exception");
                e.printStackTrace();
            } finally {
                System.out.println("Server closed");
                closeCrap();
            }
        } catch (Exception e) {
            System.out.println("ServerSocket was not created");
            e.printStackTrace();
        }

    }

    private void createServerSocket() throws IOException {
        try {
            providerSocket = new ServerSocket(T.PORT);
        } catch (Exception e) {
            System.out.println("Create Socket exception");
            e.printStackTrace();

        }
    }
    
//    public void printKeyPair(final KeyStore keystore, final String alias, final String password) {
//        final Key key;
//        try {
//            key = (PrivateKey) keystore.getKey(alias, password.toCharArray());
//            final Certificate cert = keystore.getCertificate(alias);
//            final PublicKey publicKey = cert.getPublicKey();
//
//            System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
//            System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        
//    }

    private void waitForConnection() throws IOException {
        System.out.println("Message: Waiting for connection");
        connection = providerSocket.accept();
    }

    private void closeCrap() {
        System.out.println("Message: " + "Closing Connections in server main...");
        try {
            if (connection != null) {
                connection.close();
            }
            if (providerSocket != null) {
                providerSocket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
