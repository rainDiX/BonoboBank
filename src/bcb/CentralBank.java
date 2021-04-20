package bcb;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import blockChain.*;
import blockchainUtils.*;

public class CentralBank {

    public String name;
    
    private final int MAX_TRANSAC_PER_BLOC =10;

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
        injectTransactionsIntoNewBlock();
    }

    
    public void injectTransactionsIntoNewBlock() {
    	while (transactionQueue.size()>=MAX_TRANSAC_PER_BLOC) { /* tant qu'on a des ressources dans notre liste de 
    	transactions de users, on peut donner la taille qu'on veut à la liste de transaction qui va se trouver dans chaque bloc*/
    		int taille_transac_copy_list=rng.nextInt(MAX_TRANSAC_PER_BLOC);
    		String[] transac_list_copy_=new String[taille_transac_copy_list];
    		for (int i=0;i<taille_transac_copy_list;i++) {/* on copie une partie de la grande liste de transaction dans la plus 
    		petite pour pouvoir l'injecter dans le bloc*/
    			transac_list_copy_[i]=transactionQueue.remove().toString();
    		}
    		Block b = asktoMine(transac_list_copy_);
            blockchain.addBlock(b);
    	}
    	if (!transactionQueue.isEmpty()) {/*  si il reste des éléments dans la grande file de 
    	transactions, on les injecte tous dans un dernier bloc*/
    		String[] transac_list_copy_=new String[transactionQueue.size()];
    		for (int i=0;i<transactionQueue.size();i++) {
    			transac_list_copy_[i]=transactionQueue.remove().toString();
    		}
    		Block b = asktoMine(transac_list_copy_);
            blockchain.addBlock(b);
    	}
    }
    
    
    public Block asktoMine(String[] txList) {
        // un mineur est choisi au hasard
        User miner = users.get(rng.nextInt(users.size()));
        Block toMine = new Block(blockchain.getSize(), blockchain.getLastBlock().getHash(), txList);
        miner.Mine(blockchain.getDifficulty(), toMine);
        return toMine;
    }
}
