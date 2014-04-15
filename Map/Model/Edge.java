package Map.Model;

import java.lang.RuntimeException;
import java.util.StringTokenizer;

import Map.Vector;
import Map.Model.Groups;

public class Edge {
	public final Node START, END;
	public final Vector CENTER;
	public final String NAME;
	public final int TYPE;
	public final double LENGTH;

	public Edge(Node start, Node end, double length, String name, int type) {
		START = start;
		END = end;
		NAME = name;
		TYPE = type;
		LENGTH = length;
		CENTER = START.VECTOR
			.copy()
			.add(END.VECTOR)
			.div(2);
	}

	public Vector[] getVectors() {
		return new Vector[] {START.VECTOR.copy(), END.VECTOR.copy()};
	}

	public Vector getCenter() {
		return CENTER;
	}
}