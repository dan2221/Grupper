package modmanager;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;

public class AboutG extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutG dialog = new AboutG();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutG() {
		setTitle("About");
		setResizable(false);
		setBounds(100, 100, 387, 237);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null); // Posição da janela

		JLabel lblNewLabel = new JLabel("Grupper v1.0 by Chavyn (daniel2221)");
		lblNewLabel.setBounds(55, 25, 281, 14);
		contentPanel.add(lblNewLabel);

		JTextPane txtpnProgramCreatedTo = new JTextPane();
		txtpnProgramCreatedTo.setEditable(false);
		txtpnProgramCreatedTo
				.setText("Grupper is a open source freeware created to facilitate the activation of SORR mods.");
		txtpnProgramCreatedTo.setBounds(35, 62, 301, 52);
		contentPanel.add(txtpnProgramCreatedTo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Github page");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Desktop desk = Desktop.getDesktop();

						// now we enter our URL that we want to open in our
						// default browser
						try {
							desk.browse(new URI("https://github.com/dan2221/Grupper"));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
