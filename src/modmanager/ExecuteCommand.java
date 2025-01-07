package modmanager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ExecuteCommand {

	public static void main(String[] args) {

		// Check there are resources
		if (FuncMods.exist("resources.zip")) {
			try {
				// Unzip resources
				FuncMods.unzip("resources.zip", "unzipped");

				// Checking denoiser components
				String[] filesToCheck = { "a_noise.bat", "noise.bat", "noise.dpr", "noise.exe" };
				int count = 0;
				for (String file : filesToCheck) {
					if (FuncMods.exist("unzipped//" + file)) {
						count++;
					}
				}
				// If the four files are found, the next steps can be done
				if (count == 4) {
					System.out.println("Denoiser components: OK\n------------------------");

					// Set the appearance of the graphical interface to match the look of the
					// user's operating system.
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					File[] selectedFiles = openImageFileChooser();
					// Operate with the selected files.
					if (selectedFiles != null) {
						// Make a temporary folder
						new File("temp").mkdirs();

						for (File file : selectedFiles) {
							System.out.println(file.getAbsolutePath());
							processSprite(file);
						}

						// Copy files for noising
						System.out.println("Check denoiser...");
						FuncMods.copy("unzipped\\noise.exe", "temp");
						FuncMods.copy("unzipped\\noise.dpr", "temp");

						// Combine two commands using && to change directory and then run the for loop
						runCmd("cd temp && for %i in (*.bmp) do noise \"%i\" /n");

						// Getting all images in temporary folder and process them again.
						File[] files = getFilesByType("temp", ".bmp");
						for (File file : files) {
							System.out.println(file.getAbsolutePath());
							processSprite(file);
						}

						// Show a success message
						JOptionPane.showMessageDialog(new JFrame(), "Images successfully manipulated!\n", "Information",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						System.out.println("No files selected.");
					}
				} else {

					// Show an error message
					JOptionPane.showMessageDialog(new JFrame(), "Denoiser's components not found!\n", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			System.err.println("\"Resources.zip\" not found!");
			JOptionPane.showMessageDialog(new JFrame(), "The file \"resources.zip\" was not found!\n", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}

		// Delete unzipped files
		try {
			FuncMods.deleteDirectory(new File("unzipped"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method executes a specified command line operation in the system's
	 * command prompt and handles the output and error streams.
	 * 
	 * @param commandLine
	 * @throws IOException
	 */
	public static void runCmd(String commandLine) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", commandLine);

		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}
	}

	public static File[] openImageFileChooser() {
		// Create a file chooser
		JFileChooser fileChooser = new JFileChooser();

		// Set the dialog title
		fileChooser.setDialogTitle("Select Image Files");

		// Set the file selection mode to allow multiple selections
		fileChooser.setMultiSelectionEnabled(true);

		// Create a custom file filter for image files
		fileChooser.addChoosableFileFilter(
				new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "bmp", "png", "gif", "pcx"));

		// Show the file chooser dialog
		int returnValue = fileChooser.showOpenDialog(null);

		// Check if the user selected files
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			// Return the selected files
			return fileChooser.getSelectedFiles();
		} else {
			// Return null if no files were selected
			return null;
		}
	}

	public static void processSprite(File img) {
		try {
			// Carregar a imagem
			BufferedImage originalImage = ImageIO.read(img);

			/////////////////////////////////////////////////////
			// Get the name of the file (with extension)
			String fileNameWithExtension = img.getName();

			// Get the index of the last dot in the file name
			int lastDotIndex = fileNameWithExtension.lastIndexOf('.');

			// If there is a dot, get the name without the extension
			String fileNameWithoutExtension;
			if (lastDotIndex != -1) {
				fileNameWithoutExtension = fileNameWithExtension.substring(0, lastDotIndex);
			} else {
				// If there is no dot, the name is the same as the full name
				fileNameWithoutExtension = fileNameWithExtension;
			}

			// Print the file name without the extension
			System.out.println("File name without extension: " + fileNameWithoutExtension);

			///////////////////////////////

			// Check if the image is indexed
			if (originalImage.getColorModel() instanceof IndexColorModel) {
				// Manipular a imagem (girar verticalmente)
				BufferedImage processedImage = flipImage(originalImage);

				// Save image and preserve palette
				saveImageWithPalette(processedImage, (IndexColorModel) originalImage.getColorModel(),
						"temp/" + fileNameWithoutExtension + ".bmp");
			} else {
				System.out.println("A imagem não é indexada.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage flipImage(BufferedImage originalImage) {
		// Criar uma nova imagem com as mesmas dimensões
		BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				originalImage.getType());

		// Girar a imagem verticalmente (inverter as linhas)
		for (int x = 0; x < originalImage.getWidth(); x++) {
			for (int y = 0; y < originalImage.getHeight(); y++) {
				// Copiar o pixel da linha correspondente
				newImage.setRGB(x, y, originalImage.getRGB(x, originalImage.getHeight() - 1 - y));
			}
		}
		return newImage;
	}

	private static void saveImageWithPalette(BufferedImage image, IndexColorModel colorModel, String outputPath)
			throws IOException {
		// Criar uma nova imagem com a mesma paleta
		BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_INDEXED, colorModel);

		// Copiar os pixels da imagem processada para a nova imagem indexada
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				indexedImage.setRGB(x, y, image.getRGB(x, y));
			}
		}

		// Salvar a imagem
		ImageIO.write(indexedImage, "bmp", new File(outputPath));
	}

	/**
	 * Method to get all files of a specified type in a given directory.
	 * 
	 * @param directoryPath
	 * @param fileType
	 * @return
	 */
	public static File[] getFilesByType(String directoryPath, String fileType) {
		// Create a File object for the directory
		File directory = new File(directoryPath);

		// Check if the directory exists and is indeed a directory
		if (directory.exists() && directory.isDirectory()) {
			// Get all files of the specified type in the directory
			return directory.listFiles((dir, name) -> name.toLowerCase().endsWith(fileType.toLowerCase()));
		} else {
			System.out.println("The specified path is not a valid directory.");
			return new File[0]; // Return an empty array if the directory is invalid
		}
	}
}
