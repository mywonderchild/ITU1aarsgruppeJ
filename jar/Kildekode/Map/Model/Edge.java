package Map.Model;

import java.lang.RuntimeException;
import java.util.StringTokenizer;

import Map.Vector;
import Map.Model.Groups;

public class Edge {
	public final int ID;
	public final Node START, END;
	public final double LENGTH;
	public final byte TYPE;
	public final String NAME;
	public final int ZIP;
	public final int SPEED;

	public final double TIME;

	public Edge(int id, Node start, Node end, double length, byte type, String name, int zip, int speed) {
		ID = id;
		START = start;
		END = end;
		LENGTH = length;
		TYPE = type;
		NAME = name;
		ZIP = zip;
		SPEED = speed;

		TIME = LENGTH/1000 / SPEED * 1.15; // 15% extra, as Krak does.
	}

	public Vector[] getVectors() {
		return new Vector[] {START.VECTOR.copy(), END.VECTOR.copy()};
	}

	@Override
	public int hashCode() {
		return ID;
	}
}