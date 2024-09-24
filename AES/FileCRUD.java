package AES;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;



public class FileCRUD {
	public static String read(String filePath) throws IOException {
		try
		{
			String content=new String(Files.readAllBytes(Paths.get(filePath)));
		    return content;
		} catch (IOException e) {
            throw new IOException("Error reading file: " + filePath + " - " + e.getMessage());
        }
	}

	public static void write(String filePath, String content) throws IOException {
		try
		{
			FileWriter writer = new FileWriter(filePath);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			throw new IOException("Error writing to file: " + filePath + " - " + e.getMessage());
		}
	}


	public static String encryptedFileName(String inputFilePath, String outputDir) {
		File inputFile = new File(inputFilePath);
		String fileName = inputFile.getName();
		return outputDir + File.separator + "stf." + fileName;
	}


	public static String decryptedFileName(String inputFilePath, String outputDir) {
		File inputFile = new File(inputFilePath);
		String fileName = inputFile.getName();
		String newFileName = fileName.replace("stf.", "");
		return outputDir + File.separator + newFileName;
	}

	public static List<String> traverseDirectory(String directoryPath) throws Exception
	{
		List<String> filePaths = new ArrayList<>();
		File directory = new File(directoryPath);
			File[] files = directory.listFiles();  
			if (files == null) 
			{
				throw new IOException("Error accessing files in directory: " + directoryPath);
			}
			for (File f : files) 
			{
				if (f.isFile())
				{
					filePaths.add(f.getAbsolutePath());
				} else if (f.isDirectory()) 
				{
					filePaths.addAll(traverseDirectory(f.getAbsolutePath()));
				}
			}
			return filePaths;
		}
	}

