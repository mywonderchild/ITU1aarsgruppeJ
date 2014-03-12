package Map.View;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

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
		this.addMouseMotionListener(new MouseMoveListener());
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

	private class MouseMoveListener implements MouseMotionListener {
			public void mouseDragged(MouseEvent e) {
				// Unimplemented
			}

			public void mouseMoved(MouseEvent e) {
				// INSERT CODE FOR LOCATING NEAREST ROAD HERE!
			}
	}
}