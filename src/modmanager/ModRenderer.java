package modmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

/**
 * This class edits individual mod itens displayed on mod list.
 */
public class ModRenderer extends JPanel implements ListCellRenderer<SorrMod> {

	private static final long serialVersionUID = 1L;
	private JLabel lbIcon = new JLabel();
	private JLabel lbName = new JLabel();
	private JLabel lbAuthor = new JLabel();
	private JPanel panelText;
	private JPanel panelIcon;

	public ModRenderer() {
		setLayout(new BorderLayout(5, 5));

		panelText = new JPanel(new GridLayout(0, 1));
		panelText.add(lbName);
		panelText.add(lbAuthor);

		panelIcon = new JPanel();
		panelIcon.setBorder(new EmptyBorder(2, 4, 2, 4));
		panelIcon.add(lbIcon);

		add(panelIcon, BorderLayout.WEST);
		add(panelText, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends SorrMod> list, SorrMod SorrMod, int index,
			boolean isSelected, boolean cellHasFocus) {

		// insert mod logo file.
		if (new File(Main.sorrPath + "//mod//games//" + SorrMod.getName() + "//logo.png").exists()) {
			lbIcon.setIcon(new ImageIcon(Main.sorrPath + "\\mod\\games\\" + SorrMod.getName() + "\\logo.png"));
		} else {
			lbIcon.setIcon(new ImageIcon(getClass().getResource("/images/default_logo.png")));
		}

		lbName.setText(SorrMod.getName());
		lbAuthor.setText(SorrMod.getAuthor());
		lbAuthor.setForeground(Color.blue);

		// set Opaque to change background color of JLabel
		lbName.setOpaque(true);
		lbAuthor.setOpaque(true);
		lbIcon.setOpaque(true);

//		Font currentFont = lbName.getFont();
//		Font newFont = currentFont.deriveFont(currentFont.getSize() + 2);
//		lbName.setFont(newFont);

		// Load conguration
		Start.scanConfig();

		// Custom colors using RGB values
		Color backColorSel = new Color(55, 55, 55);
		Color backColorNoSel = new Color(34, 34, 34);

		// Font colors for authors
		Color avColorAuthor = new Color(64, 150, 255);
		Color unColorAuthor = new Color(87, 109, 169);

		// Font colots for mod names
		Color availableTextColor = new Color(220, 220, 220);
		Color unavailableTextColor = new Color(180, 80, 82);

		// to use default colors: setBackground(list.getBackground());

		// Assumes the stuff in the list has a pretty toString
		// setName(value.toString());

		boolean hideauthor = Start.getConfig()[1];

		if (isSelected) {
			setBackground(backColorSel);
			lbName.setBackground(backColorSel);
			if (hideauthor) {
				lbAuthor.setForeground(backColorSel);
			}
			lbAuthor.setBackground(backColorSel);
			lbIcon.setBackground(backColorSel);
			panelIcon.setBackground(backColorSel);
		} else { // when don't select
			setBackground(backColorNoSel);
			lbName.setBackground(backColorNoSel);
			if (hideauthor) {
				lbAuthor.setForeground(backColorNoSel);
			}
			lbAuthor.setBackground(backColorNoSel);
			lbIcon.setBackground(backColorNoSel);
			panelIcon.setBackground(backColorNoSel);
		}

		if (SorrMod.getStatus() == 2) { // Unavailable mod
			lbName.setForeground(unavailableTextColor);
			if (!hideauthor) {
				lbAuthor.setForeground(unColorAuthor);
			}
			lbIcon.setForeground(unavailableTextColor);
		} else { // Available mod
			lbName.setForeground(availableTextColor);
			if (!hideauthor) {
				lbAuthor.setForeground(avColorAuthor);
			}
			lbIcon.setForeground(availableTextColor);
		}

		// When select an item
		Font myFont1 = new Font("Monospace", Font.BOLD, 14);
		lbName.setFont(myFont1);
		return this;
	}
}
