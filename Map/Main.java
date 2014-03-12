package Map;

import Map.Controller.*;
import Map.View.*;
import Map.Model.*;

public class Main
{
	public static void main(String[] args) {

		// Set up model
		long start = System.currentTimeMillis(); // Timer start
		QuadTree[] qt = (new Loader()).groupedqt;
		long stop = System.currentTimeMillis(); // Timer stop
		System.out.printf(
			"Done loading data after %.2f seconds.\n",
			(stop - start) / 1000.0
		);

		// Set up view
		Canvas canvas = new Canvas();
		Window window = new Window(canvas);

		Translator translator = new Translator(canvas, qt);
		canvas.setTranslator(translator);
	}
}