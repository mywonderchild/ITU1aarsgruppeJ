package Map.Model;

import java.lang.RuntimeException;

import Map.Vector;
import Map.Controller.Groups;

public class Edge {
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

	public Vector[] getVectors() {
		return new Vector[]{nodes[0].vector.copy(), nodes[1].vector.copy()};
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
		for (int i = 0; i < Groups.GROUPS.length; i++) {
			for (int id : Groups.GROUPS[i]) {
				if (type == id) {
					return i;
				}
			}
		}

		throw new RuntimeException("Road group not found, type is: " + type);
	}
}