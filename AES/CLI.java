package AES;

import java.util.Scanner;

public class CLI 
{
	public static void options(int choice, Scanner sc)
	{
		try {
		switch (choice) {
		case 1:
			String inputFileP = CLI_utilities.validateFilePath("Enter the input file path: ");
			String outputDirP = CLI_utilities.validateDirectoryPath("Enter the output directory path: ");
			String secretK = CLI_utilities.padSecretKey();
			try 
			{
				MainController.encryptFile(inputFileP, outputDirP,secretK);
				System.out.println("File successfully encrypted  at : " +FileCRUD_utilities.filePath(MainController.outputFilePath));
			}
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
			break;
		case 2:
			inputFileP = CLI_utilities.validateFilePath("Enter the encrypted file path: ");
			outputDirP = CLI_utilities.validateDirectoryPath("Enter the output directory path: ");
			secretK = CLI_utilities.padSecretKey();
			try 
			{
				MainController.decryptFile(inputFileP, outputDirP, secretK);
				System.out.println("File successfully decrypted  at : " +FileCRUD_utilities.filePath(MainController.outputFilePath));
			} 
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
			break;
		case 3:
			String inputDirP = CLI_utilities.validateDirectoryPath("Enter the input directory path: ");
			outputDirP = CLI_utilities.validateDirectoryPath("Enter the output directory path: ");
			secretK = CLI_utilities.padSecretKey();
			try 
			{
				MainController.encryptDirectory(inputDirP, outputDirP, secretK);
				System.out.println("Directory: " +inputDirP + " is encrypted successfully.");
			} 
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
			break;
		case 4:
			inputDirP = CLI_utilities.validateDirectoryPath("Enter the encrpted directory path: ");
			outputDirP = CLI_utilities.validateDirectoryPath("Enter the output directory path: ");
			secretK = CLI_utilities.padSecretKey();
			try {
				MainController.decryptDirectory(inputDirP, outputDirP, secretK);
				System.out.println("Directory: " +inputDirP + " is decrypted successfully.");
			} 
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
			break;
		case 5:
			System.out.println("Program Exited!");
			MainController.flag=false;
			break;
		default:
			System.out.println("Invalid choice. Try again!");
		}
		  } catch (Exception e) {
	            System.err.println("An error occurred while processing your request: " + e.getMessage());
	        }
	}
}

