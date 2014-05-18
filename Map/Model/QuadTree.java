package Map.Model;

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

    // DEBUG
    static private int carecount = 0;
    static private int careless = 0;
    static private int carefull = 0;

	public QuadTree(Box box) {
		this.box = box;
		edges = new Edge[NODE_CAPACITY];
	}

	public boolean insert(Edge edge) {
		if(!box.overlapping(edge.START.VECTOR, edge.END.VECTOR))
			return false; // Element does not belong here!

		if(n < NODE_CAPACITY) {
			edges[n++] = edge;
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
	}

	public Collection<Edge> queryRange(Box query) {
        HashSet<Edge> result = new HashSet<Edge>();
		queryRange(query, result, false);
		return result;
	}

    private void carebugger(boolean lazy) {
        if(lazy) careless++;
        else carefull++;
        carecount++;

        if(carecount%10000==0) {
            System.out.printf("Bypassing query-check %.2f%% of the time\n",
                ((double)careless)/(double)(careless+carefull)*100.0);
            careless = 0;
            carefull = 0;
        }
    }

	private void queryRange(Box query, HashSet<Edge> result, boolean lazy) {
        if(!lazy) lazy = box.isInside(query); // lazy if quad is completely inside query

        carebugger(lazy); // DEBUG

        if(lazy) {
            for (int i = 0; i < n; i++)
                result.add(edges[i]);
        }
        else {
    		if(!box.overlapping(query)) return; // not touching quad at all
            for (int i = 0; i < n; i++) {
                if(result.contains(edges[i])) continue; // no reason to do expensive overlap method if already in set
                if (query.overlapping(edges[i].START.VECTOR, edges[i].END.VECTOR))
                    result.add(edges[i]);
            }
        }
		
        if(children == null) return;
        for (QuadTree child : children) {
                child.queryRange(query, result, lazy);
        }
	}

    public Edge findClosestEdge(Vector point, boolean withName) {
        double size = 1;

        Box query = new Box(
        	new Vector(point.x - size, point.y - size),
        	new Vector(point.x + size, point.y + size)
        );

        // Find some edge(s):
        Collection<Edge> edges = queryRange(query);
        boolean nameFound = false;
        while(edges.size() == 0 || nameFound == false) {
            query.scale(2); // double query size
        	edges = queryRange(query);
        	
        	if(withName) { // check if any name is present
        		for(Edge edge : edges) {
	       			if(edge.NAME != null && edge.NAME.length() > 0) {
	       				nameFound = true;
	       				break;
	       			}
	       		}
        	}
        }

        Edge closest = null;
        double closestDist = Double.POSITIVE_INFINITY;

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
        double size = 1;

        Box query = new Box(
        	new Vector(point.x - size, point.y - size),
        	new Vector(point.x + size, point.y + size)
        );

        // Find some edge(s):
        Collection<Edge> edges = queryRange(query);
        while(edges.size() == 0) {
            query.scale(2); // double query size
        	edges = queryRange(query);
        }

        // Find longest edge in query
        double longest = Double.NEGATIVE_INFINITY;
        Edge longestEdge = null;
        for(Edge edge : edges) {
            if(edge.LENGTH > longest) {
                longest = edge.LENGTH;
                longestEdge = edge;
            }
        }

        // Edge.LENGTH is not the actual length, but the scale
        // is the same among all edges. Find the true length:
        longest = longestEdge.START.VECTOR.dist(longestEdge.END.VECTOR);

        // To make sure that the closest NODE, and not just the
        // closest edge was found, we must add ½ the length of
        // the longest edge to our query.
        edges = queryRange(query.grow(longest/2));

        // Now that we are sure, that the edge housing the closest
        // node is in our collection, find it.
        Node closest = null;
        double closestDist = Double.POSITIVE_INFINITY;

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