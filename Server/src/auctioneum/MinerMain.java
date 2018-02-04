package auctioneum;

import auctioneum.network.mining.Miner;

public class MinerMain {
    public static void main(String[] args){
        try {
            Miner self = new Miner();
            self.connect();
            self.getPeers().stream().forEach(System.out::println);
            self.startTxServer();
            self.startVdnServer();
            self.startMining();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
