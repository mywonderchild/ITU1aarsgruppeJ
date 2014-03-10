package Map.Model;

public class Node
{
	private final NodeData node;

	public Node(NodeData node)
	{
		this.node = node;
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