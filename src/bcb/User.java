package bcb;


/* Import lié à l'execution concurrente */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
/* fin Import execution concurrente */

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
    public void Mine2(int difficulty, Block toMine) {
        int threadCount = Runtime.getRuntime().availableProcessors() + 1;
        // Executor avec ncoeurs+1 threads
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<ComputeBlockResult>> list = new ArrayList<Future<ComputeBlockResult>>();
        Future<ComputeBlockResult> res;
        int nonce = 0;
        List<ComputeBlockHash> tasks = new ArrayList<ComputeBlockHash>();

        // On ajoute des task à la liste
        for (int i = nonce; i < nonce + threadCount; ++i) {
            ComputeBlockHash compute = new ComputeBlockHash(toMine, nonce + i, threadCount, difficulty);
            tasks.add(compute);
        }

        for (ComputeBlockHash task : tasks) {
            task.start();
        }
        // on les executent en parallèle
        try {
            list = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // On prend le résultat du premier fini
        // Traitement des résultat obtenus
        for (Future<ComputeBlockResult> fut : list) {
            try {
                ComputeBlockResult res = fut.get();
                String tempHash = res.getHash();
                // System.out.println("Nonce: " + res.nonce + " Hash : " + tempHash);
                nonce = res.getNonce();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // on vide la liste des taches
        tasks.clear();
        toMine.setNonce(nonce);
    }

    public void Mine(int difficulty, Block toMine) {
        int nonce = 0;
        String tempHash = computeBlockHash(toMine, nonce);
        while (!(tempHash.startsWith("0".repeat(difficulty)))) {
            ++nonce;
            tempHash = computeBlockHash(toMine, nonce);
        }
        toMine.setNonce(nonce);
    }

}

/**
 * Tuple résultat (nonce,hash)
 */
class ComputeBlockResult {
    String hash;
    int nonce;

    public ComputeBlockResult(String hash, int nonce) {
        this.nonce = nonce;
        this.hash = hash;
    }

    public String getHash() {
        return this.hash;
    }

    public int getNonce() {
        return this.nonce;
    }
}

/**
 * classe callable permettant l'exacution en parrallèle
 */
class ComputeBlockHash implements Callable<ComputeBlockResult> {

    // volatile for multi-threaded reasons
    protected volatile int nonce = 0;
    protected volatile Block b;
    protected volatile int offset = 0;
    protected volatile int difficulty = 0;
    // to stop the thread
    private boolean exit;

    public ComputeBlockHash(Block b, int nonce, int difficulty, int offset) {
        this.nonce = nonce;
        this.offset = offset;
        this.difficulty = difficulty;
        this.b = b;
        
    }

    @Override
    public ComputeBlockResult call() {
        String hash = HashUtil.applySha256(
                b.getIndex() + nonce + b.getTimestamp() + b.getMerkelTreeRootHash() + b.getPrevBlockHash());
        while ( !exit && hash.startsWith("0".repeat(difficulty))) {
            nonce += offset;
            hash = HashUtil.applySha256(
                    b.getIndex() + nonce + b.getTimestamp() + b.getMerkelTreeRootHash() + b.getPrevBlockHash());
        }
        return new ComputeBlockResult(hash, nonce);
    }

    public void stop()
    {
        exit = true;
    }

}
