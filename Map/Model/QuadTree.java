package Map.Model;

import java.util.ArrayList;
import java.lang.RuntimeException;

import Map.Box;
import Map.Vector;

public class QuadTree
{
	private static final int NODE_CAPACITY = 500;
	private Edge[] edges;
	private final double[][] bounds;

	private QuadTree NW;
	private QuadTree NE;
	private QuadTree SW;
	private QuadTree SE;

	private int n = 0;
	public static int count = 0;

	public QuadTree(double[][] bounds)
	{
		edges = new Edge[NODE_CAPACITY];
		this.bounds = bounds;
		count++;
	}

	public boolean insert(Edge element)
	{
		if(!isInside(bounds, element.getCenter()))
			return false; // Element does not belong here!

		if(n < NODE_CAPACITY)
		{
			edges[n++] = element;
			return true;
		}

		if(NW == null) subdivide(); // Subdivide if not already.
		if(NW.insert(element)) return true;
		if(NE.insert(element)) return true;
		if(SW.insert(element)) return true;
		if(SE.insert(element)) return true;

		// Must never happen:
		throw new RuntimeException(this.toString() + " has reached its maximum capacity, "
			+ "but failed to insert the element into any of its children.");
	}

	public void subdivide()
	{
		double[] half = {(bounds[1][0]-bounds[0][0])/2, (bounds[1][1]-bounds[0][1])/2};
		NW = new QuadTree(new double[][]{{bounds[0][0], bounds[0][1]}, {bounds[0][0]+half[0], bounds[0][1]+half[1]}});
		NE = new QuadTree(new double[][]{{bounds[0][0]+half[0], bounds[0][1]}, {bounds[1][0], bounds[0][1]+half[1]}});
		SW = new QuadTree(new double[][]{{bounds[0][0], bounds[0][1]+half[1]}, {bounds[0][0]+half[0], bounds[1][1]}});
		SE = new QuadTree(new double[][]{{bounds[0][0]+half[0], bounds[0][1]+half[1]}, {bounds[1][0], bounds[1][1]}});
		for(Edge edge : edges) {
			NW.insert(edge);
			NE.insert(edge);
			SW.insert(edge);
			SE.insert(edge);
		}
		edges = null;
	}

	public ArrayList<Edge> queryRange(double[][] selection)
	{
		ArrayList<Edge> found = new ArrayList<Edge>();

		if(!isColliding(selection)) return found;

		if(isLeaf()) {
			for (int i = 0; i < n; i++)
				if (isInside(selection, edges[i].getCenter()))
					found.add(edges[i]);
		}
		else {
			found.addAll(NW.queryRange(selection));
			found.addAll(NE.queryRange(selection));
			found.addAll(SW.queryRange(selection));
			found.addAll(SE.queryRange(selection));
		}

		return found;
	}

    public Edge search(double[] point)
    {
        Edge edge = null;
        double size = 10;
        double[][] range = new double[][] {
                {point[0] - size, point[1] - size},
                {point[0] + size, point[1] + size},
        };
        while(edge == null)
        {
            ArrayList<Edge> edges = queryRange(range);
            if(edges.isEmpty())
            {
                // double size
                for(int i = 0; i < 2; i++)
                    for(int j = 0; j < 2; j++)
                        range[i][j] = range[i][j] * 2;
            }
            else
            {
                // find closest in arraylist
                edge = edges.get(0);
                for(int i = 1; i < edges.size(); i++)
                {
                    if(edges.get(i).distanceToPoint(point) < edge.distanceToPoint(point))
                        edge = edges.get(i);
                }
            }
        }
        return edge;
    }

	public double[][] getBounds()
	{
		return bounds;
	}

	public Box getBox() {
		Vector start = new Vector(bounds[0][0], bounds[0][1]);
		Vector stop = new Vector(bounds[1][0], bounds[1][1]);
		return new Box(start, stop);
	}

	private boolean isInside(double[][] selection, double[] coords)
	{
		if(coords[0] >= selection[0][0] && coords[0] < selection[1][0])
		{
			if(coords[1] >= selection[0][1] && coords[1] < selection[1][1])
				return true;
		}
		return false;
	}

	private boolean isColliding(double[][] selection)
	{
		if (selection[0][0] > bounds[1][0]) return false;
		if (selection[1][0] < bounds[0][0]) return false;
		if (selection[0][1] > bounds[1][1]) return false;
		if (selection[1][1] < bounds[0][1]) return false;
		return true;
	}

	private boolean isLeaf()
	{
		return NW == null;
	}
}