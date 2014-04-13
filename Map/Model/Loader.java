package Map.Model;

import java.util.ArrayList;
import java.util.StringTokenizer;
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
	public final Node[] nodes;
	public final QuadTree all;
	public final QuadTree[] groups;
	StringTokenizer tokenizer;
	Vector min, max;
	Box box;

	public Loader() {
		
		nodes = new Node[700000];

		min = new Vector(442254.35659, 6049914.43018);
		max = new Vector(892658.21706, 6402050.98297);
		Vector stop = max.sub(min);
		box = new Box(new Vector(0, 0), stop);

		all = new QuadTree(box);

		groups = new QuadTree[Groups.GROUPS.length];
		for(int i = 0; i < groups.length; i++)
			groups[i] = new QuadTree(box);

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

		// Edges
		br = new BufferedReader(new InputStreamReader(new FileInputStream(edgePath), "UTF8"));

		while((line = br.readLine()) != null)
			processEdge(line);
		br.close();

		// Coastline
		br = new BufferedReader(new InputStreamReader(new FileInputStream(coastPath), "UTF8"));

		while((line = br.readLine()) != null)
			processCoast(line);
		br.close();

		// Garbage collect
		System.gc();
	}

	public void processNode(String line) {

		tokenizer = new StringTokenizer(line, ",");

		int id = readInt();
		Vector vector = new Vector(readDouble(), readDouble());
		vector.sub(min).mirrorY(box);
		Node node = new Node(vector, id);
		
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

	public void processCoast(String line) {

		tokenizer = new StringTokenizer(line, ",");

		Node start = new Node((new Vector(readDouble(), readDouble())).sub(min).mirrorY(box));
		Node stop = new Node((new Vector(readDouble(), readDouble())).sub(min).mirrorY(box));
		Edge edge = new Edge(start, stop, null, 81);

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