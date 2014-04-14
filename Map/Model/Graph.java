package Map.Model;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class Graph {
	private final int V;
	private int E;
 	private Bag<Edge>[] adj;
 	public int maxDepth = 0;

	@SuppressWarnings("unchecked")
	public Graph(int V) {
		this.V = V;
		adj = (Bag<Edge>[]) new Bag[V];
		for(int i = 0; i < adj.length; i++) {
			adj[i] = new Bag<Edge>();
		}
		System.out.println("Initialized graph[" + V + "]");
	}

	public void addEdge(Edge edge) {
		// Add edge and incr edge counter.
		adj[edge.START.KDV_ID].add(edge);
		E++;
	}

	public Iterable<Edge> adj(int v) {
		return adj[v];
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public int degree(int v) {
		return adj[v].size();
	}

	public Iterable<Edge> edges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(Bag<Edge> bag : adj) {
			for(Edge e : bag) {
				edges.add(e);
			}
		}
		return edges;
	}

	public String toString() {
		String ret = "";
		for(int i = 0; i < adj.length; i++) {
			for(Edge e : adj[i]) {
				ret += i + " --> " + e.STOP.KDV_ID + "\n";
			}
		}
		return ret;
	}
}