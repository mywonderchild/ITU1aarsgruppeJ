package Map.View;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import Map.Vector;
import Map.Box;
import Map.Line;

public class Painter {

	public static void paintLines(Graphics2D g, ArrayList<Line> lines) {
		if(lines == null) return;
		for(Line line : lines) {
			g.setColor(line.color);
			g.setStroke(new BasicStroke(line.width));
			g.drawLine(
				(int) line.start.x, (int) line.start.y,
				(int) line.stop.x, (int) line.stop.y
			);
		}
	}

	public static void paintBox(Graphics2D g, Box box) {

		Vector dimensions = box.dimensions();

		// Draw opaque edges
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		g.drawRect(
			(int)box.start.x, (int)box.start.y,
			(int)dimensions.x, (int)dimensions.y
		);

		// Draw semi-transparent fill
		g.setColor(new Color(0f, 0f, 0f, 0.15f));
		g.fillRect(
			(int)box.start.x, (int)box.start.y,
			(int)dimensions.x, (int)dimensions.y
		);
	}
}