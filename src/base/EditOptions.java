package base;
import elements.CircuitElm;

public class EditOptions implements Editable {
	CirSim sim;

	public EditOptions(CirSim s) {
		sim = s;
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0)
			return new EditInfo("Time step size (s)", sim.getTimeStep(), 0, 0);
		if (n == 1)
			return new EditInfo("Range for voltage color (V)", CircuitElm.getVoltageRange(), 0, 0);

		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0 && ei.getValue() > 0)
			sim.setTimeStep(ei.getValue());
		if (n == 1 && ei.getValue() > 0)
			CircuitElm.setVoltageRange(ei.getValue());
	}
};
