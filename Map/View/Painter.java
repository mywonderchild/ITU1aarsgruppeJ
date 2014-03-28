package Map.View;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;

import Map.Vector;
import Map.Box;
import Map.Line;

public class Painter {

	private Graphics2D g;

	public void setGraphics(Graphics2D g) {
		this.g = g;
	}

	public void paintLines(ArrayList<Line> lines) {
		long timer = System.currentTimeMillis();
		if(lines == null) return;
		for(Line line : lines) {
			g.setColor(line.color);
			g.drawLine(
				(int) line.start.x, (int) line.start.y,
				(int) line.stop.x, (int) line.stop.y
			);
		}
		System.out.printf(
			"Painting lines took %d ms\n",
			System.currentTimeMillis() - timer
		);
	}

	public void paintBox(Box box) {

		Vector dimensions = box.dimensions();

		// Draw opaque edges
		g.setColor(Color.BLACK);
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