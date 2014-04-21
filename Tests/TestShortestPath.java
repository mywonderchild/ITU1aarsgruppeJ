package Tests;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.List;

import Map.Model.ShortestPath;
import Map.Vector;
import Map.Model.Graph;
import Map.Model.Node;
import Map.Model.Edge;
import Map.Controller.Path;

public class TestShortestPath {
	Graph g;
	double delta = 1e-3;

	@Before
	public void setup() {
		// The graph used as test setup is depicted
		// and can be found in the test folder.

		Vector vec = new Vector(0,0); // not relevant

		Node[] nodes = new Node[5];
		for(int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(vec, i);
		}

		Edge[] edges = new Edge[] {
			new Edge(nodes[0], nodes[2], 6, "", -1),
			new Edge(nodes[0], nodes[3], 3, "", -1),
			new Edge(nodes[1], nodes[0], 3, "", -1),
			new Edge(nodes[2], nodes[3], 2, "", -1),
			new Edge(nodes[3], nodes[1], 1, "", -1),
			new Edge(nodes[3], nodes[2], 1, "", -1),
			new Edge(nodes[4], nodes[1], 4, "", -1),
			new Edge(nodes[4], nodes[3], 2, "", -1),
		};

		g = new Graph(nodes.length);
		g.addEdges(edges);
	}

	@Test
	public void pathToFromZero() {
		ShortestPath sp = new ShortestPath(g, 0);

		Integer[][] truePaths = {
			new Integer[] { }, // already there - no edges
			new Integer[] { 3, 1 },
			new Integer[] { 3, 2 },
			new Integer[] { 3 },
			null // not accessible
		};

		comparePath(sp, truePaths);
	}

	@Test
	public void pathToFromOne() {
		ShortestPath sp = new ShortestPath(g, 1);

		Integer[][] truePaths = {
			new Integer[] { 0 }, 
			new Integer[] { }, // already there - no edges
			new Integer[] { 0, 3, 2 },
			new Integer[] { 0, 3 },
			null // not accessible
		};

		comparePath(sp, truePaths);
	}

	@Test
	public void pathToFromTwo() {
		ShortestPath sp = new ShortestPath(g, 2);

		Integer[][] truePaths = {
			new Integer[] { 3, 1, 0 }, 
			new Integer[] { 3, 1 },
			new Integer[] { }, // already there - no edges
			new Integer[] { 3 },
			null // not accessible
		};

		comparePath(sp, truePaths);
	}

	@Test
	public void pathToFromThree() {
		ShortestPath sp = new ShortestPath(g, 3);

		Integer[][] truePaths = {
			new Integer[] { 1, 0 }, 
			new Integer[] { 1 },
			new Integer[] { 2 },
			new Integer[] { }, // already there - no edges
			null // not accessible
		};

		comparePath(sp, truePaths);
	}

	@Test
	public void pathToFromFour() {
		ShortestPath sp = new ShortestPath(g, 4);

		Integer[][] truePaths = {
			new Integer[] { 3, 1, 0 }, 
			new Integer[] { 3, 1 },
			new Integer[] { 3, 2 },
			new Integer[] { 3 },
			new Integer[] { } // already there - no edges
		};

		comparePath(sp, truePaths);
	}

	private void comparePath(ShortestPath sp, Integer[][] truePaths) {
		for(int i = 0; i < truePaths.length; i++) {
			Path path = sp.pathTo(i);
			if (truePaths[i] == null) {
				assertNull(path);
			} else {
				assertEquals(truePaths[i].length, path.edges.size());
				for(int j = 0; j < truePaths[i].length; j++) {
					assertEquals(truePaths[i][j], path.edges.get(j).END.ID, delta);
				}
			}
		}
	}
}