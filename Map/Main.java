package Map;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import Map.Controller.*;
import Map.View.*;
import Map.Model.*;

public class Main
{
	public static void main(String[] args) {

		// Set up model
		long timer = System.currentTimeMillis();
		Loader loader = new Loader();
		System.out.printf(
			"Loading data took %.2f seconds\n",
			(System.currentTimeMillis() - timer) / 1000.0
		);

		// Set up view
		final Canvas canvas = new Canvas();
		Window window = new Window(canvas);

		// Tiler
		final Tiler tiler = new Tiler(1.3, new Vector(0.5, 0.5), canvas.getBox(), loader.all.getBox(), loader);
		canvas.tiler = tiler;
		canvas.repaint();

		// Event listeners
		KeyboardHandler keyboardHandler = new KeyboardHandler(canvas, tiler);
		canvas.addComponentListener(new ComponentListener() {
			{componentResized(null);}
			public void componentResized(ComponentEvent e) {
				tiler.viewBox = canvas.getBox();
				tiler.setZoom(tiler.zoom);
		    }
		    public void componentHidden(ComponentEvent e) {}
		    public void componentMoved(ComponentEvent e) {}
		    public void componentShown(ComponentEvent e) {}
		});
		MouseHandler mouseHandler = new MouseHandler(window, tiler);
		canvas.addMouseListener(mouseHandler);
		canvas.addMouseMotionListener(mouseHandler);
		canvas.addMouseWheelListener(mouseHandler);
	}
}