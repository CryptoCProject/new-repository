package auctioneum;

import auctioneum.network.app.Server;
import auctioneum.network.app.T;
import auctioneum.network.management.Regulator;


import java.util.concurrent.ThreadLocalRandom;


public class RegulatorMain {

    
    public static Regulator reg;
    
    public static void main(String[] args){
        new Server().start();
        
        reg = new Regulator();
        
        new Rate().start();
    }
    
    public static class Rate extends Thread {
        
        @Override
        public void run() {
            while (true) {
                T.EXCHANGE_RATE = ThreadLocalRandom.current().nextDouble(0.0001, 0.001);
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {

                }
                
            }
        }
        
    }

}
