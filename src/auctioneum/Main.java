package auctioneum;


import auctioneum.blockchain.Account;
import auctioneum.blockchain.Transaction;
import auctioneum.network.app.Server;
import auctioneum.network.common.Node;
import auctioneum.network.management.Regulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args){
        new Server().start();
        
        Regulator reg = new Regulator();
    }

}
