package Map.Model;

import java.lang.RuntimeException;

import Map.Vector;
import Map.Controller.Groups;

public class Edge {
	public final int FNODE, TNODE, TYP;
	public final String VEJNAVN;
	private Node start, stop;

	public Edge(String line) {
		DataLine dl = new DataLine(line);
		FNODE = dl.getInt();
		TNODE = dl.getInt();
		TYP = dl.getInt();
		VEJNAVN = dl.getString();
	}

	public void setNodes(Node start, Node stop) {
		this.start = start;
		this.stop = stop;
	}

	public Vector[] getVectors() {
		return new Vector[] {start.vector.copy(), stop.vector.copy()};
	}

	public Vector getCenter()
	{
		return start.vector
			.copy()
			.add(stop.vector)
			.div(2);
	}

	public int getType() {
		return TYP;
	}

	public String getName() {
		return VEJNAVN;
	}

	public int getGroup() throws RuntimeException {

		// Determine road group
		for (int i = 0; i < Groups.GROUPS.length; i++) {
			for (int id : Groups.GROUPS[i]) {
				if (TYP == id) {
					return i;
				}
			}
		}

		throw new RuntimeException("Road group not found, type is: " + TYP);
	}

	public String toString() {
		return String.format(
			"%d, %d, %d, '%s'",
			FNODE, TNODE, TYP, VEJNAVN
		);
	}
}