package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bcb.CentralBank;

import javax.swing.JSpinner;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreationWizard {

	private JFrame frame;
	private JTextField txtBanqueName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreationWizard window = new CreationWizard();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CreationWizard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 222);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JLabel lblNombreDeBlocs = new JLabel("Nombre de blocs à miner");
		springLayout.putConstraint(SpringLayout.WEST, lblNombreDeBlocs, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblNombreDeBlocs);
		
		JLabel lblDifficultDuMinage = new JLabel("Difficulté du minage");
		springLayout.putConstraint(SpringLayout.WEST, lblDifficultDuMinage, 0, SpringLayout.WEST, lblNombreDeBlocs);
		frame.getContentPane().add(lblDifficultDuMinage);
		
		JSpinner spinnerDifficulty = new JSpinner();
		springLayout.putConstraint(SpringLayout.WEST, spinnerDifficulty, 299, SpringLayout.EAST, lblDifficultDuMinage);
		springLayout.putConstraint(SpringLayout.EAST, spinnerDifficulty, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblDifficultDuMinage, 2, SpringLayout.NORTH, spinnerDifficulty);
		spinnerDifficulty.setValue(4);
		frame.getContentPane().add(spinnerDifficulty);
		
		JLabel lblNombreDutilisateurs = new JLabel("Nombre d'utilisateurs");
		springLayout.putConstraint(SpringLayout.WEST, lblNombreDutilisateurs, 0, SpringLayout.WEST, lblNombreDeBlocs);
		frame.getContentPane().add(lblNombreDutilisateurs);
		
		JSpinner spinnerUser = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, spinnerUser, 6, SpringLayout.SOUTH, spinnerDifficulty);
		springLayout.putConstraint(SpringLayout.WEST, spinnerUser, 263, SpringLayout.EAST, lblNombreDutilisateurs);
		springLayout.putConstraint(SpringLayout.EAST, spinnerUser, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblNombreDutilisateurs, 2, SpringLayout.NORTH, spinnerUser);
		spinnerUser.setValue(10);
		frame.getContentPane().add(spinnerUser);
		
		JLabel lblReward = new JLabel("Montant de la récompense (Bnb)");
		springLayout.putConstraint(SpringLayout.WEST, lblReward, 0, SpringLayout.WEST, lblNombreDeBlocs);
		frame.getContentPane().add(lblReward);
		
		JLabel lblBankName = new JLabel("Nom de la banque");
		springLayout.putConstraint(SpringLayout.WEST, lblBankName, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblBankName);
		
		txtBanqueName = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, lblBankName, 3, SpringLayout.NORTH, txtBanqueName);
		springLayout.putConstraint(SpringLayout.EAST, txtBanqueName, -10, SpringLayout.EAST, frame.getContentPane());
		txtBanqueName.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBanqueName.setText("coinbase");
		frame.getContentPane().add(txtBanqueName);
		txtBanqueName.setColumns(10);
		
		JSpinner blockSpinner = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, spinnerDifficulty, 6, SpringLayout.SOUTH, blockSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, lblNombreDeBlocs, 2, SpringLayout.NORTH, blockSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, blockSpinner, 8, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, blockSpinner, -110, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, blockSpinner, -10, SpringLayout.EAST, frame.getContentPane());
		blockSpinner.setValue(1000);
		frame.getContentPane().add(blockSpinner);
		
		JSpinner rewardSpinner = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, txtBanqueName, 6, SpringLayout.SOUTH, rewardSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, lblReward, 2, SpringLayout.NORTH, rewardSpinner);
		springLayout.putConstraint(SpringLayout.NORTH, rewardSpinner, 6, SpringLayout.SOUTH, spinnerUser);
		springLayout.putConstraint(SpringLayout.WEST, rewardSpinner, -73, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, rewardSpinner, -10, SpringLayout.EAST, frame.getContentPane());
		rewardSpinner.setValue(50);
		frame.getContentPane().add(rewardSpinner);
		
		JButton btnStart = new JButton("Commencer");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long initialReward = ((Integer) rewardSpinner.getValue()) * 100000000l;
				CentralBank bcb = new CentralBank(txtBanqueName.getText(), initialReward , (Integer) spinnerDifficulty.getValue());
				int userCount = (Integer) spinnerUser.getValue();
				// Ajout de N utilisateurs
		        for (int i = 1; i <= userCount; ++i) {
		            bcb.addUser("User");
		        }

		        // Genesis
		        bcb.genesis();

		        // Helicopter Money
		        bcb.helicopterMoney();
		        // Phase de marché :
		        bcb.mercatoPhase((Integer)blockSpinner.getValue());
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnStart, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnStart, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btnStart);
	}
}
