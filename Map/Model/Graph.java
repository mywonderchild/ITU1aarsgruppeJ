package Map.Model;

import java.util.List;
import java.util.ArrayList;

public class Graph {
	private List<Edge>[] adj;

	@SuppressWarnings("unchecked")
	public Graph(int nodes) {
		adj = (ArrayList<Edge>[]) new ArrayList[nodes];
		for(int i = 0; i < adj.length; i++) {
			adj[i] = new ArrayList<Edge>(); 
		}
	}

	public void addEdge(Edge edge) {
		adj[edge.START.ID].add(edge);
	}

	public void addEdges(Edge[] edges) {
		for(Edge e : edges) {
			addEdge(e);
		}
	}

	public List getAdj(int node) {
		return adj[node];
	}

	public int size() {
		return adj.length;
	}
}