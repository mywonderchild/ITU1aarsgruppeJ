package Map.Model;

import java.util.ArrayList;
import java.lang.RuntimeException;

import Map.Box;
import Map.Vector;

public class QuadTree
{
	private static final int NODE_CAPACITY = 500;
	public static int count = 0;

	private Edge[] edges;
	private Box box;

	private QuadTree[] children;

	private int n = 0;

	public QuadTree(Box box) {
		this.box = box;
		edges = new Edge[NODE_CAPACITY];
		count++;
	}

	public boolean insert(Edge edge) {

		if (!edge.getCenter().isInside(box))
			return false; // Element does not belong here!

		if(n < NODE_CAPACITY) {
			edges[n++] = edge;
			return true;
		}

		if (children == null) subdivide(); // Subdivide if not already.
		for (QuadTree child : children) // Try to insert edge in children
			if (child.insert(edge)) return true;

		// Must never happen:
		throw new RuntimeException(this.toString() + " has reached its maximum capacity, "
			+ "but failed to insert the edge into any of its children.");
	}

	public void subdivide() {

		Vector center = box.getCenter();
		children = new QuadTree[]{
			new QuadTree(new Box(box.start, center)), // North-west
			new QuadTree(new Box(new Vector(center.x, box.start.y), new Vector(box.stop.x, center.y))), // North-east
			new QuadTree(new Box(new Vector(box.start.x, center.y), new Vector(center.x, box.stop.y))), // South-west
			new QuadTree(new Box(center, box.stop)) // South-east
		};

		for(Edge edge : edges)
			for (QuadTree child : children)
				if (child.insert(edge))
					break;
		edges = null;
	}

	public ArrayList<Edge> queryRange(Box query) {

		ArrayList<Edge> result = new ArrayList<Edge>();

		if(!box.overlapping(query)) return result;

		if (children == null) {
			for (int i = 0; i < n; i++)
				if (edges[i].getCenter().isInside(query))
					result.add(edges[i]);
		} else {
			for (QuadTree child : children)
				result.addAll(child.queryRange(query));
		}

		return result;
	}

    // public Edge search(double[] point)
    // {
    //     Edge edge = null;
    //     double size = 10;
    //     double[][] range = new double[][] {
    //             {point[0] - size, point[1] - size},
    //             {point[0] + size, point[1] + size},
    //     };
    //     while(edge == null)
    //     {
    //         ArrayList<Edge> edges = queryRange(range);
    //         if(edges.isEmpty())
    //         {
    //             // double size
    //             for(int i = 0; i < 2; i++)
    //                 for(int j = 0; j < 2; j++)
    //                     range[i][j] = range[i][j] * 2;
    //         }
    //         else
    //         {
    //             // find closest in arraylist
    //             edge = edges.get(0);
    //             for(int i = 1; i < edges.size(); i++)
    //             {
    //                 if(edges.get(i).distanceToPoint(point) < edge.distanceToPoint(point))
    //                     edge = edges.get(i);
    //             }
    //         }
    //     }
    //     return edge;
    // }

	public Box getBox() {
		return box;
	}
}