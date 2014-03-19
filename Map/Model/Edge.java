package Map.Model;

import java.lang.RuntimeException;

import Map.Vector;
import Map.Controller.Groups;

public class Edge {
	private final EdgeData edge;
	private final Node start, stop;

	public Edge(EdgeData edge, Node start, Node stop)
	{
		this.edge = edge;
		this.start = start;
		this.stop = stop;
	}

	public String toString()
	{
		return edge.toString();
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
		return edge.TYP;
	}

	public String getName() {
		return edge.VEJNAVN;
	}

	public int getGroup() throws RuntimeException {

		int type = this.getType();

		// Determine road group
		for (int i = 0; i < Groups.GROUPS.length; i++) {
			for (int id : Groups.GROUPS[i]) {
				if (type == id) {
					return i;
				}
			}
		}

		throw new RuntimeException("Road group not found, type is: " + type);
	}
}