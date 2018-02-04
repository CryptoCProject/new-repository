package auctioneum.network.app;

import auctioneum.RegulatorMain;
import auctioneum.blockchain.Transaction;
import auctioneum.smartcontracts.Auction;
import auctioneum.database.Database;
import auctioneum.network.app.T;
import auctioneum.network.common.Node;
import auctioneum.utils.otp.OtpStats;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import org.json.JSONException;
import org.json.JSONObject;
import auctioneum.utils.hashing.SHA_256;
import auctioneum.utils.keys.Keys;
import auctioneum.utils.otp.Otp;

public class User extends Thread {

    private ObjectOutputStream out; // output stream for each client
    private ObjectInputStream in; // input stream for each client
    private Socket connection;
    private Database db;
    private SHA_256 hash;
    private HashMap <String, OtpStats> otpUsers;
    private Keys key_factory;
    private String address, pwd, email;

    public User(Socket connection, Database db, HashMap <String, OtpStats> otpUsers) throws IOException {
        this.connection = connection;
        this.db = db;
        this.hash = new SHA_256();
        this.otpUsers = otpUsers;
        this.key_factory = new Keys();
        setupStreams();
    }

    @Override
    public void run() {
        try {
            whileChatting();

        } catch (SQLException | IOException | NullPointerException ex) {
            ex.printStackTrace();

        } finally {
            System.out.println("closing crap for Thread: " + this.getId());
            closeCrap();
        }
    }

    private void setupStreams() throws IOException {
        System.out.println(connection.getOutputStream());
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
        System.out.println("Message: " + "Server: Strems are now set up");
    }

    private void whileChatting() throws IOException, SQLException {
        Object object;
        while (true) {
            try {
                object = in.readObject();
                System.out.println("Message came: " + object);

                if (object instanceof String) {
                    String mes = (String) object;
                    
                    // when User tries to sign up
                    if (mes.startsWith(T.SIGN_UP)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            address = jo.getString("u");
                            pwd = jo.getString("p");
                            email = jo.getString("e");
                            if (this.db.isUserExist(address)) {
                                this.sendMessage(T.SIGN_UP_CONFIRM + T.NAME_EXIST);
                            }
                            else {
                                key_factory.generateKeys();
                                String private_key = Base64.getEncoder().encodeToString(key_factory.getPrivateKey().getEncoded());
                                this.sendMessage(T.PRIVATE_KEY + private_key);
                            }
                        } 
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.SIGN_UP_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    // receive ACK for private key and sign up User
                    else if (mes.startsWith(T.PRIVATE_KEY)) {
                        mes = mes.substring(2);
                        if (mes.equals(T.PRIVATE_KEY_ACK)) {
                            String public_key = Base64.getEncoder().encodeToString(key_factory.getPublicKey().getEncoded());
                            System.out.println("public key length = " + public_key.length());
                            if (this.db.insertUser(address, pwd, email, public_key)) {
                                this.sendMessage(T.SIGN_UP_CONFIRM + T.SUCCESS);
                            }
                            else {
                                this.sendMessage(T.SIGN_UP_CONFIRM + T.NOT_SUCCESS);
                            }
                        }
                    }
                    
                    // when user tries to log in
                    else if (mes.startsWith(T.LOG_IN)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String name = jo.getString("u");
                            String pwd = jo.getString("p");
                            String salt = jo.getString("o");
                            String email = db.getEmailAndAuthenticate(name, pwd);
                            if (email != null) {
                                Otp o = new Otp(salt, email, hash);
                                if (o.sendMail()) {
                                    String otp = o.getOtp();
                                    if (db.insertOtp(name, hash.getHashedCode(otp))) {
                                        if (this.otpUsers.containsKey(name)) {
                                            this.otpUsers.remove(name);
                                        }
                                        this.otpUsers.put(name, new OtpStats(name, this.otpUsers));
                                        this.sendMessage(T.LOG_IN_CONFIRM + T.SUCCESS);
                                    } else {
                                        this.sendMessage(T.LOG_IN_CONFIRM + T.NOT_SUCCESS);
                                    }
                                } else {
                                    this.sendMessage(T.LOG_IN_CONFIRM + T.NOT_SUCCESS);
                                }
                            } else {
                                this.sendMessage(T.LOG_IN_CONFIRM + T.WRONG_CREDENTIALS);
                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.LOG_IN_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    // when user sends otp
                    else if (mes.startsWith(T.OTP)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String name = jo.getString("u");
                            String otpTyped = jo.getString("o");
                            if (this.otpUsers.containsKey(name)) {
                                if (this.otpUsers.get(name).areThereMoreTries()) {
                                    String realOtp = db.getOtp(name);
                                    if (realOtp.equals(hash.getHashedCode(otpTyped))) {
                                        long lastLogin = System.currentTimeMillis();
                                        if (db.insertLastlogin(name, lastLogin)) {
                                            this.otpUsers.remove(name);
                                            this.sendMessage(T.OTP_CONFIRM + T.SUCCESS);
                                        } else {
                                            this.sendMessage(T.OTP_CONFIRM + T.NOT_SUCCESS);
                                        }
                                    } else {
                                        this.sendMessage(T.OTP_CONFIRM + T.WRONG_OTP);
                                    }
                                } else {
                                    this.sendMessage(T.OTP_CONFIRM + T.OTP_ERROR);
                                }
                            } else {
                                this.sendMessage(T.OTP_CONFIRM + T.OTP_ERROR);
                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.OTP_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    // when user create auction
                    else if (mes.startsWith(T.CREATE_AUCTION)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String auction_type = jo.getString("t");
                            String auctioneer_id = jo.getString("u");
                            String object_name = jo.getString("o");
                            double initial_price  = Double.parseDouble(jo.getString("p"));
                            int auction_id = db.insertAuction(auction_type, auctioneer_id, object_name, initial_price, this);
                            if (auction_id != -1) {
                                this.sendMessage(T.CREATE_AUCTION_CONFIRM + T.AUCTION_SUCCESS + auction_id);
                            }
                            else {
                                this.sendMessage(T.CREATE_AUCTION_CONFIRM + T.AUCTION_ERROR);
                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.CREATE_AUCTION_CONFIRM + T.AUCTION_ERROR);
                        }
                    }
                    
                    // when user requests for open auctions
                    else if (mes.startsWith(T.OPEN_AUCTIONS)) {
                        mes = mes.substring(2);
                        try {
                            ArrayList<Auction> auctions = db.getOpenAuctions();
                            if (!auctions.isEmpty()) {
                                this.sendMessage(T.OPEN_AUCTIONS_CONFIRM + T.getJsonArrayAuctions(auctions));
                            }
                            else {
                                this.sendMessage(T.OPEN_AUCTIONS_CONFIRM + T.AUCTION_ERROR);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.OPEN_AUCTIONS_CONFIRM + T.AUCTION_ERROR);
                        }
                    }
                    
                    // when user requests for running auctions
                    else if (mes.startsWith(T.RUNNING_AUCTIONS)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String id = jo.getString("u");
                            ArrayList<Auction> auctions = db.getRunningAuctions(id);
                            if (!auctions.isEmpty()) {
                                this.sendMessage(T.RUNNING_AUCTIONS_CONFIRM + T.getJsonArrayAuctions(auctions));
                            }
                            else {
                                this.sendMessage(T.RUNNING_AUCTIONS_CONFIRM + T.AUCTION_ERROR);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.RUNNING_AUCTIONS_CONFIRM + T.AUCTION_ERROR);
                        }
                    }
                    
                    // when user requests to participate
                    else if (mes.startsWith(T.PARTICIPATE)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String participant_id = jo.getString("u");
                            String auction_id = jo.getString("i");
                            if (db.insertParticipant(participant_id, auction_id)) {
                                this.sendMessage(T.PARTICIPATE_CONFIRM + T.SUCCESS);
                            }
                            else {
                                this.sendMessage(T.PARTICIPATE_CONFIRM + T.NOT_SUCCESS);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.PARTICIPATE_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    // when user requests for continuous auction connection
                    else if (mes.startsWith(T.CONNECT_AUCTION)) {
                        mes = mes.substring(2);
                        String _case =  mes.substring(0,1);  // 0 for auctioneer, 1 for participant
                        int auction_id = Integer.parseInt(mes.substring(1)); // auction_id
                        try {
                            if (_case.equals("1")) { // for participant
                                T.AUCTIONS.get(auction_id).addParticipant(this);
                                if (T.AUCTIONS.get(auction_id).runAuction()) {
    //                                for (ActionsForClient afc : T.AUCTIONS.get(mes).getParticipants()) {
    //                                    this.sendMessage(T.CONNECT_AUCTION_CONFIRM + T.AUCTION_RUNNING, afc.getOut());
    //                                }
                                }
                                else {
                                    this.sendMessage(T.CONNECT_AUCTION_CONFIRM + T.AUCTION_WAITING);
                                }
                            }
                            else { // for auctioneer
                                T.AUCTIONS.get(auction_id).setAuctioneer(this);
                                this.sendMessage(T.CONNECT_AUCTION_CONFIRM + T.AUCTION_WAITING);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.CONNECT_AUCTION_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    // when user bids
                    else if (mes.startsWith(T.BID)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String participant_id = jo.getString("u");
                            int auction_id = Integer.parseInt(jo.getString("i"));
                            double price = Double.parseDouble(jo.getString("p"));
                            if (db.insertBid(participant_id, auction_id, price)) {
                                T.AUCTIONS.get(auction_id).sendBid(price);
                            }
                            else {
                                this.sendMessage(T.BID_CONFIRM + T.NOT_SUCCESS);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.BID_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    // when user wants exchange rate
                    else if (mes.startsWith(T.EXCHANGE)) {
                        this.sendMessage(T.EXCHANGE + T.EXCHANGE_RATE);
                    }
                    
                    else if (mes.startsWith(T.ADD_FUNDS)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String user_id = jo.getString("u");
                            double money = jo.getDouble("m");
                            
                            if (db.setBalance(user_id, money)) {
                                this.sendMessage(T.ADD_FUNDS_CONFIRM + T.SUCCESS);
                            }
                            else {
                                this.sendMessage(T.ADD_FUNDS_CONFIRM + T.NOT_SUCCESS);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.ADD_FUNDS_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    else if (mes.startsWith(T.BALANCE)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String user_id = jo.getString("u");
                            
                            double balance = db.getBalance(user_id, false);
                            if (balance >= 0) {
                                this.sendMessage(T.BALANCE_CONFIRM + balance);
                            }
                            else {
                                this.sendMessage(T.BALANCE_CONFIRM + T.NOT_SUCCESS);
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.BALANCE_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                    else if (mes.startsWith(T.TRANSACTION)) {
                        mes = mes.substring(2);
                        try {
                            JSONObject jo = new JSONObject(mes);
                            String user_id = jo.getString("i");
                            String signature = jo.getString("s");
                            String tr_id = jo.getString("t");
                            String res = jo.getString("r");
                            
                            String puk = db.getPublicKey(user_id);
                            Transaction tr = T.TRANSACTIONS.get(tr_id);
                            
                            if (res.equals("w")) {
                                tr.setFrom(puk);
                                tr.setSignatureFrom(signature);
                            }
                            if (res.equals("a")) {
                                tr.setTo(puk);
                                tr.setSignatureTo(signature);
                            }
                            if (tr.getFrom() != null && tr.getTo() != null) {
                                System.out.println("---------------MALAKAS----------------------");
                                for(Node peer : RegulatorMain.reg.getPeers()){
                                    RegulatorMain.reg.sendTransaction(tr, peer);
                                }
                            }
                            
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            this.sendMessage(T.BALANCE_CONFIRM + T.NOT_SUCCESS);
                        }
                    }
                    
                }

            } catch (ClassNotFoundException ex) {
                System.out.println("Message: " + "Class not found exception ");
                ex.printStackTrace();
            }
        }
    }

    public void sendMessage(Object object) {
        try {
            System.out.println("Message sent: " + object);
            out.writeObject(object);
            out.flush();
        } catch (IOException ioe) {
            System.out.println("something went wrong with the host");
            ioe.printStackTrace();
        }
    }

    public void sendMessage(Object object, ObjectOutputStream out) {
        try {
            System.out.println("Message sent: " + object);
            out.writeObject(object);
            out.flush();
        } catch (IOException ioe) {
            System.out.println("something went wrong with the host");
            ioe.printStackTrace();
        }
    }
    
    public ObjectOutputStream getOut() {
        return this.out;
    }

    private void closeCrap() {
        System.out.println("Message: " + "Closing Connections...");
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.close();
            }
//            Tools.USERS.remove(this);
        } catch (IOException ioe) {
            System.out.println("Message: " + "Closing Connections. EXCEPTION.");
            ioe.printStackTrace();
        }
        System.out.println("Message: " + "Closing Connections... COOL");
    }

}
