package Map.Model;

import java.lang.Iterable;
import java.util.ArrayList;

public class Graph {
	private final ArrayList<Edge>[] adj;
	private int edgeCount;

	@SuppressWarnings("unchecked")
	public Graph(int nodes) {
		adj = (ArrayList<Edge>[]) new ArrayList[nodes];
		for(int i = 0; i < adj.length; i++) {
			adj[i] = new ArrayList<Edge>(); 
		}
	}

	public void addEdge(Edge edge) {
		adj[edge.START.ID].add(edge);
		edgeCount++;
	}

	public void addEdges(Edge[] edges) {
		for(Edge e : edges) {
			addEdge(e);
		}
	}

	public Iterable<Edge> getAdj(int node) {
		return adj[node];
	}

	public Iterable<Edge> getAdj(Node node) {
		return getAdj(node.ID);
	}

	public int countNodes() {
		return adj.length;
	}

	public int countEdges() {
		return edgeCount;
	}
}