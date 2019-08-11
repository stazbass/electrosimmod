package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

public class CapacitorElm extends CircuitElm {
	private double capacitance;
	double compResistance, voltdiff;
	Point plate1[], plate2[];
	public static final int FLAG_BACK_EULER = 2;

	public CapacitorElm(int xx, int yy) {
		super(xx, yy);
		setCapacitance(1e-5);
	}

	public CapacitorElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		setCapacitance(new Double(st.nextToken()).doubleValue());
		voltdiff = new Double(st.nextToken()).doubleValue();
	}

	boolean isTrapezoidal() {
		return (flags & FLAG_BACK_EULER) == 0;
	}

	public void setNodeVoltage(int n, double c) {
		super.setNodeVoltage(n, c);
		voltdiff = volts[0] - volts[1];
	}

	public void reset() {
		current = curcount = 0;
		// put small charge on caps when reset to start oscillators
		voltdiff = 1e-3;
	}

	public int getDumpType() {
		return 'c';
	}

	public String dump() {
		return super.dump() + " " + getCapacitance() + " " + voltdiff;
	}

	public void setPoints() {
		super.setPoints();
		double f = (dn / 2 - 4) / dn;
		// calc leads
		lead1 = interpPoint(point1, point2, f);
		lead2 = interpPoint(point1, point2, 1 - f);
		// calc plates
		plate1 = newPointArray(2);
		plate2 = newPointArray(2);
		interpPoint2(point1, point2, plate1[0], plate1[1], f, 12);
		interpPoint2(point1, point2, plate2[0], plate2[1], 1 - f, 12);
	}

	public void draw(Graphics g) {
		int hs = 12;
		setBbox(point1, point2, hs);

		// draw first lead and plate
		setVoltageColor(g, volts[0]);
		drawThickLine(g, point1, lead1);
		setPowerColor(g, false);
		drawThickLine(g, plate1[0], plate1[1]);
		if (sim.getPowerCheckItem().getState())
			g.setColor(Color.gray);

		// draw second lead and plate
		setVoltageColor(g, volts[1]);
		drawThickLine(g, point2, lead2);
		setPowerColor(g, false);
		drawThickLine(g, plate2[0], plate2[1]);

		updateDotCount();
		if (sim.getDragElm() != this) {
			drawDots(g, point1, lead1, curcount);
			drawDots(g, point2, lead2, -curcount);
		}
		drawPosts(g);
		if (sim.getShowValuesCheckItem().getState()) {
			String s = getShortUnitText(getCapacitance(), "F");
			drawValues(g, s, hs);
		}
	}

	public void stamp() {
		// capacitor companion model using trapezoidal approximation
		// (Norton equivalent) consists of a current source in
		// parallel with a resistor. Trapezoidal is more accurate
		// than backward euler but can cause oscillatory behavior
		// if RC is small relative to the timestep.
		if (isTrapezoidal())
			compResistance = sim.getTimeStep() / (2 * getCapacitance());
		else
			compResistance = sim.getTimeStep() / getCapacitance();
		sim.stampResistor(nodes[0], nodes[1], compResistance);
		sim.stampRightSide(nodes[0]);
		sim.stampRightSide(nodes[1]);
	}

	public void startIteration() {
		if (isTrapezoidal())
			curSourceValue = -voltdiff / compResistance - current;
		else
			curSourceValue = -voltdiff / compResistance;
		// System.out.println("cap " + compResistance + " " + curSourceValue + " " +
		// current + " " + voltdiff);
	}

	void calculateCurrent() {
		double voltdiff = volts[0] - volts[1];
		// we check compResistance because this might get called
		// before stamp(), which sets compResistance, causing
		// infinite current
		if (compResistance > 0)
			current = voltdiff / compResistance + curSourceValue;
	}

	double curSourceValue;

	public void doStep() {
		sim.stampCurrentSource(nodes[0], nodes[1], curSourceValue);
	}

	public void getInfo(String arr[]) {
		arr[0] = "capacitor";
		getBasicInfo(arr);
		arr[3] = "C = " + getUnitText(getCapacitance(), "F");
		arr[4] = "P = " + getUnitText(getPower(), "W");
		// double v = getVoltageDiff();
		// arr[4] = "U = " + getUnitText(.5*capacitance*v*v, "J");
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0)
			return new EditInfo("Capacitance (F)", getCapacitance(), 0, 0);
		if (n == 1) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Trapezoidal Approximation", isTrapezoidal()));
			return ei;
		}
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0 && ei.getValue() > 0)
			setCapacitance(ei.getValue());
		if (n == 1) {
			if (ei.getCheckbox().getState())
				flags &= ~FLAG_BACK_EULER;
			else
				flags |= FLAG_BACK_EULER;
		}
	}

	public int getShortcut() {
		return 'c';
	}

	public double getCapacitance() {
		return capacitance;
	}

	public void setCapacitance(double capacitance) {
		this.capacitance = capacitance;
	}
}
