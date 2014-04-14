package Map.Model;

import java.util.TreeMap;
import java.util.Stack;

public class PathFinder {
	private double[] distTo;
	private Edge[] edgeTo;
	private TreeMap<Integer, Double> ts;
	
	public PathFinder(Graph graph, int src) {
		for(Edge e : graph.edges()) {
			if(e.LENGTH < 0) {
				throw new IllegalArgumentException("Edge " + e.toString() + " has negative length.");
			}
		}

		distTo = new double[graph.V()];
		edgeTo = new Edge[graph.V()];
		for(int i = 0; i < graph.V(); i++) {
			distTo[i] = -1;
		}
		distTo[src] = 0.0;

		ts = new TreeMap<Integer, Double>();
		ts.put(src, distTo[src]);
		while(!ts.isEmpty()) {
			int node = ts.pollFirstEntry().getKey();
			for(Edge e : graph.adj(node)) {
				relax(e);
			}
		}
	}

	public double distTo(int v) {
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
		return distTo[v] != -1;
	}

	public Iterable<Edge> pathTo(int v) {
		if(hasPathTo(v)) return null;
		Stack<Edge> path = new Stack<Edge>();
		for(Edge e = edgeTo[v]; e != null; e = edgeTo[e.START.KDV_ID]) {
			path.push(e);
		}
		return path;
	}

	private void relax(Edge e) {
		int v = e.START.KDV_ID, w = e.STOP.KDV_ID;
		if (distTo[w] > distTo[v] + e.LENGTH) {
			distTo[w] = distTo[v] + e.LENGTH;
			edgeTo[w] = e;
			if (ts.containsKey(w)) {
				ts.remove(w);
				ts.put(w, distTo[w]);
			}
			else {
				ts.put(w, distTo[w]);
			}
		}
	}
}