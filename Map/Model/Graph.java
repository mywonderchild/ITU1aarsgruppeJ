package Map.Model;

import java.util.List;
import java.util.ArrayList;

public class Graph {
	private final List<Edge>[] adj;
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

	public List<Edge> getAdj(int node) {
		return adj[node];
	}

	public int countNodes() {
		return adj.length;
	}

	public int countEdges() {
		return edgeCount;
	}

	public ArrayList<Edge> BFS(int start) {
		PriorityQueue<Integer, Integer> pq = new PriorityQueue<Integer, Integer>();
		boolean[] done = new boolean[countNodes()];
		ArrayList<Edge> edges = new ArrayList<Edge>();
		pq.push(start, null);
		done[start] = true;

		while(!pq.isEmpty()) {
			int node = (int) pq.pop().getKey();
			for(Edge e : getAdj(node)) {
				if(!done[e.END.ID]) {
					edges.add(e);
					done[e.END.ID] = true;
					pq.push(e.END.ID, null);
				}
			}
		}

		return edges;
	}
}