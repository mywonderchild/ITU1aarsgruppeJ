package Map.Model;

import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;
import java.lang.IllegalArgumentException;

public class ShortestPath {
	private double[] dist;
	private Edge[] prev;

	@SuppressWarnings("unchecked")
	public ShortestPath(Graph g, int src) {
		PriorityQueue<Double, Integer> pq = new PriorityQueue<>();
		boolean[] done = new boolean[g.size()];
		dist = new double[g.size()];
		prev = new Edge[g.size()];

		dist[src] = 0;
		for(int i = 0; i < g.size(); i++) {
			if(i != src) {
				dist[i] = Double.POSITIVE_INFINITY;
				prev[i] = null;
			}
			pq.push(dist[i], i);
		}

		while(!pq.isEmpty()) {
			Entry entry = pq.pop();
			int curr = (int) entry.getValue();
			List<Edge> adj = g.getAdj(curr);

			for(Edge edge : adj) {
				int node = edge.END.ID;
				if(done[node]) continue;

				double alt = dist[curr] + edge.LENGTH;
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

	public List<Edge> pathTo(int node) {
		if(node > dist.length-1) throw new IllegalArgumentException("Node " + node + " does not exist in graph");
		if(!hasPathTo(node)) return null;

		Stack<Edge> path = new Stack<Edge>();
		Edge edge;
		while((edge = prev[node]) != null) {
			path.push(edge);
			node = edge.START.ID;
		}

		return path;
	}
}