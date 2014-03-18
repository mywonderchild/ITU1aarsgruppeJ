package Map.View;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.Graphics2D;

import Map.Controller.Line;

public class Painter {
	public void paintLines(Graphics g, ArrayList<Line> lines)
	{
		if(lines == null) return;
		for(Line line : lines)
		{
			g.setColor(line.color);
			g.drawLine(
				(int) line.start.x, (int) line.start.y,
				(int) line.stop.x, (int) line.stop.y
			);
		}
	}
}