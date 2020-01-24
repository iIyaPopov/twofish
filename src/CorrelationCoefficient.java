import java.io.*;

public class CorrelationCoefficient {
	
	public static double toCount(String filename1, String filename2) throws IOException, FileNotFoundException {
		FileInputStream fis1 = new FileInputStream(new File(filename1));
		FileInputStream fis2 = new FileInputStream(new File(filename2));
		int data1 = -1;
		int data2 = -1;
		int coefficient = 0;
		int oneCount = 0;
		int zeroCount = 0;
		int bytesCount = 0;
		while ((data1 = fis1.read()) != -1 || (data2 = fis2.read()) != -1) {
			bytesCount += Byte.SIZE;
			int mask = 0x1;
			for (int i = 0; i < Byte.SIZE; i++) {
				if ((int)(data2 & mask) > 0) {
					oneCount++;
				} else {
					zeroCount++;
				}
				if ((data1 & mask) == (data2 & mask)) {
					coefficient++; 
				}
				mask = mask << 1;
			}
		}
		double result = ((double) coefficient - (double) (bytesCount - coefficient)) / bytesCount;
		System.out.println("oneCount = " + (double)oneCount / bytesCount);
		System.out.println("zeroCount = " + (double) zeroCount / bytesCount);
		return result;
	}
	
	public static void main(String[] args) {
		String dir = "/home/ilya/Desktop/Twofish/tests/";
		String dataFile = dir + "image.jpg";
		String encodedFile = dir + "encoded.bin";
		try {
			System.out.println(CorrelationCoefficient.toCount(dataFile, encodedFile));
		} catch (IOException e) {
		}
	}
}