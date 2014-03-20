package Map.Model;

import Map.Vector;

public class Node {
	public final int KDV_ID;
	public final Vector VECTOR;

	public Node(int id, Vector vector) {
		KDV_ID = id;
		VECTOR = vector;
	}
}