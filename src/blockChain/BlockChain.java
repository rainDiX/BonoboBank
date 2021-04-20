package blockChain;

import java.util.LinkedList;

public class BlockChain {
    /**
     * Difficulté de la blockchain, içi le nombre de 0 par lequel commence le hash
     * d'un bloc
     */
    private int difficulty;
    /**
     * Liste de blocs
     */
    private LinkedList<Block> blockList;

    public BlockChain(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean addBlock(Block b) {
        if (blockList.isEmpty() && b.getIndex() == 0) {
            blockList.add(b);
            return true;
        } else if (b.getHash().startsWith("0".repeat(this.difficulty))
                && b.getPrevBlockHash() == blockList.getLast().getPrevBlockHash() 
                && b.getIndex() == blockList.size()) {
            blockList.add(b);
            return true;
        }
        return false;
    }

	public int getDifficulty() {
		return this.difficulty;
	}
	
	public Block getLastBlock() {
		return blockList.getLast();
	}

    public int getSize() {
        return blockList.size();
    }
  
}
