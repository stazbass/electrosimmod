package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

public class LogicInputElm extends SwitchElm {
	final int FLAG_TERNARY = 1;
	final int FLAG_NUMERIC = 2;
	double hiV, loV;

	public LogicInputElm(int xx, int yy) {
		super(xx, yy, false);
		hiV = 5;
		loV = 0;
	}

	public LogicInputElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f, st);
		try {
			hiV = new Double(st.nextToken()).doubleValue();
			loV = new Double(st.nextToken()).doubleValue();
		} catch (Exception e) {
			hiV = 5;
			loV = 0;
		}
		if (isTernary())
			posCount = 3;
	}

	boolean isTernary() {
		return (flags & FLAG_TERNARY) != 0;
	}

	boolean isNumeric() {
		return (flags & (FLAG_TERNARY | FLAG_NUMERIC)) != 0;
	}

	public int getDumpType() {
		return 'L';
	}

	public String dump() {
		return super.dump() + " " + hiV + " " + loV;
	}

	public int getPostCount() {
		return 1;
	}

	public void setPoints() {
		super.setPoints();
		lead1 = interpPoint(point1, point2, 1 - 12 / dn);
	}

	public void draw(Graphics g) {
		Font f = new Font("SansSerif", Font.BOLD, 20);
		g.setFont(f);
		g.setColor(needsHighlight() ? getSelectColor() : getWhiteColor());
		String s = position == 0 ? "L" : "H";
		if (isNumeric())
			s = "" + position;
		setBbox(point1, lead1, 0);
		drawCenteredText(g, s, getX2(), getY2(), true);
		setVoltageColor(g, volts[0]);
		drawThickLine(g, point1, lead1);
		updateDotCount();
		drawDots(g, point1, lead1, curcount);
		drawPosts(g);
	}

	public void setCurrent(int vs, double c) {
		current = -c;
	}

	public void stamp() {
		double v = (position == 0) ? loV : hiV;
		if (isTernary())
			v = position * 2.5;
		sim.stampVoltageSource(0, nodes[0], voltSource, v);
	}

	public int getVoltageSourceCount() {
		return 1;
	}

	public double getVoltageDiff() {
		return volts[0];
	}

	public void getInfo(String arr[]) {
		arr[0] = "logic input";
		arr[1] = (position == 0) ? "low" : "high";
		if (isNumeric())
			arr[1] = "" + position;
		arr[1] += " (" + getVoltageText(volts[0]) + ")";
		arr[2] = "I = " + getCurrentText(getCurrent());
	}

	public boolean hasGroundConnection(int n1) {
		return true;
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0) {
			EditInfo ei = new EditInfo("", 0, 0, 0);
			ei.setCheckbox(new Checkbox("Momentary Switch", isMomentary()));
			return ei;
		}
		if (n == 1)
			return new EditInfo("High Voltage", hiV, 10, -10);
		if (n == 2)
			return new EditInfo("Low Voltage", loV, 10, -10);
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0)
			setMomentary(ei.getCheckbox().getState());
		if (n == 1)
			hiV = ei.getValue();
		if (n == 2)
			loV = ei.getValue();
	}

	public int getShortcut() {
		return 'i';
	}
}
