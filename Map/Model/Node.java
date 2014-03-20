package Map.Model;

import Map.Vector;

public class Node {
	final int KDV_ID;
	final double X_COORD, Y_COORD;
	public final Vector vector;

	public Node(String line) {
		DataLine dl = new DataLine(line);
		KDV_ID = dl.getInt();
		X_COORD = dl.getDouble();
		Y_COORD = dl.getDouble();
		vector = new Vector(X_COORD, Y_COORD);
	}

	public String toString() {
		return KDV_ID + "," + X_COORD + "," + Y_COORD;
	}

	public int getID() {
		return KDV_ID;
	}
}