package com.sec.secureapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sec.secureapp.R;
import com.sec.secureapp.databinding.ActivityProfileBinding;
import com.sec.secureapp.general.InfoMessage;
import com.sec.secureapp.general.T;
import com.sec.secureapp.general.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityProfileBinding binding;

    FundsChanged fundsChanded;

    private String auctions = "";
    //variable to store auctions
    ArrayList<HashMap<String, String>> auctionList;

    String[] auctionIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        getSupportActionBar().setTitle("My Profile");

        auctionList = new ArrayList<>();

        // take finished auctions from intent extras
        Intent intent = getIntent();
        this.auctions = intent.getStringExtra("auctions");

        try {
            jsonToObject(this.auctions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.profileId.setText(T.USER_ID);
        binding.profileBalance.setText(getString(R.string.profile_balance, Double.parseDouble(T.BALANCE_MESSAGE)));
        binding.profileShowAuctions.setOnClickListener(this);
        binding.profileAddFunds.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_show_auctions:
                T.VIEW_TOAST(getApplicationContext(), "Show Previous Auctions", Toast.LENGTH_SHORT);
                new MaterialDialog.Builder(this)
                        .title(R.string.finished)
                        .items(auctionIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                T.VIEW_TOAST(getApplicationContext(), "" + which, Toast.LENGTH_SHORT);
                                Intent mIntent = new Intent(getApplicationContext(), AuctionDetailsActivity.class);
                                mIntent.putExtra("title", auctionList.get(which).get("auction_id"));
                                mIntent.putExtra("object", auctionList.get(which).get("object_name"));
                                mIntent.putExtra("auctioneer", auctionList.get(which).get("auctioneer_id"));
                                mIntent.putExtra("price", auctionList.get(which).get("object_price"));
                                mIntent.putExtra("participated", auctionList.get(which).get("participated"));
                                mIntent.putExtra("running", 2);
                                mIntent.putExtra("auctions", auctions);
                                startActivity(mIntent);
                            }
                        })
                        .show();
                break;
            case R.id.profile_add_funds:
                new MaterialDialog.Builder(this)
                        .title(R.string.add_funds)
                        .content(R.string.insert_amount, Double.parseDouble(T.EXCHANGE_MESSAGE))
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input("5000", "100", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                int funds = Integer.parseInt("" + input);
                                new InfoMessage(getApplicationContext(), T.ADD_FUNDS, new UserInfo(T.USER_ID, "" + funds, null, null, null)).start(); // pwd is funds for this method
                                double zafeirium = funds * Double.parseDouble(T.EXCHANGE_MESSAGE);
                                //T.VIEW_TOAST(getApplicationContext(), "Zafeirium added to account " + getString(R.string.profile_balance, zafeirium), Toast.LENGTH_LONG);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InfoMessage(getApplicationContext(), T.BALANCE, new UserInfo(T.USER_ID, null, null, null, null)).start();
                                    }
                                }, 500);
                            }
                        })
                        .negativeText(R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    // get information from json received from server
    public void jsonToObject(String auctions) throws JSONException {
        JSONArray jArray = new JSONArray(auctions);
        auctionIds = new String[jArray.length()];
        for (int i = 0; i < jArray.length(); i++) {

            JSONObject jObject = jArray.getJSONObject(i);

            HashMap<String, String> auction = new HashMap<>();

            // store ids only
            auctionIds[i] = jObject.getString("a");

            auction.put("auction_id", jObject.getString("a"));
            auction.put("auctioneer_id", jObject.getString("i"));
            auction.put("auction_type", jObject.getString("t"));
            auction.put("object_name", jObject.getString("n"));
            auction.put("object_price", jObject.getString("p"));

            auctionList.add(auction);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // filter used for receiver
        IntentFilter filter = new IntentFilter();

        // create a receiver for open auctions and wait response from server
        fundsChanded = new FundsChanged();
        filter.addAction(getString(R.string.funds_changed));
        registerReceiver(fundsChanded, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(fundsChanded);
    }

    class FundsChanged extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null && intent.getAction().equals(getString(R.string.funds_changed))) {
                binding.profileBalance.setText(getString(R.string.profile_balance, Double.parseDouble(T.BALANCE_MESSAGE)));
            }
        }
    }
}
