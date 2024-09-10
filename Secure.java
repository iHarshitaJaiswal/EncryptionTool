
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.SecureRandom;
import java.util.Scanner;
public class Secure {


    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        boolean flag=true;
        while (flag) {
            System.out.println("\nChoose an option: ");
            System.out.println("1. Encrypt a File");
            System.out.println("2. Decrypt a File");
            System.out.println("3. Encrypt a Directory");
            System.out.println("4. Decrypt a Directory");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();
            switch (ch) {
                case 1:
                    System.out.print("Enter the input file path: ");
                    String inputFileP = sc.nextLine();
                    System.out.print("Enter the output directory path: ");
                    String outputDirP = sc.nextLine();
                    System.out.print("Enter the secret key: ");
                    String secretK = sc.nextLine();
                    try
                    {
                        encryptFile(inputFileP, outputDirP,secretK);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter the encrypted file path: ");
                    inputFileP = sc.nextLine();
                    System.out.print("Enter the output directory path: ");
                    outputDirP = sc.nextLine();
                    System.out.print("Enter the secret key: ");
                    secretK = sc.nextLine();
                    try
                    {
                        decryptFile(inputFileP, outputDirP, secretK);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter the input directory path: ");
                    String inputDirP = sc.nextLine();
                    System.out.print("Enter the output directory path: ");
                    outputDirP = sc.nextLine();
                    System.out.print("Enter the secret key: ");
                    secretK = sc.nextLine();
                    try
                    {
                        encryptDirectory(inputDirP, outputDirP, secretK);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter the encrypted directory path: ");
                    inputDirP = sc.nextLine();
                    System.out.print("Enter the output directory path: ");
                    outputDirP = sc.nextLine();
                    System.out.print("Enter the secret key: ");
                    secretK = sc.nextLine();
                    try {
                        decryptDirectory(inputDirP, outputDirP, secretK);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Program Exited!");
                    sc.close();
                    flag=false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    public static void encryptFile( String inputFilePath, String outputDirPath, String secretKey) throws Exception
    {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher Ecipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] Ivector = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(Ivector);
        GCMParameterSpec para = new GCMParameterSpec(128, Ivector);
        Ecipher.init(Cipher.ENCRYPT_MODE, key, para);
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputDirPath, "stf." + inputFile.getName());
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        try  {
            outputStream.write(Ivector);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
            {
                byte[] output = Ecipher.update(buffer, 0, bytesRead);
                if (output != null)
                {
                    outputStream.write(output);
                }
            }
            byte[] Ebytes = Ecipher.doFinal();
            if (Ebytes != null)
            {
                outputStream.write(Ebytes);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            inputStream.close();
            outputStream.close();
        }
        System.out.println("File encrypted successfully: " + outputFile.getAbsolutePath());
    }
    public static void decryptFile(String encryptedFilePath, String outputDirPath, String secretKey) throws Exception
    {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher Dcipher = Cipher.getInstance("AES/GCM/NoPadding");
        File file = new File(encryptedFilePath.replace("stf.", ""));
        String justname= file.getName();
        FileInputStream inputStream = new FileInputStream(encryptedFilePath);
        try{
            byte[] Ivector = new byte[12];
            inputStream.read(Ivector);
            GCMParameterSpec para = new GCMParameterSpec(128, Ivector);
            Dcipher.init(Cipher.DECRYPT_MODE, key, para);
            File outputFile = new File(outputDirPath,justname);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            try  {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    byte[] output = Dcipher.update(buffer, 0, bytesRead);
                    if (output != null)
                    {
                        outputStream.write(output);
                    }
                }
                byte[] outputBytes = Dcipher.doFinal();
                if (outputBytes != null)
                {
                    outputStream.write(outputBytes);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            finally
            {
                inputStream.close();
                outputStream.close();
            }
            System.out.println("File decrypted successfully: " + outputFile.getAbsolutePath());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
    public static void encryptDirectory(String inputDirPath, String outputDirPath, String secretKey) throws Exception {
        File directory = new File(inputDirPath);
        if (directory.exists() && directory.isDirectory())
        {
            Files.walk(Paths.get(inputDirPath)).filter(Files::isRegularFile).forEach(filePath->
            {
                try
                {
                    String fileName = filePath.getFileName().toString();
                    //String encryptedFileName = "stf." + fileName;
                    //String outputPath = Paths.get(outputDirPath, encryptedFileName).toString();
                    encryptFile(filePath.toString(), outputDirPath, secretKey);
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            } );
        }
        else
        {
            System.out.println("Invalid input directory path.");
        }
        System.out.println("Directory encrypted successfully: " + inputDirPath);
    }
    public static void decryptDirectory(String inputDirPath, String outputDirPath, String secretKey) throws Exception {
        File Inputdirectory = new File(inputDirPath);
        Path p = Paths.get(outputDirPath);
        if (Inputdirectory.exists() && Inputdirectory.isDirectory())
        {
            Files.walk(Paths.get(inputDirPath)).filter(Files::isRegularFile).forEach(filePath ->
            {
                try {
                    String fileName = filePath.getFileName().toString();
                    if (fileName.startsWith("stf."))
                    {
                        //String decryptedFileName = fileName.replace("stf.", "");
                        //String outputPath = Paths.get(outputDirPath, decryptedFileName).toString();
                        if(Files.exists(p))
                        {
                            decryptFile(filePath.toString(), outputDirPath, secretKey);
                        }
                        else
                        {
                            try
                            {
                                Files.createDirectories(p);
                                decryptFile(filePath.toString(), outputDirPath, secretKey);
                            }
                            catch (Exception e)
                            {
                                System.out.println("Invalid input directory path.");
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            });
            System.out.println("Directory decrypted successfully: " + inputDirPath);
        }
        else
        {
            System.out.println("Invalid input directory path.");
        }

    }
}
