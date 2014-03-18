package Map.Model;

import Map.Vector;

public class Node
{
	private final NodeData node;
	public final Vector vector;

	public Node(NodeData node)
	{
		this.node = node;
		vector = new Vector(node.X_COORD, node.Y_COORD);
	}

	public String toString()
	{
		return node.toString();
	}

	public int getID()
	{
		return node.KDV_ID;
	}

	public double[] getLoc()
	{
		return new double[] { node.X_COORD, node.Y_COORD };
	}
}