package auctioneum.blockchain;

import java.util.List;
import java.util.Stack;

public class BlockChain {

    private Stack<Block> blocks;

    public BlockChain(){
        this.blocks = new Stack<>();
    }

    public BlockChain(Stack<Block> blocks){
        this.blocks = blocks;
    }

    public void add(Block block){
        this.blocks.push(block);
    }

    public int size(){return (blocks.size()>0)? this.blocks.get(0).getNumber()+1 : 0;}

    //if there are new blocks take them and validate them(for miners)
    public List<Block> diff(BlockChain other){
        if (this.size() < other.size()){
            return other.blocks.subList(this.size()+1,other.size());
        }
        else {
            return new Stack<Block>();
        }
    }


    /**---------------Accessors-Mutators-------------------**/

    public Stack<Block> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(Stack<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString(){
        String blockchain = "";
        for (Block block: this.blocks){
            blockchain+=block.toString();
        }
        return blockchain;
    }
}
