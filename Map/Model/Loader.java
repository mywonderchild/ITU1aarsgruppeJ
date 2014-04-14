package Map.Model;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import Map.Vector;
import Map.Box;
import Map.Model.Groups;

public class Loader {

	private String nodePath, edgePath, coastPath;
	public final HashMap<Integer, Node> nodes;
	public QuadTree all;
	public QuadTree[] groups;
	StringTokenizer tokenizer;
	Vector min, max;
	Box quadBox, dataBox;

	public Loader() {
		
		nodes = new HashMap<>();

		try {
			String dir = "Map/Data/";
			nodePath = dir + "purged_nodes.txt";
			edgePath = dir + "purged_edges.txt";
			coastPath = dir + "coastline.txt";
			load();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void load() throws IOException {

		BufferedReader br;
		String line;

		// Nodes
		br = new BufferedReader(new InputStreamReader(new FileInputStream(nodePath), "UTF8"));
		while((line = br.readLine()) != null)
			processNode(line);
		br.close();

		// Create QuadTrees
		quadBox = new Box(
			new Vector(0, 0),
			(new Vector(1000, 1000)).mult(dataBox.ratio())
		);
		all = new QuadTree(quadBox);
		groups = new QuadTree[Groups.GROUPS.length];
		for(int i = 0; i < groups.length; i++)
			groups[i] = new QuadTree(quadBox);

		// Reset node vectors
		System.out.println(dataBox);
		for (Node node : nodes.values())
			resetVector(node.VECTOR);

		// Coastline
		br = new BufferedReader(new InputStreamReader(new FileInputStream(coastPath), "UTF8"));
		while((line = br.readLine()) != null)
			processCoast(line);
		br.close();

		// Edges
		br = new BufferedReader(new InputStreamReader(new FileInputStream(edgePath), "UTF8"));
		while((line = br.readLine()) != null)
			processEdge(line);
		br.close();

		// Garbage collect
		System.gc();
	}

	public void processNode(String line) {

		tokenizer = new StringTokenizer(line, ",");

		int id = readInt();
		Vector vector = new Vector(readDouble(), readDouble());
		Node node = new Node(vector, id);
		
		nodes.put(node.KDV_ID, node);

		if (dataBox == null) {
			dataBox = new Box(vector.copy(), vector.copy());
		} else {
			dataBox.start.x = Math.min(vector.x, dataBox.start.x);
			dataBox.start.y = Math.min(vector.y, dataBox.start.y);
			dataBox.stop.x = Math.max(vector.x, dataBox.stop.x);
			dataBox.stop.y = Math.max(vector.y, dataBox.stop.y);
		}
	}

	public void processEdge(String line) {

		tokenizer = new StringTokenizer(line, ",");

		Node start = nodes.get(readInt());
		Node stop = nodes.get(readInt());
		double length = readDouble();
		int type = readInt();
		String name = readName();
		Edge edge = new Edge(start, stop, length, name, type);

		all.insert(edge);
		groups[Groups.getGroup(edge.TYPE)].insert(edge);
	}

	public void processCoast(String line) {

		tokenizer = new StringTokenizer(line, ",");

		Node start = new Node(resetVector(new Vector(readDouble(), readDouble())));
		Node stop = new Node(resetVector(new Vector(readDouble(), readDouble())));
		Edge edge = new Edge(start, stop, 0, null, 81);

		all.insert(edge);
		groups[Groups.getGroup(edge.TYPE)].insert(edge);
	}

	public Vector resetVector(Vector vector) {
		return vector
			.sub(dataBox.start)
			.mirrorY(dataBox)
			.translate(dataBox, quadBox);
	}

	private int readInt() {
		return Integer.parseInt(tokenizer.nextToken());
	}

	private double readDouble() {
		return Double.parseDouble(tokenizer.nextToken());
	}

	private String readName() {
		tokenizer.nextToken("'"); // Switch delimiter, first token is ","
		String name = "";
		if (tokenizer.hasMoreTokens()) name = tokenizer.nextToken();
		tokenizer.nextToken(","); // Switch delimiter, first token is "'"
		if (name.trim().length() > 0)
			return name;
		else
			return null;
	}
}