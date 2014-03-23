package Map.Model;

import Map.Vector;

public class Node {
	public final int KDV_ID;
	public final Vector VECTOR;

	public Node(Vector vector) {
		VECTOR = vector;
		// Following assignments needed for compile
		KDV_ID = -1;
	}

	public Node(Vector vector, int id) {
		VECTOR = vector;
		KDV_ID = id;
	}
}