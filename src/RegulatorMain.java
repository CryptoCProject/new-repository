import auctioneum.network.app.Server;
import auctioneum.network.management.Regulator;

public class RegulatorMain {

    public static void main(String[] args){
        new Server().start();
        Regulator reg = new Regulator();
    }

}
