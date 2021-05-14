package blockChain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.time.*;
import miscUtils.HashUtil;

/**
 * Block : la structure d'un bloc
 * 
 * <p>
 * Implémente l'interface Iterable permettant d'accéder
 * facilement à toutes les transactions comprise dans le bloc
 * <p>
 * 
 * @author Dijoux Romain
 * @author Guichard Lucas
 */
public class Block implements Iterable<String> {
    private int index;
    private int nonce;
    private String timestamp;
    private String hash;
    private String merkelTreeRootHash;
    private String prevBlockHash;
    private String[] transactionList;

    public int getIndex() {
        return index;
    }

    public int getNonce() {
        return nonce;
    }

    /**
     * Set la nonce d'un bloc
     * <p>
     * ATTENTION, recalcule le hash du bloc quand appelé
     * </p>
     * @param nonce
     */
    public void setNonce(int nonce) {
        this.nonce = nonce;
        this.hash = computeHash();
    }

    public int getTransactionCount() {
        return transactionList.length;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public String getMerkelTreeRootHash() {
        return merkelTreeRootHash;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public String[] getTransactionList() {
        return transactionList;
    }

    /**
     * Constructeur du block
     * <p>
     * initialise un block non miné
     * <p>
     * 
     * @param index           index du block
     * @param prevBlockHash   hash du block précédent
     * @param transactionList list des transactions stocké dans le block
     */
    public Block(int index, String prevBlockHash, String[] transactionList) {
        this.index = index;
        nonce = 0;
        this.prevBlockHash = prevBlockHash;
        this.transactionList = transactionList;
        this.timestamp = LocalDateTime.now().toString();
        merkelTreeRootHash = ComputeMerkelTreeRootHash();
        this.hash = computeHash();
    }

    /**
     * Constructeur du block genesis
     */
    public Block() {
        this.index = 0;
        nonce = 0;
        this.prevBlockHash = "0";
        String[] tx = { "Genesis" };
        this.transactionList = tx;
        this.timestamp = LocalDateTime.now().toString();
        merkelTreeRootHash = ComputeMerkelTreeRootHash();
        this.hash = computeHash();
    }

    private String ComputeMerkelTreeRootHash() throws UnsupportedOperationException {
        // The number of level in a Merkel tree is the squareroot of
        // the number of transaction
        ArrayList<String> hashes = new ArrayList<String>();
        if (transactionList.length == 0) {
            throw new UnsupportedOperationException("Aucune transaction dans le block");
        }
        for (String transac : this) {
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
        return hashes.get(0);
    }

    private String computeHash() {
        return HashUtil.applySha256(index + nonce + timestamp + merkelTreeRootHash + prevBlockHash);
    }

    /**
     * Implémenation de l'itérateur
     */
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int cur = 0;

            @Override
            public boolean hasNext() {
                return (cur < transactionList.length);
            }

            @Override
            public String next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return transactionList[cur++];
            }
            /*
             * On ne va pas implémenter remove() L'implémentation par défaut throw une
             * UnsupportedOperationException
             */
        };
    }

}
