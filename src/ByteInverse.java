public class ByteInverse {
	private static final int INT_BYTE_COUNT = 4;
	
	public static int run(int x) {
		int mask = 0xff;
		int result = 0;
		for (int i = INT_BYTE_COUNT - 1; i >= 0; i--) {
			result += (x & mask) << Byte.SIZE * i;
			x = x >>> Byte.SIZE;
		}
		return result;
	}
}