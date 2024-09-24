package AES;


import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto
{

	public static String encryption(String message, String secretKey) throws Exception
	{
		try
		{
			byte[] iv = new byte[12];
		    SecureRandom random = new SecureRandom();
		    random.nextBytes(iv);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
			GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); 
		    cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
			byte[] encryptedBytes = cipher.doFinal(message.getBytes());
			byte[] combined = new byte[iv.length + encryptedBytes.length];
		    System.arraycopy(iv, 0, combined, 0, iv.length);
		    System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
			return  Base64.getEncoder().encodeToString(combined);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new Exception("Encryption algorithm not found: " + e.getMessage());
		} catch (InvalidKeyException e) {
			throw new Exception("Invalid secret key: " + e.getMessage());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new Exception("Error during encryption: " + e.getMessage());
		}
	}
	public static String decryption(String message, String secretKey) throws Exception
	{
		boolean decryptionSuccessful = false;
		String decryptedMessage = "";
		do {
			try
			{
				byte[] decodedBytes = Base64.getDecoder().decode(message);
				byte[] iv = new byte[12];
				byte[] encryptedBytes = new byte[decodedBytes.length - iv.length];
				System.arraycopy(decodedBytes, 0, iv, 0, iv.length);
				System.arraycopy(decodedBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);
				Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
				SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
				GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
				cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
				byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
				decryptedMessage = new String(decryptedBytes);
				decryptionSuccessful = true;

			}catch (InvalidKeyException e) {
				System.out.println("Invalid secret key. Please try again.");
			}catch (AEADBadTagException e) {
				System.out.println("Decryption failed: Authentication error. Please enter the correct key.");
				secretKey =CLI_utilities.padSecretKey();
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				throw new Exception("Decryption algorithm not found: " + e.getMessage());
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				throw new Exception("Error during decryption: " + e.getMessage());
			}
		}while(!decryptionSuccessful);
		return decryptedMessage;
	}
}