package modmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchItemsApp {
	// pasta onde os arquivos .fpg devem estar
	private static final Path ROOT_DIR = Paths.get("fpg_files"); // ajuste ou passe via args

	// Definição interna dos itens: nome do arquivo -> características
	private static final List<Item> KNOWN_ITEMS = Arrays.asList(
			new Item("abadede.fpg", Arrays.asList("wrestler", "strong")),
			new Item("adam.fpg", Arrays.asList("playable", "adam")),
			new Item("adam.wav", Arrays.asList("playable", "adam")),
			new Item("adam1.wav", Arrays.asList("playable", "adam", "death")),
			new Item("gato.fpg",
					Arrays.asList("miau", "caixa de areia", "ronronar", "delicadeza", "caça ratos", "come peixe")),
			new Item("pombo.fpg", Arrays.asList("voa", "pombal", "bicar", "miguel")) // exemplo extra
	);

	private JFrame frame;
	private JTextField searchField;
	private DefaultListModel<Item> listModel;
	private JList<Item> resultList;
	private List<Item> items = new ArrayList<>();

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Path root = args.length > 0 ? Paths.get(args[0]) : ROOT_DIR;
			new SearchItemsApp(root).start();
		});
	}

	public SearchItemsApp(Path rootDir) {
		prepareItems(rootDir);
		buildGui(rootDir);
	}

	private void start() {
		frame.setVisible(true);
	}

	// Classe representando item
	private static class Item {
		final String name;
		final List<String> features;
		boolean exists;
		Path path;

		Item(String name, List<String> features) {
			this.name = name;
			this.features = new ArrayList<>(features);
		}

		@Override
		public String toString() {
			return name + (exists ? "" : " (ausente)");
		}
	}

	// Preenche lista de itens a partir da definição interna e verifica existência
	// em rootDir
	private void prepareItems(Path rootDir) {
		items.clear();
		for (Item k : KNOWN_ITEMS) {
			Item copy = new Item(k.name, k.features);
			copy.path = rootDir.resolve(k.name);
			copy.exists = Files.exists(copy.path);
			items.add(copy);
		}
	}

	private void buildGui(Path rootDir) {
		frame = new JFrame("Busca de Itens (internos)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);

		JPanel top = new JPanel(new BorderLayout(6, 6));
		top.add(new JLabel("Pesquisar (característica): "), BorderLayout.WEST);
		searchField = new JTextField();
		top.add(searchField, BorderLayout.CENTER);

		listModel = new DefaultListModel<>();
		resultList = new JList<>(listModel);
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultList.setCellRenderer(new DefaultListCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Item) {
					Item it = (Item) value;
					setText(it.toString());
					setForeground(it.exists ? (isSelected ? getForeground() : Color.BLACK) : Color.GRAY);
				}
				return c;
			}
		});

		resultList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Item sel = resultList.getSelectedValue();
					if (sel == null)
						return;
					if (!sel.exists) {
						JOptionPane.showMessageDialog(frame, "Arquivo ausente: " + sel.name, "Aviso",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					try {
						Desktop.getDesktop().open(sel.path.toFile());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "Não foi possível abrir: " + ex.getMessage(), "Erro",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		frame.getContentPane().setLayout(new BorderLayout(8, 8));
		frame.getContentPane().add(top, BorderLayout.NORTH);
		frame.getContentPane().add(new JScrollPane(resultList), BorderLayout.CENTER);

		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(new JLabel("Diretório: " + rootDir.toAbsolutePath()), BorderLayout.WEST);
		JButton refresh = new JButton("Verificar pasta");
		refresh.addActionListener(e -> {
			prepareItems(rootDir);
			filterAndShow(searchField.getText());
		});
		bottom.add(refresh, BorderLayout.EAST);
		frame.getContentPane().add(bottom, BorderLayout.SOUTH);

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				filterAndShow(searchField.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				filterAndShow(searchField.getText());
			}

			public void changedUpdate(DocumentEvent e) {
				filterAndShow(searchField.getText());
			}
		});

		filterAndShow("");
	}

	private void filterAndShow(String term) {
		String q = term == null ? "" : term.trim().toLowerCase(Locale.ROOT);
		listModel.clear();
		for (Item it : items) {
			if (q.isEmpty()) {
				listModel.addElement(it);
				continue;
			}
			boolean matchName = it.name.toLowerCase(Locale.ROOT).contains(q);
			boolean matchFeature = it.features.stream().anyMatch(f -> f.toLowerCase(Locale.ROOT).contains(q));
			if (matchName || matchFeature) {
				listModel.addElement(it);
			}
		}
	}

}
