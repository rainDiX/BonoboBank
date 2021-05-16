package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;

import bcb.CentralBank;
import bcb.Transaction;
import bcb.User;
import blockChain.BlockChainCheck;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	
	private BlockChainView bcv;
	
	private JMenuItem mntmCheck;
	
	private JMenuItem mntmSave;
	
	private JMenuItem mntmMineMore;
	
	private JMenuItem mntmAddUser;

	private CentralBank bank;

	private DefaultListModel<User> lusers = new DefaultListModel<>();

	private long[] accounts;
	
	private DefaultListModel<Transaction> ltx = new DefaultListModel<>();

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/ressources/000.png")));
		setTitle("Bonobo Bank");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 460);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		CreationWizard cw = new CreationWizard();

		cw.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentHidden(ComponentEvent e) {
				bank = cw.getBank();
				if (bank != null) {
					updateUsers();
					updateTransactions();
					enableButtons();
					bcv.loadNewBlockChain(bank.getBlockChain());
				}
			}
		});

		JMenuItem mntmNouvelleBlockchain = new JMenuItem("Nouvelle blockchain...");
		mntmNouvelleBlockchain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cw.setVisible(true);
			}
		});
		mnFichier.add(mntmNouvelleBlockchain);

		JMenuItem mntmOuvrir = new JMenuItem("Ouvrir");
		mntmOuvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = showOpenFileDialog();
				if (path != null) {
					bank = new CentralBank(path);
					updateUsers();
					updateTransactions();
					enableButtons();
					bcv.loadNewBlockChain(bank.getBlockChain());
				}
			}
		});
		mnFichier.add(mntmOuvrir);

		mntmSave = new JMenuItem("Enregistrer");
		mntmSave.setEnabled(false);
		mnFichier.add(mntmSave);

		JMenu mnGestion = new JMenu("Gestion");
		menuBar.add(mnGestion);

		mntmAddUser = new JMenuItem("Ajouter un utilisateur");
		mntmAddUser.setEnabled(false);
		mnGestion.add(mntmAddUser);

		mntmMineMore = new JMenuItem("Miner plus de blocs");
		mntmMineMore.setEnabled(false);
		mnGestion.add(mntmMineMore);
		
		JMenu mnTools = new JMenu("Outils");
		menuBar.add(mnTools);
		
		mntmCheck = new JMenuItem("Vérifier la validitée");
		mntmCheck.setEnabled(false);
		mntmCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (BlockChainCheck.Check(bank.getBlockChain())) {
					JOptionPane.showMessageDialog(null, "BlockChain valide", "Test de validité", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "BlockChain non valide", "Test de validité", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnTools.add(mntmCheck);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "name_22859933822224");

		bcv = new BlockChainView(null);
		JScrollPane scrollableListBlockChainView = new JScrollPane(bcv);
		tabbedPane.addTab("BlockChain", null, bcv, null);

		JPanel usersView = new JPanel();
		tabbedPane.addTab("Utilisateurs", null, usersView, null);

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
				if (list.getSelectedValue() != null) {
					lblName.setText(list.getSelectedValue().toString());
					lblBalance.setText(accounts[list.getSelectedIndex()] + " Bnb");
				}
			}
		});
		
		JPanel txView = new JPanel();
		txView.setLayout(new BoxLayout(txView, BoxLayout.X_AXIS));
		
		JList listTx = new JList(ltx);

		JScrollPane scrollableListTx = new JScrollPane(listTx);
		txView.add(scrollableListTx);
		
		tabbedPane.addTab("Transactions", null, txView, null);
		
	}

	private void updateUsers() {
		accounts = new long[bank.getUserCount()];
		lusers.removeAllElements();
		for (int i = 0; i < bank.getUserCount(); ++i) {
			lusers.addElement(bank.getUser(i));
			accounts[i] = lusers.get(i).getBalanceBnb();
		}
	}
	
	private void updateTransactions() {
		ltx.removeAllElements();
		for (Transaction tx : bank) {
			ltx.addElement(tx);
		}
	}
	
	private void enableButtons() {
		mntmSave.setEnabled(true);
		mntmCheck.setEnabled(true);
		mntmAddUser.setEnabled(true);
		mntmMineMore.setEnabled(true);
	}
	
	private String showOpenFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Blockchain JSON", "json"));
 
        fileChooser.setAcceptAllFileFilterUsed(true);
 
        int result = fileChooser.showOpenDialog(this);
 
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return "";
    }
}
