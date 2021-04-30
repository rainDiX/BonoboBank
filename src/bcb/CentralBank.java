package bcb;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import blockChain.*;
import miscUtils.BCJsonUtils;


public class CentralBank {

    public String name;
    
    private final int MAX_TRANSAC_PER_BLOC =10;

    private ArrayList<User> users;

    private BlockChain blockchain;

    private Queue<Transaction> transactionQueue = new LinkedList<Transaction>();
    
    private TransactionToolkit txtk = new TransactionToolkit();

    private Random rng = new Random();

    private final long initialReward;
    
    public CentralBank(String filename) {
    	BCJsonUtils.BCJsonWriter(this.blockchain, filename);
    	//premier bloc (indice 1)
    	Transaction transacTemp=txtk.Parse(this.blockchain.getBlockindice(1).getTransactionListList()[0]);
    	//on récupère l'initial reward
    	this.initialReward=transacTemp.getMontant();
    	for (int i=2;i<this.blockchain.getSize();i++) {
    		for (int j=0;j<this.blockchain.getBlockindice(i).getTransactionCount();j++) {/* on parcourt les blocks de la blockchain */
    			/* on vérifie pour chaque transaction qui se trouve dans le bloc, si l'émetteur et le recepteur sont enregistrés dans la base de données */
    			User userTemp1=new User(txtk.Parse(this.blockchain.getBlockindice(1).getTransactionListList()[j]).getEmetteur());
    			User userTemp2=new User(txtk.Parse(this.blockchain.getBlockindice(1).getTransactionListList()[j]).getRecepteur());
    			/* et si ils ne le sont pas, on les ajoute*/
    			if (!users.contains(userTemp1)) {
    				users.add(userTemp1);
    			}
    			if (!users.contains(userTemp2)) {
    				users.add(userTemp2);
    			}
    		}
    		
    	}
    			
    }

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
    		String[] transac_list_copy=new String[taille_transac_copy_list];
    		for (int i=0;i<taille_transac_copy_list;i++) {/* on copie une partie de la grande liste de transaction dans la plus 
    		petite pour pouvoir l'injecter dans le bloc*/
    			transac_list_copy[i]=transactionQueue.remove().toString();
    		}
    		Block b = asktoMine(transac_list_copy);
            blockchain.addBlock(b);
    	}
    	if (!transactionQueue.isEmpty()) {/*  si il reste des éléments dans la grande file de 
    	transactions, on les injecte tous dans un dernier bloc*/
    		String[] transac_list_copy=new String[transactionQueue.size()];
    		for (int i=0;i<transactionQueue.size();i++) {
    			transac_list_copy[i]=transactionQueue.remove().toString();
    		}
    		Block b = asktoMine(transac_list_copy);
            blockchain.addBlock(b);
    	}
    }
    
    public void mercatoPhase(int blockCount) {
    	for (int i=0;i<blockCount;i++) {
    		/* maintenant on fait des transactions aléatoires qu'on injecte dans chaque bloc  */
    		int taille_transac_list=rng.nextInt(MAX_TRANSAC_PER_BLOC);
    		/* pour cela on va utiliser un tableau de transactions*/
    		String[] transac_list_copy=new String[taille_transac_list];
    		for (int j=0;j<taille_transac_list;j++) {
    			transac_list_copy[j]=txtk.Generate(this.users).toString();
    		}
    		/* une fois la liste de transactions pour un bloc terminé, on injecte le tout dans le bloc concerné*/
    		Block b = asktoMine(transac_list_copy);
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
