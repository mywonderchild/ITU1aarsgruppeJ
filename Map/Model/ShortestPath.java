package Map.Model;

import java.lang.Iterable;
import java.util.List;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Collections;
import java.lang.IllegalArgumentException;

import Map.Controller.Path;

public class ShortestPath {
	private double[] dist;
	private Edge[] prev;

	@SuppressWarnings("unchecked")
	public ShortestPath(Graph g, int src) {
		PriorityQueue<Double, Integer> pq = new PriorityQueue<>();
		boolean[] done = new boolean[g.countNodes()];
		dist = new double[g.countNodes()];
		prev = new Edge[g.countNodes()];

		for(int i = 0; i < g.countNodes(); i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}
		dist[src] = 0;
		pq.push(dist[src], src);

		while(!pq.isEmpty()) {
			Entry entry = pq.pop();
			int curr = (int) entry.getValue();
			Iterable<Edge> adj = g.getAdj(curr);

			for(Edge edge : adj) {
				int node = edge.END.ID;
				if(done[node]) continue;

				double alt = dist[curr] + edge.TIME;
				if(alt < dist[node]) {
					prev[node] = edge;
					dist[node] = alt;
					pq.push(alt, node);
				}
			}
			done[curr] = true;
		}
	}

	public boolean hasPathTo(int node) {
		if(node > dist.length-1) throw new IllegalArgumentException("Node " + node + " does not exist in graph");
		return dist[node] < Double.POSITIVE_INFINITY;
	}

	public Path pathTo(int node) {
		if(node > dist.length-1) throw new IllegalArgumentException("Node " + node + " does not exist in graph");
		if(!hasPathTo(node)) return null;

		LinkedList<Edge> edges = new LinkedList<>();
		Edge edge;
		while((edge = prev[node]) != null) {
			edges.addFirst(edge);
			node = edge.START.ID;
		}

		return new Path(edges);
	}
}