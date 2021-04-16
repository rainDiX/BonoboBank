package test;

import java.util.ArrayList;

import blockchainUtils.Transaction;
import blockchainUtils.TransactionToolkit;
import blockChain.*;

public class Test {

    public static void main(String[] args) {
        transactionToolkitTest();
        merkelTest();
        miningtest();
    }

    public static void transactionToolkitTest() {
        TransactionToolkit txtk = new TransactionToolkit();
        for (int i = 0; i < 200; ++i) {
            String tx = txtk.Generate(100);
            System.out.println(tx);
            Transaction txParsed = txtk.Parse(tx);
            System.out.println(txParsed);
            assert(tx == txParsed.toString());
        }
    }

    public static void miningtest() {
        TransactionToolkit txtk = new TransactionToolkit();
        // test Minage de 100 blocks
        for (int i = 1; i <= 100; ++i) {
            ArrayList<String> txList = new ArrayList<String>();
            for (int j = 0; j < 10; ++j) {
                txList.add(txtk.Generate(100));
            }
            Block b = new BlockTest(i, "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef", txList);
            b.Mine(5);
            System.out.println("Block " + i + " Nonce=" + b.getNonce() + " Hash=" + b.getHash());
        }
    }

    public static void merkelTest() {
        TransactionToolkit txtk = new TransactionToolkit();
        ArrayList<String> txList = new ArrayList<String>();

        // 2 tx
        txList.add(txtk.Generate(100));
        txList.add(txtk.Generate(100));
        BlockTest test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 4 tx
        txList.add(txtk.Generate(100));
        txList.add(txtk.Generate(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 5 tx
        txList.add(txtk.Generate(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 8 tx
        txList.add(txtk.Generate(100));
        txList.add(txtk.Generate(100));
        txList.add(txtk.Generate(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        // 9 tx
        txList.add(txtk.Generate(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");
    }
}
