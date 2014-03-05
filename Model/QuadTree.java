import java.util.ArrayList;
import java.lang.RuntimeException;
import java.lang.UnsupportedOperationException;

public class QuadTree
{
	private static final int NODE_CAPACITY = 500;
	private final Edge[] edges;
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
	}

	public ArrayList<Edge> queryRange(double[][] selection)
	{
		ArrayList<Edge> found = new ArrayList<Edge>();

		if(!isColliding(selection)) return found;

		for (int i = 0; i < n; i++)
			if (isInside(selection, edges[i].getCenter()))
				found.add(edges[i]);

		if (NW == null) return found;

		found.addAll(NW.queryRange(selection));
		found.addAll(NE.queryRange(selection));
		found.addAll(SW.queryRange(selection));
		found.addAll(SE.queryRange(selection));

		return found;
	}

	public double[][] getBounds()
	{
		return bounds;
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
}