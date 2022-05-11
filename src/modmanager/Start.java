package modmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.DefaultListModel;

public class Start {

	/**
	 * Grupper configuration array:
	 * 
	 * 0: Hide unavailable mods; 1: Put the installed mod as the first one of the
	 * sormaker list.
	 */
	private static boolean[] configrupper = new boolean[2];

	/**
	 * Scan all mods to update the mods Jlist.
	 * 
	 * @param allmodsvalues
	 * @return
	 */
	public static DefaultListModel<SorrMod> refreshModList(String[][] allmodsvalues) {
		scanConfig();
		DefaultListModel<SorrMod> myModel = new DefaultListModel<>();
		boolean installed = false;
		for (int i = 0; i < allmodsvalues.length; i++) {
			System.out.println();
			for (int j = 0; j < allmodsvalues[i].length; j++) {
				if (j == 2) {
					// Add the mod to list {Mod Name},{Author},{status}
					int modstatus = FuncMods.scanMod(allmodsvalues[i][1]);
					if (modstatus == 2) {
						if (configrupper[0] == false) {
							myModel.addElement(new SorrMod(allmodsvalues[i][0], allmodsvalues[i][2], modstatus));
						}
					} else {
						myModel.addElement(new SorrMod(allmodsvalues[i][0], allmodsvalues[i][2], modstatus));
					}

					// Check if one of the mods is installed
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

	/**
	 * Get all configuration variables from a cfg file.
	 */
	public static void scanConfig() {
		// If there aren't a configuration file, it will be created.
		if (!new File("grupper.cfg").exists()) {
			File makesor = new File("grupper.cfg");
			String[] defaultconfig = { "//Grupper configuration file//\n", "hide_unavailable_mods=0;",
					"installed_mod_first=0;" };
			try {
				makesor.createNewFile();
				try (FileWriter writer = new FileWriter("grupper.cfg")) {
					for (String item : defaultconfig) {
						writer.write(item + "\n");
					}
					writer.close();
				}
				System.out.println("Configuration file created with the default values!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Reading the cfg file
		System.out.println("Configuration ------------------------------");
		try {
			FileReader stream = new FileReader("grupper.cfg");
			BufferedReader reader = new BufferedReader(stream);

			String line = reader.readLine();
			while (line != null) {
				if (line.startsWith("hide_unavailable_mods=") && line.endsWith(";")) {
					// Isolate value (1 or 0)
					String linecontent = line.replace("hide_unavailable_mods=", "").replace(";", "");
					if (linecontent.equals("1")) {
						configrupper[0] = true;
					} else {
						configrupper[0] = false;
					}
					System.out.println("hide_unavailable_mods=" + configrupper[0]);
				}

				if (line.startsWith("installed_mod_first=") && line.endsWith(";")) {
					// Isolate value (1 or 0)
					String linecontent = line.replace("installed_mod_first=", "").replace(";", "");
					if (linecontent.equals("1")) {
						configrupper[1] = true;
					} else {
						configrupper[1] = false;
					}
					System.out.println("installed_mod_first=" + configrupper[1]);
				}

				// Next line of the file
				line = reader.readLine();

			}
			// Closes the reading
			reader.close();
			// Closes the file
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
