package modmanager;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel; // Lista padr�o
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import java.awt.FlowLayout;

/**
 * The main class and the program's window.
 */
public class Main extends JFrame {

	ImageIcon modImage;
	static int modQuantity;
	/**
	 * This multidimensional array contais values of all mods. Index 1: mod index;
	 * Index 2: mod folder (0), mod title (1), mod author (2).
	 */
	static String[][] allModData;
	String modSelecionado;
	boolean[] allConfig = Start.getConfig();
	static String sorrPath;

	/**
	 * This variable is necessary to close the whole program when you closed certain
	 * popups, instand of only close the popup itself.
	 */
	static boolean closeJVM = true;

	/**
	 * Return the mod quantity.
	 */
	public static int getModQuantity() {
		return modQuantity;
	}

	public void updateModList() {
		setModData();

		// Create model with attributes based on SorrMod class and add the elements.
		DefaultListModel<SorrMod> myModel = Start.refreshModList(allModData);
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

	public String getInstalledMod() {
		return modSelecionado;

	}

	public void setInstalledMod() {
		modSelecionado = FuncMods.existsInstallation();
	}

	/**
	 * Set all mod values by scanning folders and files.
	 */
	public void setModData() {
		ArrayList<String> modFolders = FuncMods.getAllFolders(sorrPath + "//mod//games");
		modQuantity = modFolders.size();
		String[][] allModsValues = new String[modQuantity][3];

		System.out.println("Size: " + modQuantity);
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
		setBounds(100, 100, 413, 510);
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

		// Search for important files and folders.
		// Check for SorR palette folders
		String[] palFolders = { "chars", "backup_chars", "enemies", "backup_enemies" };
		for (String i : palFolders) {
			if (!new File(Main.sorrPath + "//palettes//" + i).exists()) {

				String message = "The directory \" palettes/" + i.replace("//", "/") + "\" was not found!\n"
						+ "The program needs that to work. Please select a different path or check your folder manually.";
				JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);

				openFileDialog();
			}
		}

		// Check for important folders which contains mods
		String[] foldersToMake = { "data", "mod//chars", "mod//themes", "mod//games" };
		for (String i : foldersToMake) {
			File selectedFolder = new File(Main.sorrPath + "//" + i);
			if (!selectedFolder.exists()) {
				selectedFolder.mkdirs();
				System.out.println(String.format("Directory \"%s\" created!", i));
			}
		}

		// Get all mod values from txt.
		setModData();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setToolTipText("");
		tabbedPane.setBounds(0, 0, 399, 471);
		contentPane.add(tabbedPane);

		JPanel panel_level = new JPanel();
		tabbedPane.addTab("   Levels    ", null, panel_level, null);
		panel_level.setLayout(null);

		// Instancing custom object //////////////////////////////////////////////

		// Mod List
		updateModList();
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
		pn_installed.setBounds(10, 9, 374, 423);
		panel_level.add(pn_installed);
						pn_installed.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
						JPanel panelButtons2 = new JPanel();
						panelButtons2.setBackground(Color.RED);
						pn_installed.add(panelButtons2);
		
				JPanel panelButtons1 = new JPanel();
				panelButtons1.setBackground(Color.BLUE);
				pn_installed.add(panelButtons1);

		JLabel lblTitleImg = new JLabel("");
		// Getting image from the .jar file
		lblTitleImg.setIcon(new ImageIcon(Main.class.getResource("/images/default_title.png")));
		pn_installed.add(lblTitleImg);

		setInstalledMod();
		
		

		JPanel panel_option = new JPanel();
		tabbedPane.addTab("Options", null, panel_option, null);
		panel_option.setLayout(null);

		JCheckBox chckbxAvMods = new JCheckBox("Show only available mods.");
		chckbxAvMods.setBounds(10, 7, 341, 23);
		panel_option.add(chckbxAvMods);

		if (allConfig[0] == true) {
			System.out.println("checkbox esta marcado!");
			chckbxAvMods.setSelected(true);
		}

	

		JCheckBox chckAuthors = new JCheckBox("Hide authors in the mod list.");
		chckAuthors.setBounds(10, 33, 341, 23);
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

		JCheckBox chckFistMod = new JCheckBox("Show installed mod as the first one of the sormaker\r\n list");
		chckFistMod.setBounds(10, 59, 341, 23);
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

		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Use \r\npalettes from mods playable characters (you can't");
		chckbxNewCheckBox_1.setEnabled(false);
		chckbxNewCheckBox_1.setBounds(10, 125, 341, 23);
		panel_option.add(chckbxNewCheckBox_1);

		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Use playable chars from mods (you cannot use another");
		chckbxNewCheckBox_2.setEnabled(false);
		chckbxNewCheckBox_2.setBounds(10, 176, 341, 23);
		panel_option.add(chckbxNewCheckBox_2);

		JLabel lblNewLabel = new JLabel("use characters mods).");
		lblNewLabel.setEnabled(false);
		lblNewLabel.setBounds(31, 155, 314, 14);
		panel_option.add(lblNewLabel);

		JLabel lblListByAdding = new JLabel("by adding \"-\" to the begining of its folder name.");
		lblListByAdding.setBounds(31, 89, 314, 14);
		panel_option.add(lblListByAdding);

		boolean conditOption = true;
		if (getInstalledMod() != null) {
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

		JButton btnNewButton_1 = new JButton("Browse...");
		btnNewButton_1.setBounds(290, 273, 94, 23);
		panel_option.add(btnNewButton_1);

		JLabel lblNewLabel_1 = new JLabel("SorR path:");
		lblNewLabel_1.setBounds(10, 248, 94, 14);
		panel_option.add(lblNewLabel_1);

		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(10, 273, 269, 20);
		panel_option.add(textPane);
		textPane.setText(sorrPath + "\"SorR.exe");
		
		if(panelButtons1.isShowing()) {
			System.out.println("pane1 está ativo!");
		}
		if(panelButtons1.isVisible()) {
			System.out.println("pane1 está visível!");
		}
	}
}
