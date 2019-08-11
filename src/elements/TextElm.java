package elements;
import java.awt.*;
import java.util.StringTokenizer;
import java.util.Vector;

import base.EditInfo;

public class TextElm extends GraphicElm {
	String text;
	Vector<String> lines;
	int size;
	final int FLAG_CENTER = 1;
	final int FLAG_BAR = 2;

	public TextElm(int xx, int yy) {
		super(xx, yy);
		text = "hello";
		lines = new Vector<String>();
		lines.add(text);
		size = 24;
	}

	public TextElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		size = new Integer(st.nextToken()).intValue();
		text = st.nextToken();
		while (st.hasMoreTokens())
			text += ' ' + st.nextToken();
		split();
	}

	void split() {
		int i;
		lines = new Vector<String>();
		StringBuffer sb = new StringBuffer(text);
		for (i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c == '\\') {
				sb.deleteCharAt(i);
				c = sb.charAt(i);
				if (c == 'n') {
					lines.add(sb.substring(0, i));
					sb.delete(0, i + 1);
					i = -1;
					continue;
				}
			}
		}
		lines.add(sb.toString());
	}

	public String dump() {
		return super.dump() + " " + size + " " + text;
	}

	public int getDumpType() {
		return 'x';
	}

	public void drag(int xx, int yy) {
		setX(xx);
		setY(yy);
		setX2(xx + 16);
		setY2(yy);
	}

	public void draw(Graphics g) {
		// Graphics2D g2 = (Graphics2D)g;
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(needsHighlight() ? getSelectColor() : getLightGrayColor());
		Font f = new Font("SansSerif", 0, size);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int i;
		int maxw = -1;
		for (i = 0; i != lines.size(); i++) {
			int w = fm.stringWidth((String) (lines.elementAt(i)));
			if (w > maxw)
				maxw = w;
		}
		int cury = getY();
		setBbox(getX(), getY(), getX(), getY());
		for (i = 0; i != lines.size(); i++) {
			String s = (String) (lines.elementAt(i));
			if ((flags & FLAG_CENTER) != 0)
				setX((sim.getWinSize().width - fm.stringWidth(s)) / 2);
			g.drawString(s, getX(), cury);
			if ((flags & FLAG_BAR) != 0) {
				int by = cury - fm.getAscent();
				g.drawLine(getX(), by, getX() + fm.stringWidth(s) - 1, by);
			}
			adjustBbox(getX(), cury - fm.getAscent(), getX() + fm.stringWidth(s), cury + fm.getDescent());
			cury += fm.getHeight();
		}
		setX2(boundingBox.x + boundingBox.width);
		setY2(boundingBox.y + boundingBox.height);
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0) {
			EditInfo ei = new EditInfo("Text", 0, -1, -1);
			ei.setText(text);
			return ei;
		}
		if (n == 1)
			return new EditInfo("Size", size, 5, 100);
		if (n == 2) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Center", (flags & FLAG_CENTER) != 0));
			return ei;
		}
		if (n == 3) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Draw Bar On Top", (flags & FLAG_BAR) != 0));
			return ei;
		}
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0) {
			text = ei.getTextf().getText();
			split();
		}
		if (n == 1)
			size = (int) ei.getValue();
		if (n == 3) {
			if (ei.getCheckbox().getState())
				flags |= FLAG_BAR;
			else
				flags &= ~FLAG_BAR;
		}
		if (n == 2) {
			if (ei.getCheckbox().getState())
				flags |= FLAG_CENTER;
			else
				flags &= ~FLAG_CENTER;
		}
	}

	public boolean isCenteredText() {
		return (flags & FLAG_CENTER) != 0;
	}

	public void getInfo(String arr[]) {
		arr[0] = text;
	}

	@Override
	public int getShortcut() {
		return 't';
	}
}
