package auctioneum.network.mining;


import auctioneum.blockchain.Block;
import auctioneum.utils.hashing.SHA3_256;

import java.math.BigInteger;


public class MiningProcess {

    private Miner owner;

    private boolean running;

    private Block block;

    private int difficulty;

    private boolean solved;


    public MiningProcess(Miner owner ,Block block,int difficulty){
        this.owner = owner;
        this.block = block;
        this.difficulty = difficulty;
        this.running = true;
        this.solved = false;
    }


    public void run() {
        try {
            System.out.println("Started new mining process for block at time: "+block.getTimestamp());
            while (this.running && this.count("0", SHA3_256.hash(block.getData())) != this.difficulty){
                this.block.setNonce(this.block.getNonce().add(BigInteger.ONE));
                System.out.println(this.block.getNonce());
            }
            this.block.setHash(SHA3_256.hash(block.getData()));
            this.solved = true;
            this.running = false;
            System.out.println("Solved block: "+this.block.getHash());
            System.out.println("Nonce value: "+this.block.getNonce());
            this.owner.addBlock(this.block);
            System.out.println(block);
        }catch (Exception e){
            e.printStackTrace();
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

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isSolved() {
        return this.solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

}
