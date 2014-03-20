package Map.Model;

import java.lang.RuntimeException;
import java.util.StringTokenizer;

import Map.Vector;
import Map.Controller.Groups;

public class Edge {
	private final Node START, STOP;
	public final String NAME;
	public final int TYPE;

	public Edge(Node start, Node stop, String name, int type) {
		START = start;
		STOP = stop;
		NAME = name;
		TYPE = type;
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