package Map.View;

import java.awt.*;
import java.util.Random;
import java.awt.Graphics2D;
import Map.Controller.Line;

public class Painter {

	public void paintCross(Graphics g, int x, int y, int size) {
		g.setColor(Color.BLACK);
		g.drawRect(x, y + size / 3, x + size, y + size / 3);
		g.drawRect(x + size / 3, y, x + size / 3, y + size);
	}

	public void paintComplex(Graphics g, int w, int h) {

		g.setColor(Color.BLACK);

		Random random = new Random();

		for (int i = 0; i < 1000; i++) {
			g.drawLine(
				random.nextInt(w),
				random.nextInt(h),
				random.nextInt(w),
				random.nextInt(h)
			);
		}
	}

	public void paintLines(Graphics g, Line[] lines)
	{
		if(lines == null) return;
		for(Line line : lines)
		{
			g.setColor(line.color);
			g.drawLine(
				(int) line.coords[0][0],
				(int) line.coords[0][1],
				(int) line.coords[1][0],
				(int) line.coords[1][1]
			);
		}
	}
}