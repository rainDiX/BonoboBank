package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.SpringLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;

public class WelcomeWindow {

	private JFrame frmBonoboBank;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeWindow window = new WelcomeWindow();
					window.frmBonoboBank.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WelcomeWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBonoboBank = new JFrame();
		frmBonoboBank.setMinimumSize(new Dimension(450, 280));
		frmBonoboBank.setSize(new Dimension(450, 280));
		frmBonoboBank.setTitle("Bonobo Bank");
		frmBonoboBank.setBounds(100, 100, 450, 250);
		frmBonoboBank.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmBonoboBank.getContentPane().setLayout(springLayout);
		
		JButton btnOpenButton = new JButton("Ouvrir une blockchain");
		springLayout.putConstraint(SpringLayout.WEST, btnOpenButton, 10, SpringLayout.WEST, frmBonoboBank.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnOpenButton, -10, SpringLayout.SOUTH, frmBonoboBank.getContentPane());
		frmBonoboBank.getContentPane().add(btnOpenButton);
		
		JButton btnNewButton_1 = new JButton("Nouvelle blockchain");
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton_1, 0, SpringLayout.SOUTH, btnOpenButton);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_1, -10, SpringLayout.EAST, frmBonoboBank.getContentPane());
		frmBonoboBank.getContentPane().add(btnNewButton_1);
		
		JLabel lblBienvenueDansBonobo = new JLabel("Bienvenue dans Bonobo Banque !");
		lblBienvenueDansBonobo.setFont(new Font("Dialog", Font.BOLD, 14));
		springLayout.putConstraint(SpringLayout.NORTH, lblBienvenueDansBonobo, 10, SpringLayout.NORTH, frmBonoboBank.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblBienvenueDansBonobo, 10, SpringLayout.WEST, frmBonoboBank.getContentPane());
		frmBonoboBank.getContentPane().add(lblBienvenueDansBonobo);
		
		JLabel lblQueVoulezvousFaire = new JLabel("Que voulez-vous faire ?");
		springLayout.putConstraint(SpringLayout.NORTH, lblQueVoulezvousFaire, 6, SpringLayout.SOUTH, lblBienvenueDansBonobo);
		springLayout.putConstraint(SpringLayout.WEST, lblQueVoulezvousFaire, 10, SpringLayout.WEST, frmBonoboBank.getContentPane());
		frmBonoboBank.getContentPane().add(lblQueVoulezvousFaire);
		
		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 6, SpringLayout.SOUTH, lblQueVoulezvousFaire);
		springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, frmBonoboBank.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, -6, SpringLayout.NORTH, btnOpenButton);
		springLayout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, btnNewButton_1);
		frmBonoboBank.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(WelcomeWindow.class.getResource("/ressources/000-128px.png")));
		panel.add(label);
	}
}
