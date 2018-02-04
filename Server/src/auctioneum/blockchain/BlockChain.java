package auctioneum.blockchain;

import java.util.List;
import java.util.Stack;

public class BlockChain {

    private Stack<Block> blocks;


    public BlockChain(Stack<Block> blocks){
        this.blocks = blocks;
    }

    public void add(Block block){
        this.blocks.push(block);
    }

    public int size(){return this.blocks.get(0).getNumber()+1;}

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
}
