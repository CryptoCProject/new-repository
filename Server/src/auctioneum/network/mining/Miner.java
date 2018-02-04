package auctioneum.network.mining;

import auctioneum.blockchain.Block;
import auctioneum.blockchain.Transaction;
import auctioneum.network.common.Node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Miner extends Node{

    private Map<Thread,MiningProcess> miningProcesses;

    private final int MIN_TX_NUM = 1;
    private final int DEFAULT_DIFFICULTY = 4;

    public Miner(){
        this.miningProcesses = new HashMap<>();
    }

    /**
     * Create a new Block ready to be mined
     * @param size
     * @param difficulty
     * @return
     */
    public Block createBlock(int size, int difficulty){
        int number = this.getBlockChain().size()+1;
        String selfAddress = this.getAccount().getAddress();
        List<Transaction> txsIncluded = this.getTxPool().subList(0,size);
        return new Block(BigInteger.ZERO,number,difficulty,selfAddress,txsIncluded);
    }

    /**
     * Mines a given block by solving the underlying problem
     * @param block
     * @return
     */
    private void mine(Block block){
        MiningProcess miningProcess = new MiningProcess(block,block.getDifficulty());
        Thread miningService = new Thread(miningProcess);
        miningService.start();
        this.miningProcesses.put(miningService,miningProcess);
    }

    public void declareOrphans(List<Transaction> validatedTxs){
        synchronized (this.miningProcesses){
            for (Transaction tx : validatedTxs) {
                for (MiningProcess mp : this.miningProcesses.values()) {
                    if (mp.getBlock().contains(tx)) {
                        mp.kill();
                    }
                }
            }
            this.clearKilled();
        }
    }

    private void clearKilled(){
        List<Thread> toRemove = new ArrayList<>();
        for (Thread t : this.miningProcesses.keySet()){
            if (t.getState() == Thread.State.TERMINATED){
                toRemove.add(t);
            }
        }
        for (Thread t : toRemove){
            this.miningProcesses.remove(t);
        }
    }


    public void startMining(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Thread miningStrategy = new Thread(()-> {
            if(this.getTxPool().size() >= MIN_TX_NUM) {
                this.mine(this.createBlock(MIN_TX_NUM, DEFAULT_DIFFICULTY));
            }
        });
    }


}
