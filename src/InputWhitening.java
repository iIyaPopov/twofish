
import java.io.*;

public class InputWhitening {
	private static final int INT_BLOCK_COUNT = 4;
	public static int[] run(int[] dataBlock) {
		int[] result = new int[INT_BLOCK_COUNT];
		for (int i = 0; i < INT_BLOCK_COUNT; i++) {
			result[i] = dataBlock[i] ^ Twofish.getKSubkey(i);
		}
		return result;
	}
}