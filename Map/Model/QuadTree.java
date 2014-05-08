package Map.Model;

import java.util.ArrayList;
import java.lang.RuntimeException;

import Map.Box;
import Map.Vector;

public class QuadTree
{
	private static final int NODE_CAPACITY = 500;
    public static final int[] ALL;
    static {
        ALL = new int[Groups.GROUPS.length];
        for(int i = 0; i < ALL.length; i++) ALL[i] = i;
    }

	private Box box;
	private QuadTree[][] children;
	private Edge[][] edges;
	private int[] n;

	private double maxLen = 0;

	public QuadTree(Box box) {
		this.box = box;

        int groups = Groups.GROUPS.length;
        children = new QuadTree[groups][];
		edges = new Edge[groups][NODE_CAPACITY];
        n = new int[groups];
	}

	public boolean insert(Edge edge) {
        int g = Groups.getGroup(edge.TYPE);

		if (!edge.getCenter().isInside(box))
			return false; // Element does not belong here!

		if(n[g] < NODE_CAPACITY) {
			edges[g][n[g]++] = edge;

			// Update max edge length
			double edgeLen = edge.getVectors()[0].dist(edge.getVectors()[1]);
			if(edgeLen > maxLen) maxLen = edgeLen;

			return true;
		}

		if (children[g] == null) subdivide(g); // Subdivide if not already.
		for (QuadTree child : children[g]) // Try to insert edge in children
			if (child.insert(edge)) return true;

		// Must never happen:
		throw new RuntimeException(this.toString() + " has reached its maximum capacity, "
			+ "but failed to insert the edge into any of its children.");
	}

	public void subdivide(int group) {

		Vector center = box.getCenter();
		children[group] = new QuadTree[]{
			new QuadTree(new Box(box.start, center)), // North-west
			new QuadTree(new Box(new Vector(center.x, box.start.y), new Vector(box.stop.x, center.y))), // North-east
			new QuadTree(new Box(new Vector(box.start.x, center.y), new Vector(center.x, box.stop.y))), // South-west
			new QuadTree(new Box(center, box.stop)) // South-east
		};

        for(int i = 0; i < n[group]; i++) {
            for(QuadTree child : children[group]) {
                if(child.insert(edges[group][i])) break;
            }
        }
		edges[group] = null;
	}

	public ArrayList<Edge> queryRange(Box query, int[] groups) {
		ArrayList<Edge> result = new ArrayList<>();
		queryRange(query.copy().grow(maxLen), groups, result);
		return result;
	}

	public void queryRange(Box query, int[] groups, ArrayList<Edge> result) {

		if(!box.overlapping(query)) return;

        for(int g = 0; g < groups.length; g++) {
            if (children[g] == null) {
                for (int i = 0; i < n[g]; i++) {
    				if (edges[g][i].getCenter().isInside(query))
    					result.add(edges[g][i]);
                }
            }
            else {
                for (QuadTree child : children[g])
                    child.queryRange(query, groups, result);
            }
        }
		
	}

    public Edge findClosestEdge(Vector point, boolean withName) {
        double size = 10;

        Box query = new Box(
        	new Vector(point.x - size, point.y - size),
        	new Vector(point.x + size, point.y + size)
        );

        ArrayList<Edge> edges = null;
        // Find some edge(s):
        while(edges == null) {
        	edges = queryRange(query, ALL);
        	
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
        edges = queryRange(query, ALL);

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

        ArrayList<Edge> edges = null;
        // Find some edge(s):
        while(edges == null) {
        	edges = queryRange(query, ALL);
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
        edges = queryRange(query, ALL);

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