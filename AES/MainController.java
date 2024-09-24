package AES;
import javax.crypto.AEADBadTagException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

public class MainController 
{

	public static boolean flag=true;
	public static String outputFilePath;

	public static void main( String[] args ) 
	{
		Scanner sc = new Scanner(System.in);
		while (flag) 
		{
			System.out.println("\nChoose an option: ");
			System.out.println("1. Encrypt a File.");
			System.out.println("2. Decrypt a File.");
			System.out.println("3. Encrypt a Directory.");
			System.out.println("4. Decrypt a Directory.");
			System.out.println("5. Exit.");
			System.out.print("Enter choice: ");
			int ch = sc.nextInt();
			sc.nextLine();
			CLI.options(ch, sc);
		}
		sc.close();
	}

	public static void encryptFile( String inputFilePath, String outputDirPath, String secretKey) throws Exception 
	{
		try {
			String message=FileCRUD.read(inputFilePath);
			String Emessage= Crypto.encryption(message, secretKey);
			outputFilePath = FileCRUD.encryptedFileName(inputFilePath, outputDirPath);
			FileCRUD.write(outputFilePath, Emessage);
		} catch (IOException e) {
			System.err.println("Error: Problem while reading or writing the file. " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An unexpected error occurred during encryption: " + e.getMessage());
		}


	}
	public static void decryptFile(String encryptedFilePath, String outputDirPath, String secretKey) throws Exception 
	{
		try
		{String message=FileCRUD.read(encryptedFilePath);
		String Dmessage= Crypto.decryption(message, secretKey);
		outputFilePath = FileCRUD.decryptedFileName(encryptedFilePath, outputDirPath);
		FileCRUD.write(outputFilePath,Dmessage);
		} catch (AEADBadTagException e) {
			System.err.println("Error: Decryption failed due to authentication tag mismatch.");
		} catch (IOException e) {
			System.err.println("Error: Problem while reading or writing the file. " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An unexpected error occurred during decryption: " + e.getMessage());
		}
	}
	public static void encryptDirectory(String inputDirPath, String outputDirPath, String secretKey) throws Exception {
		try{
			List<String> filePaths = FileCRUD.traverseDirectory(inputDirPath);	
			for (String f : filePaths) {
				encryptFile(f.toString(), outputDirPath, secretKey);
			}
		}
		catch (Exception e) 
		{
			 System.err.println("Error while encrypting directory: " + e.getMessage());
		}
	}
	public static void decryptDirectory(String inputDirPath, String outputDirPath, String secretKey) throws Exception {
		try{
			List<String> filePaths = FileCRUD.traverseDirectory(inputDirPath);	
			for (String f : filePaths) {
				File inputFile = new File(f);
				String fileName = inputFile.getName();
				if (fileName.startsWith("stf.")) 
					decryptFile(f.toString(), outputDirPath, secretKey);
			}
		}
		catch (Exception e) 
		{
			 System.err.println("Error while decrypting directory: " + e.getMessage());
		}
	}  
}


