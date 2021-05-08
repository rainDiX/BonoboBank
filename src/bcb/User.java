package bcb;

/* Import lié à l'execution concurrente */
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
/* fin Import execution concurrente */

import blockChain.Block;
import miscUtils.HashUtil;

public class User {

    /**
     * Nom de l'utilisateur
     */
    private String name;

    private CentralBank bank;

    public User(String name, CentralBank bank) {
        this.name = name;
        this.bank = bank;
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
     * @return solde du compte en satoBnb (O(n))
     */
    public long getBalance() {
        long balance = 0;
        for (Transaction tx : bank) {
            if (tx.getEmetteur().equals(this.name)) {
                balance -= tx.getMontant();
            } else if (tx.getRecepteur().equals(this.name)) {
                balance += tx.getMontant();
            }
        }
        return balance;
    }

    /**
     * @return solde du compte en Bnb (O(n))
     */
    public long getBalanceBnb() {
        return (this.getBalance() / 100000000);
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

    /**
     * Mine le block avec plusieur threads
     * 
     * @param difficulty difficulté du minage
     * @param toMine     block à Miner
     */
    public void MineConcurrent(int difficulty, Block toMine) {
        boolean finished = false;
        int nonce = 0;
        int threadCount = Runtime.getRuntime().availableProcessors();
        // Executor avec ncoeurs threads
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executor);

        // On lance des tasks en parallele
        for (int i = 0; i < threadCount; ++i) {
            ComputeBlockHash compute = new ComputeBlockHash(toMine, nonce + i, difficulty, threadCount);
            completionService.submit(compute);
        }
        // on prends le 1er resultat arrivé
        while (!finished) {
            try {
                Future<Integer> future = completionService.poll();
                if (future != null) {
                    int firstResult = future.get();
                    nonce = firstResult;
                    finished = true;
                }
            } catch (NullPointerException | InterruptedException | ExecutionException ex) {
                System.err.println(ex.getMessage());
            }
        }
        // On quitte tout les threads restant
        executor.shutdownNow();
        toMine.setNonce(nonce);
    }

}

/**
 * classe callable permettant l'execution en parrallèle
 */
class ComputeBlockHash implements Callable<Integer> {
    protected int nonce = 0;
    protected Block b;
    protected int offset = 0;
    protected int difficulty = 0;

    public ComputeBlockHash(Block b, int nonce, int difficulty, int offset) {
        this.nonce = nonce;
        this.offset = offset;
        this.difficulty = difficulty;
        this.b = b;
    }

    @Override
    public Integer call() throws Exception {
        String hash = HashUtil.applySha256(
                b.getIndex() + nonce + b.getTimestamp() + b.getMerkelTreeRootHash() + b.getPrevBlockHash());
        while (!hash.startsWith("0".repeat(difficulty))) {
            // On arrête tout si le thread est interrompu
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            nonce += offset;
            hash = HashUtil.applySha256(
                    b.getIndex() + nonce + b.getTimestamp() + b.getMerkelTreeRootHash() + b.getPrevBlockHash());
        }
        return nonce;
    }
}
