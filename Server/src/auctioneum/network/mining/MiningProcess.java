package auctioneum.network.mining;


import auctioneum.blockchain.Block;
import auctioneum.utils.hashing.SHA3_256;

import java.math.BigInteger;


public class MiningProcess implements Runnable{

    private boolean running;

    private Block block;

    private int difficulty;


    public MiningProcess(Block block,int difficulty){
        this.block = block;
        this.difficulty = difficulty;
        this.running = true;
    }


    @Override
    public void run() {
        while (this.running && this.count("0", SHA3_256.hash(block.getData())) != this.difficulty){
            this.block.setNonce(this.block.getNonce().add(BigInteger.ONE));
        }
    }

    /**
     * Auxiliary method for solving the problem
     * @param string
     * @param substring
     * @return
     */
    private int count(final String substring, final String string){
        int count = 0;
        int idx = 0;

        while ((idx = string.indexOf(substring, idx)) != -1)
        {
            idx++;
            count++;
        }

        return count;
    }

    public void kill(){ this.running = false; }

    /**----------------------------- Accessors-Mutators -----------------------------**/

    public Block getBlock() {
        return this.block;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

}
