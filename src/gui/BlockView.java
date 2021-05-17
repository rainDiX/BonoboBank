package gui;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;

import blockChain.Block;
import blockChain.BlockChainCheck;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;

import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BlockView extends JDialog {

	private DefaultListModel<String> ltx;
	private Block block;

	/**
	 * Create the frame.
	 */
	public BlockView(Block b) {
		this.block = b;
		setPreferredSize(new Dimension(530, 500));
		setMinimumSize(new Dimension(530, 400));
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/ressources/000.png")));
		setTitle("Block " + block.getIndex());
		setBounds(100, 100, 530, 552);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblIndex = new JLabel("Index: ");
		springLayout.putConstraint(SpringLayout.WEST, lblIndex, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblIndex);
		
		JLabel labelIndexN = new JLabel("0");
		springLayout.putConstraint(SpringLayout.WEST, labelIndexN, 10, SpringLayout.WEST, getContentPane());
		labelIndexN.setFont(new Font("Dialog", Font.PLAIN, 12));
		labelIndexN.setText(Integer.toString(block.getIndex()));
		getContentPane().add(labelIndexN);
		
		JLabel lblNonce= new JLabel("Nonce:");
		springLayout.putConstraint(SpringLayout.NORTH, lblNonce, 42, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNonce, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, labelIndexN, -6, SpringLayout.NORTH, lblNonce);
		getContentPane().add(lblNonce);
		
		JLabel lblNonceN = new JLabel("0");
		springLayout.putConstraint(SpringLayout.WEST, lblNonceN, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblNonceN, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, labelIndexN, 0, SpringLayout.EAST, lblNonceN);
		springLayout.putConstraint(SpringLayout.NORTH, lblNonceN, 6, SpringLayout.SOUTH, lblNonce);
		lblNonceN.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNonceN.setText((Integer.toString(block.getNonce())));
		getContentPane().add(lblNonceN);
		
		JLabel lblTimestamp = new JLabel("Date de création:");
		springLayout.putConstraint(SpringLayout.NORTH, lblTimestamp, 6, SpringLayout.SOUTH, lblNonceN);
		springLayout.putConstraint(SpringLayout.WEST, lblTimestamp, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblTimestamp);
		
		JLabel lblTimestampStr = new JLabel("timestampStr");
		springLayout.putConstraint(SpringLayout.NORTH, lblTimestampStr, 6, SpringLayout.SOUTH, lblTimestamp);
		springLayout.putConstraint(SpringLayout.WEST, lblTimestampStr, 10, SpringLayout.WEST, getContentPane());
		lblTimestampStr.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTimestampStr.setText(block.getTimestamp());
		getContentPane().add(lblTimestampStr);
		
		
		JLabel lblHash = new JLabel("Hash:");
		springLayout.putConstraint(SpringLayout.NORTH, lblHash, 6, SpringLayout.SOUTH, lblTimestampStr);
		springLayout.putConstraint(SpringLayout.WEST, lblHash, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblHash);
		
		JLabel lblHashStr = new JLabel("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");
		springLayout.putConstraint(SpringLayout.NORTH, lblHashStr, 6, SpringLayout.SOUTH, lblHash);
		springLayout.putConstraint(SpringLayout.WEST, lblHashStr, 10, SpringLayout.WEST, getContentPane());
		lblHashStr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lblHashStr.setText(block.getHash());
		getContentPane().add(lblHashStr);
		
		JLabel lblMerkel = new JLabel("Hash de la racine de l'arbre de Merkel :");
		springLayout.putConstraint(SpringLayout.NORTH, lblMerkel, 6, SpringLayout.SOUTH, lblHashStr);
		springLayout.putConstraint(SpringLayout.WEST, lblMerkel, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblMerkel);
		
		JLabel lblHashMerkelStr = new JLabel("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");
		springLayout.putConstraint(SpringLayout.NORTH, lblHashMerkelStr, 6, SpringLayout.SOUTH, lblMerkel);
		springLayout.putConstraint(SpringLayout.WEST, lblHashMerkelStr, 10, SpringLayout.WEST, getContentPane());
		lblHashMerkelStr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lblHashMerkelStr.setText(block.getMerkelTreeRootHash());
		getContentPane().add(lblHashMerkelStr);
		
		JLabel lblprevBlockHash = new JLabel("Hash du bloc précédent:");
		springLayout.putConstraint(SpringLayout.NORTH, lblprevBlockHash, 6, SpringLayout.SOUTH, lblHashMerkelStr);
		springLayout.putConstraint(SpringLayout.WEST, lblprevBlockHash, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblprevBlockHash);
		
		JLabel lblPrevHashStr = new JLabel("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");
		springLayout.putConstraint(SpringLayout.NORTH, lblPrevHashStr, 6, SpringLayout.SOUTH, lblprevBlockHash);
		springLayout.putConstraint(SpringLayout.WEST, lblPrevHashStr, 10, SpringLayout.WEST, getContentPane());
		lblPrevHashStr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lblPrevHashStr.setText(block.getPrevBlockHash());
		getContentPane().add(lblPrevHashStr);
		
		JLabel lblTransactions = new JLabel("Transactions:");
		springLayout.putConstraint(SpringLayout.NORTH, lblTransactions, 6, SpringLayout.SOUTH, lblPrevHashStr);
		springLayout.putConstraint(SpringLayout.WEST, lblTransactions, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblTransactions);
		
		ltx = new DefaultListModel<String>();
		for (String tx: b){
			ltx.addElement(tx);
		}
		JList list = new JList(ltx);
		JScrollPane scrollableList = new JScrollPane(list);
		springLayout.putConstraint(SpringLayout.NORTH, scrollableList, 6, SpringLayout.SOUTH, lblTransactions);
		springLayout.putConstraint(SpringLayout.WEST, scrollableList, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(scrollableList);
		
		JButton btnCheck = new JButton("Vérifier");
		springLayout.putConstraint(SpringLayout.SOUTH, scrollableList, -6, SpringLayout.NORTH, btnCheck);
		springLayout.putConstraint(SpringLayout.EAST, scrollableList, 0, SpringLayout.EAST, btnCheck);
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!BlockChainCheck.CheckBlockHash(b)) {
					JOptionPane.showMessageDialog(null, "Hash du bloc invalide", "Test de validité", JOptionPane.ERROR_MESSAGE);
				} else if (!BlockChainCheck.CheckBlockMerkelTree(b)) {
					JOptionPane.showMessageDialog(null, "arbre de Merkel invalide", "Test de validité", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Hash du bloc et arbre de Merkel valide", "Test de validité", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnCheck, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnCheck, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnCheck);
	}
}
