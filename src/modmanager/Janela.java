package modmanager;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel; // Lista padrão
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

public class Janela extends JFrame {

	ImageIcon imgmod;
	String[][] allmoddata;
	String modselecionado;

	/**
	 * Get title image of the installed mod.
	 * @return imgmod
	 */
	public ImageIcon getModImage() {
		return imgmod;
	}

	public void setImageIcon(String[][] allmodsvalues) {
		imgmod = new ImageIcon("mod//games//" + FuncMods.existsInstallation(allmodsvalues) + "//title.png");
	}

	public String getInstalledMod() {
		return modselecionado;
	}

	public void setInstalledMod() {
		modselecionado = FuncMods.existsInstallation(allmoddata);
	}

	public void setModData() {
		// Get all txt mod files
		ArrayList<String> txtmodfiles = FuncMods.getAllFiles("mod_list//*.txt");

		// Count txt mod files
		int quantmods = txtmodfiles.size();

		String[][] allmodsvalues = new String[quantmods][3];
		String[] moddata = new String[3];
		int counter = 0;
		for (String arq : txtmodfiles) {

			// Get mod data (folder, title and author)
			moddata = FuncMods.readTxt(arq);
			allmodsvalues[counter][0] = moddata[0];
			allmodsvalues[counter][1] = moddata[1];
			allmodsvalues[counter][2] = moddata[2];

			counter++;
		}
		allmoddata = allmodsvalues;
	}
	
	public String[][] getModData(){
		return allmoddata;
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
		setLocationRelativeTo(null); // Posição da janela
		setResizable(false); // Não pode ser redimensionada
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Show current directory
		String dir_atual = System.getProperty("user.dir");
		System.out.println("Grupper directory: " + dir_atual);

		// Search for important files and folders
		FuncMods.importantFiles();
		
		// Get all mods values from txt
		setModData();
		String[][] allmodsvalues = getModData();

		// create the model and add elements
		DefaultListModel<SorrMod> myModel = Start.refreshModList(allmodsvalues);

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

		///////// Botão para ESCOLHER MOD
		///////// /////////////////////////////////////////////////////////
		JButton btFolder = new JButton("Open SorR Folder");
		btFolder.setBounds(21, 357, 146, 23);
		panel_level.add(btFolder);
		btFolder.setBackground(UIManager.getColor("Button.background"));

		JScrollPane scrollPane_mods = new JScrollPane();
		scrollPane_mods.setBounds(10, 9, 374, 308);
		panel_level.add(scrollPane_mods);

		// Instanciando objeto personalizado

		//////// LISTA DE MODS
		//////// ////////////////////////////////////////////////////////////////////
		// Create Jlist using a model
		JList<SorrMod> listMod = new JList<SorrMod>(myModel);
		scrollPane_mods.setViewportView(listMod);
		// add(new JScrollPane(listMod));
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

		JPanel panel_char = new JPanel();
		tabbedPane.addTab("Characters", null, panel_char, null);

		JPanel panel_theme = new JPanel();
		tabbedPane.addTab("Themes", null, panel_theme, null);

		JPanel panel_music = new JPanel();
		tabbedPane.addTab("Music Player", null, panel_music, null);

		JPanel panel_option = new JPanel();
		tabbedPane.addTab("Options", null, panel_option, null);
		panel_option.setLayout(null);

		JCheckBox chckbxAvMods = new JCheckBox("Show only available mods.");
		chckbxAvMods.setBounds(26, 38, 341, 23);
		panel_option.add(chckbxAvMods);

		JCheckBox chckbxNewCheckBox = new JCheckBox("Use level mod menu images.");
		chckbxNewCheckBox.setBounds(26, 88, 341, 23);
		panel_option.add(chckbxNewCheckBox);

		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Use level mod playable characters\r \npalettes");
		chckbxNewCheckBox_1.setBounds(26, 144, 341, 23);
		panel_option.add(chckbxNewCheckBox_1);

		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Use level mod chars (you cannot use another chars).");
		chckbxNewCheckBox_2.setBounds(26, 214, 341, 23);
		panel_option.add(chckbxNewCheckBox_2);

		JLabel lblNewLabel = new JLabel("(you cannot use characters mods).");
		lblNewLabel.setBounds(47, 174, 314, 14);
		panel_option.add(lblNewLabel);

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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		btPlay.setBounds(256, 321, 118, 23);
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

		JButton btUninstall = new JButton("Uninstall mod");
		btUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Verify installed mod
				setInstalledMod();
				
				// Call method to uninstall the mod
				FuncMods.uninstallMod(getInstalledMod());
				
				// Check if the mod is uninstalled
				setInstalledMod();
				if (FuncMods.existsInstallation(allmodsvalues) == null) {
					// Use isso para alternar de painel
					pn_installed.setVisible(false);
					btInstall.setVisible(true);
					btFolder.setVisible(true);
					scrollPane_mods.setVisible(true);
				}

			}
		});
		btUninstall.setBounds(10, 321, 135, 23);
		pn_installed.add(btUninstall);

		btFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Use isso para alternar de painel
				// scrollPane_mods.setVisible(false);

				// Show current directory
				try {
					Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listMod.getSelectedIndex() != -1) { // There is a selected mod
					System.out.println(allmodsvalues[listMod.getSelectedIndex()][0]);
					FuncMods.installMod(allmodsvalues[listMod.getSelectedIndex()][0]);
					// Use isso para alternar de painel
					pn_installed.setVisible(true);
					btInstall.setVisible(false);
					btFolder.setVisible(false);
					scrollPane_mods.setVisible(false);
					setInstalledMod();
					lblTitleImg.setIcon(
							new ImageIcon("mod//games//" + getInstalledMod() + "//title.png"));
				}
			}
		});
	}
}
