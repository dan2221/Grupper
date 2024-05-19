package modmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TestZip {

	// Main Function
    public static void main(String[] args) {
        try {
            // Zip files
//            String[] filesToZip = {"file1.txt", "file2.txt"};
            String zipFileName = "resources.zip";
//            zipFiles(filesToZip, zipFileName);
//            System.out.println("Files zipped successfully.");

            // Unzip files
            String destinationFolder = "unzipped";
            unzip(zipFileName, destinationFolder);
            System.out.println("Files unzipped successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	// Method to zip files
    public static void zipFiles(String[] srcFiles, String zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            byte[] buffer = new byte[1024];
            
            for (String srcFile : srcFiles) {
                File fileToZip = new File(srcFile);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    zos.putNextEntry(new ZipEntry(fileToZip.getName()));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }
            }
        }
    }
    
 // Method to unzip files
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
