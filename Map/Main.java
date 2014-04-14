package Map;

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
		Canvas canvas = new Canvas();
		Window window = new Window(canvas);

		// Tiler
		Tiler tiler = new Tiler(1.3, new Vector(0.5, 0.5), canvas.getBox(), loader.all.getBox(), loader);
		canvas.tiler = tiler;
		canvas.repaint();

		// Event listeners
		KeyboardHandler keyboardHandler = new KeyboardHandler(canvas, tiler);
		canvas.addComponentListener(new ResizeHandler(canvas, tiler));
		MouseHandler mouseHandler = new MouseHandler(window, tiler);
		canvas.addMouseListener(mouseHandler);
		canvas.addMouseMotionListener(mouseHandler);
		canvas.addMouseWheelListener(mouseHandler);
	}
}