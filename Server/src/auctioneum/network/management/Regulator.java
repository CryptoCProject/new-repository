package auctioneum.network.management;


import auctioneum.blockchain.Account;
import auctioneum.network.common.Node;
import auctioneum.network.common.Server;
import auctioneum.network.common.Settings;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Responsible for connections,updates in the network **/
public class Regulator extends Node{

    private ArrayList<Node> advertisedPeers2;
    
    private Map<Integer,HashSet<Node>> advertisedPeers;

    private Server connectionServer;
    
    private Server registrationServer;
    
    public Regulator(){
        this.advertisedPeers2 = new ArrayList();
        advertisedPeers = new TreeMap<>();
        connectionServer = new Server<ConnectionService>(this,Settings.CONNECTION_PORT,"ConnectionServer",ConnectionService.class);
        registrationServer = new Server<RegisterService>(this,Settings.REGISTRATION_PORT,"RegistrationServer",RegisterService.class);
        Thread connectionService = new Thread(connectionServer);
        Thread registrationService = new Thread(registrationServer);
        registrationService.start();
        connectionService.start();
    }


    public void addPeer(InetAddress ip, Account ac){
        Node peer = new Node();
        peer.setIp(ip);
        peer.setAccount(ac);
        peer.setTransactionsPort(Settings.TRANSACTIONS_PORT);
        peer.setValidationsPort(Settings.VALIDATIONS_PORT);
        this.advertisedPeers2.add(peer);
//        if(this.advertisedPeers.containsKey(0)){
//            this.advertisedPeers.get(0).add(peer);
//        }
//        else{
//            HashSet <Node> hs = new HashSet();
//            hs.add(peer);
//            this.advertisedPeers.put(0,hs);
//        }
    }
    
    public int hasPeer(Node peer){
        for(Map.Entry<Integer,HashSet<Node>> entry : this.advertisedPeers.entrySet()){
            if(entry.getValue().contains(peer)){
                return entry.getKey();
            }
        }
        return -1;
    }
    
    

    //public void

    public Map<Integer,HashSet<Node>> getAdvertisedPeers() {
        return this.advertisedPeers;
    }

    public void setAdvertisedPeers(Map<Integer,HashSet<Node>> advertisedPeers) {
        this.advertisedPeers = advertisedPeers;
    }
    
    public ArrayList<Node> getAdvertisedPeers2() {
        return this.advertisedPeers2;
    }
    
//   public void close() {
//       this.connectionServer.stop();
//       this.registrationServer.stop();
//   }

}
