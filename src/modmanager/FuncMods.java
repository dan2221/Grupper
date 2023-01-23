package modmanager;

import java.util.ArrayList; // import the ArrayList class

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.BufferedReader;

// Libraries for file checking
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FuncMods {

	/**
	 * Move desired file or folder. It works like the Batch Script move command.
	 * 
	 * @param origem
	 * @param destino
	 * 
	 */
	public static void move(String origem, String destino) {
		System.out.println("\nMoving file...");
		// Getting the filename or folder
		String[] tree = origem.split("//", 0);
		String item = tree[tree.length - 1];

		File param1 = new File(origem);
		File param2 = new File(destino + "//" + item);
		System.out.println("Origin: " + param1);
		System.out.println("Destiny: " + param2);
		try {
			Files.move(param1.toPath(), param2.toPath());
			System.out.println("\"" + item + "\" moved successfully!");
		} catch (IOException ex) {
			System.err.println("Error found!");
			ex.printStackTrace();
		}
	}

	/**
	 * Rename desired file or folder. It works like the Batch Script ren command.
	 * 
	 * @param origin
	 * @param newname
	 * 
	 */
	public static void ren(String origin, String newname) {
		// Getting the filename or folder
		String[] tree = origin.split("//", 0);
		String item = tree[tree.length - 1];
		System.out.println("\nRenaming: " + origin);
		System.out.println("To: " + newname);
		if (new File(origin.replace(item, newname)).exists()) {
			System.err.println("Impossible to rename! Already exists a file or folder with the same name!");
		} else {
			new File(origin).renameTo(new File(origin.replace(item, newname)));
		}
	}

	public static boolean exist(String arq) {
		return new File(arq).exists();
	}

	/**
	 * Search for files and folders needed for Grupper works correctly.
	 */
	public static void importantFiles() {
		// Check for important folders which contains mods
		String[] foldersToMake = { "data", "mod//chars", "mod//themes", "mod//games" };
		for (String i : foldersToMake) {
			File selectedFolder = new File(i);
			if (!selectedFolder.exists()) {
				selectedFolder.mkdirs();
				System.out.println(String.format("Directory \"%s\" created!", i));
			}
		}
		// Check for SorR palette folders
		String[] palFolders = { "chars", "backup_chars", "enemies", "backup_enemies" };
		for (String i : palFolders) {
			if (!new File("palettes//" + i).exists()) {
				errorMsg("palettes//" + i, "folder");
			}
		}
		// Check for SorR executable file
		if (!new File("SorR.exe").exists()) {
			errorMsg("SorR.exe", "file");
		}
	}

	/**
	 * Checks if exists any file with the desired extension.
	 * 
	 * @param diretorio
	 * @return boolean value
	 */
	public static boolean anyFile(String diretorio) {
		boolean existe = false;
		String params[] = diretorio.split("\\*");
		File folder = new File(params[0]);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file.isFile() && file.getName().endsWith(params[1])) {
					existe = true;
					break;
				}
			}
		}
		return existe;
	}

	/**
	 * Get all files inside a folder.
	 * 
	 * @param diretorio
	 * @return list of files as an ArrayList
	 */
	public static ArrayList<String> getAllFiles(String diretorio) {
		String[] params = diretorio.split("\\*");
		File folder = new File(params[0]);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> allFiles = new ArrayList<String>(); // Create an ArrayList object
		int count = 0;

		if (listOfFiles != null) {
			if (diretorio.endsWith("*.*")) { // No extension defined
				for (File file : listOfFiles) {
					if (file.isFile()) {
						allFiles.add(file.getName().toString());
						System.out.println(allFiles.get(count) + " added!");
						count++;
					}
				}
			} else { // extension defined
				for (File file : listOfFiles) {
					if (file.isFile() && file.getName().endsWith(params[1])) {
						allFiles.add(file.getName().toString());
						System.out.println(allFiles.get(count) + " added!");
						count++;
					}
				}
			}
		}
		// System.out.println(allfiles); // print array
		return allFiles;
	}

	/**
	 * Get all folders inside a directory.
	 * 
	 * @param diretorio
	 * @return list of folders as an ArrayList
	 */
	public static ArrayList<String> getAllFolders(String diretorio) {
		System.out.println("\nGetting all mod foldes in \"" + diretorio + "\" directory...");
		File folder = new File(diretorio);
		File[] listOfFolders = folder.listFiles();
		ArrayList<String> allFolders = new ArrayList<String>(); // Create an ArrayList object
		int count = 0;

		if (listOfFolders != null) {
			for (File item : listOfFolders) {
				if (item.isDirectory()) {
					allFolders.add(item.getName().toString());
					System.out.println(allFolders.get(count) + " added!");
					count++;
				}
			}
		}
		return allFolders;
	}

	/**
	 * Read a txt file to colect all mod data.
	 * 
	 * @param filename
	 * @return
	 */
	public static String[] readTxt(String filename) {
		String[] modData = new String[3];
		modData[0] = filename.replace(".txt", "");
		System.out.println("-----------------------\nReading " + filename + "...");

		try {
			// Open the file
			FileReader stream = new FileReader("mod_list//".concat(filename));
			BufferedReader reader = new BufferedReader(stream);

			// Method that reads a line from the file
			String line = reader.readLine();
			while (line != null) {

				if (line.startsWith("Mod Title:")) {
					// Isolating mod title
					modData[1] = line.replace("Mod Title:", "").replace("\n", "");
				}
				if (line.startsWith("Author:")) {
					// Isolating mod author name
					modData[2] = line.replace("Author:", "").replace("\n", "");
				}
				// Read next line of the file
				line = reader.readLine();
			}
			// If a mod doesn't have a title, its folder name will be its title
			if (modData[1] == null) {
				modData[1] = modData[0];
			}
			// If a mod doesn't have a declared author on its txt, the string below
			// will be used.
			if (modData[2] == null) {
				modData[2] = " - ";
			}
			// Close the reading
			reader.close();
			// Close the file
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Mod data ------------------");
		System.out.println("Folder: " + modData[0]);
		System.out.println("Title: " + modData[1]);
		System.out.println("Author: " + modData[2]);

		return modData;
	}

	/**
	 * Checks the status of the mod.
	 * 
	 * @param proj
	 * @return 1: installed, 2: unavaliable, 0: available
	 */
	public static int scanMod(String proj) {
		int status = 0;

		System.out.println("\nSearching for " + proj + "...");
		// Creates a palette folder if it doesn't exists
		if (!new File("mod//games//" + proj + "//palettes").exists()) {
			new File("mod//games//" + proj + "//palettes").mkdirs();
			System.out.println("\"palettes\" folder created in \"" + proj + "\" project!");
		}
		// Checking for enemie's palettes
		if (anyFile("mod//games//" + proj + "//palettes//enemies//*.pal")) {
			System.out.println("Enemies palettes found!");
			status = 0; // Disabled mod
		} else {
			// Auto fix enemies palette folder path
			if (anyFile("mod//games//" + proj + "//enemies//*.pal")) {
				move("mod//games//" + proj + "//enemies", "mod//games//" + proj + "//palettes");
			} else {
				if (anyFile("palettes//sorr_enemies//*.pal")) {
					if (exist("mod//" + proj + ".txt")) {
						status = 1; // Installed mod
						System.out.println("The mod is installed!");
					} else {
						status = 2; // Unavailable mod
						System.out.println("Unavailable mod! Txt mod file not found!");
					}
				} else {
					System.out.println("The mod can't be installed! \"sorr_enemies\" folder not found!");
					status = 2; // Unavaliable modd
				}
			}
		}

		System.out.println("Scanning completed!");
		System.out.println("--------------------------------------------------");
		return status;
	}

	public static String existsInstallation() {
		String[][] allModsValues = Main.getAllModData();
		String installed = null;
		for (int i = 0; i < Main.getModQuantity(); i++) {
			if (scanMod(allModsValues[i][0]) == 1) {
				System.out.println("\nInstalled: " + allModsValues[i][0]);
				installed = allModsValues[i][0];
				break;
			}
		}
		return installed;
	}

	/**
	 * Install the mod
	 * 
	 * @param selectedMod
	 */
	public static void installMod(String selectedMod) {
		// This is for "First mod of the list" configuration
		if (Start.getConfig()[2]) {
			ren("mod//games//" + selectedMod, "- " + selectedMod);
			selectedMod = "- " + selectedMod;
		}

		System.out.println("Installing " + selectedMod + "...");

		// Palettes
		if (new File("mod//games//" + selectedMod + "//palettes//enemies").exists()) {
			ren("palettes//enemies", "sorr_enemies");
			move("mod//games//" + selectedMod + "//palettes//enemies", "palettes");

		}

		// List all data files in a txt file
		if (new File("mod//games//" + selectedMod + "//data").exists()) {
			ArrayList<String> dataFiles = getAllFiles("mod//games//" + selectedMod + "//data//*.*");
			try (FileWriter writer = new FileWriter("mod//sorr.txt")) {
				for (String item : dataFiles) {
					System.out.println(item);
					writer.write(item + "\n");

					// Your data files will not be replaced by mod's files, because the mod's files
					// receives "[mod]" in their names when exists files with the same names in SorR
					// data folder.
					if (new File("data//" + item).exists()) {
						ren("mod//games//" + selectedMod + "//data//" + item, "[mod]" + item);
						move("mod//games//" + selectedMod + "//data//[mod]" + item, "data");
					} else {
						move("mod//games//" + selectedMod + "//data//" + item, "data");
					}
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Renaming the txt for identify mod installation
			ren("mod//sorr.txt", selectedMod + ".txt");
		}
	}

	public static void uninstallMod(String proj) {
		System.out.println("Uninstalling \"" + proj + "\"...");

		// Palettes
		move("palettes//enemies", "mod//games//" + proj + "//palettes");
		ren("palettes//sorr_enemies", "enemies");

		try {
			// Opens the file
			FileReader stream = new FileReader("mod//" + proj + ".txt");
			BufferedReader reader = new BufferedReader(stream);

			// Method that reads a line from the file
			String line = reader.readLine();
			while (line != null) {

				// Isolating mod txt file name
				String modFile = line.replace("\n", "");

				// If a file has "[mod]" in its name, these characters will be removed
				// during the uninstalling.
				if (new File("data//[mod]" + modFile).exists()) {
					move("data//[mod]" + modFile, "mod//games//" + proj + "//data");
					ren("mod//games//" + proj + "//data//" + "[mod]" + modFile, modFile);
				} else {
					if (new File("data//" + modFile).exists()) {
						move("data//" + modFile, "mod//games//" + proj + "//data");
					}
				}

				// Read next line of the file
				line = reader.readLine();
			}
			reader.close(); // Close the reading
			stream.close(); // Close the file
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fileToDelete = new File("mod//" + proj + ".txt");
		if (fileToDelete.delete()) {
			System.out.println("\"mod//" + proj + ".txt\" deleted!");
		} else {
			System.err.println("Failed to delete \"mod//" + proj + ".txt\"");
		}
		// Create sorr txt file		
		try {
			new File("mod//sorr.txt").createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Remove "- " from the mod folder if the configuration for set
		// installed mod as the first one of the list is activated.
		if (Start.getConfig()[2]) {
			if (proj.startsWith("- ")) {
				// Remove 2 first characters of the folder.
				ren("mod//games//" + proj, proj.substring(2));
				System.out.println("Renamed to " + proj.substring(2));
			}
		}
		System.out.println("Uninstalled!");
	}

	/**
	 * Show an error message when an important file or folder is missing.
	 * 
	 * @param file
	 * @param additionalText
	 */
	public static void errorMsg(String file, String additionalText) {
		String message = "The " + additionalText + " \"" + file.replace("//", "/") + "\" was not found!\n"
				+ "The program needs that to work.";
		JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
}