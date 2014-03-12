package Map.Model;

import java.lang.RuntimeException;

public class Edge
{
	private final static int[][] GROUPS;
	private final EdgeData edge;
	private final Node[] nodes;

	static {
		// Groups
		// We view "Motortrafik" and "Sekundærrute" as main roads
		// We view "Markvej" as a path
		// Road type 0 and 95 are undocumented but present – we added them to other
		GROUPS = new int[6][];
		GROUPS[0] = new int[]{1, 21, 31, 41}; // Highways
		GROUPS[1] = new int[]{2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44}; // Main roads
		GROUPS[2] = new int[]{8, 10, 28, 48}; // Paths
		GROUPS[3] = new int[]{11}; // Pedestrian
		GROUPS[4] = new int[]{80}; // Naval
		GROUPS[5] = new int[]{0, 5, 6, 25, 26, 35, 45, 46, 95, 99}; // Other
	}

	public Edge(EdgeData edge, Node[] nodes)
	{
		this.edge = edge;
		this.nodes = nodes;
	}

	public String toString()
	{
		return edge.toString();
	}

	public double[][] getCoords()
	{
		return new double[][] {nodes[0].getLoc(), nodes[1].getLoc()};
	}

	public double[] getCenter()
	{
		return new double[] {
			nodes[0].getLoc()[0] + (nodes[1].getLoc()[0] - nodes[0].getLoc()[0])/2,
			nodes[0].getLoc()[1] + (nodes[1].getLoc()[1] - nodes[0].getLoc()[1])/2
		};
	}

    public double distanceToPoint(double[] point)
    {
        double[] center = getCenter();
        double x = Math.abs(center[0] - point[0]);
        double y = Math.abs(center[1] - point[1]);
        return Math.sqrt(x*x + y*y);
    }

	public int getType()
	{
		return edge.TYP;
	}

	public int getGroup() throws RuntimeException {

		int type = this.getType();

		// Determine road group
		for (int i = 0; i < GROUPS.length; i++) {
			for (int id : GROUPS[i]) {
				if (type == id) {
					return i;
				}
			}
		}

		throw new RuntimeException("Road group not found, type is: " + type);
	}

	public int getGroupLength()
	{
		return GROUPS.length;
	}
}