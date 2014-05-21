package Map.View;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import Map.Box;
import Map.Vector;
import Map.Line;
import Map.Controller.Tiler;

public class Canvas extends JPanel {
	
	public Tiler tiler;
	public Box selectionBox = null;

	@Override
	public void paintComponent(Graphics graphics) {
		if (tiler != null) {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			tiler.render((Graphics2D)graphics);
			if (selectionBox != null) Painter.paintBox((Graphics2D)graphics, selectionBox);
		}
	}

	public Box getBox() {
		Vector start = new Vector(0, 0);
		Vector stop = new Vector(getWidth(), getHeight());
		return new Box(start, stop);
	}
}