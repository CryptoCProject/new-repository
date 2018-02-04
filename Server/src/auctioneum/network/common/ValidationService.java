package auctioneum.network.common;

import auctioneum.network.mining.Miner;
import auctioneum.blockchain.Block;

import java.io.ObjectInputStream;
import java.net.Socket;


public class ValidationService implements Runnable {

    /** Miner that runs validation **/
    private Node owner;

    /** Client connection **/
    private Socket connection;

    /** Block to be validated **/
    private Block candidateBlock;



    public ValidationService(Node owner, Socket connection){
        this.owner = owner;
        this.connection = connection;
    }

    /**
     * Adds a new block in the blockchain if valid and then broadcasts it to other peers.
     */
    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            this.candidateBlock = (Block) ois.readObject();
            boolean hasHigherNum = this.candidateBlock.getNumber()>this.owner.getBlockChain().size();
            if( hasHigherNum && this.candidateBlock.isValid()){
                this.owner.addBlock(this.candidateBlock);
                if(this.owner instanceof Miner){
                    Miner self = (Miner) this.owner;
                    self.declareOrphans(this.candidateBlock.getTransactions());
                }
                this.owner.updateTxPool("remove",this.candidateBlock.getTransactions());
                for (Node peer: this.owner.getPeers()){
                    this.owner.sendForValidation(this.candidateBlock,peer);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**-------------------Accessors-Mutators---------------------**/

    public Node getOwner() {
        return this.owner;
    }

    public void setOwner(Miner owner) {
        this.owner = owner;
    }

    public Socket getConnection() {
        return this.connection;
    }

    public void setConnection(Socket connection) {
        this.connection = connection;
    }

    public Block getCandidate() {
        return this.candidateBlock;
    }

    public void setCandidate(Block candidateBlock) {
        this.candidateBlock = candidateBlock;
    }

}
