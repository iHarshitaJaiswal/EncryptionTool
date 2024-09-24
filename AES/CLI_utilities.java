package AES;
import java.util.Scanner;
import java.io.File;

public class CLI_utilities 
{
	static Scanner sc=new Scanner(System.in);
	public static String validateFilePath( String prompt) {
		while (true) {
			System.out.print(prompt);
			String filePath = sc.nextLine();
			File file = new File(filePath);
			if (file.exists() && file.isFile()) {
				return filePath;
			} else {
				System.out.println("Error: File not found. Please enter a valid file path.");
			}
		}
	}

	public static String validateDirectoryPath( String prompt) {
		while (true) {
			System.out.print(prompt);
			String dirPath = sc.nextLine();
			File dir = new File(dirPath);
			if (dir.exists() && dir.isDirectory()) {
				return dirPath;
			} else {
				System.out.println("Error: Directory not found. Please enter a valid directory path.");
			}
		}
	}

	public static String padSecretKey() {
		while (true) {
			System.out.print("Enter the secret key (max 16 characters): ");
			String secretKey = sc.nextLine();
			if (secretKey.length() > 16) {
				System.out.println("Error: Secret key too long. Please enter a key with up to 16 characters.");
			} else {
				StringBuilder paddedKey = new StringBuilder(secretKey);
				while (paddedKey.length() < 16) {
					paddedKey.append("0"); 
				}
				return paddedKey.toString();
			}
		}
	}

}
