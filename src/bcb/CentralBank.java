package bcb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import blockChain.*;
import blockchainUtils.*;

public class CentralBank {

    public String name;

    private ArrayList<User> users;

    private BlockChain blockchain;

    private Queue<Transaction> transactionQueue = new LinkedList<Transaction>();

    //private TransactionToolkit txtk = new TransactionToolkit();

    private Random rng = new Random();

    private final long initialReward;

    public CentralBank(String name, long initialReward ,int blockchainDifficulty) {
        this.name = name;
        users = new ArrayList<User>();
        users.add(new User("Creator"));
        this.initialReward = initialReward;
        blockchain = new BlockChain(blockchainDifficulty);
    }

    public int getUserCount() {
        return users.size();
    }

    public String getName() {
        return name;
    }

    public void addUser() {
        int index = users.size();
        users.add(new User("User" + index));
    }

    public void genesis() {
        Block blockGenesis = users.get(0).createGenesisBlock();
        blockchain.addBlock(blockGenesis);
        String[] firstTx = { (new Transaction(this.name, users.get(0).getName(), initialReward)).toString() };
        asktoMine(firstTx);
    }

    public void helicopterMoney() {
        for (int i = 1; i < users.size(); ++i ) {
            transactionQueue.add(new Transaction(this.name, users.get(i).getName(), initialReward));
        }
        while (transactionQueue.size() > 0) {
            transactionQueue.remove();
        }
    }

    public void asktoMine(String[] txList) {
        // un mineur est choisi au hasard
        User miner = users.get(rng.nextInt(users.size()));
        Block toMine = new Block(blockchain.getSize(), "TODO", txList);
        miner.Mine(blockchain.getDifficulty(), toMine);
    }
}
