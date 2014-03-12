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

	public Translator(Canvas canvas, QuadTree qt)
	{
		this.canvas = canvas;
		this.qt = qt;
	}

	public void setLines(double[][] bounds)
	{
		double qtWidth = qt.getBounds()[1][0] - qt.getBounds()[0][0];
		double scale = canvas.getSize().width / qtWidth;

		ArrayList<Edge> edges = qt.queryRange(bounds);
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
					scaled[j][k] = (coords[j][k] - qt.getBounds()[0][k]) * scale;
					if (k == 1) scaled[j][k] = canvas.getSize().height - scaled[j][k];
				}
			}

			Color color = getColor(edge.getType());
			lines[i] = new Line(scaled, color, 0);
		}
		canvas.setLines(lines);
	}

	private Color getColor(int type) {

		// Types
		// We view "Motortrafik" and "Sekundærrute" as main roads
		int[][] types = new int[4][];
		types[0] = new int[]{1, 21, 31, 41}; // Highways
		types[1] = new int[]{2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44}; // Main roads
		types[2] = new int[]{8, 28, 48}; // Paths
		types[3] = new int[]{11}; // Pedestrian

		// Determine road group
		int group = -1;
		for (int i = 0; i < types.length; i++) {
			for (int id : types[i]) {
				if (type == id) {
					group = i;
					break;
				}
			}
			if (group != -1) break; 
		}

		// Set correct color
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