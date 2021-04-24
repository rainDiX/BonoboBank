package bcb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import blockChain.*;

public class CentralBank {
    /**
     * nom de la banque
     */
    public String name;

    /**
     * Nombre maximum de transaction par block
     */
    private final int MAX_TRANSAC_PER_BLOC = 10;

    /**
     * Utilisateur/mineurs de la blockchain
     */
    private ArrayList<User> users;

    /**
     * La blockchain
     */
    private BlockChain blockchain;

    /**
     * Queue de transaction
     * <p>
     * Les mineurs vont piocher dans cette liste pour miner des blocks
     * <p>
     */
    private Queue<Transaction> transactionQueue = new LinkedList<Transaction>();

    /**
     * Contient une méthode pour génerer des transactions aléatoire
     */
    private TransactionToolkit txtk = new TransactionToolkit();

    /**
     * Générateur de nombre aléatoire
     */
    private Random rng = new Random();

    /**
     * Récompense attribué lors de l'hélicopter maney
     */
    private final long initialReward;

    /**
     * 
     * @param name                 Nom de la banque
     * @param initialReward        récompense de l'helicapter money
     * @param blockchainDifficulty difficulté de minage
     */
    public CentralBank(String name, long initialReward, int blockchainDifficulty) {
        this.name = name;
        users = new ArrayList<User>();
        users.add(new User("Creator"));
        this.initialReward = initialReward;
        blockchain = new BlockChain(blockchainDifficulty);
    }

    /**
     * @return le nombre d'utilisateur
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * 
     * @return le nom de la banque
     */
    public String getName() {
        return name;
    }

    /**
     * Ajoute un utilisateur
     * 
     * @return l'index de l'utilisateur ajouté
     */
    public int addUser() {
        int index = users.size();
        users.add(new User("User" + index));
        return index;
    }

    /**
     * 
     * @param index index de l'utilisateur
     * @return utilisateur demandé
     * @throws IndexOutOfBoundsException
     */
    public User getUser(int index) throws IndexOutOfBoundsException {
        if (index < users.size()) {
            return users.get(index);
        } else {
            throw new IndexOutOfBoundsException(index + " > " + users.size());
        }
    }

    /**
     * Créer le block Genesis
     */
    public void genesis() {
        Block blockGenesis = users.get(0).createGenesisBlock();
        blockchain.addBlock(blockGenesis);
        String[] firstTx = { (new Transaction(this.name, users.get(0).getName(), initialReward)).toString() };
        asktoMine(firstTx);
    }

    /**
     * Lance l'helicopter money
     */
    public void helicopterMoney() {
        for (int i = 1; i < users.size(); ++i) {
            transactionQueue.add(new Transaction(this.name, users.get(i).getName(), initialReward));
        }
        injectTransactionsIntoNewBlock();
    }

    /** 
     * Vide la file de trancaction en créant des nouveaux blocks
     */
    public void injectTransactionsIntoNewBlock() {
        while (transactionQueue
                .size() >= MAX_TRANSAC_PER_BLOC) { /*
                                                    * tant qu'on a des ressources dans notre liste de transactions de
                                                    * users, on peut donner la taille qu'on veut à la liste de
                                                    * transaction qui va se trouver dans chaque bloc
                                                    */
            int taille_transac_copy_list = rng.nextInt(MAX_TRANSAC_PER_BLOC) + 1;
            String[] transac_list_copy = new String[taille_transac_copy_list];
            for (int i = 0; i < taille_transac_copy_list; i++) {/*
                                                                 * on copie une partie de la grande liste de transaction
                                                                 * dans la plus petite pour pouvoir l'injecter dans le
                                                                 * bloc
                                                                 */
                transac_list_copy[i] = transactionQueue.remove().toString();
            }
            Block b = asktoMine(transac_list_copy);
            blockchain.addBlock(b);
        }
        if (!transactionQueue.isEmpty()) {/*
                                           * si il reste des éléments dans la grande file de transactions, on les
                                           * injecte tous dans un dernier bloc
                                           */
            String[] transac_list_copy = new String[transactionQueue.size()];
            for (int i = 0; i < transac_list_copy.length; i++) {
                transac_list_copy[i] = transactionQueue.remove().toString();
            }
            Block b = asktoMine(transac_list_copy);
            blockchain.addBlock(b);
        }
    }

    public void mercatoPhase(int blockCount) {
        for (int i = 0; i < blockCount; i++) {
            /*
             * maintenant on fait des transactions aléatoires qu'on injecte dans chaque bloc
             */
            int taille_transac_list = rng.nextInt(MAX_TRANSAC_PER_BLOC) + 1;
            /* pour cela on va utiliser un tableau de transactions */
            String[] transac_list_copy = new String[taille_transac_list];
            for (int j = 0; j < taille_transac_list; j++) {
                transac_list_copy[j] = txtk.Generate(this.users).toString();
            }
            /*
             * une fois la liste de transactions pour un bloc terminé, on injecte le tout
             * dans le bloc concerné
             */
            Block b = asktoMine(transac_list_copy);
            blockchain.addBlock(b);
        }
    }

    /**
     * Choisit un mineur au hasard et lui demande de miner un block
     * @param txList les transactions du block
     * @return un block miné
     */
    public Block asktoMine(String[] txList) {
        User miner = users.get(rng.nextInt(users.size()));
        Block toMine = new Block(blockchain.getSize(), blockchain.getLastBlock().getHash(), txList);
        miner.Mine(blockchain.getDifficulty(), toMine);
        return toMine;
    }
}
