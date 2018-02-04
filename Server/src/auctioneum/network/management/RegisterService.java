package auctioneum.network.management;


import auctioneum.blockchain.Account;
import auctioneum.network.common.Node;
import auctioneum.network.common.Request;
import auctioneum.utils.keys.RSA;
import auctioneum.database.Database;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;
import auctioneum.utils.keys.Keys;

public class RegisterService implements Runnable{

    private static final int NODES_THRES_DOWN = 5;

    private Node owner;

    private Socket connection;

    private Request request;

    public RegisterService(Node owner, Socket connection){
        this.owner = owner;
        this.connection = connection;
    }



    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(this.connection.getInputStream());
            Request request = (Request)ois.readObject();
            System.out.println();
            if (request.getServiceType().equals(Request.REGISTER)) {
                
                Database db = new Database();
                Keys keys = new Keys();
                keys.generateKeys();
                String publicKey = Base64.getEncoder().encodeToString(keys.getPublicKey().getEncoded());
                if (db.insertMiner(publicKey)) {
                    String privateKey = Base64.getEncoder().encodeToString(keys.getPrivateKey().getEncoded());
                    ObjectOutputStream oos = new ObjectOutputStream(this.connection.getOutputStream());
                    oos.writeObject(2);
                    oos.flush();
                    oos.writeObject(publicKey);
                    oos.flush();
                    oos.writeObject(privateKey);
                    oos.flush();
                    oos.close();
                }
            }
            else {
                this.connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
