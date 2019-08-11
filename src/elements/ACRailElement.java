package elements;

public class ACRailElement extends RailElement {
	public ACRailElement(int xx, int yy) {
		super(xx, yy, WF_AC);
	}

	public Class getDumpClass() {
		return RailElement.class;
	}

	public int getShortcut() {
		return 0;
	}
}
