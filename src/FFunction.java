import java.io.*;
import java.util.*;

public class FFunction {
	private static final int RESULT_BLOCK_COUNT = 2;
	private static final long MODUL = 0x100000000L;

	public static int[] run(int R0, int R1, int round) {
		int[] result = new int[RESULT_BLOCK_COUNT];
		R1 = CyclicShift.toLeft(R1, Integer.SIZE, 8);
		int T0 = GFunction.run(R0);
		int T1 = GFunction.run(R1);
		PHT pht = new PHT(T0, T1);
		pht.directTransformation();
		int A = pht.getA();
		int B = pht.getB();
		result[0] = (int)( (A + Twofish.getKSubkey(2 * round + 8)) % MODUL);
		result[1] = (int)( (B + Twofish.getKSubkey(2 * round + 9)) % MODUL);
		return result;
	}
}