package com.sec.secureapp.recyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sec.secureapp.R;

public class MyViewHolder extends RecyclerView.ViewHolder{

    TextView _auctionTitle;
    TextView _auctionObject;
    TextView _auctionAuctioneer;
    TextView _auctionPrice;
    CardView _cardView;

    public MyViewHolder(View itemView) {
        super(itemView);

        this._auctionTitle = (TextView) itemView.findViewById(R.id.auction_title);
        this._auctionObject = (TextView) itemView.findViewById(R.id.auction_object);
        this._auctionAuctioneer = (TextView) itemView.findViewById(R.id.auction_auctioneer);
        this._auctionPrice = (TextView) itemView.findViewById(R.id.auction_price);
        this._cardView = (CardView) itemView.findViewById(R.id.cardview);

    }
}
