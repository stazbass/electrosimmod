package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

// contributed by Edward Calver

public class TriStateElm extends CircuitElm {
	double resistance, r_on, r_off;

	public TriStateElm(int xx, int yy) {
		super(xx, yy);
		r_on = 0.1;
		r_off = 1e10;
	}

	public TriStateElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		r_on = 0.1;
		r_off = 1e10;
		try {
			r_on = new Double(st.nextToken()).doubleValue();
			r_off = new Double(st.nextToken()).doubleValue();
		} catch (Exception e) {
		}

	}

	public String dump() {
		return super.dump() + " " + r_on + " " + r_off;
	}

	public int getDumpType() {
		return 180;
	}

	boolean open;

	Point ps, point3, point4, lead3;

	Polygon gatePoly;

	public void setPoints() {
		super.setPoints();
		calcLeads(32);
		ps = new Point();
		int hs = 16;

		int ww = 16;
		if (ww > dn / 2)
			ww = (int) (dn / 2);
		Point triPoints[] = newPointArray(3);
		interpPoint2(lead1, lead2, triPoints[0], triPoints[1], 0, hs + 2);
		triPoints[2] = interpPoint(point1, point2, .5 + (ww - 2) / dn);
		gatePoly = createPolygon(triPoints);

		point3 = interpPoint(point1, point2, .5, -hs);
		point4 = interpPoint(point1, point2, .5, 0);
		lead3 = interpPoint(point1, point2, .5, -hs / 2);
	}

	public void drawPosts(Graphics g) {
		int i;
		for (i = 0; i != 3; i++) {
			Point p = getPost(i);
			drawPost(g, p.x, p.y, nodes[i]);
		}
	}

	public void draw(Graphics g) {
		int hs = 16;
		setBbox(point1, point2, hs);

		draw2Leads(g);

		g.setColor(getLightGrayColor());
		drawThickPolygon(g, gatePoly);
		setVoltageColor(g, volts[2]);
		drawThickLine(g, point3, lead3);

		drawPosts(g);
	}

	void calculateCurrent() {
		current = (volts[0] - volts[1]) / resistance;
	}

	// we need this to be able to change the matrix for each step
	public boolean nonLinear() {
		return true;
	}

	public void stamp() {
		sim.stampVoltageSource(0, nodes[3], voltSource);
		sim.stampNonLinear(nodes[3]);
		sim.stampNonLinear(nodes[1]);
	}

	public void doStep() {
		open = (volts[2] < 2.5);
		resistance = (open) ? r_off : r_on;
		sim.stampResistor(nodes[3], nodes[1], resistance);
		sim.updateVoltageSource(0, nodes[3], voltSource, volts[0] > 2.5 ? 5 : 0);
	}

	public void drag(int xx, int yy) {
		xx = sim.snapGrid(xx);
		yy = sim.snapGrid(yy);
		if (abs(getX() - xx) < abs(getY() - yy))
			xx = getX();
		else
			yy = getY();
		int q1 = abs(getX() - xx) + abs(getY() - yy);
		int q2 = (q1 / 2) % sim.getGridSize();
		if (q2 != 0)
			return;
		setX2(xx);
		setY2(yy);
		setPoints();
	}

	public int getPostCount() {
		return 4;
	}

	public int getVoltageSourceCount() {
		return 1;
	}

	public Point getPost(int n) {
		if (point4 == null)
			System.out.print("Hello\n");
		return (n == 0) ? point1 : (n == 1) ? point2 : (n == 2) ? point3 : point4;
	}

	public void getInfo(String arr[]) {
		arr[0] = "tri-state buffer";
		arr[1] = open ? "open" : "closed";
		arr[2] = "Vd = " + getVoltageDText(getVoltageDiff());
		arr[3] = "I = " + getCurrentDText(getCurrent());
		arr[4] = "Vc = " + getVoltageText(volts[2]);
	}
	// we have to just assume current will flow either way, even though that
	// might cause singular matrix errors

//     0---3----------1
//            /
//           2

	public boolean getConnection(int n1, int n2) {
		if ((n1 == 1 && n2 == 3) || (n1 == 3 && n2 == 1))
			return true;
		return false;
	}

	public EditInfo getEditInfo(int n) {

		if (n == 0)
			return new EditInfo("On Resistance (ohms)", r_on, 0, 0);
		if (n == 1)
			return new EditInfo("Off Resistance (ohms)", r_off, 0, 0);
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {

		if (n == 0 && ei.getValue() > 0)
			r_on = ei.getValue();
		if (n == 1 && ei.getValue() > 0)
			r_off = ei.getValue();
	}
}
