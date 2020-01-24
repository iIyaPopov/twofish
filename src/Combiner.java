
public class Combiner {
	private static final int MASK = 0xff;
	private static final int INT_BYTE_COUNT = 4;
	
	public static int run(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < INT_BYTE_COUNT; i++) {
			result += ((int) bytes[i] & MASK) << (Byte.SIZE * i);
		}
		return result;
	}
}