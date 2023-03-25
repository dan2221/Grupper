package modmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

/**
 * Class used for load program configuration and refresh mod list.
 */
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
	 * Scan all mods for getting attributes to SorrMod Class and update the
	 * mod Jlist.
	 * 
	 * @param allmodsvalues
	 * @return
	 */
	public static DefaultListModel<SorrMod> refreshModList(String[][] allmodsvalues) {
		scanConfig();
		DefaultListModel<SorrMod> myModel = new DefaultListModel<>();
		boolean installed = false;
		for (int i = 0; i < Main.getModQuantity(); i++) {

			// Add the mod to list {Mod Name},{Author},{status}
			int modStatus = FuncMods.scanMod(allmodsvalues[i][1]);

			// System.out.println("Curent mod data: " + allmodsvalues[i][0] + " " +
			// allmodsvalues[i][2] + " " + modstatus);

			if (modStatus == 2) {
				if (configrupper[0] == false) {
					myModel.addElement(new SorrMod(allmodsvalues[i][0], allmodsvalues[i][2], modStatus));
				}
			} else {
				myModel.addElement(new SorrMod(allmodsvalues[i][0], allmodsvalues[i][2], modStatus));
			}

			// Check if one of the mods is installed
			if (modStatus == 1) {
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
			// Values to add to cfg file				
			String[] defaultconfig = { "//Grupper configuration file//\n", "hide_unavailable_mods=0;",
					"list_without_authors=0;", "installed_mod_first=0;" };
			try {
				new File("grupper.cfg").createNewFile();
				
				// Adding default values to the cfg file			
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
					String lineContent = line.replace("hide_unavailable_mods=", "").replace(";", "");
					if (lineContent.equals("1")) {
						configrupper[0] = true;
					} else {
						configrupper[0] = false;
					}
					System.out.println("hide_unavailable_mods=" + configrupper[0]);
				}

				if (line.startsWith("list_without_authors=") && line.endsWith(";")) {
					// Isolate value (1 or 0)
					String lineContent = line.replace("list_without_authors=", "").replace(";", "");
					if (lineContent.equals("1")) {
						configrupper[1] = true;
					} else {
						configrupper[1] = false;
					}
					System.out.println("list_without_authors=" + configrupper[1]);
				}

				if (line.startsWith("installed_mod_first=") && line.endsWith(";")) {
					// Isolate value (1 or 0)
					String lineContent = line.replace("installed_mod_first=", "").replace(";", "");
					if (lineContent.equals("1")) {
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
		String textToFind;

		// Get all content inside the grupper.cfg
		try {
			stream = new FileReader("grupper.cfg");
			try (BufferedReader reader = new BufferedReader(stream)) {
				String line = reader.readLine();
				// File content
				ArrayList<String> fileBefore = new ArrayList<String>();
				// Add each line to the array
				while (line != null) {
					if (!line.startsWith("//")) { // Ignore commented lines
						fileBefore.add(line);
					}
					line = reader.readLine(); // Next line of the file
				}

				// Write a new cfg file with the desired changes
				FileWriter writer = new FileWriter("grupper.cfg");
				writer.write("//Grupper configuration file//\n");
				for (String item : fileBefore) {
					switch (option) {
					case 0:
						textToFind = "hide_unavailable_mods=";
						if (item.startsWith(textToFind) && item.endsWith(";")) {
							// Isolate value (1 or 0)
							String lineContent = item.replace(textToFind, "").replace(";", "");
							if (lineContent.equals("1")) {
								writer.write(textToFind + "0;" + "\n");
							} else {
								writer.write(textToFind + "1;" + "\n");
							}
						} else {
							writer.write(item + "\n");
						}
						break;
					case 1:
						textToFind = "list_without_authors=";
						if (item.startsWith(textToFind) && item.endsWith(";")) {
							// Isolate value (1 or 0)
							String lineContent = item.replace(textToFind, "").replace(";", "");
							if (lineContent.equals("1")) {
								writer.write(textToFind + "0;" + "\n");
							} else {
								writer.write(textToFind + "1;" + "\n");
							}
						} else {
							writer.write(item + "\n");
						}
						break;
					case 2:
						textToFind = "installed_mod_first=";
						if (item.startsWith(textToFind) && item.endsWith(";")) {
							// Isolate value (1 or 0)
							String lineContent = item.replace(textToFind, "").replace(";", "");
							if (lineContent.equals("1")) {
								writer.write(textToFind + "0;" + "\n");
							} else {
								writer.write(textToFind + "1;" + "\n");
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
