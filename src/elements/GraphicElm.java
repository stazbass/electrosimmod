package elements;
public class GraphicElm extends CircuitElm {

	public GraphicElm(int xx, int yy) {
		super(xx, yy);
	}

	public GraphicElm(int xa, int ya, int xb, int yb, int flags) {
		super(xa, ya, xb, yb, flags);
	}

	public int getPostCount() {
		return 0;
	}
}
