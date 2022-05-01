package modmanager;

import java.util.ArrayList; // import the ArrayList class
import java.io.BufferedReader;

// Libraries for file checking
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class FuncoesMods {

	public static boolean anyFile(String diretorio)
	// Checks if exists any file with the desired extension
	{
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

	public static ArrayList<String> getAllFiles(String extension, String diretorio)
	// Get all files inside a folder
	{
		File folder = new File(diretorio);
		File[] listoffiles = folder.listFiles();
		ArrayList<String> allfiles = new ArrayList<String>(); // Create an ArrayList object
		int count = 0;
		// System.out.println("tamanho: "+listoffiles.length);

		if (listoffiles != null) {
			for (File file : listoffiles) {
				if (file.isFile() && file.getName().endsWith(extension)) {
					allfiles.add(file.getName().toString());
					System.out.println(allfiles.get(count) + " added!");
					count++;
				}
			}
		}
		// System.out.println(allfiles); // print array
		return allfiles;
	}

	public static void move(String origem, String destino) {
		// Move files and folders
		File param1 = new File(origem);
		File param2 = new File(destino);
		System.out.println("Origin: " + param1);
		System.out.println("Destiny: " + param2);
		if (param1.isDirectory() == true) {
			System.out.println("É um diretório!");
			try {
				Files.move(param1.toPath(), param2.toPath());
				System.out.println("Directory moved successfully.");
			} catch (IOException ex) {
				System.out.println("Error found!");
				ex.printStackTrace();
			}
		}
	}

	// faça com que o método retorne um array!!!!!!!!!!!!!!!!!!!!!!!!!!
	public static String[] readTxt(String filename) {
		// Read txt file to colect mod data

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
			// If a mod doesn't have an declared author on its txt, the string bellow
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

	public static int scanMod(String proj) {
		// Checks if a mod is installed or not
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
			if (FuncoesMods.anyFile("mod//games//" + proj + "//palettes//enemies//*.pal")) {
				System.out.println("Enemies palettes not found!");
				status = 0; // Disabled mod
			} else {
				// Auto fix enemies palette folder path
				if (FuncoesMods.anyFile("mod//games//" + proj + "//enemies//*.pal")) {
					FuncoesMods.move("mod//games//" + proj + "//enemies",
							"mod//games//" + proj + "//palettes//enemies");
				} else {
					if (FuncoesMods.anyFile("palettes//sorr_enemies//*.pal")) {
						System.out.println("The mod is installed!");
						status = 1; // Installed mod
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
}