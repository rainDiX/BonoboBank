package bcb;

import java.util.ArrayList;
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
        // premier bloc (indice 1)
        Transaction transacTemp = txtk.Parse(this.blockchain.getBlockAtIndex(1).getTransactionListList()[0]);
        // on récupère l'initial reward
        this.initialReward = transacTemp.getMontant();

        users = new ArrayList<User>();

        for (int i = 2; i < this.blockchain.getSize(); i++) {
            for (int j = 0; j < this.blockchain.getBlockAtIndex(i)
                    .getTransactionCount(); j++) {/* on parcourt les blocks de la blockchain */
                /*
                 * on vérifie pour chaque transaction qui se trouve dans le bloc, si l'émetteur
                 * et le recepteur sont enregistrés dans la base de données
                 */
                User userTemp1 = new User(
                        txtk.Parse(this.blockchain.getBlockAtIndex(1).getTransactionListList()[j]).getEmetteur());
                User userTemp2 = new User(
                        txtk.Parse(this.blockchain.getBlockAtIndex(1).getTransactionListList()[j]).getRecepteur());
                /* et si ils ne le sont pas, on les ajoute */
                if (!users.contains(userTemp1)) {
                    users.add(userTemp1);
                }
                if (!users.contains(userTemp2)) {
                    users.add(userTemp2);
                }
            }
        }

    }

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
     * Ajoute un utilisateur
     * 
     * @return l'index de l'utilisateur ajouté
     */
    public int addUser() {
        int index = users.size();
        logr.fine("Ajout de l'utilisateur User" + index);
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
        transactionQueue.add(new Transaction(this.name, users.get(0).getName(), initialReward));
        blockchain.addBlock(asktoMine());
    }

    /**
     * Lance l'helicopter money
     */
    public void helicopterMoney() {
        logr.info("# HELICOPTER MONEY #");
        for (int i = 1; i < users.size(); ++i) {
            transactionQueue.add(new Transaction(this.name, users.get(i).getName(), initialReward));
        }
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
            Block b = asktoMine();
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
            Block b = asktoMine();
            blockchain.addBlock(b);
        }
    }

    /**
     * Choisit un mineur au hasard et lui demande de miner un block
     * 
     * @return un block miné
     */
    public Block asktoMine() {
        User miner = users.get(rng.nextInt(users.size()));
        logr.fine("Demande de minage à " + miner.getName());

        int tailleTransacListCopy = rng.nextInt(MAX_TRANSAC_PER_BLOC) + 1;
        if (tailleTransacListCopy > transactionQueue.size()) {
            // Si il reste moins d'élément dans la queue on injecte ce qu'il reste
            tailleTransacListCopy = transactionQueue.size();
        }
        String[] transacListCopy = new String[tailleTransacListCopy];
        for (int i = 0; i < tailleTransacListCopy; i++) {
            // on copie une partie de la grande liste de transaction dans la plus petite
            // pour pouvoir l'injecter dans le bloc
            transacListCopy[i] = transactionQueue.remove().toString();
        }
        Block toMine = new Block(blockchain.getSize(), blockchain.getLastBlock().getHash(), transacListCopy);
        miner.Mine2(blockchain.getDifficulty(), toMine);
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
