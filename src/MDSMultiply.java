
import java.io.*;
import java.util.*;

public class MDSMultiply {
	private static final int[][] MDS = {
		{0x01, 0xEF, 0x5B, 0x5B},
		{0x5B, 0xEF, 0xEF, 0x01},
		{0xEF, 0x5B, 0x01, 0xEF},
		{0xEF, 0x01, 0xEF, 0x5B}
	};
	private static final int BYTE_COUNT = 4;
	private static final int MODUL = 0x169;	//x^8 + x^6 + x^5 + x^3 + 1
	
	public static int run(byte[] bytes) {
		byte[] result = new byte[BYTE_COUNT];
		for (int i = 0; i < BYTE_COUNT; i++) {
			int tmp = 0;
			for (int j = 0; j < BYTE_COUNT; j++) {
				tmp = tmp ^ PolynomMultiply.run(MDS[i][j], ((int)bytes[j] & 0xff));
			}
			result[i] = (byte) PolynomModul.run(tmp, MODUL);
		}
		return Combiner.run(result);
	}
}