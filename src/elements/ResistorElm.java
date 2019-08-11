package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

public class ResistorElm extends CircuitElm {
	private double resistance;

	public ResistorElm(int xx, int yy) {
		super(xx, yy);
		setResistance(100);
	}

	public ResistorElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		setResistance(new Double(st.nextToken()).doubleValue());
	}

	public int getDumpType() {
		return 'r';
	}

	public String dump() {
		return super.dump() + " " + getResistance();
	}

	Point ps3, ps4;

	public void setPoints() {
		super.setPoints();
		calcLeads(32);
		ps3 = new Point();
		ps4 = new Point();
	}

	public void draw(Graphics g) {
		int segments = 16;
		int i;
		int ox = 0;
		int hs = sim.getEuroResistorCheckItem().getState() ? 6 : 8;
		double v1 = volts[0];
		double v2 = volts[1];
		setBbox(point1, point2, hs);
		draw2Leads(g);
		setPowerColor(g, true);
		double segf = 1. / segments;
		if (!sim.getEuroResistorCheckItem().getState()) {
			// draw zigzag
			for (i = 0; i != segments; i++) {
				int nx = 0;
				switch (i & 3) {
				case 0:
					nx = 1;
					break;
				case 2:
					nx = -1;
					break;
				default:
					nx = 0;
					break;
				}
				double v = v1 + (v2 - v1) * i / segments;
				setVoltageColor(g, v);
				interpPoint(lead1, lead2, ps1, i * segf, hs * ox);
				interpPoint(lead1, lead2, ps2, (i + 1) * segf, hs * nx);
				drawThickLine(g, ps1, ps2);
				ox = nx;
			}
		} else {
			// draw rectangle
			setVoltageColor(g, v1);
			interpPoint2(lead1, lead2, ps1, ps2, 0, hs);
			drawThickLine(g, ps1, ps2);
			for (i = 0; i != segments; i++) {
				double v = v1 + (v2 - v1) * i / segments;
				setVoltageColor(g, v);
				interpPoint2(lead1, lead2, ps1, ps2, i * segf, hs);
				interpPoint2(lead1, lead2, ps3, ps4, (i + 1) * segf, hs);
				drawThickLine(g, ps1, ps3);
				drawThickLine(g, ps2, ps4);
			}
			interpPoint2(lead1, lead2, ps1, ps2, 1, hs);
			drawThickLine(g, ps1, ps2);
		}
		if (sim.getShowValuesCheckItem().getState()) {
			String s = getShortUnitText(getResistance(), "");
			drawValues(g, s, hs);
		}
		doDots(g);
		drawPosts(g);
	}

	void calculateCurrent() {
		current = (volts[0] - volts[1]) / getResistance();
		// System.out.print(this + " res current set to " + current + "\n");
	}

	public void stamp() {
		sim.stampResistor(nodes[0], nodes[1], getResistance());
	}

	public void getInfo(String arr[]) {
		arr[0] = "resistor";
		getBasicInfo(arr);
		arr[3] = "R = " + getUnitText(getResistance(), sim.getOhmString());
		arr[4] = "P = " + getUnitText(getPower(), "W");
	}

	public EditInfo getEditInfo(int n) {
		// ohmString doesn't work here on linux
		if (n == 0)
			return new EditInfo("Resistance (ohms)", getResistance(), 0, 0);
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (ei.getValue() > 0)
			setResistance(ei.getValue());
	}

	public int getShortcut() {
		return 'r';
	}

	public double getResistance() {
		return resistance;
	}

	public void setResistance(double resistance) {
		this.resistance = resistance;
	}
}
