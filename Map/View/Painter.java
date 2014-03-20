package Map.View;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;

import Map.Vector;
import Map.Box;
import Map.Controller.Line;

public class Painter {

	private Graphics g;

	public void setGraphics(Graphics g) {
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

		g.setColor(Color.BLACK);
		g.drawRect(
			(int)box.start.x, (int)box.start.y,
			(int)dimensions.x, (int)dimensions.y
		);

		g.setColor(new Color(0f, 0f, 0f, 0.15f)); // Semi-transparent
		g.fillRect(
			(int)box.start.x, (int)box.start.y,
			(int)dimensions.x, (int)dimensions.y
		);
	}
}