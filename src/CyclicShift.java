
public class CyclicShift {
	
	public static int toRight(int number, int size, int bitCount) {
		int tmp = number >>> bitCount;
		tmp = tmp | (number << (size - bitCount));
		int mask = 1;
		int result = 0;
		for (int i = 0; i < size; i++) {			
			result = result | (tmp & mask);
			mask = mask << 1;
		}
		return result;
	}
	
	public static int toLeft(int number, int size, int bitCount) {
		int tmp = number << bitCount;
		tmp = tmp | (number >>> (size - bitCount));
		int mask = 1;
		int result = 0;
		for (int i = 0; i < size; i++) {			
			result = result | (tmp & mask);
			mask = mask << 1;
		}
		return result;
	}
}