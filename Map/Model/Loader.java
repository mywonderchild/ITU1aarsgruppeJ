package Map.Model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import Map.Vector;
import Map.Box;
import Map.Model.Groups;
import Map.Controller.AddressFinder;

public class Loader {
	public static final Pattern PATTERN = Pattern.compile("((?<=').*(?=')|[^',]+)");
	public Matcher matcher;

	private String nodePath, edgePath;
	private Vector max;

	public Map<Integer, Node> nodes;
	public QuadTree all;
	public QuadTree[] groups;
	public Graph graph;
	public AddressFinder addressFinder;
	public Map<String, List<Edge>> addresses;

	public Loader() {
		System.out.println("JVM OS architecture: " + System.getProperty("os.arch"));
		System.out.println("----------------------------------------");
		long timer = System.currentTimeMillis();

		try {
			String dir = "Map/Data/";
			nodePath = dir + "purged_nodes.txt";
			edgePath = dir + "purged_edges.txt";
			load();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("----------------------------------------");
		System.out.println("Program startup took " + (System.currentTimeMillis()-timer) + "ms.");
	}

	private void load() throws IOException {
		BufferedReader br;
		String line;
		long timer;

		// Nodes
		timer = System.currentTimeMillis();
		System.out.print("Loading nodes... ");
		nodes = new HashMap<>();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(nodePath), "UTF8"));
		String[] tokens = br.readLine().split(",");
		max = new Vector(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
		while((line = br.readLine()) != null) processNode(line);
		br.close();
		System.out.println("done in " + (System.currentTimeMillis()-timer) + "ms.");

		// Create QuadTrees and Graph
		Box quadBox = new Box(new Vector(0, 0), max);
		all = new QuadTree(quadBox);
		groups = new QuadTree[Groups.GROUPS.length];
		for(int i = 0; i < groups.length; i++) groups[i] = new QuadTree(quadBox);
		graph = new Graph(nodes.size());
		addresses = new HashMap<String, List<Edge>>();

		// Edges
		timer = System.currentTimeMillis();
		System.out.print("Loading edges... ");
		br = new BufferedReader(new InputStreamReader(new FileInputStream(edgePath), "UTF8"));
		while((line = br.readLine()) != null) processEdge(line);
		br.close();
		System.out.println("done in " + (System.currentTimeMillis()-timer) + "ms.");

		// AddressFinder
		addressFinder = new AddressFinder(addresses);

		// Garbage collect
		timer = System.currentTimeMillis();
		System.out.print("Final garbage collection... ");
		System.gc();
		System.out.println("done in " + (System.currentTimeMillis()-timer) + "ms.");
	}

	private void processNode(String line) {
		matcher = PATTERN.matcher(line);

		int id = readInt();
		Vector vector = new Vector(readDouble(), readDouble());
		Node node = new Node(vector, id-1);
		
		nodes.put(node.ID, node);
	}

	private void processEdge(String line) {
		matcher = PATTERN.matcher(line);

		Node start = nodes.get(readInt()-1);
		Node end = nodes.get(readInt()-1);
		double length = readDouble();
		int type = readInt();
		String name = readString();
		int place = readInt();
		int speed = readInt();
		Edge edge = new Edge(start, end, length, type, name, place, speed);

		groups[Groups.getGroup(edge.TYPE)].insert(edge);
		
		if (type == 81) return;

		all.insert(edge);
		Edge invertedEdge = new Edge(end, start, length, type, name, speed);
		graph.addEdge(edge);
		graph.addEdge(invertedEdge);
		if(name != null) {
			List<Edge> edges = addresses.get(edge.NAME);
			if(edges != null) {
				// already there; just add the edge to list
				edges.add(edge);
			}
			else {
				// not there; create new list with edge
				edges = new ArrayList<Edge>();
				edges.add(edge);
				addresses.put(edge.NAME, edges);
			}
		}
	}

	private int readInt() {
		matcher.find();
		return Integer.parseInt(matcher.group());
	}

	private double readDouble() {
		matcher.find();
		return Double.parseDouble(matcher.group());
	}

	private String readString() {
		matcher.find();
		String name = matcher.group();
		return name.length() == 0 ? null : name;
	}
}