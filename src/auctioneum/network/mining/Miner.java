package auctioneum.network.mining;

import auctioneum.blockchain.Account;
import auctioneum.blockchain.Block;
import auctioneum.blockchain.Transaction;
import auctioneum.network.common.Node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Miner extends Node {

    private Map<Thread,MiningProcess> miningProcesses;

    public Miner(Account account){
        super(account);
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
    private void mine(Block block, int difficulty){
        MiningProcess miningProcess = new MiningProcess(block,difficulty);
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

    @Override
    public void connect(){


    }


    public void start(){}


}
