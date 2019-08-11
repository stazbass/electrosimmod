package base;
import java.util.Vector;

public class CircuitNode {
	int x, y;
	private Vector<CircuitNodeLink> links;
	boolean internal;

	CircuitNode() {
		setLinks(new Vector<CircuitNodeLink>());
	}

	public Vector<CircuitNodeLink> getLinks() {
		return links;
	}

	public void setLinks(Vector<CircuitNodeLink> links) {
		this.links = links;
	}
}
