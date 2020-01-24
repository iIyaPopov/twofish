
import java.io.*;
import java.util.*;

public class Data {
	private final int BYTE_COUNT = 16;	//128 bit
	private final int BLOCK_SIZE = 4;
	
	private FileInputStream reader;
	private int blocksCount;
	
	public Data(String filename) throws FileNotFoundException {
		File file = new File(filename);
		byte endBytesCount = (byte)( file.length() % BYTE_COUNT);
		blocksCount = (int)Math.ceil((double)(file.length()) / BYTE_COUNT);
		Twofish.setEndBytesCount(endBytesCount);
		reader = new FileInputStream(file);
	}
	
	public int[] getBlock() throws IOException {
		byte[] dataBytes = new byte[this.BYTE_COUNT];
		int countByte = reader.read(dataBytes);
		if (countByte == -1) {
			return null;
		}
		int[] data = new int[BYTE_COUNT / BLOCK_SIZE];
		for (int i = 0; i < data.length; i++) {
			for (int j = BLOCK_SIZE * i; j < BLOCK_SIZE * i + BLOCK_SIZE; j++) {
				data[i] = (data[i] << Byte.SIZE) | ((int) dataBytes[j] & 0xff);
			}
		}
		return data;
	}
	
	public byte getFirstByte() throws IOException {
		return (byte) reader.read();
	}
	
	public int getBlocksCount() {
		return blocksCount;
	}
}