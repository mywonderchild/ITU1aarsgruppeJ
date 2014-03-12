package Map.Controller;

import java.util.ArrayList;
import java.awt.Color;

import Map.View.Canvas;
import Map.Model.QuadTree;
import Map.Model.Edge;

public class Translator
{
	private Canvas canvas;
	private QuadTree all;
	private QuadTree[] groups;
	private float mainScale = 1;

	public Translator(Canvas canvas, QuadTree all, QuadTree[] groups) {
		this.canvas = canvas;
		this.all = all;
		this.groups = groups;
	}

	public ArrayList<Line> getLines()
	{
		double qtWidth = all.getBounds()[1][0] - all.getBounds()[0][0];
		double scale = canvas.getSize().width / qtWidth;

		long start = System.currentTimeMillis(); // Timer start
		ArrayList<Edge> edges;
		for (QuadTree group : visibleGroups())
			edges.addAll(group.queryRange(qt.getBounds()));
		long stop = System.currentTimeMillis(); // Timer stop
		System.out.printf("Query took %d ms\n", stop - start);

		ArrayList<Line> lines = new ArrayList<>();
		for(int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			double[][] coords = edge.getCoords();
			double[][] scaled = new double[2][2];

			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					scaled[j][k] = (coords[j][k] - qt.getBounds()[0][k]) * scale;
					if (k == 1) scaled[j][k] = canvas.getSize().height - scaled[j][k];
				}
			}

			Color color = getGroupColor(edge.getGroup());
			lines.add(new Line(scaled, color, 0));
		}
		return lines;
	}

	private QuadTree[] visibleGroups() {
		if (mainScale <= 1)
			return new QuadTree[]{groups[0], groups[1]};
		else
			return new QuadTree[]{all};
	}

	private Color getGroupColor(int group) throws RuntimeException {

		// Return color
		switch(group) {
			case 0:
				return Color.RED;
			case 1:
				return Color.BLUE;
			case 2:
				return Color.GREEN;
			case 3:
				return Color.YELLOW;
			case 4:
				return Color.CYAN;
			case 5:
				return Color.BLACK;
			default:
				throw new RuntimeException("Road group " + group + " is not recognized");
		}
	}
}