package blockChain;

import java.util.LinkedList;

public class BlockChain {
    int difficulty;
    int blockCount;
    LinkedList<Block> blockList;


    public BlockChain(int difficulty) {
        this.difficulty = difficulty;
    }

    public int mineBlock() {
        return 0;
    }
    
}
