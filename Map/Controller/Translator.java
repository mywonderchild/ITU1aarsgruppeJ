package Map.Controller;

import java.util.ArrayList;
import java.awt.Color;

import Map.View.Canvas;
import Map.Model.QuadTree;
import Map.Model.Edge;

public class Translator
{
	private Canvas canvas;
	private QuadTree qt;
	private float mainScale = 1;

	public Translator(Canvas canvas, QuadTree qt)
	{
		this.canvas = canvas;
		this.qt = qt;
	}

	public Line[] getLines()
	{
		double qtWidth = qt.getBounds()[1][0] - qt.getBounds()[0][0];
		double scale = canvas.getSize().width / qtWidth;

		ArrayList<Edge> edges = qt.queryRange(qt.getBounds());
		Line[] lines = new Line[edges.size()];
		for(int i = 0; i < lines.length; i++)
		{
			Edge edge = edges.get(i);
			double[][] coords = edge.getCoords();
			double[][] scaled = new double[2][2];

			for (int j = 0; j < 2; j++)
			{
				for (int k = 0; k < 2; k++)
				{
					scaled[j][k] = (coords[j][k] - qt.getBounds()[0][k]) * scale * mainScale;
					if (k == 1) scaled[j][k] = canvas.getSize().height - scaled[j][k];
				}
			}

			int group = getGroup(edge);
			Color color = getGroupColor(group);
			lines[i] = new Line(scaled, color, 0);
		}
		return lines;
	}

	private int getGroup(Edge edge) {

		int type = edge.getType();

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

	private Color getGroupColor(int group) {

		// Return color
		switch(group) {
			case 0:
				return Color.RED;
			case 1:
				return Color.BLUE;
			case 2:
				return Color.GREEN;
			case 3:
				return Color.CYAN;
			default:
				return Color.BLACK;
		}
	}
}