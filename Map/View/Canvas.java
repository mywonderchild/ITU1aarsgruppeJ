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

public class Canvas extends JPanel {
	
	private Painter painter = new Painter();
	private Timer timer;
	private ActionListener resizeListener;
	private boolean beauty;
	private ArrayList<Line> lines;
	private Translator translator;

	public Canvas() {
		super();

		this.setPreferredSize(new Dimension(800, 600));

		resizeListener = new ResizeListener(this);
		this.addMouseMotionListener(new MouseMoveListener(this));
	}

	@Override
	public void paintComponent(Graphics g) {

		long start = System.currentTimeMillis(); // Timer start
		lines = translator.getLines();
		long stop = System.currentTimeMillis(); // Timer stop
		System.out.printf(
			"Getting lines took %d ms\n",
			(stop - start)
		);

		if (timer == null) {
			timer = new Timer(500, resizeListener);
    		timer.setRepeats(false);
    		timer.start();
		}

		if(timer.isRunning()) {
			timer.restart();
		} else if (beauty == false) {
			timer.start();
		}

		Graphics2D g2d = (Graphics2D)g;

		if (beauty) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			beauty = false;
		}

		painter.paintLines(g2d, this.lines);
	}

	public void setTranslator(Translator translator) {
		this.translator = translator;
	}

	private class ResizeListener implements ActionListener {

		Canvas canvas;

		public ResizeListener(Canvas canvas) {
			this.canvas = canvas;
		}

		public void actionPerformed(ActionEvent e) {
			canvas.beauty = true;
			canvas.repaint();
		}
	}

	private class MouseMoveListener implements MouseListener, MouseMotionListener {
		private Canvas canvas;
		private Point clickPoint = null;

		public MouseMoveListener(Canvas canvas) {
			this.canvas = canvas;
		}

		public void mouseClicked(MouseEvent e) {
			clickPoint = e.getPoint();
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}


		public void mouseDragged(MouseEvent e) {
			translator.center = new double[] {
				e.getX() - clickPoint.getX(),
				e.getY() - clickPoint.getY()
			};

			if(translator.center[0] > translator.center[1])
				translator.zoomScale = (e.getX() - clickPoint.getX()) / canvas.getWidth();
			else
				translator.zoomScale = (e.getY() - clickPoint.getY()) / canvas.getHeight();

		}

		public void mouseMoved(MouseEvent e) {
			// INSERT CODE FOR LOCATING NEAREST ROAD HERE!
		}
	}
}