package auctioneum.blockchain;

import auctioneum.utils.hashing.SHA3_256;
import auctioneum.utils.keys.RSA;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.UUID;


public class Transaction implements Serializable{

    private static final long serialVersionUID = -1565349679238127979L;

    /** Transaction identifier **/
    private String id;

    /** Matches number of transactions made by the sender **/
    private int nonce;
    
    private String fromId;
    private String toId;

    /** Receiver's address **/
    private String to;

    /** Sender's address **/
    private String from;

    /** Amount to be transfered **/
    private double value;

    /** Amount to be paid for tx execution **/
    private double reward;

    /** Transaction signature **/
    private String signatureFrom;
    private String signatureTo;

    public Transaction(){}

    public Transaction(double value, double reward) {
        this.id = String.valueOf(UUID.randomUUID());
        this.value = value;
        this.reward = reward;
    }

    public Transaction(String fromId, String toId, double value, double reward) {
        this.id = String.valueOf(UUID.randomUUID());
        this.fromId = fromId;
        this.toId = toId;
        this.value = value;
        this.reward = reward;
    }

    /**
     * Checks if a transaction is valid
     * @return
     */
    public boolean isValid(){
        String transactionHash = SHA3_256.hash(this.toString());
        PublicKey senderKey = RSA.getPublicKeyFromString(this.from);
        PublicKey receiverKey = RSA.getPublicKeyFromString(this.to);
        boolean senderSignatureIsValid = RSA.verify(transactionHash, this.signatureFrom, senderKey);
        boolean receiverSignatureIsValid = RSA.verify(transactionHash, this.signatureTo, receiverKey);
        boolean adequateBalance = Account.getBalance(this.from) >= this.value;
        return senderSignatureIsValid && receiverSignatureIsValid && adequateBalance;
     }


    /**--------------Accessors-Mutators------------------**/

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getReward() { return this.reward; }

    public void setReward(double reward) { this.reward = reward; }

    public String getSignatureFrom() { return this.signatureFrom; }

    public void setSignatureFrom(String signature) { this.signatureFrom = signature; }
    
    public String getSignatureTo() { return this.signatureTo; }

    public void setSignatureTo(String signature) { this.signatureTo = signature; }


    public String toString() {
        String info = "";
        info += "ID: "+ this.id;
        info += "\nFrom: "+ this.from;
        info += "\nTo: "+ this.to;
        info += "\nValue: "+ this.value;
        info += "\nReward: "+ this.reward;
        return info;
    }

}
