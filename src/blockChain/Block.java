package blockChain;

import java.util.*;

import java.time.*;
import miscUtils.HashUtil;

public class Block {
    int index;
    int nonce;
    int transactionCount;
    String timestamp;
    String hash;
    String merkelTreeRootHash;
    String lastBlockHash;
    ArrayList<String> transactionList;

    public int getIndex() {
        return index;
    }

    public int getNonce() {
        return nonce;
    }

    public int getTransactionCount() {
        return transactionCount;
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

    public String getLastBlockHash() {
        return lastBlockHash;
    }

    public ArrayList<String> getTransactionListList() {
        return transactionList;
    }

    public Block(int index, int transactionCount, String lastBlockHash,
            ArrayList<String> transactionList) {
        this.index = index;
        nonce = 0;
        this.transactionCount = transactionCount;
        this.lastBlockHash = lastBlockHash;
        this.transactionList = transactionList;
        transactionCount = transactionList.size();
        this.timestamp = LocalDateTime.now().toString();
        merkelTreeRootHash = ComputeMerkelTreeRootHash();
    }

    public Block() {
        this.index = 0;
        this.nonce = 0;
        transactionCount = 1;
        transactionList.add("Genesis");
        this.timestamp = LocalDateTime.now().toString();
    }


    private String computeNode(List<String> hashes) {
        int size = hashes.size();
        if (size > 2) {
            String left = computeNode(hashes.subList(0, (int) Math.ceil(size / 2.0f)));
            String right = computeNode(hashes.subList(((int) Math.ceil(size / 2.0f) )+ 1, size));
            return HashUtil.applySha256(left + right);
        } else if (size == 2) {
            return HashUtil.applySha256(hashes.get(0) + hashes.get(1));
        } else {
            return HashUtil.applySha256(hashes.get(0) + hashes.get(0));
        }
    }

    private String ComputeMerkelTreeRootHash(){
        // The number of level in a Merkel tree is the squareroot of
        // the number of transaction
        ArrayList<String> hashes = new ArrayList<String>();

        for (int i = 0; i < transactionCount; ++i) {
            hashes.set(i, HashUtil.applySha256(transactionList.get(i)));
        }
        return computeNode(hashes);
    }

}
