
public class PHT {
	private final long MODUL = 0x100000000L;
	
	private int a;
	private int b;
	
	public PHT(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	public void directTransformation() {
		int _a = (int)((long)(this.a + this.b) % this.MODUL);
		int _b = (int)((long)(this.a + 2 * this.b) % this.MODUL);
		this.a = _a;
		this.b = _b;
	}
	
	public int getA() {
		return this.a;
	}
	
	public int getB() {
		return this.b;
	}
}