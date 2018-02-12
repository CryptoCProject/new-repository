package auctioneum.network.mining;

import auctioneum.blockchain.Block;
import auctioneum.blockchain.Transaction;
import auctioneum.network.common.Node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Miner extends Node{

    private List<MiningProcess> miningProcesses;

    private final int MIN_TX_NUM = 1;

    private final int DEFAULT_DIFFICULTY = 4;

    public Miner(){
        this.miningProcesses = new ArrayList<>();
    }

    /**
     * Create a new Block ready to be mined
     * @param size
     * @param difficulty
     * @return
     */
    public Block createBlock(int size, int difficulty){
        try {
            System.out.println("Creating new block");
            int number = this.getBlockChain().size();
            System.out.println(this.getAccount().getAddress());
            String selfAddress = this.getAccount().getAddress();
            List<Transaction> txsIncluded = this.getTxPool().subList(0,size);
            return new Block(BigInteger.ZERO,number,difficulty,selfAddress,txsIncluded);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mines a given block by solving the underlying problem
     * @param block
     * @return
     */
    private void mine(Block block){
        System.out.println("Added new mining process");
        MiningProcess miningProcess = new MiningProcess(this,block,block.getDifficulty());
        this.miningProcesses.add(miningProcess);
        miningProcess.run();
    }

    public void declareOrphans(List<Transaction> validatedTxs){
        synchronized (this.miningProcesses){
            for (Transaction tx : validatedTxs) {
                for (MiningProcess mp : this.miningProcesses) {
                    if (mp.getBlock().contains(tx)) {
                        mp.kill();
                    }
                }
            }
            this.clearKilled();
        }
    }


    private void clearKilled(){
        List<MiningProcess> toRemove = new ArrayList<>();
        for (MiningProcess mp : this.miningProcesses){
            if (!mp.isRunning() && !mp.isSolved()){
                toRemove.add(mp);
            }
        }
        for (MiningProcess killedMP : toRemove){
            this.miningProcesses.remove(killedMP);
        }
    }

    public void startMining(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Thread miningStrategy = new Thread(()-> {
            System.out.println("Checking for transactions...");
            if(this.getTxPool().size() >= MIN_TX_NUM) {
                System.out.println("Found "+this.getTxPool().size()+" transactions");
                this.mine(this.createBlock(MIN_TX_NUM, DEFAULT_DIFFICULTY));
                for(int i=0; i<MIN_TX_NUM; i++){
                    this.getTxPool().remove(0);
                }
            }
        });
        executor.scheduleAtFixedRate(miningStrategy,0,2, TimeUnit.SECONDS);
    }

//    public void startMining(){
//        Thread miningStrategy = new Thread(()-> {
//            while (true) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("CHECKING FOR TRANSACTIONS");
//                if (this.getTxPool().size() >= MIN_TX_NUM) {
//                    System.out.println("FOUND " + this.getTxPool().size() + " Transactions");
//                    this.mine(this.createBlock(MIN_TX_NUM, DEFAULT_DIFFICULTY));
//                    for (int i = 0; i <= MIN_TX_NUM; i++) {
//                        this.getTxPool().remove(0);
//                    }
//                }
//            }
//        });
//
//        miningStrategy.start();
//    }

}
