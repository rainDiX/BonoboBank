package blockChain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Blockchain regroupe la liste de block et les fonctions associées
 *
 * <p>
 * La blockChain est une liste chainée de block associé à une difficulté de minage.
 * Elle implémente l'interface Iterable qui permet de faire des foreach et ainsi
 * à parcourir linéairement les blocs de la blockchain.
 * </p>
 * @author Dijoux Romain
 * @author Guichard Lucas
 */

public class BlockChain implements Iterable<Block> {
    /**
     * Difficulté de la blockchain, içi le nombre de 0 par lequel commence le hash
     * d'un bloc
     */
    private int difficulty;
    /**
     * Liste de blocs
     */
    private LinkedList<Block> blockList;

    /**
     * Constructeur de la blockchain
     * <p>
     * initialise une blockchain vide
     * <p>
     * 
     * @param difficulty      index du block
     */
    public BlockChain(int difficulty) {
        this.difficulty = difficulty;
        this.blockList = new LinkedList<Block>();
    }

    /**
     * Ajoute un bloc à la blockchain.
     * <p>
     * Le chainage sera vérifié avant d'ajouter le bloc
     * </p>
     * @param b bloc à ajouter
     * @return true si le bloc à été ajouté avec succès
     */
    public boolean addBlock(Block b) {
        if (blockList.isEmpty() && b.getIndex() == 0) {
            blockList.add(b);
            return true;
        } else if (b.getHash().startsWith("0".repeat(this.difficulty))
                && b.getPrevBlockHash() == blockList.getLast().getHash() && b.getIndex() == blockList.size()) {
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

    public Block getBlockAtIndex(int index) {
        return blockList.get(index);
    }

    /**
     * Implémentation de l'itérateur pour l'interface iterable
     */
    @Override
    public Iterator<Block> iterator() {
        // return blockList.iterator();
        return new Iterator<Block>() {
            private int cur = 0;

            @Override
            public boolean hasNext() {
                return (cur < blockList.size());
            }

            @Override
            public Block next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return blockList.get(cur++);
            }
            /*
             * On ne va pas implémenter remove() L'implémentation par défaut throw une
             * UnsupportedOperationException
             */
        };
    }

}
