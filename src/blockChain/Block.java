package blockChain;

import java.util.*;

import java.time.*;
import miscUtils.HashUtil;

public class Block {
    int index;
    int nonce;
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
        return transactionList.size();
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

    public Block(int index, String lastBlockHash, ArrayList<String> transactionList) {
        this.index = index;
        nonce = 0;
        this.lastBlockHash = lastBlockHash;
        this.transactionList = transactionList;
        this.timestamp = LocalDateTime.now().toString();
        merkelTreeRootHash = ComputeMerkelTreeRootHash();
    }

    public Block() {
        this.index = 0;
        this.nonce = 0;
        transactionList.add("Genesis");
        this.timestamp = LocalDateTime.now().toString();
    }


    private String ComputeMerkelTreeRootHash() {
        // The number of level in a Merkel tree is the squareroot of
        // the number of transaction
        ArrayList<String> hashes = new ArrayList<String>();

        for (int i = 0; i < transactionList.size(); ++i) {
            hashes.add(HashUtil.applySha256(transactionList.get(i)));
        }
        while (hashes.size() != 1) {
            if ((hashes.size() % 2) != 0) {
                hashes.add(hashes.get(hashes.size() - 1));
            }
            int size = hashes.size();
            for (int i = 0; i < size; i += 2) {
                hashes.set(i, HashUtil.applySha256(hashes.get(i) + hashes.get(i+1)));
            }
            for (int i = size -1 ; i > 0; i -= 2) {
                hashes.remove(i);
            }
        }
        return hashes.get(0);
    }

}
