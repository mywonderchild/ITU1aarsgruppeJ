package Map.Model;

public class Edge
{
	private final EdgeData edge;
	private final Node[] nodes;

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

	public int getGroup() {

		int type = this.getType();

		// Groups
		// We view "Motortrafik" and "Sekundærrute" as main roads
		int[][] groups = new int[4][];
		groups[0] = new int[]{1, 21, 31, 41}; // Highways
		groups[1] = new int[]{2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44}; // Main roads
		groups[2] = new int[]{8, 28, 48}; // Paths
		groups[3] = new int[]{11}; // Pedestrian

		// Determine road group
		for (int i = 0; i < groups.length; i++) {
			for (int id : groups[i]) {
				if (type == id) {
					return i;
				}
			}
		}
		return -1;
	}
}