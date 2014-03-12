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

	public ArrayList<Line> getLines()
	{
		double qtWidth = qt.getBounds()[1][0] - qt.getBounds()[0][0];
		double scale = canvas.getSize().width / qtWidth;

		long start = System.currentTimeMillis(); // Timer start
		ArrayList<Edge> edges = qt.queryRange(qt.getBounds());
		long stop = System.currentTimeMillis(); // Timer stop
		System.out.printf(
			"Query took %d ms\n",
			(stop - start)
		);
		
		ArrayList<Line> lines = new ArrayList<>();
		for(int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			if (isVisible(edge)) {
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

				int group = getGroup(edge);
				Color color = getGroupColor(group);
				lines.add(new Line(scaled, color, 0));
			}
		}
		return lines;
	}

	private boolean isVisible(Edge edge) {
		int group = getGroup(edge);

		if (mainScale <= 1) {
			if (group != 0 && group != 1)
				return false;
		}

		return true;
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