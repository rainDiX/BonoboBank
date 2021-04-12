package test;

import java.util.ArrayList;
import blockchainUtils.TransactionToolkit;

public class Test {
    public static void main(String[] args) {
        TransactionToolkit txtk = new TransactionToolkit();
        ArrayList<String> txList = new ArrayList<String>();

        //2 tx
        txList.add(txtk.Generator(100));
        txList.add(txtk.Generator(100));
        BlockTest test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        //4 tx
        txList.add(txtk.Generator(100));
        txList.add(txtk.Generator(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        //5 tx
        txList.add(txtk.Generator(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        //8 tx
        txList.add(txtk.Generator(100));
        txList.add(txtk.Generator(100));
        txList.add(txtk.Generator(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
        System.out.println("####################################");

        //9 tx
        txList.add(txtk.Generator(100));
        test = new BlockTest(0, "0", txList);
        test.TestComputeMerkelTreeRootHash();
    }
}
