package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

import bcb.CentralBank;
import bcb.User;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.ScrollPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class DataVisual extends JFrame {

	private JPanel contentPane;
	
	private long[] accounts;

	/**
	 * Create the frame.
	 */
	public DataVisual(CentralBank bank) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 460);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "name_6909810446769");
		panel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel blockchainView = new JPanel();
		tabbedPane.addTab("BlockChain", null, blockchainView, null);
		
		JPanel usersView = new JPanel();
		tabbedPane.addTab("Users", null, usersView, null);
		
		DefaultListModel<User> lusers = new DefaultListModel<>();
		
		accounts = new long[bank.getUserCount()];
		for (int i = 0; i < bank.getUserCount(); ++i) {
			lusers.addElement(bank.getUser(i));
			accounts[i] = lusers.get(i).getBalanceBnb();
		}
		usersView.setLayout(new BoxLayout(usersView, BoxLayout.X_AXIS));
		
		JList list = new JList(lusers);
		
		JScrollPane scrollableList = new JScrollPane(list);
		usersView.add(scrollableList);
		
		JPanel userInfoPanel = new JPanel();
		FlowLayout fl_userInfoPanel = (FlowLayout) userInfoPanel.getLayout();
		fl_userInfoPanel.setAlignment(FlowLayout.LEFT);
		usersView.add(userInfoPanel);
		
		JPanel panel_2 = new JPanel();
		userInfoPanel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		JLabel userLabel = new JLabel("Nom de l'utilisateur :");
		panel_2.add(userLabel);
		
		JLabel lblName = new JLabel(" ");
		panel_2.add(lblName);
		
		JLabel lblBalanceTitle = new JLabel("Solde du compte:");
		panel_2.add(lblBalanceTitle);
		
		JLabel lblBalance = new JLabel("0 Bnb");
		panel_2.add(lblBalance);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				lblName.setText(list.getSelectedValue().toString());
				lblBalance.setText(accounts[list.getSelectedIndex()] + " Bnb");
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnQuitter = new JButton("Quitter");
		panel_1.add(btnQuitter);
		
		JButton btnSauvegarder = new JButton("Sauvegarder");
		panel_1.add(btnSauvegarder);
	}
}
