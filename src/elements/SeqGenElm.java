package elements;
import java.awt.*;
import java.util.StringTokenizer;

import base.EditInfo;

// contributed by Edward Calver

public class SeqGenElm extends ChipElm {
	boolean hasReset() {
		return false;
	}

	public SeqGenElm(int xx, int yy) {
		super(xx, yy);
	}

	public SeqGenElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f, st);
		data = (short) (new Integer(st.nextToken()).intValue());
		if (st.hasMoreTokens()) {
			oneshot = new Boolean(st.nextToken()).booleanValue();
			position = 8;
		}
	}

	short data = 0;
	byte position = 0;
	boolean oneshot = false;
	double lastchangetime = 0;
	boolean clockstate = false;

	String getChipName() {
		return "Sequence generator";
	}

	void setupPins() {
		sizeX = 2;
		sizeY = 2;
		pins = new Pin[getPostCount()];

		pins[0] = new Pin(0, SIDE_W, "");
		pins[0].clock = true;
		pins[1] = new Pin(1, SIDE_E, "Q");
		pins[1].output = true;
	}

	public int getPostCount() {
		return 2;
	}

	public int getVoltageSourceCount() {
		return 1;
	}

	void GetNextBit() {
		if (((data >>> position) & 1) != 0)
			pins[1].value = true;
		else
			pins[1].value = false;
		position++;
	}

	void execute() {
		if (oneshot) {
			if (sim.getT() - lastchangetime > 0.005) {
				if (position <= 8)
					GetNextBit();
				lastchangetime = sim.getT();
			}
		}
		if (pins[0].value && !clockstate) {
			clockstate = true;
			if (oneshot) {
				position = 0;
			} else {
				GetNextBit();
				if (position >= 8)
					position = 0;
			}
		}
		if (!pins[0].value)
			clockstate = false;

	}

	public int getDumpType() {
		return 188;
	}

	public String dump() {
		return super.dump() + " " + data + " " + oneshot;
	}

	public EditInfo getEditInfo(int n) {
		// My code
		if (n == 0) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 0 set", (data & 1) != 0));
			return ei;
		}

		if (n == 1) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 1 set", (data & 2) != 0));
			return ei;
		}
		if (n == 2) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 2 set", (data & 4) != 0));
			return ei;
		}
		if (n == 3) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 3 set", (data & 8) != 0));
			return ei;
		}

		if (n == 4) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 4 set", (data & 16) != 0));
			return ei;
		}
		if (n == 5) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 5 set", (data & 32) != 0));
			return ei;
		}

		if (n == 6) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 6 set", (data & 64) != 0));
			return ei;
		}

		if (n == 7) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("Bit 7 set", (data & 128) != 0));
			return ei;
		}
		if (n == 8) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.setCheckbox(new Checkbox("One shot", oneshot));
			return ei;
		}
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0) {
			if (ei.getCheckbox().getState())
				data |= 1;
			else
				data &= ~1;
			setPoints();
		}
		if (n == 1) {
			if (ei.getCheckbox().getState())
				data |= 2;
			else
				data &= ~2;
			setPoints();
		}
		if (n == 2) {
			if (ei.getCheckbox().getState())
				data |= 4;
			else
				data &= ~4;
			setPoints();
		}
		if (n == 3) {
			if (ei.getCheckbox().getState())
				data |= 8;
			else
				data &= ~8;
			setPoints();
		}
		if (n == 4) {
			if (ei.getCheckbox().getState())
				data |= 16;
			else
				data &= ~16;
			setPoints();
		}
		if (n == 5) {
			if (ei.getCheckbox().getState())
				data |= 32;
			else
				data &= ~32;
			setPoints();
		}
		if (n == 6) {
			if (ei.getCheckbox().getState())
				data |= 64;
			else
				data &= ~64;
			setPoints();
		}
		if (n == 7) {
			if (ei.getCheckbox().getState())
				data |= 128;
			else
				data &= ~128;
			setPoints();
		}
		if (n == 8) {
			if (ei.getCheckbox().getState()) {
				oneshot = true;
				position = 8;
			} else {
				position = 0;
				oneshot = false;
			}
		}

	}

}
