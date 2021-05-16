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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JSpinner;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	
	private BlockChainView bcv;
	
	private JMenuItem mntmCheck;
	
	private JMenuItem mntmSave;
	
	private JMenuItem mntmMineMore;
	
	private JMenuItem mntmAddUser;
	
	private JButton btnGoto;
	
	private JSpinner spinnerBlock;
	
	private JLabel lblInfos;

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
				if (cw.getBank() != null) {
					bank = cw.getBank();
					updateGUIElements();
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
				if (path != "") {
					bank = new CentralBank(path);
					updateGUIElements();
					bcv.loadNewBlockChain(bank.getBlockChain());
				}
			}
		});
		mnFichier.add(mntmOuvrir);

		mntmSave = new JMenuItem("Enregistrer");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = showSaveFileDialog();
				if (path != "") {
					bank.writeJson(path);
				}
			}
		});
		mntmSave.setEnabled(false);
		mnFichier.add(mntmSave);

		JMenu mnGestion = new JMenu("Gestion");
		menuBar.add(mnGestion);

		mntmAddUser = new JMenuItem("Ajouter un utilisateur");
		mntmAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = JOptionPane.showInputDialog("Nom de l'utilisateur");
				if (username != null) {
					bank.addUser(username);
					JOptionPane.showMessageDialog(null, "Des blocs devront être minés pour valider l'ajout");
				}
			}
		});
		mntmAddUser.setEnabled(false);
		mnGestion.add(mntmAddUser);

		mntmMineMore = new JMenuItem("Miner plus de blocs");
		mntmMineMore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String blockCountStr = JOptionPane.showInputDialog("Entrez le nombre à miner");
				while (blockCountStr != null && !blockCountStr.matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Entrez un entier positif", "Erreur", JOptionPane.ERROR_MESSAGE);
					blockCountStr = JOptionPane.showInputDialog("Entrez le nombre à miner");
				}
				if (blockCountStr != null) {
					int blockCount = Integer.valueOf(blockCountStr);
					if (blockCount > 0) {
						bank.mercatoPhase(blockCount);
						updateGUIElements();
					}
				}
			}
		});
		mntmMineMore.setEnabled(false);
		mnGestion.add(mntmMineMore);
		
		JMenu mnTools = new JMenu("Outils");
		menuBar.add(mnTools);
		
		mntmCheck = new JMenuItem("Vérifier la validité");
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
		
		JScrollPane scrollableBlockChainView = new JScrollPane(bcv);
		JPanel blockchainTab = new JPanel();
		SpringLayout sl_blockchainTab = new SpringLayout();
		sl_blockchainTab.putConstraint(SpringLayout.NORTH, scrollableBlockChainView, 5, SpringLayout.NORTH, blockchainTab);
		sl_blockchainTab.putConstraint(SpringLayout.WEST, scrollableBlockChainView, 10, SpringLayout.WEST, blockchainTab);
		blockchainTab.setLayout(sl_blockchainTab);
		blockchainTab.add(scrollableBlockChainView);

		bcv.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				bcv.clickBlock(e.getX(), e.getY());
			}
		});
		
		tabbedPane.addTab("BlockChain", null, blockchainTab, null);
		
		btnGoto = new JButton("Goto");
		sl_blockchainTab.putConstraint(SpringLayout.SOUTH, scrollableBlockChainView, -6, SpringLayout.NORTH, btnGoto);
		sl_blockchainTab.putConstraint(SpringLayout.EAST, scrollableBlockChainView, 0, SpringLayout.EAST, btnGoto);
		btnGoto.setEnabled(false);
		btnGoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = (int) spinnerBlock.getValue();
				if (index < 0 || index >= bank.getBlockChain().getSize()) {
					JOptionPane.showMessageDialog(null, "Index pas dans la blockchain", "Test de validité", JOptionPane.ERROR_MESSAGE);
				} else {
					BlockView bv = new BlockView(bank.getBlockChain().getBlockAtIndex(index));
					bv.setVisible(true);
				}
			}
		});
		
		sl_blockchainTab.putConstraint(SpringLayout.SOUTH, btnGoto, -10, SpringLayout.SOUTH, blockchainTab);
		sl_blockchainTab.putConstraint(SpringLayout.EAST, btnGoto, -10, SpringLayout.EAST, blockchainTab);
		blockchainTab.add(btnGoto);
		
		spinnerBlock = new JSpinner();
		sl_blockchainTab.putConstraint(SpringLayout.NORTH, spinnerBlock, -30, SpringLayout.SOUTH, blockchainTab);
		sl_blockchainTab.putConstraint(SpringLayout.WEST, spinnerBlock, -98, SpringLayout.WEST, btnGoto);
		sl_blockchainTab.putConstraint(SpringLayout.SOUTH, spinnerBlock, -10, SpringLayout.SOUTH, blockchainTab);
		sl_blockchainTab.putConstraint(SpringLayout.EAST, spinnerBlock, -6, SpringLayout.WEST, btnGoto);
		blockchainTab.add(spinnerBlock);
		
		lblInfos = new JLabel("infos");
		sl_blockchainTab.putConstraint(SpringLayout.WEST, lblInfos, 10, SpringLayout.WEST, blockchainTab);
		sl_blockchainTab.putConstraint(SpringLayout.SOUTH, lblInfos, 0, SpringLayout.SOUTH, btnGoto);
		blockchainTab.add(lblInfos);
		
		JLabel lblBlock = new JLabel("Block");
		sl_blockchainTab.putConstraint(SpringLayout.SOUTH, lblBlock, 0, SpringLayout.SOUTH, btnGoto);
		sl_blockchainTab.putConstraint(SpringLayout.EAST, lblBlock, -6, SpringLayout.WEST, spinnerBlock);
		blockchainTab.add(lblBlock);
		

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

	private void updateGUIElements() {
		accounts = new long[bank.getUserCount()];
		lusers.removeAllElements();
		for (int i = 0; i < bank.getUserCount(); ++i) {
			lusers.addElement(bank.getUser(i));
			accounts[i] = lusers.get(i).getBalanceBnb();
		}
		ltx.removeAllElements();
		for (Transaction tx : bank) {
			ltx.addElement(tx);
		}
		lblInfos.setText("Banque: " + bank.getName() + " | difficulté: " 
				+ bank.getBlockChain().getDifficulty()
				+ " | taille: " + this.bank.getBlockChain().getSize());
		mntmSave.setEnabled(true);
		mntmCheck.setEnabled(true);
		mntmAddUser.setEnabled(true);
		mntmMineMore.setEnabled(true);
		btnGoto.setEnabled(true);
	}
	
	
	private String showOpenFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Ouvrir");
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
	
	private String showSaveFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		fileChooser.setDialogTitle("Sauvegarder");
        
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Blockchain JSON", "json"));
 
        fileChooser.setAcceptAllFileFilterUsed(true);
 
        int result = fileChooser.showSaveDialog(this);
 
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return "";
    }
}
