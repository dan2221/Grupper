package modmanager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SupportDialog {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new SupportDialog().createAndShowGUI());
	}

	public void createAndShowGUI() {
		// Criação do JFrame
		JFrame frame = new JFrame("Opções de Apoio");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLayout(new BorderLayout());

		// Texto informativo
		JTextArea textArea = new JTextArea();
		textArea.setText("Considere apoiar o desenvolvimento de novos softwares para a comunidade beatemap.\n\n"
				+ "Ko-Fi: https://ko-fi.com/danchavyn\n" + "PayPal: https://www.paypal.com/donate/...\n"
				+ "PIX: 888a0f24-fc77-4105-b8ea-e1f919849c4f\n" + "YouTube Super Thanks: disponível em qualquer vídeo");
		textArea.setEditable(false);

		// Adicionando JTextArea ao JFrame
		frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

		// Botão de fechar
		JButton closeButton = new JButton("Fechar");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// Adicionando botão ao JFrame
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(closeButton);
		frame.add(buttonPanel, BorderLayout.SOUTH);

		// Configurações do JFrame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
