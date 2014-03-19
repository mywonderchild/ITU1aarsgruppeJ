package Map.View;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import Map.Controller.Line;
import Map.Box;

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
		// Draw lines in the following sequence: North, east, south, west
		g.drawLine((int)box.start.x, (int)box.start.y, (int)box.stop.x, (int)box.start.y);
		g.drawLine((int)box.stop.x, (int)box.start.y, (int)box.stop.x, (int)box.stop.y);
		g.drawLine((int)box.stop.x, (int)box.stop.y, (int)box.start.x, (int)box.stop.y);
		g.drawLine((int)box.start.x, (int)box.stop.y, (int)box.start.x, (int)box.start.y);
	}
}