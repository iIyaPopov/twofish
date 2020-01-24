
import java.util.*;

public class Splitter {
	private static final int MASK = 0xff;
	private static final int INT_BYTE_COUNT = 4;
	
	public static byte[] run(int x) {
		byte[] bytes = new byte[INT_BYTE_COUNT];
		for (int i = 0; i < INT_BYTE_COUNT; i++) {
			bytes[i] = (byte)((x >> (Byte.SIZE * i)) & MASK);
		}
		return bytes;
	}
}