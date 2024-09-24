package AES;

import java.io.File;

public class FileCRUD_utilities
{
	public static String filePath(String output)
	{
		File file = new File(output);
		if (!file.exists()) {
            throw new IllegalArgumentException("The specified file does not exist: " + output);
        }
        
        if (!file.isFile()) {
            throw new IllegalArgumentException("The specified path is not a file: " + output);
        }
		return  file.getAbsolutePath();
	}

}
