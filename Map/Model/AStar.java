package Map.Model;

import java.util.List;
import java.util.LinkedList;

import Map.Controller.Path;

public class AStar {
	private final Graph g;

	private boolean[] closed;
	private boolean[] open;
	private Edge[] came_from;
	private double[] g_score;
	private double[] f_score;

	private int closedC, calcC;

	public AStar(Graph g) {
		this.g = g;
	}

	public Path findPath(Node from, Node to) {
		// Ready arrays for use
		ready();

		// Open set - key is f_score, val is node
		PriorityQueue<Double, Node> openQueue = new PriorityQueue<>();

		g_score[from.ID] = 0;
		f_score[from.ID] = heuristic(from, to);
		openQueue.push(f_score[from.ID], from);
		open[from.ID] = true;

		while(!openQueue.isEmpty()) {
			// Node with lowest f_score:
			Node current = (Node) openQueue.pop().getValue();

			if(current.equals(to))
				return compile(to); // destination reached

			closed[current.ID] = true; closedC++;
			open[current.ID] = false;
			for(Edge edge : g.getAdj(current)) {
				Node adj = edge.END;

				if(closed[adj.ID])
					continue; // neighbor is in closed set

				double t_score = g_score[current.ID] + edge.TIME;
				if(!open[adj.ID] || t_score < g_score[adj.ID]) {
					came_from[adj.ID] = edge;
					g_score[adj.ID] = t_score;
					f_score[adj.ID] = t_score + heuristic(adj, to);

					if(!open[adj.ID]) {
						openQueue.push(f_score[adj.ID], adj);
						open[adj.ID] = true;
					}
					calcC++;
				}
			}
		}

		return null;
	}

	private Path compile(Node to) {
		// DEBUG //
		System.out.printf("%.2f%% of graph used\n", (double)closedC / g.countNodes() * 100.0);
		System.out.printf("%.2f%% recalculation\n", (double)(calcC - closedC) / closedC * 100.0);
		// DEBUG DONE //

		LinkedList<Edge> edges = new LinkedList<Edge>();
		Edge edge;
		while((edge = came_from[to.ID]) != null) {
			edges.addFirst(edge);
			to = edge.START;
		}
		return new Path(edges);
	}

	private void ready() {
		// Create possibly large arrays on first use
		if(closed == null) {
			closed = new boolean[g.countNodes()];
			open = new boolean[g.countNodes()];
			came_from = new Edge[g.countNodes()];
			g_score = new double[g.countNodes()];
			f_score = new double[g.countNodes()];
		}
		else {
			// Clear closed set.
			// It is not necessary for
			// g_score and f_score to be cleared.
			for(int i = 0; i < closed.length; i++) {
				closed[i] = false;
				open[i] = false;
				came_from[i] = null;
			}
		}

		calcC = 0;
		closedC = 0;
	}

	private static double heuristic(Node from, Node to) {
		// dist[meters] / 1000m / 130 kph * 60 min
		return from.VECTOR.dist(to.VECTOR) / 1000 / 130 * 60;
	}
}