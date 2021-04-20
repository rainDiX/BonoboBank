package bcb;

public class Main {
    public static void main(String[] args) {

        // TODO Prendre les args de la ligne de commande
        int difficulty = 4;
        // 50 Bnb
        long initialReward = 5000000000l;
        // nombre d'utilisateur
        int userCount = 10;

        CentralBank coinbase = new CentralBank("coinbase", initialReward, difficulty);
        // Genesis
        coinbase.genesis();
        // Ajout de N utilisateurs
        for (int i = 1; i <= userCount; ++i) {
            coinbase.addUser();
        }
        // Helicopter Money
        coinbase.helicopterMoney();

        // Phase de marché : Creation de la file de transactions
        // Un mineur choisi au hasard créer le bloc
        // boucle ??

        // arret
    }
}
