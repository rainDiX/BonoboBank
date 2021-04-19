package test;

import java.util.*;
import miscUtils.*;
import blockChain.*;

public class BlockTest extends Block {

    String[] transactionListTest;

    public BlockTest(int index, String lastBlockHash, String[] transactionList) {
        super(index, lastBlockHash, transactionList);
        this.transactionListTest = transactionList;
    }

    private String ComputeMerkelTreeRootHashSize2(List<String> hashes) {
        return HashUtil.applySha256(hashes.get(0) + hashes.get(1));
    }

    private String ComputeMerkelTreeRootHashSize4(List<String> hashes) {
        String left = HashUtil.applySha256(hashes.get(0) + hashes.get(1));
        String right = HashUtil.applySha256(hashes.get(2) + hashes.get(3));

        return HashUtil.applySha256(left + right);
    }

    private String ComputeMerkelTreeRootHashSize5(List<String> hashes) {
        String left = HashUtil.applySha256(hashes.get(0) + hashes.get(1));
        String mid = HashUtil.applySha256(hashes.get(2) + hashes.get(3));
        String right = HashUtil.applySha256(hashes.get(4) + hashes.get(4));

        left = HashUtil.applySha256(left + mid);
        right = HashUtil.applySha256(right + right);

        return HashUtil.applySha256(left + right);
    }

    private String ComputeMerkelTreeRootHashSize8(List<String> hashes) {
        String left = HashUtil.applySha256(hashes.get(0) + hashes.get(1));
        String midleft = HashUtil.applySha256(hashes.get(2) + hashes.get(3));
        String midright = HashUtil.applySha256(hashes.get(4) + hashes.get(5));
        String right = HashUtil.applySha256(hashes.get(6) + hashes.get(7));

        left = HashUtil.applySha256(left + midleft);
        right = HashUtil.applySha256(midright + right);

        return HashUtil.applySha256(left + right);
    }

    private String ComputeMerkelTreeRootHashSize9(List<String> hashes) {
        String left = HashUtil.applySha256(hashes.get(0) + hashes.get(1));
        String midleft = HashUtil.applySha256(hashes.get(2) + hashes.get(3));
        String mid = HashUtil.applySha256(hashes.get(4) + hashes.get(5));
        String midright = HashUtil.applySha256(hashes.get(6) + hashes.get(7));
        String right = HashUtil.applySha256(hashes.get(8) + hashes.get(8));

        left = HashUtil.applySha256(left + midleft);
        mid = HashUtil.applySha256(mid + midright);
        right = HashUtil.applySha256(right + right);

        left = HashUtil.applySha256(left + mid);
        right = HashUtil.applySha256(right + right);

        return HashUtil.applySha256(left + right);
    }

    public void TestComputeMerkelTreeRootHash() {
        // The number of level in a Merkel tree is the squareroot of
        // the number of transaction
        ArrayList<String> hashes = new ArrayList<String>();

        for (int i = 0; i < transactionListTest.length; ++i) {
            hashes.add(HashUtil.applySha256(transactionListTest[i]));
        }
        String merkelrootHash;
        System.out.println("Number of transactions : " + transactionListTest.length);
        switch (transactionListTest.length) {
        case 2:
            merkelrootHash = ComputeMerkelTreeRootHashSize2(hashes);
            break;
        case 4:
            merkelrootHash = ComputeMerkelTreeRootHashSize4(hashes);
            break;
        case 5:
            merkelrootHash = ComputeMerkelTreeRootHashSize5(hashes);
            break;
        case 8:
            merkelrootHash = ComputeMerkelTreeRootHashSize8(hashes);
            break;
        case 9:
            merkelrootHash = ComputeMerkelTreeRootHashSize9(hashes);
            break;
        default:
            merkelrootHash = "";
            System.out.println("No test for " + transactionListTest.length + " transactions");
        }
        System.out.println("MerkelTreeRootHash calculated    : " + super.getMerkelTreeRootHash());
        System.out.println("MerkelTreeRootHash found in test : " + merkelrootHash);
        assert merkelrootHash == this.getMerkelTreeRootHash() : "Wrong hash";
    }

}
