
import java.util.logging.*;

import bcb.CentralBank;

public class Main {

    public static void launchBCB(int difficulty, int userCount, int blockCount, long initialReward, boolean saving,
            String filename) {
        Logger logr = Logger.getLogger("launchBCB");
        logr.info("difficulté du minage: " + difficulty);
        logr.info("Nombre d'utilisateur: " + userCount);
        logr.info("Nombre de block à miner: " + blockCount);
        logr.info("Récompense de l'helicopter money: " + initialReward + " satoBnb");
        logr.info("Récompense de l'helicopter money: " + initialReward / 100000000l + " Bnb");

        CentralBank coinbase = new CentralBank("coinbase", initialReward, difficulty);
        // Genesis
        coinbase.genesis();
        // Ajout de N utilisateurs
        for (int i = 1; i <= userCount; ++i) {
            coinbase.addUser();
        }
        // Helicopter Money
        coinbase.helicopterMoney();
        // Phase de marché :
        coinbase.mercatoPhase(blockCount);

        if (saving) {
            coinbase.writeJson(filename);
        }
    }

    public static void openBCB(String filename) {
        CentralBank bcb = new CentralBank(filename);
        System.out.println(bcb.getName());
    }

    public static void printUsage() {
        System.out.println("Arguments Valide :");
        System.out.println("-b nombre : nombre de block à miner (10 par défaut)");
        System.out.println("-d difficulté : difficulté du minage (4 par défaut)");
        System.out.println("-r montant : Montant initial en Bnb recu pendant d'helicopter Money (50 Bnb par défaut)");
        System.out.println("-u nb : nombre d'utilisateur (10 par défaut)");
        System.out.println("-f fichier.json : fichier contenant une blockchain");
        System.out.println("-s fichier.json : fichier où sauvegarder la blockchain à la fin");
    }

    public static void main(String[] args) throws Exception {
        boolean opening = false;
        boolean saving = false;
        boolean helpflag = false;
        String filename = "";
        int blockCount = 100;
        int difficulty = 4;
        // 50 Bnb
        long initialReward = 5000000000l;
        // nombre d'utilisateur
        int userCount = 10;

        Logger rootLogr = Logger.getLogger("");
        rootLogr.setLevel(Level.SEVERE);

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-b":
                    if (i + 1 == args.length) {
                        printUsage();
                        throw new Exception("Argument manquant");
                    }
                    try {
                        blockCount = Integer.parseUnsignedInt(args[i + 1]);
                        ++i;
                    } catch (NumberFormatException ex) {
                        System.out.println("le nombre de block n'est pas fournis ou invalide");
                        System.out.println("utilisation de la valeur par défaut");
                    }
                    break;
                case "-d":
                    if (i + 1 == args.length) {
                        printUsage();
                        throw new Exception("Argument manquant");
                    }
                    try {
                        difficulty = Integer.parseUnsignedInt(args[i + 1]);
                        ++i;
                    } catch (NumberFormatException ex) {
                        System.out.println("la difficulté n'est pas fournis ou invalide");
                        System.out.println("utilisation de la valeur par défaut");
                    }
                    break;
                case "-r":
                    if (i + 1 == args.length) {
                        printUsage();
                        throw new Exception("Argument manquant");
                    }
                    try {
                        int montantBnb = Integer.parseUnsignedInt(args[i + 1]);
                        initialReward = montantBnb * 100000000l;
                        ++i;
                    } catch (NumberFormatException ex) {
                        System.out.println("le montant n'est pas fournis ou invalide");
                        System.out.println("utilisation de la valeur par défaut");
                    }
                    break;
                case "-u":
                    if (i + 1 == args.length) {
                        printUsage();
                        throw new Exception("Argument manquant");
                    }
                    try {
                        userCount = Integer.parseUnsignedInt(args[i + 1]);
                        ++i;
                    } catch (NumberFormatException ex) {
                        System.out.println("le nombre d'utilisateur n'est pas fournis ou invalide");
                        System.out.println("utilisation de la valeur par défaut");
                    }
                    break;
                case "-s":
                    if (i + 1 == args.length) {
                        printUsage();
                        throw new Exception("Argument manquant");
                    }
                    filename = args[i + 1];
                    saving = true;
                    ++i;
                    break;
                case "-f":
                    if (i + 1 == args.length) {
                        printUsage();
                        throw new Exception("Argument manquant");
                    }
                    filename = args[i + 1];
                    opening = true;
                case "-v":
                    rootLogr.setLevel(Level.INFO);
                    break;
                case "-vv":
                    rootLogr.setLevel(Level.FINE);
                    break;
            }
        }
        if (helpflag || (opening && saving)) {
            printUsage();
        } else if (opening) {
            openBCB(filename);
        } else {
            launchBCB(difficulty, userCount, blockCount, initialReward, saving, filename);
        }
    }
}
