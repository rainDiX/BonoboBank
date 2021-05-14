package blockChain;

import miscUtils.HashUtil;
import java.util.ArrayList;
import java.util.Iterator;

/**
* BlockChainCheck regroupe les tests de validité 1 et 2
* @author Dijoux Romain
* 
*/
public class BlockChainCheck {
    /**
     * Vérifie si le hash d'un bloc est correct
     * @param b bloc
     * @return
     */
    public static boolean CheckBlockHash(Block b) {
        String blockHash = HashUtil.applySha256(
                b.getIndex() + b.getNonce() + b.getTimestamp() + b.getMerkelTreeRootHash() + b.getPrevBlockHash());
        return blockHash.equals(b.getHash());
    }

    /**
     * vérifie si l'arbre de merkel d'un bloc est correct
     * @param b bloc
     * @return
     */
    public static boolean CheckBlockMerkelTree(Block b) {
        ArrayList<String> hashes = new ArrayList<String>();
        if (b.getTransactionCount() == 0) {
            throw new UnsupportedOperationException("Aucune transaction dans le block");
        }
        for (String transac : b) {
            hashes.add(HashUtil.applySha256(transac));
        }
        while (hashes.size() != 1) {
            if ((hashes.size() % 2) != 0) {
                hashes.add(hashes.get(hashes.size() - 1));
            }
            int size = hashes.size();
            for (int i = 0; i < size; i += 2) {
                hashes.set(i, HashUtil.applySha256(hashes.get(i) + hashes.get(i + 1)));
            }
            for (int i = size - 1; i > 0; i -= 2) {
                hashes.remove(i);
            }
        }
        return hashes.get(0).equals(b.getMerkelTreeRootHash());
    }

    /**
     * Vérifie le chainage et les blocs d'une blockchain
     * @param bc blockchain
     * @return
     */
    public static boolean Check(BlockChain bc) {
        String prevBlockHash = "";
        boolean valid = true;
        Iterator<Block> it = bc.iterator();

        while (valid && it.hasNext()) {
            Block b = it.next();
            if (b.getIndex() == 0) {
                valid = valid && b.getTransactionList().length == 1 && b.getTransactionList()[0].equals("Genesis");
            } else {
                valid = valid && b.getPrevBlockHash().equals(prevBlockHash);
            }
            valid = valid && CheckBlockHash(b) && CheckBlockMerkelTree(b);
            prevBlockHash = b.getHash();
        }
        return valid;
    }
}
