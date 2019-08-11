package elements;
public class DCVoltageElm extends VoltageElement {
	public DCVoltageElm(int xx, int yy) {
		super(xx, yy, WF_DC);
	}

	public Class getDumpClass() {
		return VoltageElement.class;
	}

	public int getShortcut() {
		return 'v';
	}
}
