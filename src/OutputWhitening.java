
public class OutputWhitening {
	private static final int INT_BLOCK_COUNT = 4;
	
	public static int[] run(int[] blocks) {
		int[] result = new int[INT_BLOCK_COUNT];
		for (int i = 0; i < INT_BLOCK_COUNT; i++) {
			result[i] = blocks[i] ^ Twofish.getKSubkey(4 + i);	//KSubkey = 4, 5, 6, 7
		}
		return result;
	}
}