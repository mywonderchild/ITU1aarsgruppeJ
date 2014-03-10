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
			Color color;
			switch(edge.getType())
			{
				case 1:
					color = Color.RED;
					break;
				default:
					color = Color.BLACK;
			}
			lines[i] = new Line(scaled, color, 0);
		}
		canvas.setLines(lines);
	}
}