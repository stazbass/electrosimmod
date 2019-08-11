package elements;

public class SquareRailElm extends RailElement {
	public SquareRailElm(int xx, int yy) {
		super(xx, yy, WF_SQUARE);
	}

	public Class getDumpClass() {
		return RailElement.class;
	}

	public int getShortcut() {
		return 0;
	}
}
