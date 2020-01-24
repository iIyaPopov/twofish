import java.io.*;
import java.util.*;

public class Twofish {
	private final int KEY_LENGTH 	= 128;
	private final int BLOCK_SIZE 	= 4;
	private final int ROUND_COUNT 	= 16;
	
	private static byte[] globalKey;
	private static HFunction KSubkeys;
	private static KeySchedule subkeys;
	private static byte endBytesCount;
	private Data data;
	
	public Twofish(byte[] key, Data data) {
		this.globalKey = key;
		subkeys = new KeySchedule(globalKey);
		KSubkeys = new HFunction();
		this.data = data;
	}
	
	public static int getKSubkey(int index) {
		return KSubkeys.getKSubkey(index);
	}
	
	public static int getMSubkey(int index) {
		return subkeys.getMSubkey(index);
	}
	
	public static int getSSubkey(int index) {
		return subkeys.getSSubkey(index);
	}
	
	public static byte[] getGlobalKey() {
		return globalKey;
	}
	
	public static void setEndBytesCount(byte value) {
		endBytesCount = value;
	}
	
	public void encrypt(String filename) throws IOException {
		File output = new File(filename);
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(endBytesCount);
		while (true) {
			int[] dataBlocks = data.getBlock();
			if (dataBlocks == null) {
				break;
			}
			
			int[] R = InputWhitening.run(dataBlocks);
		
			for (int round = 0; round < this.ROUND_COUNT; round++) {		
				if (round % 2 == 0) {
					int[] F = FFunction.run(R[0], R[1], round);
					R[2] = CyclicShift.toRight((F[0] ^ R[2]), Integer.SIZE, 1);
					R[3] = F[1] ^ (CyclicShift.toLeft(R[3], Integer.SIZE, 1));
				} else {
					int[] F = FFunction.run(R[2], R[3], round);
					R[0] = CyclicShift.toRight((F[0] ^ R[0]), Integer.SIZE, 1);
					R[1] = F[1] ^ (CyclicShift.toLeft(R[1], Integer.SIZE, 1));
				}
			}

			int A = R[0];
			int B = R[1];
			R[0] = R[2];
			R[1] = R[3];
			R[2] = A;
			R[3] = B;
			
			int[] result = OutputWhitening.run(R);
			for (int i = 0; i < result.length; i++) {
				result[i] = ByteInverse.run(result[i]);
			}
			for (int i = 0; i < this.BLOCK_SIZE; i++) {
				byte[] resultBytes = Splitter.run(result[i]);
				fos.write(resultBytes);
			}
		}
		fos.close();
	}
	
	public void decrypt(String filename) throws IOException {
		File output = new File(filename);
		FileOutputStream fos = new FileOutputStream(output);
		byte endBytes = data.getFirstByte();
		
		for (int blockIterator = 0; blockIterator < data.getBlocksCount(); blockIterator++) {
			int[] dataBlocks = data.getBlock();
			if (dataBlocks == null) {
				break;
			}
		
			int[] R = OutputWhitening.run(dataBlocks);
		
			int A = R[0];
			int B = R[1];
			R[0] = R[2];
			R[1] = R[3];
			R[2] = A;
			R[3] = B;
		
			for (int round = this.ROUND_COUNT - 1; round >= 0; round--) {	
				if ((round) % 2 == 0) {
					int[] F = FFunction.run(R[0], R[1], round);
					R[2] = F[0] ^ CyclicShift.toLeft(R[2], Integer.SIZE, 1);
					R[3] = CyclicShift.toRight(F[1] ^ R[3], Integer.SIZE, 1);
				} else {
					int[] F = FFunction.run(R[2], R[3], round);
					R[0] = F[0] ^ CyclicShift.toLeft(R[0], Integer.SIZE, 1);
					R[1] = CyclicShift.toRight(F[1] ^ R[1], Integer.SIZE, 1);
				}
			}
	
			int[] result = InputWhitening.run(R);

			for (int i = 0; i < dataBlocks.length; i++) {
				result[i] = ByteInverse.run(result[i]);
			}
		
			if (blockIterator == data.getBlocksCount() - 2) {
				int count = 0;
				for (int i = 0; i < this.BLOCK_SIZE; i++) {
					byte[] resultBytes = Splitter.run(result[i]);
					for (int j = 0; j < 4; j++) {
						if (count == endBytes) {
							i = 5;
							j = 5;
							break;
						}
						fos.write(resultBytes[j]);
						count++;
					}
				}
			} else {
				for (int i = 0; i < this.BLOCK_SIZE; i++) {
					byte[] resultBytes = Splitter.run(result[i]);
					fos.write(resultBytes);
				}
			}
		}
		fos.close();
	}
	
	public static void main(String[] args) {
		String dir = "/home/ilya/Desktop/Twofish/tests/";
		String dataFile = dir + "test.bmp";
		String keyFile = dir + "key.bin";
		String encodedFile = dir + "encoded.bin";
		String decodedFile = dir + "decoded.bmp";
		try {
			//Key key = new Key(16, dir + "rand_key.bin");
			Key key = new Key(keyFile);
			
			Data data = new Data(dataFile);
			Twofish twofish = new Twofish(key.getKey(), data);
			twofish.encrypt(encodedFile);
			
			Data data2 = new Data(encodedFile);
			Twofish twofish2 = new Twofish(key.getKey(), data2);
			twofish2.decrypt(decodedFile);
			
			System.out.println(CorrelationCoefficient.toCount(dataFile, encodedFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}