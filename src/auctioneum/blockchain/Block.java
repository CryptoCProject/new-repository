package auctioneum.blockchain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import auctioneum.utils.hashing.SHA3_256;

public class Block implements Serializable{

    private static final long serialVersionUID = -8355673521334364077L;

    /** Hash value of block **/
    private String hash;

    /** Nonce value **/
    private BigInteger nonce;

    /** Blockchain height **/
    private int number;

    /** Block difficulty **/
    private int difficulty;

    /** Generation timestamp **/
    private long timestamp;

    /** Public address of the one who mined it **/
    private String beneficiary;

    /** Transactions contained in the block **/
    private List<Transaction> transactions;


    public Block(BigInteger nonce, int number, int difficulty, String beneficiary, List<Transaction> transactions){
        this.beneficiary = beneficiary;
        this.number = number;
        this.nonce = nonce;
        this.difficulty = difficulty;
        this.timestamp = System.currentTimeMillis();
        this.transactions = transactions;
    }


    public boolean contains(Transaction tx){ return this.transactions.contains(tx); }

    public boolean isValid(){
        if(!(SHA3_256.hash(this.getData()).equals(this.hash))) {
            return false;
        }
        for(Transaction transaction : this.transactions){
            if(!transaction.isValid())
                return false;
        }
        return true;
    }

    public String getData(){
        String blockData = "";
        blockData += "Number: "+ this.number;
        blockData += "\nNonce: "+ this.nonce;
        blockData += "\nBeneficiary: "+ this.beneficiary;
        blockData += "\nTimestamp: "+ this.timestamp;
        blockData += "\nTransactions: "+ this.transactions;
        return blockData;
    }


    /**--------------Accessors-Mutators------------------**/

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBeneficiary() {
        return this.beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigInteger getNonce() {
        return this.nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }


    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
