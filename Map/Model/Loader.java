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

	public final Node[] ndata;
	public final QuadTree all;
	public final QuadTree[] groups;

	public Loader() {
		
		ndata = new Node[700000];

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

		StringTokenizer tokenizer = new StringTokenizer(line, ",");
		int id = Integer.parseInt(tokenizer.nextToken());
		Vector vector = new Vector(
			Double.parseDouble(tokenizer.nextToken()),
			Double.parseDouble(tokenizer.nextToken())
		);

		Node node = new Node(id, vector);
		ndata[node.KDV_ID] = node;
	}

	public void processEdge(String line) {

		StringTokenizer tokenizer = new StringTokenizer(line, ",");
		Node start = ndata[Integer.parseInt(tokenizer.nextToken())];
		Node stop = ndata[Integer.parseInt(tokenizer.nextToken())];
		int type = Integer.parseInt(tokenizer.nextToken());
		String name = tokenizer.nextToken();
		name = name.substring(1, name.length() - 1);

		Edge edge = new Edge(start, stop, name, type);
		all.insert(edge);
		groups[Groups.getGroup(edge.TYPE)].insert(edge);
	}
}