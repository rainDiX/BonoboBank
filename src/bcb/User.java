package bcb;

import blockChain.Block;
import miscUtils.HashUtil;

public class User {
    /**
     * Portefeuille de l'utilisateur en satoBnb
     */
    private long wallet = 0;

    /**
     * Nom de l'utilisateur
     */
    private String name;

    public User(String name) {
        this.name = name;
    }

    /**
     * Getteur pour le nom de l'utilisateur
     * 
     * @return nom de l'utilisateur
     */
    public String getName() {
        return name;
    }

    /**
     * @return solde du compte en satoBnb
     */
    public long getBalance() {
        return wallet;
    }

    /**
     * @return solde du compte en Bnb
     */
    public int getBalanceBnb() {
        return (int) (wallet / 100000000);
    }

    protected Block createGenesisBlock() {
        String[] tx = { "Genesis" };
        Block genesis = new Block(0, "0", tx);
        return genesis;
    }

    private String computeBlockHash(Block b, int nonce) {
        return HashUtil.applySha256(
                b.getIndex() + nonce + b.getTimestamp() + b.getMerkelTreeRootHash() + b.getPrevBlockHash());
    }

    /**
     * Mine le block
     * 
     * @param difficulty difficulté du minage
     * @param toMine     block à Miner
     */
    public void Mine(int difficulty, Block toMine) {
        int nonce = 0;
        String tempHash = computeBlockHash(toMine, nonce);
        while (!(tempHash.startsWith("0".repeat(difficulty)))) {
            ++nonce;
            tempHash = computeBlockHash(toMine, nonce);
        }
        toMine.setNonce(nonce);
    }

    @Override
    public boolean equals(Object obj) {
        // On compare avec lui-même
        if (obj == this) {
            return true;
        }
        // Si ce n'est pas un user retourne faux
        if (!(obj instanceof User)) {
            return false;
        }

        User user = (User) obj;
        return user.getName().equals(this.name);
    }

}
