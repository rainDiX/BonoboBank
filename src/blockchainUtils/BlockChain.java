package blockchainUtils;

import java.util.LinkedList;

public class BlockChain {
    int difficulty;
    int blockCount;
    LinkedList<Block> blockList;


    public BlockChain(int difficulty) {
        this.difficulty = difficulty;
    }
    
}
