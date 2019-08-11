package elements;
public class NJfetElm extends JfetElm {
	public NJfetElm(int xx, int yy) {
		super(xx, yy, false);
	}

	public Class getDumpClass() {
		return JfetElm.class;
	}
}

class PJfetElm extends JfetElm {
	public PJfetElm(int xx, int yy) {
		super(xx, yy, true);
	}

	public Class getDumpClass() {
		return JfetElm.class;
	}
}
