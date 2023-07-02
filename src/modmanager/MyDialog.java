package modmanager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileFilter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MyDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	static String selectedDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MyDialog dialog = new MyDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MyDialog() {
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
			contentPanel.add(lblNewLabel);
		}

		JLabel lblPath = new JLabel("");
		lblPath.setBounds(121, 40, 288, 14);
		contentPanel.add(lblPath);

		JPanel panel = new JPanel();
		panel.setBounds(10, 65, 414, 33);
		contentPanel.add(panel);

		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add game path to global variables
				Main.sorrPath = selectedDir;

				// Close dialog
				dispose();
			}
		});
		btnConfirm.setEnabled(false);
		panel.add(btnConfirm);

		{
			JButton btnPath = new JButton("Browse...");
			btnPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					// FileChooser object
					JFileChooser sorChooser = new JFileChooser();
					sorChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					sorChooser.setAcceptAllFileFilterUsed(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Executable (*.exe)", "exe");
					sorChooser.setFileFilter(filter);

					// Show the file chooser dialog
					int result = sorChooser.showOpenDialog(null);
					if (result == JFileChooser.APPROVE_OPTION) {
						// User selected a file
						selectedDir = sorChooser.getSelectedFile().getParent() + "\"";
						lblPath.setText(sorChooser.getSelectedFile().toString());
						btnConfirm.setEnabled(true);

					}
				}
			});
			btnPath.setBounds(10, 36, 101, 23);
			contentPanel.add(btnPath);
		}
	}
}
