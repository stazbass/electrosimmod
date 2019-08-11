package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

public class OutputElm extends CircuitElm {
	final int FLAG_VALUE = 1;

	public OutputElm(int xx, int yy) {
		super(xx, yy);
	}

	public OutputElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
	}

	public int getDumpType() {
		return 'O';
	}

	public int getPostCount() {
		return 1;
	}

	public void setPoints() {
		super.setPoints();
		lead1 = new Point();
	}

	public void draw(Graphics g) {
		boolean selected = (needsHighlight() || sim.getPlotYElm() == this);
		Font f = new Font("SansSerif", selected ? Font.BOLD : 0, 14);
		g.setFont(f);
		g.setColor(selected ? getSelectColor() : getWhiteColor());
		String s = (flags & FLAG_VALUE) != 0 ? getVoltageText(volts[0]) : "out";
		FontMetrics fm = g.getFontMetrics();
		if (this == sim.getPlotXElm())
			s = "X";
		if (this == sim.getPlotYElm())
			s = "Y";
		interpPoint(point1, point2, lead1, 1 - (fm.stringWidth(s) / 2 + 8) / dn);
		setBbox(point1, lead1, 0);
		drawCenteredText(g, s, getX2(), getY2(), true);
		setVoltageColor(g, volts[0]);
		if (selected)
			g.setColor(getSelectColor());
		drawThickLine(g, point1, lead1);
		drawPosts(g);
	}

	public double getVoltageDiff() {
		return volts[0];
	}

	public void getInfo(String arr[]) {
		arr[0] = "output";
		arr[1] = "V = " + getVoltageText(volts[0]);
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Show Voltage", (flags & FLAG_VALUE) != 0));
			return ei;
		}
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0)
			flags = (ei.getCheckbox().getState()) ? (flags | FLAG_VALUE) : (flags & ~FLAG_VALUE);
	}
}
