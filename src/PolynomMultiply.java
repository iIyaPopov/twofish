public class PolynomMultiply {
	
	public static int run(int x, int y) {
		int result = 0;
		int degree = 0;
		int mask = 0x1;
		while (y != 0) {
			if ((int)( y & mask) > 0) {
				result = result ^ (x << degree);
			}
			y = y >>> 1;
			degree++;
		}
		return result;
	}
}