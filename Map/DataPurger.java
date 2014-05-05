package Map;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

import Map.Vector;
import Map.Box;
import Map.Model.Node;
import Map.Model.Edge;

public class DataPurger {

	static BufferedReader nodesIn, edgesIn, coastlineIn;
	static PrintWriter nodesOut, edgesOut;
	static ArrayList<Node> nodes = new ArrayList<>();
	static int nodeId;

	static public void purgeNodes(int idKey, int xKey, int yKey) throws IOException {

		// Load nodes from data
		String line = null;
		while ((line = nodesIn.readLine()) != null) {
			String[] tokens = line.split(",");
			int id = Integer.parseInt(tokens[idKey]);
			double x = Double.parseDouble(tokens[xKey]);
			double y = Double.parseDouble(tokens[yKey]);
			Vector vector = new Vector(x, y);
			nodes.add(new Node(vector, id));
		}

		// Set nodeId for later use
		nodeId = nodes.get(nodes.size() - 1).ID;
	}

	static public void purgeEdges(int[] keys) throws IOException {

		// Purge and output
		String line = null;
		while ((line = edgesIn.readLine()) != null) {
			String[] tokens = line.split(",");
			for (int key : keys) {
				edgesOut.print(tokens[key]);
				if (key != keys[keys.length -1]) edgesOut.print(",");
			}
			edgesOut.println();
		}
	}

	static public void purgeCoastline() throws IOException {

		// Purge and output
		String line = null;
		Vector previous = null;
		double[] doubles = new double[4];
		while ((line = coastlineIn.readLine()) != null) {
			String[] tokens = line.split(",");
			for (int i = 0; i < 4; i++) doubles[i] = Double.parseDouble(tokens[i]);
			Vector start = new Vector(doubles[0], doubles[1]);
			Vector stop = new Vector(doubles[2], doubles[3]);
			if (previous == null || !previous.equals(start)) nodes.add(new Node(start, ++nodeId));
			nodes.add(new Node(stop, ++nodeId));
			previous = stop;
			edgesOut.printf("%d,%d,%d,%d,'',%d,%d\n", nodeId - 1, nodeId, 0, 81, 0, 0);
		}
	}

	static public void resetAndOutputNodes() {

		// Find databox size
		Box dataBox = null;
		for (Node node : nodes) {
			Vector vector = node.VECTOR;
			if (dataBox == null) {
				dataBox = new Box(vector.copy(), vector.copy());
			} else {
				dataBox.start.x = Math.min(vector.x, dataBox.start.x);
				dataBox.start.y = Math.min(vector.y, dataBox.start.y);
				dataBox.stop.x = Math.max(vector.x, dataBox.stop.x);
				dataBox.stop.y = Math.max(vector.y, dataBox.stop.y);
			}
		}

		// Create destinationbox according to databox scale
		Vector stop = (new Vector(1000, 1000)).mult(dataBox.ratio());
		nodesOut.println(String.format(Locale.ENGLISH, "%.4f,%.4f", stop.x, stop.y)); // Jot down max values for loader
		Box destinationBox = new Box(new Vector(0, 0), stop);

		// Reset and output nodes
		for (Node node : nodes) {
			node.VECTOR
				.sub(dataBox.start)
				.mirrorY(dataBox)
				.translate(dataBox, destinationBox);
			String format = "%d,%.4f,%.4f";
			nodesOut.println(String.format(Locale.ENGLISH, format, node.ID, node.VECTOR.x, node.VECTOR.y));
		}
	}

	public static void main(String[] args) throws IOException {

		nodesIn = new BufferedReader(new FileReader(new File("Map/Data/kdv_node_unload.txt")));
		edgesIn = new BufferedReader(new FileReader(new File("Map/Data/kdv_unload.txt")));
		coastlineIn = new BufferedReader(new FileReader(new File("Map/Data/coastline.txt")));

		nodesIn.readLine(); // First line is useless tags
		edgesIn.readLine();	// First line is useless tags

		nodesOut = new PrintWriter("Map/Data/purged_nodes.txt", "UTF-8");
		edgesOut = new PrintWriter("Map/Data/purged_edges.txt", "UTF-8");

		DataPurger.purgeNodes(2, 3, 4);
		DataPurger.purgeEdges(new int[] {0, 1, 2, 5, 6, 18, 25});
		DataPurger.purgeCoastline();
		resetAndOutputNodes();

		nodesOut.flush();
		edgesOut.flush();
	}
}