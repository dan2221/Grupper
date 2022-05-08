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
		// Getting the filename or folder
		String[] tree = origem.split("//", 0);
		String item = tree[tree.length - 1];

		File param1 = new File(origem);
		File param2 = new File(destino + "//" + item);
		System.out.println("Origin: " + param1);
		System.out.println("Destiny: " + param2);
		try {
			Files.move(param1.toPath(), param2.toPath());
			System.out.println("\"" + item + "\" moved successfully.");
		} catch (IOException ex) {
			System.out.println("Error found!");
			ex.printStackTrace();
		}
	}

	/**
	 * Rename desired file or folder. It works like the Batch Script move command.
	 * 
	 * @param origin
	 * @param newname
	 * 
	 */
	public static void ren(String origin, String newname) {
		// Getting the filename or folder
		String[] tree = origin.split("//", 0);
		String item = tree[tree.length - 1];
		System.out.println("Renaming: " + origin);
		System.out.println("To: " + newname);
		new File(origin).renameTo(new File(origin.replace(item, newname)));
	}

	public static boolean exist(String arq) {
		return new File(arq).exists();
	}

	/**
	 * Search for files and folders needed for Grupper works correctly.
	 */
	public static void importantFiles() {
		String[] folders2make = { "data", "mod//chars", "mod//themes", "mod//games" };
		for (String i : folders2make) {
			File selIndex = new File(i);
			if (!selIndex.exists()) {
				selIndex.mkdirs();
				System.out.println(String.format("Directory \"%s\" created!", i));
			}
		}
		String[] pal_folders = { "palettes//chars", "palettes//backup_chars", "palettes//enemies",
				"palettes//backup_enemies" };
		for (String i : pal_folders) {
			File selIndex = new File(i);
			if (!selIndex.exists()) {
				errorMsg(i, "folder");
			}
		}
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
	 * @return list of files as a ArrayList
	 */
	public static ArrayList<String> getAllFiles(String diretorio) {
		String[] params = diretorio.split("\\*");
		File folder = new File(params[0]);
		File[] listoffiles = folder.listFiles();
		ArrayList<String> allfiles = new ArrayList<String>(); // Create an ArrayList object
		int count = 0;

		if (listoffiles != null) {
			if (diretorio.endsWith("*.*")) { // No extension defined
				for (File file : listoffiles) {
					if (file.isFile()) {
						allfiles.add(file.getName().toString());
						System.out.println(allfiles.get(count) + " added!");
						count++;
					}
				}
			} else { // extension defined
				for (File file : listoffiles) {
					if (file.isFile() && file.getName().endsWith(params[1])) {
						allfiles.add(file.getName().toString());
						System.out.println(allfiles.get(count) + " added!");
						count++;
					}
				}
			}
		}
		// System.out.println(allfiles); // print array
		return allfiles;
	}

	/**
	 * Read a txt file to colect all mod data.
	 * 
	 * @param filename
	 * @return
	 */
	public static String[] readTxt(String filename) {
		String[] moddata = new String[3];

		try {
			// Opens the file
			FileReader stream = new FileReader("mod_list//".concat(filename));
			BufferedReader reader = new BufferedReader(stream);

			// Method that reads a line from the file
			String line = reader.readLine();
			while (line != null) {

//				To compare a readline value it's necessary to use the equals method!
//			    if (linha.equals(testigual)) {
//			    	System.out.println("Texto desejado encontrado!!!");
//			    }
				if (line.startsWith("Mod Folder:")) {
					// Isolating mod folder name
					moddata[0] = line.replace("Mod Folder:", "").replace("\n", "");
				}
				if (line.startsWith("Mod Title:")) {
					// Isolating mod title
					moddata[1] = line.replace("Mod Title:", "").replace("\n", "");
				}
				if (line.startsWith("Author:")) {
					// Isolating mod author name
					moddata[2] = line.replace("Author:", "").replace("\n", "");
				}
				// Reads next line of the file
				line = reader.readLine();
			}
			// If a mod doesn't have a title, its folder name will be its title
			if (moddata[1] == null) {
				moddata[1] = moddata[0];
			}
			// If a mod doesn't have an declared author on its txt, the string below
			// will be used.
			if (moddata[2] == null) {
				moddata[2] = " - ";
			}
			// Closes the reading
			reader.close();
			// Closes the file
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Mod data ------------------");
		System.out.println("Folder: " + moddata[0]);
		System.out.println("Title: " + moddata[1]);
		System.out.println("Author: " + moddata[2]);

		return moddata;
	}

	/**
	 * Checks the status of the mod.
	 * 
	 * @param proj
	 * @return 1: installed, 2: unavaliable, 0: available
	 */
	public static int scanMod(String proj) {
		int status = 0;

		System.out.println("Searching for " + proj + "...");
		// Checks mod's folder and palette folder
		if (!new File("mod//games//" + proj).exists()) {
			System.out.println("Unavaliable mod!");
			status = 2; // Unavaliable mod
		} else {
			// Creates a palette folder if it doesn't exists
			if (!new File("mod//games//" + proj + "//palettes").exists()) {
				new File("mod//games//" + proj + "//palettes").mkdirs();
				System.out.println("\"palettes\" folder created ");
			}
			// Checking for enemie's palettes
			if (FuncMods.anyFile("mod//games//" + proj + "//palettes//enemies//*.pal")) {
				System.out.println("Enemies palettes not found!");
				status = 0; // Disabled mod
			} else {
				// Auto fix enemies palette folder path
				if (FuncMods.anyFile("mod//games//" + proj + "//enemies//*.pal")) {
					FuncMods.move("mod//games//" + proj + "//enemies", "mod//games//" + proj + "//palettes");
				} else {
					if (FuncMods.anyFile("palettes//sorr_enemies//*.pal")) {
						if (new File("mod//" + proj + ".txt").exists()) {
							status = 1; // Installed mod
							System.out.println("The mod is installed!");
						} else {
							status = 2; // Unavailable mod
							System.out.println("mod txt file not found!");
						}
					} else {
						System.out.println("The mod can't be installed!");
						status = 2; // Unavaliable modd
					}
				}
			}
		}

		System.out.println("Scanning completed!");
		System.out.println("--------------------------------------------------");
		return status;
	}

	public static String existsInstallation(String[][] allmodsvalues) {
		String installed = null;
		for (int i = 0; i < allmodsvalues.length; i++) {
			System.out.println();
			for (int j = 0; j < allmodsvalues[i].length; j++) {
				if (j == 2) {
					if (scanMod(allmodsvalues[i][0]) == 1) {
						System.out.println("Installed: " + allmodsvalues[i][0]);
						installed = allmodsvalues[i][0];
						break;
					}
				}
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
		if (new File("mod//games//" + selectedMod + "//palettes//enemies").exists()) {
			ren("palettes//enemies", "sorr_enemies");
			move("mod//games//" + selectedMod + "//palettes//enemies", "palettes");

		}
		// List all data files in a txt file
		System.out.println("Installing " + selectedMod + "...");
		if (new File("mod//games//" + selectedMod + "//data").exists()) {
			ArrayList<String> datafiles = getAllFiles("mod//games//" + selectedMod + "//data//*.*");
			try (FileWriter writer = new FileWriter("mod//sorr.txt")) {
				for (String item : datafiles) {
					System.out.println(item);
					writer.write(item + "\n");
					if (new File("data//" + item).exists()) {
						ren("mod//games//" + selectedMod + "//data//" + item, "[mod]" + item);
						move("mod//games//" + selectedMod + "//data//[mod]" + item, "data");
					}
					move("mod//games//" + selectedMod + "//data//" + item, "data");
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Renaming the txt to identify mod installation
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

				// Isolating mod folder name
				String mfile = line.replace("\n", "");
				if (new File("data//[mod]" + mfile).exists()) {
					move("data//[mod]" + mfile, "mod//games//" + proj + "//data");
				} else {
					if (new File("data//" + mfile).exists()) {
						move("data//" + mfile, "mod//games//" + proj + "//data");
					}
				}

				// Reads next line of the file
				line = reader.readLine();
			}
			reader.close(); // Closes the reading
			stream.close(); // Closes the file
		} catch (IOException e) {
			e.printStackTrace();
		}

		File filetodel = new File("mod//" + proj + ".txt");
		if (filetodel.delete()) {
			System.out.println("\"mod//" + proj + ".txt\" was deleted!");
		} else {
			System.out.println("Failed to delete the file.");
		}
		File makesor = new File("mod//sorr.txt");
		try {
			makesor.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Uninstalled!");
	}

	/**
	 * Show an error message when an important file is missing.
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