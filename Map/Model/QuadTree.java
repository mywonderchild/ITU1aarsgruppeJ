package Map.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.lang.RuntimeException;

import Map.Box;
import Map.Vector;

public class QuadTree
{
	private static final int NODE_CAPACITY = 500;

	private Box box;
	private QuadTree[] children;
	private Edge[] edges;
	private int n = 0;

	private double maxLen = 0;

	public QuadTree(Box box) {
		this.box = box;
		edges = new Edge[NODE_CAPACITY];
	}

	public boolean insert(Edge edge) {
		if(!box.overlapping(edge.START.VECTOR, edge.END.VECTOR))
			return false; // Element does not belong here!

		if(n < NODE_CAPACITY) {
			edges[n++] = edge;

			// Update max edge length
			double edgeLen = edge.getVectors()[0].dist(edge.getVectors()[1]);
			if(edgeLen > maxLen) maxLen = edgeLen;

			return true;
		}

		if (children == null) subdivide(); // Subdivide if not already.

        boolean insertFlag = false;
		for (QuadTree child : children) // Try to insert edge in children
			if (child.insert(edge)) insertFlag = true;

        // Must never happen:
        if(!insertFlag) throw new RuntimeException(this.toString() + " has reached its maximum capacity, "
            + "but failed to insert the edge into any of its children.");
		
        return true;   
	}

	public void subdivide() {
		Vector center = box.getCenter();
		children = new QuadTree[]{
			new QuadTree(new Box(box.start, center)), // North-west
			new QuadTree(new Box(new Vector(center.x, box.start.y), new Vector(box.stop.x, center.y))), // North-east
			new QuadTree(new Box(new Vector(box.start.x, center.y), new Vector(center.x, box.stop.y))), // South-west
			new QuadTree(new Box(center, box.stop)) // South-east
		};

        boolean insertFlag = false;
		for(Edge edge : edges)
			for (QuadTree child : children)
				if(child.insert(edge)) insertFlag = true;

        // Must never happen:
        if(!insertFlag) throw new RuntimeException(this.toString() + " has reached its maximum capacity, "
            + "but failed to insert the edge into any of its children.");

		edges = null;
	}

	public Collection<Edge> queryRange(Box query) {
        HashSet<Edge> result = new HashSet<Edge>();
		queryRange(query.copy().grow(0), result);
		return result;
	}

	private void queryRange(Box query, HashSet<Edge> result) {
		if(!box.overlapping(query)) return;

		if (children == null) {
			for (int i = 0; i < n; i++) {
                if(result.contains(edges[i])) continue; // no reason to do expensive overlap method then
				if (query.overlapping(edges[i].START.VECTOR, edges[i].END.VECTOR))
					result.add(edges[i]);
            }
		} else {
			for (QuadTree child : children)
				child.queryRange(query, result);
		}
	}

    public Edge findClosestEdge(Vector point, boolean withName) {
        double size = 10;

        Box query = new Box(
        	new Vector(point.x - size, point.y - size),
        	new Vector(point.x + size, point.y + size)
        );

        Collection<Edge> edges = null;
        // Find some edge(s):
        while(edges == null) {
        	edges = queryRange(query);
        	
        	if(withName) { // check if any name is present
        		boolean nameFound = false;
        		for(Edge edge : edges) {
	       			if(edge.NAME != null) {
	       				nameFound = true;
	       				break;
	       			}
	       		}
	       		if(!nameFound) edges = null; // no name, no game
        	}

        	query.scale(2); // double query size
        }

        // When any edges are found, we must ensure all
        // nearby edges get a chance. Because edges are
        // placed in the quadtree based on center location,
        // we must expand the search area by half the length
        // of the longest edge in the quadtree.

        Edge closest = null;
        double closestDist = Double.POSITIVE_INFINITY;
    	
    	query.grow(maxLen/2); // grow query with ½ longest edge
        edges = queryRange(query);

        for (Edge edge : edges) {
        	if(withName && edge.NAME == null) continue; // no name, no game

        	// calculate distance from point to line
        	Vector a = edge.START.VECTOR;
        	Vector b = edge.END.VECTOR;
        	Vector c = point.copy().sub(a); // a -> point
        	Vector unit = b.copy().sub(a).norm(); // unit vector from a -> b
        	double len = a.dist(b); // edge length
        	double dot = unit.dot(c); // intersecting point dist from a
        	double dist;

        	// As we don't want to know the point's distnace to to
        	// infinite line, but rather the finite line segment,
        	// check if the intersection point is on the segment.
        	if(dot < 0) { dist = a.dist(point); }
        	else if(dot > len) { dist = b.dist(point); }

        	// Point intersects with line somewhere in segment range.
        	// Intersectin point must then be unit*dot + a
        	else { dist = unit.mult(dot).add(a).dist(point); }

    		if (closest == null || dist < closestDist) {
    			closest = edge;
    			closestDist = dist;
    		}
    	}

        return closest;
    }

    public Node findClosestNode(Vector point) {
        double size = 10;

        Box query = new Box(
        	new Vector(point.x - size, point.y - size),
        	new Vector(point.x + size, point.y + size)
        );

        Collection<Edge> edges = null;
        // Find some edge(s):
        while(edges == null) {
        	edges = queryRange(query);
        	query.scale(2); // double query size
        }

        // When any edges are found, we must ensure all
        // nearby edges get a chance. Because edges are
        // placed in the quadtree based on center location,
        // we must expand the search area by half the length
        // of the longest edge in the quadtree.

        Node closest = null;
        double closestDist = Double.POSITIVE_INFINITY;
    	
    	query.grow(maxLen/2); // grow query with ½ longest edge
        edges = queryRange(query);

        for (Edge edge : edges) {
        	double startDist = point.dist(edge.START.VECTOR);
        	double endDist = point.dist(edge.END.VECTOR);

    		if (closest == null || startDist < closestDist) {
    			closest = edge.START;
    			closestDist = startDist;
    		}
    		if (endDist < closestDist) {
    			closest = edge.END;
    			closestDist = endDist;
    		}
    	}

        return closest;
    }

	public Box getBox() {
		return box;
	}
}