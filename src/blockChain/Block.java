package blockChain;

import java.util.ArrayList;

import java.time.*;
import miscUtils.HashUtil;

public class Block {
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

    public String[] getTransactionListList() {
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

    private String ComputeMerkelTreeRootHash() {
        // The number of level in a Merkel tree is the squareroot of
        // the number of transaction
        ArrayList<String> hashes = new ArrayList<String>();

        for (int i = 0; i < transactionList.length; ++i) {
            hashes.add(HashUtil.applySha256(transactionList[i]));
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

}
