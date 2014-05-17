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

	private String nodePath, edgePath, cityPath;
	private Vector max;
	private int edgeID = 0;

	public ArrayList<Node> nodes;
	public QuadTree all;
	public QuadTree[] groups;
	public Graph graph;
	public AddressFinder addressFinder;
	public Map<String, List<Edge>> addresses;
	public Map<Integer, String> cities;
	public AStar pathFinder;

	public Loader(String dataSet) {
		System.out.println("JVM OS architecture: " + System.getProperty("os.arch"));
		System.out.println("----------------------------------------");
		long timer = System.currentTimeMillis();

		try {
			String dir = String.format("Map/Data/%s/", dataSet);
			nodePath = dir + "purged_nodes.txt";
			edgePath = dir + "purged_edges.txt";
			cityPath = dir + "zip.txt";
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
		nodes = new ArrayList<Node>();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(nodePath), "UTF8"));
		String[] tokens = br.readLine().split(",");
		max = new Vector(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
		while((line = br.readLine()) != null) processNode(line);
		br.close();
		System.out.println("done in " + (System.currentTimeMillis()-timer) + "ms.");

		// Create QuadTrees, Graph and maps
		Box quadBox = new Box(new Vector(0, 0), max);
		all = new QuadTree(quadBox);
		groups = new QuadTree[Groups.GROUPS.length];
		for(int i = 0; i < groups.length; i++) groups[i] = new QuadTree(quadBox);
		graph = new Graph(nodes.size());
		addresses = new HashMap<String, List<Edge>>();
		cities = new HashMap<Integer, String>();

		// Cities
		timer = System.currentTimeMillis();
		System.out.print("Loading cities... ");
		br = new BufferedReader(new InputStreamReader(new FileInputStream(cityPath), "UTF8"));
		while((line = br.readLine()) != null) processCity(line);
		br.close();
		System.out.println("done in " + (System.currentTimeMillis()-timer) + "ms.");

		// Edges
		timer = System.currentTimeMillis();
		System.out.print("Loading edges... ");
		br = new BufferedReader(new InputStreamReader(new FileInputStream(edgePath), "UTF8"));
		while((line = br.readLine()) != null) processEdge(line);
		br.close();
		System.out.println("done in " + (System.currentTimeMillis()-timer) + "ms.");

		// AddressFinder
		addressFinder = new AddressFinder(addresses);

		// AStar pathfinder
		pathFinder = new AStar(graph);

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
		
		nodes.add(node);
	}

	private void processCity(String line) {
		matcher = PATTERN.matcher(line);

		int zip = readInt();
		String city = readString();
		cities.put(zip, city);
	}

	private void processEdge(String line) {
		matcher = PATTERN.matcher(line);

		Node start = nodes.get(readInt()-1);
		Node end = nodes.get(readInt()-1);
		double length = readDouble();
		byte type = readByte();
		String name = readString();
		int zip = readInt();
		int speed = readInt();
		Edge edge = new Edge(edgeID++, start, end, length, type, name, zip, speed);

		groups[Groups.getGroup(edge.TYPE)].insert(edge);
		
		if (type == 81) return;

		all.insert(edge);
		Edge invertedEdge = new Edge(-1, end, start, length, type, name, zip, speed);
		graph.addEdge(edge);
		graph.addEdge(invertedEdge);
		if(name != null) {
			String suffix = zip > 0 ? ", " + zip : "";
			String city = cities.get(zip);
			if(zip > 0 && city != null) suffix += " " + city;
			
			String address = name + suffix; // add zip and city to name, if edge has real zip
			List<Edge> edges = addresses.get(address);
			if(edges != null) {
				// already there; just add the edge to list
				edges.add(edge);
			}
			else {
				// not there; create new list with edge
				edges = new ArrayList<Edge>();
				edges.add(edge);
				addresses.put(address, edges);
			}
		}
	}

	private int readInt() {
		matcher.find();
		return Integer.parseInt(matcher.group());
	}

	private byte readByte() {
		matcher.find();
		return Byte.parseByte(matcher.group());
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