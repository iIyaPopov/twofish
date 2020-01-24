
import java.io.*;
import java.util.*;

public class KeySchedule {
	private final static int[][] RS = {
		{0x01, 0xA4, 0x55, 0x87, 0x5A, 0x58, 0xDB, 0x9E},
		{0xA4, 0x56, 0x82, 0xF3, 0x1E, 0xC6, 0x68, 0xE5},
		{0x02, 0xA1, 0xFC, 0xC1, 0x47, 0xAE, 0x3D, 0x19},
		{0xA4, 0x55, 0x87, 0x5A, 0x58, 0xDB, 0x9E, 0x03}
	};
	private final int MODUL = 0x14D;	//x^8 + x^6 + x^3 + x^2 + 1
	private static final int BLOCK_COUNT = 4;
	private static final int M_BLOCK_BYTE_COUNT = 4;
	private static final int S_BLOCK_BYTE_COUNT = 8;
	private static final int MASK = 0xff;

	private int globalKeySize;
	private byte[][] SSubkey;
	private byte[][] MSubkey;
	
	public KeySchedule(byte[] globalKey) {
		this.globalKeySize = globalKey.length;
		this.SKeyGen(globalKey);
		this.MKeyGen(globalKey);
	}
	
	private void SKeyGen(byte[] globalKey) {
		int size = this.globalKeySize / this.S_BLOCK_BYTE_COUNT;
		this.SSubkey = new byte[size][this.BLOCK_COUNT];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < this.BLOCK_COUNT; j++) {
				int tmp = 0;
				for (int k = i * this.S_BLOCK_BYTE_COUNT; k < (i + 1) * this.S_BLOCK_BYTE_COUNT; k++) {
					tmp = tmp ^ PolynomMultiply.run(this.RS[j][k % this.S_BLOCK_BYTE_COUNT], ((int) globalKey[k] & 0xff));
				}
				this.SSubkey[i][j] = (byte) PolynomModul.run(tmp, MODUL);
			}
		}
	}
	
	private void MKeyGen(byte[] globalKey) {
		int size = this.globalKeySize / this.M_BLOCK_BYTE_COUNT;
		this.MSubkey = new byte[size][this.BLOCK_COUNT];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < this.M_BLOCK_BYTE_COUNT; j++) {
				this.MSubkey[i][j] = globalKey[this.M_BLOCK_BYTE_COUNT * i + j];
			}
		}
	}
	
	public int getSSubkey(int index) {
		return Combiner.run(SSubkey[index]);
	}
	
	public int getMSubkey(int index) {
		return Combiner.run(MSubkey[index]);
	}
}