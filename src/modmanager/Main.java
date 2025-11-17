package modmanager;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The main class and the program's window.
 */
public class Main extends JFrame {

	/**
	 * This multidimensional array contais values of all mods. Index 1: mod index;
	 * Index 2: mod folder (0), mod title (1), mod author (2).
	 */
	static String[][] allModData;

	/**
	 * This variable is necessary to close the whole program when you closed certain
	 * popups, instead of only close the popup itself.
	 */
	static boolean closeJVM = true;

	/**
	 * This array contains some configurations.
	 */
	boolean[] allConfig = Start.getConfig();

	// The rest of the global variables are self-explanatory.
	static int modQuantity;
	static String selectedMod, sorrPath, gameAssetsPath;
	JList<SorrMod> listMod;
	ImageIcon modImage;
	static String selectedDir;

	/**
	 * Update the UI with the available mods.
	 */
	public void updateModList() {
		setModData();

		// Create model with attributes based on SorrMod class and add the elements.
		DefaultListModel<SorrMod> myModel = Start.refreshModList(allModData);
		listMod = new JList<SorrMod>(myModel);

		// List Background color
		listMod.setBackground(new Color(90, 90, 90));

		// Foreground color
		listMod.setForeground(Color.white);
		listMod.setBorder(new LineBorder(UIManager.getColor("inactiveCaptionText")));
		listMod.setBackground(UIManager.getColor("textInactiveText"));
		listMod.setForeground(Color.BLACK);

		// Add the list to the window
		listMod.setCellRenderer(new ModRenderer());
	}

	/**
	 * Get title image of the installed mod.
	 * 
	 * @return mod title screen file.
	 */
	public ImageIcon getModImage() {
		return modImage;
	}

	public void setImageIcon() {
		modImage = new ImageIcon("mod//games//" + FuncMods.existsInstallation() + "//title.png");
	}

	public void setInstalledMod() {
		selectedMod = FuncMods.existsInstallation();
	}

	/**
	 * Set all mod values by scanning folders and files.
	 */
	public void setModData() {
		ArrayList<String> modFolders = FuncMods.getAllFolders(sorrPath + "//mod//games");
		modQuantity = modFolders.size();
		String[][] allModsValues = new String[modQuantity][3];

		System.out.println("Amount of mods: " + modQuantity);
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

	public void openFileDialog() {
		System.out.println("Opening a window for selecting a SorR path...");
		SorrChooser dial = new SorrChooser();
		dial.setVisible(true);

		// If you close the dialog instead of click on the "confirm" button, the entire
		// program will be closed.
		if (closeJVM) {
			System.out.println("--------------------------------------------------");
			System.out.println("Software closed!");
			System.out.print("--------------------------------------------------");
			System.exit(-1);
		}

		// Save the selected path in the configuration
		Start.changeConfig(3, sorrPath);
		System.out.println("Path you choosed:" + sorrPath);
	}

	private static final long serialVersionUID = 1L;

	// DefaultListModel model;
	private JPanel contentPane;
	private JTextField txtPath;
	private JTextField txtAltPath;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
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
	public Main() {
		setTitle("Grupper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 413, 457);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(20, 20, 20));
		contentPane.setBorder(new LineBorder(UIManager.getColor("inactiveCaptionText")));
		setLocationRelativeTo(null); // Window position
		setResizable(false); // Cannot be resized
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Show current directory
		String dir_atual = System.getProperty("user.dir");
		System.out.println("Grupper directory: " + dir_atual);

		// Load configuration to found the sorr path.
		Start.scanConfig();

		// Checking if the executable path exists.
		if (sorrPath != null && !sorrPath.equals("null")) {
			if (!FuncMods.exist(sorrPath + "//SorR.exe")) {
				String message = sorrPath + "\\SorR.exe was not found!\n"
						+ "CLick in \"OK\" to Select a different path.";
				JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);
				// Assign the null value to sorrPath for fulfulling the next conditional
				// statement.
				sorrPath = null;
			}
		}

		// Open dialog to set SorR executable if there isn't one.
		if (sorrPath == null || Start.getPendingExecStatus() == true) {
			openFileDialog();
		}

		// Search for SorR palette folders. These folders are importants for Grupper.
		String[] palFolders = { "chars", "backup_chars", "enemies", "backup_enemies" };
		for (String i : palFolders) {
			if (!new File(sorrPath + "//palettes//" + i).exists()) {
				String message = "The directory \" palettes/" + i.replace("//", "/") + "\" was not found!\n"
						+ "The program needs that to work. Please select a different path or check your folder manually.";
				JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);
				// In the absence of these folders, the program will prompt for a new path.
				openFileDialog();
			}
		}

		// Checking for folders which contains mods.
		String[] foldersToMake = { "data", "mod//chars", "mod//themes", "mod//games" };
		for (String i : foldersToMake) {
			File selectedFolder = new File(sorrPath + "//" + i);
			// If those folders don't exist, they will be created.
			if (!selectedFolder.exists()) {
				selectedFolder.mkdirs();
				System.out.println(String.format("Directory \"%s\" created!", i));
			}
		}

		// Get all mod values from txt.
		setModData();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setToolTipText("");
		tabbedPane.setBounds(0, 0, 399, 419);
		contentPane.add(tabbedPane);

		JPanel panel_level = new JPanel();
		tabbedPane.addTab("Levels", null, panel_level, null);
		panel_level.setLayout(null);

		// Load an icon image
		ImageIcon SorrIcon = new ImageIcon(getClass().getResource("/images/sorremake.png"));
		JButton btnRunRemake2 = new JButton(SorrIcon);
		btnRunRemake2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Run SorR executable through the exec method.
					Runtime.getRuntime().exec(sorrPath + "//SorR.exe", null, new File(sorrPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(-1);
			}
		});
		btnRunRemake2.setToolTipText("Run Sor Remake");
		btnRunRemake2.setBounds(20, 358, 59, 23);
		panel_level.add(btnRunRemake2);

		// Load an icon image
		ImageIcon makerIcon = new ImageIcon(getClass().getResource("/images/sormaker.png"));

		JButton btnSorMaker_1 = new JButton(makerIcon);
		btnSorMaker_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Run SorR executable through the exec method.
					Runtime.getRuntime().exec(sorrPath + "//SorMaker.exe", null, new File(sorrPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(-1);
			}
		});

		JButton btFolder_1 = new JButton("📁");
		btFolder_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Show current directory
				try {
					Desktop.getDesktop().open(new File(sorrPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btFolder_1.setToolTipText("Open SoRR Folder");
		btFolder_1.setBackground(UIManager.getColor("Button.background"));
		btFolder_1.setBounds(90, 358, 59, 23);
		panel_level.add(btFolder_1);
		btnSorMaker_1.setToolTipText("Run SorMaker");
		btnSorMaker_1.setBounds(158, 358, 59, 23);
		panel_level.add(btnSorMaker_1);

		JButton btInstall = new JButton("Install mod");
		btInstall.setBounds(240, 357, 137, 23);
		panel_level.add(btInstall);

		JScrollPane scrollPane_mods = new JScrollPane();
		scrollPane_mods.setBounds(10, 9, 374, 326);
		panel_level.add(scrollPane_mods);

		// Mod List
		updateModList();
		// Create Jlist using a model
		scrollPane_mods.setViewportView(listMod);
		// add(new JScrollPane(listMod));

		////////////////////////////////////////////////////////////////////////////////////////
		/////// PANEL CHARACTERS
		//////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////////////////////////

		JPanel panel_char = new JPanel();
		tabbedPane.addTab("Characters", null, panel_char, null);

		JFormattedTextField frmtdtxtfld01 = new JFormattedTextField();
		frmtdtxtfld01.setText("This section is not available yet. Please wait for an update.");
		frmtdtxtfld01.setEditable(false);
		panel_char.add(frmtdtxtfld01);

		////////////////////////////////////////////////////////////////////////////////////////
		/////// PANEL TOOLS
		//////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////////////////////////////////

		JPanel panel_tools = new JPanel();
		tabbedPane.addTab("Tools", null, panel_tools, null);
		panel_tools.setLayout(null);

		JLabel lblNewLabel_2_1 = new JLabel("Unlock everyting in the game.");
		lblNewLabel_2_1.setBounds(21, 116, 352, 21);
		panel_tools.add(lblNewLabel_2_1);

		JLabel lblNewLabel_2 = new JLabel("Restore the default backup palettes.");
		lblNewLabel_2.setBounds(21, 29, 352, 21);
		panel_tools.add(lblNewLabel_2);

		JCheckBox chckChars = new JCheckBox("Chars");
		chckChars.setBounds(102, 60, 87, 21);
		panel_tools.add(chckChars);

		JCheckBox chckEnemies = new JCheckBox("Enemies");
		chckEnemies.setBounds(15, 60, 87, 21);
		panel_tools.add(chckEnemies);

		JButton btnRestorePal = new JButton("Restore");
		btnRestorePal.setEnabled(false);
		btnRestorePal.setBounds(232, 60, 141, 21);
		panel_tools.add(btnRestorePal);
		btnRestorePal.addActionListener(new ActionListener() {
			/**
			 * Restore palettes
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				if (FuncMods.exist("resources.zip")) {
					// Show a confirmation dialog
					int response = JOptionPane.showConfirmDialog(new JFrame(),
							"This will replace any files in the backup folders. Do you want to proceed?",
							"Confirm Action", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

					// Check the user's response
					if (response == JOptionPane.YES_OPTION) {
						try {
							FuncMods.unzip("resources.zip", "unzipped");

							if (chckEnemies.isSelected()) {
								ArrayList<String> backupEnemiesFiles = FuncMods
										.getAllFiles("unzipped//palettes//backup_enemies//*.*");

								// Move each file from backup_enemies to the destination
								for (String fileName : backupEnemiesFiles) {
									FuncMods.move("unzipped//palettes//backup_enemies//" + fileName,
											sorrPath + "//palettes//backup_enemies");
								}
							}

							if (chckChars.isSelected()) {
								// Get all files from the backup_chars and backup_enemies directories
								ArrayList<String> backupCharsFiles = FuncMods
										.getAllFiles("unzipped//palettes//backup_chars//*.*");

								// Move each file from backup_chars to the destination
								for (String fileName : backupCharsFiles) {
									FuncMods.move("unzipped//palettes//backup_chars//" + fileName,
											sorrPath + "//palettes//backup_chars");
								}
							}

							FuncMods.deleteDirectory(new File("unzipped"));

							// Show a success message
							JOptionPane.showMessageDialog(new JFrame(), "Backup palettes successfully restored!\n",
									"Information", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						// User chose not to proceed
						JOptionPane.showMessageDialog(new JFrame(), "Operation cancelled.\n", "Information",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "The file \"resources.zip\" was not found!\n", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		chckChars.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckChars.isSelected()) {
					System.out.println("I'm selected!");
				} else {
					System.out.println("I'm not selected!");
				}
				if (!chckChars.isSelected() && !chckEnemies.isSelected()) {
					btnRestorePal.setEnabled(false);
					System.out.println("FALSE");
				} else {
					btnRestorePal.setEnabled(true);
					System.out.println("TRUE");
				}
			}
		});

		chckEnemies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckChars.isSelected()) {
					System.out.println("I'm selected!");
				} else {
					System.out.println("I'm not selected!");
				}
				if (!chckChars.isSelected() && !chckEnemies.isSelected()) {
					btnRestorePal.setEnabled(false);
					System.out.println("FALSE");
				} else {
					btnRestorePal.setEnabled(true);
					System.out.println("TRUE");
				}
			}
		});

		JButton btnApply = new JButton("Import save file");
		btnApply.addActionListener(new ActionListener() {
			/**
			 * Use a 100% savegame.
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				// Path to the savegame.sor file
				File saveGameFile = new File(sorrPath + "//savegame//savegame.sor");

				// Check if the file already exists
				if (saveGameFile.exists()) {
					// Show a confirmation pop-up
					int response = JOptionPane.showConfirmDialog(null,
							"Do you want to replace your current savegame.sor?", "Confirmation",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					// If the user chooses "No", exit the method
					if (response != JOptionPane.YES_OPTION) {
						return; // Cancel the operation
					} else {
						// If the user chooses "Yes", delete the existing file
						saveGameFile.delete();
					}
				}

				// Check if the resources.zip file exists
				if (FuncMods.exist("resources.zip")) {
					try {
						// Unzip the file
						FuncMods.unzip("resources.zip", "unzipped");
						// Move the unzipped file
						FuncMods.move("unzipped//savegame.sor", sorrPath + "//savegame");
						// Delete the unzipped directory
						FuncMods.deleteDirectory(new File("unzipped"));
						// Show a success message
						JOptionPane.showMessageDialog(new JFrame(), "New savegame copied successfully!\n",
								"Information", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					// Show an error message if the resources.zip file is not found
					JOptionPane.showMessageDialog(new JFrame(), "The file \"resources.zip\" was not found!\n", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		btnApply.setBounds(232, 137, 141, 21);
		panel_tools.add(btnApply);

		JSeparator separator_2_1 = new JSeparator();
		separator_2_1.setToolTipText("");
		separator_2_1.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Savegame 100%", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		separator_2_1.setBounds(8, 96, 378, 82);
		panel_tools.add(separator_2_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBorder(
				new TitledBorder(null, "Palette Recover", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		separator_2.setToolTipText("Palette recover");
		separator_2.setBounds(8, 10, 378, 82);
		panel_tools.add(separator_2);

		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setToolTipText("");
		separator_2_1_1.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Palette editor", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		separator_2_1_1.setBounds(8, 186, 378, 82);
		panel_tools.add(separator_2_1_1);

		JLabel lblNewLabel_2_1_1 = new JLabel("External color editor with more options. (future feature)");
		lblNewLabel_2_1_1.setBounds(21, 206, 352, 21);
		panel_tools.add(lblNewLabel_2_1_1);

		JButton palEditorButton = new JButton("Unnavailable yet");
		palEditorButton.setBounds(232, 228, 141, 21);
		palEditorButton.setEnabled(false);
		panel_tools.add(palEditorButton);

		JSeparator separator_2_1_1_1 = new JSeparator();
		separator_2_1_1_1.setToolTipText("");
		separator_2_1_1_1.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"File Finder (for mod makers)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		separator_2_1_1_1.setBounds(8, 276, 378, 82);
		panel_tools.add(separator_2_1_1_1);

		JLabel lblNewLabel_3 = new JLabel("Find a FPG or WAV file name based in a search term.");
		lblNewLabel_3.setBounds(21, 297, 310, 14);
		panel_tools.add(lblNewLabel_3);

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(284, 322, 89, 23);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: implementar ação de busca aqui
				// If there isn't a defined path
				if (gameAssetsPath == null) {
					System.err.println("The game assets variable is empty!");
					JOptionPane.showMessageDialog(null,
							"No game's asset directory defined. Please set the path before starting.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// If the path is invalid
				if (!FuncMods.exist(gameAssetsPath)) {
					System.err.println("The game assets folder doesn't exist!");
					JOptionPane.showMessageDialog(null, "Game assets directory does not exist: " + gameAssetsPath,
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				SearchTextApp.main(null);
			}
		});
		panel_tools.add(btnSearch);
		panel_tools.setComponentZOrder(btnSearch, 0); // garante que o botão fique no topo do z-order do painel
		btnSearch.setFocusable(true);

		////////////////////////////////////////////////////////////////////////////////////////
		/////// PANEL MUSIC
		//////////////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////////////////////////////

		JPanel panel_music = new JPanel();
		tabbedPane.addTab("Music Player", null, panel_music, null);

		JFormattedTextField frmtdtxtfld02 = new JFormattedTextField();
		frmtdtxtfld02.setText("This section is not available yet. Please wait for an update.");
		frmtdtxtfld02.setEditable(false);
		panel_music.add(frmtdtxtfld02);

		// chckEnemies.addActionListener(BoxChecker("chars"));

		////////////// INSTALLED MOD //////////////////////////////////////////////////
		JPanel pn_installed = new JPanel();
		pn_installed.setBounds(10, 9, 374, 382);
		panel_level.add(pn_installed);
		pn_installed.setLayout(null);

		JLabel lblTitleImg = new JLabel("");
		lblTitleImg.setBounds(27, 34, 320, 240);
		// Getting image from project resources
		lblTitleImg.setIcon(new ImageIcon(Main.class.getResource("/images/default_title.png")));
		pn_installed.add(lblTitleImg);

		JLabel lblAlText = new JLabel("Installed mod:");
		lblAlText.setBounds(27, 0, 320, 30);
		pn_installed.add(lblAlText);

		////////////////////////////////////////////////////////////////////////////////////////
		/////////// PANEL OPTION //////////////////////////////////////////////////

		JPanel panel_option = new JPanel();
		tabbedPane.addTab("Options", null, panel_option, null);
		panel_option.setLayout(null);

		JButton btnExePath = new JButton("Browse...");
		btnExePath.setBounds(295, 268, 89, 23);
		panel_option.add(btnExePath);

		JLabel lblNewLabel_1 = new JLabel("Executable path:");
		lblNewLabel_1.setBounds(10, 244, 223, 14);
		panel_option.add(lblNewLabel_1);

		txtPath = new JTextField(sorrPath + "\\SorR.exe");
		txtPath.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtPath.setBounds(10, 270, 275, 20);
		panel_option.add(txtPath);
		txtPath.setColumns(10);

		/////////////////////// resourses (extracted files for mod makers ////////////

		// --- inserir logo após txtPath.setColumns(10);

		JLabel lblAltPath = new JLabel("Extracted resources folder (modders):");
		lblAltPath.setBounds(10, 300, 223, 14);
		panel_option.add(lblAltPath);

		String content = "";
		if (gameAssetsPath != "") {
			content = gameAssetsPath;
		}
		txtAltPath = new JTextField(content);
		txtAltPath.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtAltPath.setBounds(10, 320, 275, 20);
		panel_option.add(txtAltPath);
		txtAltPath.setColumns(10);

		JButton btAltBrowse = new JButton("Browse...");
		btAltBrowse.setBounds(295, 320, 89, 23);
		panel_option.add(btAltBrowse);

		btAltBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String startPath;
				if (gameAssetsPath == "") {
					startPath = gameAssetsPath;
				} else {
					startPath = sorrPath;
				}
				// FileChooser object
				JFileChooser sorChooser = new JFileChooser(startPath);
				sorChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// sorChooser.setAcceptAllFileFilterUsed(false);

				System.out.println("Openinig File dialog and waiting for user choice...");

				// Show the file chooser dialog
				int result = sorChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					// User selected a file
					String selectedDir2 = sorChooser.getSelectedFile().toString();
					txtAltPath.setText(selectedDir2);
					txtAltPath.setToolTipText(selectedDir2);
					gameAssetsPath = selectedDir2;
					Start.changeConfig(4, gameAssetsPath);
				}
			}
		});

		setInstalledMod();

		if (selectedMod == null) {
			pn_installed.setVisible(false);
		} else {
			btInstall.setVisible(false);
			scrollPane_mods.setVisible(false);
			lblTitleImg.setIcon(new ImageIcon(sorrPath + "//mod//games//" + selectedMod + "//title.png"));
		}

		JCheckBox chckAvailable = new JCheckBox("Show only available mods.");
		chckAvailable.setBounds(10, 7, 341, 23);
		panel_option.add(chckAvailable);

		chckAvailable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Changing configuration 0...");
				Start.changeConfig(0);
				Start.refreshModList(allModData);
				updateModList();
				scrollPane_mods.setViewportView(listMod);
			}
		});

		JCheckBox chckAuthors = new JCheckBox("Hide authors in the mod list.");
		chckAuthors.setBounds(10, 33, 341, 23);
		panel_option.add(chckAuthors);

		chckAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Changing configuration 1...");
				Start.changeConfig(1);
			}
		});

		JCheckBox chckFistMod = new JCheckBox("Show installed mod as the first one of the sormaker\r\n list");
		chckFistMod.setBounds(10, 59, 374, 23);
		panel_option.add(chckFistMod);

		// Fill checkboxes according to class variable.
		if (allConfig[0] == true) {
			chckAvailable.setSelected(true);
		}
		if (allConfig[1] == true) {
			chckAuthors.setSelected(true);
		}
		if (allConfig[2] == true) {
			chckFistMod.setSelected(true);
		}

		chckFistMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Changing configuration 2...");
				Start.changeConfig(2);
			}
		});

		JCheckBox chckPalettes = new JCheckBox("Use player palettes from mods (you can't use custom");
		chckPalettes.setEnabled(false);
		chckPalettes.setBounds(10, 125, 374, 23);
		panel_option.add(chckPalettes);

		JLabel lblNewLabel = new JLabel("chars).");
		lblNewLabel.setEnabled(false);
		lblNewLabel.setBounds(31, 155, 314, 14);
		panel_option.add(lblNewLabel);

		JCheckBox chckPlayableChars = new JCheckBox("Use playable chars from mods (you cannot use another");
		chckPlayableChars.setEnabled(false);
		chckPlayableChars.setBounds(10, 176, 374, 23);
		panel_option.add(chckPlayableChars);

		JLabel lblListByAdding = new JLabel("by adding \"-\" to the begining of its folder name.");
		lblListByAdding.setBounds(31, 89, 353, 14);
		panel_option.add(lblListByAdding);

		boolean conditOption = true;
		if (selectedMod != null) {
			conditOption = false;
		}
		chckFistMod.setEnabled(conditOption);
		lblListByAdding.setEnabled(conditOption);

		JLabel lblUseCharactersMods = new JLabel("characters mods).");
		lblUseCharactersMods.setEnabled(false);
		lblUseCharactersMods.setBounds(31, 206, 314, 14);
		panel_option.add(lblUseCharactersMods);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 112, 374, 2);
		panel_option.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 231, 374, 2);
		panel_option.add(separator_1);
		btInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Check if there is a selected item
				if (listMod.getSelectedIndex() != -1) {
					System.out.println("Selected mod:" + listMod.getSelectedValue());
					// Check if the mod is available to install
					if (FuncMods.scanMod(listMod.getSelectedValue().toString()) == 0) {
						// Check if the project folder is used by another application.
						if (FuncMods.isFileUnlocked(new File(sorrPath + "\\mod\\games\\" + selectedMod))) {
							// Everything is okay to install the mod
							FuncMods.installMod(listMod.getSelectedValue().toString());

							// Remove a temp file used to check if the folder is used
							if (new File(sorrPath + "\\mod\\games\\null").delete()) {
								System.out.println("\"null\" deleted successfully.");
							} else {
								System.err.println("Failed to delete \"null\". It may not exist or is in use.");
							}

							// Swapping a panel
							pn_installed.setVisible(true);
							btInstall.setVisible(false);
							scrollPane_mods.setVisible(false);
							setModData();
							setInstalledMod();
							lblTitleImg
									.setIcon(new ImageIcon(sorrPath + "//mod//games//" + selectedMod + "//title.png"));
							chckFistMod.setEnabled(false);
							lblListByAdding.setEnabled(false);
						} else {
							System.err.println("The project files are being used by another program!");
							// Show a pop-up message informing the user that the file is locked
							JOptionPane.showMessageDialog(null,
									"You cannot continue because the project folder or some file of the project \""
											+ selectedMod + "\" is being used by another program.\n"
											+ "Please close any programs that might be using these files and try again.",
									"File Locked", JOptionPane.WARNING_MESSAGE);

						}
					}
				}
			}
		});

		///////////////////////////////////////////////////////////////////////////////////////
		////////// PANEL INFO
		///////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////

		JPanel panel_info = new JPanel();
		tabbedPane.addTab("🛈", null, panel_info, null);
		panel_info.setLayout(null);
		
		JLabel lblNewLabel_5 = new JLabel("Grupper beta 7 v1.0 by DanChavyn (2025)");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(10, 11, 374, 14);
		panel_info.add(lblNewLabel_5);
		
		JLabel lblAbout = new JLabel("<html><body style='text-align:center;'><p style='margin:0 0 8px 0;'>Grupper is an open source mod manager for SorR. Have fun with awesome projects by all mod creators!</p><p style='margin:0 0 8px 0;'>This software was Developed in Eclipse IDE (Java) and uses Apache Library.</p><p style='margin:0 0 8px 0;color: #A500B5;'>If you love beat 'em ups, I invite you to check out my YouTube channel, where you can follow my creations and learn a lot of cool stuff about modding!</p></body></html>");
		lblAbout.setVerticalAlignment(SwingConstants.TOP);
		lblAbout.setBounds(10, 36, 374, 131);
		panel_info.add(lblAbout);
		
		JLabel lblNewLabel_6 = new JLabel("Software released under the MIT License.");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(10, 366, 374, 14);
		panel_info.add(lblNewLabel_6);
		
		// Cria o botão
        JButton btnNewButton = new JButton("Visit my channel");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			URI uri = new URI("https://www.youtube.com/channel/UCvWEo-Qj_iOsRTsz4KqP_lA");
        			Desktop.getDesktop().browse(uri);
        		} catch (IOException | URISyntaxException e1) {
        			e1.printStackTrace();
        		}
        	}
        });
        btnNewButton.setBounds(119, 180, 161, 23);
        
     // Define o ícone da janela
        Image icon = Toolkit.getDefaultToolkit().getImage("caminho/para/seu/icone.png"); // Altere o caminho
        setIconImage(icon);
        
        // Carrega a imagem do ícone
        ImageIcon iconyt = new ImageIcon(getClass().getResource("/images/youtube.png")); // Altere o caminho
        btnNewButton.setIcon(iconyt); // Define a imagem como ícone do botão
        
        // Adiciona o botão ao painel
        panel_info.add(btnNewButton);
		
		JLabel lblconsiderSupporting = new JLabel("<html><body style='text-align:center;'><p style='margin:0 0 8px 0;'>Visit the project page on GitHub to check updates. Also, consider supporting me to help the development of new software for beatemups.</p></body></html>");
		lblconsiderSupporting.setVerticalAlignment(SwingConstants.TOP);
		lblconsiderSupporting.setBounds(10, 237, 374, 53);
		panel_info.add(lblconsiderSupporting);
		
		// Cria o botão
        JButton btnNewButton_1 = new JButton("");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SupportDialog.main(null);
        	}
        });
        btnNewButton_1.setBounds(243, 315, 58, 23);
        
        // Carrega a imagem
        ImageIcon iconHeart = new ImageIcon(getClass().getResource("/images/heart16x16.png")); // Altere o caminho
        btnNewButton_1.setIcon(iconHeart); // Define a imagem como ícone do botão
        // ImageIcon SorrIcon = new ImageIcon(getClass().getResource("/images/sorremake.png"));
        panel_info.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("🌐 GitHub Page");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			URI uri = new URI("https://github.com/dan2221/Grupper");
        			Desktop.getDesktop().browse(uri);
        		} catch (IOException | URISyntaxException e1) {
        			e1.printStackTrace();
        		}
        	}
        });
        btnNewButton_2.setBounds(105, 315, 129, 23);
        panel_info.add(btnNewButton_2);

		JButton btUninstall = new JButton("Uninstall mod");
		btUninstall.setBounds(231, 349, 135, 23);
		pn_installed.add(btUninstall);

		btUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Verify installed mod
				setInstalledMod();

				// Check if data folder is used by another application
				if (FuncMods.isFileUnlocked(new File(sorrPath + "\\data"))) {

					// Call method to uninstall the mod
					FuncMods.uninstallMod(selectedMod);

					// Check if the mod was really uninstalled
					setModData();
					setInstalledMod();

					// Disable and enable GUI components
					if (FuncMods.existsInstallation() == null) {
						// jerry-rigged way to change the panel
						pn_installed.setVisible(false);
						btInstall.setVisible(true);
						scrollPane_mods.setVisible(true);
						chckFistMod.setEnabled(true);
						lblListByAdding.setEnabled(true);
					}
					updateModList();
					scrollPane_mods.setViewportView(listMod);
				} else {
					System.err.println("The project files are being used by another program!");
					// Show a pop-up message informing the user that the file is locked
					JOptionPane.showMessageDialog(null,
							"You cannot continue because some file of your data folder is being used by another program.\n"
									+ "Please close any programs that might be using these files and try again.",
							"File Locked", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnExePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// FileChooser object
				JFileChooser sorChooser = new JFileChooser(sorrPath);
				sorChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				sorChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Executable (*.exe)", "exe");
				sorChooser.setFileFilter(filter);

				System.out.println("Openinig File dialog and waiting for user choice...");

				// Show the file chooser dialog
				int result = sorChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					// User selected a file
					selectedDir = sorChooser.getSelectedFile().getParent();
					txtPath.setText(sorChooser.getSelectedFile().toString());
					txtPath.setToolTipText(sorChooser.getSelectedFile().toString());

					boolean fulfilled = true;
					String selectedPath = txtPath.getText();

					// The game doesn't work if you use an executable name different than
					// "sorr.exe", this is why any other name won't be accepted here.
					if (!selectedPath.toLowerCase().endsWith("sorr.exe")) {
						String message = "The executable you selected must has the name of \"SorR.exe\"!";
						JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.WARNING_MESSAGE);
						fulfilled = false;
					} else {
						int pathSize = selectedPath.length();
						String cutPath = selectedPath.substring(0, pathSize - 9);
						System.out.println("Path you chose: " + cutPath + "\nChecking folders...");
						String[] palFolders = { "chars", "backup_chars", "enemies", "backup_enemies" };
						for (String i : palFolders) {
							if (!new File(cutPath + "//palettes//" + i).exists()) {
								fulfilled = false;
								errorMsg(cutPath + "\\palettes\\" + i, "folder");
								break;
							}
						}
						// With all conditions accomplished, the path can be chose.
						if (fulfilled) {
							// Add game path to global variables
							sorrPath = selectedDir;
							System.out.println("Folders found!\n----------------------");
						}
					}

					// Save the new path in the configuration
					Start.changeConfig(3, sorrPath);

				}

				System.out.println("SorR Directory changed! Looking for mods in the new folder...");

				setInstalledMod();
				Start.refreshModList(allModData);
				updateModList();
				scrollPane_mods.setViewportView(listMod);

				// Show a panel if there is an installed mod.
				if (selectedMod == null) {
					pn_installed.setVisible(false);
					btInstall.setVisible(true);
					scrollPane_mods.setVisible(true);
				} else {
					pn_installed.setVisible(true);
					btInstall.setVisible(false);
					scrollPane_mods.setVisible(false);
					lblTitleImg.setIcon(new ImageIcon(sorrPath + "//mod//games//" + selectedMod + "//title.png"));
				}
			}
		});
	}

	public static void errorMsg(String file, String additionalText) {
		String message = "The " + additionalText + " \"" + file.replace("//", "/") + "\" was not found!\n"
				+ "The program needs that to work.";
		JOptionPane.showMessageDialog(new JFrame(), message, "WARNING", JOptionPane.WARNING_MESSAGE);
	}
}