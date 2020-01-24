
public class GFunction {
	private static final int BLOCK_COUNT = 4;
	
	public static int run(int x) {
		byte[] bytes = Splitter.run(x);
		SBox sBox = new SBox(x);
		bytes = sBox.S_run();
		int result = MDSMultiply.run(bytes);
		return result;
	}
}