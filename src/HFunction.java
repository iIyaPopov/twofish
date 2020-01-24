
import java.util.Arrays;
public class HFunction {
	private final int KEYS_COUNT = 40;
	private final int BYTE_COUNT = 4;
	private final int Ro = 0x01010101;
	
	private int[] KKey;
	
	public HFunction() {
		this.KKey = new int[KEYS_COUNT];
		this.keygen();
	}
	
	private void keygen() {
		for (int k = 0; k < this.KEYS_COUNT; k += 2) {
			int even = Ro * k;
			int odd = Ro * (k + 1);
			
			SBox evenSBox = new SBox(even);
			SBox oddSBox = new SBox(odd);
			
			byte[] evenBytes = evenSBox.M_run(SBox.M_EVEN);
			byte[] oddBytes = oddSBox.M_run(SBox.M_ODD);
			
			int evenMDSres = MDSMultiply.run(evenBytes);
			int oddMDSres = MDSMultiply.run(oddBytes);
			
			PHT pht = new PHT(evenMDSres, CyclicShift.toLeft(oddMDSres, Integer.SIZE, 8));
			pht.directTransformation();
			
			this.KKey[k] = pht.getA();
			this.KKey[k+1] = CyclicShift.toLeft(pht.getB(), Integer.SIZE, 9);
		}
	}
	
	public int getKSubkey(int index) {
		return this.KKey[index];
	}
}