import bcb.CentralBank;


public class Main {
    public static void launchBCB(int difficulty, int userCount, int blockCount, long initialReward) {
        System.out.println("Difficulté du minage             : " + difficulty);
        System.out.println("Nombre d'utilisateur             : " + userCount);
        System.out.println("Nombre de block à miner          : " + blockCount);
        System.out.println("Récompense de l'helicopter money : " + initialReward + " satoBnb");

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

        coinbase.writeJson("./BC.json");
    }

    public static void printUsage() {
        System.out.println("Arguments Valide :");
        System.out.println("-b nombre : nombre de block à miner (10 par défaut)");
        System.out.println("-d difficulté : difficulté du minage (4 par défaut)");
        System.out.println("-r montant : Montant initial en Bnb recu pendant d'helicopter Money (50 Bnb par défaut)");
        System.out.println("-u nb : nombre d'utilisateur (10 par défaut)");
    }

    public static void main(String[] args) {
        int blockCount = 100;
        int difficulty = 4;
        // 50 Bnb
        long initialReward = 5000000000l;
        // nombre d'utilisateur
        int userCount = 10;
        
        for (int i = 0; i < args.length - 1; ++i) {
            switch (args[i]) {
            case "-b":
                try {
                    difficulty = Integer.parseUnsignedInt(args[i + 1]);
                    ++i;
                } catch (NumberFormatException ex) {
                    System.out.println("le nombre de block n'est pas fournis ou invalide");
                    System.out.println("utilisation de la valeur par défaut");
                }
                break;
            case "-d":
                try {
                    difficulty = Integer.parseUnsignedInt(args[i + 1]);
                    ++i;
                } catch (NumberFormatException ex) {
                    System.out.println("la difficulté n'est pas fournis ou invalide");
                    System.out.println("utilisation de la valeur par défaut");
                }
                break;
            case "-r":
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
                try {
                    userCount = Integer.parseUnsignedInt(args[i + 1]);
                    ++i;
                } catch (NumberFormatException ex) {
                    System.out.println("le nombre d'utilisateur n'est pas fournis ou invalide");
                    System.out.println("utilisation de la valeur par défaut");
                }
                break;
            }
        }
        launchBCB(difficulty, userCount, blockCount, initialReward);
    }
}
