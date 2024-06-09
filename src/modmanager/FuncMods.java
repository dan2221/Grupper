package modmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class has methods related to file manipulation.
 */
public class FuncMods {

	public void copyResource() {
		String location = "/images/default_logo.png";
		String destiny = "/myfolder/new_image.png";
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(destiny)) {
			Files.copy(is, Paths.get(location));
		} catch (IOException e) {
			// An error occurred copying the resource
		}
	}

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
		System.out.println("Moving: " + param1);
		System.out.println("To:     " + param2);
		try {
			Files.move(param1.toPath(), param2.toPath());
			System.out.println("\"" + item + "\" successfully moved!");
		} catch (IOException ex) {
			System.err.println("Error found!");
			ex.printStackTrace();
		}
	}

	/**
	 * Move desired file or folder. It works like the Batch Script move command.
	 * 
	 * @param origem
	 * @param destino
	 * 
	 */
	public static void copy(String origem, String destino) {
		// Getting the filename or folder
		String[] tree = origem.split("//", 0);
		String item = tree[tree.length - 1];

		File param1 = new File(origem);
		File param2 = new File(destino + "//" + item);
		System.out.println("Moving: " + param1);
		System.out.println("To:     " + param2);
		try {
			Files.copy(param1.toPath(), param2.toPath());
			System.out.println("\"" + item + "\" successfully copied!");
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
			System.err.println("Impossible to rename! Already exists a item with the same name!");
		} else {
			new File(origin).renameTo(new File(origin.replace(item, newname)));
		}
	}

	/**
	 * Method that checks if a file exists.
	 */
	public static boolean exist(String arq) {
		return new File(arq).exists();
	}

	/**
	 * Checks if exists any file with the desired extension. It works like like the
	 * exist command in Batch Script.
	 * 
	 * @param directory
	 * @return boolean value
	 */
	public static boolean anyFile(String directory) {
		boolean existe = false;
		String params[] = directory.split("\\*");
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
		System.out.println("\nGetting all folders in \"" + diretorio + "\"...");
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
	 * @return All mod data form txts.
	 */
	public static String[] readTxt(String filename) {
		String[] modData = new String[3];
		modData[0] = filename.replace(".txt", "");
		System.out.println("-----------------------");
		System.out.println("Getting data from " + filename + "...\n");

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
		String modPath = Main.sorrPath + "//mod//games//" + proj;

		System.out.println("\nAnalyzing " + proj + "...");
		if (new File(modPath).exists()) {

			// Creates a palette folder if it doesn't exists
			if (!new File(modPath + "//palettes").exists()) {
				new File(modPath + "//palettes").mkdirs();
				System.out.println("\"palettes\" folder created in \"" + Main.sorrPath + "\\" + proj + "\" project!");
			}
		}

		// Checking for enemie's palettes
		if (anyFile(modPath + "//palettes//enemies//*.pal")) {
			System.out.println("Enemies palettes found!");
			status = 0; // Disabled mod
		} else {
			// Auto-fix enemies palette folder path
			if (anyFile(modPath + "//enemies//*.pal")) {
				move(modPath + "//enemies", modPath + "//palettes");
			} else {
				if (anyFile(Main.sorrPath + "//palettes//sorr_enemies//*.pal")) {
					if (exist(Main.sorrPath + "//mod//" + proj + ".txt")) {
						status = 1; // Installed mod
						System.out.println("The mod is installed!");
					} else {
						status = 2; // Unavailable mod
						System.out.println("Unavailable mod! Txt mod file not found!");
					}
				} else {
					System.out.println("The mod can't be installed! \"sorr_enemies\" folder not found!");
					status = 2; // Unavaliable mod
				}
			}
		}
		// System.out.println("Scanning completed!");
		System.out.println("--------------------------------------------------");
		return status;
	}

	/**
	 * This method detects whether any level mod is installed, regardless of the
	 * mod.
	 * 
	 * @return mod's name
	 */
	public static String existsInstallation() {
		String[][] allModsValues = Main.getAllModData();
		String installed = null;
		for (int i = 0; i < Main.modQuantity; i++) {
			if (scanMod(allModsValues[i][0]) == 1) {
				System.out.println("\nInstalled: " + allModsValues[i][0]);
				installed = allModsValues[i][0];
				break;
			}
		}
		if (installed == null) {
			System.out.println("There isn't any installed mods!");
		}
		return installed;
	}

	/**
	 * Install the modification
	 * 
	 * @param selectedMod
	 */
	public static void installMod(String selectedMod) {
		String modPath = Main.sorrPath + "//mod//games//" + selectedMod;

		// This is for "First mod of the list" configuration
		if (Start.getConfig()[2]) {
			ren(modPath, "- " + selectedMod);
			selectedMod = "- " + selectedMod;
		}

		System.out.println("------------------------------\nInstalling \"" + selectedMod + "\"...");

		// Palettes
		if (new File(modPath + "//palettes//enemies").exists()) {
			ren(Main.sorrPath + "//palettes//enemies", "sorr_enemies");
			move(modPath + "//palettes//enemies", Main.sorrPath + "//palettes");
		}

		// List all data files in a txt file
		if (new File(modPath + "//data").exists()) {
			ArrayList<String> dataFiles = getAllFiles(modPath + "//data//*.*");
			try (FileWriter writer = new FileWriter(Main.sorrPath + "//mod//sorr.txt")) {
				System.out.println("Handling with data files...");
				for (String item : dataFiles) {
					System.out.println("--------------------\n" + item);
					writer.write(item + "\n");

					// User's data files won't be replaced by mod's files, because the mod's ones
					// receives "[mod]" in their names when exists files with the same names.
					if (new File(Main.sorrPath + "//data//" + item).exists()) {
						ren(modPath + "//data//" + item, "[mod]" + item);
						move(modPath + "//data//[mod]" + item, Main.sorrPath + "//data");
					} else {
						move(modPath + "//data//" + item, Main.sorrPath + "//data");
					}
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Renaming the txt for identifying mod installation.
		ren(Main.sorrPath + "//mod//sorr.txt", selectedMod + ".txt");
		System.out.println("Successfully installed!");
	}

	public static void uninstallMod(String proj) {
		String modPath = Main.sorrPath + "//mod//games//" + proj;
		System.out.println("Uninstalling \"" + proj + "\"...");

		// Palettes
		move(Main.sorrPath + "//palettes//enemies", modPath + "//palettes");
		ren(Main.sorrPath + "//palettes//sorr_enemies", "enemies");

		try {
			// Opens the file
			FileReader stream = new FileReader(Main.sorrPath + "//mod//" + proj + ".txt");
			BufferedReader reader = new BufferedReader(stream);

			// Method that reads a line from the file
			String line = reader.readLine();
			while (line != null) {

				// Isolating mod txt file name
				String modFile = line.replace("\n", "");

				// If a file has "[mod]" in its name, these characters will be removed
				// during the uninstalling.
				if (new File(Main.sorrPath + "//data//[mod]" + modFile).exists()) {
					move(Main.sorrPath + "//data//[mod]" + modFile, modPath + "//data");
					ren(modPath + "//data//" + "[mod]" + modFile, modFile);
				} else {
					if (new File(Main.sorrPath + "//data//" + modFile).exists()) {
						move(Main.sorrPath + "//data//" + modFile, modPath + "//data");
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

		File fileToDelete = new File(Main.sorrPath + "//mod//" + proj + ".txt");
		if (fileToDelete.delete()) {
			System.out.println("\"mod//" + proj + ".txt\" deleted!");
		} else {
			System.err.println("Failed to delete \"mod//" + proj + ".txt\"");
		}
		// Create sorr txt file
		try {
			new File(Main.sorrPath + "//mod//sorr.txt").createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Remove "- " from the mod folder if the configuration for setting
		// installed mod as the first one of the list is activated.
		if (Start.getConfig()[2]) {
			if (proj.startsWith("- ")) {
				// Remove the 2 first characters of the folder name.
				ren(modPath, proj.substring(2));
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
	public static void errorMsg(String file) {
		String message = "The directory \"" + file.replace("//", "/") + "\" was not found!\n"
				+ "The program needs that to work. Please select a different path or check your folder manually.";
		JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	/**
	 * Method to unzip files
	 * 
	 * @param zipFile
	 * @param destFolder
	 * @throws IOException
	 */
	public static void unzip(String zipFile, String destFolder) throws IOException {
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry entry;
			byte[] buffer = new byte[1024];
			while ((entry = zis.getNextEntry()) != null) {
				File newFile = new File(destFolder + File.separator + entry.getName());
				if (entry.isDirectory()) {
					newFile.mkdirs();
				} else {
					new File(newFile.getParent()).mkdirs();
					try (FileOutputStream fos = new FileOutputStream(newFile)) {
						int length;
						while ((length = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, length);
						}
					}
				}
			}
		}
	}
}