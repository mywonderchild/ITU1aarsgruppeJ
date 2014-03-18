package Map.View;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Point;

import Map.Controller.Line;
import Map.Controller.Translator;

import Map.Box;
import Map.Vector;

public class Canvas extends JPanel {
	
	private Painter painter = new Painter();
	private ArrayList<Line> lines;
	private Translator translator;

	public boolean beauty = true;

	public Canvas() {
		super();

		this.setPreferredSize(new Dimension(800, 600));

		MouseMoveListener mml = new MouseMoveListener(this);
		this.addMouseMotionListener(mml);
		this.addMouseListener(mml);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D)g;

		if (beauty) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			beauty = false;
		}

		painter.paintLines(g2d, this.lines);
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public void setTranslator(Translator translator) {
		this.translator = translator;
	}

	public Box getBox() {
		Vector start = new Vector(0, 0);
		Vector stop = new Vector(getWidth(), getHeight());
		return new Box(start, stop);
	}

	private class MouseMoveListener implements MouseListener, MouseMotionListener {
		private Canvas canvas;
		private Point clickPoint = null;

		public MouseMoveListener(Canvas canvas) {
			this.canvas = canvas;
		}

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			/*System.out.println("Clicked");
			clickPoint = e.getPoint();*/
		}

		public void mouseReleased(MouseEvent e) {
			/*System.out.println("Released");
			translator.center = new double[] {
				(clickPoint.getX() + (e.getX() - clickPoint.getX())/2.0)/canvas.getWidth(),
				(clickPoint.getY() + (e.getY() - clickPoint.getY())/2.0)/canvas.getHeight()
			};

			if(translator.center[0] > translator.center[1]) {
				translator.zoomScale = (e.getX() - clickPoint.getX()) / canvas.getWidth();
			}
			else {
				translator.zoomScale = (e.getY() - clickPoint.getY()) / canvas.getHeight();
			}
			System.out.println("zoomScale: " + translator.zoomScale);
			System.out.println("center: " + translator.center[0] + ", " + translator.center[1]);

			canvas.repaint();*/
		}


		public void mouseDragged(MouseEvent e) {

		}

		public void mouseMoved(MouseEvent e) {
			// INSERT CODE FOR LOCATING NEAREST ROAD HERE!
		}
	}
}