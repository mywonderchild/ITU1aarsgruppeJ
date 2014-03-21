package Map.Model;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import Map.Vector;
import Map.Box;
import Map.Controller.Groups;

public class Loader {

	public final Node[] nodes;
	public final QuadTree all;
	public final QuadTree[] groups;
	StringTokenizer tokenizer;

	public Loader() {
		
		nodes = new Node[700000];

		Vector start = new Vector(442254.35659, 6049914.43018);
		Vector stop = new Vector(892658.21706, 6402050.98297);
		Box box = new Box(start, stop);

		all = new QuadTree(box);
		groups = new QuadTree[Groups.GROUPS.length];
		for(int i = 0; i < groups.length; i++)
			groups[i] = new QuadTree(box);

		try {
			String dir = new File(".").getCanonicalPath() + "/Map/data/";
			load(dir + "purged_kdv_node_unload.txt", dir + "purged_kdv_unload.txt");
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void load(String nodeFile, String edgeFile) throws IOException {

		BufferedReader br;
		String line;

		// Nodes
		br = new BufferedReader(new FileReader(nodeFile));
		br.readLine(); // First line is column names, not data

		while((line = br.readLine()) != null)
			processNode(line);
		br.close();

		// Edges
		br = new BufferedReader(new FileReader(edgeFile));
		br.readLine(); // First line is column names, not data

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

		Node node = new Node(id, vector);
		nodes[node.KDV_ID] = node;
	}

	public void processEdge(String line) {

		tokenizer = new StringTokenizer(line, ",");
		Node start = nodes[readInt()];
		Node stop = nodes[readInt()];
		int type = readInt();
		String name = readName();

		Edge edge = new Edge(start, stop, name, type);
		all.insert(edge);
		groups[Groups.getGroup(edge.TYPE)].insert(edge);
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