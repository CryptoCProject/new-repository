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

public class ConnectionService implements Runnable{

    private static final int NODES_THRES_DOWN = 5;

    private Node owner;

    private Socket connection;

    private Request request;

    public ConnectionService(Node owner, Socket connection){
        this.owner = owner;
        this.connection = connection;
    }



    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(this.connection.getInputStream());
            Request request = (Request)ois.readObject();
            String pkToString = request.getParams().get(0);
            PublicKey publicKey = RSA.getPublicKeyFromString(pkToString);
            if (RSA.verify(Request.CONNECT,request.getServiceType(),publicKey)){
                ObjectOutputStream oos = new ObjectOutputStream(this.connection.getOutputStream());
                //Connect to DB and return relative account
                Database db = new Database();
                double balance = db.getBalance(pkToString,false);
                if (balance >= 0) {
                    Account account = new Account();
                    account.setBalance(balance);
                    account.setAddress(pkToString);
                    oos.writeObject(2);
                    oos.flush();
                    oos.writeObject(1);
                    oos.flush();
                    oos.writeObject(account);
                    oos.flush();
                    //Add IP to advertised peers
                    ((Regulator)this.owner).addPeer(this.connection.getInetAddress(), account);
                    
                    //Update peers Service
                    int nodesKept = ((Regulator)this.owner).getAdvertisedPeers2().size();
                    int peersToSend = nodesKept > NODES_THRES_DOWN ? (int) (0.1 * nodesKept) : nodesKept;
                    oos.writeObject(peersToSend);
                    oos.flush();
                    for (int i = 0; i < peersToSend; i++) {
                        //oos.writeObject(((Regulator)this.owner).getAdvertisedPeers().values().toArray()[i]);
                        oos.writeObject(((Regulator)this.owner).getAdvertisedPeers2().toArray()[i]);
                        oos.flush();
                    }
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
