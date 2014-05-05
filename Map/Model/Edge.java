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

	public Edge(Node start, Node end, double length, int type, String name, int zip, int speed) {
		START = start;
		END = end;
		LENGTH = length;
		TYPE = type;
		NAME = name + (zip > 0 ? ", " + zip : ""); // add zip to name, if edge has real zip
		SPEED = speed;

		TIME = LENGTH/1000 / SPEED * 1.15; // 15% extra, as Krak does.
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