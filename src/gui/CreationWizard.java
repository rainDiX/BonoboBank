package gui;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bcb.CentralBank;

import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

public class CreationWizard extends JDialog {

	private JTextField txtBanqueName;
	
	private CentralBank bank;

	/**
	 * Create the frame.
	 */
	public CreationWizard() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/ressources/000.png")));
		setTitle("Creation d'une nouvelle banque");
		setBounds(100, 100, 500, 222);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblNombreDeBlocs = new JLabel("Nombre de blocs à miner");
		springLayout.putConstraint(SpringLayout.WEST, lblNombreDeBlocs, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblNombreDeBlocs);
		
		JLabel lblDifficultDuMinage = new JLabel("Difficulté du minage");
		springLayout.putConstraint(SpringLayout.WEST, lblDifficultDuMinage, 0, SpringLayout.WEST, lblNombreDeBlocs);
		getContentPane().add(lblDifficultDuMinage);
		
		JSpinner spinnerDifficulty = new JSpinner();
		springLayout.putConstraint(SpringLayout.WEST, spinnerDifficulty, 299, SpringLayout.EAST, lblDifficultDuMinage);
		springLayout.putConstraint(SpringLayout.EAST, spinnerDifficulty, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblDifficultDuMinage, 2, SpringLayout.NORTH, spinnerDifficulty);
		spinnerDifficulty.setValue(4);
		getContentPane().add(spinnerDifficulty);
		
		JLabel lblNombreDutilisateurs = new JLabel("Nombre d'utilisateurs");
		springLayout.putConstraint(SpringLayout.WEST, lblNombreDutilisateurs, 0, SpringLayout.WEST, lblNombreDeBlocs);
		getContentPane().add(lblNombreDutilisateurs);
		
		JSpinner spinnerUser = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, spinnerUser, 6, SpringLayout.SOUTH, spinnerDifficulty);
		springLayout.putConstraint(SpringLayout.WEST, spinnerUser, 263, SpringLayout.EAST, lblNombreDutilisateurs);
		springLayout.putConstraint(SpringLayout.EAST, spinnerUser, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblNombreDutilisateurs, 2, SpringLayout.NORTH, spinnerUser);
		spinnerUser.setValue(10);
		getContentPane().add(spinnerUser);
		
		JLabel lblReward = new JLabel("Montant de la récompense (Bnb)");
		springLayout.putConstraint(SpringLayout.WEST, lblReward, 0, SpringLayout.WEST, lblNombreDeBlocs);
		getContentPane().add(lblReward);
		
		JLabel lblBankName = new JLabel("Nom de la banque");
		springLayout.putConstraint(SpringLayout.WEST, lblBankName, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblBankName);
		
		txtBanqueName = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, lblBankName, 3, SpringLayout.NORTH, txtBanqueName);
		springLayout.putConstraint(SpringLayout.EAST, txtBanqueName, -10, SpringLayout.EAST, getContentPane());
		txtBanqueName.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBanqueName.setText("coinbase");
		getContentPane().add(txtBanqueName);
		txtBanqueName.setColumns(10);
		
		JSpinner blockSpinner = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, spinnerDifficulty, 6, SpringLayout.SOUTH, blockSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, lblNombreDeBlocs, 2, SpringLayout.NORTH, blockSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, blockSpinner, 8, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, blockSpinner, -110, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, blockSpinner, -10, SpringLayout.EAST, getContentPane());
		blockSpinner.setValue(1000);
		getContentPane().add(blockSpinner);
		
		JSpinner rewardSpinner = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, txtBanqueName, 6, SpringLayout.SOUTH, rewardSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, lblReward, 2, SpringLayout.NORTH, rewardSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, rewardSpinner, 6, SpringLayout.SOUTH, spinnerUser);
		springLayout.putConstraint(SpringLayout.WEST, rewardSpinner, -73, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, rewardSpinner, -10, SpringLayout.EAST, getContentPane());
		rewardSpinner.setValue(50);
		getContentPane().add(rewardSpinner);
		
		JButton btnStart = new JButton("Commencer");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long initialReward = ((Integer) rewardSpinner.getValue()) * 100000000l;
				bank = new CentralBank(txtBanqueName.getText(), initialReward , (Integer) spinnerDifficulty.getValue());
				int userCount = (Integer) spinnerUser.getValue();
				// Ajout de N utilisateurs
		        for (int i = 1; i <= userCount; ++i) {
		            bank.addUser("User");
		        }
		        // Genesis
		        bank.genesis();
		        // Helicopter Money
		        bank.helicopterMoney();
		        // Phase de marché :
		        bank.mercatoPhase((Integer)blockSpinner.getValue());
		        setVisible(false);
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnStart, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnStart, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnStart);
	}
	
	public CentralBank getBank() {
		return bank;
	}
	
}
