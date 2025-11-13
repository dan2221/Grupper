package modmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

public class SearchTextApp {
    private static final Path ROOT_DIR = Paths.get(Main.gameAssetsPath);       // pasta dos .fpg
    private static final Path ITEMS_FILE = Paths.get("wordlist.txt");     // arquivo com dados

    private JFrame frame;
    private JTextField searchField;
    private DefaultListModel<Item> model = new DefaultListModel<>();
    private JList<Item> list;

    static class Item {
        final String name;
        final List<String> features;
        boolean exists;
        Path path;
        Item(String name, List<String> features){ this.name=name; this.features=new ArrayList<>(features); }
        public String toString(){ return name + (exists ? "" : " (absent)"); }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new SearchTextApp().start());
    }

    private List<Item> items = new ArrayList<>();

    private void start(){
        loadItemsFromText();
        buildGui();
        frame.setVisible(true);
    }

    private void loadItemsFromText(){
        items.clear();
        try {
            if (!Files.exists(ITEMS_FILE)) {
                // cria exemplo se não existir
                Files.write(ITEMS_FILE, Arrays.asList(
                    "cachorro.fpg|hidrante,osso,au au,latido,rabo abanando,poste",
                    "gato.fpg|miau,caixa de areia,ronronar,delicadeza,caça ratos,come peixe"
                ), StandardOpenOption.CREATE);
            }
            List<String> lines = Files.readAllLines(ITEMS_FILE);
            for (String ln : lines) {
                String s = ln.trim();
                if (s.isEmpty() || s.startsWith("#")) continue;
                String[] parts = s.split("\\|",2);
                String name = parts[0].trim();
                List<String> feats = parts.length>1 ?
                        Arrays.stream(parts[1].split(","))
                              .map(String::trim)
                              .filter(x->!x.isEmpty())
                              .collect(Collectors.toList())
                        : Collections.emptyList();
                Item it = new Item(name, feats);
                it.path = ROOT_DIR.resolve(name);
                it.exists = Files.exists(it.path);
                items.add(it);
            }
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Erro ao ler " + ITEMS_FILE + ": " + e.getMessage());
        }
    }

    private void buildGui(){
        frame = new JFrame("Busca (texto)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);

        JPanel top = new JPanel(new BorderLayout(6,6));
        top.add(new JLabel("Pesquisar:"), BorderLayout.WEST);
        searchField = new JTextField();
        top.add(searchField, BorderLayout.CENTER);

        list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new DefaultListCellRenderer(){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component getListCellRendererComponent(JList<?> list,Object value,int idx,boolean sel,boolean foc){
                Component c = super.getListCellRendererComponent(list,value,idx,sel,foc);
                if (value instanceof Item){
                    Item it = (Item)value;
                    setText(it.toString());
                    setForeground(it.exists ? (sel?getForeground():Color.BLACK) : Color.GRAY);
                }
                return c;
            }
        });

        list.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if (e.getClickCount()==2){
                    Item it = list.getSelectedValue();
                    if (it==null) return;
                    if (!it.exists){
                        JOptionPane.showMessageDialog(frame, "Arquivo ausente: " + it.name);
                        return;
                    }
                    try { Desktop.getDesktop().open(it.path.toFile()); }
                    catch (Exception ex){ JOptionPane.showMessageDialog(frame, "Erro ao abrir: " + ex.getMessage()); }
                }
            }
        });

        frame.getContentPane().setLayout(new BorderLayout(8,8));
        frame.getContentPane().add(top, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Pasta: " + ROOT_DIR.toAbsolutePath()), BorderLayout.WEST);
        JButton refresh = new JButton("Recarregar");
        refresh.addActionListener(e -> { loadItemsFromText(); filter(searchField.getText()); });
        bottom.add(refresh, BorderLayout.EAST);
        frame.getContentPane().add(bottom, BorderLayout.SOUTH);

        searchField.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){ filter(searchField.getText()); }
            public void removeUpdate(DocumentEvent e){ filter(searchField.getText()); }
            public void changedUpdate(DocumentEvent e){ filter(searchField.getText()); }
        });

        filter("");
    }

    private void filter(String term){
        String q = term==null? "": term.trim().toLowerCase(Locale.ROOT);
        model.clear();
        for (Item it : items){
            if (q.isEmpty()) { model.addElement(it); continue; }
            boolean nameMatch = it.name.toLowerCase(Locale.ROOT).contains(q);
            boolean featMatch = it.features.stream().anyMatch(f->f.toLowerCase(Locale.ROOT).contains(q));
            if (nameMatch || featMatch) model.addElement(it);
        }
    }
}
