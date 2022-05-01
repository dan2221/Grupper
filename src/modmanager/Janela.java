package modmanager;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel; // Lista padrão
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import java.awt.Font;

public class Janela extends JFrame {

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
		setBounds(100, 100, 450, 480);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(20, 20, 20));
		contentPane.setBorder(new LineBorder(UIManager.getColor("inactiveCaptionText")));
		setLocationRelativeTo(null); // Posição da janela
		setResizable(false); // Não pode ser redimensionada
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// label
		JLabel lblNewLabel = new JLabel("Welcome to Grupper mod manager!");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setForeground(new Color(128, 128, 128));
		lblNewLabel.setBounds(96, 11, 241, 26);
		contentPane.add(lblNewLabel);

		// create the model and add elements
		DefaultListModel<SorrMod> myModel = new DefaultListModel<>();

		// Show current directory
		String dir_atual = System.getProperty("user.dir");
		System.out.println("Grupper directory: " + dir_atual);

		// Get all txt mod files
		ArrayList<String> txtmodfiles = FuncoesMods.getAllFiles(".txt", "mod_list");

		// Count txt mod files
		int quantmods = txtmodfiles.size();

		String[][] allmodsvalues = new String[quantmods][3];
		String[] moddata = new String[3];
		int counter = 0;
		for (String arq : txtmodfiles) {
			
			// Get mod data (folder, title and author)
			moddata = FuncoesMods.readTxt(arq);
			allmodsvalues[counter][0] = moddata[0];
			allmodsvalues[counter][1] = moddata[1];
			allmodsvalues[counter][2] = moddata[2];

			counter++;
		}

		// String[][] ordenado = Arrays.sort(allmodsvalues);
		// Custom input array
		int[] arr = { 13, 7, 6, 45, 21, 9, 101, 102 };

		// Applying sort() method over to above array
		// by passing the array as an argument
		Arrays.sort(arr);

		// Printing the array after sorting
//		for(int loop = 0; loop != 0; loop++) {
//			
//		}
		System.out.println("Modified array : " + Arrays.toString(arr));

		for (int i = 0; i < allmodsvalues.length; i++) {
			System.out.println();
			for (int j = 0; j < allmodsvalues[i].length; j++) {
				// System.out.print(allmodsvalues[i][j] + " - ");
				if (j == 0) {

				}
				if (j == 2) {
					// Add the mod to list {Mod Name},{Author},{status}
					myModel.addElement(new SorrMod(allmodsvalues[i][0], allmodsvalues[i][2],
							FuncoesMods.scanMod(allmodsvalues[i][0])));
					// System.out.println("Elemento acessado direto da lista: " +
					// DLM.get(j).toString());
				}
			}
		}

///////// Botão para ESCOLHER MOD /////////////////////////////////////////////////////////
		JButton btScan = new JButton("Options");
		btScan.setBackground(UIManager.getColor("Button.background"));
		btScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btScan.setBounds(32, 387, 146, 23);
		contentPane.add(btScan);

/////// Instalar Mod ///////////////////////////////////////////////////////
		JButton btInstall = new JButton("Install mod");
		btInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btInstall.setBounds(260, 387, 137, 23);
		contentPane.add(btInstall);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 48, 365, 307);
		contentPane.add(scrollPane);

		// Instanciando objeto personalizado

//////// LISTA DE MODS ////////////////////////////////////////////////////////////////////
		// Create Jlist using a model
		JList<SorrMod> listMod = new JList<SorrMod>(myModel);
		scrollPane.setViewportView(listMod);
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
	}
}
