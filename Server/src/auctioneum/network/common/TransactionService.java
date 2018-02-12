package auctioneum.network.common;

import auctioneum.blockchain.Transaction;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;

public class TransactionService implements Runnable{

    private Node owner;

    private Socket connection;

    private Transaction transaction;


    public TransactionService(Node owner, Socket connection){
        this.owner = owner;
        this.connection = connection;
    }


    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            this.transaction = (Transaction) ois.readObject();
            System.out.println("Received Transaction "+new Date()+" :"+this.transaction);
            if (!this.owner.hasTransaction(this.transaction) && this.transaction.isValid()) {
                this.owner.addTransaction(this.transaction);
                System.out.println("Transaction is new and valid");
                for (Node peer : this.owner.getPeers()){
                    this.owner.sendTransaction(this.transaction,peer);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
