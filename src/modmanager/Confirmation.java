package modmanager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class Confirmation extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Confirmation dialog = new Confirmation();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Confirmation() {
		setBounds(100, 100, 326, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Chose the kind of palette below.");
		lblNewLabel.setBounds(63, 27, 380, 13);
		contentPanel.add(lblNewLabel);
		{
			JLabel lblNewLabel_1 = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
			lblNewLabel_1.setBounds(8, 16, 47, 41);
			contentPanel.add(lblNewLabel_1);
		}
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Enemies");
		chckbxNewCheckBox.setBounds(8, 73, 124, 21);
		contentPanel.add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Characters");
		chckbxNewCheckBox_1.setBounds(132, 73, 109, 21);
		contentPanel.add(chckbxNewCheckBox_1);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Restore");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
