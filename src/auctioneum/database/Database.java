package auctioneum.database;

import auctioneum.smartcontracts.Auction;
import auctioneum.smartcontracts.OpenAuction;
import auctioneum.network.app.T;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import auctioneum.network.app.User;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;


public class Database {

    private final String url = "jdbc:mysql://localhost:3306/crypto?verifyServerCertificate=false&requireSSL=false&useSSL=false";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "root";
    private final String password = "rdghm3AS";

    private Connection conn = null;

    public Database() {

    }

    private void openDB() {
        try {
            Class.forName(driver).newInstance(); // register jdbc server
            conn = DriverManager.getConnection(url, userName, password); // open the connection
        } catch (Exception e) {
            e.printStackTrace(); // Handle errors for JDBC
            System.out.println("Closing database");
        }
    }

    private void closeDB() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
//    public boolean insertUser(String name, String pwd, String email) {
//        this.openDB();
//        String query = "INSERT INTO Users (name, pwd, email) VALUES ('" + name + "', '" + pwd + "', '" + email + "')";
//        try {
//            Statement stmt = conn.createStatement();
//            stmt.executeUpdate(query);
//            this.closeDB();
//            return true;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            this.closeDB();
//            return false;
//        }
//    }
    
    public boolean insertUser(String user_id, String pwd, String email, String publicKey) {
        this.openDB();
        String query = "INSERT INTO User (user_id, pwd, email, public_key) VALUES ('" + user_id + "', '" + pwd + "', '" + email + "', '" + publicKey + "')";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public boolean isUserExist(String user_id) {
        this.openDB();
        String query = "SELECT user_id FROM User WHERE user_id = '" + user_id + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String n = null;
            while (rs.next()) {
                n = rs.getString("user_id");
            }
            this.closeDB();
            if (n == null){
                return false;
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return true;
        }
    }
    
    public String getEmailAndAuthenticate(String user_id, String pwd) {
        this.openDB();
        String query = "SELECT email FROM User WHERE user_id = '" + user_id + "' AND pwd ='" + pwd + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String email = null;
            while (rs.next()) {
                email = rs.getString("email");
            }
            this.closeDB();
            return email;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return null;
        }
    } 
    
    public boolean insertOtp(String user_id, String otp) {
        this.openDB();
        String query = "UPDATE User SET otp = '" + otp +  "' WHERE user_id = '" + user_id + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public String getOtp(String user_id){
        this.openDB();
        String query = "SELECT otp FROM User WHERE user_id = '" + user_id + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String otp = null;
            while (rs.next()) {
                otp = rs.getString("otp");
            }
            this.closeDB();
            return otp;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return null;
        }
    }
    
    public boolean insertLastlogin(String user_id, long time) {
        this.openDB();
        String query = "UPDATE User SET last_login = " + time +  " WHERE user_id = '" + user_id + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public int insertAuction(String auction_type, String auctioneer_id, String object_name, double initial_price, User afc) {
        this.openDB();
        String query = "INSERT INTO auction (auctioneer_id, auction_status, auction_type, initial_price, object_name, current_price) "
                + "VALUES ('" + auctioneer_id + "', " + 0 + ", '" + auction_type + "', '" + initial_price + "', '" + object_name + "', '" + initial_price + "')";
        try {
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();    
            keys.next();
            int auctionId = keys.getInt(1);
            T.AUCTIONS.put(auctionId, new OpenAuction(auctionId, auctioneer_id, this));
            this.closeDB();
            return auctionId;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return -1;
        }
    }
    
    public ArrayList<Auction> getOpenAuctions() {
        this.openDB();
        String query = "SELECT auction_id, auctioneer_id, auction_type, initial_price, object_name FROM auction "
                + "WHERE auction_status = 0";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<Auction> auctions = new ArrayList();
            int auction_id;
            double object_price;
            String auctioneer_id, auction_type, object_name;
            while (rs.next()) {
                auction_id = rs.getInt("auction_id");
                auctioneer_id = rs.getString("auctioneer_id");
                auction_type = rs.getString("auction_type");
                object_price = rs.getDouble("initial_price");
                object_name = rs.getString("object_name");
                auctions.add(new Auction(auction_id, auctioneer_id, auction_type, object_price, object_name));
            }
            this.closeDB();
            return auctions;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return null;
        }
    }
    
    public ArrayList<Auction> getRunningAuctions(String id) {
        this.openDB();
        String query = "SELECT auction_id, auctioneer_id, auction_type, current_price, object_name " +
                        "FROM auction " +
                        "WHERE auctioneer_id = '" + id + "' AND auction_status = 1 " +
                        "UNION " +
                        "SELECT a.auction_id, a.auctioneer_id, a.auction_type, a.current_price, a.object_name " +
                        "FROM auction as a, participant as p " +
                        "WHERE a.auction_id = p.auction_id AND p.participant_id = '" + id + "' AND a.auction_status = 1;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<Auction> auctions = new ArrayList();
            int auction_id;
            double object_price;
            String auctioneer_id, auction_type, object_name;
            while (rs.next()) {
                auction_id = rs.getInt("auction_id");
                auctioneer_id = rs.getString("auctioneer_id");
                auction_type = rs.getString("auction_type");
                object_price = rs.getDouble("current_price");
                object_name = rs.getString("object_name");
                auctions.add(new Auction(auction_id, auctioneer_id, auction_type, object_price, object_name));
            }
            this.closeDB();
            return auctions;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return null;
        }
    }
    
    public ArrayList<Auction> getUserAuctions(String id) {
        this.openDB();
        String query = "SELECT auction.auction_id, auction.auctioneer_id, auction.auction_type, auction.initial_price, auction.object_name " +
                        "FROM auction " +
                        "WHERE auction.auctioneer_id = '" + id + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<Auction> auctions = new ArrayList();
            int auction_id;
            double object_price;
            String auctioneer_id, auction_type, object_name;
            while (rs.next()) {
                auction_id = rs.getInt("auction.auction_id");
                auctioneer_id = rs.getString("auction.auctioneer_id");
                auction_type = rs.getString("auction.auction_type");
                object_price = rs.getDouble("auction.initial_price");
                object_name = rs.getString("auction.object_name");
                auctions.add(new Auction(auction_id, auctioneer_id, auction_type, object_price, object_name));
            }
            this.closeDB();
            return auctions;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return null;
        }
    }
    
    public boolean insertParticipant(String participant_id, String auction_id) {
        this.openDB();
        String query = "INSERT INTO participant (participant_id, auction_id, outcome) "
                + "VALUES ('" + participant_id + "', '" + auction_id + "', " + 0 + ")";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public boolean insertBid(String participant_id, int auction_id, double price) {
        this.openDB();
        String query = "UPDATE auction "
                + "SET current_price = " + price + ", "
                + "bidder = '" + participant_id + "' "
                + "WHERE auction_id = '" + auction_id + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public boolean runAuction(int auction_id) {
        this.openDB();
        String query = "UPDATE auction SET auction_status = 1 WHERE auction_id = '" + auction_id + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public boolean finishAuction(int auction_id) {
        this.openDB();
        String query = "UPDATE auction SET auction_status = 2 WHERE auction_id = '" + auction_id + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public String getAuctionWinner(int auction_id) {
        this.openDB();
        String query = "SELECT bidder FROM Auction WHERE auction_id = '" + auction_id + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String winner = "";
            while (rs.next()) {
                winner = rs.getString("bidder");
            }
            this.closeDB();
            return winner;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return null;
        }
    }
    
    public boolean insertMiner(String publicKey) {
        this.openDB();
        String query = "INSERT INTO Miner (public_key) VALUES ('" + publicKey + "')";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public double getMinerBalance(String publicKey) {
        this.openDB();
        String query = "SELECT balance FROM Miner WHERE public_key = '" + publicKey + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            double balance = 0;
            while (rs.next()) {
                balance = rs.getDouble("balance");
            }
            this.closeDB();
            return balance;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return -1;
        }
    }
    
    public boolean setBalance(String id, double money) {
        this.openDB();
        
        double balance = this.getBalance(id, true);
        String query = "UPDATE user SET balance = " + (balance+money*T.EXCHANGE_RATE) + " WHERE user_id = '" + id + "'";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            this.closeDB();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return false;
        }
    }
    
    public double getBalance(String user_id, boolean db_open){
        this.openDB();
        String query = "SELECT balance FROM User WHERE user_id = '" + user_id + "'";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            double balance = 0;
            while (rs.next()) {
                balance = rs.getDouble("balance");
            }
            if (!db_open) this.closeDB();
            return balance;
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (!db_open) this.closeDB();
            return -1;
        }
    }
    
    
    
    public String getPublicKey(String user_id) {
        this.openDB();
        String query = "SELECT public_key FROM user WHERE user_id = '" + user_id + "'";
        try {
            String puk = "";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Blob bl = rs.getBlob("public_key");
                puk = new String(bl.getBytes(1l, (int) bl.length()));
            }
            this.closeDB();
            return puk;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeDB();
            return null;
        }
    }
    
}
