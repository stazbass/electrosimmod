package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

// contributed by Edward Calver

public class AMElm extends CircuitElm {
	static final int FLAG_COS = 2;
	double carrierfreq, signalfreq, maxVoltage, freqTimeZero;

	public AMElm(int xx, int yy) {
		super(xx, yy);
		maxVoltage = 5;
		carrierfreq = 1000;
		signalfreq = 40;
		reset();
	}

	public AMElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		carrierfreq = new Double(st.nextToken()).doubleValue();
		signalfreq = new Double(st.nextToken()).doubleValue();
		maxVoltage = new Double(st.nextToken()).doubleValue();
		if ((flags & FLAG_COS) != 0) {
			flags &= ~FLAG_COS;
		}
		reset();
	}

	public int getDumpType() {
		return 200;
	}

	public String dump() {
		return super.dump() + " " + carrierfreq + " " + signalfreq + " " + maxVoltage;
	}
	/*
	 * void setCurrent(double c) { current = c; System.out.print("v current set to "
	 * + c + "\n"); }
	 */

	public void reset() {
		freqTimeZero = 0;
		curcount = 0;
	}

	public int getPostCount() {
		return 1;
	}

	public void stamp() {
		sim.stampVoltageSource(0, nodes[0], voltSource);
	}

	public void doStep() {
		sim.updateVoltageSource(0, nodes[0], voltSource, getVoltage());
	}

	double getVoltage() {
		double w = 2 * pi * (sim.getT() - freqTimeZero);
		return ((Math.sin(w * signalfreq) + 1) / 2) * Math.sin(w * carrierfreq) * maxVoltage;
	}

	final int circleSize = 17;

	public void draw(Graphics g) {
		setBbox(point1, point2, circleSize);
		setVoltageColor(g, volts[0]);
		drawThickLine(g, point1, lead1);

		Font f = new Font("SansSerif", 0, 12);
		g.setFont(f);
		g.setColor(needsHighlight() ? getSelectColor() : getWhiteColor());
		setPowerColor(g, false);
		double v = getVoltage();
		String s = "AM";
		drawCenteredText(g, s, getX2(), getY2(), true);
		drawWaveform(g, point2);
		drawPosts(g);
		curcount = updateDotCount(-current, curcount);
		if (sim.getDragElm() != this)
			drawDots(g, point1, lead1, curcount);
	}

	public void drawWaveform(Graphics g, Point center) {
		g.setColor(needsHighlight() ? getSelectColor() : Color.gray);
		setPowerColor(g, false);
		int xc = center.x;
		int yc = center.y;
		drawThickCircle(g, xc, yc, circleSize);
		int wl = 8;
		adjustBbox(xc - circleSize, yc - circleSize, xc + circleSize, yc + circleSize);
	}

	public void setPoints() {
		super.setPoints();
		lead1 = interpPoint(point1, point2, 1 - circleSize / dn);
	}

	public double getVoltageDiff() {
		return volts[0];
	}

	public boolean hasGroundConnection(int n1) {
		return true;
	}

	public int getVoltageSourceCount() {
		return 1;
	}

	public double getPower() {
		return -getVoltageDiff() * current;
	}

	public void getInfo(String arr[]) {

		arr[0] = "AM Source";
		arr[1] = "I = " + getCurrentText(getCurrent());
		arr[2] = "V = " + getVoltageText(getVoltageDiff());
		arr[3] = "cf = " + getUnitText(carrierfreq, "Hz");
		arr[4] = "sf = " + getUnitText(signalfreq, "Hz");
		arr[5] = "Vmax = " + getVoltageText(maxVoltage);
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0)
			return new EditInfo("Max Voltage", maxVoltage, -20, 20);
		if (n == 1)
			return new EditInfo("Carrier Frequency (Hz)", carrierfreq, 4, 500);
		if (n == 2)
			return new EditInfo("Signal Frequency (Hz)", signalfreq, 4, 500);

		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0)
			maxVoltage = ei.getValue();
		if (n == 1)
			carrierfreq = ei.getValue();
		if (n == 2)
			signalfreq = ei.getValue();
	}
}
