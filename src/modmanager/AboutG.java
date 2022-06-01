package modmanager;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AboutG extends JDialog {

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

		JLabel lblNewLabel = new JLabel("Grupper (beta 3) v1.0 by Chavyn (daniel2221)", SwingConstants.CENTER);
		lblNewLabel.setBounds(48, 11, 267, 14);
		contentPanel.add(lblNewLabel);

		JTextPane txtpnProgramCreatedTo = new JTextPane();
		txtpnProgramCreatedTo.setEditable(false);
		txtpnProgramCreatedTo.setText(
				"Grupper is an open source freeware created to facilitate the activation of SORR mods.\r\n\r\nHave fun with awesome projects by all mod creators!\r\n\r\nThis software was Developed in Eclipse IDE (Java).");
		txtpnProgramCreatedTo.setBounds(34, 36, 307, 104);
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
							// tente e veja se ainda aparece aviso de licen�a.
							// Desktop.getDesktop().browse(new
							// URL("https://github.com/dan2221/Grupper").toURI());
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
