package Map.Tests;

import static org.junit.Assert.*;
import org.junit.*;

import Map.Vector;
import Map.Model.Graph;
import Map.Model.Node;
import Map.Model.Edge;
import Map.Model.AStar;
import Map.Controller.Path;

public class TestAStar {
	Node[] nodes;
	Graph g;
	AStar astar;
	double delta = 1e-3;

	@Before
	public void setup() {
		nodes = new Node[] {
			new Node(new Vector(1,2), 0),
			new Node(new Vector(2,2), 1),
			new Node(new Vector(3,2), 2),
			new Node(new Vector(2,1), 3),
			new Node(new Vector(3,1), 4),
			new Node(new Vector(4,1), 5)
		};

		Edge[] edges = new Edge[] {
			new Edge(-1, nodes[0], nodes[1], -1, (byte)-1, "", -1, 2),
			new Edge(-1, nodes[1], nodes[2], -1, (byte)-1, "", -1, 1),
			new Edge(-1, nodes[1], nodes[3], -1, (byte)-1, "", -1, 5),
			new Edge(-1, nodes[2], nodes[4], -1, (byte)-1, "", -1, 1),
			new Edge(-1, nodes[4], nodes[3], -1, (byte)-1, "", -1, 1),
			new Edge(-1, nodes[4], nodes[5], -1, (byte)-1, "", -1, 2)
		};

		g = new Graph(nodes.length);
		g.addEdges(edges);

		astar = new AStar(g);
	}

	@Test
	public void pathToFromZero() {
		Integer[][] truePaths = {
			new Integer[] { }, // already there - no edges
			new Integer[] { 1 },
			new Integer[] { 1, 2 },
			new Integer[] { 1, 2, 4, 3 },
			new Integer[] { 1, 2, 4 },
			new Integer[] { 1, 2, 4, 5 }
		};

		comparePath(0, truePaths);
	}

	@Test
	public void pathToFromOne() {
		Integer[][] truePaths = {
			null, // not accessible
			new Integer[] { }, // already there - no edges
			new Integer[] { 2 },
			new Integer[] { 2, 4, 3 },
			new Integer[] { 2, 4 },
			new Integer[] { 2, 4, 5 }
		};

		comparePath(1, truePaths);
	}

	@Test
	public void pathToFromTwo() {
		Integer[][] truePaths = {
			null, // not accessible
			null, // not accessible
			new Integer[] { }, // already there - no edges
			new Integer[] { 4, 3 }, 
			new Integer[] { 4 },
			new Integer[] { 4, 5 },
		};

		comparePath(2, truePaths);
	}

	@Test
	public void pathToFromThree() {
		Integer[][] truePaths = {
			null, // not accessible
			null, // not accessible
			null, // not accessible
			new Integer[] { }, // already there - no edges
			null, // not accessible
			null // not accessible
		};

		comparePath(3, truePaths);
	}

	@Test
	public void pathToFromFour() {
		Integer[][] truePaths = {
			null, // not accessible
			null, // not accessible
			null, // not accessible
			new Integer[] { 3 },
			new Integer[] { }, // already there - no edges
			new Integer[] { 5 }
		};

		comparePath(4, truePaths);
	}

	@Test
	public void pathToFromFive() {
		Integer[][] truePaths = {
			null, // not accessible
			null, // not accessible
			null, // not accessible
			null, // not accessible
			null, // not accessible
			new Integer[] { } // already there - no edges
		};

		comparePath(5, truePaths);
	}

	private void comparePath(int from, Integer[][] 	truePaths) {
		for(int i = 0; i < truePaths.length; i++) {
			Path path = astar.findPath(nodes[from], nodes[i]);

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