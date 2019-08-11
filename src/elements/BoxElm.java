package elements;
import java.awt.*;
import java.util.StringTokenizer;
import java.util.Vector;

import base.EditInfo;

public class BoxElm extends GraphicElm {

	public BoxElm(int xx, int yy) {
		super(xx, yy);
		setX2(xx + 16);
		setY2(yy + 16);
		setBbox(getX(), getY(), getX2(), getY2());
	}

	public BoxElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		setX2(xb);
		setY2(yb);
		/*
		 * if ( st.hasMoreTokens() ) x = new Integer(st.nextToken()).intValue(); if (
		 * st.hasMoreTokens() ) y = new Integer(st.nextToken()).intValue(); if (
		 * st.hasMoreTokens() ) x2 = new Integer(st.nextToken()).intValue(); if (
		 * st.hasMoreTokens() ) y2 = new Integer(st.nextToken()).intValue();
		 */
		setBbox(getX(), getY(), getX2(), getY2());
	}

	public String dump() {
		return super.dump();
	}

	public int getDumpType() {
		return 'b';
	}

	public void drag(int xx, int yy) {
		setX(xx);
		setY(yy);
	}

	public void draw(Graphics g) {
		// g.setColor(needsHighlight() ? selectColor : lightGrayColor);
		g.setColor(needsHighlight() ? getSelectColor() : Color.GRAY);
		setBbox(getX(), getY(), getX2(), getY2());
		if (getX() < getX2() && getY() < getY2())
			g.fillRect(getX(), getY(), getX2() - getX(), getY2() - getY());
		else if (getX() > getX2() && getY() < getY2())
			g.fillRect(getX2(), getY(), getX() - getX2(), getY2() - getY());
		else if (getX() < getX2() && getY() > getY2())
			g.fillRect(getX(), getY2(), getX2() - getX(), getY() - getY2());
		else
			g.fillRect(getX2(), getY2(), getX() - getX2(), getY() - getY2());
	}

	public EditInfo getEditInfo(int n) {
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
	}

	public void getInfo(String arr[]) {
	}

	@Override
	public int getShortcut() {
		return 0;
	}
}
