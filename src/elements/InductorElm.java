package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;
import base.Inductor;

public class InductorElm extends CircuitElm {
	Inductor ind;
	private double inductance;

	public InductorElm(int xx, int yy) {
		super(xx, yy);
		ind = new Inductor(sim);
		setInductance(1);
		ind.setup(getInductance(), current, flags);
	}

	public InductorElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		ind = new Inductor(sim);
		setInductance(new Double(st.nextToken()).doubleValue());
		current = new Double(st.nextToken()).doubleValue();
		ind.setup(getInductance(), current, flags);
	}

	public int getDumpType() {
		return 'l';
	}

	public String dump() {
		return super.dump() + " " + getInductance() + " " + current;
	}

	public void setPoints() {
		super.setPoints();
		calcLeads(32);
	}

	public void draw(Graphics g) {
		double v1 = volts[0];
		double v2 = volts[1];
		int i;
		int hs = 8;
		setBbox(point1, point2, hs);
		draw2Leads(g);
		setPowerColor(g, false);
		drawCoil(g, 8, lead1, lead2, v1, v2);
		if (sim.getShowValuesCheckItem().getState()) {
			String s = getShortUnitText(getInductance(), "H");
			drawValues(g, s, hs);
		}
		doDots(g);
		drawPosts(g);
	}

	public void reset() {
		current = volts[0] = volts[1] = curcount = 0;
		ind.reset();
	}

	public void stamp() {
		ind.stamp(nodes[0], nodes[1]);
	}

	public void startIteration() {
		ind.startIteration(volts[0] - volts[1]);
	}

	public boolean nonLinear() {
		return ind.nonLinear();
	}

	void calculateCurrent() {
		double voltdiff = volts[0] - volts[1];
		current = ind.calculateCurrent(voltdiff);
	}

	public void doStep() {
		double voltdiff = volts[0] - volts[1];
		ind.doStep(voltdiff);
	}

	public void getInfo(String arr[]) {
		arr[0] = "inductor";
		getBasicInfo(arr);
		arr[3] = "L = " + getUnitText(getInductance(), "H");
		arr[4] = "P = " + getUnitText(getPower(), "W");
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0)
			return new EditInfo("Inductance (H)", getInductance(), 0, 0);
		if (n == 1) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Trapezoidal Approximation", ind.isTrapezoidal()));
			return ei;
		}
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0)
			setInductance(ei.getValue());
		if (n == 1) {
			if (ei.getCheckbox().getState())
				flags &= ~Inductor.FLAG_BACK_EULER;
			else
				flags |= Inductor.FLAG_BACK_EULER;
		}
		ind.setup(getInductance(), current, flags);
	}

	public double getInductance() {
		return inductance;
	}

	public void setInductance(double inductance) {
		this.inductance = inductance;
	}
}
