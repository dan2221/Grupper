package modmanager;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel; // Lista padr?o
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;

public class Janela extends JFrame {

	ImageIcon modImage;
	static int modQuantity;
	static String[][] allModData;
	String modSelecionado;
	boolean[] allConfig = Start.getConfig();
	JList<SorrMod> listMod;

	public static int getModQuantity() {
		return modQuantity;
	}

	public void updateModList() {
		setModData();
		// create the model and add elements
		DefaultListModel<SorrMod> myModel = Start.refreshModList(allModData);
		listMod = new JList<SorrMod>(myModel);
		// // List Background color
		listMod.setBackground(new Color(90, 90, 90));
		// // Foreground color
		listMod.setForeground(Color.white);
		//
		listMod.setBorder(new LineBorder(UIManager.getColor("inactiveCaptionText")));
		listMod.setBackground(UIManager.getColor("textInactiveText"));
		listMod.setForeground(Color.BLACK);

		// Adiciona a lista para a janela :)
		listMod.setCellRenderer(new ModRenderer());
	}

	/**
	 * Get title image of the installed mod.
	 * 
	 * @return imgmod
	 */
	public ImageIcon getModImage() {
		return modImage;
	}

	public void setImageIcon() {
		modImage = new ImageIcon("mod//games//" + FuncMods.existsInstallation() + "//title.png");
	}

	public String getInstalledMod() {
		return modSelecionado;
		
	}

	public void setInstalledMod() {
		modSelecionado = FuncMods.existsInstallation();
	}

	/**
	 * Define all mod values by scanning folders and files.
	 */
	public void setModData() {
		ArrayList<String> modFolders = FuncMods.getAllFolders("mod//games");
		modQuantity = modFolders.size();
		String[][] allModsValues = new String[modQuantity][3];

		System.out.println("Tamanho: " + modQuantity);
		for (int i = 0; i < modFolders.size(); i++) {
			String current = modFolders.get(i).toString();
			allModsValues[i][0] = current;
			if (FuncMods.exist("mod_list//" + current + ".txt")) {
				String[] modData = new String[2];
				modData = FuncMods.readTxt(current + ".txt");

				allModsValues[i][1] = modData[1];
				allModsValues[i][2] = modData[2];
			} else {
				allModsValues[i][1] = current;
				allModsValues[i][2] = "-";
			}
		}
		allModData = allModsValues;
	}

	public static String[][] getAllModData() {
		return allModData;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// DefaultListModel model;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Janela frame = new Janela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Janela() {
		setTitle("Grupper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 413, 457);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(20, 20, 20));
		contentPane.setBorder(new LineBorder(UIManager.getColor("inactiveCaptionText")));
		setLocationRelativeTo(null); // Posi??o da janela
		setResizable(false); // N?o pode ser redimensionada
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Show current directory
		String dir_atual = System.getProperty("user.dir");
		System.out.println("Grupper directory: " + dir_atual);

		// Search for important files and folders
		FuncMods.importantFiles();

		// Get all mods values from txt
		setModData();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setToolTipText("");
		tabbedPane.setBounds(0, 0, 399, 419);
		contentPane.add(tabbedPane);

		JPanel panel_level = new JPanel();
		tabbedPane.addTab("   Levels    ", null, panel_level, null);
		panel_level.setLayout(null);

		/////// Instalar Mod ///////////////////////////////////////////////////////
		JButton btInstall = new JButton("Install mod");
		btInstall.setBounds(247, 357, 137, 23);
		panel_level.add(btInstall);

		///////// Bot?o para ESCOLHER MOD
		///////// /////////////////////////////////////////////////////////
		JButton btFolder = new JButton("Open SorR Folder");
		btFolder.setBounds(21, 357, 146, 23);
		panel_level.add(btFolder);
		btFolder.setBackground(UIManager.getColor("Button.background"));

		JScrollPane scrollPane_mods = new JScrollPane();
		scrollPane_mods.setBounds(10, 9, 374, 326);
		panel_level.add(scrollPane_mods);

		// Instanciando objeto personalizado

		//////// LISTA DE MODS
		//////// ////////////////////////////////////////////////////////////////////
		updateModList();
		// Create Jlist using a model
		scrollPane_mods.setViewportView(listMod);
		// add(new JScrollPane(listMod));

		JPanel panel_char = new JPanel();
		tabbedPane.addTab("Characters", null, panel_char, null);

		JFormattedTextField frmtdtxtfldThisSectionIs = new JFormattedTextField();
		frmtdtxtfldThisSectionIs.setText("This section is not available yet. Please wait for an update.");
		frmtdtxtfldThisSectionIs.setEditable(false);
		panel_char.add(frmtdtxtfldThisSectionIs);

		JPanel panel_theme = new JPanel();
		tabbedPane.addTab("Themes", null, panel_theme, null);

		JFormattedTextField frmtdtxtfldThisSectionIs_1 = new JFormattedTextField();
		frmtdtxtfldThisSectionIs_1.setText("This section is not available yet. Please wait for an update.");
		frmtdtxtfldThisSectionIs_1.setEditable(false);
		panel_theme.add(frmtdtxtfldThisSectionIs_1);

		JPanel panel_music = new JPanel();
		tabbedPane.addTab("Music Player", null, panel_music, null);

		JFormattedTextField frmtdtxtfldThisSectionIs_2 = new JFormattedTextField();
		frmtdtxtfldThisSectionIs_2.setText("This section is not available yet. Please wait for an update.");
		frmtdtxtfldThisSectionIs_2.setEditable(false);
		panel_music.add(frmtdtxtfldThisSectionIs_2);

		////////////// INSTALLED MOD //////////////////////////////////////////////////
		JPanel pn_installed = new JPanel();
		pn_installed.setBounds(10, 9, 374, 382);
		panel_level.add(pn_installed);
		pn_installed.setLayout(null);

		JLabel lblTitleImg = new JLabel("");
		lblTitleImg.setBounds(27, 11, 320, 240);
		lblTitleImg.setIcon(new ImageIcon(Janela.class.getResource("/images/default_title.png")));
		pn_installed.add(lblTitleImg);

		JButton btPlay = new JButton("Start SorR");
		btPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("SorR.exe");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		btPlay.setBounds(256, 348, 118, 23);
		pn_installed.add(btPlay);

		setInstalledMod();

		if (getInstalledMod() == null) {
			// Use isso para alternar de painel
			pn_installed.setVisible(false);
		} else {
			btInstall.setVisible(false);
			btFolder.setVisible(false);
			scrollPane_mods.setVisible(false);
			lblTitleImg.setIcon(new ImageIcon("mod//games//" + getInstalledMod() + "//title.png"));
		}

		JPanel panel_option = new JPanel();
		tabbedPane.addTab("Options", null, panel_option, null);
		panel_option.setLayout(null);

		JCheckBox chckbxAvMods = new JCheckBox("Show only available mods.");
		chckbxAvMods.setBounds(26, 38, 341, 23);
		panel_option.add(chckbxAvMods);

		Start.scanConfig(); // Load configuration

		if (allConfig[0] == true) {
			System.out.println("checkbox esta marcado!");
			chckbxAvMods.setSelected(true);
		}

		chckbxAvMods.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Changing configuration 0...");
				Start.changeConfig(0);
				Start.refreshModList(allModData);
				updateModList();
				scrollPane_mods.setViewportView(listMod);
			}
		});

		JCheckBox chckAuthors = new JCheckBox("Hide authors in the mod list.");
		chckAuthors.setBounds(26, 79, 341, 23);
		panel_option.add(chckAuthors);

		if (allConfig[1] == true) {
			System.out.println("checkbox esta marcado!");
			chckAuthors.setSelected(true);
		}

		chckAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Changing configuration 1...");
				Start.changeConfig(1);
			}
		});

		JCheckBox chckFistMod = new JCheckBox("Show installed mod as the first one of the sormaker\r\n");
		chckFistMod.setBounds(26, 118, 341, 23);
		panel_option.add(chckFistMod);

		if (allConfig[2] == true) {
			System.out.println("checkbox esta marcado!");
			chckFistMod.setSelected(true);
		}

		chckFistMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Changing configuration 2...");
				Start.changeConfig(2);
			}
		});

		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Use level mod playable characters\r \r\npalettes");
		chckbxNewCheckBox_1.setEnabled(false);
		chckbxNewCheckBox_1.setBounds(26, 184, 341, 23);
		panel_option.add(chckbxNewCheckBox_1);

		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Use level mod chars (you cannot use another chars).");
		chckbxNewCheckBox_2.setEnabled(false);
		chckbxNewCheckBox_2.setBounds(26, 252, 341, 23);
		panel_option.add(chckbxNewCheckBox_2);

		JLabel lblNewLabel = new JLabel("(you cannot use characters mods).");
		lblNewLabel.setEnabled(false);
		lblNewLabel.setBounds(47, 214, 314, 14);
		panel_option.add(lblNewLabel);

		JLabel lblListByAdding = new JLabel("list by adding \"-\" to the begining of its folder name.");
		lblListByAdding.setBounds(47, 148, 314, 14);
		panel_option.add(lblListByAdding);
		
		boolean conditOption = true;
		if(getInstalledMod() != null) {
			conditOption = false;
		}
		chckFistMod.setEnabled(conditOption);
		lblListByAdding.setEnabled(conditOption);

		JButton btnNewButton = new JButton("About Grupper");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutG.main(null);
			}
		});
		btnNewButton.setBounds(140, 317, 118, 23);
		panel_option.add(btnNewButton);

		btFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Use isso para alternar de painel
				// scrollPane_mods.setVisible(false);

				// Show current directory
				try {
					Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btInstall.addActionListener(new ActionListener() {
			/**
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				// Check if there are a selected item
				if (listMod.getSelectedIndex() != -1) {
					System.out.println("Selected mod:" + listMod.getSelectedValue());
					// Check if the mod is available to install
					if (FuncMods.scanMod(listMod.getSelectedValue().toString()) == 0) {
						FuncMods.installMod(listMod.getSelectedValue().toString());
						// Use isso para alternar de painel
						pn_installed.setVisible(true);
						btInstall.setVisible(false);
						btFolder.setVisible(false);
						scrollPane_mods.setVisible(false);
						setModData();
						setInstalledMod();
						lblTitleImg.setIcon(new ImageIcon("mod//games//" + getInstalledMod() + "//title.png"));
						
						chckFistMod.setEnabled(false);
						lblListByAdding.setEnabled(false);
					}
				}
			}
		});
		
		JButton btUninstall = new JButton("Uninstall mod");
		btUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Verify installed mod
				setInstalledMod();

				// Call method to uninstall the mod
				FuncMods.uninstallMod(getInstalledMod());

				// Check if the mod is uninstalled
				setModData();
				if (FuncMods.existsInstallation() == null) {
					// Use isso para alternar de painel
					pn_installed.setVisible(false);
					btInstall.setVisible(true);
					btFolder.setVisible(true);
					scrollPane_mods.setVisible(true);
					chckFistMod.setEnabled(true);
					lblListByAdding.setEnabled(true);
				}
				// Check if the mod is uninstalled
				setModData();
				updateModList();
				scrollPane_mods.setViewportView(listMod);
			}
		});
		btUninstall.setBounds(10, 348, 135, 23);
		pn_installed.add(btUninstall);
	}
}
