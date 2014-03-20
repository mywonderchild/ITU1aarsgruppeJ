package Map.View;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import Map.Box;
import Map.Vector;
import Map.Controller.Line;

public class Canvas extends JPanel {
	
	private Painter painter = new Painter();
	private ArrayList<Line> lines;
	private Timer timer;

	public Box selectionBox = null;

	public boolean beauty = true;

	@Override
	public void paintComponent(Graphics g) {
		if(timer != null)
			timer.cancel();

		g.clearRect(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D)g;

		if (beauty) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			beauty = false;
		}
		else {
			timer = new Timer();
			timer.schedule(new BeautyTask(), 500);
		}

		painter.setGraphics(g2d);
		painter.paintLines(this.lines);
		if (selectionBox != null) painter.paintBox(selectionBox);
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public Box getBox() {
		Vector start = new Vector(0, 0);
		Vector stop = new Vector(getWidth(), getHeight());
		return new Box(start, stop);
	}

	private class BeautyTask extends TimerTask {

		public void run() {
			beauty = true;
			repaint();
		}
	}
}