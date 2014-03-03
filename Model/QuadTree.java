import java.util.ArrayList;
import java.lang.RuntimeException;
import java.lang.UnsupportedOperationException;

public class QuadTree
{
	private static final int NODE_CAPACITY = 500;
	private final Locatable[] nodes;
	private final Boundary bounds;

	private QuadTree NW;
	private QuadTree NE;
	private QuadTree SW;
	private QuadTree SE;

	private int n = 0;

	public QuadTree(double x, double y, double size)
	{
		nodes = new Locatable[NODE_CAPACITY];
		bounds = new Boundary(x, y, size);
	}

	public boolean insert(Locatable element)
	{
		if(!bounds.contains(element))
			return false; // Element does not belong here!

		if(n < NODE_CAPACITY)
		{
			nodes[n++] = element;
			return true;
		}

		if(NW == null) subdivide(); // Subdivide if not already.
		if(NW.insert(element)) return true;
		if(NE.insert(element)) return true;
		if(SW.insert(element)) return true;
		if(SE.insert(element)) return true;

		// Must never happen:
		throw new RuntimeException(this.toString() + " has reached its maximum capacity, "
			+ "but failed to insert the element into any of its children. ("
			+ this.bounds.x + ", " + this.bounds.y + ") Size: " + this.bounds.size
			+ " n: " + this.n + "/" + NODE_CAPACITY + " Element(" + element.getX()
			+ ", " + element.getY() + ")");
	}

	public void subdivide()
	{
		double hSize = bounds.size/2;
		NW = new QuadTree(bounds.x - hSize, bounds.y - hSize, hSize);
		NE = new QuadTree(bounds.x + hSize, bounds.y - hSize, hSize);
		SW = new QuadTree(bounds.x - hSize, bounds.y + hSize, hSize);
		SE = new QuadTree(bounds.x + hSize, bounds.y + hSize, hSize);
	}

	public ArrayList<Locatable> queryRange(Boundary range)
	{
		throw new UnsupportedOperationException();
	}

	private static class Boundary
	{
		public final double x, y;
		public final double size;
		private final double x1, x2, y1, y2;

		public Boundary(double x, double y, double size)
		{
			this.x = x;
			this.y = y;
			this.size = size;

			// Find bounds.
			x1 = x - size;
			x2 = x + size;
			y1 = y - size;
			y2 = y + size;
		}

		public boolean contains(Locatable element)
		{
			if(element.getX() >= x1 && element.getX() < x2)
			{
				if(element.getY() >= y1 && element.getY() < y2)
					return true;
			}
			return false;
		}
	}
}