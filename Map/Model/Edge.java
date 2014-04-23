package Map.Model;

import java.lang.RuntimeException;
import java.util.StringTokenizer;

import Map.Vector;
import Map.Model.Groups;

public class Edge {
	public final Node START, END;
	public final double LENGTH;
	public final int TYPE;
	public final String NAME;
	public final int SPEED;

	public final double TIME;
	public final Vector CENTER;


	public Edge(Node start, Node end, double length, int type, String name, int speed) {
		START = start;
		END = end;
		LENGTH = length;
		TYPE = type;
		NAME = name;
		SPEED = speed;

		TIME = LENGTH/1000 / SPEED;
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