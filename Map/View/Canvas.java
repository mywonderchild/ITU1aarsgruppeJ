package Map.View;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.Point;

import Map.Controller.Line;
import Map.Controller.Translator;

import Map.Box;
import Map.Vector;

public class Canvas extends JPanel {
	
	private Painter painter = new Painter();
	private ArrayList<Line> lines;
	private Box selectionBox = null;

	public boolean beauty = true;

	public Canvas() {
		super();

		this.setPreferredSize(new Dimension(800, 600));
	}

	@Override
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D)g;

		if (beauty) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			beauty = false;
		}

		painter.setGraphics(g2d);
		painter.paintLines(this.lines);
		if (selectionBox != null) painter.paintBox(selectionBox);
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public void setSelectionBox(Box box) {
		selectionBox = box;
	}

	public Box getBox() {
		Vector start = new Vector(0, 0);
		Vector stop = new Vector(getWidth(), getHeight());
		return new Box(start, stop);
	}
}