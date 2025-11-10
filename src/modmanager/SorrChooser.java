package modmanager;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * This class is contais a dialog that appears when you have no no executable
 * path defined.
 */
public class SorrChooser extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	static String selectedDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SorrChooser dialog = new SorrChooser();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SorrChooser() {
		setTitle("Grupper");
		setBounds(100, 100, 450, 145);
		// The command below is responsible for "preventing" code execution
		// until the current dialog is closed.
		setModalityType(ModalityType.APPLICATION_MODAL);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Select your SorR Executable path:");
			lblNewLabel.setBounds(10, 11, 233, 14);
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
			contentPanel.add(lblNewLabel);
		}

		JLabel lblPath = new JLabel("");
		lblPath.setBounds(121, 40, 288, 14);
		contentPanel.add(lblPath);

		JPanel panel = new JPanel();
		panel.setBounds(10, 65, 414, 33);
		contentPanel.add(panel);

		// Confirm Button
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean fulfilled = true;
				String selectedPath = lblPath.getText();

				// The game doesn't work if you use an executable name different than
				// "sorr.exe",
				// this is why any other name won't be accepted here.
				if (!selectedPath.toLowerCase().endsWith("sorr.exe")) {
					String message = "The executable you selected must has the name of \"SorR.exe\"!";
					JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.WARNING_MESSAGE);
					fulfilled = false;
				} else {
					int pathSize = selectedPath.length();
					String cutPath = selectedPath.substring(0, pathSize - 9);
					System.out.println("Checking folders in:" + cutPath);
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
						Main.sorrPath = selectedDir;

						// The program will not closed
						Main.closeJVM = false;

						// Close dialog
						dispose();
					}
				}
			}
		});
		btnConfirm.setEnabled(false);
		panel.add(btnConfirm);

		{
			// Botão path
			JButton btnPath = new JButton("Browse...");
			btnPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String initialPath;
					String labelText = lblPath.getText();
					if (!labelText.isEmpty() && FuncMods.exist(labelText)) {
						initialPath = labelText;
					} else {
						initialPath = System.getProperty("user.dir");
					}

					// FileChooser object
					JFileChooser sorChooser = new JFileChooser(initialPath);
					sorChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					sorChooser.setAcceptAllFileFilterUsed(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Executable (*.exe)", "exe");
					sorChooser.setFileFilter(filter);

					// Show the file chooser dialog
					int result = sorChooser.showOpenDialog(null);
					if (result == JFileChooser.APPROVE_OPTION) {
						// User selected a file
						selectedDir = sorChooser.getSelectedFile().getParent();
						lblPath.setText(sorChooser.getSelectedFile().toString());
						lblPath.setToolTipText(sorChooser.getSelectedFile().toString());
						btnConfirm.setEnabled(true);
					} else {
						System.out.println("Operation canceled!");
					}
				}
			});
			btnPath.setBounds(10, 36, 101, 23);
			contentPanel.add(btnPath);
		}
	}

	/**
	 * Show an error message when an important file or folder is missing.
	 * 
	 * @param file
	 * @param additionalText
	 */
	public static void errorMsg(String file, String additionalText) {
		String message = "The " + additionalText + " \"" + file.replace("//", "/") + "\" was not found!\n"
				+ "The program needs that to work.";
		JOptionPane.showMessageDialog(new JFrame(), message, "WARNING", JOptionPane.WARNING_MESSAGE);
	}
}
