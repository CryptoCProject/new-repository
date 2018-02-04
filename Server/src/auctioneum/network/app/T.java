package auctioneum.network.app;

import auctioneum.blockchain.Transaction;
import auctioneum.smartcontracts.Auction;
import auctioneum.smartcontracts.OpenAuction;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class T {
    
    public static double EXCHANGE_RATE;
    
    public static final int PORT = 54321;
    public static final String CERT_PATH = "C:/Users/vagelis/Documents/NetBeansProjects/SSLserver/sslsec.jks";
    public static final long OTP_VALID_TIME = 180000; // 3 minutes
    public static final long AUCTION_TIME = 20000; // 20 sec
    public static final int NUMBER_OF_PARTICIPANTS = 1;
    public static HashMap<Integer, OpenAuction> AUCTIONS; // auctions are on
    public static HashMap<String, Transaction> TRANSACTIONS = new HashMap(); // transactions
    
    public static String SUCCESS = "1";
    public static String NOT_SUCCESS = "2";
    public static String NAME_EXIST = "3";
    public static String WRONG_CREDENTIALS = "4";
    public static String WRONG_OTP = "5";
    public static String OTP_ERROR = "6";
    public static String PRIVATE_KEY_ACK = "7";
    public static String AUCTION_SUCCESS= "8";
    public static String AUCTION_ERROR= "9";
    public static String AUCTION_WAITING = "10";
    public static String AUCTION_RUNNING = "11";
    public static String AUCTION_FINISHED = "12";

    public static String SIGN_UP = " a";
    public static String SIGN_UP_CONFIRM = " b";

    public static String LOG_IN = " c";
    public static String LOG_IN_CONFIRM = " d";

    public static String OTP = " e";
    public static String OTP_CONFIRM = " f";

    public static String MAIN = " g";
    public static String MAIN_CONFIRM = " h";

    public static String PRIVATE_KEY = " i";

    public static String CREATE_AUCTION = " j";
    public static String CREATE_AUCTION_CONFIRM = " k";

    public static String OPEN_AUCTIONS = " l";
    public static String OPEN_AUCTIONS_CONFIRM = " m";

    public static String RUNNING_AUCTIONS = " n";
    public static String RUNNING_AUCTIONS_CONFIRM = " o";
    
    public static String PARTICIPATE = " p";
    public static String PARTICIPATE_CONFIRM = " q";
    
    public static String CONNECT_AUCTION = " r";
    public static String CONNECT_AUCTION_CONFIRM = " s";
    
    public static String BID = " t";
    public static String BID_CONFIRM = " u";
    
    public static String EXCHANGE = " v";
    
    public static String ADD_FUNDS = " w";
    public static String ADD_FUNDS_CONFIRM = " x";
    
    public static String BALANCE = " y";
    public static String BALANCE_CONFIRM = " z";
    
    public static String TRANSACTION = " 0";
    
    public static JSONObject getJson (String ... strings) {
        JSONObject jo = new JSONObject();
        HashMap <String, String> map = new HashMap<>();
        int i = 0;
        try {
            for(;;) {
                try {
                    map.put(strings[i], strings[i + 1]);
                    i += 2;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    break;
                }
            }
            for (String k : map.keySet()) {
                jo.put(k, map.get(k));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return jo;
    }
    
    public static JSONArray getJsonArrayAuctions (ArrayList <Auction> auctions) {
        JSONArray array = new JSONArray();
        JSONObject jo = null;
        try {
            for (Auction a : auctions) {
                jo = new JSONObject();
                jo.put("a", a.getAuction_id());
                jo.put("i", a.getAuctioneer_id());
                jo.put("t", a.getAuction_type());
                jo.put("p", a.getObject_price());
                jo.put("n", a.getObject_name());
                array.put(jo);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return array;
    }
    
}
