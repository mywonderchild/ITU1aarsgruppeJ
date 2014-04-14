package Map.Model;

import java.lang.RuntimeException;
import java.util.StringTokenizer;

import Map.Vector;
import Map.Model.Groups;

public class Edge {
	public final Node START, STOP;
	public final String NAME;
	public final int TYPE;
	public final double LENGTH;

	public Edge(Node start, Node stop, double length, String name, int type) {
		START = start;
		STOP = stop;
		NAME = name;
		TYPE = type;
		LENGTH = length;
	}

	public Vector[] getVectors() {
		return new Vector[] {START.VECTOR.copy(), STOP.VECTOR.copy()};
	}

	public Vector getCenter()
	{
		return START.VECTOR
			.copy()
			.add(STOP.VECTOR)
			.div(2);
	}
}