package com.sec.secureapp.general;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.sec.secureapp.client.SSLclient;
import com.sec.secureapp.database.DB;
import com.sec.secureapp.security.Keys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class T {

    public static DB DB;
    public static String USER_ID;// = "gcxbFwGGdLQBtC81uge7eeIRI5wjv/5ljaFXfj5kaHc=";
    //public static String USER_ID = "Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=";
    public static AuctionConnection AC;

    public static String SERVER_IP = "192.168.1.3";//"zafeiratosv.ddns.net";//"zafeiratosv.ddns.net";
    public static int SERVER_PORT = 54321;
    public static String CERT_NAME = "sslsec.bks";

    public static String SUCCESS = "1";
    public static String NOT_SUCCESS = "2";
    public static String NAME_EXIST = "3";
    public static String WRONG_CREDENTIALS = "4";
    public static String WRONG_OTP = "5";
    public static String OTP_ERROR = "6";
    public static String PRIVATE_KEY_ACK = "7";
    public static String AUCTION_SUCCESS = "8";
    public static String AUCTION_ERROR = "9";
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

    public static String SIGN_UP_MESSAGE = null;
    public static String LOG_IN_MESSAGE = null;
    public static String OTP_MESSAGE = null;
    public static String MAIN_MESSAGE = null;
    public static String CREATE_AUCTION_MESSAGE = null;
    public static String OPEN_AUCTIONS_MESSAGE = null;
    public static String RUNNING_AUCTIONS_MESSAGE = null;
    public static String PARTICIPATE_MESSAGE = null;
    public static String CONNECT_AUCTION_MESSAGE = null;
    public static String BID_MESSAGE = null;
    public static String EXCHANGE_MESSAGE = null;
    public static String ADD_FUNDS_MESSAGE = null;
    public static String BALANCE_MESSAGE = null;

    public static void VIEW_TOAST(final Context con, final String toast, final int dur) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                Toast.makeText(con, toast, dur).show();
            }
        });
    }

    public static void SLEEP(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getJson(String... strings) {
        JSONObject jo = new JSONObject();
        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        try {
            for (; ; ) {
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

}
