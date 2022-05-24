package modmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class Start {

	/**
	 * Grupper configuration array:
	 * 
	 * 0: Hide unavailable mods; 1: Hide authors from the list; 2: Put the installed
	 * mod as the first one of the sormaker list.
	 */
	private static boolean[] configrupper = new boolean[3];

	public static boolean[] getConfig() {
		return configrupper;
	}

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
		for (int i = 0; i < Janela.getModQuantity(); i++) {

			// Add the mod to list {Mod Name},{Author},{status}
			int modstatus = FuncMods.scanMod(allmodsvalues[i][1]);

			// System.out.println("Curent mod data: " + allmodsvalues[i][0] + " " +
			// allmodsvalues[i][2] + " " + modstatus);

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
					"list_without_authors=0;", "installed_mod_first=0;" };
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
		System.out.println("\nYour configuration:");
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

				if (line.startsWith("list_without_authors=") && line.endsWith(";")) {
					// Isolate value (1 or 0)
					String linecontent = line.replace("list_without_authors=", "").replace(";", "");
					if (linecontent.equals("1")) {
						configrupper[1] = true;
					} else {
						configrupper[1] = false;
					}
					System.out.println("list_without_authors=" + configrupper[1]);
				}

				if (line.startsWith("installed_mod_first=") && line.endsWith(";")) {
					// Isolate value (1 or 0)
					String linecontent = line.replace("installed_mod_first=", "").replace(";", "");
					if (linecontent.equals("1")) {
						configrupper[2] = true;
					} else {
						configrupper[2] = false;
					}
					System.out.println("installed_mod_first=" + configrupper[2]);
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
		System.out.println("----------------------");
	}

	/**
	 * Change one value in the grupper.cfg.
	 * 
	 * @param option (0 to 1)
	 */
	public static void changeConfig(int option) {
		FileReader stream;
		String textofind;

		// Get all content inside the grupper.cfg
		try {
			stream = new FileReader("grupper.cfg");
			try (BufferedReader reader = new BufferedReader(stream)) {
				String line = reader.readLine();
				// File content
				ArrayList<String> filebefore = new ArrayList<String>();
				// Add each line to the array
				while (line != null) {
					if (!line.startsWith("//")) { // Ignore commented lines
						filebefore.add(line);
					}
					line = reader.readLine(); // Next line of the file
				}

				// Write a new cfg file with the desired changes
				FileWriter writer = new FileWriter("grupper.cfg");
				writer.write("//Grupper configuration file//\n");
				for (String item : filebefore) {
					switch (option) {
					case 0:
						textofind = "hide_unavailable_mods=";
						if (item.startsWith(textofind) && item.endsWith(";")) {
							// Isolate value (1 or 0)
							String linecontent = item.replace(textofind, "").replace(";", "");
							if (linecontent.equals("1")) {
								writer.write(textofind + "0;" + "\n");
							} else {
								writer.write(textofind + "1;" + "\n");
							}
						} else {
							writer.write(item + "\n");
						}
						break;
					case 1:
						textofind = "list_without_authors=";
						if (item.startsWith(textofind) && item.endsWith(";")) {
							// Isolate value (1 or 0)
							String linecontent = item.replace(textofind, "").replace(";", "");
							if (linecontent.equals("1")) {
								writer.write(textofind + "0;" + "\n");
							} else {
								writer.write(textofind + "1;" + "\n");
							}
						} else {
							writer.write(item + "\n");
						}
						break;
					case 2:
						textofind = "installed_mod_first=";
						if (item.startsWith(textofind) && item.endsWith(";")) {
							// Isolate value (1 or 0)
							String linecontent = item.replace(textofind, "").replace(";", "");
							if (linecontent.equals("1")) {
								writer.write(textofind + "0;" + "\n");
							} else {
								writer.write(textofind + "1;" + "\n");
							}
						} else {
							writer.write(item + "\n");
						}
						break;
					}
				}
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanConfig(); // Update all configuration variables
	}
}
