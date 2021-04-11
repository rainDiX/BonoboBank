package blockchainUtils;

import java.util.*;  


public class Block {
    int index;
    int nonce;
    int transactionCount;
    String timestamp;
    String hash;
    String merkelRootHash;
    String lastBlockHash;
    String currentBlockHash;
    ArrayList<Transaction> transactionList;

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

    public String getMerkelRootHash() {
        return merkelRootHash;
    }

    public String getLastBlockHash() {
        return lastBlockHash;
    }

    public String getCurrentBlockHash() {
        return currentBlockHash;
    }

    public ArrayList<Transaction> getTransactionListList() {
        return transactionList;
    }


}
