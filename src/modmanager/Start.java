package modmanager;

import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;

public class Start {
	/**
	 * Scan all mods to update the mods Jlist.
	 * 
	 * @param allmodsvalues
	 * @return
	 */
	public static DefaultListModel<SorrMod> refreshModList(String[][] allmodsvalues) {
		DefaultListModel<SorrMod> myModel = new DefaultListModel<>();
		boolean installed = false;
		for (int i = 0; i < allmodsvalues.length; i++) {
			System.out.println();
			for (int j = 0; j < allmodsvalues[i].length; j++) {
				if (j == 0) {

				}
				if (j == 2) {
					// Add the mod to list {Mod Name},{Author},{status}
					int modstatus = FuncMods.scanMod(allmodsvalues[i][0]);
					myModel.addElement(new SorrMod(allmodsvalues[i][0], allmodsvalues[i][2], modstatus));
					if (modstatus == 1) {
						installed = true;
					}
				}
			}
		}
		if (installed == false) {
			try {
				File myObj = new File("mod//sorr.txt");
				if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		return myModel;
	}
}
