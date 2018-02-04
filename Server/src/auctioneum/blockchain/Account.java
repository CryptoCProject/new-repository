package auctioneum.blockchain;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable{
    
    

    /** Signifies the number of transactions made by this account **/
    private int nonce;

    /** Public identified of the account **/
    private String address;

    /** Account balance **/
    private double balance;

    /** Pending transactions **/
    List<Transaction> pendingTxs;

    public Account(){}


    public static double getBalance(String address) {
        return 0.0; //TODO: connect to DB and return balance for this address
    }





    /**--------------Accessors-Mutators-------------------**/

    public int getNonce() { return this.nonce; }

    public void setNonce(int nonce) { this.nonce = nonce; }

    public String getAddress() { return this.address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public double getBalance(){return this.balance;}

    public List<Transaction> getPendingTxs() { return this.pendingTxs; }

    public void setPendingTxs(List<Transaction> pendingTxs) { this.pendingTxs = pendingTxs; }
    
    public String toString(){
        return "Address: "+this.address + "\nBalance: " + this.balance;
    }
    
}
