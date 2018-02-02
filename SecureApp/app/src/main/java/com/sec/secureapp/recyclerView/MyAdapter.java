package com.sec.secureapp.recyclerView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sec.secureapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    ArrayList<HashMap<String, String>> auctionList;

    Context mContext;

    public MyAdapter(@NonNull Context context, ArrayList<HashMap<String, String>> auctionList) {
        this.auctionList = auctionList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MyViewHolder mainHolder = (MyViewHolder) holder;
        //Setting text over textview
        mainHolder._auctionTitle.setText("Auction id: "+auctionList.get(position).get("auction_id"));
        mainHolder._auctionObject.setText(auctionList.get(position).get("object_name"));
        mainHolder._auctionAuctioneer.setText(auctionList.get(position).get("auctioneer_id"));
        mainHolder._auctionPrice.setText(this.mContext.getString(R.string.auction_price, Double.parseDouble(auctionList.get(position).get("object_price"))));

        if (auctionList.get(position).get("participated").equals("1"))
            mainHolder._cardView.setCardBackgroundColor(Color.GREEN);
    }
    @Override
    public int getItemCount() {
        return auctionList.size();
    }

}
