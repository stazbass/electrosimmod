package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

public class Switch2Elm extends SwitchElm {
	int link;
	static final int FLAG_CENTER_OFF = 1;

	public Switch2Elm(int xx, int yy) {
		super(xx, yy, false);
		noDiagonal = true;
	}

	Switch2Elm(int xx, int yy, boolean mm) {
		super(xx, yy, mm);
		noDiagonal = true;
	}

	public Switch2Elm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f, st);
		link = new Integer(st.nextToken()).intValue();
		noDiagonal = true;
	}

	public int getDumpType() {
		return 'S';
	}

	public String dump() {
		return super.dump() + " " + link;
	}

	final int openhs = 16;
	Point swposts[], swpoles[];

	public void setPoints() {
		super.setPoints();
		calcLeads(32);
		swposts = newPointArray(2);
		swpoles = newPointArray(3);
		interpPoint2(lead1, lead2, swpoles[0], swpoles[1], 1, openhs);
		swpoles[2] = lead2;
		interpPoint2(point1, point2, swposts[0], swposts[1], 1, openhs);
		posCount = hasCenterOff() ? 3 : 2;
	}

	public void draw(Graphics g) {
		setBbox(point1, point2, openhs);

		// draw first lead
		setVoltageColor(g, volts[0]);
		drawThickLine(g, point1, lead1);

		// draw second lead
		setVoltageColor(g, volts[1]);
		drawThickLine(g, swpoles[0], swposts[0]);

		// draw third lead
		setVoltageColor(g, volts[2]);
		drawThickLine(g, swpoles[1], swposts[1]);

		// draw switch
		if (!needsHighlight())
			g.setColor(getWhiteColor());
		drawThickLine(g, lead1, swpoles[position]);

		updateDotCount();
		drawDots(g, point1, lead1, curcount);
		if (position != 2)
			drawDots(g, swpoles[position], swposts[position], curcount);
		drawPosts(g);
	}

	public Point getPost(int n) {
		return (n == 0) ? point1 : swposts[n - 1];
	}

	public int getPostCount() {
		return 3;
	}

	public void calculateCurrent() {
		if (position == 2)
			current = 0;
	}

	public void stamp() {
		if (position == 2) // in center?
			return;
		sim.stampVoltageSource(nodes[0], nodes[position + 1], voltSource, 0);
	}

	public int getVoltageSourceCount() {
		return (position == 2) ? 0 : 1;
	}

	public void toggle () {
		super.toggle();
		if (link != 0) {
			int i;
			for (i = 0; i != sim.getElmList().size(); i++) {
				Object o = sim.getElmList().elementAt(i);
				if (o instanceof Switch2Elm) {
					Switch2Elm s2 = (Switch2Elm) o;
					if (s2.link == link)
						s2.position = position;
				}
			}
		}
	}

	public boolean getConnection(int n1, int n2) {
		if (position == 2)
			return false;
		return comparePair(n1, n2, 0, 1 + position);
	}

	public void getInfo(String arr[]) {
		arr[0] = (link == 0) ? "switch (SPDT)" : "switch (DPDT)";
		arr[1] = "I = " + getCurrentDText(getCurrent());
	}

	public EditInfo getEditInfo(int n) {
		if (n == 1) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Center Off", hasCenterOff()));
			return ei;
		}
		return super.getEditInfo(n);
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 1) {
			flags &= ~FLAG_CENTER_OFF;
			if (ei.getCheckbox().getState())
				flags |= FLAG_CENTER_OFF;
			if (hasCenterOff())
				setMomentary(false);
			setPoints();
		} else
			super.setEditValue(n, ei);
	}

	boolean hasCenterOff() {
		return (flags & FLAG_CENTER_OFF) != 0;
	}

	public int getShortcut() {
		return 'S';
	}
}
