package auctioneum;


import auctioneum.network.common.Node;
import auctioneum.network.mining.Miner;


public class NodeMain {

    public static void main(String[] args){
        try {
            Node self = new Node();
            self.connect();
            //self.startTxServer();
            //self.startVdnServer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
