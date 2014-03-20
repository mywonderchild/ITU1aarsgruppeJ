package Map.Model;

import java.util.StringTokenizer;

import Map.Vector;

public class Node {
	final int KDV_ID;
	public final Vector vector;

	public Node(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line, ",");
		KDV_ID = Integer.parseInt(tokenizer.nextToken());
		vector = new Vector(
			Double.parseDouble(tokenizer.nextToken()),
			Double.parseDouble(tokenizer.nextToken())
		);
	}

	public String toString() {
		return KDV_ID + "," + vector;
	}

	public int getID() {
		return KDV_ID;
	}
}