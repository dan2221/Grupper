package modmanager;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class NoiseDenoiser {

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

						System.out.println("Flipping images...");
						for (File file : selectedFiles) {
							System.out.println(file.getAbsolutePath());
							processSprite(file, true);
						}

						// Check noiser
						FuncMods.copy("unzipped\\noise.exe", "temp");
						FuncMods.copy("unzipped\\noise.dpr", "temp");

						// Combine two commands using && to change directory and then run the for loop
						System.out.println("----------------\nCalling Noiser...");
						runCmd("cd temp && for %i in (*.bmp) do noise \"%i\" /n");

						// Getting all images in temporary folder and process them again.
						File[] files = getFilesByType("temp", ".bmp");
						System.out.println("Fliping again...");
						for (File file : files) {
							System.out.println(file.getAbsolutePath());
							processSprite(file, false);
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

		// Delete unzipped and temporary files
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

	public static void processSprite(File img, boolean pixelFill) {
		try {
			// Load the image
			BufferedImage originalImage = ImageIO.read(img);

			// Get the name of the file (with extension)
			String fileNameWithExtension = img.getName();
			int lastDotIndex = fileNameWithExtension.lastIndexOf('.');
			String fileNameWithoutExtension = (lastDotIndex != -1) ? fileNameWithExtension.substring(0, lastDotIndex)
					: fileNameWithExtension;

			System.out.println("File name without extension: " + fileNameWithoutExtension);

			// Check if the image is indexed
			if (originalImage.getColorModel() instanceof IndexColorModel) {
				// Store positions of pixels with color rgb(0, 0, 8)
				List<int[]> positions = new ArrayList<>();
				for (int x = 0; x < originalImage.getWidth(); x++) {
					for (int y = 0; y < originalImage.getHeight(); y++) {
						if ((originalImage.getRGB(x, y) & 0x00FFFFFF) == 0x00000008) { // Check for rgb(0, 0, 8)
							positions.add(new int[] { x, y });
							System.out.println("Special pixel found: (" + x + "," + y + ")");
						}
					}
				}

				// Flip the image vertically
				BufferedImage processedImage = flipImage(originalImage);

				// Fill the new positions with rgb(0, 0, 8)
				if (pixelFill) {
					for (int[] pos : positions) {
						int newX = pos[0];
						int newY = originalImage.getHeight() - 1 - pos[1]; // Calculate new position after flip
						processedImage.setRGB(newX, newY, 0x00000008); // Set the pixel to rgb(0, 0, 8) //0x00000008
						System.out.println("Pixel filled! (" + newX + "," + newY + ")");
					}
				}
				// Save image and preserve palette
				saveImageWithPalette(processedImage, (IndexColorModel) originalImage.getColorModel(),
						"temp/" + fileNameWithoutExtension + ".bmp");
			} else {
				System.out.println("The image is not indexed.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage flipImage(BufferedImage originalImage) {
		// Create a new image with the same dimensions
		BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				originalImage.getType());

		// Flip the image vertically (invert the rows)
		for (int x = 0; x < originalImage.getWidth(); x++) {
			for (int y = 0; y < originalImage.getHeight(); y++) {
				// Copy the pixel from the corresponding row
				newImage.setRGB(x, y, originalImage.getRGB(x, originalImage.getHeight() - 1 - y));
			}
		}
		return newImage;
	}

	private static void saveImageWithPalette(BufferedImage image, IndexColorModel colorModel, String outputPath)
			throws IOException {
		// Create a new image with the same palette
		BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_INDEXED, colorModel);

		// Copy the pixels from the processed image to the new indexed image
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				indexedImage.setRGB(x, y, image.getRGB(x, y));
			}
		}

		// Save the image
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
