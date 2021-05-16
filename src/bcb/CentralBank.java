package bcb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;
import java.util.logging.*;

import blockChain.*;
import miscUtils.BCJsonUtils;

/**
* CentralBank regroupe toutes les fonctions monétaires
* <p>
* C'est elle qui gère :
* - la phase d'helicopter money
* - la distribution des récompense de minage
* - la phase de marché
* C'est ici que réside la file de transaction à traiter.
* Elle garde aussi une liste des utilisateurs/mineurs.
* Elle implémente l'interface Iterable permettant d'accéder
* facilement à toutes les transactions comprises dans les
* blocs de la blockchain.
* </p>
* @author Dijoux Romain
* @author Guichard Lucas
* 
*/

public class CentralBank implements Iterable<Transaction> {
    /**
     * nom de la banque
     */
    public String name;

    /**
     * Utilisateurs/mineurs de la blockchain
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
        Transaction transacTemp = txtk.Parse(it.next().getTransactionList()[0]);
        // on calcul le reward à partir de l'initial reward
        this.reward = transacTemp.getMontant() / (long) Math.pow(2, 1 + (blockchain.getSize() / DECREASE_REWARD));
        this.name = transacTemp.getEmetteur();
        // on récupère Creator
        this.users.add(new User(transacTemp.getRecepteur(), this));

        while (it.hasNext()) {
            Block block = it.next();
            for (String txStr : block) {/* on parcourt les blocks de la blockchain */
                /*
                 * on vérifie pour chaque transaction qui se trouve dans le bloc, si l'émetteur
                 * et le recepteur sont enregistrés dans la base de données
                 */
                User userTemp1 = new User(txtk.Parse(txStr).getEmetteur(), this);
                User userTemp2 = new User(txtk.Parse(txStr).getRecepteur(), this);

                if (!this.users.contains(userTemp1) && !userTemp1.getName().equals(this.name)) {
                    this.users.add(userTemp1);
                }
                if (!this.users.contains(userTemp2) && !userTemp2.getName().equals(this.name)) {
                    this.users.add(userTemp2);
                }
            }
        }

        // on définit le loglevel de la console si besoin
        if (Logger.getLogger("").getLevel() == Level.FINE) {
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.FINE);
            logr.addHandler(ch);
        }
        logr.log(Level.INFO, "Import de la blockchain de " + filename + "  terminée");
    }

    /**
     * 
     * @param name                 Nom de la banque
     * @param initialReward        récompense initiale de minage
     * @param blockchainDifficulty difficulté de minage
     */
    public CentralBank(String name, long initialReward, int blockchainDifficulty, int transacLimit) {
        this.name = name;

        users.add(new User("Creator", this));
        this.reward = initialReward;
        blockchain = new BlockChain(blockchainDifficulty, transacLimit);

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
     * @param name nom donné à l'utilisateur
     * @return l'utilisateur ajouté
     */
    public String addUser(String name) {
        int index = users.size();
        String append = "@" + Integer.toHexString(index);
        logr.fine("Ajout de l'utilisateur " + name + append);
        users.add(new User(name + append, this));
        return users.get(index).getName();
    }

    /**
     * 
     * @param userName nom de l'utilisateur
     * @return l'utilisateur ou null si il n'existe pas
     */
    public User getUser(String userName) {
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (u.getName().equals(userName)) {
                return u;
            }
        }
        logr.fine(userName + " non trouvé");
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
     * 
     * @return la blockchain
     */
    public BlockChain getBlockChain() {
        return this.blockchain;
    }

    /**
     * Créer le block Genesis
     */
    public void genesis() {
        logr.info("# GENESIS #");
        Block blockGenesis = users.get(0).createGenesisBlock();
        blockchain.addBlock(blockGenesis);
        // envoie de 50 Bnb à Creator
        transactionQueue.add(new Transaction(this.name, users.get(0).getName(), reward));
    }

    /**
     * Lance l'helicopter money
     */
    public void helicopterMoney() {
        logr.info("# HELICOPTER MONEY #");
        for (int i = 1; i < users.size(); ++i) {
            transactionQueue.add(new Transaction(this.name, users.get(i).getName(), reward));
        }
        // Choix personnel : la récompense est 2fois moins importante que l'helicopter
        // money
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
            int nombreTransac = rng.nextInt(this.getBlockChain().getTransacLimit()) + 1;
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
        if (blockchain.getSize() % DECREASE_REWARD == 0) {
            reward = reward / 2;
        }
        int tailleTransacListCopy = rng.nextInt(this.getBlockChain().getTransacLimit()) + 1;

        if (tailleTransacListCopy > transactionQueue.size()) {
            // Si il reste moins d'élément dans la queue on injecte ce qu'il reste
            tailleTransacListCopy = transactionQueue.size();
        }
        // On prévoit de la place pour la tx de récompense
        if (reward > 0 && tailleTransacListCopy == 1) {
            tailleTransacListCopy++;
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

    /**
     * Iterateur permettant d'accéder directement à toutes les transactions
     */
    @Override
    public Iterator<Transaction> iterator() {
        return new Iterator<Transaction>() {
            private Iterator<Block> blkIt = blockchain.iterator();
            private Iterator<String> txIt = blkIt.next().iterator();

            @Override
            public boolean hasNext() {
                return (txIt.hasNext() || blkIt.hasNext());
            }

            @Override
            public Transaction next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                if (!txIt.hasNext()) {
                    txIt = blkIt.next().iterator();
                }
                String txString = txIt.next();
                // On passe le bloc génésis
                if (txtk.isGenesis(txString)) {
                    txIt = blkIt.next().iterator();
                    txString = txIt.next();
                }
                return txtk.Parse(txString);
            }
            /*
             * On ne va pas implémenter remove() L'implémentation par défaut throw une
             * UnsupportedOperationException
             */
        };
    }
}
