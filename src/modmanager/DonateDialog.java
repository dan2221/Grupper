package modmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DonateDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtaffcbeaefcf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DonateDialog dialog = new DonateDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openLink(String urlString) {
		try {
			URI uri = new URI(urlString);
			Desktop.getDesktop().browse(uri);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DonateDialog() {
		setTitle("Supporting options");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("<html><body>Consider supporting me to help the development of\r\n"
				+ "new software for the beatemup community.</body></html>");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setBounds(10, 11, 281, 42);
		contentPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("PayPal:");
		lblNewLabel_1.setBounds(10, 64, 58, 14);
		contentPanel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Ko-FI:");
		lblNewLabel_2.setBounds(10, 89, 46, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("PIX:");
		lblNewLabel_3.setBounds(10, 114, 46, 14);
		contentPanel.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("YT Super Thanks:");
		lblNewLabel_4.setBounds(10, 139, 111, 14);
		contentPanel.add(lblNewLabel_4);

		JLabel lblPayPalLink = new JLabel("<html><u>https://www.paypal.com/donate/...</u></html>");
		lblPayPalLink.setToolTipText("https://www.paypal.com/donate/?hosted_button_id=RK8T3UG4T2LCU");
		lblPayPalLink.setForeground(new Color(0, 0, 255));
		lblPayPalLink.setBounds(62, 64, 229, 14);
		lblPayPalLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPanel.add(lblPayPalLink);
		lblPayPalLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openLink("https://www.paypal.com/donate/?hosted_button_id=RK8T3UG4T2LCU");
			}
		});

		JLabel lblKofiLink = new JLabel("<html><u>https://ko-fi.com/danchavyn</u></html>");
		lblKofiLink.setToolTipText("https://ko-fi.com/danchavyn");
		lblKofiLink.setForeground(Color.BLUE);
		lblKofiLink.setBounds(62, 89, 229, 14);
		lblKofiLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPanel.add(lblKofiLink);
		lblKofiLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openLink("https://ko-fi.com/danchavyn");
			}
		});

		JLabel lblNewLabel_5_1_1 = new JLabel("<html><u>Available on any video</u></html>");
		lblNewLabel_5_1_1.setToolTipText("https://www.youtube.com/playlist?list=PLa-mXLTenBmKFWyTz2OeIF-b6gNuYOWzw");
		lblNewLabel_5_1_1.setForeground(Color.BLUE);
		lblNewLabel_5_1_1.setBounds(135, 139, 156, 14);
		lblNewLabel_5_1_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPanel.add(lblNewLabel_5_1_1);
		lblNewLabel_5_1_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openLink("https://www.youtube.com/playlist?list=PLa-mXLTenBmKFWyTz2OeIF-b6gNuYOWzw");
			}
		});

		txtaffcbeaefcf = new JTextField();
		txtaffcbeaefcf.setText("888a0f24-fc77-4105-b8ea-e1f919849c4f");
		txtaffcbeaefcf.setBounds(61, 111, 230, 20);
		contentPanel.add(txtaffcbeaefcf);
		txtaffcbeaefcf.setColumns(10);
	}
}
