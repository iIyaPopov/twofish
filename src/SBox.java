
import java.io.*;
import java.util.*;

public class SBox {
	public static final int M_ODD = 0;
	public static final int M_EVEN = 1;
	public static final int S = 2;
	public static final int KEY_SIZE_128 = 16;
	public static final int KEY_SIZE_192 = 24;
	public static final int KEY_SIZE_256 = 32;
	public static final int BYTE_COUNT = 4;
	
	private int x;
	private byte[] bytes;
	private byte[] globalKey;
	private int globalKeySize;
	private QFunction q;
	
	public SBox(int x) {
		this.x = x;
		this.globalKey = Twofish.getGlobalKey();
		this.globalKeySize = this.globalKey.length;
		this.q = new QFunction();
	}
	
	public byte[] M_run(int param) {
		if (param == M_ODD) {
			switch (this.globalKeySize) {
				case KEY_SIZE_256:
					this.doFor256Key(Twofish.getMSubkey(7));
				case KEY_SIZE_192:
					this.doFor192Key(Twofish.getMSubkey(5));
				case KEY_SIZE_128:
					this.doFor128Key(Twofish.getMSubkey(3), Twofish.getMSubkey(1));
			}
		} else if (param == M_EVEN) {
			switch (this.globalKeySize) {
				case KEY_SIZE_256:
					this.doFor256Key(Twofish.getMSubkey(6));
				case KEY_SIZE_192:
					this.doFor192Key(Twofish.getMSubkey(4));
				case KEY_SIZE_128:
					this.doFor128Key(Twofish.getMSubkey(2), Twofish.getMSubkey(0));
			}
		}
		return this.bytes;
	}
	
	public byte[] S_run() {
		int max = this.globalKeySize / Byte.SIZE - 1;
		switch (this.globalKeySize) {
			case KEY_SIZE_256:
				this.doFor256Key(Twofish.getSSubkey(max - 3));
			case KEY_SIZE_192:
				this.doFor192Key(Twofish.getSSubkey(max - 2));
			case KEY_SIZE_128:
				this.doFor128Key(Twofish.getSSubkey(max - 1), Twofish.getSSubkey(max - 0));
		}
		return this.bytes;
	}
	
	private void doFor256Key(int key3) {
		this.bytes = Splitter.run(this.x);
		this.bytes[0] = this.q.run(bytes[0], 1);
		this.bytes[1] = this.q.run(bytes[1], 0);
		this.bytes[2] = this.q.run(bytes[2], 0);
		this.bytes[3] = this.q.run(bytes[3], 1);
		this.x = Combiner.run(this.bytes);
		this.x = this.x ^ key3;
	}
	
	private void doFor192Key(int key2) {
		this.bytes = Splitter.run(this.x);
		this.bytes[0] = this.q.run(bytes[0], 1);
		this.bytes[1] = this.q.run(bytes[1], 1);
		this.bytes[2] = this.q.run(bytes[2], 0);
		this.bytes[3] = this.q.run(bytes[3], 0);
		this.x = Combiner.run(this.bytes);
		this.x = this.x ^ key2;
	}
	
	public void doFor128Key(int key1, int key0) {
		this.bytes = Splitter.run(this.x);
		this.bytes[0] = this.q.run(bytes[0], 0);
		this.bytes[1] = this.q.run(bytes[1], 1);
		this.bytes[2] = this.q.run(bytes[2], 0);
		this.bytes[3] = this.q.run(bytes[3], 1);
		this.x = Combiner.run(this.bytes);
		this.x = this.x ^ key1;
		this.bytes = Splitter.run(this.x);
		this.bytes[0] = this.q.run(bytes[0], 0);
		this.bytes[1] = this.q.run(bytes[1], 0);
		this.bytes[2] = this.q.run(bytes[2], 1);
		this.bytes[3] = this.q.run(bytes[3], 1);
		this.x = Combiner.run(this.bytes);
		this.x = this.x ^ key0;
		this.bytes = Splitter.run(this.x);
		this.bytes[0] = this.q.run(bytes[0], 1);
		this.bytes[1] = this.q.run(bytes[1], 0);
		this.bytes[2] = this.q.run(bytes[2], 1);
		this.bytes[3] = this.q.run(bytes[3], 0);
	}
}