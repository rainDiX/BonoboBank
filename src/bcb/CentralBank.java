package bcb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.*;

import blockChain.*;
import miscUtils.BCJsonUtils;

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
    private ArrayList<User> users = new ArrayList<User>();

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
     * Récompense attribué lors de l'hélicopter maney, puis au minage
     */
    private long reward;

    /**
     * On divise par 2 la récompense tout les DECREASE_REWARD blocs
     */
    private static final int DECREASE_REWARD = 10;

    /**
     * logger pour gérer les messages
     */
    private static Logger logr = Logger.getLogger(CentralBank.class.getName());

    /**
     * Instancie une CentralBank à partir d'une blockain sauvegardé dans un fichier
     * JSON
     * 
     * @param filename chemin du fichier json
     */
    public CentralBank(String filename) {
        logr.log(Level.INFO, "Tentative de lecture du fichier " + filename);
        this.blockchain = BCJsonUtils.BCJsonReader(filename);
        Iterator<Block> it = blockchain.iterator();
        // bloc 0 : genesis
        it.next();
        // premier bloc (indice 1)
        Transaction transacTemp = txtk.Parse(it.next().getTransactionListList()[0]);
        // on récupère l'initial reward
        this.reward = transacTemp.getMontant();
        this.name = transacTemp.getEmetteur();

        while (it.hasNext()){
            Block block = it.next();
            for (String txStr : block) {/* on parcourt les blocks de la blockchain */
                /*
                 * on vérifie pour chaque transaction qui se trouve dans le bloc, si l'émetteur
                 * et le recepteur sont enregistrés dans la base de données
                 */
                User userTemp1 = new User(
                        txtk.Parse(txStr).getEmetteur(), this);
                User userTemp2 = new User(
                        txtk.Parse(txStr).getRecepteur(), this);

                if (!this.users.contains(userTemp1) && !userTemp1.getName().equals(this.name)) {
                    this.users.add(userTemp1);
                }
                if (!this.users.contains(userTemp2) && !userTemp2.getName().equals(this.name)) {
                    this.users.add(userTemp2);
                }
            }
        }
        logr.log(Level.INFO, "Import de la blockchain de " + filename + "  terminée");
    }

    /**
     * 
     * @param name                 Nom de la banque
     * @param initialReward        récompense initiale de minage
     * @param blockchainDifficulty difficulté de minage
     */
    public CentralBank(String name, long initialReward, int blockchainDifficulty) {
        this.name = name;

        users.add(new User("Creator", this));
        this.reward = initialReward;
        blockchain = new BlockChain(blockchainDifficulty);

        // on définit le loglevel de la console si besoin
        if (Logger.getLogger("").getLevel() == Level.FINE) {
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.FINE);
            logr.addHandler(ch);
        }
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
     * 
     * @return la blockchain
     */
    protected BlockChain getBlockchain() {
        return this.blockchain;
    }

    /**
     * Ajoute un utilisateur
     * 
     * @return le nom de l'utilisateur ajouté
     */
    public String addUser() {
        int index = users.size();
        logr.fine("Ajout de l'utilisateur User" + index);
        users.add(new User("User" + index, this));
        return users.get(index).getName();
    }
    
    /**
     * 
     * @param userName nom de l'utilisateur
     * @return l'utilisateur ou null si il n'existe pas
     */
    public User getUser(String userName) {
        Iterator<User> it = users.iterator();
        while(it.hasNext()) {
            User u = it.next();
            if (u.getName() == userName) {
                return u;
            }
        }
        return null;
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
            logr.log(Level.SEVERE, "getUser() appelé avec l'index " + index + ", nombre de users: " + users.size());
            throw new IndexOutOfBoundsException(index + " > " + users.size());
        }
    }

    /**
     * Créer le block Genesis
     */
    public void genesis() {
        logr.info("# GENESIS #");
        Block blockGenesis = users.get(0).createGenesisBlock();
        blockchain.addBlock(blockGenesis);
        transactionQueue.add(new Transaction(this.name, users.get(0).getName(), reward));
        blockchain.addBlock(mineBlock());
    }

    /**
     * Lance l'helicopter money
     */
    public void helicopterMoney() {
        logr.info("# HELICOPTER MONEY #");
        for (int i = 1; i < users.size(); ++i) {
            transactionQueue.add(new Transaction(this.name, users.get(i).getName(), reward));
        }
        // Choix personnel : la récompense est 2fois moins importante que l'helicopter money
        reward = reward / 2;
        emptyTransactionQueue();
    }

    /**
     * Vide la file de trancaction en créant des nouveaux blocks
     */
    public void emptyTransactionQueue() {
        while (!transactionQueue.isEmpty()) {
            /*
             * tant qu'on a des ressources dans notre liste de transactions de users, on
             * peut donner la taille qu'on veut à la liste de transaction qui va se trouver
             * dans chaque bloc
             */
            Block b = mineBlock();
            blockchain.addBlock(b);
        }
    }

    /**
     * Débute la phase de marché
     * 
     * @param blockCount nombre de bloque à miner avant l'arrêt
     */
    public void mercatoPhase(int blockCount) {
        logr.info("# MARCHÉ #");
        for (int i = 0; i < blockCount; i++) {
            /*
             * maintenant on fait des transactions aléatoires qu'on injecte dans des
             * nouveaux blocs
             */
            int nombreTransac = rng.nextInt(MAX_TRANSAC_PER_BLOC) + 1;
            /* pour cela on va utiliser la queue de transactions */
            for (int j = 0; j < nombreTransac; j++) {
                transactionQueue.add(txtk.Generate(this.users));
            }
            /*
             * une fois des transactions ajoutées on mine un bloc
             */
            Block b = mineBlock();
            blockchain.addBlock(b);
        }
    }

    /**
     * Créer une la liste de transaction à miner et appèle askMinerToMine
     * 
     * @return Block Miné
     */

    public Block mineBlock() {
        if (blockchain.getSize() % DECREASE_REWARD == 0){
            reward = reward/2;
        }
        int tailleTransacListCopy;
        if (reward > 0) {
            // On garde une transaction de libre pour la récompense
            // pas de frais pour les tx en V1
            tailleTransacListCopy = rng.nextInt(MAX_TRANSAC_PER_BLOC - 1) + 1;
        } else {
            tailleTransacListCopy = rng.nextInt(MAX_TRANSAC_PER_BLOC) + 1;
        }

        if (tailleTransacListCopy > transactionQueue.size() && reward == 0) {
            // Si il reste moins d'élément dans la queue on injecte ce qu'il reste
            tailleTransacListCopy = transactionQueue.size();
        } else if (tailleTransacListCopy > transactionQueue.size()) {
            tailleTransacListCopy = transactionQueue.size() + 1;
        }
        String[] transacListCopy = new String[tailleTransacListCopy];
        if (reward > 0) {
            tailleTransacListCopy -= 1;
        }
        for (int i = 0; i < tailleTransacListCopy; i++) {
            // on copie une partie de la grande liste de transaction dans la plus petite
            // pour pouvoir l'injecter dans le bloc
            transacListCopy[i] = transactionQueue.remove().toString();
        }
        Block mined = askMinerToMine(transacListCopy);
        return mined;
    }

    /**
     * Choisit un mineur au hasard et lui demande de miner un block
     * 
     * @return un block miné
     */
    public Block askMinerToMine(String[] transacList) {
        User miner = users.get(rng.nextInt(users.size()));
        logr.fine("Demande de minage à " + miner.getName());
        if (reward > 0) {
            transacList[transacList.length - 1] = new Transaction("coinbase", miner.getName(), reward).toString();
        }
        Block toMine = new Block(blockchain.getSize(), blockchain.getLastBlock().getHash(), transacList);
        miner.MineConcurrent(blockchain.getDifficulty(), toMine);
        logr.info("Block Miné !! : Block n°" + toMine.getIndex() + " Hash: " + toMine.getHash() + " Nonce: "
                + toMine.getNonce());
        return toMine;
    }

    /**
     * Écrit la blockchain dans un fichier au format JSON
     * 
     * @param filename chemin du fichier dans lequel sauvegarder
     */
    public void writeJson(String filename) {
        logr.info("Écriture de la blockchain dans " + filename);
        BCJsonUtils.BCJsonWriter(this.blockchain, filename);
    }
}
