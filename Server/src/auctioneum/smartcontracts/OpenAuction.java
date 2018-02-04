package auctioneum.smartcontracts;

import auctioneum.blockchain.Transaction;
import auctioneum.network.app.T;
import auctioneum.database.Database;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import auctioneum.network.app.User;

public class OpenAuction {
    
    private int auction_id;
    private String auctioneer_id;
    private String winner_id;
    private double final_price;
    private User auctioneer; // stream with auctioneer
    private ArrayList<User> participants; // streams with participants
    private Database db;
    private boolean finished;
    private boolean running;

    public OpenAuction(int auction_id, String auctioneer_id,  Database db) {
        this.auction_id = auction_id;
        this.auctioneer_id = auctioneer_id;
        this.participants = new ArrayList();
        this.db = db;
        this.finished = false;
        this.running = false;
    }

    public int getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(int auction_id) {
        this.auction_id = auction_id;
    }

    public String getAuctioneer_id() {
        return auctioneer_id;
    }

    public void setAuctioneer_id(String auctioneer_id) {
        this.auctioneer_id = auctioneer_id;
    }
    
    public User getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(User auctioneer) {
        this.auctioneer = auctioneer;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }
    
    public void addParticipant(User participant) {
        this.participants.add(participant);
    }
    
    public boolean runAuction() {
        if (this.participants.size() == T.NUMBER_OF_PARTICIPANTS) {
            if (this.db.runAuction(this.auction_id)) {
                this.running = true;
                for (User user : getParticipants()) {
                    sendMessage(T.CONNECT_AUCTION_CONFIRM + T.AUCTION_RUNNING, user.getOut());
                }
                new Timer(auction_id).start();
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    
    public boolean sendBid(double price) {
        final_price = price;
        for (User user : getParticipants()) {
            sendMessage(T.BID_CONFIRM + T.SUCCESS + T.getJson(new String[]{"p", String.valueOf(price), "i", String.valueOf(auction_id)}), user.getOut());
        }
        return true;
    }
    
    private class Timer extends Thread {
        
        private int auction_id;
        
        public Timer(int auction_id) {
            this.auction_id = auction_id;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(T.AUCTION_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (db.finishAuction(this.auction_id)) {
                winner_id = db.getAuctionWinner(this.auction_id);
                Transaction transaction = new Transaction(winner_id, auctioneer_id, final_price, final_price+final_price*0.1);
                T.TRANSACTIONS.put(transaction.getId(), transaction);
                finished = true;
                for (User user : getParticipants()) {
                    sendMessage(T.CONNECT_AUCTION_CONFIRM + T.AUCTION_FINISHED + 
                            T.getJson(new String[]{"a", String.valueOf(auctioneer_id), "w", String.valueOf(winner_id), "t", transaction.toString(), "i", transaction.getId()}), user.getOut());
                }
            }
        }
    }
    
    private void sendMessage(Object object, ObjectOutputStream out) {
        try {
            System.out.println("Message sent: " + object);
            out.writeObject(object);
            out.flush();
        } catch (IOException ioe) {
            System.out.println("something went wrong with the host");
            ioe.printStackTrace();
        }
    }
    
}