
public class PolynomModul {
	
	public static int run(int polynom, int modul) {
		int result = polynom;
		int mask = 0;
		int modulDegree = 0;
		
		for (int i = Integer.SIZE - 1; i > 0; i--) {
			mask = 1 << i;
			if ((int)(modul & mask) > 0) {
				modulDegree = i;
				break;
			}
		}
		for (int i = Integer.SIZE - 1; i > 0; i--) {
			mask = 1 << i;
			if (polynom < modul) {
				if ((int)(polynom & 0x100) > 0) {
					polynom = polynom ^ modul;
				}
				result = polynom;
				break;
			} else if ((int)(polynom & mask) > 0) {
				polynom = polynom ^ (modul << (i - modulDegree));
			}
		}
		return result;
	}
}