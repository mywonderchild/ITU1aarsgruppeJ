package View;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Canvas extends JPanel {
	
	private Painter painter = new Painter();
	private Timer timer;
	private ActionListener listener;
	private boolean beauty;
	// Edge[] edges;

	public Canvas() {
		super();

		this.setPreferredSize(new Dimension(800, 600));

		listener = new Listener(this);
	}

	// public void setEdges(Edge[] edges) {
	// 	this.edges = edges;
	// }

	@Override
	public void paintComponent(Graphics g) {

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

		System.out.println(beauty);

		if (beauty) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			beauty = false;
		}

		painter.paintComplex(g2d, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());
		// painter.drawEdges(this.edges);
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