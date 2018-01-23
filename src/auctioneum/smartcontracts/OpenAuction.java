package auctioneum.smartcontracts;

import auctioneum.network.app.T;
import auctioneum.network.app.T;
import auctioneum.database.Database;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import auctioneum.network.app.User;
import auctioneum.network.app.User;

public class OpenAuction {
    
    private int auction_id;
    private String auctioneer_id;
    private User auctioneer; // stream with auctioneer
    private ArrayList<User> participants; // streams with participants
    private Database db;
    private String auctioneerPuk;
    private String winnerPuk;
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
                new Timer(auction_id);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
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
                finished = true;
                for (User user : getParticipants()) {
                    sendMessage(T.CONNECT_AUCTION_CONFIRM + T.AUCTION_FINISHED, user.getOut());
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