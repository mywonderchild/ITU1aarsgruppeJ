package Map.View;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import Map.Controller.Line;
import Map.Controller.Translator;

public class Canvas extends JPanel {
	
	private Painter painter = new Painter();
	private Timer timer;
	private ActionListener listener;
	private boolean beauty;
	private ArrayList<Line> lines;
	private Translator translator;

	public Canvas() {
		super();

		this.setPreferredSize(new Dimension(800, 600));

		listener = new Listener(this);
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
			timer = new Timer(500, listener);
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

	private class Listener implements ActionListener {

		Canvas canvas;

		public Listener(Canvas canvas) {
			this.canvas = canvas;
		}

		public void actionPerformed(ActionEvent e) {
			canvas.beauty = true;
			canvas.repaint();
		}
	}
}