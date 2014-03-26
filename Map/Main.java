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

		// Translator...
		Translator translator = new Translator(canvas, loader);

		// Event listeners
		KeyboardHandler keyboardHandler = new KeyboardHandler(canvas, translator);
		canvas.addComponentListener(new ResizeHandler(canvas, translator));
		MouseHandler mouseHandler = new MouseHandler(window, translator);
		canvas.addMouseListener(mouseHandler);
		canvas.addMouseMotionListener(mouseHandler);
		canvas.addMouseWheelListener(mouseHandler);
	}
}