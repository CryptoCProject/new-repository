package com.sec.secureapp.general;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sec.secureapp.client.SSLclient;
import com.sec.secureapp.security.Hashing;
import com.sec.secureapp.security.Keys;

import org.json.JSONException;
import org.json.JSONObject;

public class AuctionConnection extends Thread {

    private Context context;
    private String auction_id;
    private SSLclient client;
    private String _case; // 0 for auctioneer, 1 for participant
    private Hashing hash;

    public AuctionConnection(Context context, String auction_id, String _case) {
        this.context = context;
        this.auction_id = auction_id;
        this._case = _case;
        this.hash = new Hashing();
    }

    @Override
    public void run() {
        client = new SSLclient(this.context);
        client.start();
        client.sendMessage(T.CONNECT_AUCTION + this._case + this.auction_id);
        for (;;){
            T.SLEEP(500);
            if (T.CONNECT_AUCTION_MESSAGE != null) {
                if (T.CONNECT_AUCTION_MESSAGE.equals(T.AUCTION_WAITING)) {
                    T.VIEW_TOAST(this.context, "Waiting for auction " + this.auction_id  + " to start.", Toast.LENGTH_LONG);
                }
                else if (T.CONNECT_AUCTION_MESSAGE.startsWith(T.AUCTION_RUNNING)) {
                    T.VIEW_TOAST(this.context, "Auction " + this.auction_id  + " started!!!", Toast.LENGTH_LONG);
                }
                else if (T.CONNECT_AUCTION_MESSAGE.startsWith(T.AUCTION_FINISHED)) {
                    T.VIEW_TOAST(this.context, "Auction " + this.auction_id  + " finished!!!", Toast.LENGTH_LONG);
                    String id = T.CONNECT_AUCTION_MESSAGE.substring(2);
                    try {
                        JSONObject jsonObject = new JSONObject(id);
                        String auctioneer_id = jsonObject.getString("a");
                        String winner_id = jsonObject.getString("w");
                        String transaction = jsonObject.getString("t");
                        String tr_id = jsonObject.getString("i");
                        if (T.USER_ID.equals(auctioneer_id)) {
                            String signature = Keys.sign(transaction, T.DB.getPrivateKey(T.USER_ID));
                            client.sendMessage(T.TRANSACTION + T.getJson(new String[]{"r", "a", "s", signature, "i", T.USER_ID, "t", tr_id}).toString());
                        }
                        if (T.USER_ID.equals(winner_id)) {
                            String signature = Keys.sign(transaction, T.DB.getPrivateKey(T.USER_ID));
                            client.sendMessage(T.TRANSACTION + T.getJson(new String[]{"r", "w", "s", signature, "i", T.USER_ID, "t", tr_id}).toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                else if (T.CONNECT_AUCTION_MESSAGE.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Server not responding . Try again please.", Toast.LENGTH_LONG);
                    break;
                }
                //T.CONNECT_AUCTION_MESSAGE = null;
            }
        }
        this.client.closeCrap();
    }

}
