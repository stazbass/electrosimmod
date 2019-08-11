package elements;
public class ACVoltageElm extends VoltageElement {
	public ACVoltageElm(int xx, int yy) {
		super(xx, yy, WF_AC);
	}

	public Class getDumpClass() {
		return VoltageElement.class;
	}
}
