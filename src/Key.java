
import java.io.*;
import java.util.*;
import java.lang.*;

public class Key {
	public static final int KEY_SIZE_128 = 16;
	public static final int KEY_SIZE_192 = 24;
	public static final int KEY_SIZE_256 = 32;
	
	private byte[] key;
	
	public Key(String filename) throws FileNotFoundException, IOException {
		File file = new File(filename);
		int keySize = (int) file.length();
		if (keySize == KEY_SIZE_128 || keySize == KEY_SIZE_192 || keySize == KEY_SIZE_256) {
			this.key = new byte[keySize];
			FileInputStream fis = new FileInputStream(file);
			fis.read(key);
			fis.close();
		} else {
			throw new RuntimeException("Error: impossible key length.");
		}
	}
	
	public Key(int keySize, String outputFile) throws IOException {
		if (keySize == KEY_SIZE_128 || keySize == KEY_SIZE_192 || keySize == KEY_SIZE_256) {
			this.key = new byte[keySize];
			Random rand = new Random();
			rand.nextBytes(key);
			File file = new File(outputFile);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(key);
			fos.close();
		} else {
			throw new RuntimeException("Error: impossible key length.");
		}
	}
	
	public byte[] getKey() {
		return this.key;
	}
}