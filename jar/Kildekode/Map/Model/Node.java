package Map.Model;

import Map.Vector;

public class Node {
	public final int ID;
	public final Vector VECTOR;

	public Node(Vector vector) {
		VECTOR = vector;
		// Following assignments needed for compile
		ID = -1;
	}

	public Node(Vector vector, int id) {
		VECTOR = vector;
		ID = id;
	}
}