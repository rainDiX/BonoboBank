package test;

import blockchainUtils.Transaction;
import blockchainUtils.TransactionToolkit;
import bcb.User;
import blockChain.*;

public class Test {

    public static void main(String[] args) {
        transactionToolkitTest();
        merkelTest();
        miningtest(4);
    }

    public static void transactionToolkitTest() {
        TransactionToolkit txtk = new TransactionToolkit();
        for (int i = 0; i < 200; ++i) {
            String tx = txtk.Generate(100);
            System.out.println(tx);
            Transaction txParsed = txtk.Parse(tx);
            System.out.println(txParsed);
            assert (tx == txParsed.toString());
        }
    }

    public static void miningtest(int difficulty) {
        TransactionToolkit txtk = new TransactionToolkit();
        User u = new User("Test User");
        // test Minage de 100 blocks
        for (int i = 1; i <= 100; ++i) {
            String[] txList = new String[10];
            for (int j = 0; j < 10; ++j) {
                txList[j] = txtk.Generate(100);
            }
            Block b = new BlockTest(i, "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef", txList);
            u.Mine(difficulty,b);
            System.out.println("Block " + i + " Nonce=" + b.getNonce() + " Hash=" + b.getHash());
        }
    }

    public static void merkelTest() {
        TransactionToolkit txtk = new TransactionToolkit();
        String[] txList;

        // 2 tx
        txList = new String[2];
        for (int i = 0; i < 2; ++i) {
            txList[i] = (txtk.Generate(100));
        }
        BlockTest test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 4 tx
        txList = new String[4];
        for (int i = 0; i < 4; ++i) {
            txList[i] = (txtk.Generate(100));
        }
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 5 tx
        txList = new String[5];
        for (int i = 0; i < 5; ++i) {
            txList[i] = (txtk.Generate(100));
        }
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 8 tx
        txList = new String[8];
        for (int i = 0; i < 8; ++i) {
            txList[i] = (txtk.Generate(100));
        }
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 9 tx
        txList = new String[9];
        for (int i = 0; i < 9; ++i) {
            txList[i] = (txtk.Generate(100));
        }
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");
    }
}
